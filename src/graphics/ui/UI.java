package graphics.ui;

import java.util.List;
import com.troy.troyberry.math.Vector2f;
import graphics.gui.GuiTexture;

public class UI {

	public Vector2f position, size;
	private UIPanel mainDrawable;
	List<GuiTexture> textures;

	public UI(Vector2f position, Vector2f size, List<GuiTexture> textures) {
		this.position = position;
		this.size = size;
		this.mainDrawable = new UIPanel(position, size, null, textures);
		this.textures = textures;
	}

	public void update() {
		mainDrawable.update();
	}

	public void render() {
		mainDrawable.render();
	}

	public UIPanel getTopLevel() {
		return mainDrawable;
	}

	public void add(UIComponent c) {
		mainDrawable.addChild(c);
	}

	public boolean isHovering() {
		return mainDrawable.hovering;
	}
}
