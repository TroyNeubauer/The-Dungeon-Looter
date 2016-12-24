package graphics.renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.troy.troyberry.logging.Timer;
import com.troy.troyberry.opengl.util.Window;

public class ScreenShotMaster {
	
	private static AtomicBoolean working = new AtomicBoolean(false);
	private static ByteBuffer buffer;
	private static final int BYTES_PER_PIXEL = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
	private static final String FORMAT = "png";
	
	
	/**
	 * Gets the pixels from the current frame on the default window
	 * @return an array of pixels in an ARGB format
	 */
	public static void takeAndSaveScreenShot() {
		if(working.get()) return;
		working.set(true);
		Timer t = new Timer();
		GL11.glReadBuffer(GL11.GL_FRONT);
		int width = Window.getInstance().getWidth();
		int height= Window.getInstance().getHeight();
		buffer = BufferUtils.createByteBuffer(width * height * BYTES_PER_PIXEL);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
		Thread saver = new Thread(fileSaver, "Thread for saving screenshots");
		saver.setPriority((Thread.MIN_PRIORITY + Thread.NORM_PRIORITY) / 2);
		saver.start();
		System.out.println("Screenshot took:");
		t.printTime();
	}
	
	private static Runnable fileSaver = new Runnable() {
		
		@Override
		public void run() {
			
			int width = Window.getInstance().getWidth();
			int height= Window.getInstance().getHeight();
			
			File file = new File("./screenshots/" + new SimpleDateFormat("MM-dd-yyyy HH_mm_ss.SSSS").format(new Date()) + "." + FORMAT);
			file.mkdirs();
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			   
			for(int x = 0; x < width; x++) 
			{
			    for(int y = 0; y < height; y++)
			    {
			        int i = (x + (width * y)) * BYTES_PER_PIXEL;
			        int r = buffer.get(i) & 0xFF;
			        int g = buffer.get(i + 1) & 0xFF;
			        int b = buffer.get(i + 2) & 0xFF;
			        image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
			    }
			}
			   
			try {
			    ImageIO.write(image, FORMAT, file);
			} catch (IOException e) { e.printStackTrace(); }
			
			
			
			working.set(false);
		}
	};

	private ScreenShotMaster() {
	}

}
