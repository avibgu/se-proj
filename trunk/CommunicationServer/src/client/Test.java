package client;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import simulator.Location;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Test {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		//System.out.println(service.path("sim").path("location").accept(
				//MediaType.APPLICATION_JSON).get(String.class));
		
		String obj = service.path("sim").path("location").accept(
				MediaType.APPLICATION_JSON).get(String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		Location l = mapper.readValue(obj, Location.class);
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8080/CommunicationServer").build();
	}
}
