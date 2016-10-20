package entity;

import com.troy.troyberry.math.Vector3f;
import graphics.TexturedModel;

public abstract class EntityLiving extends Entity {

	protected boolean isInAir = false, isDead = false;
	protected float health;

	public EntityLiving(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
		this.health = 100f;
	}

	public EntityLiving(TexturedModel model, Vector3f position) {
		super(model, position);
	}

	public void setHealth(float health) {
		this.health += health;
		checkDead();
	}

	public void damage(float amount) {
		this.health -= amount;
		checkDead();
	}

	public boolean isDead() {
		return health <= 0f;
	}

	public void checkDead() {
		if (isDead) return;
		if (isDead()) {
			onDeath();
			isDead = true;
		}
	}

	public boolean isAlive() {
		return !isDead();
	}

	public abstract void onDeath();

}
