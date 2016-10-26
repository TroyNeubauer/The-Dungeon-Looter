package graphics.postprocessing;

import com.troy.troyberry.math.Vector3f;
import graphics.shader.ShaderProgram;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/graphics/postprocessing/contrast.vert";
	private static final String FRAGMENT_FILE = "src/graphics/postprocessing/contrast.frag";
	private int location_contrast;
	private int location_add;

	public ContrastShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_contrast = super.getUniformLocation("contrast");
		location_add = super.getUniformLocation("add");
	}

	public void loadContrast(float contrast) {
		super.loadFloat(location_contrast, contrast);
	}

	public void loadAdd(Vector3f add) {
		super.loadVector(location_add, add);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
