package particle;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;

import entity.Camera;
import graphics.Mesh;
import graphics.Texture;
import input.GameSettings;
import loader.Loader;

public class ParticleRenderer {

	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };

	private Mesh quad;
	private ParticleShader shader;

	protected ParticleRenderer(Matrix4f projectionMatrix) {
		quad = Loader.getLoader().loadToVAO(VERTICES, 2);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	protected void render(Map<Texture, List<Particle>> particles) {
		Matrix4f viewMatrix = Camera.getCamera().getViewMatrix();
		prepare();
		for (Texture texture : particles.keySet()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.id);
			for (Particle particle : particles.get(texture)) {
				if(Maths.approximateDistanceBetweenPoints(particle.position, Camera.getCamera().position) > GameSettings.RENDER_DISTANCE / 4f){
					continue;
				}
				updateModelViewMatrix(particle.position, particle.rotation, particle.scale, viewMatrix);
				shader.loadTextureCoordInfo(particle.textureOffset1, particle.textureOffset2, texture.getNumberOfRows(), particle.blend);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		finishRendering();
	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.translate(position);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));
		modelMatrix.scale(new Vector3f(scale, scale, scale));
		Matrix4f modelViewMatrix = Matrix4f.multiply(viewMatrix, modelMatrix, null);
		shader.loadModelViewMatrix(modelViewMatrix);
	}

	protected void cleanUp() {
		shader.cleanUp();
	}

	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}

	private void finishRendering() {
		shader.stop();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

	}

}
