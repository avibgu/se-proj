package resource;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import simulator.Simulator;
import type.MessageType;
import utilities.Location;
import utilities.MovaJson;
import actor.Agent;

import c2dm.C2dmController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;

@Path("/agents")
public class AgentResource {
	
	DBHandler db = DBHandler.getInstance();
	Simulator simulator = Simulator.getInstance(null);
	
	@PUT
	@Path("/changeAgentLocation")
	public void changeAgentLocation(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String agentId = j.get("id").getAsString();
		String jsonLocation = j.get("location").getAsString();
		
		MovaJson mj = new MovaJson();
		Location location = mj.jsonToLocation(jsonLocation);
		if(location != null){
			simulator.changeAgentLocationMessage(agentId, location);
		}
	}
	
	@PUT
	@Path("/registerAgent")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void registerAgent(String jsonObject){
		MovaJson mj = new MovaJson();
		Agent agent = mj.jsonToAgent(jsonObject);
		db.insertAgentType(agent.getType().toString());
		db.insertAgent(agent.getId(), agent.getType().toString(), true,agent.getRegistrationId());
		Vector<String> agentsIds = new Vector<String>();
		agentsIds.add(agent.getId());
		boolean ans = db.insertAgent(agent.getId(), agent.getType().toString(), true,agent.getRegistrationId());
		if (ans){
			C2dmController.getInstance().sendMessageToDevice("3", jsonObject,agentsIds,MessageType.REGISTER_SUCCESS);
		}else{
			C2dmController.getInstance().sendMessageToDevice("3", jsonObject,agentsIds,MessageType.REGISTER_FAILED);
		}
		simulator.registerAgentMessage(agent.getId());
	}
	
	@DELETE
	@Path("/deleteAgent/{id}")
	public void deleteAgent(@PathParam("id") String agentId){
		db.deleteAgent(agentId);
	}
	
	@POST
	@Path("/changeAgentStatus")
	public void changeAgentStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String agentId = j.get("agentId").getAsString();
		Boolean newStatus = j.get("newStatus").getAsBoolean();
		// Update DB
		db.changeAgentStatus(agentId, newStatus);
	}
	
	@PUT
	@Path("/createAgentType")
	public void createAgentType(String jsonObject){
		db.insertAgentType(jsonObject);
	}
	
	@PUT
	@Path("/deleteAgentType")
	public void deleteAgentType(String jsonObject){
		db.deleteAgentType(jsonObject);
	}
	
}
