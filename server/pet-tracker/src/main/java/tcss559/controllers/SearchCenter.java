package tcss559.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import tcss559.hibernate.HibernateUtils;
import tcss559.model.Pet;
import tcss559.model.PetLocation;
import tcss559.model.User;
import tcss559.model.UsersXML;
import tcss559.utilities.*;

/**
 * Resource class that invokes another web services to search for specified results.
 */
@Path("")
public class SearchCenter {
	
	/**
	 * 
	 * @api {GET} /searchUserByAddress?address=:address Request user by fuzzy address
	 * @apiName searchUserByAddress
	 * @apiGroup SearchCenter
	 *
	 * @apiParam {String} address User address.
	 * @apiSuccess {Number} id Users unique ID.
	 * @apiSuccess {String} address  User address.
	 * @apiSuccess {String} contact User contact.
	 * @apiSuccess {Number} createTime  User create time.
	 * @apiSuccess {Number} modifyTime  User modify time.
	 * @apiSuccessExample {xml} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	 *     <users>
	 *     	   <user>
	 *         		<active>Y</active>
	 *         		<address>test</address>
	 *         		<contact>test</contact>
	 *         		<createTime>2021-12-05T00:01:50+08:00</createTime>
	 *         		<id>1</id>
	 *         		<loginName>test1</loginName>
	 *         		<loginPassword>1</loginPassword>
	 *         		<modifyTime>2021-12-05T21:36:57+08:00</modifyTime>
	 *         </user>
	 *     </users>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/searchUserByAddress") 
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response searchUserByLocation(@QueryParam("address") String address) throws JAXBException {
		
		Session session = HibernateUtils.getSession();
		
		// Returns the list of active users with the specified address
		Query query = session.createQuery("from User where address like :address and active = 'Y'");
		List<User> userList = query.setParameter("address", "%"+address+"%").list();
        
		session.close();
        
		// Returns error if no user record is found
        if (userList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();	
        }
        
        // Transfer user class to xml format
        JAXBContext context = JAXBContext.newInstance(UsersXML.class, User.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); 
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
        UsersXML users = new UsersXML();
        users.setUsers(userList);       
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        marshaller.marshal(users, baos);  
        
        // Return response in non-RESTful format
        String xmlObj = new String(baos.toByteArray());
        return Response.status(Response.Status.OK).entity(xmlObj).build();	
	}
	
	/**
	 * @api {GET} /searchPetWithWeather?id=:id Request current weather condition of the latest location of the specified pet using WeatherAPI.com
	 * @apiName searchPetWithWeather
	 * @apiGroup SearchCenter
	 *
	 * @apiParam {Number} id Pets unique ID
	 *
	 * @apiSuccess {String} rfidNumber Pet's unique RFID tag number
	 * @apiSuccess {Number} longitude  Longitude of the pet's location
	 * @apiSuccess {Number} latitude Latitide of the pet's location
	 * @apiSuccess {String} lastSeenDate  Date/time that the pet was seen in the specified location
	 * @apiSuccess {String} weather Weather condition of the pet's location
	 * @apiSuccess {String} iconLink HTTP link to the icon that represents the weather condition
	 * 
	 * @apiSuccessExample {json} Success-Response:
	 *     HTTP/1.1 200 OK
	 *     {
	 *         "rfidNumber": "1020391293",
	 *         "longitude": 12.923432,
	 *         "latitude": 5.66666,
	 *         "lastSeenTime": "Jan 1, 2011, 12:00:00 AM",
	 *         "weather": "Clear",
	 *         "iconLink": "http://cdn.weatherapi.com/weather/64x64/day/143.png"
	 *     }
	 * @apiError(Error 404) UserNotFound The <code>id</code> of the Pet was not found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("/searchPetWithWeather")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchPetWithWeather(@QueryParam("id") int id) {
		
		Session session = HibernateUtils.getSession();
		
		// Return the list of pets with the specified ID (expected one record returns since ID is unique)
		Query petQuery = session.createQuery("from Pet where id= :id");
		List<Pet> petList = petQuery.setParameter("id", id).list();
		
		// Return error when no pet with the specified ID was found
		if (petList.isEmpty()) {
			return Response.status(Response.Status.NOT_FOUND).entity("").build();
		}
		
		// Get RFID tag number of the specified pet
		String rfidNumber = petList.get(0).getRfidNumber();
		
		// Returns the list of locations of the specified pet ID in descending order (latest first)
		Query query = session
				.createSQLQuery("select * from pet_location where pet_id = :pet_id ORDER BY id desc")
				.addEntity(PetLocation.class);
		List<PetLocation> locationList = query.setParameter("pet_id", id).list();
		
        session.close();
        
        // Return an error if no location is found
        if (locationList.isEmpty()) {
        	return Response.status(Response.Status.NOT_FOUND).entity("").build();
        }
        
        // Set latitude and longitude value
        double latitude = locationList.get(0).getLatitude();
        double longitude = locationList.get(0).getLongitude();
        System.out.println("latitude: " + latitude + ", longitude: " + longitude);
        
        Gson g = new Gson();
        String weather = ""; // Current weather condition
        String iconLink = ""; // HTTP link of the icon that represents the returned weather condition
        
        try {
        	
        	// Invoke weather web service to get the current weather of the specified geolocation
			String data = WeatherService.getCurrentWeather(latitude, longitude);
			JsonObject obj = g.fromJson(data, JsonObject.class);
			JsonObject current = g.fromJson(obj.get("current"), JsonObject.class);
			JsonObject condition = g.fromJson(current.get("condition"), JsonObject.class);
			weather = condition.get("text").getAsString();
			iconLink = condition.get("icon").getAsString();

		} catch (IOException e) {
			// Return expected error if fail to invoke web service
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("").build();
		}
        
        // Construct response to return
        HashMap<String, Object> hMap = new HashMap<>();
        hMap.put("rfidNumber", rfidNumber);
        hMap.put("longitude", longitude);
        hMap.put("latitude", latitude);
        hMap.put("lastSeenTime", locationList.get(0).getLastSeenDate());
        hMap.put("weather", weather);
        hMap.put("iconLink", "http:" + iconLink);
        
        return Response.status(Response.Status.OK).entity(g.toJson(hMap)).build();
	}
}
