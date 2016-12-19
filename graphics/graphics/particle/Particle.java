package graphics.particle;

import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.util.Window;

import asset.ParticleTexture;
import camera.FirstPersonCamera;
import camera.ICamera;
import world.World;

public class Particle {

	public Vector3f position, velocity;
	private float gravityEffect, lifeLength, rotation, scale, distance;
	public ParticleTexture texture;

	public Vector2f textureOffset1 = new Vector2f(), textureOffset2 = new Vector2f();
	public float blend;

	private float age;

	public Particle(Vector3f position, Vector3f velocity, ParticleTexture texture, float gravityEffect, float lifeLength, float rotation,
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

	protected boolean update(ICamera camera) {
		this.velocity.y -= World.GRAVITY * gravityEffect * (Window.getFrameTimeSeconds() * 1000f);
		Vector3f change = new Vector3f(velocity);
		change.scale(Window.getFrameTimeSeconds());
		this.position.add(change);
		distance = Vector3f.subtract(camera.getPosition(), position).lengthSquared();
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
	
	public float getDistance() {
		return distance;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public float getGravityEffect() {
		return gravityEffect;
	}

	public float getLifeLength() {
		return lifeLength;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector2f getTextureOffset1() {
		return textureOffset1;
	}

	public Vector2f getTextureOffset2() {
		return textureOffset2;
	}

	public float getBlend() {
		return blend;
	}
	
	

}
