package graphics.particle;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.Window;

import asset.ParticleTexture;
import asset.Texture;
import camera.FirstPersonCamera;
import input.GameSettings;
import main.GameManager;

public class ParticleSystem {
	
	private float pps, ppsError;
	private float speed, speedError;
	private float size, sizeError;
	private float gravityComplient, gravityComplientError;
	private float particleLifeLength, particleLifeLengthError;
	private float systemLifeLength, systemAge;
	private ParticleTexture texture;
	private Vector3f systemCenter;
	private ParticleSystemShape shape;
	private float radius;

	public ParticleSystem(float pps, float ppsError, float speed, float speedError, float size, float sizeError,
			float gravityComplient, float gravityComplientError, float particleLifeLength, float particleLifeLengthError,
			float systemLifeLength, ParticleTexture texture, Vector3f systemCenter, ParticleSystemShape shape, float radius) {
		
		this.pps = pps;
		this.ppsError = ppsError;
		this.speed = speed;
		this.speedError = speedError;
		this.size = size;
		this.sizeError = sizeError;
		this.gravityComplient = gravityComplient;
		this.gravityComplientError = gravityComplientError;
		this.particleLifeLength = particleLifeLength;
		this.particleLifeLengthError = particleLifeLengthError;
		this.systemLifeLength = systemLifeLength;
		this.texture = texture;
		this.systemCenter = systemCenter;
		this.shape = shape;
		this.radius = radius;
	}

	public boolean update() {
		float delta = Window.getFrameTimeSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle();
		}
		if (Math.random() < partialParticle) {
			emitParticle();
		}
		systemAge += Window.getFrameTimeSeconds();
		return systemAge > systemLifeLength;
	}

	private void emitParticle() {
		if(Maths.approximateDistanceBetweenPoints(systemCenter, GameManager.getCamera().getPosition()) > GameSettings.RENDER_DISTANCE / 10.0f)return;
		Vector3f velocity = Vector3f.randomVector(1f).setLength(speed + Maths.randRange(-speedError, +speedError));
		if(shape == ParticleSystemShape.POINT) {
			new Particle(new Vector3f(systemCenter), velocity, texture, gravityComplient, particleLifeLength, 0, size + Maths.randRange(-sizeError, +sizeError));
		} else if(shape == ParticleSystemShape.SPHERE) {
			new Particle(Vector3f.add(systemCenter, Vector3f.randomVector(radius)), velocity, texture, gravityComplient, particleLifeLength, 0, 1);
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public float getParticlesPerSecond() {
		return pps;
	}

	public float getSystemLifeLength() {
		return systemLifeLength;
	}
	
	public float getAveragePatricleLifeLength() {
		return particleLifeLength;
	}

	public float getAverageParticleSpeed() {
		return speed;
	}
	
	public static enum ParticleSystemShape {
		POINT, SPHERE
	}

}
