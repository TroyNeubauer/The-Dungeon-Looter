package thedungeonlooter.gamestate;

import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.input.Keyboard;
import com.troy.troyberry.opengl.util.Window;

import graphics.fontcreator.GUIText;
import graphics.particle.ParticleMaster;
import graphics.particle.system.SnowFlakeSystem;
import graphics.postprocessing.Fbo;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import graphics.water.WaterFrameBuffers;
import loader.Assets;
import thedungeonlooter.camera.*;
import thedungeonlooter.camera.CameraMaster.CameraMode;
import thedungeonlooter.entity.EntityManager;
import thedungeonlooter.entity.player.EntityPlayer;
import thedungeonlooter.input.Controls;
import thedungeonlooter.input.GameSettings;
import thedungeonlooter.utils.MousePicker;
import thedungeonlooter.world.World;
import thedungeonlooter.world.WorldLoader;

public class WorldState implements GameState {
	
	private static MousePicker picker;
	private static GUIText FPSText;
	public static ICamera camera;
	
	public static Fbo multisampleFbo, outputFbo;
	
	public static World world;
	private static EntityPlayer player;
	
	@Override
	public void render() {
		camera.render();
		updateText();
		ParticleMaster.update();
		if (GameSettings.SHAWODS_ENABLED) MasterRenderer.renderShadowMap(camera, world.entities, world.sun);
		
		multisampleFbo.bindFrameBuffer();
		world.render(camera, new Vector4f(0, -1, 0, 100000));
		multisampleFbo.unbindFrameBuffer();
		multisampleFbo.resolveTo(outputFbo);
		
		PostProcessing.doPostProcessing(outputFbo.getColorTexture());
		if(Controls.ESCAPE.hasBeenPressed()){
			GameStateManager.setState(new TitleScreenState());
		}
		
		ParticleMaster.render();
	}

	private void updateText() {
		FPSText.setText("FPS: " + Window.getInstance().getCurrentFPS());
		FPSText.setColor(world.blendFactor);
	}

	@Override
	public void update(int updateCount) {
		camera.update();
		picker.update();
		world.update();
	}

	@Override
	public void onStart() {
		camera = CameraMaster.init(CameraMode.FIRST_PERSON);
		FPSText = new GUIText("FPS: 1", 1.2f, Assets.font, new Vector2f(0.001f, 0.001f), 1.0f, false);
		multisampleFbo = new Fbo(Window.getInstance().getWidth(), Window.getInstance().getHeight(), new Integer(GameSettings.MULTISAMPLE_COUNT));
		outputFbo = new Fbo(Window.getInstance().getWidth(), Window.getInstance().getHeight(), Fbo.DEPTH_TEXTURE);
		
		picker = new MousePicker(camera);

		player = new EntityPlayer(Assets.person, new Vector3f(50, -100, 50), new Vector3f(), 0.6f);
		EntityManager.addEntity(player);
		
		world = new World();
		MasterRenderer.setWorld(world);
		
		if(camera instanceof PlayerBasedCamera)((PlayerBasedCamera)camera).setPlayer(player);
		camera.update();
		
		MasterRenderer.init(world, camera);
		ParticleMaster.init(camera);
		Assets.init();
		
		PostProcessing.init();
		
		if (GameSettings.DEBUG) world.printGenStats();
		WorldLoader.update();
		ParticleMaster.addSystem(new SnowFlakeSystem(player));
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
	
	public static void setCamera(ICamera camera) {
		WorldState.camera = camera;
		if(camera instanceof PlayerBasedCamera && player != null)((PlayerBasedCamera)camera).setPlayer(player);
	}

	@Override
	public void cleanUp() {
		FPSText.delete();
	}

}
