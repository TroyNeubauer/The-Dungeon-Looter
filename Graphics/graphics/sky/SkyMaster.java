package graphics.sky;

import thedungeonlooter.camera.ICamera;
import thedungeonlooter.world.World;

public class SkyMaster {
	
	public static final float SUN_DISTANCE = 400f;
	
	public static World world;
	
	public static SkyboxRenderer skyboxRenderer;
	private static SunRenderer sunRenderer;
	
	public static void render(ICamera camera, float red, float green, float blue){
		skyboxRenderer.render(camera, red, green, blue);
		sunRenderer.render(camera, world.sun);
	}

	public static void init(World world, ICamera camera) {
		skyboxRenderer = new SkyboxRenderer(world, camera);
		sunRenderer = new SunRenderer(camera);
	}

}
