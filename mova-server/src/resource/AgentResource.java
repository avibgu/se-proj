package resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import utilities.Location;
import utilities.MovaJson;
import actor.Agent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;

@Path("/agents")
public class AgentResource {
	
	DBHandler db = DBHandler.getInstance();
	
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
	//@Consumes(MediaType.APPLICATION_JSON)
	public void registerAgent(String jsonObject){
		MovaJson mj = new MovaJson();
		Agent agent = mj.jsonToAgent(jsonObject);
		db.insertAgent(agent.getId(), agent.getType().toString(), true,agent.getRegistrationId());
	}
	
	@DELETE
	@Path("/deleteAgent/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteAgent(@PathParam("id") String agentId){
		db.deleteAgent(agentId);
	}
	
	@POST
	@Path("/changeAgentStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changeAgentStatus(@QueryParam("agentId") String agentId,
			 					  @QueryParam("newStatus") Boolean newStatus){
			// Update DB
			db.changeAgentStatus(agentId, newStatus);
			
			// Recalculate(???)
	}
	
	@PUT
	@Path("/createAgentType")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createAgentType(String jsonObject){
		db.insertAgentType(jsonObject);
	}
	
	@PUT
	@Path("/deleteAgentType")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteAgentType(String jsonObject){
		db.deleteAgentType(jsonObject);
	}
	
}
