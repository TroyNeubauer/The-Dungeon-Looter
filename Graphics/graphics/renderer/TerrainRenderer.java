package graphics.renderer;

import java.util.List;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.GLMaths;

import graphics.shader.TerrainShader;
import graphics.shadows.ShadowBox;
import graphics.shadows.ShadowMapMasterRenderer;
import graphics.sky.SkyMaster;
import loader.asset.Mesh;
import loader.asset.TerrainTexturePack;
import thedungeonlooter.input.GameSettings;
import thedungeonlooter.world.Terrain;

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
		shader.loadSkyBlendFactor(SkyMaster.skyboxRenderer.blendFactor);
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unbindTexturedModel();
		}
	}

	private void prepareTerrain(Terrain terrain) {
		Mesh rawModel = terrain.model;
		GL30.glBindVertexArray(rawModel.getID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
	}

	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.texturePack;
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.blendMap.getID());
	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void loadModelMatrix(Terrain terrain) {
		shader.loadTransformationMatrix(GLMaths.createTransformationMatrix(new Vector3f(terrain.x, 0, terrain.z), 0f, 0f, 0f, 1f));
	}

}
