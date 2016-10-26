package audio;

import org.lwjgl.openal.AL10;
import com.troy.troyberry.math.Vector3f;

public class Source {

	public final int id;

	public Source() {
		id = AL10.alGenSources();
	}

	public void pause() {
		AL10.alSourcePause(id);
	}

	public void resume() {
		AL10.alSourcePlay(id);
	}

	public void stop() {
		AL10.alSourceStop(id);
	}

	public void setVelocity(Vector3f vel) {
		AL10.alSource3f(id, AL10.AL_VELOCITY, vel.x, vel.y, vel.z);
	}

	public void setLooping(boolean looping) {
		AL10.alSourcei(id, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public boolean isPlaying() {
		return AL10.alGetSourcei(id, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}

	public void setVolume(float volume) {
		AL10.alSourcef(id, AL10.AL_GAIN, volume);
	}

	public void setPitch(float pitch) {
		AL10.alSourcef(id, AL10.AL_PITCH, pitch);
	}

	public void setPosition(Vector3f position) {
		AL10.alSource3f(id, AL10.AL_POSITION, position.x, position.y, position.z);
	}

	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(id, AL10.AL_POSITION, x, y, z);
	}

	public void play(int buffer) {
		stop();
		AL10.alSourcei(id, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(id);
	}

	public void delete() {
		stop();
		AL10.alDeleteSources(id);
	}

}
