package graphics.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;

import input.Controls;
import utils.MiscUtil;

public class UI {

	public Vector2f position, size;
	List<UIComponent> children;

	public UI(Vector2f position, Vector2f size) {
		this.position = position;
		this.size = size;
		this.children = new ArrayList<UIComponent>();
	}

	public void update(int updateCount) {
		for (UIComponent c : children) {
			if (Maths.inRange(Mouse.getX(),
					MiscUtil.xToIntCoords(position.x, Display.getWidth())
							- MiscUtil.xToIntWidth(c.size.x, Display.getWidth()),
					MiscUtil.xToIntCoords(position.x, Display.getWidth())
							+ MiscUtil.xToIntWidth(c.size.x, Display.getWidth()))
					&& Maths.inRange(Mouse.getY(),
							MiscUtil.xToIntCoords(position.y, Display.getHeight())
									- MiscUtil.xToIntWidth(c.size.y, Display.getHeight()),
							MiscUtil.xToIntCoords(position.y, Display.getHeight())
									+ MiscUtil.xToIntWidth(c.size.y, Display.getHeight()))) {
				c.hovering = true;
				if(!c.hoveringLast){
					System.out.println("now hovering");
					c.onHover(MiscUtil.getMouse());
 				}
				if(Controls.SELECT.hasBeenPressed()){
					c.onClick(MiscUtil.getMouse());
				}
			} else {
				c.hovering = false;
				if(c.hoveringLast){
					c.offHover(MiscUtil.getMouse());
				}
			}
			c.update(updateCount);

			c.hoveringLast = c.hovering;
		}
	}

	public void render() {
		for (UIComponent c : children) {
			c.render();
		}
	}

	public void add(UIComponent c) {
		children.add(c);
	}

	public void cleanUp() {
		for (UIComponent c : children) {
			c.cleanUp();
		}
		
	}
}
