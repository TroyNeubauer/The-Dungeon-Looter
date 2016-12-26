package loader.texture;

import com.troy.troyberry.util.MyFile;

public class TextureAtlas2D extends Texture2D {
	
	private int numberOfRows;

	public TextureAtlas2D(MyFile path, TextureBuilder builder, int numberOfRows) {
		super(path, builder);
		this.numberOfRows = numberOfRows;
	}

	public TextureAtlas2D(MyFile path, int numberOfRows) {
		super(path);
		this.numberOfRows = numberOfRows;
	}

	public TextureAtlas2D(String path, int numberOfRows) {
		super(path);
		this.numberOfRows = numberOfRows;
	}

	public TextureAtlas2D(String path, TextureBuilder builder, int numberOfRows) {
		super(path, builder);
		this.numberOfRows = numberOfRows;
	}
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

}
