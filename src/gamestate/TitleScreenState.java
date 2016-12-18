package gamestate;

import assets.Assets;
import input.Controls;

public class TitleScreenState implements GameState {


	@Override
	public void render() {
		Assets.loadNext();
		if (Controls.NEXT.hasBeenPressed() && Assets.hasLoadedAll()) {
			GameStateManager.setState(new WorldState());
			
		}
		if (Controls.ESCAPE.hasBeenPressed()) {
			System.exit(0);
		}
	}

	@Override
	public void update(int updateCount) {
		if(Controls.SELECT.hasBeenPressed()){
			System.out.println("click");
		}
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onEnd() {
	}

}
