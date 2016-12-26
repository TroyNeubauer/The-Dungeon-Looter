package graphics.particle.system;

import com.troy.troyberry.math.Vector3f;

import graphics.particle.ParticleMaster;
import graphics.particle.system.ParticleSystem.ParticleSystemShape;
import loader.Assets;

public class SmokeSystem {

	public SmokeSystem(Vector3f position) {
		ParticleSystemBuilder b = new ParticleSystemBuilder(Assets.smokeParticle, position, ParticleSystemShape.SPHERE, 250);
		b.setParticleLife(3, 1);
		b.setGravity(0, 0.2f);
		b.setSize(0.35f, 0.05f);
		b.setParticleSpeed(0.2f, 0.01f);
		b.setSystemLifeLength(30);
		b.setRadius(0.5f);
		
		ParticleMaster.addSystem(b.getSystem());
	}

}
