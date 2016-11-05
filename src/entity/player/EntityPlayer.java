package entity.player;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;

import assets.Assets;
import entity.Camera;
import entity.EntityLiving;
import gamestate.WorldState;
import graphics.TexturedModel;
import graphics.font.loader.GUIText;
import graphics.font.renderer.TextMaster;
import graphics.postprocessing.ContrastChanger;
import graphics.renderer.MasterRenderer;
import input.Controls;
import input.GameSettings;
import world.World;

public class EntityPlayer extends EntityLiving {

	private boolean grabbed = true, sprinting = false;
	private static final float ORIGIONAL_RUN_SPEED = 0.05f, ORIGIONAL_JUMP_POWER = 0.05f;
	private float runSpeed = ORIGIONAL_RUN_SPEED, jumpPower = ORIGIONAL_JUMP_POWER;
	private float slope = 0f;
	private GUIText healthText, deadText;
	private DeathAnimation deathAnimation;

	public EntityPlayer(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale, 100f);
		this.scale = 0.25f;
		this.invincible = true;
	}

	public void update() {
		updateStats();
		if (Controls.KILL_PLAYER.hasBeenPressed()) {
			this.kill();
		}
		move();
		this.position.add(velocity);
		if (isDead) {
			deathAnimation.update();
			Camera.getCamera().roll = (deathAnimation.redFactor * 60f);
		}
	}

	private void updateStats() {
		if(((Controls.SPRINT.isPressedUpdateThread() || Controls.MOSUE_SPRINT.isPressed()) && Controls.isPressingMoreThenAmount(Controls.DIRECT_MOVEMENT, 0)) || sprinting){
			runSpeed = 1.5f * ORIGIONAL_RUN_SPEED;
			jumpPower = 1.5f * ORIGIONAL_JUMP_POWER;
			sprinting = true;
			if(!Controls.isPressingMoreThenAmount(Controls.DIRECT_MOVEMENT, 0)){
				sprinting = false;
			}
		}else{
			runSpeed = ORIGIONAL_RUN_SPEED;
			jumpPower = ORIGIONAL_JUMP_POWER;
			sprinting = false;
		}
		
	}

	public void render() {
		TextMaster.removeText(healthText);
		healthText = new GUIText((Math.max(Maths.round(getHealth()), 0)) + "HP", GameSettings.FONT_SIZE + 0.3f,
				Assets.font, new Vector2f(0.91f, 0.96f), 1f, false);
		TextMaster.loadText(healthText);

		if (isDead() && deathAnimation != null) {
			Camera.getCamera().cameraHeight = (float) (1
					+ Math.sin(Math.toRadians((0.3 - Math.min(deathAnimation.redFactor * 3, 0.3)) * 90.0)));
			ContrastChanger.add.x = deathAnimation.redFactor;
			ContrastChanger.add.y = -deathAnimation.redFactor / 2;
			ContrastChanger.add.z = -deathAnimation.redFactor;
		}

	}

	private void move() {
		Mouse.setGrabbed(grabbed);

		if (grabbed && isAlive()) {
			int x = Mouse.getX() - Display.getWidth() / 2, y = Mouse.getY() - Display.getHeight() / 2;
			rotation.y += (x / 10.0);
			rotation.x -= (y / 10.0);
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}

		float dx = (float) (Math.cos(Math.toRadians(rotation.y - 90)));
		float dz = (float) (Math.sin(Math.toRadians(rotation.y - 90)));
		Vector2f forward = checkInputs(new Vector2f(dx, dz));
		velocity.y -= World.GRAVITY;

		float terrainHeight = WorldState.world.getHeight(position.x, position.z);

		this.velocity.x = forward.x;
		this.velocity.z = forward.y;
		if (this.rotation.x > 90)
			this.rotation.x = 90;
		if (this.rotation.x < -90)
			this.rotation.x = -90;
		this.rotation.modulus(180f, 360f, 180f);

		if (position.y - (jumpPower * 0.9f) <= terrainHeight) {
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
		if (isAlive()) {
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
			total.setLength(runSpeed);
		}
		return total;
	}

	private void jump() {
		if (!isInAir) {
			velocity.y += jumpPower;
			position.y += (float)Math.cbrt(jumpPower) / 3f;
			isInAir = true;
		}
	}

	@Override
	public void onDeath() {
		deathAnimation = new DeathAnimation(0.0007f);
		deadText = new GUIText("YOU DIED ", 5f, Assets.font, new Vector2f(0f, 0.4f), 1f, true);
		TextMaster.loadText(deadText);
	}

}
