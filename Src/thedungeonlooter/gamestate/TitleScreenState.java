package thedungeonlooter.gamestate;

import loader.asset.Assets;
import thedungeonlooter.input.Controls;

public class TitleScreenState implements GameState {

	@Override
	public void render() {
		Assets.loadNext();
		if (Assets.hasLoadedAll()) {
			GameStateManager.setState(new WorldState());
			
		}
		if (Controls.ESCAPE.hasBeenPressed()) {
			System.exit(0);
		}
	}

	@Override
	public void update(int updateCount) {
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onEnd() {
	}

}
