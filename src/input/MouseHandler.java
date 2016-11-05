package input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MouseHandler {

	protected static List<MouseBinding> mouseBinds = new ArrayList<MouseBinding>();

	private MouseHandler() {
	}

	/** Called to update the list of keybinds **/
	public static void update() {
		Iterator<MouseBinding> i = mouseBinds.iterator();
		while (i.hasNext()) {
			MouseBinding button = (MouseBinding) i.next();
			if (button.isPressed() && button.lastPress == false) {
				button.pressed = true;
			}
			button.lastPress = button.isPressed();
		}
	}

	/** Resets all keybindings to their default setting **/
	public static void resetAllKeysBindings() {
		Iterator<MouseBinding> i = mouseBinds.iterator();
		while (i.hasNext()) {
			((MouseBinding) i.next()).resetKey();

		}
	}

	/** Sets all keys to be un-pressed **/
	public static void unPressAllButtons() {
		Iterator<MouseBinding> i = mouseBinds.iterator();
		while (i.hasNext()) {
			MouseBinding key = (MouseBinding) i.next();
			key.pressed = false;
			key.lastPress = false;
		}
	}

}
