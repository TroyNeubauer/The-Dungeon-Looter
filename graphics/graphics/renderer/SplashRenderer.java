package graphics.renderer;

import org.lwjgl.opengl.*;

import com.troy.troyberry.opengl.util.Window;

import asset.Assets;
import asset.Mesh;
import loader.Loader;
import loader.LoadingScreenShader;

public class SplashRenderer {

	public static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static Mesh quad;
	private static LoadingScreenShader shader;

	public static void init() {
		quad = Loader.getLoader().loadToVAO(POSITIONS, 2);
		shader = new LoadingScreenShader();
		SplashRenderer.render();
	}

	public static void render() {
		start();
		shader.start();

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Assets.loadingTexture.getID());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		shader.start();
		end();
		Window.getInstance().update();
	}

	private static void start() {
		GL30.glBindVertexArray(quad.getID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
