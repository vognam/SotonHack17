import java.io.IOException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class TakePicture extends WebcamPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TakePicture(Webcam webcam) {
		super(webcam);
	}

	public void startCamera() throws InterruptedException, IOException {
		
		//Add the three lines below and pass in 'webcam' as constructor when creating an object of this class
		//Webcam webcam = Webcam.getDefault();
		//webcam.setViewSize(WebcamResolution.VGA.getSize());
		//WebcamPanel panel = new TakePicture(webcam);
		
		this.setFPSDisplayed(true);
		this.setDisplayDebugInfo(true);
		this.setImageSizeDisplayed(true);
		this.setMirrored(true);
		
	}
}
