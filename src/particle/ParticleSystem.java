package particle;

import com.troy.troyberry.math.Vector3f;
import main.DisplayManager;

public class ParticleSystem {

	private float pps;
	private float speed;
	private float gravityComplient;
	private float lifeLength;
	private ParticleTexture texture;

	public ParticleSystem(ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength) {
		this.texture = texture;
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
	}

	public void generateParticles(Vector3f systemCenter) {
		float delta = DisplayManager.getFrameTimeSeconds();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter);
		}
	}

	private void emitParticle(Vector3f center) {
		Vector3f velocity = new Vector3f((float) Math.random() * 2f - 1f, 0, (float) Math.random() * 2f - 1f);
		velocity.setLength(speed);
		new Particle(new Vector3f(center), velocity, texture, gravityComplient, lifeLength, 0, 1);
	}

}
