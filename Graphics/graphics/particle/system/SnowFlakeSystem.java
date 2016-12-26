package graphics.particle.system;

import com.troy.troyberry.math.Vector3f;

import loader.Assets;
import thedungeonlooter.entity.player.EntityPlayer;

public class SnowFlakeSystem extends ParticleSystem {

	private EntityPlayer player;
	
	public SnowFlakeSystem(EntityPlayer player) {
		super(150, 0, 0.1f, 0, 1.0f, 0, 0.1f, 0.01f, 10, 2, 1, Assets.snowflake, player.position, ParticleSystemShape.SPHERE, 80, true);
		this.player = player;
	}
	
	@Override
	public boolean update() {
		this.systemCenter = new Vector3f(player.position.x, player.position.y + 20, player.position.z);
		return super.update();
	}

}
