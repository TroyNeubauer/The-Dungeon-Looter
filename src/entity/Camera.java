package entity;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import toolbox.Maths;

public class Camera {

	private static final Camera currentCamera = new Camera();

	public Vector3f position = new Vector3f(0, 0, 0);
	public float pitch = 0, yaw = 0, roll = 0;
	private static final float CAMERA_HEIGHT = 1.5f;
	private Matrix4f viewMatrix;

	private EntityPlayer player;

	public Camera() {

	}

	public void update() {
		this.viewMatrix = Maths.createViewMatrix(this);
		move();
	}

	public void move() {
		this.position.x = player.position.x;
		this.position.y = player.position.y + CAMERA_HEIGHT;
		this.position.z = player.position.z;

		this.pitch = player.rotation.x;
		this.yaw = player.rotation.y;
		this.roll = player.rotation.z;
	}

	public void invertPitch() {
		this.pitch = -pitch;
	}

	public static Camera copyCamera(Camera camera) {
		Camera dummyCamera = new Camera();
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

	public void setPlayer(EntityPlayer player) {
		this.player = player;
		move();
		viewMatrix = Maths.createViewMatrix(this);
	}

	public static synchronized Camera getCamera() {
		return currentCamera;
	}
}
