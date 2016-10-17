package entity;

import com.troy.troyberry.math.Vector3f;
import graphics.TexturedModel;
import world.World;

public abstract class Entity {

	public TexturedModel model;
	public Vector3f position, rotation, velocity;
	public float scale;
	public static volatile World world;
	public boolean skipRender = false;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale, boolean normalMapped) {
		this.model = model;
		this.position = position;
		this.velocity = new Vector3f(90, 0, 0);
		this.rotation = rotation;
		this.scale = scale;
		world.addEntity(this, normalMapped);

	}

	public Entity(TexturedModel model, Vector3f position) {
		this(model, position, new Vector3f(0, 0, 0), 1f, false);
	}

	public abstract void update();

	public float getTextureXOffset() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return (float) column / (float) model.getTexture().getNumberOfRows();
	}

	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}

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

	public static void setWorld(World world) {
		Entity.world = world;
	}

	public Entity hide() {
		skipRender = true;
		return this;
	}

	public Entity show() {
		skipRender = false;
		return this;
	}
}
