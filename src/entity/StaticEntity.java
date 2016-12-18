package entity;

import com.troy.troyberry.math.Vector3f;

import asset.TexturedModel;

public class StaticEntity extends Entity {

	public StaticEntity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}

	public StaticEntity(TexturedModel model, Vector3f position) {
		super(model, position);
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
		System.out.println("calling static render");
	}

}
