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

public class YahooTermExtractor implements TermExtractor {


	@Override
	public List<String> extractKeywords(String plaintext) {
		// Construct data 
		List<String> results = new ArrayList<String>();
		try{
			String data = URLEncoder.encode("appid", "UTF-8") + "=" + URLEncoder.encode("pI33PCbV34GjjoWIK_uX62NzEMYXtIANQ_Ol9EYRGM067Rg6_BLeH.gOW2hMvO7p0Lwy.UbN", "UTF-8"); 
			data += "&" + URLEncoder.encode("context", "UTF-8") + "=" + URLEncoder.encode(plaintext, "UTF-8"); 
			data += "&" + URLEncoder.encode("output", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"); 
			// Send data 
			URL url = new URL("http://search.yahooapis.com/ContentAnalysisService/V1/termExtraction"); 
			URLConnection conn = url.openConnection(); 
			conn.setDoOutput(true); 
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
			wr.write(data); wr.flush(); 
			// Get the response
			JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			JSONObject jsonResponse = new JSONObject(jsonTokener);
			JSONArray jsonResults = jsonResponse.getJSONObject("ResultSet").getJSONArray("Result");			
			
			for(int i=0; i<jsonResults.length(); i++){
				results.add(jsonResults.getString(i));
			}
			wr.close(); 
		//	System.out.println(results);
		} catch (Exception e){
			e.printStackTrace();
		}
		return results;
	}

}
