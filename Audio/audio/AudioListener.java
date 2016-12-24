package audio;

import com.troy.troyberry.math.Vector3f;

/**
 * An audio listener in the 3D world. The volume of sound effects depends on the
 * position of the sound emitter and the position of the listener.
 */
public interface AudioListener {

	/**
	 * @return The 3D position of the listener.
	 */
	public Vector3f getPosition();

}
