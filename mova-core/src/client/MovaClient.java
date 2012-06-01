package client;

import java.net.URI;
import java.util.Vector;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.restlet.resource.ClientResource;

import state.ActivityState;
import state.ItemState;
import type.AgentType;
import type.ItemType;
import utilities.Location;
import utilities.MovaJson;
import actor.Activity;
import actor.Agent;
import actor.Item;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 
 * This class uses the jersey client to access
 * the application server's web services. 
 * It converts MOVA objects into JSON Objects and uses REST API to 
 * POST, GET, PUT and DELETE Resources
 *
 */
public class MovaClient {
	
	private MovaJson mMj;
	private WebResource mService;
	
	/**
	 * Creates a new MOVA client containing a MOVA to JSON Serializer
	 * and a Jersey Client to access server web services
	 */
	public MovaClient(){
		mMj = new MovaJson();
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.setConnectTimeout(300000);
		mService = client.resource(getBaseURI());
	}
	
	private URI getBaseURI() {
		return UriBuilder.fromUri("http://10.0.2.2:8080/mova-server").build();
		//return UriBuilder.fromUri("http://192.168.123.24:8080/mova-server").build();
		//return UriBuilder.fromUri("http://localhost:8080/mova-server").build();
	}
	
	// ITEMS
	
	
	/**
	 * @param pType the type of item to find
	 * @param pQuantity the number of items to find
	 * @param pLocation the location of the agent
	 * @return a vector of items of type <type> closest to the agent's location.
	 * if the number of items are more than <quantity>, only the first <quantity> items
	 * are returned. If less, only those items are returned
	 */
	public Vector<Item> findItem(ItemType pType, int pQuantity, Location pLocation){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", pType.toString());
		queryParams.add("quantity", Integer.toString(pQuantity));
		queryParams.add("location", mMj.locationToJson(pLocation));
		
		String response = mService.queryParams(queryParams).path("items").path("findItem").accept(
				MediaType.APPLICATION_JSON).get(String.class);
		
		return mMj.jsonToItems(response);
	}
	
	/**
	 * @param pItemId the id of the item
	 * @param pLocation the item's current location
	 */
	public void distributeItemLocation(String pItemId, Location pLocation){
		JsonObject j = new JsonObject();
		j.addProperty("id", pItemId);
		j.addProperty("location", mMj.locationToJson(pLocation));
		
//		mService.path("items").path("distributeItemLocation")
//			.type(MediaType.APPLICATION_JSON).put(j.toString());
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/distributeItemLocation");
		resource.put(j.toString());
		
	}
	
	/**
	 * @param pItemId the id of the item
	 * @param pState the item's current state
	 */
	public void distributeItemState(String pItemId, ItemState pState){
		JsonObject j = new JsonObject();
		j.addProperty("id", pItemId);
		j.addProperty("state", pState.toString());
		
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/distributeItemState");
		resource.put(j.toString());
	}
	
	public void changeItemStatus(String itemId, String newStatus, String pAgentId){
		JsonObject j = new JsonObject();
		j.addProperty("itemId", itemId);
		j.addProperty("newStatus", newStatus);
		j.addProperty("agentId", pAgentId);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/changeItemStatus");
		resource.put(j.toString());
	}
	
	public void deleteItem(String itemId){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/deleteItem");
		resource.getReference().addQueryParameter("itemId", itemId);
		resource.get(String.class);
	}
	
	public void getItems(String agentId){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/getItems");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}
	
//	public void getItemsAvailability(String agentId) {
//		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/getItemsAvailability");
//		resource.getReference().addQueryParameter("agentId", agentId);
//		resource.get();
//	}
	
	// ACTIVITY
	
	/**
	 * @param pActivity the activity to send
	 * @param pAgentIds a vector of agent ID's to send to
	 */
	public void sendActivity(Activity pActivity, Vector<String> pAgentIds){
		JsonObject j = new JsonObject();
		j.addProperty("activity", mMj.activityToJson(pActivity));
		j.addProperty("agentIds", mMj.agentsToJson(pAgentIds));

		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/sendActivity");
		resource.put(j.toString());
		
	}
	
	/**
	 * @param pActivities the schedule to send.
	 * @param pAgentId The agent id who should get the schedule.
	 */
	public void sendSchedule(Vector<Activity> pActivities, String pAgentId){
		JsonObject j = new JsonObject();
		j.addProperty("activities", mMj.createJsonObj(pActivities));
		j.addProperty("agentId", mMj.createJsonObj(pAgentId));

//		mService.path("activities").path("sendScheduledActivities")
//		.type(MediaType.APPLICATION_JSON).post(j.toString());
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/sendScheduledActivities");
		resource.put(j.toString());
		
	}
	
	/**
	 * @param pId the id of the activity
	 * @param pState the new state of the activity
	 */
	public void changeActivityStatus(String pId, ActivityState pState){
		JsonObject j = new JsonObject();
		j.addProperty("activityId", pId);
		j.addProperty("state", pState.toString());
		
//		mService.path("activities").path("changeActivityStatus")
//			.type(MediaType.APPLICATION_JSON).put(j.toString());
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/changeActivityStatus");
		resource.put(j.toString());
	}
	
	/**
	 * Adds the activity to the system
	 * @param pActivity the activity to add
	 */
	public void addActivity(Activity pActivity){
		String jAc = mMj.activityToJson(pActivity);

//		mService.path("activities").path("addActivity")
//		.type(MediaType.APPLICATION_JSON).put(jAc);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/addActivity");
		resource.put(jAc.toString());
	}
	
	public void postponeActivity(String activityId, long newFinishTime){
		JsonObject j = new JsonObject();
		j.addProperty("activityId", activityId);
		j.addProperty("newFinishTime", newFinishTime);
//		mService.path("activities").path("postponeActivity").queryParams(queryParams).type(MediaType.APPLICATION_JSON)
//		.post();
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/postponeActivity");
		resource.put(j.toString());
	}
	
	
	public void getSchedule(String agentId){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("agentId", agentId);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/getAgentSchedule");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}
	
	public void getAllActivities(String agentId){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/getAllActivities");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}
	
	// AGENT
	
	/**
	 * @param pId the id of the activity
	 * @param pNewLocation the new location of the agent
	 */
	public void changeAgentLocation(String pId, Location pNewLocation){
		JsonObject j = new JsonObject();
		j.addProperty("id", pId);
		j.addProperty("location", mMj.locationToJson(pNewLocation));
		
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/changeAgentLocation");
		resource.put(j.toString());
	}
	
	public Agent registerAgent(String pRegistrationId, String pAgentType){
		Agent pAgent = new Agent(new AgentType(pAgentType));
		pAgent.setRegistrationId(pRegistrationId);
		String j = mMj.agentToJson(pAgent);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/registerAgent");
		resource.put(j.toString());
		return pAgent;
	}
	
	public void registerAgent(Agent pAgent){
		String j = mMj.agentToJson(pAgent);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/registerAgent");
		resource.put(j.toString());
	}
	
	public void changeAgentStatus(String agentId, boolean isLogin){
		JsonObject j = new JsonObject();
		j.addProperty("agentId", agentId);
		j.addProperty("newStatus", isLogin);
		
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/changeAgentStatus");
		resource.put(j.toString());
	}
	
	public void deleteAgent(String agentId){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("agentId", agentId);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/deleteAgent");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.delete();
	}
	
	public void sendRegistrationId(String registrationId){
		JsonObject j = new JsonObject();
		j.addProperty("registrationId", registrationId);
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/getStaticTypes");
		resource.put(j.toString());
	}
	
	public void getAllAgents(String agentId) {
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/getAllAgents");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}
	
	public void getAllObjects(String agentId) {
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/getAllObjects");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}
	
//	public void getAgentsAvailability(String agentId) {
//		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/getAgentsAvailability");
//		resource.getReference().addQueryParameter("agentId", agentId);
//		resource.get();
//	}
	
	// Static
	
	public void createAgentType(String type){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/createAgentType");
		resource.put(type);
	}
	
	public void deleteAgentType(String type){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/deleteAgentType");
		resource.put(type);
	}
	
	public void createItemType(String type){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/createItemType");
		resource.put(type);
	}
	
	public void deleteItemType(String type){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/items/deleteItemType");
		resource.put(type);
	}
	
	public void createActivityType(String type){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/createActivityType");
		resource.put(type);
	}
	
	public void deleteActivityType(String type){
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/deleteActivityType");
		resource.put(type);
	}

	public void startRecalculate(String agentId) {
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/startRecalculate");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}

	
	public void finishRecalculate(String agentId) {
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/finishRecalculate");
		resource.getReference().addQueryParameter("agentId", agentId);
		resource.get(String.class);
	}

	
	
	
	
	
//	public void changeAgentStatus(String agentId, boolean isLogin){
//		JsonObject j = new JsonObject();
//		j.addProperty("agentId", agentId);
//		j.addProperty("newStatus", isLogin);
//		
//		ClientResource resource = new ClientResource(getBaseURI().toString() + "/agents/changeAgentStatus");
//		resource.put(j.toString());
//	}
}
