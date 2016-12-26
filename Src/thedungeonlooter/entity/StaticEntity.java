package thedungeonlooter.entity;

import com.troy.troyberry.math.Vector3f;

import loader.CompleteModel;

public class StaticEntity extends Entity {

	public StaticEntity(CompleteModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	public StaticEntity(CompleteModel model, Vector3f position) {
		super(model, position);
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
		
	}

}
