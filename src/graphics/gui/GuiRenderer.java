package graphics.gui;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.math.Matrix4f;
import graphics.Mesh;
import loader.Loader;
import utils.MathUtil;

public class GuiRenderer {

	private final Mesh quad;
	private GuiShader shader;

	public GuiRenderer() {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = Loader.getLoader().loadToVAO(positions, 2);
		shader = new GuiShader();
	}

	public void render(List<GuiTexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for (GuiTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.texture);
			Matrix4f matrix = MathUtil.createTransformationMatrix(gui.position, gui.scale);
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
