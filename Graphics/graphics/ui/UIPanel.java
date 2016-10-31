package graphics.ui;

import java.util.List;
import com.troy.troyberry.math.Vector2f;
import graphics.gui.GuiTexture;

public class UIPanel extends UIComponent {

	protected List<GuiTexture> textures;

	public UIPanel(Vector2f position, Vector2f size, UIComponent parent, List<GuiTexture> textures) {
		super(position, size, parent);
		this.textures = textures;
	}

	@Override
	public void onHover(Vector2f mousePosition) {
	}

	@Override
	public void onClick(Vector2f mousePosition) {
	}

	@Override
	public void render() {
		for (UIComponent c : children) {
			c.render();
		}
	}

	@Override
	public void update() {
		for (UIComponent c : children) {
			c.update();
		}
	}

}
