package thedungeonlooter.gamestate;

import org.lwjgl.opengl.GL30;

import graphics.renderer.SplashRenderer;
import loader.Assets;
import thedungeonlooter.input.Controls;

public class TitleScreenState implements GameState {

	@Override
	public void render() {
		SplashRenderer.render();
		if (Controls.ESCAPE.hasBeenPressed()) {
			System.exit(0);
		}
		
		if (Controls.NEXT.hasBeenPressed()) {
			GameStateManager.setState(new WorldState());
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

	@Override
	public void cleanUp() {
		
	}

}
