package location;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import simulator.Location;;

@Path("/location")
public class LocationResource {
	// This method is called if XMLis request
		@GET
		@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public Location getXML() {
			Location l = new Location(4, 4);
			return l;
		}
		
		// This can be used to test the integration with the browser
		@GET
		@Produces( { MediaType.TEXT_XML })
		public Location getHTML() {
			Location l = new Location(1, 1);
			return l;
		}
}
