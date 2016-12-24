package thedungeonlooter.input;

import com.troy.troyberry.opengl.input.*;

/** This class will contain all of the controls for the game **/
public class Controls {

	/** A keybinding **/
	public static final KeyBinding FORWARD = new KeyBinding(Keyboard.KEY_W), BACKWARD = new KeyBinding(Keyboard.KEY_S),
		LEFT = new KeyBinding(Keyboard.KEY_A), RIGHT = new KeyBinding(Keyboard.KEY_D),
		TOGGLE_FULLSCREEN = new KeyBinding(Keyboard.KEY_BACKSLASH), UP = new KeyBinding(Keyboard.KEY_SPACE), 
		KILL_PLAYER = new KeyBinding(Keyboard.KEY_HOME), TOGGLE_HOUR = new KeyBinding(Keyboard.KEY_P), 
		NEXT = new KeyBinding(Keyboard.KEY_ENTER), ESCAPE = new KeyBinding(Keyboard.KEY_ESCAPE), 
		DEBUG_MENU = new KeyBinding(Keyboard.KEY_F3), CHANGE_VIEW = new KeyBinding(Keyboard.KEY_F5),
		TAKE_SCREENSHOT = new KeyBinding(Keyboard.KEY_F2); 
	/** A mouse bindings*/
	public static final MouseBinding SELECT = new MouseBinding(1), MOSUE_SPRINT = new MouseBinding(5);
	
	public static final KeyBinding[] DIRECT_MOVEMENT = {Controls.FORWARD, Controls.BACKWARD, Controls.LEFT, Controls.RIGHT};

	public static void init() {

	}

	public static boolean isPressingMoreThenAmount(KeyBinding[] keyBindings, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount cant be negative! " + amount);
		}
		int count = 0;
		for (KeyBinding i : keyBindings) {
			if (i.isPressed())
				count++;
		}
		return count > amount;
	}
}
