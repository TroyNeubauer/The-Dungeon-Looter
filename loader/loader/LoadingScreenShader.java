package loader;

import graphics.shader.ShaderProgram;

public class LoadingScreenShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/graphics/shader/loading.vert";
	private static final String FRAGMENT_FILE = "/graphics/shader/loading.frag";

	public LoadingScreenShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
	}

	@Override
	protected void bindAttributes() {
	}

}
