package main;

import java.awt.GraphicsEnvironment;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import com.troy.troyberry.math.Vector2f;
import graphics.Assets;
import graphics.font.loader.GUIText;
import graphics.font.renderer.TextMaster;
import input.GameSettings;

public class DisplayManager {

	private static int WIDTH;
	private static int HEIGHT;

	private static long lastFrameTime = 0, timer = 0, frames = 0;
	private static float timeTaken;
	private static double delta;

	private static GUIText FPSText;

	public static void createDisplay(int width, int height, boolean fullscreen) {
		if (fullscreen) {
			java.awt.DisplayMode de = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
			width = de.getWidth();
			height = de.getHeight();
		}
		WIDTH = width;
		HEIGHT = height;
		ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);

		try {
			setDisplayMode(width, height, fullscreen);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(Version.getWindowTitle());
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		if (GameSettings.DEBUG && !fullscreen) Display.setLocation((1920 - Display.getWidth()) / 2, 0);
		lastFrameTime = getCurrentTime();

	}

	public static void setDisplayMode(int width, int height, boolean fullscreen) {
		if (fullscreen) {
			java.awt.DisplayMode de = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
			width = de.getWidth();
			height = de.getHeight();
		}
		// return if requested DisplayMode is already set
		if ((Display.getDisplayMode().getWidth() == width) && (Display.getDisplayMode().getHeight() == height)
			&& (Display.isFullscreen() == fullscreen)) {
			return;
		}

		try {
			DisplayMode targetDisplayMode = null;

			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;

				for (int i = 0; i < modes.length; i++) {
					DisplayMode current = modes[i];

					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against the 
						// original display mode then it's probably best to go for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
							&& (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width, height);
			}

			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);

		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
		}
	}

	public static void updateDisplay(boolean calcFPS) {
		if (calcFPS) {
			frames++;
			long now = System.nanoTime();
			timeTaken = (now - lastFrameTime);
			timer += timeTaken;
			delta = timeTaken / 1000000000l;
			lastFrameTime = now;
			if (timer > 1000000000l) {
				TextMaster.removeText(FPSText);
				FPSText = new GUIText("fps " + frames, GameSettings.FONT_SIZE, Assets.font, new Vector2f(0.001f, 0.001f), 1f, false);
				TextMaster.loadText(FPSText);
				timer = 0;
				frames = 0;
			}
		}
		Display.update();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 1, 1, 1);
	}

	public static void closeDisplay() {
		Display.destroy();
	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

	public static float getFrameTimeSeconds() {
		return (float) Updater.TIME_PER_UPDATE;
	}

}
