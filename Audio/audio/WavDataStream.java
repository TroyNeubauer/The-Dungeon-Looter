package audio;

import java.io.*;
import java.nio.ByteBuffer;

import javax.sound.sampled.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

import com.troy.troyberry.util.MyFile;

public class WavDataStream {

	final int alFormat;
	final int sampleRate;
	final int totalBytes;
	final int bytesPerFrame;

	private final int chunkSize;
	private final AudioInputStream audioStream;

	private final ByteBuffer buffer;
	private final byte[] data;

	private int totalBytesRead = 0;

	private WavDataStream(AudioInputStream stream, int chunkSize) {
		this.audioStream = stream;
		this.chunkSize = chunkSize;
		AudioFormat format = stream.getFormat();
		alFormat = getOpenAlFormat(format.getChannels(), format.getSampleSizeInBits());
		this.buffer = BufferUtils.createByteBuffer(chunkSize);
		this.data = new byte[chunkSize];
		this.sampleRate = (int) format.getSampleRate();
		this.bytesPerFrame = format.getFrameSize();
		this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
	}

	protected void setStartPoint(int bytesRead) {
		this.totalBytesRead = bytesRead;
		try {
			// why can't I use audioStream.skip(bytesRead)?? Surely that
			// should work, but doesn't :(
			audioStream.read(data, 0, bytesRead);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the next chunk of data from the .wav file into a ByteBuffer. The
	 * amount of bytes that it attempts to load is determined by the
	 * {@code chunkSize} argument when the {@link #openWavStream(MyFile, int)
	 * openWavStream()} method was called to create this stream. The actual
	 * number of bytes loaded may be less depending on how close to the end of
	 * the stream it is, or if the {@code chunkSize} doesn't represent an
	 * integer number of audio frames.
	 * 
	 * @return The loaded byte buffer.
	 */
	protected ByteBuffer loadNextData() {
		try {
			int bytesRead = audioStream.read(data, 0, chunkSize);
			totalBytesRead += bytesRead;
			buffer.clear();
			buffer.put(data, 0, bytesRead);
			buffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't read more bytes from audio stream!");
		}
		return buffer;
	}

	/**
	 * @return {@code true} if the stream has read all the audio data and
	 *         reached the end of the data.
	 */
	protected boolean hasEnded() {
		return totalBytesRead >= totalBytes;
	}

	/**
	 * @return The total number of bytes in the audio data.
	 */
	protected int getTotalBytes() {
		return totalBytes;
	}

	/**
	 * Closes the stream.
	 */
	protected void close() {
		try {
			audioStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a new open wave data stream which can be used to stream audio
	 * data from the chosen .wav file.
	 * 
	 * @param wavFile
	 *            - the file to be streamed.
	 * @param chunkSize
	 *            - the maximum amount of data in bytes that should be read from
	 *            the file each time {@link #loadNextData()} is called
	 * @return The open wave data stream.
	 * @throws Exception
	 *             When something goes wrong :'(
	 */
	protected static WavDataStream openWavStream(MyFile wavFile, int chunkSize) throws Exception {
		InputStream bufferedInput = new BufferedInputStream(wavFile.getInputStream());
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
		WavDataStream wavStream = new WavDataStream(audioStream, chunkSize);
		return wavStream;
	}

	/**
	 * Determines the OpenAL ID of the sound data format.
	 * 
	 * @param channels
	 *            - number of channels in the audio data.
	 * @param bitsPerSample
	 *            - number of bits per sample (either 8 or 16).
	 * @return The OpenAL format ID of the sound data.
	 */
	private static int getOpenAlFormat(int channels, int bitsPerSample) {
		if (channels == 1) {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
		} else {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
		}
	}

}
