package graphics;

import java.io.File;
import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.utils.CrashReport;
import graphics.font.loader.FontType;
import loader.Loader;
import loader.NormalMappedObjLoader;
import loader.OBJLoader;
import particles.ParticleTexture;
import renderEngine.SplashRenderer;

public class Assets {

	public static TerrainTexture backgroundTexture, rTexture, gTexture, bTexture;
	public static Texture loadingTexture, sun;

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
			SplashRenderer.render();
			personMesh = OBJLoader.loadObjModel("person", loader);
			rocksMesh = OBJLoader.loadObjModel("boulder", loader);
			treeMesh = OBJLoader.loadObjModel("pine", loader);
			particleTexture = new ParticleTexture(new Texture("particleAtlas", true), 4);

			SplashRenderer.render();
			rock = new TexturedModel(NormalMappedObjLoader.loadOBJ("boulder", loader), new Texture("boulder", true));
			rock.getTexture().setNormalMap(new Texture("boulderNormal", true));
			rock.getTexture().setShineDamper(10);
			rock.getTexture().setReflectivity(0.5f);

			font = new FontType(new Texture("./res/fonts/verdana.png").id, new File("./res/fonts/verdana.fnt"));
			SplashRenderer.render();

			person = new TexturedModel(personMesh, new Texture("playerTexture", true));
			tree = new TexturedModel(treeMesh, new Texture("pine", true));

			backgroundTexture = new TerrainTexture(new Texture("grassy2", true));
			rTexture = new TerrainTexture(new Texture("mud", true));
			gTexture = new TerrainTexture(new Texture("grassFlowers", true));
			bTexture = new TerrainTexture(new Texture("path", true));

			texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
			blendMap = new TerrainTexture(new Texture("blendMap", true));

			sun = new Texture("sun", true);

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
	}

}
