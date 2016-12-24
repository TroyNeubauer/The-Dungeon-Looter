package thedungeonlooter.frustumculling;

import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.util.Window;

import thedungeonlooter.camera.ICamera;

public class Frustum {
	
	public static final int VERTEX_COUNT = 8;

	private Vector4f[] originalVertices = new Vector4f[VERTEX_COUNT];
	private Vector4f[] frustumVertices = new Vector4f[VERTEX_COUNT];

	private ICamera camera;

	private float frustumLength;
	private float farWidth, farHeight, nearWidth, nearHeight;
	private Matrix4f cameraTransform = new Matrix4f();

	public Frustum(ICamera camera) {
		this.camera = camera;
		this.frustumLength = ICamera.FAR_PLANE;
		initFrusutmVertices();
		calculateOriginalVertices();
		update(camera);
	}
	
	public void update(ICamera camera) {
		this.camera = camera;
		updateCameraTransform();
		for (int i = 0; i < frustumVertices.length; i++) {
			Matrix4f.transform(cameraTransform, originalVertices[i], frustumVertices[i]);
		}
	}

	public Vector3f getVertex(int i){
		return new Vector3f(frustumVertices[i]);
	}

	private void calculateWidthsAndHeights() {
		farHeight = (float) (frustumLength * Math.tan(Math.toRadians(ICamera.FOV/2f)));
		nearHeight = (float) (ICamera.NEAR_PLANE * Math.tan(Math.toRadians(ICamera.FOV/2f)));
		farWidth = farHeight * Window.getInstance().getAspectRatio();
		nearWidth = nearHeight * Window.getInstance().getAspectRatio();
	}

	private void calculateOriginalVertices() {
		calculateWidthsAndHeights();
		for (int i = 0; i < originalVertices.length; i++) {
			originalVertices[i] = getVertex((i / 4) % 2 == 0, i % 2 == 0, (i / 2) % 2 == 0);
		}
	}

	private Vector4f getVertex(boolean isNear, boolean positiveX, boolean positiveY) {
		Vector4f vertex = new Vector4f();
		vertex.z = isNear ? -ICamera.NEAR_PLANE : -frustumLength;
		Vector2f sizes = isNear ? new Vector2f(nearWidth, nearHeight) : new Vector2f(farWidth, farHeight);
		vertex.x = positiveX ? sizes.x : -sizes.x;
		vertex.y = positiveY ? sizes.y : -sizes.y;
		vertex.w = 1;
		return vertex;
	}

	private void initFrusutmVertices() {
		for (int i = 0; i < frustumVertices.length; i++) {
			frustumVertices[i] = new Vector4f();
		}
	}

	private void updateCameraTransform() {
		cameraTransform.setIdentity();
		cameraTransform.translate(camera.getPosition());
		cameraTransform.rotate(Maths.degreesToRadians(-camera.getPitch()), new Vector3f(1, 0, 0));
		cameraTransform.rotate(Maths.degreesToRadians(-camera.getYaw()), new Vector3f(0, 1, 0));
		cameraTransform.rotate(Maths.degreesToRadians(-camera.getRoll()), new Vector3f(0, 0, 1));
	}
}
