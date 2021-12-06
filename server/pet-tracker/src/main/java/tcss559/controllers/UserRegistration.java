package tcss559.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import tcss559.request.UpdateUser;
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
					.entity(jsonResponse)
					.build();
						
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
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
			user.setConfirmationCode(confirmationCode);
			
			session.save(user);
			
			if (user.getPhoneNumber() != null) {
				
				// Send text message to user with the confirmation code. Pet owner must use this confirmation code to login with the app
				NotificationProvider.sendSMS(user.getPhoneNumber(),
						"Your account has been registered with PawTracker. You can now login to the app with this confimation code: "
								+ confirmationCode);
			}
			
			
			
			int userId = user.getId();
			System.out.println(userId);
						
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
	 * Check if the specified badge number exists in the system. If yes, returns the vet account detail.
	 * @param badgeNumber
	 * @return
	 */
	@Path("/vet/{badge_number}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVetAccountDetail(@PathParam("badge_number") String badgeNumber) {
				
		try { 
			
			// Establish connection to Google Cloud MySQL
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
	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateUser(User user, @PathParam("id") int id) {
		
		try { 
			user.setModificationTime(new Date());
			// Constructure JSON response from Java object
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // configure pretty print
			String jsonResponse = gson.toJson(user);

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
