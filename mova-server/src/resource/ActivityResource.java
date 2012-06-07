package resource;

import java.sql.Timestamp;
import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import simulator.Simulator;
import state.ActivityState;
import type.MessageType;
import utilities.MovaJson;
import actor.Activity;
import c2dm.C2dmController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;
/**
 * The activity resource is used to process incoming connections
 * from the Mova Client that are relevant to activites
 */
@Path("/activities")
public class ActivityResource {
	
	DBHandler db = DBHandler.getInstance();
	MovaJson mj = new MovaJson();
	Simulator simulator = Simulator.getInstance(null);
	/**
	 * Sends an activity to all the relevant agents
	 * @param jsonObject a json object that holds the agent ids of the relevant agents
	 */
	@PUT
	@Path("/sendActivity")
	public void sendActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		JsonObject jsonActivity = (JsonObject) jp.parse(j.get("activity").getAsString());
		Vector<String> jsonIds = null;
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,jsonIds,MessageType.SEND_ACTIVITY);
	}
	/**
	 * Sends a whole activity schedule to all relevant agents
	 * @param jsonObject a json object that holds the agent ids of the relevant agents
	 * and the activity to send
	 */
	@PUT
	@Path("/sendScheduledActivities")
	public void sendScheduledActivities(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String jsonActivities =  j.get("activities").getAsString();
		
		MovaJson mj = new MovaJson();
		List<Activity> activities = mj.jsonToActivities(jsonActivities);
		
		for (Activity activity : activities) {
			if(activity.getState() == ActivityState.PENDING){
				db.deleteActivity(activity.getId());
				db.insertActivity(activity);
			}
		}
		
		Vector<String> jsonIds = new Vector<String>();
		for (Activity activity : activities) {
			jsonIds.add(activity.getId());
		}
		
//		C2dmController.getInstance().sendMessageToDevice("3", jsonActivities,jsonIds,MessageType.GOT_SCHEDULE);
	}
	/**
	 * Changes an activity status
	 * @param jsonObject a json object that holds the activity id to change
	 * and the new state of the activity
	 */
	@PUT
	@Path("/changeActivityStatus")
	public void changeActivityStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("activityId").getAsString(); 
		ActivityState state = ActivityState.valueOf(j.get("state").getAsString());
		db.updateActivityState(id, state.toString());
		simulator.changeActivityStatusMessage(id, state);
	}
	/**
	 *
	 * @param jsonObject
	 */
	//TODO finish javadoc
	@PUT
	@Path("/addActivity")
	public void addActivity(String jsonObject){
		Activity activity = mj.jsonToActivity(jsonObject);
		db.insertActivityType(activity.getType());
		db.insertActivity(activity);
		
		// Recalculate?
	}
	/**
	 * 
	 * @param jsonObject
	 */
	//TODO finish javadoc
	@PUT
	@Path("/postponeActivity")
	public void postponeActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String activityId = j.get("activityId").getAsString(); 
		long addedTime = j.get("addedTime").getAsLong();
		Timestamp currentEndTime = db.getActivityDeadline(activityId);
		int currentEstimatedTime = db.getActivityDuration(activityId);
		String newFinishTime = String.valueOf(currentEndTime.getTime() + addedTime);
		db.updateActivityDeadline(activityId, new Timestamp(currentEndTime.getTime() + addedTime*60*1000));
		db.updateActivityDuration(activityId, currentEstimatedTime+addedTime*60*1000);
		simulator.postoneActivityMessage(activityId, newFinishTime);
		// Recalculate.
		
	}
	/**
	 * Creates a new Activity Type in the system
	 * @param jsonObject a json object that holds the new activity type
	 */
	@PUT
	@Path("/createActivityType")
	public void createActivityType(String jsonObject){
		db.insertActivityType(jsonObject);
	}
	/**
	 * Deletes an existing Activity Type from the system
	 * @param jsonObject a json object that holds the existing activity type
	 */
	@PUT
	@Path("/deleteActivityType")
	public void deleteActivityType(String jsonObject){
		db.deleteActivityType(jsonObject);
	}
	
	@GET
	@Path("/getAgentSchedule")
	public String getAgentSchedule(@QueryParam("agentId") String agentId){
		
		Vector<Activity> schedule = db.getAgentSchedule(agentId);
		
		JsonObject j = new JsonObject();
		j.addProperty("activities", new MovaJson().createJsonObj(schedule));
		
		return j.toString();
	}
	
	@GET
	@Path("/getAllActivities")
	public void getAllActivities(@QueryParam("agentId") String agentId){
		List<Activity> activities = db.getAllActivities();
		Vector<String> agentIds = new Vector<String>();
		agentIds.add(agentId);
		C2dmController.getInstance().sendMessageToDevice("3", new MovaJson().createJsonObj(activities),agentIds, MessageType.GOT_ACTIVITIES);
	}
	
}
