package resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import db.DBHandler;

@Path("/webapp")
public class WebApp {

	DBHandler db = DBHandler.getInstance();
	
	@GET
	@Path("/main")
	public String mainGET(String params) {
		return mainPage();
	}

	private String mainPage() {
		return page(WebApp.class.getResourceAsStream("index.html"));
	}

	private String page(InputStream pInputStream) {

		StringBuilder stringBuilder = new StringBuilder();
		
		BufferedReader br = null;

		try {

			br = new BufferedReader(new InputStreamReader(pInputStream));

			while (br.ready())
				stringBuilder.append(br.readLine() + "\n");

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
	
	// Activity Functions
	
	@GET
	@Path("/AddActivity")
	public String addActivityGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@POST
	@Path("/AddActivity")
	public String addActivityPOST(String params) {
		// TODO Auto-generated method stub
		
//		Activity activity = mj.jsonToActivity(params);
//		db.insertActivityType(activity.getType());
//		db.insertActivity(activity);
//		
		return mainPage();
	}
	
	@GET
	@Path("/RemoveActivity")
	public String removeActivityGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/AddActivityType")
	public String AddActivityTypeGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveActivityType")
	public String RemoveActivityTypeGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Agent Functions
	
	@GET
	@Path("/AddAgentType")
	public String AddAgentTypeGET(String params) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("<form class=\"well\" method=\"post\" action=\"AddAgentType\">\n");
		sb.append("<label>Add Agent Type</label>\n");
		sb.append("<input name=\"type\" type=\"text\" class=\"span3\" placeholder=\"New Agent Type\">\n");
		sb.append("<button type=\"submit\" class=\"btn\">Add</button>\n");
		sb.append("</form>\n");

		return sb.toString();
	}
	
	@POST
	@Path("/AddAgentType")
	public String AddAgentTypePOST(String params) {
		db.insertAgentType(params.split("=")[1]);
		return mainPage();
	}
	
	@GET
	@Path("/RemoveAgentType")
	public String RemoveAgentTypeGET(String params) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("<form class=\"well\" method=\"post\" action=\"RemoveAgentType\">\n");
		sb.append("<label class=\"control-label\" for=\"selectAgentType\">Remove Agent Type</label>\n");
		
		sb.append("<div class=\"controls\">");
		
		sb.append("<select id=\"selectAgentType\" name=\"selectAgentType\">");
		 
		for (String type : db.getAgentTypes())
			sb.append("<option value=\"" + type + "\">" + type + "</option>\n");

        sb.append("</select>\n");
        sb.append("</div>\n");
		sb.append("<button type=\"submit\" class=\"btn\">Remove</button>\n");
		sb.append("</form>\n");

		return sb.toString();
	}
	
	@POST
	@Path("/RemoveAgentType")
	public String RemoveAgentTypePOST(String params) {
		db.deleteAgentType(params.split("=")[1]);
		return mainPage();
	}
	
	// Item Functions
	
	@GET
	@Path("/AddItem")
	public String AddItemGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveItem")
	public String RemoveItemGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/AddItemType")
	public String AddItemTypeGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@GET
	@Path("/RemoveItemType")
	public String RemoveItemTypeGET(String params) {
		// TODO Auto-generated method stub
		return null;
	}
}
