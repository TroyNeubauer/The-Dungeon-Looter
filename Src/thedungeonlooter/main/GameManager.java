package thedungeonlooter.main;

import org.lwjgl.glfw.GLFW;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.opengl.input.*;
import com.troy.troyberry.opengl.util.OpenGlUtil;
import com.troy.troyberry.opengl.util.Window;
import com.troy.troyberry.utils.graphics.ResolutionUtil;

import graphics.fontrendering.TextMaster;
import graphics.image.ImageRenderer;
import graphics.particle.ParticleMaster;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import graphics.renderer.SplashRenderer;
import loader.Loader;
import loader.asset.Assets;
import thedungeonlooter.gamestate.GameStateManager;
import thedungeonlooter.gamestate.TitleScreenState;

public class GameManager {

	private static Window window;

	private GameManager() {
	}

	public static void update() {
		KeyHandler.update();
		MouseHandler.update();
	}

	public static void render() {
		Mouse.update();
		GameStateManager.render();
		ImageRenderer.render();
		TextMaster.render();
	}

	public static void cleanUp() {
		Mouse.setGrabbed(false);
		ParticleMaster.cleanUp();
		PostProcessing.cleanUp();
		ImageRenderer.cleanUp();
		MasterRenderer.cleanUp();
		Assets.cleanUp();
		Loader.cleanUp();
	}

	public static void init() {
		if(!OpenGlUtil.init()){
			System.exit(1);
		}
		System.out.println("Starting " + Version.getWindowTitle() + "\n");
		Timer t = new Timer();
		window = new Window(ResolutionUtil.getscaledResolution(0.75));
		Mouse.init(window);
		Keyboard.init(window);
		
		Assets.loadCoreAssets();
		SplashRenderer.init();
		TextMaster.init();

		Assets.init();

		System.out.println("finished game initalization took " + t.getTime() + "\n");

		GameStateManager.start(new TitleScreenState());
		Updater.init();
	}

}
