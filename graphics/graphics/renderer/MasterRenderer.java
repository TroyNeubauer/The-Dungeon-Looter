package graphics.renderer;

import java.util.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.troy.troyberry.math.*;

import asset.TexturedModel;
import camera.FirstPersonCamera;
import camera.ICamera;
import entity.*;
import entity.light.Light;
import graphics.shader.StaticShader;
import graphics.shader.TerrainShader;
import graphics.shadows.ShadowMapMasterRenderer;
import graphics.sky.SkyMaster;
import world.Terrain;
import world.World;

public class MasterRenderer {

	public static final float RED = 119f / 256f;
	public static final float GREEN = 169f / 256f;
	public static final float BLUE = 254f / 256f;

	private static StaticShader shader = new StaticShader();
	private static EntityRenderer renderer;

	private static TerrainRenderer terrainRenderer;
	private static TerrainShader terrainShader = new TerrainShader();

	private static ShadowMapMasterRenderer shadowMapRenderer;

	private static NormalMappingRenderer normalMapRenderer;

	private static Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private static Map<TexturedModel, List<Entity>> normalMapEntities = new HashMap<TexturedModel, List<Entity>>();
	private static List<Terrain> terrains = new ArrayList<Terrain>();

	public static void init(World world, ICamera camera) {
		enableCulling();
		
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
		renderer = new EntityRenderer(shader, camera.getProjectionMatrix());
		terrainRenderer = new TerrainRenderer(terrainShader, camera.getProjectionMatrix());
		
		SkyMaster.init(world, camera.getProjectionMatrix());
		normalMapRenderer = new NormalMappingRenderer(camera.getProjectionMatrix());
	}

	public static void setWorld(World world) {
		SkyMaster.world = world;
	}

	public static void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights, ICamera camera, Vector4f clipPlane,
		double renderDistance) {
		for (Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			if (Maths.approximateDistanceBetweenPoints(entity.position.x, entity.position.z, camera.getPosition().x, camera.getPosition().z) > renderDistance)
				continue;
			if (entity.hasNormalMap) {
				processNormalMapEntity(entity);
			} else {
				processEntity(entity);
			}
		}
		render(lights, camera, clipPlane);
	}

	public static void render(List<Light> lights, ICamera camera, Vector4f clipPlane) {
		prepare();
		disableCulling();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
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
		SkyMaster.render(camera, RED, GREEN, BLUE);
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

	public static void renderShadowMap(List<Entity> entitList, Light sun) {
		for (Entity entity : entitList) {
			if (entity.hasNormalMap) {
				processNormalMapEntity(entity);
			} else {
				processEntity(entity);
			}
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
}
