package gamestate;

import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.Window;

import assets.Assets;
import entity.Camera;
import entity.EntityManager;
import entity.player.EntityPlayer;
import graphics.postprocessing.Fbo;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import input.Controls;
import input.GameSettings;
import particle.ParticleSystem;
import utils.MousePicker;
import world.World;
import world.WorldLoader;

public class WorldState implements GameState {
	
	private static MousePicker picker;
	
	public static Fbo multisampleFbo, outputFbo;
	
	public static World world;
	private static EntityPlayer player;
	private ParticleSystem system = new ParticleSystem(Assets.particleTexture, 100, 0.5f, 0.3f, 3);

	@Override
	public void render() {
		if (GameSettings.SHAWODS_ENABLED) MasterRenderer.renderShadowMap(world.entities, world.sun);
		world.render(multisampleFbo);
		
		multisampleFbo.resolveTo(outputFbo);
		PostProcessing.doPostProcessing(outputFbo.getColorTexture());
		if(Controls.ESCAPE.hasBeenPressed()){
			GameStateManager.setState(new TitleScreenState());
		}
		system.generateParticles(new Vector3f(50, world.getHeight(50, 50) + 2, 50));
	}

	@Override
	public void update(int updateCount) {
		Camera.getCamera().update();
		picker.update();
		world.update();
		
	}

	@Override
	public void onStart() {
		multisampleFbo = new Fbo(Window.getInstance().getWidth(), Window.getInstance().getHeight(), new Integer(GameSettings.MULTISAMPLE_COUNT));
		outputFbo = new Fbo(Window.getInstance().getWidth(), Window.getInstance().getHeight(), Fbo.DEPTH_TEXTURE);
		
		picker = new MousePicker(MasterRenderer.projectionMatrix);
		
		world = new World();
		MasterRenderer.setWorld(world);

		float playerX = 50, playerZ = 50;
		player = new EntityPlayer(Assets.tree, new Vector3f(playerX, -1000, playerZ), new Vector3f(12, 0, 0), 0.6f);
		player.skipRender = true;
		EntityManager.addEntity(player);
		Camera.getCamera().setPlayer(player);
		
		if (GameSettings.DEBUG) world.printGenStats();
		WorldLoader.update();
	}

	@Override
	public void onEnd() {
		
		world.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
	}

}
