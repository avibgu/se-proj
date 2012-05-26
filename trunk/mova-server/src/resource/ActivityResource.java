package resource;

import java.sql.Timestamp;
import java.util.Vector;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import simulator.Simulator;
import state.ActivityState;
import type.MessageType;
import utilities.MovaJson;
import actor.Activity;
import c2dm.C2dmController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;

@Path("/activities")
public class ActivityResource {
	
	DBHandler db = DBHandler.getInstance();
	MovaJson mj = new MovaJson();
	Simulator simulator = Simulator.getInstance(null);
	
	@PUT
	@Path("/sendActivity")
	public void sendActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		JsonObject jsonActivity = (JsonObject) jp.parse(j.get("activity").getAsString());
		Vector<String> jsonIds = null;
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,jsonIds,MessageType.SEND_ACTIVITY);
	}
	
	@POST
	@Path("/sendScheduledActivities")
	public void sendScheduledActivities(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String jsonActivities =  j.get("activities").getAsString();
		Vector<String> jsonIds = null;
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonActivities,jsonIds,MessageType.SEND_SCHEDULE);
	}
	
	@PUT
	@Path("/changeActivityStatus")
	public void changeActivityStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("activityId").getAsString(); 
		ActivityState state = ActivityState.valueOf(j.get("state").getAsString());
		db.updateActivityState(id, state.toString());
	}
	
	@PUT
	@Path("/addActivity")
	public void addActivity(String jsonObject){
		Activity activity = mj.jsonToActivity(jsonObject);
		db.insertActivity(activity);
		
		// Recalculate?
	}
	
	@PUT
	@Path("/postponeActivity")
	public void postponeActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String activityId = j.get("activityId").getAsString(); 
		String newFinishTime = j.get("newFinishTime").getAsString();
		db.updateActivityDeadline(activityId, Timestamp.valueOf(newFinishTime));
		
		// Recalculate.
		
	}
	
	@PUT
	@Path("/createActivityType")
	public void createActivityType(String jsonObject){
		db.insertActivityType(jsonObject);
	}
	
	@PUT
	@Path("/deleteActivityType")
	public void deleteActivityType(String jsonObject){
		db.deleteActivityType(jsonObject);
	}
}