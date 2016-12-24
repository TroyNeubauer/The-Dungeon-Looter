package thedungeonlooter.gamestate;

import com.troy.troyberry.opengl.input.KeyHandler;

public class GameStateManager {

	private static GameState currentState = null;

	public static void start(GameState startingState) {
		KeyHandler.unPressAllKeys();
		currentState = startingState;
		startingState.onStart();
	}

	public static void setState(GameState newState) {
		currentState.onEnd();
		KeyHandler.unPressAllKeys();
		newState.onStart();
		currentState = newState;
	}

	public static void render() {
		currentState.render();
	}

	public static void update(int updateCount) {
		currentState.update(updateCount);
	}

}
