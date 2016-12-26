package loader;

import loader.mesh.Mesh;
import loader.texture.Skin;
import loader.texture.Texture2D;

public class CompleteModel {
	
	private Mesh mesh;
	private Skin skin;

	
	public CompleteModel(Mesh model, Skin skin){
		this.mesh = model;
		this.skin = skin;
	}
	
	public Skin getSkin() {
		return skin;
	}

	public Mesh getRawModel() {
		return mesh;
	}

	public Texture2D getTexture() {
		return skin.getTexture();
	}
	
	public Texture2D getNormalMap(){
		return skin.getNormalMap();
	}
	
	public Texture2D getExtraInfoMap(){
		return skin.getExtraInfoMap();
	}
	
	public boolean hasNormalMap(){
		return skin.hasNormalMap();
	}

	public boolean hasExtraInfoMap(){
		return skin.hasExtraInfoMap();
	}
	
}
