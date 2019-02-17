package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;


public class TicketMasterAPI {
	private static final String URL =  "https://app.ticketmaster.com/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = "";
	private static final String API_KEY = "OOdL1p1KCG0qqe7tM6UEoRNMYlASQJTC";
	
	public JSONArray search(double lat, double lon, String keyword) {
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// final url like : apikey=OOdL1p1KCG0qqe7tM6UEoRNMYlASQJTC&latlong=37,-120&keyword=event&raduis=50
		String query = String.format("apikey=%s&latlong=%s,%s&keyword=%s&radius=%s", API_KEY, lat,lon,keyword,50);
		String url = URL + "?" + query;
		try {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		int responseCode = connection.getResponseCode();
		System.out.println("Sending request to url: " + url);
		System.out.println("Response code: " + responseCode);
		
		if (responseCode != 200) {
			return new JSONArray();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		StringBuilder response = new StringBuilder();
		while ( (line = reader.readLine()) != null) {
			response.append(line);
		}
		reader.close();
		JSONObject obj = new JSONObject(response.toString());
		
		if (!obj.isNull("_embedded")) {
			JSONObject embedded = obj.getJSONObject("_embedded");
			return embedded.getJSONArray("events");
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	
	private void queryAPI(double lat, double lon) {
		JSONArray events = search(lat, lon, null);

		try {
			for (int i = 0; i < events.length(); ++i) {
				JSONObject event = events.getJSONObject(i);
				System.out.println(event.toString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		TicketMasterAPI tmApi = new TicketMasterAPI();
		// Mountain View, CA
		// tmApi.queryAPI(37.38, -122.08);
		// London, UK
		// tmApi.queryAPI(51.503364, -0.12);
		// Houston, TX
		tmApi.queryAPI(29.682684, -95.295410);
	}

	
	
}
