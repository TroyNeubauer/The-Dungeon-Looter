package graphics.fontrendering;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;

import graphics.shader.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/graphics/fontrendering/font.vert";
	private static final String FRAGMENT_FILE = "/graphics/fontrendering/font.frag";
	
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
	
	protected void loadColour(Vector3f colour){
		super.loadVector(location_colour, colour);
	}
	
	protected void loadTranslation(Vector2f translation){
		super.loadVector(location_translation, translation);
	}


}
