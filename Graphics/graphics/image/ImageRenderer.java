package graphics.image;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.opengl.util.GLMaths;
import com.troy.troyberry.opengl.util.OpenGlUtil;

import loader.mesh.*;

public class ImageRenderer {
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static final Mesh QUAD = new CustomMesh("Image renderer quad", new RawMeshData(POSITIONS, 2)).loadNow();
	private static ImageShader shader = new ImageShader();
	private static volatile List<GLTexture> guis = new ArrayList<GLTexture>();

	public static void render() {
		shader.start();
		QUAD.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GLTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.texture);
			Matrix4f matrix = GLMaths.createTransformationMatrix(gui.position, gui.scale, 0);
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		guis.clear();
	}

	public static void render(GLTexture texture) {
		shader.start();
		QUAD.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.texture);
		Matrix4f matrix = GLMaths.createTransformationMatrix(texture.position, texture.scale, 0);
		shader.loadTransformation(matrix);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, QUAD.getVertexCount());

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		guis.clear();
	}

	public static void cleanUp() {
		shader.cleanUp();
	}

	public static void add(GLTexture texture) {
		if (texture != null) {
			guis.add(texture);
		}
	}

	public static void remove(GLTexture texture) {
		if (texture != null) {
			guis.remove(texture);
		}
	}
}
