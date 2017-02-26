import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	 * 		To send this with http request: setHeader("Content-Type",
	 *         "application/octet-stream"); request.setEntity(ByteArrayEntity);
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
	public static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.getLocation().x+(rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.getLocation().y+((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    
	    // draw offsets
	    int offset = 2;
		g.setColor(Color.BLACK);
		g.drawString(text, x - offset, y - offset);
		g.drawString(text, x - offset, y + offset);
		g.drawString(text, x + offset, y - offset);
		g.drawString(text, x + offset, y + offset);
	    
	    // Draw the String
		g.setColor(Color.WHITE);
	    g.drawString(text, x, y);
	}
	
	public static BufferedImage drawCaption(BufferedImage img, String topCaption, String bottomCaption) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		// make it smoother
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		int margin = 30;

		int width = img.getWidth() - margin * 2;
		int height = img.getHeight();

		int fontSize = 40; // min font size
		Font currentFont = new Font("Impact", Font.PLAIN, fontSize);
		FontMetrics fontMatric = g.getFontMetrics(currentFont);
		// draw top caption
		//if (bottomCaption!=null)
		//	topCaption+=bottomCaption;
		if (topCaption != null) {
			// cut caption into lines if it is really long
			ArrayList<String> topLines = new ArrayList<String>();
			String stringLeft = topCaption; // string has not into lines yet
			while (fontMatric.stringWidth(stringLeft) > width) {
				int endIndex = stringLeft.length();

				while(fontMatric.stringWidth(stringLeft.substring(0, endIndex) ) >width || stringLeft.charAt(endIndex)!=' ' ){
					--endIndex;
				}
				
				topLines.add(stringLeft.substring(0, endIndex));
				stringLeft = stringLeft.substring(endIndex);
			}
			topLines.add(stringLeft);
			
			currentFont = new Font("Impact", Font.PLAIN, fontSize);
			fontMatric = g.getFontMetrics(currentFont);
			/*
			for (String a : topLines){
				System.out.println("--> "+a);
			}*/
			// draw
			g.setFont(currentFont);
			
			//DRAW TOP PART OF CAPTION
			for (int i=0;i<topLines.size()/2;++i){
				Rectangle rect = new Rectangle();
				rect.width = width;
				rect.height = (int)(fontMatric.getHeight()*1.1f);
				rect.setLocation(margin, rect.height*(i)+margin/4);
				drawCenteredString(g, topLines.get(i), rect, currentFont);
				//System.out.println(topLines.get(i));
			}
			
			//DRAW LOWER PART OF CAPTION
			int nr = ((topLines.size()%2==0)?topLines.size()/2:topLines.size()/2+1);
			//System.out.println("! = "+nr);
			for (int i=0;i<nr;++i){
				Rectangle rect = new Rectangle();
				rect.width = width;
				rect.height = (int)(fontMatric.getHeight()*1.1f);
				rect.setLocation(margin, height - margin/4 - rect.height*nr+rect.height*i);
				//System.out.println("Y = "+rect.getLocation().getY());
				drawCenteredString(g, topLines.get(i+topLines.size()/2), rect, currentFont);
				//System.out.println("BOTOM "+topLines.get(i+topLines.size()/2));
			}
			
		}
		return img;
	}
	
	/**
	 * 
	 * @param lines
	 * @param fontSize base FontSize (min fontSize)
	 * @param g
	 * @param width
	 * @return
	 */

	public static void main(String[] args) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("C:/Users/Saras/OneDrive - University of Southampton/Projektai/memesGather/MemeCamera/fruit-of-the-loom-cotton-t-131313.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		img = resizeImage(img);
		img = drawCaption(img,
				"TEST ON A WHITE BACKGROUND. HOWEVER THIS LINE IS SO LONG! WHAT IF I WILL MAKE IT REALLY REALLY LONG?",
				"THIS IS ALSO A TEST. HOWEVER THIS LINE IS ALSO LONG XDXDXDXDXDXDXDXDXXDXDDXDXDXD.");
		try {
			ImageIO.write(img, "jpg", new File("C:/Users/Saras/OneDrive - University of Southampton/Projektai/memesGather/MemeCamera/1k54f8.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}