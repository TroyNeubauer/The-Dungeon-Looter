package loader;

import java.util.LinkedList;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.util.CrashReport;
import com.troy.troyberry.util.MyFile;

import graphics.fontcreator.FontType;
import graphics.renderer.SplashRenderer;
import loader.mesh.Mesh;
import loader.mesh.OBJFileMesh;
import loader.texture.*;

public class Assets {

	private static LinkedList<Asset> toLoad = new LinkedList<Asset>(), allAssets = new LinkedList<Asset>();
	private static Object lock = new Object();

	public static Texture backgroundTexture, rTexture, gTexture, bTexture;
	public static Texture loadingTexture;

	private static Mesh personMesh, treeMesh, lampModel;
	public static CompleteModel person, rock, tree, lamp;
	public static ParticleTexture smokeParticle, snowflake;
	
	public static FontType font;

	public static TerrainTexturePack texturePack;
	public static Texture blendMap;

	public static void init() {
		try {
			System.out.println("Loading assets  ");
			Timer t = new Timer();

			personMesh = new OBJFileMesh("person", false).loadLater();
			treeMesh = new OBJFileMesh("pine", false).loadLater();
			lampModel = new OBJFileMesh("lamp", false).loadLater();
			
			snowflake = (ParticleTexture) new ParticleTexture("snowflake", 1).loadLater();
			
			smokeParticle = (ParticleTexture) new ParticleTexture("smoke", 4).loadLater();
			
			rock = new CompleteModel(new OBJFileMesh("boulder", true), new Skin(new Texture2D("boulder"), new Texture2D("boulderNormal")));
			rock.getSkin().setShineValues(10, 0.2f);

			person = new CompleteModel(personMesh, new Skin(new Texture2D("playerTexture")));
			tree = new CompleteModel(treeMesh, new Skin(new Texture2D("pine")));
			
			lamp = new CompleteModel(lampModel, new Skin(new Texture2D("lamp")));

			backgroundTexture = new Texture2D("grassy2").loadLater();
			rTexture = new Texture2D("mud").loadLater();
			gTexture = new Texture2D("grassFlowers").loadLater();
			bTexture = new Texture2D("path").loadLater();

			texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
			blendMap = new Texture2D("blendMap").loadLater();

			SplashRenderer.render();
			System.gc();
			System.out.println("done!  Took " + t.getTime());
		} catch (Exception e) {
			new CrashReport("Loading Textures", e).print();
		}
	}

	private Assets() {
	}

	public static void loadCoreAssets() {
		loadingTexture = new Texture2D("loading").loadCompletely();
		font = new FontType(new Texture2D(new MyFile("/fonts/arial.png")).loadLater(), "arial");
	}
}
