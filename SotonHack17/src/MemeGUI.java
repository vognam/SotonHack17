import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

/**
 * GUI. This is the entrance of the app.
 * 
 * @author Allen
 *
 */
public class MemeGUI {
	
	public MemeGUI() {
		new MemeFrame();
	}

	public static void main(String[] args) {
		new MemeGUI();
	}

}

class MemeFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private Container container;
	private WebcamPanel camPanel;
	private String upperText;
	private String lowerText;
	private int windowWidth = 1750;
	private int windowHeight = 1000;
	
	public MemeFrame() {
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		init();
		
		this.setSize(windowWidth, windowHeight);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		this.setLocation((int)(width - windowWidth) /2, (int)(height - windowHeight) / 2);
		this.setVisible(true);
	}
	
	private void init() {
		// root container
		container = this.getContentPane();
		container.setLayout(new BorderLayout());
		
		// displayPanel
		//JPanel displayPanel = new JPanel(new GridLayout(1, 2));
		
		// webcam
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(WebcamResolution.VGA.getSize());
		
		camPanel = new TakePicture(webcam);
		
		ImagePanel memePanel = new ImagePanel(null);
		
		//displayPanel.add(camPanel);
		//displayPanel.add(memePanel);
		
		JSplitPane displayPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, camPanel, memePanel);
		//displayPanel.setOneTouchExpandable(true);
		displayPanel.setResizeWeight(0.5);
		displayPanel.setDividerLocation(windowWidth / 2 + displayPanel.getInsets().left);
		
		
		Icon buttonIcon = new ImageIcon("pepe.jpg");
		
		JButton picButton = new JButton(buttonIcon);
		picButton.setPreferredSize( new Dimension((int) picButton.getPreferredSize().getWidth(), 175));
		picButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		picButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
		
		picButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				picButton.setEnabled(false);
				
				img = webcam.getImage();
				//passes the image to the meme processor
				MemeMain memeMain = new MemeMain();
				memeMain.processAPI((ImageTools.getBinary(img)));
				upperText = memeMain.getUpperText();
				lowerText = memeMain.getLowerText();
				img = ImageTools.drawCaption(img, upperText, lowerText);
				
				memePanel.setImage(img);
				memePanel.repaint();
				
				picButton.setEnabled(true);
			}
			
		});
		
		container.add(picButton, BorderLayout.SOUTH);
		container.add(displayPanel, BorderLayout.CENTER);
	}
	
}

class ImagePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	
	public ImagePanel(BufferedImage img) {
		super();
		this.img = img;
		this.setBackground(Color.black);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img == null) {
			return;
		}
		
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
	
	public void setImage(BufferedImage img) {
		this.img = img;
	}
	
}
