package entity;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import graphics.Assets;
import graphics.TexturedModel;
import input.Controls;
import input.GameSettings;
import world.World;

public class EntityPlayer extends EntityLiving {

	private boolean grabbed = true;
	private static final float RUN_SPEED = 0.05f, JUMP_POWER = 0.05f;
	private float slope = 0f;
	private GUIText healthText;

	public EntityPlayer(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale, false);
		this.scale = 0.25f;
	}

	public void update() {
		if (isDead) return;
		move();
		this.position.add(velocity);
	}

	public void render() {
		TextMaster.removeText(healthText);
		healthText = new GUIText((Maths.round(health)) + "HP", GameSettings.FONT_SIZE + 0.3f, Assets.debugFont, new Vector2f(0.001f, 0.96f), 1f,
			false);
		TextMaster.loadText(healthText);
	}

	private void move() {
		Mouse.setGrabbed(grabbed);

		if (grabbed) {
			int x = Mouse.getX() - Display.getWidth() / 2, y = Mouse.getY() - Display.getHeight() / 2;
			rotation.y += (x / 10.0);
			rotation.x -= (y / 10.0);
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}

		float dx = (float) (Math.cos(Math.toRadians(rotation.y - 90)));
		float dz = (float) (Math.sin(Math.toRadians(rotation.y - 90)));
		Vector2f forward = checkInputs(new Vector2f(dx, dz));
		velocity.y -= World.GRAVITY;

		float terrainHeight = world.getHeight(position.x, position.z);

		this.velocity.x = forward.x;
		this.velocity.z = forward.y;
		if (this.rotation.x > 90) this.rotation.x = 90;
		if (this.rotation.x < -90) this.rotation.x = -90;
		this.rotation.modulus(180f, 360f, 180f);

		if (position.y - (JUMP_POWER * 0.9f) <= terrainHeight) {
			if (isInAir && velocity.y < -0.2f) {
				float damage = (Math.abs(velocity.y) - 0.2f) * 250f;
				this.damage(damage);
			}
			isInAir = false;
			position.y = terrainHeight;
			if (velocity.y < 0) {
				velocity.y = 0;
			}
		} else {
			isInAir = true;
			float airDrag = 0.6f;
			this.velocity.x *= airDrag;
			this.velocity.z *= airDrag;
		}
	}

	private Vector2f checkInputs(Vector2f forward) {
		Vector2f total = new Vector2f(0f, 0f);

		if (Controls.FORWARD.isPressedUpdateThread()) {
			total.add(forward);
		}
		if (Controls.BACKWARD.isPressedUpdateThread()) {
			total.add(Vector2f.negate(forward));
		}
		if (Controls.LEFT.isPressedUpdateThread()) {
			total.add(forward.rotate(-90));
		}
		if (Controls.RIGHT.isPressedUpdateThread()) {
			total.add(forward.rotate(90));
		}
		if (Controls.UP.hasBeenPressed()) {
			jump();
		}
		total.setLength(RUN_SPEED);
		return total;
	}

	private void jump() {
		if (!isInAir) {
			velocity.y += JUMP_POWER;
			isInAir = true;
		}
	}

	@Override
	public void onDeath() {

	}

}
