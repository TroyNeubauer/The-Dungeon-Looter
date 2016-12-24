package graphics.sky;

import com.troy.troyberry.math.Matrix4f;

import graphics.shader.ShaderProgram;

public class SunShader extends ShaderProgram {
	
	private static final String VERT_PATH = "/graphics/sky/sun.vert", FRAG_PATH = "/graphics/sky/sun.frag";
	
	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public SunShader() {
		super(VERT_PATH, FRAG_PATH);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	protected void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	protected void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
