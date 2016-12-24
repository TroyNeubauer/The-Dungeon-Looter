package loader.asset;

import java.nio.IntBuffer;

public class TextureData {

	public int width;
	public int height;
	public IntBuffer buffer;

	public TextureData(IntBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
}
