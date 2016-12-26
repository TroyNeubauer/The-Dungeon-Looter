package audio;

import java.util.ArrayList;
import java.util.List;

/**
 * This thread runs in the background and keeps any audio streams updated. It
 * continuously loops through all currently active {@link Streamer}s and updates
 * them, removing and deleting any which have finished their streaming duty.
 */
public class StreamManager extends Thread {

	protected static final StreamManager STREAMER = new StreamManager();

	public static final int SOUND_CHUNK_MAX_SIZE = 100000;
	private static final long SLEEP_TIME = 100;

	private List<Streamer> streamers = new ArrayList<Streamer>();
	private List<Streamer> toRemove = new ArrayList<Streamer>();
	private boolean alive = true;

	@Override
	public void run() {
		while (alive) {
			List<Streamer> safeCopy = new ArrayList<Streamer>(streamers);
			for (Streamer streamer : safeCopy) {
				updateStreamer(streamer);
			}
			removeFinishedStreamers();
			pause();
		}
	}

	/**
	 * Stops the thread from running.
	 */
	protected void kill() {
		alive = false;
	}

	// TODO both source and controller as arguments? Seems a bit pointless to have both.
	/**
	 * Sets up a new {@link Streamer} to stream a sound file.
	 * 
	 * @param sound
	 *            - the sound to be streamed.
	 * @param source
	 *            - the source which will play the sound while it is streamed.
	 * @param controller
	 *            - the controller which can be used to find out when the source
	 *            has finished playing the sound in question.
	 */
	protected synchronized void stream(Sound sound, SoundSource source, AudioController controller) {
		try {
			streamers.add(new Streamer(sound, source, controller));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Couldn't open stream for sound " + sound.getSoundFile());
		}
	}

	/**
	 * Removes any finished {@link Streamer}s from the list of current
	 * streamers, and deletes them (deletes their buffers).
	 */
	private synchronized void removeFinishedStreamers() {
		for (Streamer streamer : toRemove) {
			streamers.remove(streamer);
			streamer.delete();
		}
		toRemove.clear();
	}

	/**
	 * Makes the thread sleep for a while so that it doesn't take up 100% CPU.
	 */
	private void pause() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Updates a streamer and checks whether it has finished streaming. If so it
	 * indicates that it should be removed from the list of current streamers.
	 * 
	 * @param streamer
	 *            - the streamer to be updated.
	 */
	private void updateStreamer(Streamer streamer) {
		boolean stillAlive = streamer.update();
		if (!stillAlive) {
			toRemove.add(streamer);
		}
	}

}
