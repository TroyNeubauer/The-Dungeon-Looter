package world;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import graphics.Mesh;
import graphics.TerrainTexture;
import graphics.TerrainTexturePack;
import renderEngine.Loader;
import toolbox.Maths;

public class Terrain {

	public static final float SIZE = 200;
	public static final int VERTEX_COUNT = 256;

	public TerrainTexture blendMap;
	private HeightGenerator generator;

	public final float x, z;
	public final Mesh model;
	public TerrainTexturePack texturePack;

	private float[][] heights;
	private float[] vertices;
	private int[] indices;

	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, float divideFactor, long seed) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.generator = new HeightGenerator(gridX, gridZ, seed, divideFactor);
		this.model = generateTerrain(loader);
	}

	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;

		if (xCoord <= (1 - zCoord)) {
			answer = Maths.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0),
				new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
				new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	private Mesh generateTerrain(Loader loader) {

		int count = VERTEX_COUNT * VERTEX_COUNT;
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT * 1)];
		int vertexPointer = 0;
		for (int z = 0; z < VERTEX_COUNT; z++) {
			for (int x = 0; x < VERTEX_COUNT; x++) {
				heights[x][z] = getHeight(x, z);
			}
		}
		for (int z = 0; z < VERTEX_COUNT; z++) {
			for (int x = 0; x < VERTEX_COUNT; x++) {
				vertices[vertexPointer * 3] = (float) x / ((float) VERTEX_COUNT - 1) * SIZE;
				float height = heights[x][z];
				vertices[vertexPointer * 3 + 1] = height;
				heights[x][z] = height;
				vertices[vertexPointer * 3 + 2] = (float) z / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(x, z);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) x / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) z / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	private Vector3f calculateNormal(int x, int z) {
		Vector3f normal = null;
		try {
			float heightL = heights[x - 1][z];
			float heightR = heights[x + 1][z];
			float heightD = heights[x][z - 1];
			float heightU = heights[x][z + 1];
			normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		} catch (Exception e) {
			normal = new Vector3f(0, 1, 0);
		}
		normal.normalised();
		return normal;
	}

	private float getHeight(int x, int z) {
		return generator.generateHeight(x, z);
	}

}
