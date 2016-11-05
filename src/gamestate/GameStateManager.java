package gamestate;

import graphics.font.renderer.TextMaster;
import input.KeyHandler;

public class GameStateManager {

	private static GameState currentState = null;
	private static Object renderLock = new Object(), updateLock = new Object();

	public static void start(GameState startingState) {
		synchronized (renderLock) {
			synchronized (updateLock) {
				KeyHandler.unPressAllKeys();
				TextMaster.clear();
				currentState = startingState;
				startingState.onStart();
			}
		}
	}

	public static void setState(GameState newState) {
		synchronized (renderLock) {
			synchronized (updateLock) {
				currentState.onEnd();
				KeyHandler.unPressAllKeys();
				TextMaster.clear();
				newState.onStart();
				currentState = newState;
			}
		}
	}

	public static void render() {
		synchronized (renderLock) {
			currentState.render();
		}
	}

	public static void update(int updateCount) {
		synchronized (updateLock) {
			currentState.update(updateCount);
		}
	}

}
