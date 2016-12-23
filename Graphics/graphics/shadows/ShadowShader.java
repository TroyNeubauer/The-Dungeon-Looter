package graphics.shadows;

import com.troy.troyberry.math.Matrix4f;
import graphics.shader.ShaderProgram;

public class ShadowShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/graphics/shadows/shadow.vert";
	private static final String FRAGMENT_FILE = "/graphics/shadows/shadow.frag";

	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");

	}

	protected void loadMvpMatrix(Matrix4f mvpMatrix) {
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}

}
