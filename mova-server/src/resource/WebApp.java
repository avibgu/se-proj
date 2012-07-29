package resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import type.ItemType;
import utilities.Location;

import actor.Activity;
import actor.Item;

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

		// Activity activity = mj.jsonToActivity(params);
		// db.insertActivityType(activity.getType());
		// db.insertActivity(activity);
		//
		return mainPage();
	}

	@GET
	@Path("/RemoveActivity")
	public String removeActivityGET(String params) {

		Vector<String> options = new Vector<String>();

		List<Activity> activities = db.getAllActivities();

		for (Activity activity : activities)
			options.add("<option value=\"" + activity.getId() + "\">"
					+ "Activity Name: " + activity.getName() + ", Type: "
					+ activity.getType() + ", State: " + activity.getState()
					+ "</option>");

		return removeItemOrActivityHTML("Activity", options, "10");
	}

	@POST
	@Path("/RemoveActivity")
	public String removeActivityPOST(String params) {
		db.deleteActivity(params.split("=")[1]);
		return mainPage();
	}

	@GET
	@Path("/AddActivityType")
	public String AddActivityTypeGET(String params) {
		return addTypeHTML("Activity");
	}

	@POST
	@Path("/AddActivityType")
	public String AddActivityTypePOST(String params) {
		db.insertActivityType(params.split("=")[1]);
		return mainPage();
	}

	@GET
	@Path("/RemoveActivityType")
	public String RemoveActivityTypeGET(String params) {
		return removeTypeHTML(db.getActivityTypes(), "Activity");
	}

	@POST
	@Path("/RemoveActivityType")
	public String RemoveActivityTypePOST(String params) {
		db.deleteActivityType(params.split("=")[1]);
		return mainPage();
	}

	// Agent Functions

	@GET
	@Path("/AddAgentType")
	public String AddAgentTypeGET(String params) {
		return addTypeHTML("Agent");
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
		return removeTypeHTML(db.getAgentTypes(), "Agent");
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

		StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"well\" method=\"post\" action=\"AddItem\">\n");
		sb.append("<label class=\"control-label\" for=\"selectItemType\">Add Item from the following Type</label>\n");

		sb.append("<div class=\"controls\">");

		sb.append("<select id=\"selectItemType\" name=\"selectItemType\">");

		for (String type : db.getItemTypes())
			sb.append("<option value=\"" + type + "\">" + type + "</option>\n");

		sb.append("</select>\n");
		sb.append("</div>\n");
		sb.append("<label class=\"control-label\">Set the Location</label>\n");
		sb.append("<input name=\"longitude\" type=\"text\" class=\"span2\" placeholder=\"Longitude\">\n");
		sb.append("<input name=\"latitude\" type=\"text\" class=\"span2\" placeholder=\"Latitude\">\n");
		sb.append("<button type=\"submit\" class=\"btn\">Add</button>\n");
		sb.append("</form>\n");

		return sb.toString();
	}

	@POST
	@Path("/AddItem")
	public String AddItemPOST(String params) {

		System.out.println(params);

		String[] splitted = params.split("&");

		Item item = new Item(new ItemType(splitted[0].split("=")[1]));

		item.setLocation(new Location(
				Integer.parseInt(splitted[1].split("=")[1]), Integer
						.parseInt(splitted[2].split("=")[1])));

		db.insertItem(item);

		return mainPage();
	}

	@GET
	@Path("/RemoveItem")
	public String RemoveItemGET(String params) {

		Vector<String> options = new Vector<String>();

		Vector<Item> items = db.getItems();

		for (Item item : items)
			options.add("<option value=\"" + item.getId() + "\">" +

			item.getType() + ", State: " + item.getState() + ", Location: "
					+ item.getLocation().getLongitude() + ","
					+ item.getLocation().getLatitude() + "</option>");

		return removeItemOrActivityHTML("Item", options, "8");
	}

	@POST
	@Path("/RemoveItem")
	public String RemoveItemPOST(String params) {
		db.deleteItem(params.split("=")[1]);
		return mainPage();
	}

	@GET
	@Path("/AddItemType")
	public String AddItemTypeGET(String params) {
		return addTypeHTML("Item");
	}

	@POST
	@Path("/AddItemType")
	public String AddItemTypePOST(String params) {
		db.insertItemType(params.split("=")[1]);
		return mainPage();
	}

	@GET
	@Path("/RemoveItemType")
	public String RemoveItemTypeGET(String params) {
		return removeTypeHTML(db.getItemTypes(), "Item");
	}

	@POST
	@Path("/RemoveItemType")
	public String RemoveItemTypePOST(String params) {
		db.deleteItemType(params.split("=")[1]);
		return mainPage();
	}

	// html templates

	protected String addTypeHTML(String pWhichType) {

		StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"well\" method=\"post\" action=\"Add"
				+ pWhichType + "Type\">\n");
		sb.append("<label>Add " + pWhichType + " Type</label>\n");
		sb.append("<input name=\"type\" type=\"text\" class=\"span3\" placeholder=\"New "
				+ pWhichType + " Type\">\n");
		sb.append("<button type=\"submit\" class=\"btn\">Add</button>\n");
		sb.append("</form>\n");

		return sb.toString();
	}

	protected String removeTypeHTML(Vector<String> types, String pWhichType) {

		StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"well\" method=\"post\" action=\"Remove"
				+ pWhichType + "Type\">\n");
		sb.append("<label class=\"control-label\" for=\"select" + pWhichType
				+ "Type\">Remove " + pWhichType + " Type</label>\n");

		sb.append("<div class=\"controls\">");

		sb.append("<select id=\"select" + pWhichType + "Type\" name=\"select"
				+ pWhichType + "Type\">");

		for (String type : types)
			sb.append("<option value=\"" + type + "\">" + type + "</option>\n");

		sb.append("</select>\n");
		sb.append("</div>\n");
		sb.append("<button type=\"submit\" class=\"btn\">Remove</button>\n");
		sb.append("</form>\n");

		return sb.toString();
	}

	protected String removeItemOrActivityHTML(String pWhichType,
			Vector<String> options, String span) {

		StringBuilder sb = new StringBuilder();

		sb.append("<form class=\"well\" method=\"post\" action=\"Remove"
				+ pWhichType + "\">\n");
		sb.append("<label class=\"control-label\" for=\"select" + pWhichType
				+ "\">Remove " + pWhichType + "</label>\n");

		sb.append("<div class=\"controls\">");

		sb.append("<select class=\"span" + span + "\" id=\"select" + pWhichType
				+ "\" name=\"select" + pWhichType + "\">");

		for (String option : options)
			sb.append(option + "\n");

		sb.append("</select>\n");
		sb.append("</div>\n");
		sb.append("<button type=\"submit\" class=\"btn\">Remove</button>\n");
		sb.append("</form>\n");

		return sb.toString();
	}
}
