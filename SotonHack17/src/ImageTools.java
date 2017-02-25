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
import java.util.ArrayList;
import java.util.Arrays;

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
	public static BufferedImage drawCaption(BufferedImage img, String topCaption, String bottomCaption) {
		Graphics2D g = (Graphics2D) img.getGraphics();
		// make it smoother
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		int margin = 10;

		int width = img.getWidth() - margin * 2;
		int height = img.getHeight();

		int fontSize = 100; // default font size
		int offset = 2;
		// draw top caption
		if (topCaption != null) {
			Font currentFont = new Font("Impact", Font.PLAIN, fontSize);
			FontMetrics fontMatric = g.getFontMetrics(currentFont);
			while (fontMatric.stringWidth(topCaption) > width && fontSize > 30) {
				fontSize--;
				currentFont = new Font("Impact", Font.PLAIN, fontSize);
				fontMatric = g.getFontMetrics(currentFont);
			}

			// cut caption into lines if it is really long
			ArrayList<String> topLines = new ArrayList<String>();
			String stringLeft = topCaption; // string has not into lines yet
			while (fontMatric.stringWidth(stringLeft) > width) {
				int endIndex = stringLeft.length();
				// This part seems working magically
				while (fontMatric.stringWidth(stringLeft.substring(0, endIndex)) > width
						|| !stringLeft.substring(endIndex - 1, endIndex).equals(" ")
								&& !stringLeft.substring(endIndex, endIndex + 1).equals(" ")) {
					endIndex--;
				}
				topLines.add(stringLeft.substring(0, endIndex));
				stringLeft = stringLeft.substring(endIndex);
			}
			topLines.add(stringLeft);

			// set lineWidth to the width of the line with the max width
			int lineWidth = 0;
			for (int i = 0; i < topLines.size(); i++) {
				int stringWidth = fontMatric.stringWidth(topLines.get(i));
				if (stringWidth > lineWidth) {
					lineWidth = stringWidth;
				}
			}

			// draw
			g.setFont(currentFont);
			int topX = (width - lineWidth) / 2 + margin;
			for (int i = 0; i < topLines.size(); i++) {
				String line = topLines.get(i);
				int topY = fontMatric.getHeight() * (i + 1) - fontMatric.getDescent();

				// draw offsets
				g.setColor(Color.BLACK);
				g.drawString(line, topX - offset, topY - offset);
				g.drawString(line, topX - offset, topY + offset);
				g.drawString(line, topX + offset, topY - offset);
				g.drawString(line, topX + offset, topY + offset);

				// draw text
				g.setColor(Color.WHITE);
				g.drawString(line, topX, topY);
			}
		}

		// draw bottom caption
		if (bottomCaption != null && false) {
			fontSize = 100;
			Font currentFont = new Font("Impact", Font.PLAIN, fontSize);
			FontMetrics fontMatric = g.getFontMetrics(currentFont);
			while (fontMatric.stringWidth(bottomCaption) > width && fontSize > 30) {
				fontSize--;
				currentFont = new Font("Impact", Font.PLAIN, fontSize);
				fontMatric = g.getFontMetrics(currentFont);
			}
			
			// cut caption into lines if it is really long
			ArrayList<String> bottomLines = new ArrayList<String>();
			String stringLeft = bottomCaption; // string has not into lines yet
			while (fontMatric.stringWidth(stringLeft) > width) {
				int endIndex = stringLeft.length();
				// This part seems working magically
				while (fontMatric.stringWidth(stringLeft.substring(0, endIndex)) > width
						|| !stringLeft.substring(endIndex - 1, endIndex).equals(" ")
								&& !stringLeft.substring(endIndex, endIndex + 1).equals(" ")) {
					endIndex--;
				}
				bottomLines.add(stringLeft.substring(0, endIndex));
				stringLeft = stringLeft.substring(endIndex);
			}
			bottomLines.add(stringLeft);

			// set lineWidth to the width of the line with the max width
			int lineWidth = 0;
			for (int i = 0; i < bottomLines.size(); i++) {
				int stringWidth = fontMatric.stringWidth(bottomLines.get(i));
				if (stringWidth > lineWidth) {
					lineWidth = stringWidth;
				}
			}

			// draw
			g.setFont(currentFont);
			int topX = (width - lineWidth) / 2 + margin;
			for (int i = 0; i < bottomLines.size(); i++) {
				String line = bottomLines.get(i);
				int topY = height - fontMatric.getHeight() * i - fontMatric.getDescent();

				// draw offsets
				g.setColor(Color.BLACK);
				g.drawString(line, topX - offset, topY - offset);
				g.drawString(line, topX - offset, topY + offset);
				g.drawString(line, topX + offset, topY - offset);
				g.drawString(line, topX + offset, topY + offset);

				// draw text
				g.setColor(Color.WHITE);
				g.drawString(line, topX, topY);
			}
		}

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
		img = drawCaption(img,
				"TEST ON A WHITE BACKGROUND. HOWEVER THIS LINE IS SO LONG! WHAT IF I WILL MAKE IT REALLY REALLY LONG?",
				"THIS IS ALSO A TEST. HOWEVER THIS LINE IS ALSO LONG XDXDXDXDXDXDXDXDXXDXDDXDXDXD.");
		try {
			ImageIO.write(img, "jpg", new File("/Users/Allen/Documents/SotonHack17 Test/output.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
