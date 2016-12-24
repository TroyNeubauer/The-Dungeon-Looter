package graphics.water;

import java.util.List;

import thedungeonlooter.camera.ICamera;
import thedungeonlooter.input.GameSettings;

public class WaterMaster {
	
	private static HDWaterRenderer HDWaterRenderer;
	private static BasicWaterRenderer basicWaterRenderer;
	
	public static void init(ICamera camera, WaterFrameBuffers fbos){
		HDWaterRenderer = new HDWaterRenderer(camera, fbos);
		basicWaterRenderer = new BasicWaterRenderer(camera);
	}

	public static void render(List<WaterTile> waters, ICamera camera) {
		if(GameSettings.HDWater)HDWaterRenderer.render(waters, camera);
		else basicWaterRenderer.render(waters, camera);
	}
	
	public static void cleanUp(){
	}

}
