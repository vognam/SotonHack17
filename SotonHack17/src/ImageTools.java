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
	public static BufferedImage drawCaption(BufferedImage img, String topCaption, String bottomCaption) {
		Graphics2D g = (Graphics2D)img.getGraphics();
		// make it smoother
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		
		int margin = 10;
		
		int width = img.getWidth() - margin * 2;
		int height = img.getHeight();
		
		// draw top caption
		int fontSize = 100;
		Font currentFont = new Font("Impact", Font.PLAIN, fontSize);
		FontMetrics fontMatric = g.getFontMetrics(currentFont);
		while (fontMatric.stringWidth(topCaption) > width && fontSize > 30) {
			fontSize--;
			currentFont = new Font("Impact", Font.PLAIN, fontSize);
			fontMatric = g.getFontMetrics(currentFont);
		}
		g.setFont(currentFont);
		
		int topX = (width - fontMatric.stringWidth(topCaption)) / 2 + margin;
		int topY = fontMatric.getHeight() - fontMatric.getDescent();
		
		// draw offsets
		int offset = 2;
		g.setColor(Color.BLACK);
		g.drawString(topCaption, topX - offset, topY - offset);
		g.drawString(topCaption, topX - offset, topY + offset);
		g.drawString(topCaption, topX + offset, topY - offset);
		g.drawString(topCaption, topX + offset, topY + offset);
		
		// draw text
		g.setColor(Color.WHITE);
		g.drawString(topCaption, topX, topY);
		
		// draw bottom caption
		fontSize = 100;
		currentFont = new Font("Impact", Font.PLAIN, fontSize);
		fontMatric = g.getFontMetrics(currentFont);
		while (fontMatric.stringWidth(bottomCaption) > width && fontSize > 30) {
			fontSize--;
			currentFont = new Font("Impact", Font.PLAIN, fontSize);
			fontMatric = g.getFontMetrics(currentFont);
		}
		g.setFont(currentFont);

		int bottomX = (width - fontMatric.stringWidth(bottomCaption)) / 2 + margin;
		int bottomY = height - fontMatric.getDescent();

		// draw offsets
		g.setColor(Color.BLACK);
		g.drawString(bottomCaption, bottomX - offset, bottomY - offset);
		g.drawString(bottomCaption, bottomX - offset, bottomY + offset);
		g.drawString(bottomCaption, bottomX + offset, bottomY - offset);
		g.drawString(bottomCaption, bottomX + offset, bottomY + offset);

		// draw text
		g.setColor(Color.WHITE);
		g.drawString(bottomCaption, bottomX, bottomY);
		
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
		img = resizeImage(img);
		img = drawCaption(img, "TEST ON A WHITE BACKGROUND. HOWEVER THIS LINE IS SO LONG!", "THIS IS ALSO A TEST. HOWEVER THIS LINE IS ALSO LONG XD.");
		try {
			ImageIO.write(img, "jpg", new File("/Users/Allen/Documents/SotonHack17 Test/output.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
