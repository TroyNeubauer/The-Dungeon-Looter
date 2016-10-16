package renderEngine;

import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import com.troy.troyberry.math.Matrix4f;
import entity.Entity;
import entity.EntityLiving;
import entity.EntityPlayer;
import graphics.Mesh;
import graphics.Texture;
import graphics.TexturedModel;
import graphics.shader.StaticShader;
import graphics.shadows.ShadowBox;
import graphics.shadows.ShadowMapMasterRenderer;
import input.GameSettings;
import toolbox.Maths;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadFogValues(GameSettings.FOG_DENSITY, GameSettings.FOG_GRADIENT);
		shader.loadShadowMapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		shader.loadPCFCount(GameSettings.SHADOW_PCF_COUNT);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadShadowDistance(ShadowBox.SHADOW_DISTANCE);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities, Matrix4f toShadowSpace, MasterRenderer renderer) {
		shader.loadToShadowSpace(toShadowSpace);
		shader.enableShadows(GameSettings.SHAWODS_ENABLED);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model, renderer);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				if (entity instanceof EntityLiving) {
					EntityLiving living = (EntityLiving) entity;
					if (entity instanceof EntityPlayer) {
						EntityPlayer player = (EntityPlayer) entity;
						player.render();
					}
					if (living.isDead()) continue;
				}
				if (entity.skipRender) continue;
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model, MasterRenderer renderer) {
		Mesh mesh = model.getRawModel();
		GL30.glBindVertexArray(mesh.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Texture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderer.getShadowMapTexture());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.position, entity.rotation.x, entity.rotation.y, entity.rotation.z,
			entity.scale);
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}

}
