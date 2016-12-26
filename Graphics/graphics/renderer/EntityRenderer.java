package graphics.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.opengl.util.GLMaths;

import graphics.shader.StaticShader;
import graphics.shadows.ShadowBox;
import graphics.shadows.ShadowMapMasterRenderer;
import graphics.sky.SkyMaster;
import loader.CompleteModel;
import loader.mesh.Mesh;
import loader.texture.Skin;
import thedungeonlooter.camera.CameraMaster;
import thedungeonlooter.camera.CameraMaster.CameraMode;
import thedungeonlooter.entity.Entity;
import thedungeonlooter.entity.player.EntityPlayer;
import thedungeonlooter.input.GameSettings;

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

	public void render(Map<CompleteModel, List<Entity>> entities, Matrix4f toShadowSpace) {
		MasterRenderer.disableCulling();
		shader.loadToShadowSpace(toShadowSpace);
		shader.enableShadows(GameSettings.SHAWODS_ENABLED);
		shader.loadSkyBlendFactor(SkyMaster.skyboxRenderer.blendFactor);
		for (CompleteModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				entity.render();
				if(entity instanceof EntityPlayer && CameraMaster.getMode().equals(CameraMode.FIRST_PERSON))continue;
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(CompleteModel model) {
		Mesh mesh = model.getRawModel();
		mesh.bind();
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		Skin skin = model.getSkin();
		
		shader.loadShineVariables(skin.getShineDamper(), skin.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		model.getTexture().bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MasterRenderer.getShadowMapTexture());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = GLMaths.createTransformationMatrix(entity.position, entity.rotation.x, entity.rotation.y, entity.rotation.z,
			entity.scale);
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
