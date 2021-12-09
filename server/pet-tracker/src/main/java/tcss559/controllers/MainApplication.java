package tcss559.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import tcss559.utilities.CORSFilter;

/**
 * This class is a central service API endpoint which hosts the default base URI.
 * 
 * @author Jiawei Yao & Putthida Samrith
 */
@ApplicationPath("/PawTracker")
public class MainApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {

		HashSet<Class<?>> resources = new HashSet<>();

		// Enable CORS in every request at run-time
		resources.add(CORSFilter.class);
		
		// Add classes that to be supported by application	
		resources.add(PetRegistration.class);
		resources.add(UserRegistration.class);		
		resources.add(AuthenticationProvider.class);
		resources.add(LocationProvider.class);
		resources.add(NotificationProvider.class);
		resources.add(SearchCenter.class);
		resources.add(MedicalProvider.class);
		resources.add(VaccinationProvider.class);
		
		return resources;
	}

}
