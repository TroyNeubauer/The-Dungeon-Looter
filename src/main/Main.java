package main;

import java.util.concurrent.atomic.AtomicBoolean;
import org.lwjgl.opengl.Display;

public class Main {

	public static AtomicBoolean running = new AtomicBoolean(true);

	public static void main(String[] args) {
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
