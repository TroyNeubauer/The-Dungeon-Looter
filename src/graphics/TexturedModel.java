package graphics;

public class TexturedModel {
	
	private Mesh mesh;
	private Texture texture;

	
	public TexturedModel(Mesh model, Texture texture){
		this.mesh = model;
		this.texture = texture;
	}

	public Mesh getRawModel() {
		return mesh;
	}

	public Texture getTexture() {
		return texture;
	}

}
