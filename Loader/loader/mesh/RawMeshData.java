package loader.mesh;

import loader.Loader;

public class RawMeshData {
	
	private float[] vertices, textureCoords, normals, tangents;
	private boolean hastextureCoords, hasNormals, hasTangents, hasIndices;
	private int[] indices;
	private int dimensions = 3;
	
	public Vao load(){
		return Loader.loadToVAO(this);
	}
	
	public RawMeshData(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		hastextureCoords = true;
		this.normals = normals;
		hasNormals = true;
		this.tangents = tangents;
		hasTangents = true;
		this.indices = indices;
		hasIndices = true;
	}
	
	public RawMeshData(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		hastextureCoords = true;
		this.normals = normals;
		hasNormals = true;
		this.indices = indices;
		hasIndices = true;
		
		hasTangents = false;
	}
	
	public RawMeshData(float[] vertices, float[] textureCoords, int[] indices) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		hastextureCoords = true;
		this.indices = indices;
		hasIndices = true;
		
		hasNormals = false;
		hasTangents = false;
	}
	
	public RawMeshData(float[] vertices, float[] textureCoords) {
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		hastextureCoords = true;
		
		hasIndices = false;
		hasNormals = false;
		hasTangents = false;
	}
	
	public RawMeshData(float[] vertices, int dimensions) {
		this.vertices = vertices;
		this.dimensions = dimensions;
		
		hastextureCoords = false;
		hasIndices = false;
		hasNormals = false;
		hasTangents = false;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTangents() {
		return tangents;
	}

	public boolean hastextureCoords() {
		return hastextureCoords;
	}

	public boolean hasNormals() {
		return hasNormals;
	}

	public boolean hasTangents() {
		return hasTangents;
	}

	public boolean hasIndices() {
		return hasIndices;
	}

	public int[] getIndices() {
		return indices;
	}

	public int getDimensions() {
		return dimensions;
	}
	
	
	

}
