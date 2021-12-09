package tcss559.utilities;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Helper class to enable CORS in JAX-RS by creating a filter to inject necessary response header at run-time in every request. 
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {
	
	// Origin URL of the client side
	private static final String CLIENT_ORIGIN = "http://localhost:3000";

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		
		response.getHeaders().add("Access-Control-Allow-Origin", CLIENT_ORIGIN);
		response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.getHeaders().add("Access-Control-Allow-Credentials", "true");
		response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	}

}