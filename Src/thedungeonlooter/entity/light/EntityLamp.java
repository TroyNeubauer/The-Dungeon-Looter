package thedungeonlooter.entity.light;

import com.troy.troyberry.math.Vector3f;

import loader.Assets;
import thedungeonlooter.entity.Entity;

public class EntityLamp extends Entity {
	
	private Light light;

	public EntityLamp(Vector3f position, float scale, Vector3f color, Vector3f attenuation) {
		super(Assets.lamp, position, new Vector3f(), scale);
		this.light = new Light(position, color, attenuation);
	}
	
	public Light getLight() {
		return light;
	}

	@Override
	public void update() {

	}

	@Override
	public void render() {

	}

}
