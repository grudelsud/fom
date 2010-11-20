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

public class AlchemyTermExtractor implements TermExtractor {
	
	@Override
	public List<String> extractKeywords(String plaintext) {
		// Construct data 
		List<String> results = new ArrayList<String>();
		try{
			String data = URLEncoder.encode("apikey", "UTF-8") + "=" + URLEncoder.encode("cf0345bb014892433eed8f8d88958b0e1cd16a07", "UTF-8"); 
			data += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(plaintext, "UTF-8"); 
			data += "&" + URLEncoder.encode("outputMode", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"); 
			// Send data 
			URL url = new URL("http://access.alchemyapi.com/calls/text/TextGetKeywords"); 
			URLConnection conn = url.openConnection(); 
			conn.setDoOutput(true); 
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
			wr.write(data); wr.flush(); 
			// Get the response
			JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			JSONObject jsonResponse = new JSONObject(jsonTokener);
			JSONArray jsonResults = jsonResponse.getJSONArray("keywords");			
			for(int i=0; i<jsonResults.length(); i++){
				results.add(jsonResults.getJSONObject(i).getString("keyword"));
			}
			wr.close(); 
		//	System.out.println(results);
		} catch (Exception e){
			e.printStackTrace();
		}
		return results;
	}

}
