package input;

import com.troy.troyberry.opengl.input.Mouse;

public class MouseBinding {

	/** The current key code of this keybinding **/
	public int butttonNumber;

	/** The default key code of this keybinding **/
	public final int defaultButtonId;

	/** weather or not the key has just been presses **/
	protected boolean pressed;

	/** Was this key pressed last update **/
	protected boolean lastPress;

	public MouseBinding(int butttonNumber) {
		this.butttonNumber = butttonNumber;
		this.defaultButtonId = butttonNumber;
		this.pressed = false;
		this.lastPress = false;
		MouseHandler.mouseBinds.add(this);
	}

	/**
	 * @return true if the button on the mouse is physically down otherwise, false
	 */
	public boolean isPressed() {
		return Mouse.isButtonDown(butttonNumber - 1);
	}

	/**
	 * @return true if this is the first time that this method has been called since the mousebinding was pressed
	 */
	public boolean hasBeenPressed() {
		if (pressed) {
			pressed = false;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("KeyBinding:\n");
		sb.append("current button " + butttonNumber + "\n");
		sb.append("default button " + defaultButtonId + "\n");

		return sb.toString();
	}

	/** Gets the human readable name associated with this key binding **/
	public String getKeyName() {
		return "Button " + (butttonNumber);
	}

	/** Resets this keybinding to the default key **/
	public void resetKey() {
		this.butttonNumber = defaultButtonId;
	}

}
