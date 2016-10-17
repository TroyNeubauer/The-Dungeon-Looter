package particles;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import entity.Camera;
import main.DisplayManager;
import world.World;

public class Particle {

	public Vector3f position, velocity;
	public float gravityEffect, lifeLength, rotation, scale, distance;
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

	protected boolean update() {
		this.velocity.y -= World.GRAVITY * gravityEffect * (DisplayManager.getFrameTimeSeconds() * 1000f);
		Vector3f change = new Vector3f(velocity);
		change.scale(DisplayManager.getFrameTimeSeconds());
		this.position.add(change);
		distance = Vector3f.sub(Camera.getCamera().position, position, null).lengthSquared();
		updateTextureCoordInfo();
		age += DisplayManager.getFrameTimeSeconds();
		return age < lifeLength;
	}

	private void updateTextureCoordInfo() {
		float lifeFactor = age / lifeLength;
		int stageCount = texture.numberOfRows * texture.numberOfRows;
		float atlasProgression = lifeFactor * stageCount;
		int index1 = Maths.floor(atlasProgression);
		int index2 = (index1 < stageCount - 1) ? index1 + 1 : index1;
		this.blend = atlasProgression % 1f;
		setTextureOffset(textureOffset1, index1);
		setTextureOffset(textureOffset2, index2);
	}

	private void setTextureOffset(Vector2f offset, int index) {
		int colum = index % texture.numberOfRows;
		int row = index / texture.numberOfRows;
		offset.x = (float) colum / texture.numberOfRows;
		offset.y = (float) row / texture.numberOfRows;
	}

}
