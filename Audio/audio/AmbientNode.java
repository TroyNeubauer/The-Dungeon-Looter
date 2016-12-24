package audio;

import java.util.List;

import com.troy.troyberry.math.*;

/**
 * Basically a 3D sphere of sound that can be added to the world to provide
 * ambient sound effects to a certain area. The node has 2 radiuses; within the
 * inner radius the ambient sound is played at full volume. Between the inner
 * and outer radiuses the volume decreases the furth away from the node you get.
 */
public class AmbientNode {

	private static final float RADIUS_CHANGE_AGIL = 0.5f;
	private static final float RANGE_THRESHOLD = 1.2f;

	private Vector3f position;
	private SmoothFloat innerRadius;
	private SmoothFloat fadeOutRadius;

	private List<Sound> sounds;
	private float volume = 1;

	private boolean active = false;
	private AudioController controller = null;

	private Sound lastPlayed = null;

	/**
	 * Creates a new ambient node at a given position in the world.
	 * 
	 * @param position
	 *            - the position of the center of the node.
	 * @param innerRange
	 *            - the inner radius, within which the ambient sound is played
	 *            at full volume.
	 * @param fadeOutRange
	 *            - the distance between the inner and outer radiuses of the
	 *            node.
	 * @param sounds
	 *            - the various sounds that the node can play.
	 */
	public AmbientNode(Vector3f position, float innerRange, float fadeOutRange, List<Sound> sounds) {
		this.position = position;
		this.innerRadius = new SmoothFloat(innerRange, RADIUS_CHANGE_AGIL);
		this.fadeOutRadius = new SmoothFloat(fadeOutRange, RADIUS_CHANGE_AGIL);
		this.sounds = sounds;
	}

	/**
	 * Updates the node and plays or stops playing ambient sounds depending on
	 * whether the {@link AudioListener} is in range or not.
	 * 
	 * @param delta
	 *            - the time that has passed since the last frame.
	 */
	public void update(float delta) {
		updateValues(delta);
		float distance = getDistanceFromListener();
		if (!active && distance <= getRange() && !sounds.isEmpty()) {
			playNewSound();
		} else if (active) {
			updateActiveNode(distance, delta);
		}
	}

	/**
	 * Sets the volume of this node.
	 * 
	 * @param targetVolume
	 *            - the desired volume.
	 */
	public void setVolume(float targetVolume) {
		this.volume = targetVolume;
	}

	/**
	 * Set the inner and outer radiuses of the node.
	 * 
	 * @param innerRange
	 *            - the distance from the center of the node to the inner
	 *            radius. Within this radius the ambient sounds are played at
	 *            full volume.
	 * @param fadeOutRange
	 *            - the distance between the inner and outer radiuses.
	 */
	public void setRanges(float innerRange, float fadeOutRange) {
		innerRadius.setTarget(innerRange);
		fadeOutRadius.setTarget(fadeOutRange);
	}

	/**
	 * @return The distance from the center of the node to the outer radius.
	 */
	public float getRange() {
		return innerRadius.get() + fadeOutRadius.get();
	}

	/**
	 * Sets the available sounds that this ambient node can play. Fades out any
	 * current sounds that are playing before playing any of the new sounds.
	 * 
	 * @param sounds
	 *            - the list of sounds.
	 */
	public void setSoundList(List<Sound> sounds) {
		this.sounds = sounds;
		if (controller != null) {
			controller.fadeOut();
		}
	}

	/**
	 * Sets a single sound for the ambient node to play on repeat. Fades out any
	 * current sounds that are playing before playing the new sound.
	 * 
	 * @param sound
	 *            - the sound to be played on repeat.
	 */
	public void setSound(Sound sound) {
		sounds.clear();
		sounds.add(sound);
		if (controller != null) {
			controller.fadeOut();
		}
	}
	
	public void clear(){
		sounds.clear();
		if (controller != null) {
			System.out.println("fading");
			controller.fadeOut();
		}
	}

	/**
	 * Adds a sound to the list of available ambient sounds for this node to
	 * play.
	 * 
	 * @param sound
	 *            - the new sound to be added to the list.
	 */
	public void addSound(Sound sound) {
		sounds.add(sound);
	}

	/**
	 * Removes a sound from the available list of sounds for this node.
	 * 
	 * @param sound
	 *            - the sound to remove.
	 */
	public void removeSound(Sound sound) {
		sounds.remove(sound);
	}
	
	/**
	 * Updates the position of the node, and of the current source if one is currently being used.
	 * @param x - the new x position.
	 * @param y - the new y position.
	 * @param z - the new z position.
	 */
	public void setPosition(float x, float y, float z){
		this.position.set(x, y, z);
		if(controller!=null){
			controller.setPosition(position);
		}
	}

	/**
	 * Checks whether the current ambient sound should stop being played because
	 * the listener has gone far enough away from the outer radius, otherwise
	 * the currently playing sound's {@link AudioController} is updated, and a
	 * new sound is played if the current sound has come to an end.
	 * 
	 * @param distance
	 *            - the distance of the {@link AudioListener} from the node's
	 *            center.
	 * @param delta
	 *            - the time in seconds since the last frame.
	 */
	private void updateActiveNode(float distance, float delta) {
		if (distance >= getRange() * RANGE_THRESHOLD) {
			controller.stop();
			active = false;
		} else {
			boolean stillPlaying = controller.update(delta);
			if (!stillPlaying && !sounds.isEmpty()) {
				playNewSound();
			}
		}
	}

	/**
	 * @return the distance between the {@link AudioListener} and the node's
	 *         center.
	 */
	private float getDistanceFromListener() {
		Vector3f listenerPos = SoundMaster.getListener().getPosition();
		return Vector3f.subtract(listenerPos, position).length();
	}

	/**
	 * Starts playing a random sound from the available sounds list.
	 */
	private void playNewSound() {
		Sound sound = chooseNextSound();
		PlayRequest request = PlayRequest.new3dSoundPlayRequest(sound, volume, position, innerRadius.get(), getRange());
		controller = SoundMaster.play3DSound(request);
		active = controller != null;
	}

	/**
	 * Chooses a random sound from the available sounds list, and if there's
	 * more than one available sound it ensures that the previously played sound
	 * isn't repeated.
	 * 
	 * @return The
	 */
	private Sound chooseNextSound() {
		Sound sound = null;
		int index = Maths.randRange(0, sounds.size());
		if (sounds.size() > 1 && sound == lastPlayed) {
			index = (index + 1) % sounds.size();
		}
		sound = sounds.get(index);
		lastPlayed = sound;
		return sound;
	}

	/**
	 * Updates the smooth floats to slowly change the radius values.
	 * 
	 * @param delta
	 *            - the time in seconds since the last frame.
	 */
	private void updateValues(float delta) {
		innerRadius.update(delta);
		fadeOutRadius.update(delta);
		if (controller != null) {
			// FIXME update ranges
		}
	}

}
