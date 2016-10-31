package graphics.renderer;

import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import graphics.Mesh;
import graphics.TerrainTexturePack;
import graphics.shader.TerrainShader;
import graphics.shadows.ShadowBox;
import graphics.shadows.ShadowMapMasterRenderer;
import input.GameSettings;
import utils.MathUtil;
import world.Terrain;

public class TerrainRenderer {

	private TerrainShader shader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadShadowMapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		shader.loadPCFCount(GameSettings.SHADOW_PCF_COUNT);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadShadowDistance(ShadowBox.SHADOW_DISTANCE);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(List<Terrain> terrains, Matrix4f toShadowSpace) {
		shader.loadFogValues(GameSettings.FOG_DENSITY, GameSettings.FOG_GRADIENT);
		shader.loadToShadowSpace(toShadowSpace);
		shader.enableShadows(GameSettings.SHAWODS_ENABLED);
		shader.loadSkyBlendFactor(MasterRenderer.skyboxRenderer.blendFactor);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}

	private void prepareTerrain(Terrain terrain) {
		Mesh rawModel = terrain.model;
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
	}

	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.texturePack;
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.blendMap.getTextureID());
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		shader.loadTransformationMatrix(MathUtil.createTransformationMatrix(new Vector3f(terrain.x, 0, terrain.z), 0f, 0f, 0f, 1f));
	}

}
