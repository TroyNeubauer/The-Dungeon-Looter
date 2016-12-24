package thedungeonlooter.camera;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.Window;

public abstract class ICamera {
	protected Vector3f position, velocity;
	protected float pitch, yaw, roll;
	protected Matrix4f viewMatrix, projectionMatrix;
	
	public static float FOV = 70;
	public static final float NEAR_PLANE = 0.01f;
	public static final float FAR_PLANE = 1000;
	
	public ICamera() {
		this.position = new Vector3f();
		this.velocity = new Vector3f();
		updateViewMatrix();
		createProjectionMatrix();
	}
	
	public ICamera(ICamera camera) {
		this.position = new Vector3f(camera.position);
		this.velocity = new Vector3f(camera.velocity);
		this.pitch = camera.pitch;
		this.yaw = camera.yaw;
		this.roll = camera.roll;
		this.viewMatrix = new Matrix4f(camera.viewMatrix);
		this.projectionMatrix = new Matrix4f(camera.projectionMatrix);
	}

	public abstract void move();
	
	public abstract void render();
	
	public void update(){
		move();
		updateViewMatrix();
	}
	
	private Matrix4f createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Window.getInstance().getWidth() / (float) Window.getInstance().getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}
	
	public void updateViewMatrix(){
		if(viewMatrix == null) viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPos = position;
		Vector3f negativeCameraPos = Vector3f.negate(cameraPos);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void invertPitchAndRoll() {
		this.pitch = -pitch;
		this.roll = -roll;
	}
}
