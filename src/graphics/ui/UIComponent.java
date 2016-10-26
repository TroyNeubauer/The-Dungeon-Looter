package graphics.ui;

import java.util.ArrayList;
import java.util.List;
import com.troy.troyberry.math.Vector2f;
import graphics.Mesh;
import graphics.Texture;

public abstract class UIComponent {

	public Vector2f position, size;
	protected boolean hovering = false, hidden = false;
	protected Mesh mesh;
	protected Texture texture;

	private UIComponent parent;
	protected List<UIComponent> children;

	public UIComponent(Vector2f position, Vector2f size, UIComponent parent) {
		children = new ArrayList<UIComponent>();
		this.position = position;
		this.size = size;
		this.parent = parent;
		if (parent != null) parent.addChild(this);
	}

	public abstract void onHover(Vector2f mousePosition);

	public abstract void onClick(Vector2f mousePosition);

	public abstract void render();

	public abstract void update();

	public void addChild(UIComponent c) {
		if (!children.contains(c)) children.add(c);
	}

	public UIPanel getTopLevel() {
		UIComponent c = this;
		while (!c.isTopLevel()) {
			c = c.getParent();
		}
		return (UIPanel) c;
	}

	public void hide() {
		if (isTopLevel()) return;
		hidden = true;
	}

	public void show() {
		hidden = false;
	}

	public UIComponent getParent() {
		return parent;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public boolean isTopLevel() {
		return parent == null;
	}

}
