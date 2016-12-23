package graphics.sky;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Vector3f;

import loader.Loader;
import loader.asset.Mesh;
import thedungeonlooter.camera.ICamera;
import thedungeonlooter.world.World;

public class SkyboxRenderer {

	private static final float SKYBOX_SIZE = 500f;
	public float blendFactor = 0f;

	private static final float[] VERTICES = { -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,
		-SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,

		-SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE,

		SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,

		-SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE,

		-SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,

		-SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE };

	private static final String[] NIGHT_TEXTURE_FILES = { "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront", "night" };

	private Mesh cube;
	private int nightTexture;
	private SkyboxShader shader;
	public World world;

	public SkyboxRenderer(World world, ICamera camera) {
		this.world = world;
		cube = Loader.loadToVAO(VERTICES, 3);
		nightTexture = Loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.stop();
	}

	public void render(ICamera camera, float r, float g, float b) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadSkyColor(new Vector3f(r, g, b));
		GL30.glBindVertexArray(cube.getID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void bindTextures() {
		int texture1;
		int texture2;
		if (world.time >= World.END_SUNRISE && world.time < World.START_SUNSET) {
			blendFactor = 0;
		} else if (world.time >= World.START_SUNSET && world.time < World.END_SUNSET) {
			blendFactor = (world.time - World.START_SUNSET) / (World.END_SUNSET - World.START_SUNSET);
		} else if (world.time >= World.END_SUNSET || world.time < World.START_SUNRISE) {
			blendFactor = 1;
		} else {
			blendFactor = 1 - (world.time - World.START_SUNRISE) / (World.END_SUNRISE - World.START_SUNRISE);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, nightTexture);
		shader.loadBlendFactor(blendFactor);
		world.blendFactor = blendFactor;
	}

}
