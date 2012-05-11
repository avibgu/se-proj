package client;

import java.net.URI;
import java.util.Vector;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import actor.Activity;
import actor.Agent;
import actor.Item;

import utilities.Location;
import state.ActivityState;
import state.ItemState;
import type.ItemType;
import utilities.MovaJson;
import org.restlet.resource.ClientResource;

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
		mService = client.resource(getBaseURI());
	}
	
	private URI getBaseURI() {
		//return UriBuilder.fromUri("http://10.0.2.2:8080/mova-server").build();
		return UriBuilder.fromUri("http://localhost:8080/mova-server").build();
	}
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
		
		mService.path("items").path("distributeItemLocation")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param pItemId the id of the item
	 * @param pState the item's current state
	 */
	public void distributeItemState(String pItemId, ItemState pState){
		JsonObject j = new JsonObject();
		j.addProperty("id", pItemId);
		j.addProperty("state", pState.toString());
		
		mService.path("items").path("distributeItemState")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param pActivity the activity to send
	 * @param pAgentIds a vector of agent ID's to send to
	 */
	public void sendActivity(Activity pActivity, Vector<String> pAgentIds){
		JsonObject j = new JsonObject();
		j.addProperty("activity", mMj.activityToJson(pActivity));
		j.addProperty("agentIds", mMj.agentsToJson(pAgentIds));

	//	_service.path("activities").path("sendActivity")
	//		.type(MediaType.APPLICATION_JSON).put(j.toString());
		
		ClientResource resource = new ClientResource(getBaseURI().toString() + "/activities/sendActivity");
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
		
		mService.path("activities").path("changeActivityStatus")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param pId the id of the activity
	 * @param pNewLocation the new location of the agent
	 */
	public void changeAgentLocation(String pId, Location pNewLocation){
		JsonObject j = new JsonObject();
		j.addProperty("id", pId);
		j.addProperty("location", mMj.locationToJson(pNewLocation));
		
		mService.path("agents").path("changeAgentLocation")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public void registerAgent(Agent pAgent){
		String j = mMj.agentToJson(pAgent);
		mService.path("agents").path("registerAgent")
		.type(MediaType.APPLICATION_JSON).put(j);
	}
	
	/**
	 * Adds the activity to the system
	 * @param pActivity the activity to add
	 */
	public void addActivity(Activity pActivity){
		String jAc = mMj.activityToJson(pActivity);

		mService.path("activities").path("addActivity")
		.type(MediaType.APPLICATION_JSON).put(jAc);
	}
	
	public void postponeActivity(String activityId, String addedTime){
		MultivaluedMap queryParams = new MultivaluedMapImpl();
		queryParams.add("activityId", activityId);
		queryParams.add("addedTime", addedTime);
		mService.path("activities/postponeActivity").queryParams(queryParams).type(MediaType.APPLICATION_JSON)
		.post();
	}
	
	public void changeAgentStatus(String agentId, String newStatus){
		MultivaluedMap queryParams = new MultivaluedMapImpl();
		queryParams.add("agentId", agentId);
		queryParams.add("newStatus", newStatus);
		mService.path("agents/changeAgentStatus").queryParams(queryParams).type(MediaType.APPLICATION_JSON)
		.post();
	}
}
