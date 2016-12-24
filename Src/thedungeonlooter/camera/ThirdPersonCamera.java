package thedungeonlooter.camera;

import com.troy.troyberry.math.SmoothFloat;
import com.troy.troyberry.math.Vector2f;
import com.troy.troyberry.opengl.input.Mouse;

import thedungeonlooter.entity.player.EntityPlayer;
import thedungeonlooter.input.Controls;

public class ThirdPersonCamera extends PlayerBasedCamera {
	
	private SmoothFloat distanceFromPlayer = new SmoothFloat(15, 5);
	private float angleAroundPlayer = 0;
	private static final float MIN_DISTANCE_FROM_PLAYER = 2.0f, MAX_DISTANCE_FROM_PLAYER = 25.0f;

	public ThirdPersonCamera() {
	}

	public ThirdPersonCamera(PlayerBasedCamera camera) {
		super(camera);
	}
	
	public void move(){
		distanceFromPlayer.update(0.03f);
		distanceFromPlayer.clamp(MIN_DISTANCE_FROM_PLAYER, MAX_DISTANCE_FROM_PLAYER);
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.rotation.y + angleAroundPlayer);
		yaw%=360;
		
		float dx = (float) (Math.sin(Math.toRadians(player.rotation.y)));
		float dz = (float) (Math.cos(Math.toRadians(player.rotation.y)));
		Vector2f forward = checkInputs(new Vector2f(dx, dz));
		this.velocity.x = forward.x;
		this.velocity.z = forward.y;
		player.velocity = velocity;
		player.rotation.x = 0;
	}
	
	private Vector2f checkInputs(Vector2f forward) {
		Vector2f total = new Vector2f(0f, 0f);
		if (player.isAlive()) {
			if (Controls.FORWARD.isPressedUpdateThread()) {
				total.add(forward);
			}
			if (Controls.BACKWARD.isPressedUpdateThread()) {
				total.add(Vector2f.negate(forward));
			}
			if(Controls.LEFT.isPressedUpdateThread()){
				player.rotation.y += 1f;
			}
			if(Controls.RIGHT.isPressedUpdateThread()){
				player.rotation.y -= 1f;
			}
			total.setLength(player.runSpeed);
		}
		return total;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance){
		float theta = player.rotation.y + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.position.x - offsetX;
		position.z = player.position.z - offsetZ;
		position.y = player.position.y + verticDistance + EntityPlayer.PLAYER_HEIGHT;
	}
	
	private float calculateHorizontalDistance(){
		return (float) (distanceFromPlayer.get() * Math.cos(Math.toRadians(pitch+4)));
	}
	
	private float calculateVerticalDistance(){
		return (float) (distanceFromPlayer.get() * Math.sin(Math.toRadians(pitch+4)));
	}
	
	private void calculateZoom(){
		float zoomLevel = Mouse.getDWheel();
		distanceFromPlayer.increaseTarget(zoomLevel);
	}
	
	private void calculatePitch(){
		if(Mouse.isButtonDown(1)){
			float pitchChange = Mouse.getDY() * 0.2f;
			pitch += pitchChange;
			if(pitch < 0){
				pitch = 0;
			}else if(pitch > 90){
				pitch = 90;
			}
		}
	}
	
	private void calculateAngleAroundPlayer(){
		if(Mouse.isButtonDown(0)){
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

	@Override
	public void render() {
		Mouse.setGrabbed(false);
	}

}
