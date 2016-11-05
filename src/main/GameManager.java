package main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.troy.troyberry.logging.Timer;

import assets.Assets;
import gamestate.GameStateManager;
import gamestate.TitleScreenState;
import graphics.font.renderer.TextMaster;
import graphics.image.ImageRenderer;
import graphics.image.SizeableTexture;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import graphics.renderer.SplashRenderer;
import input.GameSettings;
import input.KeyHandler;
import input.MouseHandler;
import loader.Loader;
import particle.ParticleMaster;

public class GameManager {

	private static MasterRenderer renderer;

	private static ImageRenderer guiRenderer;

	private static List<SizeableTexture> guiTextures;

	private GameManager() {
	}

	public static void update() {
		KeyHandler.update();
		MouseHandler.update();
	}

	public static void render() {
		ParticleMaster.update();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		GameStateManager.render();
		ParticleMaster.render();
		TextMaster.render();
		ImageRenderer.render();
		DisplayManager.updateDisplay(true);
	}

	public static void cleanUp() {
		ParticleMaster.cleanUp();
		PostProcessing.cleanUp();
		TextMaster.cleanUp();
		ImageRenderer.cleanUp();
		MasterRenderer.cleanUp();
		Loader.getLoader().cleanUp();
	}

	public static void init() {

		System.out.println("Starting " + Version.getWindowTitle() + "\n");
		Timer t = new Timer();
		DisplayManager.createDisplay(1440, 810, false);
		try {
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Assets.loadCoreAssets(Loader.getLoader());
		SplashRenderer.init();

		TextMaster.init();
		MasterRenderer.init();
		ParticleMaster.init(MasterRenderer.projectionMatrix);
		Assets.init(Loader.getLoader());

		guiTextures = new ArrayList<SizeableTexture>();
		guiRenderer = new ImageRenderer();
		
		PostProcessing.init();

		System.out.println("finished game initalization took " + t.getTime() + "\n");

		GameStateManager.start(new TitleScreenState());
		Updater.init();
	}

}
