package loader.texture;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.sun.corba.se.impl.ior.ByteBuffer;
import com.troy.troyberry.opengl.texture.PNGDecoder;
import com.troy.troyberry.opengl.texture.PNGDecoder.Format;
import com.troy.troyberry.util.MyFile;

import loader.request.GlRequestProcessor;
import loader.request.RequestProcessor;

public class Texture2D extends Texture {
	
	private TextureData data;
	private TextureBuilder builder;
	private static final String RES_LOCATION = "/textures/";

	public Texture2D(MyFile path, TextureBuilder builder) {
		super(path, GL11.GL_TEXTURE_2D);
		this.builder = builder;
	}
	
	public Texture2D(MyFile path) {
		this(path, TextureBuilder.defaultBuilder());
	}
	
	public Texture2D(String path) {
		this(new MyFile(RES_LOCATION + path + ".png"), TextureBuilder.defaultBuilder());
	}
	
	public Texture2D(String path, TextureBuilder builder) {
		this(new MyFile(RES_LOCATION + path + ".png"), builder);
	}
	
	public Texture2D loadCompletely(){
		this.doResourceRequest();
		this.executeGlRequest();
		return this;
	}
	
	public Texture2D loadLater(){
		RequestProcessor.sendRequest(this);
		GlRequestProcessor.sendRequest(this);
		return this;
	}

	public void onResourceRequest() {
		this.data = TextureUtils.decodeTextureFile(path);
	}
	
	public boolean onExecuteGlRequest() {
		this.GLID = TextureUtils.loadTextureToOpenGL(data, builder);
		return (GLID != -1);
	}
}
