import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class CaptionChooser {

		private BufferedReader in;
		private float difference = Float.POSITIVE_INFINITY;
		String[] caption;
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
					    ParseText(line);
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
		
		void ParseText(String line){
			String[] localCaption = {"", ""};
			float[] emotions = new float[20];
			int emotionIndex = 0;
			boolean nextIsCaption = false;
			for(int i = 0; i < line.length(); i++){
				String newEmotionValue = "";
				//String captionPart = "";
				
				if (line.substring(i, i+1).equals(",")){
					nextIsCaption = true;
					i++;
					for( ; !line.substring(i+2, i+3).equals("+"); i++){
						localCaption[0] += line.substring(i+1, i+2);
					}
					i+=2;
					for( ; i + 3 < line.length(); i++){
						localCaption[1] += line.substring(i+3, i+4);
					}
				}
				
				while(!line.substring(i, i+1).equals(" ") && nextIsCaption == false){
					newEmotionValue += line.substring(i, i+1);
					i++;
				}
				
				if(nextIsCaption == false){
					emotions[emotionIndex] = Float.parseFloat(newEmotionValue);
					emotionIndex++;
				}
				
				//System.out.println("ok" + i + line.substring(i, i+1));
				//System.out.println(i  + " / " + line.length() + " nex:" + nextIsCaption);
					
			}
			
			AnalyseData(emotions, localCaption);
			
		}
		
		void AnalyseData(float[] localEmotions, String[] localCaption){
			float localDifference = 0.0f;
			for(int i = 0; i < myEmotions.length; i++){
				//System.out.println(myEmotions[i]);
				localDifference += Math.abs(myEmotions[i] - localEmotions[i]);
			}
			if(localDifference < difference){
				caption = localCaption.clone();
				difference = localDifference;
			} else if (localDifference == difference && (new Random()).nextBoolean()){
				caption = localCaption.clone();
			}
		}
		
}
