package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import tcss559.hibernate.HibernateUtils;
import tcss559.model.User;
import tcss559.request.UpdateUser;
import tcss559.utilities.*;

/**
 * Root resource (exposed at "users" path).
 * A central service to handle CRUD functionalities related to user account.
 */
@Path("/users")
public class UserRegistration {
	
	/**
	 * Register pet owner by inserting a record to MySQL database. This function is meant to be used by veterinarian to register new user.
	 * Once registration is complete, pet's owner will receive text message with the confirmation code 
	 * to sign-up and login to the mobile app.
	 * 
	 * @api {POST} /users/pet_owner Register pet owner to the system
	 * 
	 * @apiDescription This function is meant to be used by veterinarian to register new user.
	 * Once registration is complete, pet's owner will receive text message with the confirmation code 
	 * to sign-up and login to the mobile app.
	 * 
	 * @apiName RegisterPetOwner
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {String} loginName  User's login name
	 * @apiParam {String} loginPassword User's login password
	 * @apiParam {String} email User's email
	 * @apiParam {String} phoneNumber User's phone number
	 * @apiParam {String} address User's address
	 * @apiParamExample {json} Request Body - Example:
	 *		{
	 *			"loginName": "user_master",
     *			"loginPassword": "123456!",
     *			"email": "user_master@uw.edu",
     *			"phoneNumber": "2067966856",
     *			"address": "3456 S Pacific Ave, Tacoma, WA, 98404"
	 *     	}
	 *     
	 * @apiSuccess {Number} id  User's unique ID
	 * @apiSuccess {String} creationTime Account's creation time
	 * @apiSuccess {String} modificationTime Last account's modification time
	 * @apiSuccess {String} loginName Default login name set by admin
	 * @apiSuccess {String} loginPassword Default login password set by admin
	 * @apiSuccess {String} role User's role
	 * @apiSuccess {String} email User's email
	 * @apiSuccess {String} phoneNumber User's phone number
	 * @apiSuccess {String} address User's address
	 * @apiSuccess {Number} active Status to identify if the user has already created an account on the mobile app
	 * @apiSuccess {Number} confirmationCode  Code that was sent to the user's phone. User will need this code to sign-up with the app
	 * 
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
	 *		    "confirmationCode": 12340
	 *		}
	 */
	@Path("/pet_owner")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterPetOwner(User user) {
		
		try {
			
			// Obtain the session
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();
			
			// Update User entity
			user.setCreationTime(new Date());
			user.setModificationTime(new Date());
			user.setRole(User.ROLE_PET_OWNER);
			
			// Persist User entity
			session.save(user);

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
				TextMessageService.sendSMS(user.getPhoneNumber(),
						"Your account has been registered with PawTracker. You can now login to the app with this confimation code: "
								+ uniqueCode);
			}
			
			System.out.println("save!!");
			
			// Persist the User entity and close the session
			session.update(user);
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
	 * @api {GET} /users/pet_owner View all pet owners
	 * @apiName ViewAllPetOwners
	 * @apiGroup UserRegistration
	 *
	 * @apiSuccess {Number} id Users unique ID.
	 * @apiSuccess {String} username  Login name of the pet owner.
	 * @apiSuccessExample {json} Success-Response:
	 *     	HTTP/1.1 200 OK
			{
			    "result": [
			        {
			            "user_id": 6,
			            "username": "johnDoe"
			        },
			        {
			            "user_id": 7,
			            "username": "psamrith"
			        },
			        {
			            "user_id": 8,
			            "username": "app_user1"
			        }
			    ]
			}
	 * @apiError(Error 404) UserNotFound No pet owner record is found.
	 */
	@Path("/pet_owner")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewAllPetOwners() {
				
		try { 
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
    		PreparedStatement stmt = connection.prepareStatement("SELECT u.id AS user_id, role, login_name FROM user u, account_detail ad WHERE u.id = ad.id AND ad.role = ? ");
    		stmt.setString(1, User.ROLE_PET_OWNER);

    		// Execute SQL query
    		ResultSet rs = stmt.executeQuery();  
    		
			// Display function to show the Resultset
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			
			JsonArray arr = new JsonArray();
			
			// Map result set returns from query
			boolean hasRecord = false;
			while (rs.next()) {
				hasRecord = true;
				
				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("user_id", rs.getInt("user_id"));
				eachElement.addProperty("username", rs.getString("login_name"));
				
				arr.add(eachElement);
			}	
			
			JsonObject obj = new JsonObject();
			obj.add("result", arr);
			
			jsonResponse = gson.toJson(obj);

			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
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
	 * @api {GET} /users/pet_owner/{id} View one pet owner information
	 * @apiName ViewOnePetOwner
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {Number} id User's unique ID
	 *
	 * @apiSuccess {Number} id User's unique ID
	 * @apiSuccess {String} role User's role
	 * @apiSuccess {String} email  User's email
	 * @apiSuccess {String} phoneNumber User's phone number
	 * @apiSuccess {Number} active Status to identify if the user has already created an account on the mobile app
	 * @apiSuccess {Number} confirmationCode  Code that was sent to the user's phone. User will need this code to sign-up with the app
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
			{
			    "id": 7,
			    "role": "PetOwner",
			    "email": "test111@uw.edu",
			    "phoneNumber": "2065966256",
			    "active": true,
			    "confirmationCode": 1215
			}
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the User was not found.
	 */
	@Path("/pet_owner/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewOnePetOwner(@PathParam("id") int userId) {
		
		try { 
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
    		PreparedStatement stmt = connection.prepareStatement("select * from account_detail where id = ? and account_detail.role = ?");
    		stmt.setInt(1, userId);
    		stmt.setString(2, User.ROLE_PET_OWNER);

    		// Execute SQL query
    		ResultSet rs = stmt.executeQuery();  
    		
			// Display function to show the Resultset
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			
			boolean hasRecord = false;

			// Map result set returns from query to custom User class
			while (rs.next()) {
				hasRecord = true;

				User user = new User(rs.getInt("id"), rs.getString("role"),
						rs.getInt("confirmation_code"), rs.getString("email"),
						rs.getString("phone_number"), rs.getString("address"), rs.getBoolean("active"));
				jsonResponse = gson.toJson(user);
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
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	
	/**
	 * Register veterinarian to the system.
	 * This endpoint is to be used by adminstrator only to register veterinarian.
	 * Veterinarian will receive an email once account is ready to register on the app.
	 * 
	 * @api {POST} /users/vet Register veterinarian to the system
	 * @apiName RegisterVeterinarian
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {String} loginName  Veterinarian's default login name
	 * @apiParam {String} loginPassword Veterinarian's default login password
	 * @apiParam {String} badgeNumber Veterinarian's badge number
	 * @apiParam {String} email Veterinarian's email
	 * @apiParam {String} phoneNumber Veterinarian's phone number
	 * @apiParam {String} address Veterinarian's address
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
	 * @apiSuccess {Number} id  User's unique ID
	 * @apiSuccess {String} creationTime Account's creation time
	 * @apiSuccess {String} modificationTime Last account's modification time
	 * @apiSuccess {String} loginName Default login name set by admin
	 * @apiSuccess {String} loginPassword Default login password set by admin
	 * @apiSuccess {String} role User's role
	 * @apiSuccess {String} email User's email
	 * @apiSuccess {String} phoneNumber User's phone number
	 * @apiSuccess {String} address User's address
	 * @apiSuccess {Number} active Status to identify if the user has already created an account on the mobile app
	 * 
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
	 *		    "active": false
	 *		}
	 */
	@Path("/vet")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response RegisterVeterinarian(UpdateUser user) {

		System.out.println("Attempt to register a new veterinarian");

		try {
			
			// Obtain the session
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();
			
			User newUser = new User();
			newUser.setLoginName(user.getLoginName());
			newUser.setLoginPassword(user.getLoginPassword());
			newUser.setBadgeNumber(user.getBadgeNumber());
			newUser.setAddress(user.getAddress());
			newUser.setPhoneNumber(user.getPhoneNumber());
			newUser.setEmail(user.getEmail());
			
			// Update User entity
			newUser.setCreationTime(new Date());
			newUser.setModificationTime(new Date());
			newUser.setRole(User.ROLE_VETERINARIAN);
			
			/*
			 * Send email to veterinarian to inform his/her that the badge number is registered 
			 * and user is ready to login to the app with badge number.
			 * This feature only applies to veterinarian role.
			 */
			if (user.getEmail()!= null) {
				
				// Send email to vet
				EmailService.gmailSender(user.getEmail(), "Welcome to Paw Tracker", String.format(
						"Hi %s, \n\nYour badge number %s is successfully registered to the system.\n\n"
						+ "Please download PawTracker app to register an account as veterinarian using the badge number.\n\n - Admin",
						user.getLoginName(), user.getBadgeNumber()));
				
			}
			
			// Persist the User entity
			session.save(newUser);
			t.commit();
	        session.close();
			
			// Constructure JSON response from Java object
	        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
	        String jsonResponse = gson.toJson(user);
	        
	        System.out.println("User is successfully registered. Return response");
	        
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
	 * @api {GET} /users/vet View all veterinarians
	 * @apiName ViewAllVeterinarians
	 * @apiGroup UserRegistration
	 *
	 * @apiSuccess {Number} user_id User's unique ID.
	 * @apiSuccess {String} username  Login name of the veterinarian
	 * @apiSuccess {String} email User's email
	 * @apiSuccess {String} phoneNumber User's phone number
	 * @apiSuccess {String} address User's address
	 * @apiSuccess {Number} active Status to identify if the user has already created an account on the mobile app
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     	HTTP/1.1 200 OK
			{
			    "result": [
			        {
			            "user_id": 14,
			            "username": "john",
			            "badgeNumber": "1112333",
			            "email": "test@uw.edu",
			            "phoneNumber": "2037966856",
			            "address": "3456 S Pacific Ave, Tacoma, WA, 98404",
			            "active": true
			        },
			        {
			            "user_id": 15,
			            "username": "doe",
			            "badgeNumber": "1144111",
			            "email": "test@gmail.com",
			            "phoneNumber": "2065936256",
			            "address": "4445 E Pacific Ave",
			            "active": false
			        }
			    ]
			}
	 * @apiError(Error 404) UserNotFound No veterinarian record is found.
	 */
	@Path("/vet")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewAllVeterinarians() {
				
		try { 
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
    		PreparedStatement stmt = connection.prepareStatement("SELECT u.id AS user_id, u.login_name, ad.badge_number, ad.email, ad.phone_number, ad.address, ad.active "
    				+ "FROM user u, account_detail ad "
    				+ "WHERE u.id = ad.id AND role = ?");
    		stmt.setString(1, User.ROLE_VETERINARIAN);

    		// Execute SQL query
    		ResultSet rs = stmt.executeQuery();  
    		
			// Display function to show the Resultset
			// Constructure JSON response
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = "";
			
			JsonArray arr = new JsonArray();
			
			// Map result set returns from query
			boolean hasRecord = false;
			while (rs.next()) {
				hasRecord = true;

				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("user_id", rs.getInt("user_id"));
				eachElement.addProperty("username", rs.getString("login_name"));
				eachElement.addProperty("badgeNumber", rs.getString("badge_number"));
				eachElement.addProperty("email", rs.getString("email"));
				eachElement.addProperty("phoneNumber", rs.getString("phone_number"));
				eachElement.addProperty("address", rs.getString("address"));
				eachElement.addProperty("active", rs.getBoolean("active"));

				arr.add(eachElement);
			}	
			
			JsonObject obj = new JsonObject();
			obj.add("result", arr);
			
			jsonResponse = gson.toJson(obj);

			// Query returns no result
			if (!hasRecord) {
				return Response.status(Response.Status.NOT_FOUND).entity("No record found").build();
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
	 * @api {GET} /users/vet/{badge_number} View veterinarian information
	 * @apiName ViewOneVeterinarian
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {Number} badge_number Veterinarian's badge number

	 * @apiSuccess {Number} id  User's unique ID
	 * @apiSuccess {String} modificationTime Last account's modification time
	 * @apiSuccess {String} loginName Username to be used to login to the app
	 * @apiSuccess {String} role User's role
	 * @apiSuccess {Number} badge_number Veterinarian's badge number
	 * @apiSuccess {String} email User's email
	 * @apiSuccess {String} phoneNumber User's phone number
	 * @apiSuccess {String} address User's address
	 * @apiSuccess {Number} active Status to identify if the user has already created an account on the mobile app
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
			{
			    "id": 8,
			    "modificationTime": "Dec 6, 2021",
			    "loginName": "new_vet",
			    "role": "Veterinarian",
			    "badgeNumber": "3456607B",
			    "email": "vet_master@uw.edu",
			    "phoneNumber": "2067966856",
			    "address": "3456 S Pacific Ave, Tacoma, WA, 98404",
			    "active": true
			}
	 * @apiError(Error 404) UserNotFound User with the badge number does not exist
	 */
	@Path("/vet/{badge_number}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewOneVeterinarian(@PathParam("badge_number") String badgeNumber) {
				
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

			boolean hasRecord = false;

			// Map result set returns from query to custom User class
			while (rs.next()) {
				hasRecord = true;

				User user = new User(rs.getInt("user_id"), rs.getDate("modification_time"), rs.getString("role"),
						rs.getString("login_name"), rs.getString("badge_number"), rs.getString("email"),
						rs.getString("phone_number"), rs.getString("address"), rs.getBoolean("active"));
				jsonResponse = gson.toJson(user);
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
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * @api {DELETE} /users/{id} Delete a user
	 * @apiName DeleteUser
	 * @apiGroup UserRegistration
	 *
	 * @apiParam {Number} id ID of the user to be deleted
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@SuppressWarnings("rawtypes")
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteUser(@PathParam("id") int id) {
		
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		
		// disable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();

		// delete a record of the specified user ID
		Query query = session.createSQLQuery("DELETE FROM user where id = :id");
		query.setParameter("id", id);
		query.executeUpdate();

		// enable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
		
		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
	}
	
	/**
	 * @api {DELETE} /users Delete all users
	 * @apiName DeleteAllUsers
	 * @apiGroup UserRegistration
	 *
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@Path("")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteAllUsers() {
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		
		// disable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();
		
		// delete all records in user table
		session.createSQLQuery("DELETE FROM user").executeUpdate();
		
		// enable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();

		t.commit();
        session.close();
        
        return Response.status(Response.Status.OK).entity("").build();	
	}
	
}
