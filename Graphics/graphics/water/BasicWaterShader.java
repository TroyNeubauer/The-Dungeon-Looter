package graphics.water;

import com.troy.troyberry.math.Matrix4f;

import graphics.shader.ShaderProgram;
import thedungeonlooter.camera.ICamera;

public class BasicWaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "/graphics/water/BasicWater.vert";
	private final static String FRAGMENT_FILE = "/graphics/water/BasicWater.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;

	public BasicWaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(ICamera camera){
		loadMatrix(location_viewMatrix, camera.getViewMatrix());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
