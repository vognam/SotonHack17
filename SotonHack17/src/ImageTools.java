import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.http.entity.ByteArrayEntity;

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
	 * @return
	 * 
	 * To send this with http request:
	 *     setHeader("Content-Type", "application/octet-stream");
	 *     request.setEntity(ByteArrayEntity);
	 */
	public static ByteArrayEntity getBinary(BufferedImage img) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "jpg", stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] imageByte = stream.toByteArray();
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ByteArrayEntity(imageByte);
	}
	
	/**
	 * 
	 * @param img
	 * @param caption
	 * @return
	 */
	public static BufferedImage drawCaption(BufferedImage img, String upperCaption, String lowerCaption) {
		Graphics2D g = (Graphics2D)img.getGraphics();
		// make it smoother
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		// draw top caption
		int fontSize = 100;
		Font currentFont = new Font("Impact", Font.PLAIN, fontSize);
		FontMetrics fontMatric = g.getFontMetrics(currentFont);
		while (fontMatric.stringWidth(upperCaption) > width) {
			fontSize--;
			//System.out.println(fontSize);
			currentFont = new Font("Impact", Font.PLAIN, fontSize);
			fontMatric = g.getFontMetrics(currentFont);
		}
		
		int topX = (width - fontMatric.stringWidth(upperCaption)) / 2;
		int topY = fontMatric.getHeight() - fontMatric.getDescent();
		
		g.setFont(currentFont);
		g.setColor(Color.WHITE);
		
		g.drawString(upperCaption, topX, topY);
		return img;
	}
	
	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("/Users/Allen/Documents/SotonHack17 Test/2.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img = resizeImage(img);
		img = drawCaption(img, "testhahahahhahahahahahahah?", "test");
		try {
			ImageIO.write(img, "jpg", new File("/Users/Allen/Documents/SotonHack17 Test/output.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
