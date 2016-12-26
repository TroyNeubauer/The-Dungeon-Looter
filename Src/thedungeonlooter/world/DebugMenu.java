package thedungeonlooter.world;

import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.util.StringFormatter;

import graphics.fontcreator.GUIText;
import graphics.particle.ParticleMaster;
import graphics.renderer.MasterRenderer;
import loader.Assets;
import thedungeonlooter.input.Controls;

public class DebugMenu {
	
	private static World world;
	private static GUIText terrainCount = new GUIText("", 1.1f, Assets.font, new Vector2f(0, 0.05f), 1.0f, false);
	private static GUIText entityCount = new GUIText("", 1.1f, Assets.font, new Vector2f(0, 0.10f), 1.0f, false);
	private static GUIText particleCount = new GUIText("", 1.1f, Assets.font, new Vector2f(0, 0.15f), 1.0f, false);
	private static GUIText memoryUsage = new GUIText("", 1.1f, Assets.font, new Vector2f(0.9f, 0.01f), 1.0f, false);
	private static boolean lastShown = false;
	private static boolean debugShown;
	
	public static void init(World world){
		DebugMenu.world = world;
	}
	
	public static void render(){
		if(Controls.DEBUG_MENU.hasBeenPressed()){
			debugShown = !debugShown;
		}
		Runtime r =  Runtime.getRuntime();
		terrainCount.setText("T: " + MasterRenderer.terrainssRedered).setShown(debugShown).setColor(world.blendFactor);
		entityCount.setText("E: " + MasterRenderer.entitiesRedered + " / " + world.entities.size()).setShown(debugShown).setColor(world.blendFactor);
		particleCount.setText("P: " + ParticleMaster.getParticleCount()).setShown(debugShown).setColor(world.blendFactor);
		memoryUsage.setText("Mem " + StringFormatter.formatBytes(r.totalMemory() - r.freeMemory())).setShown(debugShown).setColor(world.blendFactor);
	}

}
