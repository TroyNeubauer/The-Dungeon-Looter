package loader.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.troy.troyberry.opengl.util.OpenGlUtil;
import com.troy.troyberry.util.MyFile;

import loader.Asset;
import loader.request.GlRequestProcessor;
import loader.request.ResourceRequest;

public abstract class Texture extends Asset implements ResourceRequest {
	
	protected MyFile path;
	protected final int type;

	public Texture(MyFile path, int type) {
		super(path.getName());
		this.path = path;
		this.type = type;
	}
	
	protected abstract void onResourceRequest();
	protected abstract boolean onExecuteGlRequest();
	
	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, GLID);
	}

	protected void onBind() {
		GL11.glBindTexture(type, GLID);
	}

	protected void onUnbind() {
		GL11.glBindTexture(type, 0);
	}

	protected void onDelete() {
		GL11.glDeleteTextures(GLID);
	}
	

	public void doResourceRequest() {
		this.read = true;
		this.loaded = false;
		onResourceRequest();
	}
	
	public void executeGlRequest() {
		OpenGlUtil.checkForErrors("Pre texture load...");
		if(onExecuteGlRequest()) {
			this.read = false;
			this.loaded = true;
		} else {
			System.err.println("Failed to load texture to open GL " + path.getPath());
			GlRequestProcessor.sendRequest(this);
		}
		OpenGlUtil.checkForErrors("Post image load");
	}
	
}
