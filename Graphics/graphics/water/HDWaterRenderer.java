package graphics.water;

import java.util.List;

import org.lwjgl.opengl.*;

import com.troy.troyberry.math.Matrix4f;
import com.troy.troyberry.math.Vector3f;
import com.troy.troyberry.opengl.util.GLMaths;

import loader.Loader;
import loader.mesh.*;
import thedungeonlooter.camera.ICamera;

public class HDWaterRenderer {

	private Mesh quad;
	private HDWaterShader shader;
	private WaterFrameBuffers fbos;

	public HDWaterRenderer(ICamera camera, WaterFrameBuffers fbos) {
		this.shader = new HDWaterShader();
		this.fbos = fbos;
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.stop();
		setUpVAO();
	}

	public void render(List<WaterTile> waters, ICamera camera) {
		prepareRender(camera);
		for(WaterTile water : waters){
			Matrix4f modelMatrix = GLMaths.createTransformationMatrix(new Vector3f(water.getX(), water.getHeight(), water.getZ()), 0, 0, 0, water.getRadius());
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(ICamera camera){
		shader.start();
		shader.loadViewMatrix(camera);
		quad.bind();
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbos.getRefractionTexture());
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO() {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = new CustomMesh("Quad for water", new RawMeshData(vertices, 2));
	}

}
