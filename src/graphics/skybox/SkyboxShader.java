package graphics.skybox;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import entity.Camera;
import graphics.shader.ShaderProgram;
import utils.MathUtil;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/graphics/skybox/skybox.vert";
	private static final String FRAGMENT_FILE = "src/graphics/skybox/skybox.frag";

	private static final float ROTATE_SPEED = 1f;

	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_night;
	private int location_blendFactor;

	private float rotation = 0;

	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = MathUtil.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
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
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}