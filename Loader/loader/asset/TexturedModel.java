package loader.asset;

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
	
	public boolean isLoaded(){
		return mesh.loaded && texture.loaded;
	}

}
