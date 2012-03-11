package activity;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import state.ActivityState;
import state.ItemState;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/activities")
public class ActivityResource {
	
	@PUT
	@Path("/sendActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("activity").getAsString(); 
		ItemState state = ItemState.valueOf(j.get("state").getAsString());
		//add c2dm code here
	}
	
	@PUT
	@Path("/changeActivityStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changeActivityStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("activityId").getAsString(); 
		ActivityState state = ActivityState.valueOf(j.get("state").getAsString());
		//add c2dm code here
	}
}
