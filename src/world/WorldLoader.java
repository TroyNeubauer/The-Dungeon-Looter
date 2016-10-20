package world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.troy.troyberry.math.Maths;
import entity.Camera;
import input.GameSettings;
import main.GameManager;

public class WorldLoader extends Thread {

	private static Thread thread;
	private static List<Terrain> loadedTerrains = new ArrayList<Terrain>();

	static {
		init();
	}

	public static AtomicBoolean hasUpdate = new AtomicBoolean(false), needUpdate = new AtomicBoolean(false);

	public static boolean hasUpdate() {
		return hasUpdate.get();
	}

	public static List<Terrain> getUpdate() {
		return new ArrayList<Terrain>(loadedTerrains);
	}

	public static void completedRequest() {
		hasUpdate.set(false);
	}

	private WorldLoader() {
		super("World Loader Thread");
	}

	private static void init() {
		thread = new WorldLoader();
		thread.start();
	}

	@Override
	public void run() {
		while (true) {
			int i = 0, increaseAmount = 10;
			while (i < GameSettings.TERRAIN_LOADER_INTERVAL) {
				if (needUpdate.get()) {
					break;
				}
				i += increaseAmount;
				try {
					Thread.sleep(increaseAmount);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			needUpdate.set(false);
			try {
				Camera camera = Camera.getCamera();

				loadedTerrains.clear();
				List<Terrain> coppiedFromWorld = new ArrayList<Terrain>(GameManager.world.allTerrains);
				for (Terrain terrain : coppiedFromWorld) {
					if (Maths.approximateDistanceBetweenPoints(terrain.x + Terrain.SIZE / 2.0, terrain.z + Terrain.SIZE / 2.0, camera.position.x,
						camera.position.z) < GameSettings.RENDER_DISTANCE + Terrain.SIZE * 0.75) {
						loadedTerrains.add(terrain);
					}
				}
				hasUpdate.set(true);
			} catch (Exception e) {
			}
		}
	}

	public static void update() {
		needUpdate.set(true);
	}

}
