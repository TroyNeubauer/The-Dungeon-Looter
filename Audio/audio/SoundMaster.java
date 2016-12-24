package audio;

import org.lwjgl.openal.*;

import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.util.MyFile;

public class SoundMaster {

	public static final MyFile SOUND_FOLDER = new MyFile("sounds");

	public static float SOUND_VOLUME = 1f;

	private static SourcePoolManager sourcePool;
	private static AudioListener listener;

	/**
	 * Initializes all the sound related things. Should be called when the game
	 * loads.
	 * 
	 * @param theListener
	 *            - the listener for the scene.
	 */
	public static void init(AudioListener theListener) {

		ALC.create();
		AL10.alGetError();
		AL10.alDistanceModel(AL11.AL_LINEAR_DISTANCE_CLAMPED);
		StreamManager.STREAMER.start();
		sourcePool = new SourcePoolManager();
		listener = theListener;
	}

	/**
	 * Send a request to play a system sound effect at full volume. The request
	 * is sent to the {@link SourcePoolManager} which will find a source to play
	 * the sound.
	 * 
	 * @param sound
	 *            - the sound to be played.
	 * @return The controller for the playing of this sound.
	 */
	public static AudioController playSystemSound(Sound sound) {
		if (!sound.isLoaded()) {
			return null;
		}
		return sourcePool.play(PlayRequest.newSystemPlayRequest(sound));
	}

	/**
	 * @return The listener for the scene.
	 */
	public static AudioListener getListener() {
		return listener;
	}

	/**
	 * Updates the listener's position, the music player and the source pool
	 * manager.
	 * 
	 * @param delta
	 *            - the time that has passed since the last frame.
	 */
	public static void update(float delta) {
		Vector3f position = listener.getPosition();
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
		sourcePool.update();
	}

	/**
	 * Cleans up all the audio stuff. Should be called when the game closes.
	 */
	public static void cleanUp() {
		StreamManager.STREAMER.kill();
		sourcePool.cleanUp();
		SoundLoader.cleanUp();
		ALC.destroy();
	}

	/**
	 * Plays a sound that should be emitted from somewhere in the 3D world.
	 * 
	 * @param playRequest
	 *            - the request containing the sound and all the settings for
	 *            the playing of the sound.
	 * @return The controller for the source which plays the sound. Returns
	 *         {@code null} if no source was available to play the sound.
	 */
	protected static AudioController play3DSound(PlayRequest playRequest) {
		if (!playRequest.getSound().isLoaded()) {
			return null;
		}
		return sourcePool.play(playRequest);
	}

}
