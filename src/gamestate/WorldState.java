package gamestate;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.Window;

import asset.Assets;
import camera.PlayerBasedCamera;
import entity.EntityManager;
import entity.player.EntityPlayer;
import graphics.fontcreator.GUIText;
import graphics.particle.ParticleMaster;
import graphics.postprocessing.Fbo;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import input.Controls;
import input.GameSettings;
import main.GameManager;
import utils.MousePicker;
import world.World;
import world.WorldLoader;

public class WorldState implements GameState {
	
	private static MousePicker picker;
	private static GUIText FPSText;
	
	public static Fbo multisampleFbo, outputFbo;
	
	public static World world;
	private static EntityPlayer player;
	private static PlayerBasedCamera camera;

	@Override
	public void render() {
		updateText();
		ParticleMaster.update();
		if (GameSettings.SHAWODS_ENABLED) MasterRenderer.renderShadowMap(world.entities, world.sun);
		world.render(camera, multisampleFbo);
		
		multisampleFbo.resolveTo(outputFbo);
		PostProcessing.doPostProcessing(outputFbo.getColorTexture());
		if(Controls.ESCAPE.hasBeenPressed()){
			GameStateManager.setState(new TitleScreenState());
		}
		ParticleMaster.render();
	}

	private void updateText() {
		FPSText.setText("FPS: " + Window.getInstance().getCurrentFPS());
		float color = world.blendFactor;
		FPSText.setColor(color, color, color);
	}

	@Override
	public void update(int updateCount) {
		camera.update();
		picker.update();
		world.update();
		
	}

	@Override
	public void onStart() {
		FPSText = new GUIText("FPS: 1", 1.2f, Assets.font, new Vector2f(0.001f, 0.001f), 1.0f, false);
		camera = (PlayerBasedCamera) GameManager.getCamera();
		multisampleFbo = new Fbo(Window.getInstance().getWidth(), Window.getInstance().getHeight(), new Integer(GameSettings.MULTISAMPLE_COUNT));
		outputFbo = new Fbo(Window.getInstance().getWidth(), Window.getInstance().getHeight(), Fbo.DEPTH_TEXTURE);
		
		picker = new MousePicker(camera);
		
		world = new World();
		MasterRenderer.setWorld(world);
		
		player = new EntityPlayer(Assets.tree, new Vector3f(10, -100, 10), new Vector3f(0, 0, 0), 0.6f, (PlayerBasedCamera)camera);
		player.skipRender = true;
		EntityManager.addEntity(player);
		camera.setPlayer(player);
		camera.update();
		
		MasterRenderer.init(world, camera);
		ParticleMaster.init(camera);
		Assets.init();
		
		PostProcessing.init();
		
		if (GameSettings.DEBUG) world.printGenStats();
		WorldLoader.update();
	}

	@Override
	public void onEnd() {
		
		world.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
	}
	
	public static EntityPlayer getPlayer() {
		return player;
	}

}
