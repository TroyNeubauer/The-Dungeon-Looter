package audio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A streamer object is used to carry out the streaming of one playing of a
 * large audio file. The streamer checks when the source has finished playing
 * buffers and refills them with new data before queueing them up to be played
 * again by the source.
 */
public class Streamer {

	private static final int NUM_BUFFERS = 2;

	private SoundSource source;
	private AudioController controller;
	private WavDataStream stream;

	private boolean initialBufferPlaying = true;

	private List<Integer> unusedBuffers = new ArrayList<Integer>();
	private List<Integer> bufferQueue = new ArrayList<Integer>();

	/**
	 * Create a new stream to play a certain sound using a certain sound source.
	 * This also opens the data input stream for the sound file and creates the
	 * buffers which will be used to hold chunks of audio data.
	 * 
	 * @param sound
	 *            - the sound to be streamed.
	 * @param source
	 *            - the source being used to play the sound.
	 * @param controller
	 *            - the controller which indicates when the source has stopped
	 *            playing the sound.
	 * @throws Exception
	 *             When something goes wrong :(
	 */
	protected Streamer(Sound sound, SoundSource source, AudioController controller) throws Exception {
		System.out.println("Streaming " + sound.getSoundFile());
		this.source = source;
		this.controller = controller;
		stream = WavDataStream.openWavStream(sound.getSoundFile(), StreamManager.SOUND_CHUNK_MAX_SIZE);
		stream.setStartPoint(sound.getBytesRead());
		for (int i = 0; i < NUM_BUFFERS; i++) {
			unusedBuffers.add(SoundLoader.generateBuffer());
		}
	}

	/**
	 * When the streaming of the sound has finished the buffers can be deleted.
	 */
	protected void delete() {
		stream.close();
		for (Integer buffer : bufferQueue) {
			SoundLoader.deleteBuffer(buffer);
		}
		for (Integer buffer : unusedBuffers) {
			SoundLoader.deleteBuffer(buffer);
		}
	}

	/**
	 * Checks if there are any buffers which have finished playing and refills
	 * them with data.
	 * 
	 * @return {@code false} when the source has finished playing the sound and
	 *         has already removed any buffers from its queue.
	 */
	protected boolean update() {
		if (!controller.isActive()) {
			return false;
		}
		if (!stream.hasEnded() && source.isPlaying()) {
			if (!unusedBuffers.isEmpty()) {
				queueUnusedBuffer();
			} else if (isTopBufferFinished()) {
				refillTopBuffer();
			}
		}
		return controller.isActive();
	}

	/**
	 * Fills the first unused buffer with data and queues it to be played.
	 */
	private void queueUnusedBuffer() {
		int buffer = unusedBuffers.remove(0);
		loadNextDataIntoBuffer(buffer);
		queueBuffer(buffer);
	}

	/**
	 * Refills the buffer at the front of the queue with data and re-adds it ton
	 * the end of the queue.
	 */
	private void refillTopBuffer() {
		int topBuffer = unqueueTopBuffer();
		loadNextDataIntoBuffer(topBuffer);
		queueBuffer(topBuffer);
	}

	/**
	 * Loads the next chunk of audio data into a buffer.
	 * 
	 * @param buffer
	 *            - the buffer into which the data should be loaded.
	 */
	private void loadNextDataIntoBuffer(int buffer) {
		ByteBuffer data = stream.loadNextData();
		SoundLoader.loadSoundDataIntoBuffer(buffer, data, stream.alFormat, stream.sampleRate);
	}

	/**
	 * @return {@code true} if there is a buffer which the source has already
	 *         finished playing. This doesn't include the initial sound buffer
	 *         from the {@link Sound} object, whose data is never changed.
	 */
	private boolean isTopBufferFinished() {
		int finishedBufferCount = source.getFinishedBuffersCount();
		if (finishedBufferCount > 0 && initialBufferPlaying) {
			finishedBufferCount--;
			source.unqueue();
			initialBufferPlaying = false;
		}
		return finishedBufferCount > 0;
	}

	/**
	 * Removes the top buffer from the queue.
	 * 
	 * @return The ID of the top buffer.
	 */
	private int unqueueTopBuffer() {
		int topBuffer = bufferQueue.remove(0);
		source.unqueue();
		return topBuffer;
	}

	/**
	 * Adds a buffer to the end of the queue to be played by the source.
	 * 
	 * @param buffer
	 *            - the buffer to be queued.
	 */
	private void queueBuffer(int buffer) {
		if (source.isPlaying()) {
			source.queue(buffer);
			bufferQueue.add(buffer);
		}
	}

}
