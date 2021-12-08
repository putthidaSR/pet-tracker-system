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
import tcss559.model.PetVaccination;

/**
 * Root resource (exposed at "pets" path).
 * This resource class contains functionalities related to pet vaccination records.
 */
@Path("/pets")
public class VaccinationProvider {
	
	/**
	 * @api {POST} /pets/{id}/vaccinations Add vaccination record
	 * @apiName AddVaccinationRecord
	 * @apiGroup VaccinationProvider
	 * 
	 * @apiParam {Number} pet_id pet's unique ID number
	 * @apiParam {String} vaccinationName Name of the vaccination
	 * @apiParam {Date} immunizationDate Date of the vaccination
	 * @apiParam {String} veterinarianName Name of the veterinarian
	 * @apiParam {String} veterinarianContact Contact information of the veterinarian
	 * 
	 * @apiParamExample {json} Request-Example:
		{
		    "vaccinationName": "covid vaccine",
		    "immunizationDate": "2011-03-01 01:11:23",
		    "veterinarianName": "Clint Barton",
		    "veterinarianContact": "206534566"
		}
		
	 * @apiSuccess {Number} id  Vaccination's unique ID
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
	 *		{
	 *	    	"id": 1
	 *		}
	 */
	@Path("/{id}/vaccinations")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response AddVaccinationRecord(@PathParam("pet_id") int petId, PetVaccination vaccination) {
		        
        try {
        	
        	// Obtain the session
			Session session = HibernateUtils.getSession();
			Transaction t = session.beginTransaction();
			
			// Create new entity
			vaccination.setCreationTime(new Date());
			vaccination.setPetId(petId);
			
			// Persist the entities
			session.save(vaccination);
			t.commit();
			
			int vaccinationId = vaccination.getId();
			System.out.println(vaccinationId);
			
			session.close();
        	
        	// Return successful response if no error
			return Response.status(Response.Status.OK)
					.entity(vaccinationId)
					.build();
						
		} catch (Exception e) {
			// Return expected error response
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("Error Message: " + e.getLocalizedMessage()).build();
		}
	}
	
	/**
	 * @api {GET} /pets/{id}/vaccinations View all vaccinations records
	 * @apiName ViewAllVaccinationRecords
	 * @apiGroup VaccinationProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * @apiHeader {Number} currentPage current page.
	 * @apiHeader {Number} pageSize page size.
	 * @apiHeaderExample {json} Header-Example:
	 *     {
	 *       "currentPage": 1,
	 *       "pageSize": 10
	 *     }
	 * @apiSuccess {Number} id Medical record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {Number} vaccination  Vaccination record.
	 * @apiSuccess {Number} createTime  Record create time.
	 * @apiSuccess {String} active  Record data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     [
	 *     	{
	 *         	"id": 0,
	 *         	"petId": "1020391293",
	 *         	"vaccination": "test",
	 *         	"createTime": 1293811200000,
	 *         	"active": "Y"
	 *     	}
	 *     	{
	 *         "id": 1,
	 *         "petId": "1020391293",
	 *         "vaccination": "test",
	 *         "createTime": 1293811200001,
	 *         "active": "Y"
	 *     	}
	 *     ]
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/{id}/vaccinations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewAllVaccinationRecords(@PathParam("id") int petId,
								 @HeaderParam("currentPage") int currentPage,
								 @HeaderParam("pageSize") int pageSize) {
		
		Session session = HibernateUtils.getSession();
		Query query = session.createQuery("from pet_vaccination where pet_id= :petId");
		int startNum = (currentPage - 1) * pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);
		
		List<PetVaccination> vaccinationList = query.setParameter("petId", petId).list();
        session.close();
        if (vaccinationList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(vaccinationList);
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	
	/**
	 * @api {GET} /pets/{id}/vaccinations/latest View latest vaccination record
	 * @apiName ViewLatestVaccinationRecord
	 * @apiGroup MedicalProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * 
	 * @apiSuccess {Number} id Vaccination record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {Number} vaccination  Vaccination record.
	 * @apiSuccess {Number} createTime  Record create time.
	 * @apiSuccess {String} active  Record data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     	{
	 *         "id": 1,
	 *         "petId": "1020391293",
	 *         "vaccination": "test",
	 *         "createTime": 1293811200001,
	 *         "active": "Y"
	 *     	}
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/{id}/vaccinations/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewLatestVaccinationRecord(@PathParam("id") int petId) {
		Session session = HibernateUtils.getSession();
		Query query = session.createSQLQuery("select * from pet_vaccination where pet_id = :petId ORDER BY id desc LIMIT 1");
		List<PetVaccination> vaccinationList = query.setParameter("petId", petId).list();
        session.close();
        if (vaccinationList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(vaccinationList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
}