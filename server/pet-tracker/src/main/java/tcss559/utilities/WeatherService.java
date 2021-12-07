package tcss559.utilities;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {

	public static String getCurrentWeather(double latitude, double longitude) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String url = String.format("https://weatherapi-com.p.rapidapi.com/current.json?q=%s", latitude+","+longitude);
		System.out.println("Invoke Weather API Invoke");
		Request request = new Request.Builder()
			.url("https://weatherapi-com.p.rapidapi.com/current.json?q=48.8567%2C2.3508")
			.get()
			.addHeader("x-rapidapi-host", "weatherapi-com.p.rapidapi.com")
			.addHeader("x-rapidapi-key", "7c01abf595mshf3564f9febecab4p1f8505jsna52eb41b331e")
			.build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
}
