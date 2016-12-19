package entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

	private static List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private static Object lock = new Object();

	public static void clear() {
		synchronized (lock) {
			entitiesToAdd.clear();
		}
		
	}

	public static void addEntity(Entity e) {
		synchronized (lock) {
			entitiesToAdd.add(e);
		}
	}

	public static List<Entity> getEntitiesToAdd() {
		synchronized (lock) {
			return entitiesToAdd;
		}
	}

}
