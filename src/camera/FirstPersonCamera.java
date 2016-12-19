package camera;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;

import entity.player.EntityPlayer;
import utils.MathUtil;

public class FirstPersonCamera extends PlayerBasedCamera {

	public FirstPersonCamera() {
		super();
	}

	public void move() {
		this.position.x = player.position.x;
		this.position.y = player.position.y + cameraHeight;
		this.position.z = player.position.z;

		this.pitch = player.rotation.x;
		this.yaw = player.rotation.y;

	}

	public void invertPitch() {
		this.pitch = -pitch;
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
}
