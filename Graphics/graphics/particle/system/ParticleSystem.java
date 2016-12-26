package graphics.particle.system;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.Window;

import graphics.particle.Particle;
import loader.texture.ParticleTexture;
import loader.texture.Texture;
import thedungeonlooter.gamestate.WorldState;
import thedungeonlooter.input.GameSettings;

public class ParticleSystem {
	
	protected float pps, ppsError;
	protected float speed, speedError;
	protected float size, sizeError;
	protected float gravityComplient, gravityComplientError;
	protected float particleLifeLength, particleLifeLengthError;
	protected float systemLifeLength, systemAge;
	protected ParticleTexture texture;
	protected Vector3f systemCenter;
	protected ParticleSystemShape shape;
	protected float radius;
	protected boolean diesWhenTouchesGround;

	public ParticleSystem(float pps, float ppsError, float speed, float speedError, float size, float sizeError,
			float gravityComplient, float gravityComplientError, float particleLifeLength, float particleLifeLengthError,
			float systemLifeLength, ParticleTexture texture, Vector3f systemCenter, ParticleSystemShape shape, float radius, boolean diesWhenTouchesGround) {
		
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
		this.diesWhenTouchesGround = diesWhenTouchesGround;
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
		if(Maths.approximateDistanceBetweenPoints(systemCenter, WorldState.camera.getPosition()) > GameSettings.RENDER_DISTANCE / 10.0f)return;
		Vector3f velocity = Vector3f.randomVector(1f).setLength(speed + Maths.randRange(-speedError, +speedError));
		if(shape == ParticleSystemShape.POINT) {
			new Particle(new Vector3f(systemCenter), velocity, texture, gravityComplient, particleLifeLength, 0, size + Maths.randRange(-sizeError, +sizeError), diesWhenTouchesGround);
		} else if(shape == ParticleSystemShape.SPHERE) {
			Vector3f startPos = Vector3f.add(systemCenter, Vector3f.randomVector(radius));
			if(startPos.y < WorldState.world.getHeight(startPos.x, startPos.z) && diesWhenTouchesGround) return;
			new Particle(startPos, velocity, texture, gravityComplient, particleLifeLength, 0, 1, diesWhenTouchesGround);
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
