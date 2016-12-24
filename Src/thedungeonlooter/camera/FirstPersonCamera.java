package thedungeonlooter.camera;

import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.input.Mouse;

import thedungeonlooter.entity.player.EntityPlayer;
import thedungeonlooter.input.Controls;
import thedungeonlooter.input.GameSettings;

public class FirstPersonCamera extends PlayerBasedCamera {

	public FirstPersonCamera() {
		super();
	}

	public FirstPersonCamera(PlayerBasedCamera camera) {
		super(camera);
	}

	public void move() {
		pitch += Mouse.getDY() / 20.0f * GameSettings.MOUSE_SENSITIVITY;
		yaw   += Mouse.getDX() / 20.0f * GameSettings.MOUSE_SENSITIVITY;
		float dx = (float) (Math.cos(Math.toRadians(yaw - 90)));
		float dz = (float) (Math.sin(Math.toRadians(yaw - 90)));
		Vector2f forward = checkInputs(new Vector2f(dx, dz));

		this.velocity.x = forward.x;
		this.velocity.z = forward.y;
		if (pitch > 90)
			pitch = 90;
		if (pitch < -90)
			pitch = -90;
		yaw %= 360.0f;
		roll %= 360.0f;
		player.position = this.position;
		player.velocity = this.velocity;
		player.rotation.x = pitch;
		player.rotation.y = yaw;
		player.rotation.z = roll;
	}
	
	private Vector2f checkInputs(Vector2f forward) {
		Vector2f total = new Vector2f(0f, 0f);
		if (player.isAlive()) {
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
			total.setLength(player.runSpeed);
		}
		return total;
	}

	public static FirstPersonCamera copyCamera(FirstPersonCamera camera) {
		FirstPersonCamera dummyCamera = new FirstPersonCamera();
		dummyCamera.setPlayer(camera.player);
		dummyCamera.position = new Vector3f(camera.position);
		dummyCamera.pitch = camera.pitch;
		dummyCamera.yaw = camera.yaw;
		dummyCamera.roll = camera.roll;
		return dummyCamera;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	@Override
	public void render() {
		Mouse.setGrabbed(!EntityPlayer.inUI && !player.isDead());
		
	}
}
