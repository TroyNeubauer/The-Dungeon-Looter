package input;

import java.util.*;

public class KeyHandler {

	protected static List<KeyBinding> keybinds = new ArrayList<KeyBinding>();

	private KeyHandler() {
	}
	
	/** Called to update the list of keybinds.Should only be called on the Open GL thread **/
	public static void update() {
		Iterator<KeyBinding> i = keybinds.iterator();
		while (i.hasNext()) {
			KeyBinding key = (KeyBinding) i.next();
			if (key.isPressed() && key.lastPress == false) {
				key.pressed = true;
			}
			key.isPressedNow = key.isPressed();
			key.lastPress = key.isPressed();
		}
	}
	
	/** Resets all keybindings to their default setting **/
	public static void resetAllKeysBindings() {
		Iterator<KeyBinding> i = keybinds.iterator();
		while (i.hasNext()) {
			((KeyBinding) i.next()).resetKey();

		}
	}
	
	/** Sets all keys to be un-pressed **/
	public static void unPressAllKeys() {
		Iterator<KeyBinding> i = keybinds.iterator();
		while (i.hasNext()) {
			KeyBinding key = (KeyBinding) i.next();
			key.pressed = false;
			key.lastPress = false;
		}
	}
}
