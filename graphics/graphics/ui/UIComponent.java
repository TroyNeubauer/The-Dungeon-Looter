package graphics.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.troy.troyberry.math.Vector2f;

import graphics.Mesh;
import graphics.Texture;
import loader.Loader;

public abstract class UIComponent {

	public volatile Vector2f position, size;
	protected boolean hovering = false, hidden = false, hoveringLast = false;
	protected Mesh mesh;
	protected Texture texture;
	
	protected HoverListener hoverListener;
	protected ClickListener clickListener;

	public UIComponent(UI ui, Vector2f position, Vector2f size) {
		this.position = position.add(ui.position);
		this.size = size;
	}

	public void onHover(Vector2f mousePosition){
		if(hoverListener != null)hoverListener.onHover(mousePosition);
	}
	
	public void offHover(Vector2f mousePosition){
		if(hoverListener != null)hoverListener.offHover(mousePosition);
	}

	public void onClick(Vector2f mousePosionClicktion){
		if(clickListener != null)clickListener.onClick();
		System.out.println("clicked in here!");
	}

	public abstract void render();

	public abstract void update(int updateCount);
	
	public void addClickListener(ClickListener clickListener) {
		this.clickListener = clickListener;
	}
	
	public void addHoverListener(HoverListener hoverListener) {
		this.hoverListener = hoverListener;
	}
	


	public void hide() {
		hidden = true;
	}

	public void show() {
		hidden = false;
	}

	public void cleanUp() {
		Loader.getLoader().removeVAO(mesh.getVaoID());
		GL30.glDeleteVertexArrays(mesh.getVaoID());
		GL11.glDeleteTextures(texture.id);
		
	}

}
