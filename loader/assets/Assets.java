package assets;

import java.util.LinkedList;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.util.CrashReport;

import graphics.*;
import graphics.renderer.SplashRenderer;
import loader.Loader;
import mesh.NormalMappedObjLoader;

public class Assets {

	private static LinkedList<Asset> toLoad = new LinkedList<Asset>();
	private static Object lock = new Object();

	public static Texture backgroundTexture, rTexture, gTexture, bTexture;
	public static Texture loadingTexture;

	private static Mesh personMesh, treeMesh;
	public static TexturedModel person, rock, tree;
	public static Texture particleTexture;

	public static TerrainTexturePack texturePack;
	public static Texture blendMap;

	public static void init(Loader loader) {
		try {
			System.out.println("Loading assets  ");
			Timer t = new Timer();

			personMesh = new Mesh("person", loader);
			treeMesh = new Mesh("pine", loader);
			particleTexture = new Texture("particleAtlas", true);
			particleTexture.setNumberOfRows(4);
			particleTexture.load();
			rock = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader), new Texture("boulder", true));
			rock.getTexture().setNormalMap(new Texture("boulderNormal", true));
			rock.getTexture().setShineDamper(10);
			rock.getTexture().setReflectivity(0.5f);

			person = new TexturedModel(personMesh, new Texture("playerTexture", true));
			tree = new TexturedModel(treeMesh, new Texture("pine", true));

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
		}
	}

	public static void loadCoreAssets(Loader loader) {
		loadingTexture = new Texture("loading", true);
		loadingTexture.load();
	}

	public static LinkedList<Asset> getToLoad() {
		synchronized (lock) {
			return toLoad;
		}
	}

}
