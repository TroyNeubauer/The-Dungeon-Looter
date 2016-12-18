package graphics.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.math.Matrix4f;

import asset.Mesh;
import entity.Camera;
import loader.Loader;
import world.World;

public class SkyboxRenderer {

	private static final float SIZE = 500f;
	public float blendFactor = 0f;

	private static final float[] VERTICES = { -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE,
		-SIZE, SIZE, -SIZE,

		-SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE,

		SIZE, -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE,

		-SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE, -SIZE, SIZE,

		-SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,

		-SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, -SIZE, SIZE, -SIZE, -SIZE, -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE };

	private static final String[] NIGHT_TEXTURE_FILES = { "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront", "night" };

	private Mesh cube;
	private int nightTexture;
	private SkyboxShader shader;
	public World world;

	public SkyboxRenderer(Matrix4f projectionMatrix) {
		cube = Loader.getLoader().loadToVAO(VERTICES, 3);
		nightTexture = Loader.getLoader().loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Camera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		GL30.glBindVertexArray(cube.getID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures() {
		world.time %= 24000;
		int texture1;
		int texture2;
		if (world.time >= 8000 && world.time < 19500) {
			blendFactor = 0;
		} else if (world.time >= 19500 && world.time < 21000) {
			blendFactor = (world.time - 19500) / (21000 - 19500);
		} else if (world.time >= 21000 || world.time < 5500) {
			blendFactor = 1;
		} else {
			blendFactor = 1 - (world.time - 5500) / (8000 - 5500);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
		shader.loadBlendFactor(blendFactor);
	}

}
