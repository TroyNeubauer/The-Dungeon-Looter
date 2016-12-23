package graphics.postprocessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import com.troy.troyberry.math.Vector3f;

public class ContrastChanger {

	private ImageRenderer renderer;
	private ContrastShader shader;
	public static float contrast = 0.1f;
	public static Vector3f add = new Vector3f(0, 0, 0);

	public ContrastChanger() {
		this.renderer = new ImageRenderer();
		this.shader = new ContrastShader();
	}

	public void render(int texture) {
		shader.start();
		shader.loadContrast(contrast);
		shader.loadAdd(add);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderSquare();
		shader.start();
	}

	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}

}
