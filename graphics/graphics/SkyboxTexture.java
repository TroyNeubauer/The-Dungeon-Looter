package graphics;

import assets.Asset;
import assets.Assets;

public class SkyboxTexture extends Asset {

	public SkyboxTexture(String path) {
		super(path);
	}

	@Override
	public Asset load() {
		if (Assets.getToLoad().contains(this)) {
			return this;
		}
		return this;
	}

}
