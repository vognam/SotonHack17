import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MemeGUI {
	
	public MemeGUI() {
		new MemeFrame();
	}

	public static void main(String[] args) {
		new MemeGUI();
	}

}

class MemeFrame extends JFrame {
	
	private BufferedImage img;
	
	public MemeFrame() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		
		ImagePanel canvas = new ImagePanel();
				
		JButton chooseButton = new JButton("Choose Image");
		
		container.add(chooseButton, BorderLayout.SOUTH);
		container.add(canvas, BorderLayout.CENTER);
		
		this.setSize(500, 600);
		this.setVisible(true);
	}
	
}

class ImagePanel extends JPanel {
	
	BufferedImage img;
	
	public ImagePanel() {
		super();
		
		try {
			img = ImageIO.read(new File("/Users/Allen/Documents/SotonHack17 Test/1.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("con");
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int panelWidth = this.getWidth();
		int panelHeight = this.getHeight();
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		// scale image
		int scaledWidth;
		int scaledHeight;
		
		if ((double)panelWidth / (double)panelHeight < (double)width / (double)height) { // limited by panel width
			scaledWidth = panelWidth;
			scaledHeight = height * panelWidth / width;
		} else { // limited by panel height
			scaledWidth = width * panelHeight / height;
			scaledHeight = panelHeight;
		}
		
		Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH);
		
		int x = (panelWidth - scaledWidth) / 2;
		int y = (panelHeight - scaledHeight) / 2;
		
		g.drawImage(scaledImg, x, y, null);
	}
	
}
