package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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
	
	@PUT
	@Path("/sendActivity")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void sendActivity(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		JsonObject jsonActivity = (JsonObject) jp.parse(j.get("activity").getAsString());
		JsonArray jsonIds = (JsonArray) jp.parse(j.get("agentIds").getAsString());
		
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,jsonIds);
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
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		JsonObject jsonActivity = (JsonObject) jp.parse(j.get("activity").getAsString());
		
		MovaJson mj = new MovaJson();
		Activity activity = mj.jsonToActivity(jsonActivity.toString());
		DBHandler db = DBHandler.getInstance();
		String startTime = activity.getStartTime().toString();
		String endTime = activity.getEndTime().toString();
		db.insertActivity(activity.getId(), activity.getName(), activity.getDescription(), 
				activity.getType(), startTime, endTime, (int)activity.getEstimateTime());
	}
}
