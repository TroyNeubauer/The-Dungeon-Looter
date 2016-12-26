package graphics.renderer;

import org.lwjgl.opengl.*;

import com.troy.troyberry.opengl.util.Window;

import loader.LoadingScreenShader;
import loader.mesh.*;

public class SplashRenderer {

	public static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static Mesh quad;
	private static LoadingScreenShader shader;

	public static void init() {
		quad = new CustomMesh("Splash Renderer Quad", new RawMeshData(POSITIONS, 2)).loadNow();
		shader = new LoadingScreenShader();
	}

	public static void render() {
		start();
		shader.start();
		//texture.bind();
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		shader.stop();
		end();
		Window.getInstance().update();
	}

	private static void start() {
		quad.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

}
