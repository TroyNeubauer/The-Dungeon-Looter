package thedungeonlooter.world;

import java.text.DecimalFormat;
import java.util.*;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.math.*;
import com.troy.troyberry.opengl.util.Window;

import graphics.fontcreator.GUIText;
import graphics.particle.ParticleMaster;
import graphics.particle.system.SnowFlakeSystem;
import graphics.renderer.MasterRenderer;
import graphics.renderer.SplashRenderer;
import graphics.sky.SkyMaster;
import graphics.water.WaterTile;
import loader.asset.Assets;
import thedungeonlooter.camera.ICamera;
import thedungeonlooter.entity.*;
import thedungeonlooter.entity.light.Light;
import thedungeonlooter.gamestate.WorldState;
import thedungeonlooter.input.Controls;
import thedungeonlooter.input.GameSettings;

public class World {
	private static World INSTANCE;

	private DecimalFormat df = new DecimalFormat("00.0");
	private DebugMenu d;

	public static final float GRAVITY = 0.0017f;
	private long seed;
	private int largestFeature;
	private EntityCreator entityCreator;
	private double divideFactor, persistence;

	public float time = 4500f, blendFactor, renderTime = 0.0f;
	public static final float FULL_DAY_LENGTH = 24000, END_SUNRISE = 8000, START_SUNSET = 19500, END_SUNSET = 21000, START_SUNRISE = 5500;
	public static final float SUN_OUT_DAY_LENGTH = ((START_SUNSET + END_SUNSET) / 2f) - ((START_SUNRISE + END_SUNRISE) / 2f);
	public static final float SUNRISE_LENGTH = END_SUNRISE - START_SUNRISE, SUNSET_LENGTH = END_SUNSET - START_SUNSET;
	
	private GUIText timeText = new GUIText("", 1.5f, Assets.font, new Vector2f(0.85f, 0.96f), 1.0f, false);
	public List<Terrain> allTerrains, loadedTerrains;
	public List<Entity> entities;
	public List<Light> lights;
	public Light sun;
	public List<WaterTile> waterTiles;

	public World() {
		System.out.println("Loading world");
		this.seed = new Random().nextLong();
		allTerrains = new ArrayList<Terrain>();
		loadedTerrains = new ArrayList<Terrain>();

		entities = new ArrayList<Entity>();
		waterTiles = new ArrayList<WaterTile>();

		lights = new ArrayList<Light>();
		sun = new Light(new Vector3f(0, -1000, 0), new Vector3f(1.0f, 1.0f, 1.0f));
		lights.add(sun);
		
		generateWorld();
		
		DebugMenu.init(this);
		INSTANCE = this;
	}

	private void generateWorld() {
		Timer t = new Timer();
		Maths.setSeed(new Random(this.seed).nextLong());

		this.divideFactor = WorldPreset.SMALL_HILLS.divideFactor;
		this.persistence = WorldPreset.SMALL_HILLS.persistence;
		this.largestFeature = WorldPreset.SMALL_HILLS.largestFeature;

		int radius = 3;
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
		ParticleMaster.addSystem(new SnowFlakeSystem(WorldState.getPlayer()));
		
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

	public void update() {
		updateLights();
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
		time += 1.333;
		time %= FULL_DAY_LENGTH;
		Vector3f sunColor = new Vector3f(1.1f - blendFactor * 0.7f, 1.1f - blendFactor * 0.7f, 1.1f - blendFactor * 0.7f);
		sun.setColor(sunColor);
		
		flushEntityQuoe();
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}
	
	public void updateLights(){
		double angle = (time - (START_SUNRISE + END_SUNRISE - 1000.0) / 2.0) / ((END_SUNSET + END_SUNRISE) / 2 / Math.PI);
		float sunX = (float) Math.cos(angle) * SkyMaster.SUN_DISTANCE;
		float sunY = (float) Math.sin(angle) * SkyMaster.SUN_DISTANCE;
		sun.getPosition().x = sunX + WorldState.getPlayer().position.x;
		sun.getPosition().y = sunY + WorldState.getPlayer().position.y;
	}

	public void render(ICamera camera, Vector4f clippingPlane) {
		timeText.setText(getTime() + " " + getPM());
		timeText.setColor(blendFactor);
		renderTime += Window.getFrameTimeSeconds();
		if(renderTime >= 1.0f){
			renderTime = 0.0f;
			ParticleMaster.addSystem(new SnowFlakeSystem(WorldState.getPlayer()));
		}
		DebugMenu.render();
		flushDeadEntityQuoe();
		double renderDistance = GameSettings.RENDER_DISTANCE;
		List<Entity> coppiedEntities = new ArrayList<Entity>(entities);
		
		MasterRenderer.renderScene(coppiedEntities, loadedTerrains, lights, camera, clippingPlane, renderDistance);
	}

	public void cleanUp() {
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
			entity.kill();
		}
		EntityLiving.onDead.clear();
	}

	public static int highestPoint(double persistence) {
		double y = ((2380625 * persistence * persistence) / 20601 - (3170525 * persistence) / 27468 + (353135 / 164808));

		return (int) Maths.clamp(1.0, 999999.0, y);
	}

	public static World getInstance() {
		return INSTANCE;
	}

}
