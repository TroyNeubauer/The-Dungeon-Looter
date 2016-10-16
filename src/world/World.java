package world;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.math.Vector4f;
import entity.Camera;
import entity.Entity;
import entity.Light;
import entity.StaticEntity;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
import graphics.Assets;
import graphics.water.WaterFrameBuffers;
import graphics.water.WaterRenderer;
import graphics.water.WaterShader;
import graphics.water.WaterTile;
import input.GameSettings;
import postProcessing.Fbo;
import renderEngine.Loader;
import renderEngine.MasterRenderer;

public class World {

	private Map<Entity, Boolean> entitiesToAdd = new HashMap<Entity, Boolean>();
	private DecimalFormat df = new DecimalFormat("00.0");

	public static final float GRAVITY = 0.0021f;
	private final long seed;
	private final int largestFeature;
	private final double divideFactor, persistence;

	private WaterFrameBuffers buffers;
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private List<WaterTile> waters;

	private static GUIText timeText;

	public float time = 12000f;

	public List<Terrain> terrains;
	public List<Entity> entities;
	public List<Light> lights;
	public List<Entity> normalMapEntities;
	public Light sun;

	public World(Loader loader, MasterRenderer renderer) {
		System.out.println("Loading world");
		Timer t = new Timer();
		timeText = new GUIText("" + time, 1, Assets.debugFont, new Vector2f(0.9f, 0.001f), 1f, false);
		this.seed = new Random().nextLong();
		terrains = new ArrayList<Terrain>();

		entities = new ArrayList<Entity>();
		normalMapEntities = new ArrayList<Entity>();

		lights = new ArrayList<Light>();
		sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		buffers = new WaterFrameBuffers();
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		waters = new ArrayList<WaterTile>();

		Maths.setSeed(new Random(this.seed).nextLong());
		//this.divideFactor = Maths.randRange(20.0, 140.0);
		//this.persistence = Maths.randRange(0.8, 1.5);
		//this.largestFeature = Maths.randRange(20, 150);

		this.divideFactor = 140.0;
		this.persistence = 1.5;
		this.largestFeature = 100;

		int raduis = 5;
		int terrainsToGen = 0;
		for (int z = -raduis; z <= raduis; z++) {
			for (int x = -raduis; x <= raduis; x++) {
				terrainsToGen++;
			}
		}
		int counter = 0;
		for (int z = -raduis; z <= raduis; z++) {
			for (int x = -raduis; x <= raduis; x++) {
				terrains.add(new Terrain(x, z, loader, Assets.texturePack, Assets.blendMap, this.divideFactor, this.persistence, this.largestFeature,
					this.seed));
				counter++;
				System.out.println("generating terrain " + ((double) counter / (double) terrainsToGen));
			}
		}
		System.out.println("Generating world done!  Took " + t.getTime());
	}

	public void populate() {
		for (int loop = 0; loop < terrains.size(); loop++) {
			Terrain chunk = terrains.get(loop);
			Random random = new Random();
			for (int i = 0; i < Terrain.SIZE / 10; i++) {
				float x = Maths.randRange(0, Terrain.SIZE), z = Maths.randRange(0, Terrain.SIZE);
				x += chunk.x;
				z += chunk.z;
				float y = this.getHeight(x, z);
				if (y < 0) {
					i--;
					continue;
				}
				new StaticEntity(Assets.rock, new Vector3f(x, y, z), new Vector3f(Maths.randRange(-75, 75), Maths.randRange(0, 360), 0),
					Maths.randRange(0.05f, 0.45f), true);
			}
			for (int i = 0; i < Terrain.SIZE / 10; i++) {
				float x = Maths.randRange(0, Terrain.SIZE), z = Maths.randRange(0, Terrain.SIZE);
				x += chunk.x;
				z += chunk.z;
				float y = this.getHeight(x, z);
				if (y < 0) {
					i--;
					continue;
				}
				new StaticEntity(Assets.tree, new Vector3f(x, y, z), new Vector3f(0, Maths.randRange(0, 360), 0), Maths.randRange(0.25f, 1.3f),
					false);
			}
		}
	}

	public Terrain getTerrain(Entity e) {
		return getTerrain(e.position.x, e.position.z);
	}

	public Terrain getTerrain(float x, float z) {
		for (Terrain terrain : terrains) {
			if (Maths.inRange(x, terrain.x, terrain.x + Terrain.SIZE) && Maths.inRange(z, terrain.z, terrain.z + Terrain.SIZE)) {
				return terrain;
			}
		}
		return null;
	}

	public void update() {
		time += 0.33333333f;
		waterRenderer.update();
		Iterator<Entry<Entity, Boolean>> iterator = entitiesToAdd.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Entity, Boolean> pair = (Map.Entry<Entity, Boolean>) iterator.next();
			Entity e = (Entity) pair.getKey();
			boolean normalMapped = (boolean) pair.getValue();
			if (normalMapped) {
				normalMapEntities.add(e);
			} else {
				entities.add(e);
			}
			iterator.remove();
		}
		entitiesToAdd.clear();
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update(this);
		}
	}

	public void render(MasterRenderer renderer, Camera camera, Fbo postProcessingFbo) {
		TextMaster.removeText(timeText);
		timeText = new GUIText(getTime(), GameSettings.FONT_SIZE + 0.2f, Assets.debugFont, new Vector2f(0.91f, 0.96f), 1f, false);
		TextMaster.loadText(timeText);
		double renderDistance = GameSettings.RENDER_DISTANCE;

		for (WaterTile water : waters) {
			if (Maths.getDistanceBetweenPoints(water.x, water.z, camera.position.x, camera.position.z) > renderDistance) continue;
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.position.y - water.height);
			Camera dumyCamera = Camera.copyCamera(camera);
			dumyCamera.position.y -= distance;
			dumyCamera.invertPitch();
			renderer.renderScene(new ArrayList<Entity>(entities), new ArrayList<Entity>(normalMapEntities), terrains, lights, dumyCamera,
				new Vector4f(0, 1, 0, -water.height + 1), renderDistance);

			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(new ArrayList<Entity>(entities), new ArrayList<Entity>(normalMapEntities), terrains, lights, camera,
				new Vector4f(0, -1, 0, water.height), renderDistance);

		}
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.unbindCurrentFrameBuffer();
		postProcessingFbo.bindFrameBuffer();

		renderer.renderScene(new ArrayList<Entity>(entities), new ArrayList<Entity>(normalMapEntities), terrains, lights, camera,
			new Vector4f(0, -1, 0, 100000), renderDistance);
		waterRenderer.render(waters, camera, sun);
		postProcessingFbo.unbindFrameBuffer();
	}

	public void cleanUp() {
		buffers.cleanUp();
		waterShader.cleanUp();
	}

	public void addEntity(Entity entity, boolean normalMapped) {

		entitiesToAdd.put(entity, normalMapped);
	}

	public String getTime() {
		String minuites = df.format((int) time % 1000 * 60 / 1000);
		return ((int) time / 1000 % 13) + ":" + minuites.substring(0, minuites.lastIndexOf('.')) + " " + ((time >= 12000) ? "PM" : "AM");
	}

	public float getHeight(float x, float z) {
		for (Terrain terrain : terrains) {
			if (Maths.inRange(x, terrain.x, terrain.x + Terrain.SIZE) && Maths.inRange(z, terrain.z, terrain.z + Terrain.SIZE)) {
				return terrain.getHeightOfTerrain(x, z);
			}
		}

		return 0;
	}

	public void printGenStats() {
		System.out.println("World seed is " + this.seed);
		System.out.println("World divide factor is " + this.divideFactor);
		System.out.println("World presistence is " + this.persistence);
		System.out.println("World's largest feature is " + this.largestFeature);
	}

}
