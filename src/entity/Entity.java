package entity;

import com.troy.troyberry.math.Vector3f;

import asset.TexturedModel;

public abstract class Entity {

	public TexturedModel model;
	public Vector3f position, rotation, velocity;
	public float scale;
	public boolean skipRender = false, hasNormalMap = false, skipRenderMethod = false;

	private int textureIndex = 0;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.velocity = new Vector3f(90, 0, 0);
		this.rotation = rotation;
		this.scale = scale;
		this.hasNormalMap = model.getTexture().hasNormalMap;

	}

	public Entity(TexturedModel model, Vector3f position) {
		this(model, position, new Vector3f(0, 0, 0), 1f);
	}

	public abstract void update();

	public abstract void render();

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

	public Entity hide() {
		skipRender = true;
		return this;
	}

	public Entity show() {
		skipRender = false;
		return this;
	}
}
