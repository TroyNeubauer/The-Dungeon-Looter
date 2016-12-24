package audio;

/**
 * Represents a 3D sound effect which can be played in the 3D world.
 * 
 * @author Karl
 *
 */
public class SoundEffect {

	private Sound sound;
	private float range;

	/**
	 * @param sound
	 *            - the sound used by this sound effect.
	 * @param range
	 *            - the range of the sound effect.
	 */
	public SoundEffect(Sound sound, float range) {
		this.sound = sound;
		this.range = range;
	}

	/**
	 * @return The sound.
	 */
	public Sound getSound() {
		return sound;
	}

	/**
	 * @return The range of the sound effect.
	 */
	protected float getRange() {
		return range;
	}

}
