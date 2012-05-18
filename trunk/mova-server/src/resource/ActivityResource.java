package resource;

import java.sql.Timestamp;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import state.ActivityState;
import type.MessageType;
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
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		JsonObject jsonActivity = (JsonObject) jp.parse(j.get("activity").getAsString());
		String jsonIds = j.get("agentIds").getAsString();
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,jsonIds,MessageType.SEND_ACTIVITY);
	}
	
	@POST
	@Path("/sendScheduledActivities")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendScheduledActivities(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String jsonActivities =  j.get("activities").getAsString();
		String jsonId = j.get("agentId").getAsString();
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonActivities,jsonId,MessageType.SEND_SCHEDULE);
	}
	
	@PUT
	@Path("/changeActivityStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changeActivityStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("activityId").getAsString(); 
		ActivityState state = ActivityState.valueOf(j.get("state").getAsString());
		db.updateActivityState(id, state.toString());
	}
	
	@PUT
	@Path("/addActivity")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addActivity(String jsonObject){
		Activity activity = mj.jsonToActivity(jsonObject);
		db.insertActivity(activity);
		
		// Recalculate?
	}
	
	@POST
	@Path("/postponeActivity/{activityId}/{newFinishTime}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void postponeActivity(@QueryParam("activityId") String activityId,
								 @QueryParam("newFinishTime") String newFinishTime){
		
		db.updateActivityDeadline(activityId, Timestamp.valueOf(newFinishTime));
		
		// Recalculate.
		
	}
	
	@PUT
	@Path("/createActivityType")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createActivityType(String jsonObject){
		db.insertActivityType(jsonObject);
	}
	
	@PUT
	@Path("/deleteActivityType")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteActivityType(String jsonObject){
		db.deleteActivityType(jsonObject);
	}
}
