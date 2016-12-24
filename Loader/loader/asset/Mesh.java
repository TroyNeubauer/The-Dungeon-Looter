package loader.asset;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;

import loader.Loader;
import thedungeonlooter.input.GameSettings;

public class Mesh extends Asset {

	private static final String RES_LOC = "./res/objects/";

	private int vertexCount;

	public Mesh(int GLID, int vertexCount) {
		super(GLID);
		this.vertexCount = vertexCount;
	}

	public Mesh(String path) {
		super(path);
	
	}

	public Mesh(MeshData data) {
		super(data.vaoID);
		this.vertexCount = data.vertexCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public static MeshData loadObjModel(String fileName) {
		FileReader fr = null;
		int lineNumber = 0;
		String path = RES_LOC + fileName + ".obj";
		try {
			fr = new FileReader(new File(path));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		if (GameSettings.DEBUG)
			System.out.println("Loading model " + path);
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try {

			while (true) {
				lineNumber++;
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
							Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					textureArray = new float[vertices.size() * 2];
					normalsArray = new float[vertices.size() * 3];
					break;
				}
			}

			while (line != null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");

				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();

			verticesArray = new float[vertices.size() * 3];
			indicesArray = new int[indices.size()];

			int vertexPointer = 0;
			for (Vector3f vertex : vertices) {
				verticesArray[vertexPointer++] = vertex.x;
				verticesArray[vertexPointer++] = vertex.y;
				verticesArray[vertexPointer++] = vertex.z;
			}

			for (int i = 0; i < indices.size(); i++) {
				indicesArray[i] = indices.get(i);
			}
		} catch (Exception e) {
			System.err.println("Error loading object file " + fileName + " on line " + lineNumber);
		}
		System.out.println("Loading model " + fileName);
		return Loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);

	}

	private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures,
			List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTex.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		normalsArray[currentVertexPointer * 3] = currentNorm.x;
		normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}

	@Override
	public Asset load() {
		loaded = true;
		MeshData mesh = loadObjModel(path);
		this.GLID = mesh.vaoID;
		this.vertexCount = mesh.vertexCount;

		return this;
	}

	@Override
	public void delete() {
		GL30.glDeleteVertexArrays(GLID);
	}

}
