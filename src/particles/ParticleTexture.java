package particles;

import graphics.Texture;

public class ParticleTexture {

	public final int numberOfRows;
	public final Texture texture;

	public ParticleTexture(Texture texture, int numberOfRows) {
		if (texture == null) System.err.println("Texture is NULL!!!!");
		this.texture = texture;
		this.numberOfRows = numberOfRows;
	}

}
