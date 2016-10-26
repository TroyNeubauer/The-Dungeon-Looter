package entity.player;

public class DeathAnimation {

	public volatile float redFactor, increaseSpeed;

	public DeathAnimation(float increaseSpeed) {
		this.redFactor = 0;
		this.increaseSpeed = increaseSpeed;
	}

	public void update() {
		this.redFactor += increaseSpeed;
		this.redFactor = Math.min(0.3f, redFactor);
	}

}
