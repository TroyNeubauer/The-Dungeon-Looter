package thedungeonlooter.entity;

import com.troy.troyberry.math.Vector3f;

import loader.CompleteModel;
import loader.texture.Skin;
import loader.texture.Texture2D;

public abstract class Entity {

	public CompleteModel model;
	public Vector3f position, rotation, velocity;
	public float scale;

	private int textureIndex = 0;

	public Entity(CompleteModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.velocity = new Vector3f(0, 0, 0);
		this.rotation = rotation;
		this.scale = scale;

	}

	public Entity(CompleteModel model, Vector3f position) {
		this(model, position, new Vector3f(0, 0, 0), 1f);
	}

	public abstract void update();

	public abstract void render();

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) {
		this.rotation.x += dx;
		this.rotation.y += dy;
		this.rotation.z += dz;
	}

	public Skin getSkin() {
		return model.getSkin();
	}
	
	public Texture2D getTexture() {
		return model.getTexture();
	}
}
