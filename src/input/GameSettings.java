package input;

public class GameSettings {

	public static int SHADOW_PCF_COUNT = 3;
	public static boolean SHOW_PLAYER_SHADOW = (SHADOW_PCF_COUNT > 3), SHAWODS_ENABLED = false, DEBUG = true;
	public static int SHADOW_BUFFER_SIZE = 1024, MULTISAMPLE_COUNT = 2;
	public static float FOG_DENSITY = 0.004f, FOG_GRADIENT = 20.0f, FONT_SIZE = 1.1f;
	public static volatile double RENDER_DISTANCE = 500.0;

	static {
		setRenderDistance(400.0);
	}

	private GameSettings() {
	}

	public static void setRenderDistance(double renderDistance) {
		if (renderDistance < 1.0) return;
		FOG_DENSITY = (float) ((3.0 / 700000000.0) * renderDistance * renderDistance - (47.0 / 3500000.0) * renderDistance + (27.0 / 2800));
		RENDER_DISTANCE = renderDistance;
	}

}
