package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import tcss559.hibernate.HibernateUtils;
import tcss559.model.User;
import tcss559.utilities.*;

@Path("/users")
public class UserRegistration {
	
	/**
	 * Register veterinarian to the system.
	 * This endpoint is to be used on the website by adminstrator only.
	 * 
	 * @api {POST} /users/vet Register veterinarian to the system
	 * @apiName RegisterVeterinarian
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {String} loginName  Veterinarian's default login name.
	 * @apiParam {String} loginPassword Veterinarian's default login password.
	 * @apiParam {String} badgeNumber Veterinarian's badge number.
	 * @apiParam {String} email Veterinarian's email.
	 * @apiParam {String} phoneNumber Veterinarian's phone number.
	 * @apiParam {String} address Veterinarian's address.
	 * @apiParamExample {json} Request Body - Example:
	 *		{
	 *			"loginName": "vet_master",
     *			"loginPassword": "123456!",
     *			"badgeNumber": "3456607B",
     *			"email": "vet_master@uw.edu",
     *			"phoneNumber": "2067966856",
     *			"address": "3456 S Pacific Ave, Tacoma, WA, 98404"
	 *     	}
	 * 
	 * @apiSuccess {Number} active Status to identify if the veterinarian has already created an account on the mobile app.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *		{
	 *		    "id": 7,
	 *		    "creationTime": "Dec 5, 2021, 5:13:03 PM",
	 *		    "modificationTime": "Dec 5, 2021, 5:13:03 PM",
	 *		    "loginName": "vet_master",
	 *		    "loginPassword": "123456!",
	 *		    "role": "Vetarinarian",
	 *		    "email": "vet_master@uw.edu",
	 *		    "phoneNumber": "2067966856",
	 *		    "address": "3456 S Pacific Ave, Tacoma, WA, 98404"
	 *		    "active": false,
	 *		    "confirmationCode": 0
	 *		}
	 */
	@Path("/vet")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterVeterinarian(User user) {
		
		try {
			
			System.out.println("Attempt to register a new veterinarian");
			
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();
			
			user.setCreationTime(new Date());
			user.setModificationTime(new Date());
			user.setRole(User.ROLE_VETERINARIAN);
			
			/*
			 * Send email to veterinarian to inform his/her that the badge number is registered 
			 * and user is ready to login to the app with badge number.
			 * This feature only applies to veterinarian role.
			 */
			if (user.getEmail()!= null) {
				
				// TODO: Send email. Vet must use this confirmation code to login with the app
				System.out.println("To Do: Send email");
				
			}
			
			session.save(user);
			t.commit();
	        session.close();
			
			// Constructure JSON response from Java object
	        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
	        String jsonResponse = gson.toJson(user);

			// Return successful response if no error
			return Response.status(Response.Status.OK)
					.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
					.entity(jsonResponse)
					.build();
						
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
					.header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Credentials", "true")
					.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * Register pet owner by inserting a record to MySQL database.
	 * 
	 * @api {POST} /users/pet_owner Register user to the system
	 * @apiName RegisterUser
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {String} loginName  User's login name.
	 * @apiParam {String} loginPassword User's login password.
	 * @apiParam {String} email User's email.
	 * @apiParam {String} phoneNumber User's phone number.
	 * @apiParam {String} address User's address.
	 * @apiParamExample {json} Request Body - Example:
	 *		{
	 *			"loginName": "user_master",
     *			"loginPassword": "123456!",
     *			"email": "user_master@uw.edu",
     *			"phoneNumber": "2067966856",
     *			"address": "3456 S Pacific Ave, Tacoma, WA, 98404"
	 *     	}
	 *     
	 * @apiSuccess {Number} id  Users unique ID.
	 * @apiSuccess {Number} active Status to identify if the veterinarian has already created an account on the mobile app.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *		{
	 *		    "id": 7,
	 *		    "creationTime": "Dec 5, 2021, 5:13:03 PM",
	 *		    "modificationTime": "Dec 5, 2021, 5:13:03 PM",
	 *		    "loginName": "user_master",
	 *		    "loginPassword": "123456!",
	 *		    "role": "PetOwner",
	 *		    "email": "user_master@uw.edu",
	 *		    "phoneNumber": "2067966856",
	 *		    "address": "3456 S Pacific Ave, Tacoma, WA, 98404"
	 *		    "active": false,
	 *		    "confirmationCode": 0
	 *		}
	 */
	@Path("/pet_owner")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterUser(User user) {
		
		try {
			
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();
			
			user.setCreationTime(new Date());
			user.setModificationTime(new Date());
			user.setRole(User.ROLE_PET_OWNER);
			
			/*
			 * Set random generated number as confirmation code to be sent to user's phone as text message.
			 * This feature only applies to Pet Owner role.
			 */
			Random random = new Random();
			int confirmationCode = random.ints(1000, 9999).findFirst().getAsInt();
			
			// Generate unique code by placing user ID in front (since user ID is primary key, it is auto-incremented and unique)
			int uniqueCode = Integer.valueOf(String.valueOf(user.getId()) + String.valueOf(confirmationCode));
			user.setConfirmationCode(uniqueCode);
			
			if (user.getPhoneNumber() != null) {
				
				// Send text message to user with the confirmation code. Pet owner must use this confirmation code to login with the app
				NotificationProvider.sendSMS(user.getPhoneNumber(),
						"Your account has been registered with PawTracker. You can now login to the app with this confimation code: "
								+ confirmationCode);
			}
			
			session.save(user);
			t.commit();
	        session.close();
			
			// Constructure JSON response from Java object
	        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
	        String jsonResponse = gson.toJson(user);

			// Return successful response if no error
			return Response.status(Response.Status.OK)
					.entity(jsonResponse)
					.build();
						
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * Check if the specified account exists in the system. If yes, returns the account detail.
	 * @param badgeNumber
	 * @return
	 */
	@Path("/")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserAccountDetail(@PathParam("badge_number") String badgeNumber) {
				
		try { 
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	Statement sqlStatement = connection.createStatement();	
        	
        	// Execute the SQL command
			String sql = "SELECT u.id AS user_id, modification_time, role, login_name, badge_number, email, phone_number, address, active "
			+ "FROM user u, account_detail ad WHERE u.id = ad.id AND ad.badge_number = \"" + badgeNumber + "\" LIMIT 0,1";

			ResultSet rs = sqlStatement.executeQuery(sql);
						
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";

			// Query returns no result
			if (!rs.next()) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
			}
			
			// Map result set returns from query to custom User class
			while (rs.next()) {
				User user = new User(rs.getInt("user_id"), rs.getDate("modification_time"), rs.getString("role"),
						rs.getString("login_name"), rs.getString("badge_number"), rs.getString("email"),
						rs.getString("phone_number"), rs.getString("address"), rs.getBoolean("active"));
				jsonResponse = gson.toJson(user);
			}

			connection.close();
			
			// Return successful response if no error
			return Response.status(Response.Status.OK).entity(jsonResponse).build();

		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * DONE
	 * 
	 * @param user
	 * @return
	 */
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AccountLogin(User user) {
		
		try {

			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
    		PreparedStatement stmt = connection.prepareStatement("select * from user where login_name = ? and login_password = ?");
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
	@Path("/")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateUser(User user, @HeaderParam("badge_number") String badgeNumber, @HeaderParam("confirmation_code") int confirmationCode) {
		
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
	
	/**
	 * @api {DELETE} /users Delete Users information
	 * @apiName deleteAllUser
	 * @apiGroup UserRegistration
	 *
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@Path("")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteAllUser() {
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		String sql = "update user set active = 'N'";
		Query query = session.createSQLQuery(sql);
		query.executeUpdate();
		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
	}
	
	
	/**
	 * @api {GET} /users/:id Request User information
	 * @apiName getUser
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {Number} id Users unique ID.
	 *
	 * @apiSuccess {Number} id Users unique ID.
	 * @apiSuccess {String} address  User address.
	 * @apiSuccess {String} contact User contact.
	 * @apiSuccess {Number} createTime  User create time.
	 * @apiSuccess {Number} modifyTime  User modify time.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *	{
	 *	    "id": 1,
	 *	    "address": "123456",
	 *	    "contact": "1",
	 *	    "createTime": 1638633710000,
	 *	    "modifyTime": 1638633710000
	 *	}
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the User was not found.
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("id") int id) {
		Session session = HibernateUtils.getSession();
		Query query = session.createQuery("from User where id= :id and active = 'Y'");
		List<User> userList = query.setParameter("id", id).list();
        session.close();
        if (userList.isEmpty()) {
        	Response.status(Response.Status.NOT_FOUND).entity("").build();	
        }
        Gson g = new Gson();
        String responseData = g.toJson(userList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();	
	}
	
	/**
	 * @api {DELETE} /users/:id Delete User information
	 * @apiName deleteUser
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {Number} id Users unique ID.
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("id") int id) {
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		Query query = session.createQuery("update User set active = 'N' where id = :id ");
		query.setParameter("id", id);
		query.executeUpdate();
		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
	}
	
}
