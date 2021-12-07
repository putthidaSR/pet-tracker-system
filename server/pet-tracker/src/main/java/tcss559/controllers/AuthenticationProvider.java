package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import tcss559.model.User;
import tcss559.utilities.*;

@Path("/app")
public class AuthenticationProvider {

	/**
	 * Login user to the mobile app.
	 * 
	 * @param user
	 * @return
	 */
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AccountLogin(@HeaderParam("user_role") String role, User user) {
		
		try {

			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record based on user role
        	String sql = "";
        	boolean isLoggedInAsVeterinarian = false;
        	
        	if (role.equalsIgnoreCase(User.ROLE_VETERINARIAN)) {
        		isLoggedInAsVeterinarian = true;
        		sql = "select user.id, user.login_name, ad.badge_number from user, account_detail ad where login_name = ? and login_password = ? and user.id = ad.id";
        	
        	} else {
        		// assume pet_owner by default if not specify
        		sql = "select * from user where login_name = ? and login_password = ?";
        		
        	}
    		
        	PreparedStatement stmt = connection.prepareStatement(sql);
    		stmt.setString(1, user.getLoginName());
    		stmt.setString(2, user.getLoginPassword());
    		
            // Execute SQL query
    		ResultSet rs = stmt.executeQuery();  
    		
			// Display function to show the Resultset
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			JsonObject obj = new JsonObject();

	    	// Map result set returns from query
			boolean hasRecord = false;
			while (rs.next()) {
				hasRecord = true;
				obj.addProperty("id", rs.getInt("id"));
				obj.addProperty("username", rs.getString("login_name"));
				
				if (isLoggedInAsVeterinarian) {
					obj.addProperty("badgeNumber", rs.getString("badge_number"));
				}
				jsonResponse = gson.toJson(obj);
			}
			
			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
			}
			
			connection.close();
			
			// Return successful response if no error
			return Response.status(Response.Status.OK).entity(jsonResponse).build();

		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Fail to login")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	
	/**
	 * @api {PUT} /users/:id Update User information
	 * @apiName UpdateUser
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {Number} id Users unique ID.
	 * @apiParam {String} address  User address.
	 * @apiParam {String} contact User contact.
	 * @apiParamExample {json} Request-Example:
	 *     {
	 *       "address": "4711"
	 *       "contact": "4711"
	 *     }
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@Path("/register")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterAppUser(User user, @HeaderParam("badge_number") String badgeNumber, @HeaderParam("confirmation_code") int confirmationCode) {
		
		try {
			
			// Establish connection to Google Cloud MySQL
        	Connection connection = HandleConnection.getConnection();
        	Statement sqlStatement = connection.createStatement();	
        	
			String sql = "";

			if (badgeNumber != null) {
				// Update vet
				sql = "UPDATE user INNER JOIN account_detail on user.id = account_detail.id "
						+ "SET login_name = \"" + user.getLoginName() + "\", login_password = \"" + user.getLoginPassword() + "\" "
						+ "WHERE account_detail.badge_number = \"" + badgeNumber + "\"";
				
			} else if (confirmationCode != 0) {
				// Update pet's owner
				sql = "UPDATE user INNER JOIN account_detail on user.id = account_detail.id "
						+ "SET login_name = \"" + user.getLoginName() + "\", login_password = \"" + user.getLoginPassword() + "\" "
						+ "WHERE account_detail.confirmation_code = " + confirmationCode;
			
			} else {
				// Return error response
				return Response.status(Response.Status.BAD_REQUEST)
						.entity("A required header was not specified for this request.").build();
			}
			
			// Execute the SQL command
    		int resultUpdate = sqlStatement.executeUpdate(sql);
    		if (resultUpdate == 0) {
    			// Query returns no result
    			return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
    		}
    		
    		// Select the last modification record
    		PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE modification_time = (SELECT max(modification_time) FROM user)");  
    		ResultSet rs = stmt.executeQuery();  
						
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			
			// Map result set returns from query to custom User class
			JsonObject obj = new JsonObject();
			int id = 0;
			while (rs.next()) {
				id = rs.getInt("id");
				obj.addProperty("id", rs.getInt("id"));
				obj.addProperty("username", rs.getString("login_name"));
				
				jsonResponse = gson.toJson(obj);
			}

			// Execute the SQL command
			String sqlUpdateActive = "UPDATE account_detail SET active = 1 WHERE id = " + id;
    		sqlStatement.executeUpdate(sqlUpdateActive);
    		
    		connection.close();

			// Return successful response if no error
			return Response.status(Response.Status.OK).entity(jsonResponse).build();

		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
}
