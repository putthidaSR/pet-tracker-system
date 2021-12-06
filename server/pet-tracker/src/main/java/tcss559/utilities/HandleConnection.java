package tcss559.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class contains utility methods to establish database connection and invoke other resources.
 * @author Putthida Samrith
 *
 */
public class HandleConnection {

	// Necessary variables to connect to Google Cloud MySQL
    public static String mysql_ip = "34.135.77.128";
	public static String username = "thida_sr";
	public static String password = "Liverpool9!";
	//public static String connectStr ="jdbc:mysql://" + mysql_ip + ":3306/parking?user=" + username + "&password=" + password ;
	public static String connectStr ="jdbc:mysql://" + "localhost" + ":3306/paw_tracker?user=root&password=" + password ;

	/**
	 * Establish the connection to Google Cloud MySQL.
	 * @throws Exception if fail to establish connection
	 */
	public static Connection getConnection() throws Exception {
				
    	try {
    		
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		Connection connection = DriverManager.getConnection(connectStr);
			return connection;
			
		} catch (Exception e) {
			throw new Exception("Failed to establish connection");
		}
	}
	
	/**
	 * Invoke the provided URL and return response in string format.
	 * This method is inspired by source code provided in the handout in Assignment 3.
	 * @param serviceURL URL to invoke
	 */
	public static String getResourceFromURL(String serviceURL) throws Exception {

		// construct the service URL
		URL serviceEndpoint = new URL(serviceURL);
		
		// initialize the HTTP Request with the service endpoint URL
		HttpURLConnection httpRequestCon = (HttpURLConnection) serviceEndpoint.openConnection();
		httpRequestCon.setRequestMethod("GET");
		httpRequestCon.setRequestProperty("Accept", "*/*");
		
		// check to see whether we get HTTP OK (status code 200) or not
		if (httpRequestCon.getResponseCode() != 200) {
			throw new RuntimeException("HTTP Error code is: " + httpRequestCon.getResponseCode());
		}
		
		// Read the stream response and store into a buffered reader
		BufferedReader httpResponse = new BufferedReader(new InputStreamReader((httpRequestCon.getInputStream())));
		
		// Retrieve output from the response object (HTTP Response)
		String responseOutput;
		StringBuffer responseMessage = new StringBuffer();
		while ((responseOutput = httpResponse.readLine()) != null) {
			responseMessage.append(responseOutput);
		}
		
		// Close connection
		httpResponse.close();
		httpRequestCon.disconnect();
		
		// Convert the response into JSON format
		JsonObject obj = new JsonParser().parse(responseMessage.toString()).getAsJsonObject();
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure for pretty printing
		String output = gson.toJson(obj);
		
		//System.out.println(output);
		
		return output;
		
	}
	
}
