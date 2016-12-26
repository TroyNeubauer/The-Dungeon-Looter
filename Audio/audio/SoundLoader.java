package audio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;

/**
 * Contains methods used for loading and deleting sounds, and also keeps track
 * of all the sound buffers that are currently loaded so that it can delete them
 * when the game closes.
 *
 */
public class SoundLoader {

	private static List<Integer> buffers = new ArrayList<Integer>();

	/**
	 * Generates an OpenAL buffer and loads some, if not all, of the sound data
	 * into it. The buffer and other information about the audio data gets set in
	 * the sound object. This is called to load a new sound for the first time.
	 * 
	 * @param sound - the sound to be loaded.
	 */
	protected static void doInitialSoundLoad(Sound sound) {
		try {
			System.out.println("Loading " + sound.getSoundFile());
			WavDataStream stream = WavDataStream.openWavStream(sound.getSoundFile(),
					StreamManager.SOUND_CHUNK_MAX_SIZE);
			sound.setTotalBytes(stream.getTotalBytes());
			ByteBuffer byteBuffer = stream.loadNextData();
			int bufferID = generateBuffer();
			loadSoundDataIntoBuffer(bufferID, byteBuffer, stream.alFormat, stream.sampleRate);
			sound.setBuffer(bufferID, byteBuffer.limit());
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Couldn't load sound file " + sound.getSoundFile());
		}
	}

	/**
	 * Generates an empty sound buffer.
	 * 
	 * @return The ID of the buffer.
	 */
	protected static int generateBuffer() {
		int bufferID = AL10.alGenBuffers();
		buffers.add(bufferID);
		return bufferID;
	}

	/**
	 * Removes a certain sound buffer from memory by removing from the list of
	 * buffers and deleting it.
	 * 
	 * @param bufferID
	 *            - the ID of the buffer to be deleted.
	 */
	protected static void deleteBuffer(Integer bufferID) {
		buffers.remove(bufferID);
		AL10.alDeleteBuffers(bufferID);
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			System.err.println("Problem deleting sound buffer.");
	}

	/**
	 * Deletes all the sound buffers that are currently in memory. Should be
	 * called when the application closes.
	 */
	protected static void cleanUp() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		if (AL10.alGetError() != AL10.AL_NO_ERROR)
			System.err.println("Problem deleting sound buffers.");
	}

	/**
	 * Loads audio data of a certain format into an OpenAL buffer.
	 * @param bufferID - the buffer to which the data should be loaded.
	 * @param data - the audio data.
	 * @param format - the OpenAL format of the data (mono, stereo, etc.)
	 * @param sampleRate - the sample rate of the audio.
	 */
	protected static void loadSoundDataIntoBuffer(int bufferID, ByteBuffer data, int format, int sampleRate) {
		AL10.alBufferData(bufferID, format, data, sampleRate);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			System.err.println("Problem loading sound data into buffer. " + error);
		}
	}

}
