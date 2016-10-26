package graphics.font.renderer;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import graphics.shader.ShaderProgram;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/graphics/font/renderer/font.vert";
	private static final String FRAGMENT_FILE = "src/graphics/font/renderer/font.frag";

	private int location_colour;
	private int location_translation;
	private int location_width;
	private int location_edge;
	private int location_boarderWidth;
	private int location_boarderEdge;
	private int location_offset;
	private int location_outlineColor;
	private int location_xSpread;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_colour = super.getUniformLocation("colour");
		location_translation = super.getUniformLocation("translation");

		location_width = super.getUniformLocation("width");
		location_edge = super.getUniformLocation("edge");
		location_boarderWidth = super.getUniformLocation("boarderWidth");
		location_boarderEdge = super.getUniformLocation("boarderEdge");
		location_offset = super.getUniformLocation("offset");
		location_outlineColor = super.getUniformLocation("outlineColor");

		location_xSpread = super.getUniformLocation("xSpread");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	protected void loadColour(Vector3f vector3f) {
		super.loadVector(location_colour, vector3f);
	}

	protected void loadWidth(float width, float edge) {
		super.loadFloat(location_width, width);
		super.loadFloat(location_edge, edge);
	}

	protected void loadOffset(Vector2f offset) {
		super.loadVector(location_offset, offset);
	}

	protected void loadXSpread(float x) {
		super.loadFloat(location_xSpread, x);
	}

	protected void loadShadow(float boarderWidth, float boarderEdge, Vector3f outlineColor) {
		super.loadFloat(location_boarderWidth, boarderWidth);
		super.loadFloat(location_boarderEdge, boarderEdge);
		super.loadVector(location_outlineColor, outlineColor);
	}

	protected void loadTranslation(Vector2f translation) {
		super.loadVector(location_translation, translation);
	}

}
