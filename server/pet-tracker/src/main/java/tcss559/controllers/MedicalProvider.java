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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.google.gson.Gson;

import tcss559.hibernate.HibernateUtils;
import tcss559.model.Pet;
import tcss559.model.PetLocation;
import tcss559.model.PetMedical;
import tcss559.model.PetVaccination;
import tcss559.model.User;


@Path("/medical")
public class MedicalProvider {

	/**
	 * @api {GET} /pets/:id/medicals Request medicals information
	 * @apiName getMedicals
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
	 * @apiSuccess {Number} medical  Medical record.
	 * @apiSuccess {Number} createTime  Record create time.
	 * @apiSuccess {String} active  Record data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     [
	 *     	{
	 *         	"id": 0,
	 *         	"petId": "1020391293",
	 *         	"medical": "cold",
	 *         	"createTime": 1293811200000,
	 *         	"active": "Y"
	 *     	}
	 *     	{
	 *         "id": 1,
	 *         "petId": "1020391293",
	 *         "medical": "cold",
	 *         "createTime": 1293811200001,
	 *         "active": "Y"
	 *     	}
	 *     ]
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@Path("/{id}/medicals")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocations(@PathParam("id") int petId,
								 @HeaderParam("currentPage") int currentPage,
								 @HeaderParam("pageSize") int pageSize) {
		Session session = HibernateUtils.getSession();
		Query query = session.createQuery("from PetMedical where petId= :petId and active = 'Y'");
		int startNum = (currentPage - 1) * pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);
		List<PetMedical> medicalList = query.setParameter("petId", petId).list();
        session.close();
        if (medicalList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(medicalList);
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	
	/**
	 * @api {GET} /pets/:id/medicals/latest Request locations information
	 * @apiName getMedicals
	 * @apiGroup MedicalProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * 
	 * @apiSuccess {Number} id Medical record unique ID.
	 * @apiSuccess {Number} petId Related pet unique ID.
	 * @apiSuccess {Number} medical  Medical record.
	 * @apiSuccess {Number} createTime  Record create time.
	 * @apiSuccess {String} active  Record data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     	{
	 *         "id": 1,
	 *         "petId": "1020391293",
	 *         "medical": "cold",
	 *         "createTime": 1293811200001,
	 *         "active": "Y"
	 *     	}
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@Path("/{id}/medicals/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestLocation(@PathParam("id") int petId) {
		Session session = HibernateUtils.getSession();
		Query query = session.createSQLQuery("select * from pet_medical where pet_id = :petId and active = 'Y' ORDER BY id desc LIMIT 1");
		List<PetMedical> medicalList = query.setParameter("petId", petId).list();
        session.close();
        if (medicalList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(medicalList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	/**
	 * @api {GET} /pets/:id/vaccinations Request vaccinations information
	 * @apiName getVaccinations
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
	@Path("/{id}/vaccinations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVaccinations(@PathParam("id") int petId,
								 @HeaderParam("currentPage") int currentPage,
								 @HeaderParam("pageSize") int pageSize) {
		Session session = HibernateUtils.getSession();
		Query query = session.createQuery("from PetVaccination where petId= :petId and active = 'Y'");
		int startNum = (currentPage - 1) * pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);
		List<PetVaccination> medicalList = query.setParameter("petId", petId).list();
        session.close();
        if (medicalList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(medicalList);
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	
	/**
	 * @api {GET} /pets/:id/vaccinations/latest Request latest vaccination information
	 * @apiName getMedicals
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
	@Path("/{id}/vaccinations/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestVaccination(@PathParam("id") int petId) {
		Session session = HibernateUtils.getSession();
		Query query = session.createSQLQuery("select * from pet_vaccination where pet_id = :petId and active = 'Y' ORDER BY id desc LIMIT 1");
		List<PetVaccination> medicalList = query.setParameter("petId", petId).list();
        session.close();
        if (medicalList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(medicalList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();
	}

	
}
