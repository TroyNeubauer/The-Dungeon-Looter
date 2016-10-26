package main;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.math.Vector3f;
import entity.Camera;
import entity.EntityManager;
import entity.player.EntityPlayer;
import graphics.Assets;
import graphics.font.renderer.TextMaster;
import graphics.gui.GuiRenderer;
import graphics.gui.GuiTexture;
import graphics.postprocessing.Fbo;
import graphics.postprocessing.PostProcessing;
import graphics.water.WaterRenderer;
import input.GameSettings;
import input.KeyHandler;
import loader.Loader;
import particles.ParticleMaster;
import renderEngine.MasterRenderer;
import renderEngine.SplashRenderer;
import utils.MousePicker;
import world.World;
import world.WorldLoader;

public class GameManager {

	private static MasterRenderer renderer;
	private static WaterRenderer waterRenderer;
	private static MousePicker picker;
	private static GuiRenderer guiRenderer;

	public static World world;
	private static EntityPlayer player;

	private static Fbo multisampleFbo, outputFbo;
	private static List<GuiTexture> guiTextures;

	private GameManager() {
	}

	public static void update() {
		KeyHandler.update();
		world.update();
		Camera.getCamera().update();
		picker.update();
	}

	public static void render() {
		ParticleMaster.update();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		if (GameSettings.SHAWODS_ENABLED) MasterRenderer.renderShadowMap(world.entities, world.sun);

		world.render(multisampleFbo);
		multisampleFbo.resolveTo(outputFbo);
		PostProcessing.doPostProcessing(outputFbo.getColorTexture());

		ParticleMaster.render();
		guiRenderer.render(guiTextures);
		TextMaster.render();

		DisplayManager.updateDisplay(true);
		guiTextures.clear();
	}

	public static void cleanUp() {
		ParticleMaster.cleanUp();
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		TextMaster.cleanUp();
		world.cleanUp();
		guiRenderer.cleanUp();
		MasterRenderer.cleanUp();
		Loader.getLoader().cleanUp();
	}

	public static void init() {

		System.out.println("Starting " + Version.getWindowTitle() + "\n");
		Timer t = new Timer();
		DisplayManager.createDisplay(1440, 810, false);
		Assets.loadCoreAssets(Loader.getLoader());
		SplashRenderer.init();

		TextMaster.init();
		MasterRenderer.init();
		ParticleMaster.init(MasterRenderer.projectionMatrix);
		Assets.load(Loader.getLoader());

		world = new World();
		MasterRenderer.setWorld(world);

		float playerX = 50, playerZ = 50;
		player = new EntityPlayer(Assets.tree, new Vector3f(playerX, -1000, playerZ), new Vector3f(12, 0, 0), 0.6f);
		player.skipRender = true;
		EntityManager.addEntity(player);
		Camera.getCamera().setPlayer(player);

		if (GameSettings.DEBUG) System.out.println("Creating Fbos");
		guiTextures = new ArrayList<GuiTexture>();
		guiRenderer = new GuiRenderer();
		picker = new MousePicker(MasterRenderer.projectionMatrix);
		multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight(), new Integer(GameSettings.MULTISAMPLE_COUNT));
		outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init();

		System.out.println("finished game initalization took " + t.getTime() + "\n");

		if (GameSettings.DEBUG) world.printGenStats();
		Updater.init();
		WorldLoader.update();
	}

}
