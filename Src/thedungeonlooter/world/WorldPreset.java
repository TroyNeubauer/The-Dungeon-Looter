package thedungeonlooter.world;

public class WorldPreset {

	public double divideFactor, persistence;
	public int largestFeature;
	public String name;

	public static WorldPreset SUPER_FLAT = new WorldPreset(10000.0, 0.001, 1, "Super Flat"), FLAT = new WorldPreset(70.0, 1.0, 5, "Flat"),
		SMALL_HILLS = new WorldPreset(80.0, 1.0, 10, "Small Hills"), HILLS = new WorldPreset(100.0, 1.2, 30, "Hills"),
		BIG_HILLS = new WorldPreset(200.0, 1.6, 75, "Big Hills"), GIANT_HILLS = new WorldPreset(400.0, 1.9, 200, "Giant Hills");

	private WorldPreset(double divideFactor, double persistence, int largestFeature, String name) {
		this.divideFactor = divideFactor;
		this.persistence = persistence;
		this.largestFeature = largestFeature;
		this.name = name;
	}

}
