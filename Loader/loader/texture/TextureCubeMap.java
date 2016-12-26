package loader.texture;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.troy.troyberry.util.MyFile;

public class TextureCubeMap extends Texture {
	
	private TextureData[] data;

	public TextureCubeMap(MyFile folder, String description) {
		super(new MyFile(description), GL13.GL_TEXTURE_CUBE_MAP);
	}

	public void onResourceRequest() {
		try {
			this.data = TextureUtils.loadCubeMap(path);
		} catch (IOException e) {
			System.err.println("Unable to load cube map in folder " + path.getPath());
		}
	}
	
	public boolean onExecuteGlRequest() {
		this.GLID = TextureUtils.loadCubeMap(data);
		return true;
	}

}
