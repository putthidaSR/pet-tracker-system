package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import tcss559.utilities.HandleConnection;
import tcss559.utilities.WeatherService;

/**
 * Root resource (exposed at "locations" path).
 * This resource class contains functionalities related to location lookup.
 */
@Path("/locations")
public class LocationProvider {

	/**
	 * @api {GET} /locations/{id} Request locations information of specified pet
	 * @apiName getLocations
	 * @apiGroup LocationProvider
	 * 
	 * @apiHeader {Number} limit Maximum number of locations to return
	 * 
	 * @apiParam {Number} id Pets unique ID.
	 * 
	 * 
	 * @apiSuccess {String} petName Name of the pet.
	 * @apiSuccess {Number} longitude  Longitude of the pet's location.
	 * @apiSuccess {Number} latitude Latitude of the pet's location.
	 * @apiSuccess {String} address  Address of the pet's location
	 * @apiSuccess {String} lastSeenDate  Date/time that the pet was seen in the specified location (format: YYYY-MM-DD hh:mm:ss)
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
			{
			    "results": [
			        {
				    	"petName": "Violet",
					    "latitude": 47.4567856,
					    "longitude": -124.344555,
					    "address": "1132, Pacific Ave, Tacoma, WA, 98404",
					    "lastSeenDate": "2021-12-07 15:07:05"
			        },
			        {
				    	"petName": "Bella",
					    "latitude": 47.454356,
					    "longitude": -124.345455,
					    "address": "4532, Pacific Ave, Tacoma, WA, 98404",
					    "lastSeenDate": "2021-12-08 15:07:05"
			        }
			    ]
			}
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocations(@PathParam("id") int id, @DefaultValue("10") @HeaderParam("limit") int limit) {
		
		try {
			
			System.out.println("Attempt to get the list of locations for petId: " + id);
			
			// Establish connection to MySQL server
			Connection connection = HandleConnection.getConnection();

			// Returns the list of location records of the specified pet ID
			PreparedStatement stmt = connection.prepareStatement(""
					+ "SELECT pl.latitude, pl.longitude, pl.address, pl.last_seen, pd.name as pet_name "
					+ "FROM pet_location pl, pet_detail pd "
					+ "WHERE pd.pet_detail_id = pl.pet_id AND pd.active = 1 AND pl.pet_id = ? "
					+ "ORDER BY last_seen DESC LIMIT ?");
			stmt.setInt(1, id);
			stmt.setInt(2, limit);

			// Execute SQL query
			ResultSet rs = stmt.executeQuery();
			
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			JsonArray arr = new JsonArray();
			
			// Map result set returns from query
			boolean hasRecord = false;
			while (rs.next()) {
				hasRecord = true;

				// Each element in the array (pet with details)
				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("petName", rs.getString("pet_name"));
				eachElement.addProperty("latitude", rs.getDouble("latitude"));
				eachElement.addProperty("longitude", rs.getDouble("longitude"));
				eachElement.addProperty("address", rs.getString("address"));
                eachElement.addProperty("lastSeenDate", rs.getString("last_seen"));

				arr.add(eachElement);
			}

			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
			}
			
			// Format to return: Array of objects
			JsonObject obj = new JsonObject();
			obj.add("results", arr);
			jsonResponse = gson.toJson(obj);

			connection.close();
						
			// Return successful response if no error
			return Response.status(Response.Status.OK).entity(jsonResponse).build();

		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Error Message: " + e.getLocalizedMessage())
					.build();
		}
	}
	
	
	/**
	 * @api {GET} /locations/{id}/latest Request latest location information of specified pet
	 * @apiName getLatestLocation
	 * @apiGroup LocationProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 *
	 * @apiSuccess {String} petName Name of the pet.
	 * @apiSuccess {Number} longitude  Longitude of the pet's location.
	 * @apiSuccess {Number} latitude Latitude of the pet's location.
	 * @apiSuccess {String} address  Address of the pet's location
	 * @apiSuccess {String} lastSeenDate  Date/time that the pet was seen in the specified location (format: YYYY-MM-DD hh:mm:ss)
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
			{
			    "petName": "Bella",
			    "latitude": 47.4556,
			    "longitude": -124.3455,
			    "address": "4432, Pacific Ave, Tacoma, WA, 98404",
			    "lastSeenDate": "2021-12-07 15:07:05"
			}
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@Path("/{id}/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestLocation(@PathParam("id") int id) {
		
		System.out.println("Attempt to get the latest location for petId: " + id);
		
		try {

			// Establish connection to MySQL server
			Connection connection = HandleConnection.getConnection();
			
			// Return the latest location with details of the specified pet ID
			PreparedStatement stmt = connection.prepareStatement(""
					+ "SELECT pl.latitude, pl.longitude, pl.address, pl.last_seen, pd.name as pet_name "
					+ "FROM pet_location pl, pet_detail pd "
					+ "WHERE pd.pet_detail_id = pl.pet_id AND pd.active = 1 AND pl.pet_id = ? "
					+ "ORDER BY last_seen DESC LIMIT 1");
			stmt.setInt(1, id);

			// Execute SQL query
			ResultSet rs = stmt.executeQuery();
			
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			JsonObject obj = new JsonObject();

			// Map result set returns from query
			boolean hasRecord = false;
			while (rs.next()) {
				hasRecord = true;

				// Each element in the array (pet with details)
				obj.addProperty("petName", rs.getString("pet_name"));
				obj.addProperty("latitude", rs.getDouble("latitude"));
				obj.addProperty("longitude", rs.getDouble("longitude"));
				obj.addProperty("address", rs.getString("address")); 
                obj.addProperty("lastSeenDate", rs.getString("last_seen"));
			}

			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
			}
			
			jsonResponse = gson.toJson(obj);

			connection.close();
						
			// Return successful response if no error
			return Response.status(Response.Status.OK).entity(jsonResponse).build();

		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Error Message: " + e.getLocalizedMessage())
					.build();
		}
	}

	/**
	 * @api {GET} /locations/users/{user_id} Request latest locations of all pets belong to specified user
	 * @apiName getLatestLocationAllPets
	 * @apiGroup LocationProvider
	 *
	 * @apiParam {Number} user_id Unique user ID.
	 *
	 * @apiSuccess {Number} petId Unique ID of each pet.
	 * @apiSuccess {String} petName Name of the pet.
	 * @apiSuccess {String} petRFID Pet's RFID tag number.
	 * @apiSuccess {Number} longitude  Longitude of the pet's location.
	 * @apiSuccess {Number} latitude Latitude of the pet's location.
	 * @apiSuccess {String} address  Address of the pet's location
	 * @apiSuccess {String} lastSeenDate  Date/time that the pet was seen in the specified location (format: YYYY-MM-DD hh:mm:ss)
	 * @apiSuccess {String} weather Weather condition of the pet's location
	 * @apiSuccess {String} iconLink HTTP link to the icon that represents the weather condition
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     
	 *     {
			"results": [
			        {
			            "petId": 2,
			            "petName": "hssel1111lohaa",
			            "petRFID": "1AV1112",
			            "latitude": 47.4586,
			            "longitude": -124.3455,
			            "address": "New Test Pacific Ave Tacoma",
			            "lastSeenDate": "2021-12-07 15:07:02",
			            "weather": "Mist",
			            "iconLink": "http://cdn.weatherapi.com/weather/64x64/day/143.png"
			        },
			        {
			            "petId": 3,
			            "petName": "test2",
			            "petRFID": "2234455555",
			            "latitude": 47.4556,
			            "longitude": -122.3455,
			            "address": "Pacific Ave Tacoma",
			            "lastSeenDate": "2011-01-01 00:00:00",
			            "weather": "Mist",
			            "iconLink": "http://cdn.weatherapi.com/weather/64x64/day/143.png"
			        }
			    ]
			}
			
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the user was not found.
	 */
	@Path("/users/{user_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestLocationAllPets(@PathParam("user_id") int id) {
		
		try {
			
			// Establish connection to MySQL server
			Connection connection = HandleConnection.getConnection();
			
			// Returns the latest location with details of each pet that belongs to the specified user
			String sql = "SELECT DISTINCT "
					+ "p.id AS pet_id, pd.name AS pet_name, p.rfid_number, "
					+ "pl.latitude, pl.longitude, pl.address, pl.last_seen "
					+ "FROM user u "
					+ "LEFT JOIN pet p ON u.id = p.user_id "
					+ "LEFT JOIN pet_detail pd ON p.id = pd.pet_detail_id "
					+ "LEFT JOIN pet_location pl ON p.id = pl.pet_id "
					+ "WHERE u.id = ? AND pd.active = 1 AND pl.id "
					+ "IN (SELECT MAX(id) FROM pet_location GROUP BY pet_id)";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setInt(1, id);

			// Execute SQL query
			ResultSet rs = stmt.executeQuery();
			
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			JsonArray arr = new JsonArray();

			// Map result set returns from query
			boolean hasRecord = false;
			while (rs.next()) {
				hasRecord = true;

				// Each element in the array (pet with details)
				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("petId", rs.getInt("pet_id"));
				eachElement.addProperty("petName", rs.getString("pet_name"));
				eachElement.addProperty("petRFID", rs.getString("rfid_number"));
				eachElement.addProperty("latitude", rs.getDouble("latitude"));
				eachElement.addProperty("longitude", rs.getDouble("longitude"));
				eachElement.addProperty("address", rs.getString("address"));  
				eachElement.addProperty("lastSeenDate", rs.getString("last_seen"));
				
				// Invoke weather web service to get weather info of that particular location
				String data = WeatherService.getCurrentWeather(rs.getDouble("latitude"), rs.getDouble("longitude"));
				
				Gson g = new Gson();
				JsonObject obj = g.fromJson(data, JsonObject.class);
				JsonObject current = g.fromJson(obj.get("current"), JsonObject.class);
				JsonObject condition = g.fromJson(current.get("condition"), JsonObject.class);
				String weather = condition.get("text").getAsString();
				String iconLink = condition.get("icon").getAsString();
				
				// Add weather info to response object
				eachElement.addProperty("weather", weather);
				eachElement.addProperty("iconLink", iconLink);

				arr.add(eachElement);
			}

			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
			}

			// Construct array of object to return as response
			JsonObject obj = new JsonObject();
			obj.add("results", arr);
			jsonResponse = gson.toJson(obj);

			connection.close();
			
			// Return successful response if no error
			return Response.status(Response.Status.OK).entity(jsonResponse).build();
		
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Error Message: " + e.getLocalizedMessage())
					.build();
		}
	}

}
