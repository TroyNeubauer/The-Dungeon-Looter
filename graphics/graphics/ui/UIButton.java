package graphics.ui;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;

import graphics.Texture;
import graphics.font.loader.GUIText;
import graphics.font.renderer.TextMaster;
import graphics.image.ImageRenderer;
import graphics.image.SizeableTexture;
import loader.Loader;
import main.Updater;

public class UIButton extends UIComponent {

	private SizeableTexture guiTexture;
	public float changeFactor;
	public int changeTime;
	public GUIText text;

	public UIButton(UI ui, Vector2f position, Vector2f size, Texture texture, GUIText text, float changeFactor, int changeTime) {
		super(ui, position, size);
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		this.mesh = Loader.getLoader().loadToVAO(positions, 2);
		this.texture = texture;
		this.text = text;
		this.guiTexture = new SizeableTexture(texture.id, position, size);
		this.changeFactor = changeFactor / 1000f;
		this.changeTime = changeTime;
	}

	@Override
	public void onHover(Vector2f mousePosition) {
		if(hoverListener != null)hoverListener.onHover(mousePosition);
		startScaleing(Vector2f.scale(this.size, 1.0f + changeFactor), changeTime);
	}

	@Override
	public void offHover(Vector2f mousePosition) {
		if(hoverListener != null)hoverListener.offHover(mousePosition);
		startScaleing(Vector2f.scale(this.size, 1.0f - changeFactor), changeTime);
	}

	@Override
	public void onClick(Vector2f mousePosition) {

	}

	@Override
	public void render() {
		ImageRenderer.render(guiTexture);
		TextMaster.removeText(text);
		TextMaster.loadText(text);
		
	}

	@Override
	public void update(int updateCount) {
		guiTexture.scale = new Vector2f(this.size);
		if (scaleing) {
			int stopTime = changeUPSStart + changeTimeUpdate;
			if (stopTime < updateCount) {
				scaleing = false;
				this.size = new Vector2f(desiredSize);
			} else {
				float ratio = (float) (updateCount - changeUPSStart) / (float) (stopTime - changeUPSStart);
				this.size = Vector2f.lerp(startingSize, desiredSize, ratio);
			}
		}
	}

	private Vector2f startingSize, desiredSize;
	private int changeUPSStart, changeTimeUpdate;
	private boolean scaleing = false;

	public void startScaleing(Vector2f desiredSize, int changeUPSAmount) {
		startingSize = new Vector2f(this.size);
		this.desiredSize = desiredSize;
		changeUPSStart = Updater.updateCount;
		changeTimeUpdate = changeUPSAmount;
		scaleing = true;
	}


}
