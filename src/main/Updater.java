package main;

import gamestate.GameStateManager;

public class Updater extends Thread {

	public static volatile int updateCount = 0;
	public static final double UPDATES_PER_SECOND = 100.0, TIME_PER_UPDATE = 1.0 / UPDATES_PER_SECOND;

	private static Thread thread = null;

	public static void init() {
		if (thread == null) {
			thread = new Updater();
			thread.start();
		}
	}

	private void update(int updateCount) {
		GameManager.update();
		GameStateManager.update(updateCount);
	}

	public void run() {
		double timePerTick = 1000000000.0 / UPDATES_PER_SECOND;

		long updateTimer = 0;

		double delta = 0;
		int updates = 0, missedUpdates;
		long now, lastTime = System.nanoTime();
		while (Main.running.get()) {
			now = System.nanoTime();
			long nowMinusLastTime = (now - lastTime);

			delta += nowMinusLastTime / timePerTick;
			updateTimer += nowMinusLastTime;
			lastTime = now;

			while (delta >= 1) {
				update(updateCount);
				updateCount++;
				updates++;
				delta--;
			}
			if (updateTimer >= 1000000000) {
				if (updates * 1.05 < (double) UPDATES_PER_SECOND) System.out.println("5% behind on update " + updates + "/" + UPDATES_PER_SECOND);
				updates = 0;
				updateTimer = 0;
			}

		}
	}

}
