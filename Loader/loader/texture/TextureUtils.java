package loader.texture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.*;

import com.troy.troyberry.opengl.texture.PNGDecoder;
import com.troy.troyberry.opengl.texture.PNGDecoder.Format;
import com.troy.troyberry.util.MyFile;

public class TextureUtils {

	public static int createEmptyCubeMap(int size) {
		int texID = GL11.glGenTextures();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		for (int i = 0; i < 6; i++) {
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA8, size, size, 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
		return texID;
	}
	
	/**
	 * Loads the 6 images for a cube map from 6 files
	 * @param files the files to load
	 * @return an array of texture data representing the images
	 */
	public static TextureData[] loadCubeMap(MyFile[] files){
		if(files.length != 6) throw new IllegalArgumentException("Skybox files must contain 6 files!");
		TextureData[] result = new TextureData[6];
		int i = 0;
		for(MyFile file : files){
			result[i]= decodeTextureFile(file);
			i++;
		}
		
		return result;
	}
	
	public static TextureData[] loadCubeMap(MyFile skyboxFolder) throws IOException {
		try {
			MyFile[] actualFiles = new MyFile[6];
			actualFiles[0] = new MyFile(skyboxFolder, "right");// "right", "left", "top", "bottom", "back", "front"
			actualFiles[1] = new MyFile(skyboxFolder, "left");
			actualFiles[2] = new MyFile(skyboxFolder, "top");
			actualFiles[3] = new MyFile(skyboxFolder, "bottom");
			actualFiles[4] = new MyFile(skyboxFolder, "back");
			actualFiles[5] = new MyFile(skyboxFolder, "front");
			return loadCubeMap(actualFiles);
		} catch (Exception e) {
			throw new IOException("Unable to find all cube map files in folder " + skyboxFolder.getPath());
		}
	}

	/**
	 * Loads a cube map to Open GL with 6 textures
	 * @param textureDatas the textures to use in the cube map
	 * @return The of the id of the new cube map's 3D texture
	 */
	public static int loadCubeMap(TextureData[] textureDatas) {
		if(textureDatas.length != 6) throw new IllegalArgumentException("Cannot load a cube map with " + textureDatas.length + " textures! Must be 6");
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		for (int i = 0; i < textureDatas.length; i++) {
			TextureData data = textureDatas[i];
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(),
					data.getHeight(), 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
		return texID;
	}
	
	/**
	 * Loads a new 2D PNG texture from a file
	 * @param file The image to load
	 * @return TextureData representing the PNG file
	 */
	protected static TextureData decodeTextureFile(MyFile file) {

		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		PNGDecoder decoder = null;
		try{
			InputStream in = file.getInputStream();
			if(in == null) throw new IOException("Couldn't find texture!");
			decoder = new PNGDecoder(in);

			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.BGRA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			System.err.println("Error in loading texture " + file.getPath());
			e.printStackTrace();
		}
		return new TextureData(buffer, width, height);
	}
	
	/**
	 * Loads a 2D texture to Open GL with the settings specified by the texture builder
	 * @param data The image data to load to open GL
	 * @param builder A texture builder to controls settings like mipmapping, clamping ect.
	 * @return The texture ID that repersents this texture or -1 if the creation failed
	 */
	protected static int loadTextureToOpenGL(TextureData data, TextureBuilder builder) {
		if(data == null) return -1;
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL12.GL_BGRA,
				GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		if (builder.isMipmap()) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			if (builder.isAnisotropic() && GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,
						4.0f);
			}
		} else if (builder.isNearest()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		}
		if (builder.isClampEdges()) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return texID;
	}

}
