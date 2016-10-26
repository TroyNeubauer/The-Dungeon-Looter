package audio;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.WaveData;
import com.troy.troyberry.math.Vector3f;

public class AudioMaster {

	private static List<Integer> buffers = new ArrayList<Integer>();

	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			System.err.println("Unable to create Open AL!!!!!!!!!");
			e.printStackTrace();

		}
	}

	public static int loadSound(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(Class.class.getResourceAsStream(file));
		AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		return buffer;
	}

	public static void setListener(Vector3f position) {
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	public static void cleanUp() {
		for (int i : buffers) {
			AL10.alDeleteBuffers(i);
		}
		AL.destroy();
	}

	private AudioMaster() {
	}

}
