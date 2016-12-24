package audio;

import com.troy.troyberry.math.Vector3f;

/**
 * A request that can be sent to the {@link SoundMaster} specifying what sound
 * should be played and what settings the source playing it should use.
 *
 */
public class PlayRequest {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float innerRange = 1;
	private float outerRange = 1;

	private boolean systemSound = true;
	private boolean loop = false;
	private float volume = 1;

	private Sound sound;

	/**
	 * @param sound
	 *            - the sound that needs to be played.
	 * @param volume
	 *            - the volume that the sound should be played at.
	 */
	private PlayRequest(Sound sound, float volume) {
		this.sound = sound;
		this.volume = volume * SoundMaster.SOUND_VOLUME;
	}

	/**
	 * @return The position in the 3D world where the sound should be emitted
	 *         from.
	 */
	protected Vector3f getPosition() {
		return position;
	}

	/**
	 * @return The inner range of the sound. Inside this range the sound is
	 *         heard at full volume.
	 */
	protected float getInnerRange() {
		return innerRange;
	}

	/**
	 * @return The total range of the sound. Outside this range the sound cannot
	 *         be heard.
	 */
	protected float getOuterRange() {
		return outerRange;
	}

	/**
	 * @return {@code true} if the sound is a "system sound", meaning that it
	 *         should be played at full volume regardless of where the
	 *         {@link AudioListener} is.
	 */
	protected boolean isSystemSound() {
		return systemSound;
	}

	/**
	 * @return The volume at which this sound should be played.
	 */
	protected float getVolume() {
		return volume;
	}

	/**
	 * @return The sound that is being requested to be played.
	 */
	protected Sound getSound() {
		return sound;
	}

	/**
	 * Indicates that the request is requesting the sound to be played on loop.
	 * 
	 * @param loop
	 *            - whether the sound should be played on loop or not.
	 */
	protected void setLooping(boolean loop) {
		this.loop = loop;
	}

	/**
	 * @return {@code true} if the sound should be played on loop.
	 */
	protected boolean isLooping() {
		return loop;
	}

	/**
	 * Creates a new request for playing a "system sound", which is a sound that
	 * isn't part of the 3D world and is simple played at full volume regardless
	 * of where the {@link AudioListener} is.
	 * 
	 * @param systemSound
	 *            - the sound to be played.
	 * @return The newly created play request.
	 */
	protected static PlayRequest newSystemPlayRequest(Sound systemSound) {
		return new PlayRequest(systemSound, 1);
	}

	/**
	 * Creates a new request for playing a 3D sound, which is a sound that
	 * should appear to be emitted from somewhere in the 3D world.
	 * 
	 * @param sound
	 *            - the sound to be played.
	 * @param volume
	 *            - the volume of the sound.
	 * @param position
	 *            - the position from where the sound should be emitted.
	 * @param innerRange
	 *            - the inner range of the sound. Within this range of the
	 *            sound's position the sound is played at full volume.
	 * @param outerRange
	 *            - the outer range of the sound. Outside this range from the
	 *            sound's position the sound can't be heard at all.
	 * @return The newly created request.
	 */
	protected static PlayRequest new3dSoundPlayRequest(Sound sound, float volume, Vector3f position, float innerRange,
			float outerRange) {
		PlayRequest request = new PlayRequest(sound, volume);
		request.systemSound = false;
		request.innerRange = innerRange < 1 ? 1 : innerRange;
		request.outerRange = outerRange;
		request.position = position;
		return request;
	}

}
