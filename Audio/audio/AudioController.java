package audio;

import com.troy.troyberry.math.Vector3f;

/**
 * An object which allows other classes to alter the settings of a sound source
 * while playing a certain sound. Whenever a play request is made to the
 * {@link SoundSource} an AudioController is returned allowing the source's
 * settings to be updated while the sound is playing. Once the sound has
 * finished playing the contoller becomes inactive.
 */
public class AudioController {

	private static final float FADE_TIME = 2;

	private SoundSource source;
	private boolean active = true;
	private boolean fading = false;

	private float finalVolume;
	private float fadeFactor = 1;

	/**
	 * Creates a new controller for the given sound source.
	 * 
	 * @param source
	 *            - the sound source that this controller can control.
	 */
	protected AudioController(SoundSource source) {
		this.source = source;
	}

	/**
	 * Stops the source playing the sound.
	 */
	protected void stop() {
		if (active) {
			source.stop();
		}
	}

	/**
	 * Indicates that the sound needs to be faded out.
	 */
	protected void fadeOut() {
		fading = true;
		finalVolume = source.getVolume();
	}

	/**
	 * Updates the controller, checking whether the controller is still active.
	 * 
	 * @param delta
	 *            - the time in seconds since the last frame.
	 * @return {@code true} if the controller is still active. If the source has
	 *         stopped playing the sound that this controller was created for
	 *         then the controller is no longer active and will return
	 *         {@code false}.
	 */
	protected boolean update(float delta) {
		if (active) {
			updateActiveController(delta);
		}
		return active;
	}

	/**
	 * Sets the position of the source (as long as the source is still playing
	 * the sound that this controller was created for).
	 * 
	 * @param position
	 *            - the new position of the source.
	 */
	protected void setPosition(Vector3f position) {
		if (active) {
			source.setPosition(position);
		}
	}

	/**
	 * @return The volume that the sound is currently being played at.
	 */
	protected float getVolume() {
		return source.getVolume();
	}

	/**
	 * @return {@code true} if the source that the controller is assigned to is
	 *         still playing the sound.
	 */
	protected boolean isActive() {
		return active;
	}

	/**
	 * Indicates that the source is no longer playing the sound that this
	 * controller was created for, rendering the controller inactive.
	 */
	protected void setInactive() {
		active = false;
	}

	/**
	 * If necessary it continues fading out the volume of the source.
	 * 
	 * @param delta
	 *            - time in seconds since the last frame.
	 */
	private void updateActiveController(float delta) {
		if (fading) {
			updateFadingOut(delta);
		}
	}

	/**
	 * Fades out the volume of the source over time. Once the volume reaches 0
	 * the source playing the sound is stopped (rendering this controller
	 * inactive).
	 * 
	 * @param delta
	 *            - time in seconds since the last frame.
	 */
	private void updateFadingOut(float delta) {
		fadeFactor -= delta / FADE_TIME;
		source.setVolume(finalVolume * fadeFactor);
		if (fadeFactor <= 0) {
			source.stop();
		}
	}

}
