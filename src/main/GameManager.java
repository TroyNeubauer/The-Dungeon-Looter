package main;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.opengl.input.Keyboard;
import com.troy.troyberry.opengl.input.Mouse;
import com.troy.troyberry.opengl.util.Window;
import com.troy.troyberry.utils.graphics.ResolutionUtil;

import assets.Assets;
import gamestate.GameStateManager;
import gamestate.TitleScreenState;
import graphics.image.ImageRenderer;
import graphics.image.SizeableTexture;
import graphics.postprocessing.PostProcessing;
import graphics.renderer.MasterRenderer;
import graphics.renderer.SplashRenderer;
import input.KeyHandler;
import input.MouseHandler;
import loader.Loader;
import particle.ParticleMaster;

public class GameManager {

	private static MasterRenderer renderer;

	private static Window window;


	private GameManager() {
	}

	public static void update() {
		Mouse.update();
		KeyHandler.update();
		MouseHandler.update();
	}

	public static void render() {
		ParticleMaster.update();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		GameStateManager.render();
		ParticleMaster.render();
		ImageRenderer.render();
	}

	public static void cleanUp() {
		ParticleMaster.cleanUp();
		PostProcessing.cleanUp();
		ImageRenderer.cleanUp();
		MasterRenderer.cleanUp();
		Loader.getLoader().cleanUp();
	}

	public static void init() {
		if(!GLFW.glfwInit()){
			System.err.println("Failed to initalise GLFW!");
			System.exit(1);
		}
		System.out.println("Starting " + Version.getWindowTitle() + "\n");
		Timer t = new Timer();
		window = new Window(ResolutionUtil.getscaledResolution(0.75));
		Mouse.init(window);
		Keyboard.init(window);
		
		Assets.loadCoreAssets(Loader.getLoader());
		SplashRenderer.init();

		MasterRenderer.init();
		ParticleMaster.init(MasterRenderer.projectionMatrix);
		Assets.init(Loader.getLoader());
		
		PostProcessing.init();

		System.out.println("finished game initalization took " + t.getTime() + "\n");

		GameStateManager.start(new TitleScreenState());
		Updater.init();
	}

}
