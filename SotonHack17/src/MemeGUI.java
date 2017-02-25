import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

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
	private Container container;
	private WebcamPanel camPanel;
	
	public MemeFrame() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		init();
		
		this.setSize(500, 600);
		this.setVisible(true);
	}
	
	private void init() {
		container = this.getContentPane();
		container.setLayout(new BorderLayout());
				
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		camPanel = new TakePicture(webcam);
				
		JButton picButton = new JButton("Take Picture!");
				
		picButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				picButton.setEnabled(false);
				img = webcam.getImage();
				webcam.close();
				img = ImageTools.drawCaption(img, "Test???", "?");
				showMeme();
			}
		});
		
		container.add(picButton, BorderLayout.SOUTH);
		container.add(camPanel, BorderLayout.CENTER);
	}
	
	private void showMeme() {
		container.remove(camPanel);
		container.revalidate();
		ImagePanel imgPanel = new ImagePanel(img);
		container.add(imgPanel, BorderLayout.CENTER);
		container.repaint();
	}
	
}

class ImagePanel extends JPanel {
	
	private BufferedImage img;
	
	public ImagePanel(BufferedImage img) {
		super();
		this.img = img;
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
		System.out.println("hi");
	}
	
}
