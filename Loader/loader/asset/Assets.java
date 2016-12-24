package loader.asset;

import java.util.LinkedList;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.util.CrashReport;

import graphics.fontcreator.FontType;
import graphics.renderer.SplashRenderer;
import loader.mesh.NormalMappedObjLoader;

public class Assets {

	private static LinkedList<Asset> toLoad = new LinkedList<Asset>(), allAssets = new LinkedList<Asset>();
	private static Object lock = new Object();

	public static Texture backgroundTexture, rTexture, gTexture, bTexture;
	public static Texture loadingTexture;

	private static Mesh personMesh, treeMesh, lampModel;
	public static TexturedModel person, rock, tree, lamp;
	public static Texture smokeParticle, snowflake;
	
	public static FontType font;

	public static TerrainTexturePack texturePack;
	public static Texture blendMap;

	public static void init() {
		try {
			System.out.println("Loading assets  ");
			Timer t = new Timer();

			personMesh = new Mesh("person");
			treeMesh = new Mesh("pine");
			lampModel = new Mesh("lamp");
			
			snowflake = new Texture("snowflake", false);
			snowflake.setNumberOfRows(1);
			snowflake.load();
			
			smokeParticle = new Texture("smoke", false);
			smokeParticle.setNumberOfRows(4);
			smokeParticle.load();
			rock = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder"), new Texture("boulder", true));
			rock.getTexture().setNormalMap(new Texture("boulderNormal", true));
			rock.getTexture().setShineDamper(10);
			rock.getTexture().setReflectivity(0.2f);

			person = new TexturedModel(personMesh, new Texture("playerTexture", true));
			tree = new TexturedModel(treeMesh, new Texture("pine", true));
			
			lamp = new TexturedModel(lampModel, new Texture("lamp", true));

			backgroundTexture = new Texture("grassy2", true);
			rTexture = new Texture("mud", true);
			gTexture = new Texture("grassFlowers", true);
			bTexture = new Texture("path", true);

			texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
			blendMap = new Texture("blendMap", true);

			SplashRenderer.render();
			System.gc();
			System.out.println("done!  Took " + t.getTime());
		} catch (Exception e) {
			new CrashReport("Loading Textures", e).print();
		}
	}

	private Assets() {
	}

	public static void loadNext() {
		synchronized (lock) {
			if (toLoad.isEmpty()) {
				return;
			}
			toLoad.getFirst().load();
			toLoad.removeFirst();
		}
	}
	
	public static boolean hasLoadedAll(){
		return toLoad.isEmpty();
	}

	public static void add(Asset a) {
		synchronized (lock) {
			toLoad.add(a);
			allAssets.add(a);
		}
	}

	public static void loadCoreAssets() {
		loadingTexture = new Texture("loading", true);
		loadingTexture.load();
		Texture fontAtlas = new Texture("/fonts/arial.png");
		fontAtlas.load();
		font = new FontType(fontAtlas, "/fonts/arial.fnt");
	}

	public static LinkedList<Asset> getToLoad() {
		synchronized (lock) {
			return toLoad;
		}
	}
	
	public static void cleanUp(){
		for(Asset a : allAssets){
			a.delete();
		}
	}

}
