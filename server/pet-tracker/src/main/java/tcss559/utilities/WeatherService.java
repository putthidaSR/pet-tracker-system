package tcss559.utilities;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {

	/**
	 * Invoke WeatherAPI.com to return the real time weather information.
	 * 
	 * @param latitude latitude of the location to retrieve weather condition
	 * @param longitude longitude of the location to retrieve weather condition
	 */
	public static String getCurrentWeather(double latitude, double longitude) throws IOException {
		
		OkHttpClient client = new OkHttpClient();
		String requestUrl = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", latitude + "%2C" + longitude);
		
		System.out.println("Invoke Weather API: " + requestUrl);
		
		Request request = new Request.Builder()
			.url(requestUrl)
			.get()
			.addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
			.addHeader("x-rapidapi-key", "7c01abf595mshf3564f9febecab4p1f8505jsna52eb41b331e")
			.build();
		
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
}
