package thedungeonlooter.input;

public class GameSettings {
	
	
	public static int SHADOW_PCF_COUNT = 3, TERRAIN_LOADER_INTERVAL = 250;
	public static boolean SHOW_PLAYER_SHADOW = (SHADOW_PCF_COUNT > 3), SHAWODS_ENABLED = false, DEBUG = true, CLOCK_24_HOUR = false, SHOW_FPS = true;
	public static int SHADOW_BUFFER_SIZE = 3072, MULTISAMPLE_COUNT = 4;
	public static float FOG_DENSITY, FOG_GRADIENT = 20.0f, FONT_SIZE = 1.1f;
	public static volatile double RENDER_DISTANCE;
	public static float MOUSE_SENSITIVITY = 2.0f;
	public static boolean HDWater = true;// TODO: Actually implement water!

	static {
		setRenderDistance(700.0);
	}

	public static void setRenderDistance(double renderDistance) {
		if (renderDistance < 1.0) return;
		FOG_DENSITY = (float) ((3.0 / 700000000.0) * renderDistance * renderDistance - (47.0 / 3500000.0) * renderDistance + (27.0 / 2800));
		RENDER_DISTANCE = renderDistance;
	}
	

	private GameSettings() {
	}

}
