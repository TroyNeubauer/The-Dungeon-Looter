package graphics.renderer;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector4f;
import com.troy.troyberry.opengl.util.GLMaths;

import graphics.shader.NormalMappingShader;
import graphics.shadows.ShadowBox;
import graphics.shadows.ShadowMapMasterRenderer;
import loader.CompleteModel;
import loader.mesh.Mesh;
import loader.texture.Texture;
import thedungeonlooter.camera.ICamera;
import thedungeonlooter.entity.Entity;
import thedungeonlooter.entity.light.Light;
import thedungeonlooter.input.GameSettings;

public class NormalMappingRenderer {

	private NormalMappingShader shader;

	public NormalMappingRenderer(Matrix4f projectionMatrix) {
		this.shader = new NormalMappingShader();
		shader.start();
		shader.loadFogValues(GameSettings.FOG_DENSITY, GameSettings.FOG_GRADIENT);
		shader.loadShadowMapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		shader.loadPCFCount(GameSettings.SHADOW_PCF_COUNT);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadShadowDistance(ShadowBox.SHADOW_DISTANCE);
		shader.connectTextureUnits();
		shader.stop();
	}

	public void render(Map<CompleteModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, ICamera camera, Matrix4f toShadowSpace) {

		shader.start();
		shader.loadToShadowSpace(toShadowSpace);
		shader.enableShadows(GameSettings.SHAWODS_ENABLED);
		prepare(clipPlane, lights, camera);
		for (CompleteModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				entity.render();
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	private void prepareTexturedModel(CompleteModel model) {
		Mesh mesh = model.getRawModel();
		mesh.bind();
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		Texture texture = model.getTexture();
		shader.loadShineVariables(model.getSkin().getShineDamper(), model.getSkin().getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		model.getTexture().bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		model.getNormalMap().bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MasterRenderer.getShadowMapTexture());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = GLMaths.createTransformationMatrix(entity.position, entity.rotation.x, entity.rotation.y, entity.rotation.z,
			entity.scale);
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadShineVariables(entity.getSkin().getShineDamper(), entity.getSkin().getReflectivity());
	}

	private void prepare(Vector4f clipPlane, List<Light> lights, ICamera camera) {
		MasterRenderer.disableCulling();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColour(MasterRenderer.RED, MasterRenderer.GREEN, MasterRenderer.BLUE);

		shader.loadLights(lights, camera.getViewMatrix());
		shader.loadViewMatrix(camera.getViewMatrix());
	}

}
