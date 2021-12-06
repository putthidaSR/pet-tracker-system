package tcss559.controllers;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import tcss559.model.User;


@Path("/pets")
public class LocationProvider {

	/**
	 * @api {GET} /pets/:id/locations?currentPage=:currentPage&pageSize=:pageSize Request locations information
	 * @apiName getLocations
	 * @apiGroup LocationProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 * @apiParam {Number} currentPage current page.
	 * @apiParam {Number} pageSize page size.
	 * 
	 * @apiSuccess {Number} id Location record unique ID.
	 * @apiSuccess {String} rfidNumber Related rfidNumber unique ID.
	 * @apiSuccess {Number} longitude  Pet longitude.
	 * @apiSuccess {Number} latitude Pet latitude.
	 * @apiSuccess {Number} createTime  Location create time.
	 * @apiSuccess {String} active  Location data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     [
	 *     	{
	 *         	"id": 0,
	 *         	"rfidNumber": "1020391293",
	 *         	"longitude": 12.923432,
	 *         	"latitude": 5.66666,
	 *         	"createTime": 1293811200000,
	 *         	"active": "Y"
	 *     	}
	 *     	{
	 *         "id": 1,
	 *         "rfidNumber": "1020391293",
	 *         "longitude": 13.923432,
	 *         "latitude": 4.66666,
	 *         "createTime": 1293811200001,
	 *         "active": "Y"
	 *     	}
	 *     ]
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@Path("/{id}/locations")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocations(@PathParam("id") int id,
								 @QueryParam("currentPage") int currentPage,
								 @QueryParam("pageSize") int pageSize) {
		System.out.println(currentPage);
		System.out.println(pageSize);
		Session session = HibernateUtils.getSession();
		Query petQuery = session.createQuery("from Pet where id= :id and active = 'Y'");
		List<Pet> petList = petQuery.setParameter("id", id).list();
		if (petList.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("").build();
		}
		String rfidNumber = petList.get(0).getRfidNumber();
		System.out.println("The rfidNumber is " + rfidNumber);
		Query query = session.createQuery("from PetLocation where rfidNumber= :rfidNumber and active = 'Y'");
		int startNum = (currentPage - 1) * pageSize;
		query.setFirstResult(startNum);
		query.setMaxResults(pageSize);
		List<PetLocation> locationList = query.setParameter("rfidNumber", rfidNumber).list();
        session.close();
        if (locationList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(locationList);
        return Response.status(Response.Status.OK).entity(responseData).build();
	}
	
	
	/**
	 * @api {GET} /pets/:id Request latest location information
	 * @apiName getLatestLocation
	 * @apiGroup LocationProvider
	 *
	 * @apiParam {Number} id Pets unique ID.
	 *
	 * @apiSuccess {Number} id Location record unique ID.
	 * @apiSuccess {String} rfidNumber Related rfidNumber unique ID.
	 * @apiSuccess {Number} longitude  Pet longitude.
	 * @apiSuccess {Number} latitude Pet latitude.
	 * @apiSuccess {Number} createTime  Location create time.
	 * @apiSuccess {String} active  Location data status.
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     {
	 *         "id": 0,
	 *         "rfidNumber": "1020391293",
	 *         "longitude": 12.923432,
	 *         "latitude": 5.66666,
	 *         "createTime": 1293811200000,
	 *         "active": "Y"
	 *     }
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@Path("/{id}/locations/latest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLatestLocation(@PathParam("id") int id) {
		Session session = HibernateUtils.getSession();
		Query petQuery = session.createQuery("from Pet where id= :id and active = 'Y'");
		List<Pet> petList = petQuery.setParameter("id", id).list();
		if (petList.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("").build();
		}
		String rfidNumber = petList.get(0).getRfidNumber();
		Query query = session.createSQLQuery("select * from pet_location where rfid_number = :rfidNumber and active = 'Y' ORDER BY id desc LIMIT 1");
		List<PetLocation> locationList = query.setParameter("rfidNumber", rfidNumber).list();
        session.close();
        if (locationList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        Gson g = new Gson();
        String responseData = g.toJson(locationList.get(0));
        return Response.status(Response.Status.OK).entity(responseData).build();
	}

	
}
