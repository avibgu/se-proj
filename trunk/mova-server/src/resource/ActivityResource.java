package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import state.ActivityState;
import utilities.MovaJson;
import actor.Activity;
import c2dm.C2dmController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;

@Path("/activities")
public class ActivityResource {
	
	DBHandler db = DBHandler.getInstance();
	MovaJson mj = new MovaJson();
	
	@PUT
	@Path("/sendActivity")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void sendActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		JsonObject jsonActivity = (JsonObject) jp.parse(j.get("activity").getAsString());
		JsonArray jsonIds = (JsonArray) jp.parse(j.get("agentIds").getAsString());
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,jsonIds,"SEND_ACTIVITY");
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
	
	@PUT
	@Path("/addActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addActivity(String jsonObject){
		Activity activity = mj.jsonToActivity(jsonObject);
		System.out.println("ASASAS");
//		db.insertActivity(activity.getId(), activity.getName(), activity.getDescription(), 
//				activity.getType(), activity.getStartTime().toString(), activity.getEndTime().toString(), (int)activity.getEstimateTime());
		
		// Recalculate?
	}
	
	@POST
	@Path("/postponeActivity/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postponeActivity(@QueryParam("activityId") String activityId,
								 @QueryParam("addedTime") String addedTime){
		
		// Change in db.
		
		// Recalculate.
		
	}
}
