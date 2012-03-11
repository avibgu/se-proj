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

import simulator.Location;
import state.ActivityState;
import state.ItemState;
import type.ItemType;
import utilities.MovaJson;

/**
 * 
 * This class uses the jersey client to access
 * the application server's web services. 
 * It converts MOVA objects into JSON Objects and uses REST API to 
 * POST, GET, PUT and DELETE Resources
 *
 */
public class MovaClient {
	
	private MovaJson _mj;
	private WebResource _service;
	
	/**
	 * Creates a new MOVA client containing a MOVA to JSON Serializer
	 * and a Jersey Client to access server web services
	 */
	public MovaClient(){
		_mj = new MovaJson();
		ClientConfig config = new DefaultClientConfig();
		
		Client client = Client.create(config);
		_service = client.resource(getBaseURI());
	}
	
	private URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/CommunicationServer").build();
	}
	/**
	 * @param type the type of item to find
	 * @param quantity the number of items to find
	 * @param location the location of the agent
	 * @return a vector of items of type <type> closest to the agent's location.
	 * if the number of items are more than <quantity>, only the first <quantity> items
	 * are returned. If less, only those items are returned
	 */
	public Vector<Item> findItem(ItemType type, int quantity, Location location){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", type.toString());
		queryParams.add("quantity", Integer.toString(quantity));
		queryParams.add("location", _mj.locationToJson(location));
		
		String response = _service.queryParams(queryParams).path("items").path("findItem").accept(
				MediaType.APPLICATION_JSON).get(String.class);
		
		return _mj.jsonToItems(response);
	}
	
	/**
	 * @param itemId the id of the item
	 * @param location the item's current location
	 */
	public void distributeItemLocation(String itemId, Location location){
		JsonObject j = new JsonObject();
		j.addProperty("id", itemId);
		j.addProperty("location", _mj.locationToJson(location));
		
		_service.path("items").path("distributeItemLocation")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param itemId the id of the item
	 * @param state the item's current state
	 */
	public void distributeItemState(String itemId, ItemState state){
		JsonObject j = new JsonObject();
		j.addProperty("id", itemId);
		j.addProperty("state", state.toString());
		
		_service.path("items").path("distributeItemState")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param activity the activity to send
	 * @param agentIds a vector of agent ID's to send to
	 */
	public void sendActivity(Activity activity, Vector<String> agentIds){
		JsonObject j = new JsonObject();
		j.addProperty("activity", _mj.activityToJson(activity));
		j.addProperty("agentIds", _mj.agentsToJson(agentIds));
		
		_service.path("activities").path("sendActivity")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param id the id of the activity
	 * @param state the new state of the activity
	 */
	public void changeActivityStatus(String id, ActivityState state){
		JsonObject j = new JsonObject();
		j.addProperty("activityId", id);
		j.addProperty("state", state.toString());
		
		_service.path("activities").path("changeActivityStatus")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	/**
	 * @param id the id of the activity
	 * @param newLocation the new location of the agent
	 */
	public void changeAgentLocation(String id, Location newLocation){
		JsonObject j = new JsonObject();
		j.addProperty("id", id);
		j.addProperty("location", _mj.locationToJson(newLocation));
		
		_service.path("agents").path("changeAgentLocation")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public void registerAgent(Agent agent){
		JsonObject j = new JsonObject();
		j.addProperty("id", agent.getId());
		j.addProperty("type", agent.getType().toString());
		j.addProperty("loggedIn", agent.isLoggedIn());
		j.addProperty("registrationId", agent.getRegistrationId());
		
		_service.path("agents").path("registerAgent")
		.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public String saveRegistrationId(String registraionId){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
	//	queryParams.add("type", type.toString());
		//queryParams.add("quantity", Integer.toString(quantity));
		//queryParams.add("location", _mj.locationToJson(location));
		
		String response = _service.path("c2dm").path("saveRegistrationId/"+registraionId).accept(
				MediaType.APPLICATION_JSON).get(String.class);
		
		return "AAAA";
	}
}
