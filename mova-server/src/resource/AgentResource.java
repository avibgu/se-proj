package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;


import simulator.Location;
import utilities.MovaJson;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;

@Path("/agents")
public class AgentResource {
	
	@PUT
	@Path("/changeAgentLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changeAgentLocation(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String agentId = j.get("id").getAsString();
		String jsonLocation = j.get("location").getAsString();
		
		MovaJson mj = new MovaJson();
		Location location = mj.jsonToLocation(jsonLocation);
		if(location != null){
			
		}
	}
	
	@PUT
	@Path("/registerAgent")
	@Consumes(MediaType.APPLICATION_JSON)
	public void registerAgent(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String jAgentId = j.get("id").getAsString();
		String jType = j.get("type").getAsString();
		boolean jLoggedIn = j.get("loggedIn").getAsBoolean();
		String jRegistrationId = j.get("registrationId").getAsString();
		
		DBHandler db = DBHandler.getInstance();
		db.insertAgent(jAgentId, jType, jLoggedIn, "", jRegistrationId);
	}
}
