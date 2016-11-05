package input;

import org.lwjgl.input.Keyboard;

import com.troy.troyberry.math.Vector2f;

/** This class will contain all of the controls for the game **/
public class Controls {

	/** A keybinding **/
	public static final KeyBinding FORWARD = new KeyBinding(Keyboard.KEY_W), BACKWARD = new KeyBinding(Keyboard.KEY_S),
			LEFT = new KeyBinding(Keyboard.KEY_A), RIGHT = new KeyBinding(Keyboard.KEY_D),
			TOGGLE_FULLSCREEN = new KeyBinding(Keyboard.KEY_BACKSLASH), UP = new KeyBinding(Keyboard.KEY_SPACE),
			DOWN = new KeyBinding(Keyboard.KEY_LSHIFT), KILL_PLAYER = new KeyBinding(Keyboard.KEY_HOME),
			TOGGLE_HOUR = new KeyBinding(Keyboard.KEY_P), NEXT = new KeyBinding(Keyboard.KEY_RETURN),
			ESCAPE = new KeyBinding(Keyboard.KEY_ESCAPE), SPRINT = new KeyBinding(Keyboard.KEY_LCONTROL);
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
