package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector4f;
import entity.Camera;
import entity.Entity;
import entity.Light;
import graphics.TexturedModel;
import graphics.shader.StaticShader;
import graphics.shader.TerrainShader;
import graphics.shadows.ShadowMapMasterRenderer;
import graphics.skybox.SkyboxRenderer;
import world.Terrain;
import world.World;

public class MasterRenderer {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000;

	public static final float RED = 119f / 256f;
	public static final float GREEN = 169f / 256f;
	public static final float BLUE = 254f / 256f;

	public static Matrix4f projectionMatrix;

	private static StaticShader shader = new StaticShader();
	private static EntityRenderer renderer;

	private static TerrainRenderer terrainRenderer;
	private static TerrainShader terrainShader = new TerrainShader();

	private static ShadowMapMasterRenderer shadowMapRenderer;

	private static NormalMappingRenderer normalMapRenderer;

	private static SkyboxRenderer skyboxRenderer;

	private static Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private static Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private static List<Terrain> terrains = new ArrayList<Terrain>();

	public static void init() {
		enableCulling();
		createProjectionMatrix();
		shadowMapRenderer = new ShadowMapMasterRenderer();
		renderer = new EntityRenderer(shader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(projectionMatrix);
		normalMapRenderer = new NormalMappingRenderer(projectionMatrix);
	}

	public static void setWorld(World world) {
		skyboxRenderer.world = world;
	}

	public static void renderScene(List<Entity> entities, List<Entity> normalEntities, List<Terrain> terrains, List<Light> lights, Camera camera,
		Vector4f clipPlane, double renderDistance) {
		for (Terrain terrain : terrains) {
			if (Maths.approximateDistanceBetweenPoints(terrain.x + Terrain.SIZE / 2.0, terrain.z + Terrain.SIZE / 2.0, camera.position.x,
				camera.position.z) > renderDistance) continue;
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			if (Maths.approximateDistanceBetweenPoints(entity.position.x, entity.position.z, camera.position.x, camera.position.z) > renderDistance)
				continue;
			processEntity(entity);
		}
		for (Entity entity : normalEntities) {
			if (Maths.approximateDistanceBetweenPoints(entity.position.x, entity.position.z, camera.position.x, camera.position.z) > renderDistance)
				continue;
			processNormalMapEntity(entity);
		}
		render(lights, camera, clipPlane);
	}

	public static void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		disableCulling();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities, shadowMapRenderer.getToShadowMapSpaceMatrix());
		shader.stop();
		normalMapRenderer.render(normalMapEntities, clipPlane, lights, camera, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		enableCulling();
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains, shadowMapRenderer.getToShadowMapSpaceMatrix());
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
		normalMapEntities.clear();
	}

	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		disableCulling();
	}

	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public static void processEntity(Entity entity) {
		TexturedModel entityModel = entity.model;
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public static void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public static void processNormalMapEntity(Entity entity) {
		TexturedModel entityModel = entity.model;
		List<Entity> batch = normalMapEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			normalMapEntities.put(entityModel, newBatch);
		}
	}

	public static void renderShadowMap(List<Entity> entitList, List<Entity> normalEntities, Light sun) {
		for (Entity entity : entitList) {
			processEntity(entity);
		}
		for (Entity entity : normalEntities) {
			processEntity(entity);
		}
		shadowMapRenderer.render(entities, sun);
		entities.clear();
		normalMapEntities.clear();
	}

	public static int getShadowMapTexture() {
		return shadowMapRenderer.getShadowMap();
	}

	public static void cleanUp() {
		shadowMapRenderer.cleanUp();
		shader.cleanUp();
		terrainShader.cleanUp();
		normalMapRenderer.cleanUp();
	}

	public static void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMapTexture());
	}

	private static void createProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}

}
