package fom.termextraction;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


public class ZemantaTermExtractor implements TermExtractor {
	
	@Override
	public List<String> extractKeywords(String plaintext) {
		// Construct data 
		List<String> results = new ArrayList<String>();
		try{
			String data = URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode("zemanta.suggest", "UTF-8"); 
			data += "&" + URLEncoder.encode("api_key", "UTF-8") + "=" + URLEncoder.encode("826meqss3sj93mqns7zb9v76", "UTF-8"); 
			data += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(plaintext, "UTF-8"); 
			data += "&" + URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8");
			data += "&" + URLEncoder.encode("return_images", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
			data += "&" + URLEncoder.encode("markup_limit", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
			data += "&" + URLEncoder.encode("articles_limit", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");

			// Send data 
			URL url = new URL("http://api.zemanta.com/services/rest/0.0/"); 
			URLConnection conn = url.openConnection(); 
			conn.setDoOutput(true); 
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
			wr.write(data); wr.flush(); 
			// Get the response
			JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			JSONObject jsonResponse = new JSONObject(jsonTokener);
			JSONArray jsonResults = jsonResponse.getJSONArray("keywords");			
			
			for(int i=0; i<jsonResults.length(); i++){
				results.add(jsonResults.getJSONObject(i).getString("name"));
			}
			wr.close();
		//	System.out.println(results);
		} catch (Exception e){
			e.printStackTrace();
		}
		return results;
	}
	
}
