package entity;

import com.troy.troyberry.math.Vector3f;
import graphics.TexturedModel;
import world.World;


public class EntityTerrain extends Entity {

	public EntityTerrain(TexturedModel model, Vector3f position, Vector3f rotation, float scale, boolean normalMapped) {
		super(model, position, rotation, scale, normalMapped);
	}

	public EntityTerrain(TexturedModel model, Vector3f position) {
		super(model, position);
	}

	@Override
	public void update(World world) {
	}

}
