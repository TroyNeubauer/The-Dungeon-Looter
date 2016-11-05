package world;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.math.Vector4f;

import assets.Assets;
import entity.Camera;
import entity.Entity;
import entity.EntityLiving;
import entity.EntityManager;
import entity.Light;
import graphics.font.loader.GUIText;
import graphics.font.renderer.TextMaster;
import graphics.postprocessing.Fbo;
import graphics.renderer.MasterRenderer;
import graphics.renderer.SplashRenderer;
import graphics.water.WaterFrameBuffers;
import graphics.water.WaterRenderer;
import graphics.water.WaterShader;
import graphics.water.WaterTile;
import input.Controls;
import input.GameSettings;

public class World {

	private DecimalFormat df = new DecimalFormat("00.0");

	public static final float GRAVITY = 0.0017f;
	private final long seed;
	private final int largestFeature;
	private EntityCreator entityCreator;
	private final double divideFactor, persistence;

	private WaterFrameBuffers buffers;
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private List<WaterTile> waters;

	private static GUIText timeText, pmText;

	public float time = 12000f;

	public List<Terrain> allTerrains, loadedTerrains;
	public List<Entity> entities;
	public List<Light> lights;
	public Light sun;

	public World() {
		System.out.println("Loading world");
		Timer t = new Timer();
		timeText = new GUIText("" + time, 1, Assets.font, new Vector2f(0.9f, 0.001f), 1f, false);
		this.seed = new Random().nextLong();
		allTerrains = new ArrayList<Terrain>();
		loadedTerrains = new ArrayList<Terrain>();

		entities = new ArrayList<Entity>();

		lights = new ArrayList<Light>();
		sun = new Light(new Vector3f(1000000, 1500000, -1000000), new Vector3f(1.3f, 1.3f, 1.3f));
		lights.add(sun);

		buffers = new WaterFrameBuffers();
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(waterShader, MasterRenderer.projectionMatrix, buffers);
		waters = new ArrayList<WaterTile>();

		Maths.setSeed(new Random(this.seed).nextLong());

		this.divideFactor = WorldPreset.SMALL_HILLS.divideFactor;
		this.persistence = WorldPreset.SMALL_HILLS.persistence;
		this.largestFeature = WorldPreset.SMALL_HILLS.largestFeature;

		int radius = 1;
		int terrainsToGen = 0;
		for (int z = -radius; z <= radius; z++) {
			for (int x = -radius; x <= radius; x++) {
				terrainsToGen++;
			}
		}
		if (GameSettings.DEBUG) System.out.println("World going to generate " + terrainsToGen + " terrains");
		int counter = 0;
		for (int z = -radius; z <= radius; z++) {
			for (int x = -radius; x <= radius; x++) {
				SplashRenderer.render();
				allTerrains.add(new Terrain(x, z, Assets.texturePack, Assets.blendMap, this.divideFactor, this.persistence, this.largestFeature,
					this.seed, entityCreator));
				counter++;
				System.out.println("generating world " + df.format(((double) counter / (double) terrainsToGen) * 100.0) + "%");
			}
		}
		System.out.println("Generating world done!  Took " + t.getTime());
		flushEntityQuoe();
	}

	public Terrain getTerrain(Entity e) {
		return getTerrain(e.position.x, e.position.z);
	}

	public Terrain getTerrain(float x, float z) {
		for (Terrain terrain : allTerrains) {
			if (Maths.inRange(x, terrain.x, terrain.x + Terrain.SIZE) && Maths.inRange(z, terrain.z, terrain.z + Terrain.SIZE)) {
				return terrain;
			}
		}
		return null;
	}

	double angle = 0.0;

	public void update() {
		float dx = (float) (Math.cos(Math.toRadians(angle))) / 200;
		float dz = (float) (Math.sin(Math.toRadians(angle))) / 200;
		angle += 0.5;
		if (Controls.TOGGLE_HOUR.hasBeenPressed()) {
			GameSettings.CLOCK_24_HOUR = !GameSettings.CLOCK_24_HOUR;
		}
		if (WorldLoader.hasUpdate()) {
			try {
				this.loadedTerrains = WorldLoader.getUpdate();
				WorldLoader.completedRequest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		time += 8.33333333f;
		waterRenderer.update();
		flushEntityQuoe();
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}

	public void render(Fbo postProcessingFbo) {
		flushDeadEntityQuoe();
		TextMaster.removeText(timeText);
		TextMaster.removeText(pmText);
		timeText = new GUIText(getTime(), GameSettings.FONT_SIZE + 0.2f, Assets.font, new Vector2f(0.001f, 0.96f), 1f, false);
		pmText = new GUIText(getPM(), GameSettings.FONT_SIZE + 0.2f, Assets.font, new Vector2f(0.056f, 0.96f), 1f, false);
		TextMaster.loadText(timeText);
		TextMaster.loadText(pmText);
		double renderDistance = GameSettings.RENDER_DISTANCE;
		List<Entity> coppiedEntities = new ArrayList<Entity>(entities);

		for (WaterTile water : waters) {
			if (Maths.getDistanceBetweenPoints(water.x, water.z, Camera.getCamera().position.x, Camera.getCamera().position.z) > renderDistance)
				continue;
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (Camera.getCamera().position.y - water.height);
			Camera dumyCamera = Camera.copyCamera(Camera.getCamera());
			dumyCamera.position.y -= distance;
			dumyCamera.invertPitch();
			MasterRenderer.renderScene(coppiedEntities, loadedTerrains, lights, dumyCamera, new Vector4f(0, 1, 0, -water.height + 1), renderDistance);

			buffers.bindRefractionFrameBuffer();
			MasterRenderer.renderScene(coppiedEntities, loadedTerrains, lights, Camera.getCamera(), new Vector4f(0, -1, 0, water.height),
				renderDistance);

		}
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		buffers.unbindCurrentFrameBuffer();
		postProcessingFbo.bindFrameBuffer();

		MasterRenderer.renderScene(coppiedEntities, loadedTerrains, lights, Camera.getCamera(), new Vector4f(0, -1, 0, 100000), renderDistance);
		waterRenderer.render(waters, sun);
		postProcessingFbo.unbindFrameBuffer();
	}

	public void cleanUp() {
		buffers.cleanUp();
		waterShader.cleanUp();
	}

	public String getTime() {
		if (GameSettings.CLOCK_24_HOUR) {
			int hour = (int) (time / 1000);
			String minuites = df.format((int) time % 1000 * 60 / 1000);
			return (hour) + ":" + minuites.substring(0, minuites.lastIndexOf('.'));
		} else {
			int hour = (int) (time / 1000);
			String minuites = df.format((int) time % 1000 * 60 / 1000);
			if (hour == 0) hour = 12;
			if (hour >= 13) {
				hour -= 12;
			}
			return (hour) + ":" + minuites.substring(0, minuites.lastIndexOf('.'));
		}
	}

	public String getPM() {
		if (GameSettings.CLOCK_24_HOUR) {
			return "";
		}
		return ((time >= 13000) ? "PM" : "AM");
	}

	public float getHeight(float x, float z) {
		for (Terrain terrain : loadedTerrains) {
			if (Maths.inRange(x, terrain.x, terrain.x + Terrain.SIZE) && Maths.inRange(z, terrain.z, terrain.z + Terrain.SIZE)) {
				return terrain.getHeightOfTerrain(x, z);
			}
		}
		return 0;
	}

	public float getSlowHeight(float x, float z) {
		for (Terrain terrain : allTerrains) {
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

	public void flushEntityQuoe() {
		for (Entity entity : EntityManager.getEntitiesToAdd()) {
			entities.add(entity);
		}
		EntityManager.clear();
	}

	public void flushDeadEntityQuoe() {
		for (EntityLiving entity : EntityLiving.onDead) {
			entity.onDeath();
			entity.isDead = true;
		}
		EntityLiving.onDead.clear();
	}

	public static int highestPoint(double persistence) {
		double y = ((2380625 * persistence * persistence) / 20601 - (3170525 * persistence) / 27468 + (353135 / 164808));

		return (int) Maths.clamp(1.0, 999999.0, y);
	}

}
