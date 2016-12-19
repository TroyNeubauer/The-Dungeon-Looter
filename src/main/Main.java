package main;

import java.util.concurrent.atomic.AtomicBoolean;

import com.troy.troyberry.opengl.util.Window;

import loader.Loader;
import utils.MiscUtil;

public class Main implements Runnable {

	public static AtomicBoolean running = new AtomicBoolean(true);
	private static Thread thread;

	public static void main(String[] args) {
		thread = new Thread(new Main(), "Open Gl Thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	public void run() {
		GameManager.init();
		//************** Game Loop **************
		while (running.get()) {
			GameManager.render();
			if (Window.getInstance().isCloseRequested()) break;
			Window.getInstance().update();
		}
		running.set(false);
		//************** Clean Up **************
		GameManager.cleanUp();
		Window.getInstance().destroy();
		System.exit(0);
	}

}
