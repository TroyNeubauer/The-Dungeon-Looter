package assets;

public abstract class Asset {
	
	public boolean loaded;
	public final String path;
	public static final String ALLREADY_LOADED = "Allready Loaded";
	
	/**
	 * Creates a Asset object and adds  it to the list of assets to load later.
	 */
	public Asset(String path) {
		this.path = path;
		loaded = false;
		Assets.add(this);
	}
	/**
	 * Creates a Asset object and DOESN'T add it to the list of assets to load later.
	 * It is assumed that this Asset has already been loaded.
	 */
	public Asset() {
		this.path = ALLREADY_LOADED;
		loaded = true;
	}

	public abstract Asset load();

}
