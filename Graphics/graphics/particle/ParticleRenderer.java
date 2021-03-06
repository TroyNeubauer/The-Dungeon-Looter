package graphics.particle;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;

import loader.mesh.*;
import loader.texture.ParticleTexture;
import loader.texture.Texture;
import thedungeonlooter.camera.ICamera;

public class ParticleRenderer {

	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };

	private Mesh quad;
	private ParticleShader shader;

	protected ParticleRenderer(ICamera camera) {
		quad = new CustomMesh("Particle Mesh", new RawMeshData(VERTICES, 2));
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.stop();
	}

	protected void render(ICamera camera, Map<ParticleTexture, List<Particle>> particles) {
		Matrix4f viewMatrix = camera.getViewMatrix();
		prepare();
		for (ParticleTexture texture : particles.keySet()) {
			texture.bindToUnit(0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			for (Particle particle : particles.get(texture)) {
				updateModelViewMatrix(particle.position, particle.getRotation(), particle.getScale(), viewMatrix);
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
		quad.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
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
