package main;

import java.util.concurrent.atomic.AtomicBoolean;
import org.lwjgl.opengl.Display;

public class Main implements Runnable {

	public static AtomicBoolean running = new AtomicBoolean(true);
	private static Thread thread;

	public static void main(String[] args) {
		thread = new Thread(new Main(), "Open Gl Thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	@Override
	public void run() {
		GameManager.init();
		//****************Game Loop Below*********************
		while (running.get()) {
			GameManager.render();
			if (Display.isCloseRequested()) running.set(false);
		}

		//*********Clean Up Below**************
		GameManager.cleanUp();
		DisplayManager.closeDisplay();
	}

}
