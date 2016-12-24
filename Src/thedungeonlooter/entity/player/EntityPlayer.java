package thedungeonlooter.entity.player;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;

import graphics.fontcreator.GUIText;
import graphics.postprocessing.ContrastChanger;
import graphics.renderer.ScreenShotMaster;
import loader.asset.Assets;
import loader.asset.TexturedModel;
import thedungeonlooter.camera.CameraMaster;
import thedungeonlooter.camera.CameraMaster.CameraMode;
import thedungeonlooter.entity.EntityLiving;
import thedungeonlooter.gamestate.WorldState;
import thedungeonlooter.input.Controls;
import thedungeonlooter.world.World;

public class EntityPlayer extends EntityLiving {

	private boolean grabbed = true, sprinting = false;
	public static final float PLAYER_HEIGHT = 2.0f;
	private static final float ORIGIONAL_RUN_SPEED = 0.075f, ORIGIONAL_JUMP_POWER = 0.05f;
	public float runSpeed = ORIGIONAL_RUN_SPEED;
	private float jumpPower = ORIGIONAL_JUMP_POWER;
	private float slope = 0f;
	private DeathAnimation deathAnimation;
	private GUIText positionText;
	public static boolean inUI = false;

	public EntityPlayer(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale, 100f);
		this.scale = 0.3f;
		this.invincible = true;
		this.positionText = new GUIText("", 1.2f, Assets.font, new Vector2f(0, 0.96f), 1.0f, false);
	}

	public void update() {
		updateStats();
		if (Controls.KILL_PLAYER.hasBeenPressed()) this.kill();
		if(Controls.CHANGE_VIEW.hasBeenPressed())CameraMaster.nextMode();
		move();
		this.position.add(velocity);
		if (isDead()) {
			deathAnimation.update();
		}
	}

	private void updateStats() {
		if(((Controls.MOSUE_SPRINT.isPressed()) && Controls.isPressingMoreThenAmount(Controls.DIRECT_MOVEMENT, 0)) || sprinting){
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
		if(Controls.TAKE_SCREENSHOT.hasBeenPressed()){
			ScreenShotMaster.takeAndSaveScreenShot();
		}
		positionText.setText("Pos: " + position.clip(1));
		positionText.setColor(WorldState.world.blendFactor);
		if (isDead() && deathAnimation != null) {
			ContrastChanger.add.x = deathAnimation.redFactor;
			ContrastChanger.add.y = -deathAnimation.redFactor / 2;
			ContrastChanger.add.z = -deathAnimation.redFactor;
		}

	}

	private void move() {
		if (Controls.UP.hasBeenPressed()) {
			jump();
		}
		
		float terrainHeight = WorldState.world.getHeight(position.x, position.z);
		velocity.y -= World.GRAVITY;
		if ((position.y - ((CameraMaster.getMode() == CameraMode.FIRST_PERSON) ? PLAYER_HEIGHT : 0.0f)) - (jumpPower * 0.9f) <= terrainHeight) {
			if (isInAir && velocity.y < -0.2f) {
				float damage = (Math.abs(velocity.y) - 0.2f) * 250f;
				this.damage(damage);
			}
			isInAir = false;
			position.y = terrainHeight + ((CameraMaster.getMode() == CameraMode.FIRST_PERSON) ? PLAYER_HEIGHT : 0.0f);
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

	public void jump() {
		if (!isInAir) {
			velocity.y += jumpPower;
			position.y += (float)Math.cbrt(jumpPower) / 3f;
			isInAir = true;
		}
	}

	@Override
	public void onDeath() {
		deathAnimation = new DeathAnimation(0.0007f);
	}
}
