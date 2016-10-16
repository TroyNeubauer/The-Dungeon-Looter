package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.math.Vector3f;
import entity.Camera;
import entity.Entity;
import entity.EntityPlayer;
import fontRendering.TextMaster;
import graphics.Assets;
import graphics.gui.GuiRenderer;
import graphics.gui.GuiTexture;
import graphics.water.WaterRenderer;
import input.GameSettings;
import input.KeyHandler;
import particles.ParticleMaster;
import particles.ParticleSystem;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import toolbox.MousePicker;
import world.World;

public class MainGameLoop implements Runnable {

	private static Thread thread;
	private static AtomicBoolean running = new AtomicBoolean(true);
	public static volatile int updateCount = 0;
	public static final double TICKS_PER_SECOND = 100.0, TIME_PER_TICK = 1.0 / TICKS_PER_SECOND;

	private static MasterRenderer renderer;
	private static WaterRenderer waterRenderer;

	private static Loader loader;
	private static Camera camera;
	private static MousePicker picker;
	private static ParticleSystem system;

	private static World world;
	private static EntityPlayer player;

	public static void main(String[] args) {
		System.out.println("Starting game\n");
		Timer t = new Timer();
		DisplayManager.createDisplay(1440, 810, false);
		loader = new Loader();
		Timer assetTimer = new Timer();
		Assets.load(loader);

		TextMaster.init(loader);
		camera = new Camera();
		renderer = new MasterRenderer(loader, camera);
		ParticleMaster.init(loader, renderer.getProjectionMatrix());

		System.out.println("\nFinished loading assets in " + assetTimer.getTime());

		world = new World(loader, renderer);
		renderer.setWorld(world);
		Entity.setWorld(world);
		world.populate();

		float playerX = 50, playerZ = 50;
		player = new EntityPlayer(Assets.person, new Vector3f(playerX, world.getHeight(playerX, playerZ), playerZ), new Vector3f(12, 0, 0), 0.6f);
		player.hide();
		camera.setPlayer(player);

		System.out.println("Creating Fbos");
		List<GuiTexture> guiTextures = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		picker = new MousePicker(camera, renderer.getProjectionMatrix());
		Fbo multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight(), new Integer(GameSettings.MULTISAMPLE_COUNT));
		Fbo outputFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_TEXTURE);
		PostProcessing.init(loader);

		thread = new Thread(new MainGameLoop(), "update-thread");
		thread.start();
		System.out.println("finished game initalization took " + t.getTime() + "\n");

		system = new ParticleSystem(Assets.particleTexture, 100, 1f, 1, 2);

		world.printGenStats();
		//****************Game Loop Below*********************
		while (running.get()) {
			ParticleMaster.update(camera);
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			if (GameSettings.SHAWODS_ENABLED) renderer.renderShadowMap(world.entities, world.normalMapEntities, world.sun);

			world.render(renderer, camera, multisampleFbo);
			multisampleFbo.resolveTo(outputFbo);
			PostProcessing.doPostProcessing(outputFbo.getColorTexture());

			ParticleMaster.render(camera);

			guiRenderer.render(guiTextures);
			TextMaster.render();

			DisplayManager.updateDisplay();
			if (Display.isCloseRequested()) running.set(false);
		}

		//*********Clean Up Below**************
		ParticleMaster.cleanUp();
		PostProcessing.cleanUp();
		multisampleFbo.cleanUp();
		outputFbo.cleanUp();
		TextMaster.cleanUp();
		world.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

	private void update() {
		KeyHandler.update();
		world.update();
		camera.update();
		picker.update();
	}

	@Override
	public void run() {
		double timePerTick = 1000000000.0 / TICKS_PER_SECOND;

		long updateTimer = 0;

		double delta = 0;
		int updates = 0;
		long now, lastTime = System.nanoTime();
		while (running.get()) {
			now = System.nanoTime();
			long nowMinusLastTime = (now - lastTime);

			delta += nowMinusLastTime / timePerTick;
			updateTimer += nowMinusLastTime;
			lastTime = now;

			while (delta >= 1) {
				update();
				updateCount++;
				updates++;
				delta--;
			}
			if (updateTimer >= 1000000000) {
				updates = 0;
				updateTimer = 0;
			}

		}
	}

}
