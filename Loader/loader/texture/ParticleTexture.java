package loader.texture;

import com.troy.troyberry.util.MyFile;

public class ParticleTexture extends TextureAtlas2D {

	public ParticleTexture(MyFile path, TextureBuilder builder,
			int numberOfRows) {
		super(path, builder, numberOfRows);
	}

	public ParticleTexture(MyFile path, int numberOfRows) {
		super(path, numberOfRows);
	}

	public ParticleTexture(String path, int numberOfRows) {
		super(path, numberOfRows);
	}

	public ParticleTexture(String path, TextureBuilder builder,
			int numberOfRows) {
		super(path, builder, numberOfRows);
	}

}
