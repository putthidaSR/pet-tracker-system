package tcss559.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import tcss559.model.Pet;
import tcss559.model.PetDetail;
import tcss559.model.User;
import tcss559.utilities.*;


@Path("/pets")
public class PetRegistration {
	
	@Path("/test")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() {
		return Response.status(Response.Status.OK).entity("Success").build();
	}
	
	/**
	 * @api {POST} /pets Add Pet information
	 * @apiName AddPet
	 * @apiGroup PetRegistration
	 *
	 * @apiParam {Number} userId Related user unique ID.
	 * @apiParam {String} rfidNumber  Pet rfid number.
	 * @apiParam {String} name Pet name.
	 * @apiParam {String} category  Pet category.
	 * @apiParam {Number} year Pet category.
	 * @apiParamExample {json} Request-Example:
			{
			    "species": "Dog",
			    "name": "fffFluffy",
			    "age": "8 months",
			    "breed": "husky",
			    "color": "white",
			    "gender": "Male"
			}
	 * @apiSuccess {Number} id  Users unique ID.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *	{
	 *	    "id": 1
	 *	}
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
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * Only active vet can view all pets.
	 * @param badgeNumber
	 * @return
	 */
	@Path("/")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response GetAllPets(@HeaderParam("badge_number") String badgeNumber) {
				
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
		String response;
		try {
			
			String serviceURL = "http://localhost:8080/PawTracker/users/vet/" + badgeNumber;
			response = HandleConnection.getResourceFromURL(serviceURL);

		} catch (Exception error) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Fail to view pet details as the provided badge number is not found in our system.")
					.build();
		}
				
		try {
			
			// select * from pet, pet_detail where pet.id = pet_detail.pet_detail_id;
			
			// Establish connection to MySQL server
        	Connection connection = HandleConnection.getConnection();
        	
        	// Construct the query to return matching record
    		PreparedStatement stmt = connection.prepareStatement("SELECT u.id AS user_id, role, login_name FROM user u, account_detail ad WHERE u.id = ad.id AND ad.role = ? ");
    		stmt.setString(1, User.ROLE_PET_OWNER);

    		// Execute SQL query
    		ResultSet rs = stmt.executeQuery();  
    		
						
			// Return successful response if no error
			return Response.status(Response.Status.OK)
					.entity(response)
					.build();
			
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	@Path("/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getOnePet(@PathParam("id") int petId) {
		
		try {
			
			Session session = HibernateUtils.getSession();
			Query query = session.createQuery("from Pet where id= :id");
			List<Pet> petList = query.setParameter("id", petId).list();
	        session.close();
	        if (petList.isEmpty()) {
	        	return Response.status(Response.Status.NOT_FOUND).entity("").build();	
	        }
	        Gson g = new Gson();
	        String responseData = g.toJson(petList.get(0));
			
			// Return successful response if no error
			return Response.status(Response.Status.OK)
					.entity(responseData)
					.build();
			
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	@Path("/details")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewAllPetsWithDetails(@HeaderParam("badge_number") String badgeNumber) {
		
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
			
			// select * from pet, pet_detail where pet.id = pet_detail.pet_detail_id;
			
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
			return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create a record")
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
//	
//	/**
//	 * @api {PUT} /pets/:id Update Pet information
//	 * @apiName upatePet
//	 * @apiGroup PetRegistration
//	 *
//	 * @apiParam {Number} userId Related user unique ID.
//	 * @apiParam {String} rfidNumber  Pet rfid number.
//	 * @apiParam {String} name Pet name.
//	 * @apiParam {String} category  Pet category.
//	 * @apiParamExample {json} Request-Example:
//	 *     {
//	 *         "rfidNumber": "0000000",
//	 *         "name": "john",
//	 *         "category": "test",
//	 *         "year": 1
//	 *     }
//	 * @apiSuccessExample {json} Success-Response:
//	 *     HTTP/1.1 200 OK
//	 */
////	@Path("/{id}")
////	@PUT
////	@Consumes(MediaType.APPLICATION_JSON)
////	@Produces(MediaType.TEXT_PLAIN)
////	public Response upatePet(UpdatePet updatePet, @PathParam("id") int id) {
////		Session session = HibernateUtils.getSession();
////		Query query = session.createQuery("from Pet where id= :id and active = 'Y'");
////		List<Pet> petList = query.setParameter("id", id).list();
////		if (petList.isEmpty()) {
////			return Response.status(Response.Status.NOT_FOUND).entity("").build();	
////		}
////		Pet pet = petList.get(0);
////		pet.setModifyTime(new Date());
////		String rfidNumber = updatePet.getRfidNumber();
////		if (rfidNumber != null & rfidNumber.length() != 0) {
////			pet.setRfidNumber(rfidNumber);
////		}
////		String name = updatePet.getName();
////		if (name != null & name.length() != 0) {
////			pet.setName(name);
////		}
////		String category = updatePet.getCategory();
////		if (category != null & category.length() != 0) {
////			pet.setCategory(category);
////		}
////		session.update(pet);
////		Transaction t = session.beginTransaction();
////		t.commit();
////        session.close();
////        return Response.status(Response.Status.OK).entity("").build();
////	}
//	
//	/**
//	 * @api {DELETE} /pets Delete Pets information
//	 * @apiName deleteAllPet
//	 * @apiGroup PetRegistration
//	 *
//	 * 
//	 * @apiSuccessExample {json} Success-Response:
//	 *     HTTP/1.1 200 OK
//	 */
//	@Path("")
//	@DELETE
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response deleteAllPet() {
//		Session session = HibernateUtils.getSession();
//		Transaction t = session.beginTransaction();
//		String sql = "update pet set active = 'N'";
//		Query query = session.createSQLQuery(sql);
//		query.executeUpdate();
//		t.commit();
//        session.close();
//        return Response.status(Response.Status.OK).entity("").build();	
//	}
//	
//	
//	/**
//	 * @api {GET} /pets/:id Request Pet information
//	 * @apiName getPet
//	 * @apiGroup PetRegistration
//	 *
//	 * @apiParam {Number} id Users unique ID.
//	 *
//	 * @apiSuccess {Number} id Pet unique ID.
//	 * @apiSuccess {Number} userId Related user unique ID.
//	 * @apiSuccess {String} rfidNumber  Pet rfid number.
//	 * @apiSuccess {String} name Pet name.
//	 * @apiSuccess {String} category  Pet category.
//	 * @apiSuccess {Number} year Pet category.
//	 * @apiSuccess {Number} createTime  Pet create time.
//	 * @apiSuccess {Number} modifyTime  Pet modify time.
//	 * @apiSuccess {String} active  Pet data status.
//	 * @apiSuccessExample {json} Success-Response:
//	 *     HTTP/1.1 200 OK
//	 *     {
//	 *         "id": 0,
//	 *         "userId": 1,
//	 *         "rfidNumber": "0000000",
//	 *         "name": "john",
//	 *         "category": "test",
//	 *         "year": 1,
//	 *         "createTime": 1293811200000,
//	 *         "modifyTime": null,
//	 *         "active": "Y"
//	 *     }
//	 * @apiError(Error 404) UserNotFound The <code>id</code> of the User was not found.
//	 */
//	@Path("/{id}")
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getPet(@PathParam("id") int id) {
//		Session session = HibernateUtils.getSession();
//		Query query = session.createQuery("from Pet where id= :id and active = 'Y'");
//		List<Pet> petList = query.setParameter("id", id).list();
//        session.close();
//        if (petList.isEmpty()) {
//        	return Response.status(Response.Status.NOT_FOUND).entity("").build();	
//        }
//        Gson g = new Gson();
//        String responseData = g.toJson(petList.get(0));
//        return Response.status(Response.Status.OK).entity(responseData).build();
//	}
//	
//	/**
//	 * @api {DELETE} /pets/:id Delete Pet information
//	 * @apiName deletePet
//	 * @apiGroup PetRegistration
//	 *
//	 * @apiParam {Number} id Pet unique ID.
//	 * 
//	 * @apiSuccessExample {json} Success-Response:
//	 *     HTTP/1.1 200 OK
//	 */
//	@Path("/{id}")
//	@DELETE
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response deletePet(@PathParam("id") int id) {
//		Session session = HibernateUtils.getSession();
//		Transaction t = session.beginTransaction();
//		Query query = session.createQuery("update Pet set active = 'N' where id = :id ");
//		query.setParameter("id", id);
//		query.executeUpdate();
//		t.commit();
//        session.close();
//        return Response.status(Response.Status.OK).entity("").build();	
//    }	
}
