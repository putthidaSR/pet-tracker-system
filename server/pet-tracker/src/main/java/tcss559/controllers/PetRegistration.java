package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import tcss559.hibernate.HibernateUtils;
import tcss559.model.Pet;
import tcss559.model.PetDetail;
import tcss559.model.User;
import tcss559.request.UpdatePet;
import tcss559.utilities.*;

/**
 * Root resource (exposed at "pets" path).
 * This resource class contains functionalities related to pet registration/account.
 */
@Path("/pets")
public class PetRegistration {
	
	/**
	 * @api {POST} /pets Add Pet information
	 * @apiName AddPet
	 * @apiGroup PetRegistration
	 * 
	 * @apiHeader {String} rfid_number Pet's unique RFID tag number
	 * @apiHeader {Number} user_id User (pet owner)'s unique ID number
	 * 
	 * @apiParam {String} species Pet's species (Support: Dog, Cat, Other)
	 * @apiParam {String} name Pet's name
	 * @apiParam {String} age Pet's age
	 * @apiParam {String} breed Pet's breed
	 * @apiParam {String} color Pet's color
	 * @apiParam {String} gender Pet's identity (Support: Female, Male, Neutered)
	 * 
	 * @apiParamExample {json} Request-Example:
			{
			    "species": "Dog",
			    "name": "fffFluffy",
			    "age": "8 months",
			    "breed": "husky",
			    "color": "white",
			    "gender": "Male"
			}
	 * @apiSuccess {Number} id  Pet's unique ID
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
	 *		{
	 *	    	"id": 1
	 *		}
	 */
	@Path("/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AddPet(PetDetail detail, 
			@HeaderParam("rfid_number") String rfidNumber, @HeaderParam("user_id") int userId) {
		
		System.out.println("Attempt to register a new pet");
        
        try {
        	
        	// Obtain the session
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();

			// Create pet entity
			Pet pet = new Pet();
			pet.setUser(new User(userId));
			pet.setRfidNumber(rfidNumber);
			pet.setRegistrationTime(new Date());
			pet.setModificationTime(new Date());
			
			detail.setActive(true);
			detail.setPet(pet);
			
			// Persist the entities
			session.save(pet);
			session.save(detail);
			t.commit();
			
			int petId = pet.getId();
			System.out.println(petId);
			
			session.close();
        	
        	// Return successful response if no error
			return Response.status(Response.Status.OK)
					.entity(petId)
					.build();
						
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * @api {GET} /pets Request all pet informations
	 * @apiDescription This endpoint is used by the veterinarian only. User with veterinarian role must specify the valid badge number.
	 * @apiName ViewAllPets
	 * @apiGroup PetRegistration
	 * 
	 * @apiHeader {String} badge_number Badge number of the user with veterinarian role
	 * 
	 * @apiSuccess {Number} id User's unique ID (pet's owner)
	 * @apiSuccess {String} username Name of the pet's owner
	 * @apiSuccess {String} email Email of the pet's owner
	 * @apiSuccess {Boolean} userActive Status to indicate if the user has registered an account in the mobile app
	 * @apiSuccess {Number} petId Pet's unique ID
	 * @apiSuccess {String} petName Pet's name
	 * @apiSuccess {String} rfidNumber Pet's RFID tag number
	 * @apiSuccess {Boolean} rfidStatus Status to indicate if the RFID tag is active
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *    	HTTP/1.1 200 OK
			{
		    	"results": [
			        {
			            "userId": 6,
			            "username": "thida",
			            "email": "johnDoe@uw.edu",
			            "userActive": true,
			            "petId": 3,
			            "petName": "test2",
			            "rfidNumber": "2234455555",
			            "rfidStatus": true
			        },
			        {
			            "userId": 6,
			            "username": "thida",
			            "email": "jessica@uw.edu",
			            "userActive": true,
			            "petId": 2,
			            "petName": "test",
			            "rfidNumber": "dsfdsf3333",
			            "rfidStatus": true
			        }
				]
			}
			
	 */
	@Path("/")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewAllPets(@HeaderParam("badge_number") String badgeNumber) {
		
		if (badgeNumber == null) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Please provide badge number as the header.")
					.build();
		}
		
		/*
		 * Check if the provided badge number exists in the system by invoking the
		 * provided service URL to get resource
		 */
		try {
			
			String serviceURL = "http://localhost:8080/PawTracker/users/vet/" + badgeNumber;
			HandleConnection.getResourceFromURL(serviceURL);

		} catch (Exception error) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Fail to view pet details as the provided badge number is not found in our system.")
					.build();
		}
		
		
		try {
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
        	String sql = "SELECT u.id AS user_id, u.login_name AS user_name, "
        			+ "ad.phone_number, ad.email, ad.address, ad.active AS user_active, "
        			+ "p.id AS pet_id, pd.name AS pet_name, p.rfid_number, pd.active AS rfid_status "
        			+ "FROM user u "
        			+ "JOIN pet p ON u.id = p.user_id "
        			+ "JOIN pet_detail pd ON p.id = pd.pet_detail_id "
        			+ "JOIN account_detail ad ON u.id = ad.id "
        			+ "ORDER BY p.id DESC";
        	
    		PreparedStatement stmt = connection.prepareStatement(sql);

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
				
				// Each element in the array (pet with details)
				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("userId", rs.getInt("user_id"));
				eachElement.addProperty("username", rs.getString("user_name"));
				eachElement.addProperty("phoneNumber", rs.getString("phone_number"));
				eachElement.addProperty("email", rs.getString("email"));
				eachElement.addProperty("address", rs.getString("address"));
				eachElement.addProperty("userActive", rs.getBoolean("user_active"));
				eachElement.addProperty("petId", rs.getInt("pet_id"));
				eachElement.addProperty("petName", rs.getString("pet_name"));
				eachElement.addProperty("rfidNumber", rs.getString("rfid_number"));
				eachElement.addProperty("rfidStatus", rs.getBoolean("rfid_status"));

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
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * @api {GET} /pets/{rfid_number} Request pet information by RFID tag number
	 * @apiName ViewPetByRfidNumber
	 * @apiGroup PetRegistration
	 * 
	 * @apiParam {String} rfid_number Pet's RFID tag number
	 * 
	 * @apiSuccess {Number} userId User's unique ID (pet's owner)
	 * @apiSuccess {String} username Name of the pet's owner
	 * @apiSuccess {String} email Email of the pet's owner
	 * @apiSuccess {Boolean} userActive Status to indicate if the user has registered an account in the mobile app
	 * @apiSuccess {Number} petId Pet's unique ID
	 * @apiSuccess {String} petName Pet's name
	 * @apiSuccess {String} rfidNumber Pet's RFID tag number
	 * @apiSuccess {Boolean} rfidStatus Status to indicate if the RFID tag is active
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *    	HTTP/1.1 200 OK
			{
			  "results": [
			        {
			            "userId": 6,
			            "username": "thida",
			            "email": "test111@uw.edu",
			            "userActive": false,
			            "petId": 2,
			            "petName": "test",
			            "rfidNumber": "dsfdsf3333",
			            "rfidStatus": true
			        }
			    ]
			}
			
	 * @apiError(Error 404) UserNotFound Fail to find any record with the specified RFID.
	 */
	@Path("/{rfid_number}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewPetByRfidNumber(@PathParam("rfid_number") String rfidNumber) {
		
		try {
			
			// Establish connection to MySQL server
			Connection connection = HandleConnection.getConnection();
			
			// Construct the query to return matching record
			String sql = "SELECT u.id AS user_id, u.login_name AS user_name, "
					+ "ad.phone_number, ad.email, ad.address, ad.active AS user_active, "
					+ "p.id AS pet_id, pd.name AS pet_name, p.rfid_number, pd.active AS rfid_status "
					+ "FROM user u "
					+ "JOIN pet p ON u.id = p.user_id "
					+ "JOIN pet_detail pd ON p.id = pd.pet_detail_id "
					+ "JOIN account_detail ad ON u.id = ad.id "
					+ "WHERE p.rfid_number = ? "
					+ "ORDER BY p.id DESC";
			
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setString(1, rfidNumber);
		
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
				
				// Each element in the array (pet with details)
				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("userId", rs.getInt("user_id"));
				eachElement.addProperty("username", rs.getString("user_name"));
				eachElement.addProperty("phoneNumber", rs.getString("phone_number"));
				eachElement.addProperty("email", rs.getString("email"));
				eachElement.addProperty("address", rs.getString("address"));
				eachElement.addProperty("userActive", rs.getBoolean("user_active"));
				eachElement.addProperty("petId", rs.getInt("pet_id"));
				eachElement.addProperty("petName", rs.getString("pet_name"));
				eachElement.addProperty("rfidNumber", rs.getString("rfid_number"));
				eachElement.addProperty("rfidStatus", rs.getBoolean("rfid_status"));
		
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
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * @api {GET} /pets/user/{user_id} Request pet informations that belongs to specific user
	 * @apiName ViewPetsByUser
	 * @apiGroup PetRegistration
	 * 
	 * @apiParam {String} user_id User's unique ID
	 * 
	 * @apiSuccess {Number} userId User's unique ID (pet's owner)
	 * @apiSuccess {String} username Name of the pet's owner
	 * @apiSuccess {String} email Email of the pet's owner
	 * @apiSuccess {String} phoneNumber User's phone number
	 * @apiSuccess {String} address User's address
	 * @apiSuccess {Boolean} userActive Status to indicate if the user has registered an account in the mobile app
	 * @apiSuccess {Number} petId Pet's unique ID
	 * @apiSuccess {String} petName Pet's name
	 * @apiSuccess {String} rfidNumber Pet's RFID tag number
	 * @apiSuccess {Boolean} rfidStatus Status to indicate if the RFID tag is active
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *    	HTTP/1.1 200 OK
			{
			  "results": [
			        {
			            "userId": 6,
			            "username": "thida",
			            "email": "test111@uw.edu",
			            "phoneNumber": "2067966856",
			            "address": "3456 S Pacific Ave, Tacoma, WA, 98404"
			            "userActive": false,
			            "petId": 2,
			            "petName": "test",
			            "rfidNumber": "3454678LDF35",
			            "rfidStatus": true
			        }
			    ]
			}
			
	 * @apiError(Error 404) UserNotFound Fail to find any record with the specified user ID.
	 */
	@Path("/user/{user_id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewPetsByUser(@PathParam("user_id") int userId) {
				
		try {
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
        	String sql = "SELECT u.id AS user_id, u.login_name AS user_name, "
        			+ "ad.phone_number, ad.email, ad.address, ad.active AS user_active, "
        			+ "p.id AS pet_id, pd.name AS pet_name, p.rfid_number, pd.active AS rfid_status "
        			+ "FROM user u "
        			+ "JOIN pet p ON u.id = p.user_id "
        			+ "JOIN pet_detail pd ON p.id = pd.pet_detail_id "
        			+ "JOIN account_detail ad ON u.id = ad.id "
        			+ "WHERE u.id = ? "
        			+ "ORDER BY p.id DESC";
        	
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		stmt.setInt(1, userId);

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
				
				// Each element in the array (pet with details)
				JsonObject eachElement = new JsonObject();
				eachElement.addProperty("userId", rs.getInt("user_id"));
				eachElement.addProperty("username", rs.getString("user_name"));
				eachElement.addProperty("phoneNumber", rs.getString("phone_number"));
				eachElement.addProperty("email", rs.getString("email"));
				eachElement.addProperty("address", rs.getString("address"));
				eachElement.addProperty("userActive", rs.getBoolean("user_active"));
				eachElement.addProperty("petId", rs.getInt("pet_id"));
				eachElement.addProperty("petName", rs.getString("pet_name"));
				eachElement.addProperty("rfidNumber", rs.getString("rfid_number"));
				eachElement.addProperty("rfidStatus", rs.getBoolean("rfid_status"));

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
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * @api {PUT} /pets/{id} Update pet information
	 * @apiName UpdatePet
	 * @apiGroup PetRegistration
	 *
	 * @apiParam {Number} id Pet's unique ID
	 * @apiParam {String} rfidNumber  Pet rfid number.
	 * @apiParam {String} name Pet's name
	 * @apiParam {String} age Pet's age
	 * @apiParam {Boolean} active Status to indicate if the RFID tag is active
	 * 
	 * @apiParamExample {json} Request-Example:
	 *     {
	 *         "rfidNumber": "22A0000000",
	 *         "name": "john",
	 *         "age": "2 months",
	 *         "active": true
	 *     }
	 *     
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdatePet(UpdatePet updatePet, @PathParam("id") int id) {
		try {
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
			// Update RFID
			String rfidNumber = updatePet.getRfidNumber();
			if (rfidNumber != null && rfidNumber.length() != 0) {
				PreparedStatement stmt = connection.prepareStatement("update pet set rfid_number = ? where id = ?");
	    		stmt.setString(1, rfidNumber);
	    		stmt.setInt(2, id);
	    		stmt.executeUpdate();
			}
			
			// Update pet name
			String name = updatePet.getName();
			if (name != null && name.length() != 0) {
				PreparedStatement stmt = connection.prepareStatement("update pet_detail set name = ? where pet_detail_id = ?");
	    		stmt.setString(1, name);
	    		stmt.setInt(2, id);
	    		stmt.executeUpdate();
			}

			// Update pet age
			String age = updatePet.getAge();
			if (age != null && age.length() != 0) {
				PreparedStatement stmt = connection.prepareStatement("update pet_detail set age = ? where pet_detail_id = ?");
				stmt.setString(1, age);
				stmt.setInt(2, id);
				stmt.executeUpdate();
			}

			// Update pet active status
			boolean status = updatePet.isActive();
			System.out.println(status);
			PreparedStatement stmt = connection.prepareStatement("update pet_detail set active = ? where pet_detail_id = ?");
			stmt.setBoolean(1, status);
			stmt.setInt(2, id);
			stmt.executeUpdate();
			
			connection.close();
			
			// Return successful response if no error
			return Response.status(Response.Status.OK).entity("").build();

		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Error Message: " + e.getLocalizedMessage())
					.build();
		}
	}
	
	/**
	 * @api {DELETE} /pets/{id} Delete a pet
	 * @apiName DeletePet
	 * @apiGroup PetRegistration
	 *
	 * @apiParam {Number} id Pet's unique ID
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@SuppressWarnings("rawtypes")
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)	
	public Response DeletePet(@PathParam("id") int id) {
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		
		// disable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();

		// delete a record of the specified pet ID
		Query query = session.createSQLQuery("DELETE FROM pet where id = :id");
		query.setParameter("id", id);
		query.executeUpdate();

		// enable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();

		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
    }	
	
	/**
	 * @api {DELETE} /pets Delete all pets
	 * @apiName DeleteAllPets
	 * @apiGroup PetRegistration
	 *
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@Path("")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteAllPets() {
		
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		
		// disable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();
		
		// delete all records in pet table
		session.createSQLQuery("DELETE FROM pet").executeUpdate();
		
		// enable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();

		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
	}
}
