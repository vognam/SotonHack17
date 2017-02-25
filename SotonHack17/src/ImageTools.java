import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 */

/**
 * @author Allen
 *
 */
public class ImageTools {
	
	/**
	 * Resize image if image is too big
	 * 
	 * @param img
	 * @return
	 */
	public static BufferedImage resizeImage(BufferedImage img) {
		int width = img.getWidth();
		int height = img.getHeight();
		
		int max = 1000; // max width or height
		
		if (width <= max && height <= max) { // if image is not too big
			return img;
		}
		
		int scaledWidth;
		int scaledHeight;
		
		if (width <= height) {
			scaledWidth = width * max / height;
			scaledHeight = 1000;
		} else {
			scaledWidth = 1000;
			scaledHeight = height * max / width;
		}
		Image tmp = img.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH);
		img = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = img.getGraphics();
		g.drawImage(tmp, 0, 0, null);
		return img;
	}
	
	/**
	 * 
	 * @param img
	 * @param caption
	 * @return
	 */
	public static BufferedImage drawCaption(BufferedImage img, String upperCaption, String lowerCaption) {
		Graphics g = img.getGraphics();
		int width = img.getWidth();
		int height = img.getHeight();
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 30));
		g.drawString(upperCaption, 10, 10);
		return img;
	}
	
	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("/Users/Allen/Documents/SotonHack17 Test/3.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//img = drawCaption(img, "test?", "test");
		img = resizeImage(img);
		try {
			ImageIO.write(img, "jpg", new File("/Users/Allen/Documents/SotonHack17 Test/output.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
