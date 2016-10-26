package graphics.font.renderer;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import graphics.shader.ShaderProgram;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/graphics/font/renderer/fontVertex.txt";
	private static final String FRAGMENT_FILE = "src/graphics/font/renderer/fontFragment.txt";

	private int location_colour;
	private int location_translation;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColour(Vector3f vector3f) {
		super.loadVector(location_colour, vector3f);
	}

	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(location_translation, translation);
	}

}
