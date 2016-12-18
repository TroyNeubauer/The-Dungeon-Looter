package graphics.particle;

import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.util.Window;

import asset.Texture;
import entity.Camera;
import world.World;

public class Particle {

	public Vector3f position, velocity;
	public float gravityEffect, lifeLength, rotation, scale, distance;
	public Texture texture;

	public Vector2f textureOffset1 = new Vector2f(), textureOffset2 = new Vector2f();
	public float blend;

	private float age;

	public Particle(Vector3f position, Vector3f velocity, Texture texture, float gravityEffect, float lifeLength, float rotation,
		float scale) {

		this.position = position;
		this.velocity = velocity;
		this.texture = texture;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.add(this);
	}

	public float getAge() {
		return age;
	}

	protected boolean update() {
		this.velocity.y -= World.GRAVITY * gravityEffect * (Window.getFrameTimeSeconds() * 1000f);
		Vector3f change = new Vector3f(velocity);
		change.scale(Window.getFrameTimeSeconds());
		this.position.add(change);
		distance = Vector3f.sub(Camera.getCamera().position, position, null).lengthSquared();
		updateTextureCoordInfo();
		age += Window.getFrameTimeSeconds();
		return age < lifeLength;
	}

	private void updateTextureCoordInfo() {
		float lifeFactor = age / lifeLength;
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = Maths.floor(atlasProgression);
		int index2 = (index1 < stageCount - 1) ? index1 + 1 : index1;
		this.blend = atlasProgression % 1f;
		setTextureOffset(textureOffset1, index1);
		setTextureOffset(textureOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int colum = index % texture.getNumberOfRows();
		int row = index / texture.getNumberOfRows();
		offset.x = (float) colum / texture.getNumberOfRows();
		offset.y = (float) row / texture.getNumberOfRows();
	}

}
