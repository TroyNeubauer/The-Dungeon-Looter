package entity;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {

	private static List<Entity> entitiesToAdd = new ArrayList<Entity>();

	public static void addEntity(Entity e) {
		entitiesToAdd.add(e);
	}

	public static void clear() {
		entitiesToAdd.clear();
	}

	public static List<Entity> getEntitiesToAdd() {
		return entitiesToAdd;
	}

	private EntityManager() {
	}

}
