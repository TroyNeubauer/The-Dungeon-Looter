package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.opengl.input.Keyboard;
import com.troy.troyberry.opengl.input.Mouse;
import com.troy.troyberry.opengl.util.Window;
import com.troy.troyberry.utils.graphics.ResolutionUtil;

import asset.Assets;
import camera.FirstPersonCamera;
import camera.ICamera;
import gamestate.GameStateManager;
import gamestate.TitleScreenState;
import graphics.fontrendering.TextMaster;
import graphics.image.ImageRenderer;
import graphics.particle.ParticleMaster;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import graphics.renderer.SplashRenderer;
import input.KeyHandler;
import input.MouseHandler;
import loader.Loader;

public class GameManager {

	private static volatile ICamera camera;

	private static Window window;


	private GameManager() {
	}

	public static void update() {
		Mouse.update();
		KeyHandler.update();
		MouseHandler.update();
	}

	public static void render() {
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
		if(!GLFW.glfwInit()){
			System.err.println("Failed to initalise GLFW!");
			System.exit(1);
		}
		System.out.println("Starting " + Version.getWindowTitle() + "\n");
		Timer t = new Timer();
		window = new Window(ResolutionUtil.getscaledResolution(0.75));
		camera = new FirstPersonCamera();
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
	
	public static ICamera getCamera(){
		return camera;
	}

}
