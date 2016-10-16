package graphics.gui;

import com.troy.troyberry.math.Matrix4f;
import graphics.shader.ShaderProgram;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/graphics/gui/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/graphics/gui/guiFragmentShader.txt";

	private int location_transformationMatrix;

	public GuiShader() {
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
