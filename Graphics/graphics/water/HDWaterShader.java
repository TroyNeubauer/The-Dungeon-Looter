package graphics.water;

import com.troy.troyberry.math.Matrix4f;

import graphics.shader.ShaderProgram;
import thedungeonlooter.camera.ICamera;

public class HDWaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "/graphics/water/HDWater.vert";
	private final static String FRAGMENT_FILE = "/graphics/water/HDWater.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;

	public HDWaterShader() {
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
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_reflectionTexture, 0);
		super.loadInt(location_refractionTexture, 1);
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
