package tcss559.controllers;

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

import tcss559.hibernate.HibernateUtils;
import tcss559.model.Pet;
import tcss559.model.PetDetail;
import tcss559.model.User;


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
