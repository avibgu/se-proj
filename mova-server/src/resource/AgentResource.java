package resource;

import java.util.Vector;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import simulator.Simulator;
import type.MessageType;
import utilities.Location;
import utilities.MovaJson;
import actor.Agent;
import c2dm.C2dmController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;
/**
 * The agent resource is used to process incoming connections
 * from the Mova Client that are relevant to agents
 */
@Path("/agents")
public class AgentResource {
	
	DBHandler db = DBHandler.getInstance();
	Simulator simulator = Simulator.getInstance(null);
	MovaJson movaJson = new MovaJson();
	
	/**
	 * Changes an agent's location in the virtual world.
	 * It uses the simulator to simulate the movement of the agent
	 * @param jsonObject a json object that holds the agent id and the new location
	 */
	@PUT
	@Path("/changeAgentLocation")
	public void changeAgentLocation(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String agentId = j.get("id").getAsString();
		String jsonLocation = j.get("location").getAsString();
		
		MovaJson mj = new MovaJson();
		Location location = mj.jsonToLocation(jsonLocation);
		if(location != null){//database update is processed in the simulator
			simulator.changeAgentLocationMessage(agentId, location);
		}
	}
	/**
	 * Registers a new agent in the system.
	 * @param jsonObject a json object that holds the agent
	 */
	@PUT
	@Path("/registerAgent")
	//@Consumes(MediaType.APPLICATION_JSON)
	public void registerAgent(String jsonObject){
		MovaJson mj = new MovaJson();
		Agent agent = mj.jsonToAgent(jsonObject);
		Vector<String> agentsIds = new Vector<String>();
		agentsIds.add(agent.getId());
		
		db.insertAgentType(agent.getType().toString());
		boolean ans = db.insertAgent(agent.getId(), agent.getType().toString(), true,agent.getRegistrationId());

		if (ans){
			C2dmController.getInstance().sendMessageToDevice("3", "",agentsIds,MessageType.REGISTER_SUCCESS);
		}else{
			C2dmController.getInstance().sendMessageToDevice("3", jsonObject,agentsIds,MessageType.REGISTER_FAILED);
		}
		simulator.registerAgentMessage(agent.getId());
	}
	/**
	 * Deletes an existing agent from the system
	 * @param agentId the agent id
	 */
	@DELETE
	@Path("/deleteAgent")
	public void deleteAgent(@QueryParam("agentId") String agentId){
		simulator.deleteAgent(agentId);
		db.deleteAgent(agentId);
	}
	/**
	 * Changes the agent's status
	 * @param jsonObject a json object that holds the agent id and the new status
	 */
	@PUT
	@Path("/changeAgentStatus")
	public void changeAgentStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String agentId = j.get("agentId").getAsString();
		Boolean newStatus = j.get("newStatus").getAsBoolean();
		// Update DB
		db.changeAgentStatus(agentId, newStatus);
	}
	/**
	 * Creates a new agent in the system
	 * @param jsonObject a json object that holds the new agent type
	 */
	@PUT
	@Path("/createAgentType")
	public void createAgentType(String jsonObject){
		db.insertAgentType(jsonObject);
	}
	/**
	 * Deletes an existing agent type from the system
	 * @param jsonObject a json object that holds the existing agent type
	 */
	@PUT
	@Path("/deleteAgentType")
	public void deleteAgentType(String jsonObject){
		db.deleteAgentType(jsonObject);
	}
	
	@PUT
	@Path("/getStaticTypes")
	public void getStaticTypes(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String registrationId = j.get("registrationId").getAsString();

		// Create static types vectors.

		Vector<String> activityTypes = db.getActivityTypes();
		Vector<String> agentTypes = db.getAgentTypes();
		Vector<String> itemTypes = db.getItemTypes();
		
		j = new JsonObject();
		j.addProperty("registrationId", registrationId);
		j.addProperty("activityTypes", movaJson.createJsonObj(activityTypes));
		j.addProperty("agentTypes", movaJson.createJsonObj(agentTypes));
		j.addProperty("itemTypes", movaJson.createJsonObj(itemTypes));

		C2dmController.getInstance().sendMessageUsingRegistrationId("3", j.toString(),registrationId,MessageType.STATIC_TYPES);
	}
	
	@GET
	@Path("/getAllAgents")
	public String getAllAgentsHTTP(@PathParam("agentId") String agentId){
		return new MovaJson().createJsonObj(db.getAllAgents());
	}
	
	public String getAllActivitiesHTTP(@PathParam("agentId") String agentId){
		return new MovaJson().createJsonObj(db.getAllActivities());
	}

	//TODO is this needed??? nobody is using this method
	@PUT
	@Path("/setCurrentActivityId")
	public void setCurrentActivityId(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String agentId = j.get("agentId").getAsString();
		String currentActivityId = j.get("currentActivityId").getAsString();
		// Update DB
		db.changeAgentActivityId(agentId, currentActivityId);
	}
	
	@GET
	@Path("/startRecalculate")
	public String startRecalculate(@QueryParam("agentId") final String agentId) {

		Boolean approvement = DBHandler.canStartNewRecalculte();
		Vector<String> agentsIds = new Vector<String>();
		agentsIds.add(agentId);
		
		// approvement = true;
		
		if (approvement) {
			
			// C2dmController.getInstance().sendMessageToDevice("3", new
			// MovaJson().createJsonObj(agentId),null,
			// MessageType.RECALCULATE_START);
			// getAllActivitiesHTTP(agentId);
			// getAllAgents(agentId);
			
			JsonObject j = new JsonObject();
			
			j.addProperty("activities", getAllActivitiesHTTP(agentId));
			j.addProperty("agents", getAllAgentsHTTP(agentId));
			
			return j.toString();
		}

		return "";
	}
	
	@GET
	@Path("/finishRecalculate")
	public void finishRecalculate(@QueryParam("agentId") String agentId){
		DBHandler.finishRecalculte();
		Vector<String> agentsIds = new Vector<String>();
		agentsIds.add(agentId);
		C2dmController.getInstance().sendMessageToDevice("3", "", null, MessageType.RECALCULATE_FINISH);
	}	
}
