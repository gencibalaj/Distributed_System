import java.io.IOException;



import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;




public class MakeRequest {
	
	private static String postUrl = "http://3.16.21.183:8080";

	private MakeRequest() {
		
	}
	
	public static String getServerUrl() {
		return postUrl;
	}
	
	public static response postRequest(String function, Object data) throws IOException, InterruptedException {
		
		Gson g = new Gson();
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(postUrl+function);
		StringEntity entity = new StringEntity(g.toJson(data));
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    CloseableHttpResponse response = client.execute(httpPost);
	    if(response.getStatusLine().getStatusCode() == 200) {
	    	
	    	String content = EntityUtils.toString(response.getEntity());
	    	System.out.println(content);
	    	return g.fromJson(content, response.class);
	    }else {
	    	return null;
	    }
	}
	
public static String postRequest2(String function, Object data) throws IOException, InterruptedException {
		
		Gson g = new Gson();
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpPost httpPost = new HttpPost(postUrl+function);
		StringEntity entity = new StringEntity(g.toJson(data));
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
	    httpPost.setHeader("Content-type", "application/json");
	    CloseableHttpResponse response = client.execute(httpPost);
	    if(response.getStatusLine().getStatusCode() == 200) {
	    	
	    	String content = EntityUtils.toString(response.getEntity());
	    	return content;
	    }else {
	    	return null;
	    }
	}
	
}