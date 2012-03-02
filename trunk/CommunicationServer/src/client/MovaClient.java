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

import State.ActivityState;
import State.ItemState;
import actor.Activity;
import actor.Item;

import simulator.Location;
import type.ItemType;
import utilities.MovaJson;

public class MovaClient {
	
	private MovaJson _mj;
	private WebResource _service;
	
	public MovaClient(){
		_mj = new MovaJson();
		ClientConfig config = new DefaultClientConfig();
		
		Client client = Client.create(config);
		_service = client.resource(getBaseURI());
	}
	
	private URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/CommunicationServer").build();
	}
	
	public Vector<Item> findItem(ItemType type, int quantity, Location location){
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("type", type.toString());
		queryParams.add("quantity", Integer.toString(quantity));
		queryParams.add("location", _mj.locationToJson(location));
		
		String response = _service.queryParams(queryParams).path("items").path("findItem").accept(
				MediaType.APPLICATION_JSON).get(String.class);
		
		return _mj.jsonToItems(response);
	}
	
	public void distributeItemLocation(String itemId, Location location){
		JsonObject j = new JsonObject();
		j.addProperty("id", itemId);
		j.addProperty("location", _mj.locationToJson(location));
		
		_service.path("items").path("distributeItemLocation")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public void distributeItemState(String itemId, ItemState state){
		JsonObject j = new JsonObject();
		j.addProperty("id", itemId);
		j.addProperty("state", state.toString());
		
		_service.path("items").path("distributeItemState")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public void sendActivity(Activity activity, Vector<String> agentIds){
		JsonObject j = new JsonObject();
		j.addProperty("activity", _mj.activityToJson(activity));
		j.addProperty("agentIds", _mj.agentsToJson(agentIds));
		
		_service.path("activities").path("sendActivity")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public void changeActivityStatus(String id, ActivityState state){
		JsonObject j = new JsonObject();
		j.addProperty("activityId", id);
		j.addProperty("state", state.toString());
		
		_service.path("activities").path("changeActivityStatus")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
	
	public void changeAgentLocation(String id, Location newLocation){
		JsonObject j = new JsonObject();
		j.addProperty("id", id);
		j.addProperty("location", _mj.locationToJson(newLocation));
		
		_service.path("agents").path("changeAgentLocation")
			.type(MediaType.APPLICATION_JSON).put(j.toString());
	}
}
