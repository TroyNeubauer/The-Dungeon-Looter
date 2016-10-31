package graphics;

public class TerrainTexture {

	private int textureID;

	public TerrainTexture(Texture textureID) {
		this.textureID = textureID.id;
	}

	public int getTextureID() {
		return textureID;
	}

}
