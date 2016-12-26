package graphics.sky;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;

import loader.mesh.*;
import loader.texture.Texture;
import loader.texture.Texture2D;
import thedungeonlooter.camera.ICamera;
import thedungeonlooter.entity.light.Light;

public class SunRenderer {
	
	private static final float[] VERTICES = { -1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f };

	private Mesh quad;
	private Texture sunTexture;
	
	private SunShader shader;

	public SunRenderer(ICamera camera) {
		quad = new CustomMesh("Mesh for sun", new RawMeshData(VERTICES, 2));
		shader = new SunShader();
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.stop();
		sunTexture = new Texture2D("sun").loadCompletely();
	}
	
	public void render(ICamera camera, Light sun){
		prepare();
		updateModelViewMatrix(sun.getPosition(), 0, 25, camera.getViewMatrix());
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
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
		Matrix4f modelViewMatrix = Matrix4f.multiply(viewMatrix, modelMatrix);
		shader.loadModelViewMatrix(modelViewMatrix);
	}
	
	protected void cleanUp() {
		shader.cleanUp();
	}

	private void prepare() {
		shader.start();
		sunTexture.bindToUnit(0);
		quad.bind();
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
	}

	private void finishRendering() {
		shader.stop();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);

	}

}
