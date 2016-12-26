package graphics.postprocessing;

import org.lwjgl.opengl.*;

import loader.Loader;
import loader.mesh.*;

public class PostProcessing {

	public static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static Mesh quad;
	private static ContrastChanger contrastChanger;

	public static void init() {
		quad = new CustomMesh("Quad for post processing", new RawMeshData(POSITIONS, 2));
		contrastChanger = new ContrastChanger();
	}

	public static void doPostProcessing(int colorTexture) {
		start();
		contrastChanger.render(colorTexture);
		end();
	}

	public static void cleanUp() {
		contrastChanger.cleanUp();
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
