package graphics;

import java.io.File;
import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.utils.CrashReport;
import fontMeshCreator.FontType;
import loader.Loader;
import loader.NormalMappedObjLoader;
import loader.OBJLoader;
import particles.ParticleTexture;
import renderEngine.SplashRenderer;

public class Assets {

	public static TerrainTexture backgroundTexture, rTexture, gTexture, bTexture;
	public static Texture loadingTexture;

	public static FontType font, debugFont;

	private static Mesh personMesh, rocksMesh, treeMesh;
	public static TexturedModel person, rock, tree;
	public static ParticleTexture particleTexture;

	public static TerrainTexturePack texturePack;
	public static TerrainTexture blendMap;

	public static void load(Loader loader) {
		try {
			System.out.println("Loading assets  ");
			Timer t = new Timer();
			personMesh = OBJLoader.loadObjModel("person", loader);
			SplashRenderer.render();
			rocksMesh = OBJLoader.loadObjModel("boulder", loader);
			SplashRenderer.render();
			treeMesh = OBJLoader.loadObjModel("pine", loader);
			SplashRenderer.render();
			particleTexture = new ParticleTexture(new Texture("particleAtlas", true), 4);
			SplashRenderer.render();

			rock = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader), new Texture("boulder", true));
			SplashRenderer.render();
			rock.getTexture().setNormalMap(new Texture("boulderNormal", true));
			SplashRenderer.render();
			rock.getTexture().setShineDamper(10);
			SplashRenderer.render();
			rock.getTexture().setReflectivity(0.5f);

			person = new TexturedModel(personMesh, new Texture("playerTexture", true));
			SplashRenderer.render();
			tree = new TexturedModel(treeMesh, new Texture("pine", true));
			SplashRenderer.render();

			backgroundTexture = new TerrainTexture(new Texture("grassy2", true));
			SplashRenderer.render();
			rTexture = new TerrainTexture(new Texture("mud", true));
			SplashRenderer.render();
			gTexture = new TerrainTexture(new Texture("grassFlowers", true));
			SplashRenderer.render();
			bTexture = new TerrainTexture(new Texture("path", true));
			SplashRenderer.render();

			texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
			SplashRenderer.render();
			blendMap = new TerrainTexture(new Texture("blendMap", true));
			SplashRenderer.render();
			System.gc();
			System.out.println("done!  Took " + t.getTime());
		} catch (Exception e) {
			new CrashReport("Loading Textures", e).print();
		}
	}

	private Assets() {
	}

	public static void loadCoreAssets(Loader loader) {
		loadingTexture = new Texture("loading", true);
		font = new FontType(new Texture("./res/fonts/harrington.png").id, new File("res/fonts/harrington.fnt"));
		debugFont = new FontType(new Texture("./res/fonts/verdana.png").id, new File("./res/fonts/verdana.fnt"));
	}

}
