package utils;

import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.input.Mouse;
import com.troy.troyberry.opengl.util.Window;

import camera.FirstPersonCamera;
import camera.ICamera;

public class MousePicker {

	private Vector3f currentRay = new Vector3f();

	private ICamera camera;
	
	public MousePicker(ICamera camera) {
		this.camera = camera;
	}

	public Vector3f getCurrentRay() {
		return currentRay;
	}

	public void update() {
		currentRay = calculateMouseRay();
	}

	private Vector3f calculateMouseRay() {
		float mouseX = Mouse.getX();
		float mouseY = Mouse.getY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = Matrix4f.invert(camera.getViewMatrix(), null);
		Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalised();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		Matrix4f invertedProjection = Matrix4f.invert(camera.getProjectionMatrix(), null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Window.getInstance().getWidth() - 1f;
		float y = (2.0f * mouseY) / Window.getInstance().getHeight() - 1f;
		return new Vector2f(x, y);
	}
}
