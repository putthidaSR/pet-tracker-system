package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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


@Path("/locations")
public class LocationProvider {

	/**
	 * @api {GET} /pets/:id/locations?currentPage=:currentPage&pageSize=:pageSize Request locations information
	 * @apiName getLocations
	 * @apiGroup LocationProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * @apiParam {Number} currentPage current page.
	 * @apiParam {Number} pageSize page size.
	 * 
	 * @apiSuccess {Number} id Location record unique ID.
	 * @apiSuccess {String} rfidNumber Related rfidNumber unique ID.
	 * @apiSuccess {Number} longitude  Pet longitude.
	 * @apiSuccess {Number} latitude Pet latitude.
	 * @apiSuccess {Number} createTime  Location create time.
	 * @apiSuccess {String} active  Location data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
{
    "results": [
        {
            "petName": "hssel1111lohaa",
            "latitude": 47.4556,
            "longitude": -124.3455,
            "address": "Test Pacific Ave Tacoma",
            "lastSeenDate": "00-01-2011 12:00:00"
        },
        {
            "petName": "hssel1111lohaa",
            "latitude": 47.4556,
            "longitude": -122.3455,
            "address": "Pacific Ave Tacoma",
            "lastSeenDate": "00-01-2011 12:00:00"
        },
        {
            "petName": "hssel1111lohaa",
            "latitude": 47.4556,
            "longitude": -122.3455,
            "address": "Pacific Ave Tacoma",
            "lastSeenDate": "00-01-2011 12:00:00"
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
	 * @api {GET} /pets/:id Request latest location information
	 * @apiName getLatestLocation
	 * @apiGroup LocationProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 *
	 * @apiSuccess {Number} id Location record unique ID.
	 * @apiSuccess {String} rfidNumber Related rfidNumber unique ID.
	 * @apiSuccess {Number} longitude  Pet longitude.
	 * @apiSuccess {Number} latitude Pet latitude.
	 * @apiSuccess {Number} createTime  Location create time.
	 * @apiSuccess {String} active  Location data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
{
    "petName": "hssel1111lohaa",
    "latitude": 47.4556,
    "longitude": -124.3455,
    "address": "Test Pacific Ave Tacoma",
    "lastSeenDate": "00-01-2011 12:00:00"
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
	 * Returns list of locations of all pets that belong to the specified user.
	 * 
	 * {
    "results": [
        {
            "petId": 2,
            "petName": "hssel1111lohaa",
            "petRFID": "1AV1112",
            "latitude": 47.4556,
            "longitude": -124.3455,
            "address": "Test Pacific Ave Tacoma",
            "lastSeenDate": "00-01-2011 12:00:00"
        },
        {
            "petId": 3,
            "petName": "test2",
            "petRFID": "2234455555",
            "latitude": 47.4556,
            "longitude": -122.3455,
            "address": "Pacific Ave Tacoma",
            "lastSeenDate": "00-01-2011 12:00:00"
        }
    ]
}
	 * @param id
	 * @return
	 */
	@Path("/users/{user_id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestLocationAllPets(@PathParam("user_id") int id) {
		
		try {
			
			// Establish connection to MySQL server
			Connection connection = HandleConnection.getConnection();
			
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

				arr.add(eachElement);
			}

			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
			}

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
