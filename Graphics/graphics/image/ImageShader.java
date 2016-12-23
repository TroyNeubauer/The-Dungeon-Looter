package graphics.image;

import com.troy.troyberry.math.Matrix4f;
import graphics.shader.ShaderProgram;

public class ImageShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/graphics/image/image.vert";
	private static final String FRAGMENT_FILE = "/graphics/image/image.frag";

	private int location_transformationMatrix;

	public ImageShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
