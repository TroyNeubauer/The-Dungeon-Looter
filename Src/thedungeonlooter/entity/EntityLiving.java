package thedungeonlooter.entity;

import java.util.ArrayList;
import java.util.List;

import com.troy.troyberry.math.Vector3f;

import loader.CompleteModel;

public abstract class EntityLiving extends Entity {

	protected boolean isInAir = false, invincible = false;
	private boolean isDead = false;
	private float health;
	public static List<EntityLiving> onDead = new ArrayList<EntityLiving>();

	public EntityLiving(CompleteModel model, Vector3f position, Vector3f rotation, float scale, float health) {
		super(model, position, rotation, scale);
		this.health = health;
	}

	public EntityLiving(CompleteModel model, Vector3f position) {
		super(model, position);
	}

	public void kill() {
		if (invincible) return;
		this.setHealth(0f);
	}

	public void setHealth(float health) {
		if (invincible && (health < this.health)) return;
		this.health = health;
		checkDead();
	}

	public float getHealth() {
		return health;
	}

	public void damage(float amount) {
		if (invincible) return;
		this.health -= amount;
		checkDead();
	}

	public void increaseHealth(float amount) {
		if (invincible) return;
		this.health += amount;
		checkDead();
	}

	public boolean isDead() {
		return health <= 0f;
	}

	public void checkDead() {
		if (isDead) return;
		if (isDead()) {
			onDead.add(this);
			isDead = true;
		}
	}

	public boolean isAlive() {
		return !isDead();
	}

	public abstract void onDeath();

}
