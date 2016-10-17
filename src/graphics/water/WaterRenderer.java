package graphics.water;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.utils.CrashReport;
import entity.Camera;
import entity.Light;
import graphics.Mesh;
import graphics.Texture;
import loader.Loader;
import toolbox.Maths;

public class WaterRenderer {

	private static final String DUDV_MAP = "waterDUDV";
	private static final String NORMAL_MAP = "normal";
	private static final float WAVE_SPEED = 0.03f;

	private Mesh quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;

	private float moveFactor = 0;

	private int dudvTexture;
	private int normalMap;

	public WaterRenderer(WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		this.shader = shader;
		this.fbos = fbos;
		try {
			dudvTexture = new Texture(DUDV_MAP, true).id;
			normalMap = new Texture(NORMAL_MAP, true).id;
		} catch (Exception e) {
			new CrashReport("Loading water textures", e).print();
		}
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO();
	}

	public void render(List<WaterTile> water, Light sun) {
		prepareRender(sun);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.x, tile.height, tile.z), 0, 0, 0, tile.size);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	public void update() {
		moveFactor += 0.0004f;
	}

	private void prepareRender(Light sun) {
		shader.start();
		shader.loadViewMatrix(Camera.getCamera());
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionDepthTexture());
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private void unbind() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
		shader.stop();
	}

	private void setUpVAO() {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = Loader.getLoader().loadToVAO(vertices, 2);
	}

}
