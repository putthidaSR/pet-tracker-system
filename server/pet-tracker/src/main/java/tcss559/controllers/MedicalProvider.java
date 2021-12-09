package tcss559.controllers;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
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
import tcss559.model.PetMedical;

/**
 * Root resource (exposed at "pets" path).
 * This resource class contains functionalities related to pet medical records.
 */
@Path("/pets")
public class MedicalProvider {
	
	/**
	 * @api {POST} /pets/{id}/medicals Add medical record
	 * @apiName AddMedicalRecord
	 * @apiGroup MedicalProvider
	 * 
	 * @apiParam {Number} pet_id pet's unique ID number
	 * @apiParam {String} medical Input text of medical record
	 * @apiParam {Date} medicalAssignedDate Date the medical record was given
	 * 
	 * @apiParamExample {json} Request-Example:
		{
		    "medical": "covid vaccine",
		    "medicalAssignedDate": "2011-03-01 01:11:23"
		}
		
	 * @apiSuccess {Number} id  Vaccination's unique ID
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
	 *		{
	 *	    	"id": 1
	 *		}
	 */
	@Path("/{id}/medicals")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AddMedicalRecord(@PathParam("pet_id") int petId, PetMedical medical) {
		        
        try {
        	
        	// Obtain the session
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();
			
			// Create new entity
			medical.setCreateTime(new Date());
			medical.setPetId(petId);
			
			// Persist the entities
			session.save(medical);
			t.commit();
			
			int id = medical.getId();
			System.out.println(medical);
			
			session.close();
        	
        	// Return successful response if no error
			return Response.status(Response.Status.OK)
					.entity(id)
					.build();
						
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}

	/**
	 * @api {GET} /pets/{id}/medicals View all medical records
	 * @apiName ViewMedicalRecords
	 * @apiGroup MedicalProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * @apiHeader {Number} currentPage current page.
	 * @apiHeader {Number} pageSize page size.
	 * @apiHeaderExample {json} Header-Example:
	 *     {
	 *       "currentPage": 1,
	 *       "pageSize": 10
	 *     }
	 *     
	 * @apiSuccess {Number} id Medical record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {String} medical Input text of medical record
	 * @apiSuccess {Date} medicalAssignedDate Date the medical record was given
	 * @apiSuccess {Date} creationTime  Record create time.
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     [
			    {
			        "id": 1,
			        "petId": 1,
			        "medical": "Allergies: None, Diet Restriction: None",
			        "medicalAssignDate": "Jan 1, 2021, 6:30:00 PM",
			        "createTime": "Dec 9, 2021, 6:26:36 AM"
			    },
			    {
			        "id": 2,
			        "petId": 1,
			        "medical": "Allergies with peanut and sweet potato",
			        "medicalAssignDate": "Feb 1, 2021, 1:30:00 PM",
			        "createTime": "Dec 9, 2021, 6:26:36 AM"
			    },
	 *     ]
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/{id}/medicals")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewMedicalRecords(@PathParam("id") int petId,
								 @HeaderParam("currentPage") int currentPage,
								 @HeaderParam("pageSize") int pageSize) {
		
		Session session = HibernateUtils.getSession();
		Query query = session.createQuery("from PetMedical where pet_id= :pet_id");
		int startNum = (currentPage - 1) * pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);
		
		List<PetMedical> medicalList = query.setParameter("pet_id", petId).list();
        session.close();
        
        // Return error if no record is found
        if (medicalList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        
        Gson g = new Gson();
        String responseData = g.toJson(medicalList);
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	
	/**
	 * @api {GET} /pets/{id}/medicals/latest View latest medical records
	 * @apiName getMedicals
	 * @apiGroup MedicalProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * 
	 * @apiSuccess {Number} id Medical record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {String} medical Input text of medical record
	 * @apiSuccess {Date} medicalAssignedDate Date the medical record was given
	 * @apiSuccess {Date} creationTime  Record create time.
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
		    {
		        "id": 1,
		        "petId": 1,
		        "medical": "Allergies: None, Diet Restriction: None",
		        "medicalAssignDate": "Jan 1, 2021, 6:30:00 PM",
		        "createTime": "Dec 9, 2021, 6:26:36 AM"
		    }
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/{id}/medicals/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewLatestMedicalRecord(@PathParam("id") int petId) {
		
		Session session = HibernateUtils.getSession();
		Query query = session.createSQLQuery("select * from PetMedical where pet_id = :petId ORDER BY id desc LIMIT 1");
		List<PetMedical> medicalList = query.setParameter("petId", petId).list();
        session.close();
        
        // Return error if no record is found
        if (medicalList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        
        Gson g = new Gson();
        String responseData = g.toJson(medicalList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	
}
