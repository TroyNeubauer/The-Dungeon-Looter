package world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;

import asset.*;
import entity.Entity;
import entity.EntityManager;
import entity.StaticEntity;
import loader.Loader;
import utils.MathUtil;

public class Terrain {

	public static final float SIZE = 200;
	public static final int VERTEX_COUNT = 256;
	public List<Entity> entitiesInTerrain = new ArrayList<Entity>();

	public Texture blendMap;
	private HeightGenerator generator, bassGenerator;

	public final float x, z;
	public final Mesh model;
	public TerrainTexturePack texturePack;
	private EntityCreator entityCreator;

	private final double divideFactor, persistence;
	private final int largestFeature;

	private float[][] heights;
	private float[] vertices;
	private int[] indices;

	public Terrain(int gridX, int gridZ, TerrainTexturePack texturePack, Texture blendMap, double divideFactor, double persistence,
		int largestFeature, long seed, EntityCreator entityCreator) {
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.divideFactor = divideFactor;
		this.persistence = persistence;
		this.largestFeature = largestFeature;
		this.entityCreator = entityCreator;
		this.generator = new HeightGenerator(gridX, gridZ, divideFactor, persistence, largestFeature, seed);
		this.bassGenerator = new HeightGenerator(gridX, gridZ, 2000.0, 1.9, 100, seed / 2l);
		this.model = generateTerrain();
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
			answer = MathUtil.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ], 0),
				new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = MathUtil.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
				new Vector3f(0, heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	private Mesh generateTerrain() {
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
		populate();
		Mesh mesh = new Mesh(Loader.getLoader().loadToVAO(vertices, textureCoords, normals, indices));
		return mesh;
	}

	private void populate() {
		Random random = new Random();
		for (int i = 0; i < SIZE / 10; i++) {
			float x = com.troy.troyberry.math.Maths.randRange(this.x, this.x + SIZE);
			float z = com.troy.troyberry.math.Maths.randRange(this.z, this.z + SIZE);
			StaticEntity e = new StaticEntity(Assets.tree, new Vector3f(x, getHeightOfTerrain(x, z), z));
			e.skipRenderMethod = true;
			EntityManager.addEntity(e);
		}
		for (int i = 0; i < SIZE / 30; i++) {
			float x = com.troy.troyberry.math.Maths.randRange(this.x, this.x + SIZE);
			float z = com.troy.troyberry.math.Maths.randRange(this.z, this.z + SIZE);
			float scale = (float) Maths.gaussian(0.02, 1.0, 0.5);

			float rotX = random.nextBoolean() ? Maths.randRange(-90, -30) : Maths.randRange(30, 90);
			float rotY = Maths.randRange(-180, 180);
			float rotZ = random.nextBoolean() ? Maths.randRange(-90, -30) : Maths.randRange(30, 90);

			StaticEntity e = new StaticEntity(Assets.rock, new Vector3f(x, getHeightOfTerrain(x, z), z), new Vector3f(rotX, rotY, rotZ), scale);
			e.skipRenderMethod = true;
			e.hasNormalMap = true;
			EntityManager.addEntity(e);
		}
	}

	private Vector3f calculateNormal(int x, int z) {
		Vector3f normal = null;
		try {
			float heightL = getHeight(x - 1, z);
			float heightR = getHeight(x + 1, z);
			float heightD = getHeight(x, z - 1);
			float heightU = getHeight(x, z + 1);
			normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		} catch (Exception e) {
			normal = new Vector3f(0, 1, 0);
		}
		normal.normalised();
		return normal;
	}

	private float getHeight(int x, int z) {
		return generator.generateHeight(x, z) + bassGenerator.generateHeight(x, z);
	}

}
