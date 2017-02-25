// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.*;

// most of this you don't need to know how it works
// not even i know how it works; just read the parts i've commented
public class MemeMain {
	
	public String[] caption = new String[2];
	
    public void processAPI(ByteArrayEntity pic) {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://westus.api.cognitive.microsoft.com/emotion/v1.0/recognize");


            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            //subscription key gets put in here. get it from the api emotion website
            request.setHeader("Ocp-Apim-Subscription-Key", "943d220c06d844848f0a1987962cc610");


            // Request body
            //either enter a url in the format "{'url':'httpblablabal'}"
            //OR binary image data (i think)
            //StringEntity reqEntity = new StringEntity("{'url' : 'http://orig15.deviantart.net/5470/f/2010/105/4/5/random_person_by_vurtov.jpg'}");
            request.setEntity(pic);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) 
            {
                //the json parsing is sent over to a separate method
            	processEmotion(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    //parses the JSON data and creates an array of floats for each emotion
    //it's organized in alphabetical order so emotions[0] will contain the float for anger
    public void processEmotion(HttpEntity entity) throws Exception{
    	//System.out.println(EntityUtils.toString(entity));
    	float[] emotions = new float[8];
    	 
    	try{
    		String temp = EntityUtils.toString(entity);
        	JSONArray jsonArray = new JSONArray(temp);
        	JSONObject scores;
    		scores = jsonArray.getJSONObject(0).getJSONObject("scores");
    		emotions[0] = Float.parseFloat(scores.get("anger").toString());
        	emotions[1] = Float.parseFloat(scores.get("contempt").toString());
        	emotions[2] = Float.parseFloat(scores.get("disgust").toString());
        	emotions[3] = Float.parseFloat(scores.get("fear").toString());
        	emotions[4] = Float.parseFloat(scores.get("happiness").toString());
        	emotions[5] = Float.parseFloat(scores.get("neutral").toString());
        	emotions[6] = Float.parseFloat(scores.get("sadness").toString());
        	emotions[7] = Float.parseFloat(scores.get("surprise").toString());

        	CaptionChooser captionChooser = new CaptionChooser();
        	caption = captionChooser.ChooseCaption(emotions);
        	
    	}catch(Exception e){
    		caption[0] = "No Face Detected";
    		caption[1] = "";
    		return;
    	}
    	
    	System.out.println(caption[0]);
    	System.out.println(caption[1]);
    }
    
    public String getUpperText(){
    	return caption[0];
    }
    
    public String getLowerText(){
    	return caption[1];
    }
}