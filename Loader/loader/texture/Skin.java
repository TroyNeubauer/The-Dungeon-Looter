package loader.texture;

public class Skin {
	
	private Texture2D diffuse, normalMap, extraInfoMap;
	private boolean hasDiffuse = true;
	private boolean hasNormalMap, hasExtraInfoMap;
	private float shineDampener, reflectivity;
	
	public Skin(Texture2D diffuse) {
		this.diffuse = diffuse;
		this.hasNormalMap = false;
		this.hasExtraInfoMap = false;
	}
	
	public Skin(Texture2D diffuse, Texture2D normalMap) {
		this.diffuse = diffuse;
		this.normalMap = normalMap;
		this.hasNormalMap = true;
		this.hasExtraInfoMap = false;
	}
	
	public Skin(Texture2D diffuse, Texture2D normalMap, Texture2D extraInfoMap) {
		this.diffuse = diffuse;
		this.normalMap = normalMap;
		this.extraInfoMap = extraInfoMap;
		this.hasNormalMap = true;
		this.hasExtraInfoMap = true;
	}
	
	public void setShineValues(float shineDampener, float reflectivity){
		this.shineDampener = shineDampener;
		this.reflectivity = reflectivity;
	}
	
	public Texture2D getDiffuse() {
		return diffuse;
	}
	
	public Texture2D getTexture() {
		return diffuse;
	}
	
	public Texture2D getNormalMap() {
		return normalMap;
	}
	
	public Texture2D getExtraInfoMap() {
		return extraInfoMap;
	}
	
	public boolean hasDiffuse() {
		return hasDiffuse;
	}
	
	public boolean hasExtraInfoMap() {
		return hasExtraInfoMap;
	}
	
	public boolean hasNormalMap() {
		return hasNormalMap;
	}
	
	public float getShineDamper() {
		return shineDampener;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}

}
