package graphics.water;

public class WaterTile {
	
	private float height;
	private float x,z, raduis;
	
	public WaterTile(float centerX, float centerZ, float height, float raduis){
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.raduis = raduis;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public float getRadius() {
		return raduis;
	}



}
