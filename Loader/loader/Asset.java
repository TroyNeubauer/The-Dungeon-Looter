package loader;

import loader.request.GlRequest;

public abstract class Asset implements GlRequest {
	
	protected int GLID;
	protected boolean read, loaded;
	private String description;
	
	public Asset(String description) {
		this.description = description;
		this.loaded = false;
		this.read = false;
	}
	
	public void bind(){
		if(!loaded && read) throw new IllegalStateException("Cannot bind an asset that hasent been loaded to Open GL!");
		if(!loaded && !read) throw new IllegalStateException("Cannot bind an asset that hasent been loaded al all!");
		onBind();
	}
	
	public void unbind(){
		onUnbind();
	}
	
	public void delete() {
		onDelete();
		this.GLID = -1;
		this.read = false;
		this.loaded = false;
	}
	
	protected abstract void onBind();
	
	protected abstract void onUnbind();
	
	protected abstract void onDelete();
	
	
	/**
	 * @return weather or not this asset has been loaded from its file (assuming this asset is loaded from a file)
	 */
	public boolean isRead() {
		return read;
	}
	
	/**
	 * @return weather or not this asset has been loaded to Open GL
	 */
	public boolean isLoaded() {
		return loaded;
	}
	
	public String getDescription() {
		return description;
	}

}
