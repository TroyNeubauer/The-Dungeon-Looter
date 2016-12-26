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
import loader.Assets;
import loader.Loader;
import loader.request.GlRequestProcessor;
import loader.request.RequestProcessor;
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
		OpenGlUtil.checkForErrors("Pre request processor");
		GlRequestProcessor.dealWithTopRequests();
		OpenGlUtil.checkForErrors("Pre mouse");
		Mouse.update();
		OpenGlUtil.checkForErrors("Pre gamestate reander");
		GameStateManager.render();
		OpenGlUtil.checkForErrors("Pre image render");
		ImageRenderer.render();
		OpenGlUtil.checkForErrors("Pre text master render");
		TextMaster.render();
	}

	public static void cleanUp() {
		GlRequestProcessor.completeAllRequests();
		RequestProcessor.cleanUp();
		Mouse.setGrabbed(false);
		ParticleMaster.cleanUp();
		PostProcessing.cleanUp();
		ImageRenderer.cleanUp();
		MasterRenderer.cleanUp();
		GameStateManager.cleanUp();
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
		
		SplashRenderer.init();
		Assets.loadCoreAssets();
		SplashRenderer.render();
		TextMaster.init();

		Assets.init();

		System.out.println("finished game initalization took " + t.getTime() + "\n");

		GameStateManager.start(new TitleScreenState());
		Updater.init();
	}

}
