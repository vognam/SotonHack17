import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CaptionChooser {

		private MyImage myImage;
		private BufferedReader in;
		private float difference;
		String[] caption = {"", ""};
		private float[] myEmotions;
		
		/*
		 * Takes all emotion float values as parameter. Returns String[] that contains two
		 * elements: top caption and bottom caption text.
		 */
		String[] ChooseCaption(float[] newEmotions){
			myEmotions = newEmotions;
			String line;
			try {
				in = new BufferedReader(new FileReader("captions.txt"));
				try {
					while((line = in.readLine()) != null)
					{
					    AnalyseData(line);
					}
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return caption;
		}
		
		void AnalyseData(String line){
			float[] emotions = new float[20];
			int emotionIndex = 0;
			int captionIndex = 0;
			int i = 0;
			boolean nextIsCaption = false;
			while(i < line.length()){
				String newEmotionValue = "";
				String captionPart = "";
				if (line.substring(i, i+1) == "+"){
					nextIsCaption = true;
				}
				while(line.substring(i, i+1) != " " && !nextIsCaption){
					newEmotionValue += line.substring(i, i+1);
					i++;
				}
				while(line.substring(i, i+1) != " " && nextIsCaption){
					captionPart += line.substring(i, i+1);
					i++;
				}
				if(nextIsCaption){
					caption[captionIndex] = captionPart;
					captionIndex++;
				}
				emotions[emotionIndex] = Float.parseFloat(newEmotionValue);
				emotionIndex++;
				i++;
			}
			for(int j = 0; emotions[j] != 0; j++){
				System.out.println(emotions[j]);
			}
		}
		
		
}
