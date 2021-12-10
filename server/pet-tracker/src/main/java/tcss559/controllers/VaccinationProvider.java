package tcss559.controllers;

import java.util.Date;
import java.util.List;

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

import tcss559.hibernate.HibernateUtils;
import tcss559.model.PetVaccination;
import tcss559.request.VaccinationUpdateDTO;

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
	 *     
	 * @apiSuccess {Number} id Medical record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {Date} creationTime  Record create time.
	 * @apiSuccess {String} vaccinationName Name of the vaccination
	 * @apiSuccess {Date} immunizationDate Date of the vaccination
	 * @apiSuccess {String} veterinarianName Name of the veterinarian
	 * @apiSuccess {String} veterinarianContact Contact information of the veterinarian
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     [
			    {
			        "id": 1,
			        "petId": 1,
			        "creationTime": "Dec 9, 2021, 6:26:36 AM",
			        "vaccinationName": "Canine Parvovirus",
			        "immunizationDate": "Jan 1, 2021, 12:30:00 PM",
			        "veterinarianName": "Lakewood Animal Shelter",
			        "veterinarianContact": "(206) 591-2543"
			    },
			    {
			        "id": 2,
			        "petId": 1,
			        "creationTime": "Dec 9, 2021, 6:26:36 AM",
			        "vaccinationName": "Canine Distemper",
			        "immunizationDate": "Dec 1, 2021, 3:30:00 PM",
			        "veterinarianName": "Lakewood Animal Shelter",
			        "veterinarianContact": "(206) 591-2543"
			    },
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
		Query query = session.createQuery("from PetVaccination where pet_id= :pet_id");
		int startNum = (currentPage - 1) * pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);
		
		List<PetVaccination> vaccinationList = query.setParameter("pet_id", petId).list();
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
	 * @apiSuccess {Number} id Medical record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {Date} creationTime  Record create time.
	 * @apiSuccess {String} vaccinationName Name of the vaccination
	 * @apiSuccess {Date} immunizationDate Date of the vaccination
	 * @apiSuccess {String} veterinarianName Name of the veterinarian
	 * @apiSuccess {String} veterinarianContact Contact information of the veterinarian
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *      HTTP/1.1 200 OK
		    {
		        "id": 1,
		        "petId": 1,
		        "creationTime": "Dec 9, 2021, 6:26:36 AM",
		        "vaccinationName": "Canine Parvovirus",
		        "immunizationDate": "Jan 1, 2021, 12:30:00 PM",
		        "veterinarianName": "Lakewood Animal Shelter",
		        "veterinarianContact": "(206) 591-2543"
		    }
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/{id}/vaccinations/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response ViewLatestVaccinationRecord(@PathParam("id") int petId) {
		Session session = HibernateUtils.getSession();
		Query query = session.createSQLQuery("select * from PetVaccination where pet_id = :petId ORDER BY id desc LIMIT 1");
		List<PetVaccination> vaccinationList = query.setParameter("petId", petId).list();
        session.close();
        if (vaccinationList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(vaccinationList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	/**
	 * @api {DELETE} /vaccinations/{id} Delete a vaccination record
	 * @apiName DeleteVaccinationRecord
	 * @apiGroup VaccinationProvider
	 *
	 * @apiParam {Number} id ID of the record to be deleted
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@SuppressWarnings({ "rawtypes" })
	@Path("/vaccinations/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteVaccinationRecord(@PathParam("id") int id) {
		
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		
		// disable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();

		// delete a record with the primary key
		Query query = session.createSQLQuery("DELETE FROM pet_vaccination where id = :id");
		query.setParameter("id", id);
		query.executeUpdate();

		// enable foreign key constraints check
		session.createSQLQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
		
		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
	}
	
	
	/**
	 * @api {PUT} /vaccinations/{id} Update a vaccination record
	 * @apiName UpdateVaccinationRecord
	 * @apiGroup VaccinationProvider
	 *
	 * @apiParam {Number} id ID of the record to be updated
	 * @apiParam {String} vaccinationNname pet's vaccination name
	 * @apiParamExample {json} Request Body - Example:
	 *		{
	 *			"vaccinationNname": "Rabies vaccine"
	 *     	}
	 *     
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 */
	@SuppressWarnings({ "rawtypes" })
	@Path("/vaccinations/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateVaccinationRecord(@PathParam("id") int id, VaccinationUpdateDTO vaccinationUpdateDTO) {
		
		Session session = HibernateUtils.getSession();
		Transaction t = session.beginTransaction();
		
		// update a record with the primary key
		Query query = session.createSQLQuery("UPDATE pet_vaccination SET vaccination_name = :vaccinationNname  where id = :id");
		query.setParameter("vaccinationNname", vaccinationUpdateDTO.getVaccinationName());
		query.setParameter("id", id);
		query.executeUpdate();		
		t.commit();
        session.close();
        return Response.status(Response.Status.OK).entity("").build();	
	}
	
}
