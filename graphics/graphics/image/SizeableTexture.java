package graphics.image;

import com.troy.troyberry.math.Vector2f;

public class SizeableTexture {

	public final int texture;
	public Vector2f position;
	public Vector2f scale;

	public SizeableTexture(int texture, Vector2f position, Vector2f scale) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
}
