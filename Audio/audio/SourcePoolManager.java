package audio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The source pool manager keeps track of all the current sound sources and
 * deals with any requests to play a sound. When a sound needs to be played an
 * unused source is removed from the pool and used to play the sound. When a
 * source finishes playing its current sound it gets added back into the pool.
 */
public class SourcePoolManager {

	private static final int NUM_SOURCES = 20;

	private List<SoundSource> sourcePool = new ArrayList<SoundSource>();
	private List<SoundSource> usedSources = new ArrayList<SoundSource>();


	/**
	 * Creates all the sound sources that will ever be used to play sound.
	 */
	protected SourcePoolManager() {
		for (int i = 0; i < NUM_SOURCES; i++) {
			sourcePool.add(new SoundSource());
		}
	}

	/**
	 * Plays a sound on an unused sound source, removing it from the pool.
	 * 
	 * @param playRequest
	 *            - the sound and information about how to play it.
	 * @return The sound source being used to play the requested sound. Returns
	 *         {@code null} if there was no source available.
	 */
	protected AudioController play(PlayRequest playRequest) {
		if (!sourcePool.isEmpty()) {
			SoundSource source = sourcePool.remove(0);
			usedSources.add(source);
			source.setPosition(playRequest.getPosition());
			source.loop(playRequest.isLooping());
			if (!playRequest.isSystemSound()) {
				source.setRanges(playRequest.getInnerRange(), playRequest.getOuterRange());
			} else {
				source.setUndiminishing();
			}
			Sound sound = playRequest.getSound();
			source.setVolume(playRequest.getVolume() * sound.getVolume());
			AudioController controller = source.playSound(sound);
			return controller;
		}
		return null;
	}

	/**
	 * Updates all the sources that are currently in use playing sounds and
	 * returns any sources that have finished playing their sound to the pool.
	 */
	protected void update() {
		Iterator<SoundSource> iterator = usedSources.iterator();
		while (iterator.hasNext()) {
			SoundSource source = iterator.next();
			if (!source.isPlaying()) {
				iterator.remove();
				source.setInactive();
				sourcePool.add(source);
			}
		}
	}

	/**
	 * Deletes all of the sound sources when the game is closed.
	 */
	protected void cleanUp() {
		for (SoundSource source : sourcePool) {
			source.delete();
		}
	}

}
