package main;

import java.util.concurrent.atomic.AtomicBoolean;

import com.troy.troyberry.opengl.util.Window;

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
			if (Window.getInstance().isCloseRequested()) break;
			Window.getInstance().update();
		}

		//*********Clean Up Below**************
		Window.getInstance().hide();
		GameManager.cleanUp();
		Window.getInstance().destroy();
		System.exit(0);
	}

}
