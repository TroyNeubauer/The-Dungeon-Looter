package gamestate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.troy.troyberry.math.Vector2f;

import assets.Assets;
import graphics.Texture;
import graphics.font.loader.GUIText;
import graphics.font.renderer.TextMaster;
import graphics.image.ImageRenderer;
import graphics.image.SizeableTexture;
import graphics.ui.ClickListener;
import graphics.ui.UI;
import graphics.ui.UIButton;
import input.Controls;
import main.Version;

public class TitleScreenState implements GameState {

	private UI ui;

	@Override
	public void render() {
		Assets.loadNext();
		ui.render();
		Display.sync(30);
		if (Controls.NEXT.hasBeenPressed()) {
			GameStateManager.setState(new WorldState());
		}
		if (Controls.ESCAPE.hasBeenPressed()) {
			System.exit(0);
		}
	}

	@Override
	public void update(int updateCount) {
		ui.update(updateCount);
		if(Controls.SELECT.hasBeenPressed()){
			System.out.println("click");
		}
	}

	@Override
	public void onStart() {
		ui = new UI(new Vector2f(0f, 0f), new Vector2f(1f, 1f));
		GUIText playButton = new GUIText("Play", 2, Assets.font, new Vector2f(0, 0.5f), 1f, true);
		UIButton play = new UIButton(ui, new Vector2f(0, 0.5f), new Vector2f(0.5f, 0.5f),
				new Texture(Texture.createColoredImage(0xffff00ff, 2, 2)), playButton, 17f, 27);
		play.addClickListener(new ClickListener() {
			
			@Override
			public void onClick() {
				System.out.println("CLicked!!!!!!!!!");
				
			}
		});
		ui.add(play);

		GUIText tdlText = new GUIText(Version.getName(), 3, Assets.font, new Vector2f(0, 0.1f), 1f, true);
		GUIText playText = new GUIText("Press Enter to Play", 2, Assets.font, new Vector2f(0, 0.8f), 1f, true);
		TextMaster.loadText(tdlText);
		TextMaster.loadText(playText);

	}

	@Override
	public void onEnd() {
		ui.cleanUp();
	}

}
