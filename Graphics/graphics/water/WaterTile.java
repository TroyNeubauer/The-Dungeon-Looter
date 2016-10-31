package graphics.water;

public class WaterTile {

	public float height, size, x, z;

	public WaterTile(float centerX, float centerZ, float size, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.size = size;
		this.height = height;
	}
}
