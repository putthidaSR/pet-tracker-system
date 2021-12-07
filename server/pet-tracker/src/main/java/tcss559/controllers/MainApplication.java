package tcss559.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This class is a central service API endpoint which hosts the default base URI.
 */
@ApplicationPath("/PawTracker")
public class MainApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {

		HashSet<Class<?>> resources = new HashSet<>();

		// Add classes that to be supported by application	
		resources.add(PetRegistration.class);
		resources.add(UserRegistration.class);		
		resources.add(AuthenticationProvider.class);

		//resources.add(LocationProvider.class);
		
		return resources;
	}

}
