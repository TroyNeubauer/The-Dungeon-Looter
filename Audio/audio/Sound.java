package audio;

import com.troy.troyberry.util.MyFile;

/**
 * Represents a single sound effect. Holds a reference to the sound's file, and
 * a reference to any buffers containing loaded data of the sound.
 */
public class Sound {

	public final String id;
	private int bufferID;
	private MyFile file;
	private float volume = 1;
	private boolean loaded = false;

	private int totalBytes;
	private int bytesRead;

	/**
	 * Creates a new sound. Note the sound hasn't been loaded at this stage.
	 * 
	 * @param soundFile
	 *            - the sound's file.
	 * @param volume
	 *            - used to change the base volume of a sound effect.
	 */
	private Sound(MyFile soundFile) {
		this.file = soundFile;
		this.id = soundFile.getName().split("\\.")[0];
	}

	/**
	 * Loads a sound file and loads some (possibly all for short sounds) of the
	 * sound data into an OpenAL buffer.
	 * 
	 * @param soundFile
	 *            - the sound file.
	 * @return A new sound object which represents the loaded sound.
	 */
	public static Sound loadSoundNow(MyFile soundFile) {
		Sound sound = new Sound(soundFile);
		SoundLoader.doInitialSoundLoad(sound);
		return sound;
	}

	/**
	 * Sends a request to the resource loading thread for the sound to be
	 * loaded.
	 * 
	 * @param soundFile
	 *            - the sound file.
	 * @return A new sound object which represents the loaded sound.
	 */
	public static Sound loadSoundInBackground(MyFile soundFile) {
		final Sound sound = new Sound(soundFile);
		//TODO: implement
		return sound;
	}

	/**
	 * Sends a request for the sound to be deleted.
	 */
	public void delete() {
		if (!loaded) {
			return;
		}
		//TODO: actually delete
		loaded = false;
	}
	
	public Sound withVolume(float volume){
		this.volume = volume;
		return this;
	}
	
	public String getId() {
		return id;
	}

	/**
	 * @return The base volume of the sound.
	 */
	protected float getVolume() {
		return volume;
	}

	/**
	 * @return Whether the sound is loaded or not.
	 */
	protected boolean isLoaded() {
		return loaded;
	}

	/**
	 * When initially loading a sound only a certain number of bytes are read.
	 * If the sound file for this sound contains more bytes than the number of
	 * bytes which were initially loaded then the sound file needs to be
	 * streamed when played.
	 * 
	 * @return {@code true} if the sound file needs streaming when it's played.
	 */
	protected boolean needsStreaming() {
		return bytesRead < totalBytes;
	}

	/**
	 * @return The number of bytes from the sound's file that have already been
	 *         loaded.
	 */
	protected int getBytesRead() {
		return bytesRead;
	}

	/**
	 * Sets the total number of bytes of audio data in the sound's file.
	 * 
	 * @param totalBytes
	 *            - the total number of bytes of data.
	 */
	protected void setTotalBytes(int totalBytes) {
		this.totalBytes = totalBytes;
	}

	/**
	 * Sets the buffer containing (at least some of) the sound's audio data. It
	 * also indicates how many bytes of the sound's data has been loaded into
	 * the buffer.
	 * 
	 * @param buffer
	 *            - the ID of the OpenAL buffer which contains the sound's
	 *            loaded audio data.
	 * @param bytesRead
	 *            - the number of bytes of the sound's audio which have been
	 *            loaded into the buffer.
	 */
	protected void setBuffer(int buffer, int bytesRead) {
		this.bufferID = buffer;
		this.bytesRead = bytesRead;
		loaded = true;
	}

	/**
	 * @return The ID of the sound's buffer.
	 */
	protected int getBufferID() {
		return bufferID;
	}

	/**
	 * @return The sound file.
	 */
	protected MyFile getSoundFile() {
		return file;
	}

}
