package resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/webapp")
public class WebApp {

	@GET
	@Path("/main")
	public String mainGET(String jsonObject) {
		return page(WebApp.class.getResourceAsStream("index.html"));
	}

	private String page(InputStream pInputStream) {

		StringBuilder stringBuilder = new StringBuilder();
		
		BufferedReader br = null;

		try {

			br = new BufferedReader(new InputStreamReader(pInputStream));

			while (br.ready())
				stringBuilder.append(br.readLine());

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
	
	// Activity Functions
	
	@GET
	@Path("/AddActivity")
	public String addActivityGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveActivity")
	public String removeActivityGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/AddActivityType")
	public String AddActivityTypeGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveActivityType")
	public String RemoveActivityTypeGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Agent Functions
	
	@GET
	@Path("/AddAgentType")
	public String AddAgentTypeGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveAgentType")
	public String RemoveAgentTypeGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Item Functions
	
	@GET
	@Path("/AddItem")
	public String AddItemGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveItem")
	public String RemoveItemGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/AddItemType")
	public String AddItemTypeGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveItemType")
	public String RemoveItemTypeGET(String jsonObject) {
		// TODO Auto-generated method stub
		return null;
	}
}
