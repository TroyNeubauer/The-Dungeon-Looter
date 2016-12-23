package graphics.sky;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;

import graphics.shader.ShaderProgram;
import thedungeonlooter.camera.ICamera;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/graphics/sky/skybox.vert";
	private static final String FRAGMENT_FILE = "/graphics/sky/skybox.frag";

	private static final float ROTATE_SPEED = 1f;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_night;
	private int location_blendFactor;
	private int location_skyColor;

	private float rotation = 0;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(ICamera camera) {
		Matrix4f matrix = new Matrix4f();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	public void connectTextureUnits() {
		super.loadInt(location_night, 0);
	}

	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_night = super.getUniformLocation("night");
		location_skyColor = super.getUniformLocation("skyColor");
	}
	
	public void loadSkyColor(Vector3f color){
		super.loadVector(location_skyColor, color);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}