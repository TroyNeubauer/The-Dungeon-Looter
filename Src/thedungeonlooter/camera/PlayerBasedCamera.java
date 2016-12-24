package thedungeonlooter.camera;

import thedungeonlooter.entity.player.EntityPlayer;

public abstract class PlayerBasedCamera extends ICamera {
	
	protected EntityPlayer player;
	protected float cameraHeight = 1.5f;
	
	public PlayerBasedCamera() {
		super();
	}
	
	public void update() {
		super.update();
		
	}
	
	public PlayerBasedCamera(PlayerBasedCamera camera) {
		super(camera);
		this.player = camera.player;
	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;
		move();
		updateViewMatrix();
	}

	public void setHeight(float f) {
		cameraHeight = f;
	}
}
