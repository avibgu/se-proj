package webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/webapp")
public class WebApp {
	
	@GET
	@Path("/test")
	public String testGet(String jsonObject){
		return
				"<html>\n" +
				"\t<head>\n" +
				"\t</head>\n" +
				"\t<body>\n" +
				"\t\t<h1>Hello World</h1>\n" +
				"\t</body>\n" +
				"</html>\n";
	}
	
	@GET
	@Path("/main")
	public String mainGet(String jsonObject){
		return
				"<html>\n" +
				"\t<head>\n" +
				"\t</head>\n" +
				"\t<body>\n" +
				"\t\t<h1>Hello World !</h1>\n" +
				"\t</body>\n" +
				"</html>\n";
	}
}
