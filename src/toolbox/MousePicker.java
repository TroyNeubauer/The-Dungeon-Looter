package toolbox;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.math.Vector4f;
import entity.Camera;

public class MousePicker {

	private static final int RECURSION_COUNT = 100;
	private static final float RAY_RANGE = 600;

	private Vector3f currentRay = new Vector3f();

	private Matrix4f projectionMatrix;
	private Camera camera;

	public MousePicker(Camera cam, Matrix4f projection) {
		camera = cam;
		projectionMatrix = projection;
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
		Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / Display.getWidth() - 1f;
		float y = (2.0f * mouseY) / Display.getHeight() - 1f;
		return new Vector2f(x, y);
	}
}
