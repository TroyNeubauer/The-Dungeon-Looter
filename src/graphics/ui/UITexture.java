package graphics.ui;

import com.troy.troyberry.math.Vector2f;
import graphics.Texture;
import graphics.gui.GuiTexture;
import loader.Loader;

public class UITexture extends UIComponent {

	private GuiTexture guiTexture;

	public UITexture(Vector2f position, Vector2f size, UIComponent parent, String path) {
		super(position, size, parent);
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		this.mesh = Loader.getLoader().loadToVAO(positions, 2);
		this.texture = new Texture(path, true);
		this.guiTexture = new GuiTexture(texture.id, position, size);
	}

	@Override
	public void onHover(Vector2f mousePosition) {
	}

	@Override
	public void onClick(Vector2f mousePosition) {
	}

	@Override
	public void render() {
		UIPanel panel = this.getTopLevel();
		panel.textures.add(guiTexture);
	}

	@Override
	public void update() {
	}

}
