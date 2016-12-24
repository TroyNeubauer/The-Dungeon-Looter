package loader.asset;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.*;

import com.troy.troyberry.logging.Log;
import com.troy.troyberry.opengl.util.BufferUtils;

public class Texture extends Asset {

	private int normalMap;

	private float shineDamper = 1;
	private float reflectivity = 0;

	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;

	private int numberOfRows = 1;
	public boolean hasNormalMap = false;

	private int width, height;
	private int filter;
	private float mipMappingBias;

	public Texture(String path) {
		this(path, GL_LINEAR, -1, 1);
	}

	public Texture(String name, boolean b) {
		this("/textures/" + name + ".png");
	}

	public Texture(String path, int filter, float mipMappingBias, int numberOfRows) {
		super(path);
		this.filter = filter;
		this.mipMappingBias = mipMappingBias;
		this.numberOfRows = numberOfRows;
	}

	public static BufferedImage createColoredImage(int color, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = color;
		}
		return image;
	}

	public Texture(int GLID) {
		super(GLID);
		loaded = true;
	}

	public Texture(BufferedImage image) {
		super();
		this.load();
	}

	private static int create(BufferedImage image, int filter, float mipMappingBias, Texture texture) {
		if (image == null)
			Log.warning("Trying to load a texture to open gl that is null!");
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE,
				BufferUtils.createIntBuffer(data));

		GL30.glGenerateMipmap(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, mipMappingBias);
		glBindTexture(GL_TEXTURE_2D, 0);
		if (texture != null) {
			texture.width = width;
			texture.height = height;
		}
		return result;
	}

	public static BufferedImage read(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Class.class.getResourceAsStream(path));

		} catch (Exception e) {
			Log.warning("Unable to load texture " + path);
			e.printStackTrace();
		}
		return image;
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, GLID);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap.GLID;
		this.hasNormalMap = true;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean hasTransparency() {
		return hasTransparency;
	}

	public boolean usesFakeLighting() {
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public static TextureData loadTextureData(String path) {
		int[] pixels = null;
		int width, height;

		BufferedImage image = read(path);
		width = image.getWidth();
		height = image.getHeight();
		pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}

		int result = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, result);
		IntBuffer buffer = BufferUtils.createIntBuffer(data);
		return new TextureData(buffer, width, height);
	}

	@Override
	public Asset load() {
		if (!loaded) {
			System.out.println("Loading texture " + path + "  to Open GL!");
			GLID = create(read(path), filter, mipMappingBias, this);
			loaded = true;
		}
		return this;
	}

	@Override
	public void delete() {
		GL11.glDeleteTextures(GLID);
	}

}
