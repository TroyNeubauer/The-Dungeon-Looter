package world;

import java.util.Random;
import com.troy.troyberry.utils.noise.SimplexNoise;

public class HeightGenerator {

	private final SimplexNoise noise;
	private Random random = new Random();
	private long seed;
	private final int xOffset, zOffset, largestFeature;

	private final double divideFactor, persistence;

	public HeightGenerator(int gridX, int gridZ, double divideFactor, double persistence, int largestFeature, long seed) {
		this.xOffset = gridX * (Terrain.VERTEX_COUNT - 1);
		this.zOffset = gridZ * (Terrain.VERTEX_COUNT - 1);
		this.noise = new SimplexNoise(largestFeature, persistence, seed);
		this.seed = seed;
		this.largestFeature = largestFeature;
		this.divideFactor = divideFactor;
		this.persistence = persistence;
	}

	public float generateHeight(int x, int z) {
		return getNoise(x, z);
	}

	private float getNoise(double x, double z) {
		return (float) noise.getNoise((x + xOffset) / divideFactor, (z + zOffset) / divideFactor);
	}
}
