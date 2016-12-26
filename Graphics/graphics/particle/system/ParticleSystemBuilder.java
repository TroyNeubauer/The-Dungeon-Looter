package graphics.particle.system;

import com.troy.troyberry.math.Vector3f;

import graphics.particle.system.ParticleSystem.ParticleSystemShape;
import loader.texture.ParticleTexture;

public class ParticleSystemBuilder {
	
	private float pps, ppsError;
	private float speed = 1, speedError = 0.05f;
	private float size, sizeError;
	private float gravityComplient = 0.5f, gravityComplientError = 0.05f;
	private float particleLifeLength = 3, particleLifeLengthError = 0.5f;
	private float systemLifeLength = 5, systemAge = 0.0f;
	private ParticleTexture texture;
	private Vector3f systemCenter;
	private ParticleSystemShape shape;
	private float radius = 0f;
	private boolean diesWhenTouchesGround = true;
	
	public ParticleSystemBuilder(ParticleTexture texture, Vector3f center, ParticleSystemShape shape, float pps) {
		this.texture = texture;
		this.systemCenter = center;
		this.shape = shape;
		this.pps = pps;
	}
	
	public ParticleSystemBuilder setppsError(float error){
		this.ppsError = error;
		return this;
	}
	
	public ParticleSystemBuilder setParticleSpeed(float speed, float speedError){
		this.speed = speed;
		this.speedError = speedError;
		return this;
	}
	
	public ParticleSystemBuilder setRadius(float radius){
		this.shape = ParticleSystemShape.SPHERE;
		this.radius = radius;
		return this;
	}
	
	public ParticleSystemBuilder setSystemLifeLength(float seconds){
		this.systemLifeLength = seconds;
		return this;
	}
	
	public ParticleSystemBuilder setSize(float size, float error){
		this.size = size;
		this.sizeError = error;
		return this;
	}
	
	public ParticleSystemBuilder setGravity(float gravityComplient, float gravityComplientError){
		this.gravityComplient = gravityComplient;
		this.gravityComplientError = gravityComplientError;
		return this;
	}
	
	public ParticleSystemBuilder setParticleLife(float life, float error){
		this.particleLifeLength = life;
		this.particleLifeLengthError = error;
		return this;
	}
	
	public ParticleSystem getSystem(){
		return new ParticleSystem(pps, ppsError, speed, speedError, size, sizeError, gravityComplient, gravityComplientError, particleLifeLength, particleLifeLengthError, systemLifeLength, texture, systemCenter, shape, radius, diesWhenTouchesGround);
	}
	
	
	

}
