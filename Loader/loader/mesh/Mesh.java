package loader.mesh;

import org.lwjgl.opengl.GL30;

import com.troy.troyberry.logging.Log;

import loader.*;
import loader.request.GlRequestProcessor;
import loader.request.RequestProcessor;

public abstract class Mesh extends Asset {
	
	private Vao vao;
	protected RawMeshData data;

	public Mesh(String description) {
		super(description);
	}

	@Override
	protected void onBind() {
		GL30.glBindVertexArray(GLID);
	}

	@Override
	protected void onUnbind() {
		GL30.glBindVertexArray(0);
	}

	@Override
	protected void onDelete() {
		vao.delete();
	}

	@Override
	public void executeGlRequest() {
		vao = Loader.loadToVAO(data);
		if(vao == null) Log.error("Could not load mesh " + getDescription());
		this.GLID = vao.getGLID();
		this.loaded = true;
		this.read = false;
		data = null; // Let GC do its work
	}

	public Vao getVao() {
		return vao;
	}
	public int getVertexCount(){
		return vao.getVertexCount();
	}
	
	/**
	 * Loads this to memory completely
	 * If this mesh is a mesh from a file, this method will read the file, then send the data to Open GL.
	 * If this is a custom mesh and the data is custom, this method assumes the data passed into that constructor
	 * is not null.
	 * @return This so that this method can be used in variable initialization
	 */
	public Mesh loadNow() {
		if(this instanceof OBJFileMesh){
			((OBJFileMesh)this).doResourceRequest();
		}
		this.executeGlRequest();
		return this;
	}
	
	/**
	 * Sends requests to both the GLrequest thread and resource thread to load this mesh
	 * @return This so that this method can be used in variable initialization
	 */
	public Mesh loadLater() {
		if(this instanceof OBJFileMesh){
			RequestProcessor.sendRequest(((OBJFileMesh)this));
		}
		GlRequestProcessor.sendRequest(this);
		return this;
	}


}
