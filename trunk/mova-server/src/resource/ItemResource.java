package resource;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import simulator.Simulator;
import type.ItemType;
import type.MessageType;
import utilities.MovaJson;
import actor.Item;
import c2dm.C2dmController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import db.DBHandler;
/**
 * The item resource is used to process incoming connections
 * from the Mova Client that are relevant to items
 */
@Path("/items")
public class ItemResource {
	
	DBHandler db = DBHandler.getInstance();
	Simulator simulator = Simulator.getInstance(null);
	/**
	 * 
	 * @param type
	 * @param quantity
	 * @param location
	 * @return
	 */
	@GET
	@Path("/findItem")
	@Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public String findItem(@DefaultValue("") @QueryParam("type") String type, 
							@DefaultValue("1") @QueryParam("quantity") int quantity,
							@QueryParam("location") String location){
		
		//add c2dm code here
		//query the agents for the wanted items
		
		
		Vector<Item> items = new Vector<Item>();
		Item i1 = new Item(new ItemType("BOARD"));
		Item i2 = new Item(new ItemType("CABLE"));
		if(new ItemType(type).equals(i1.getType()))
			items.add(i1);
		
		if(new ItemType(type).equals(i2.getType()))
			items.add(i2);
		MovaJson mj = new MovaJson();
		
		return mj.itemsToJson(items);
	}
	/**
	 * Distributes an item's location to all agents
	 * @param jsonObject a json object that holds the item id and the location of the item
	 */
	@PUT
	@Path("/distributeItemLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	public void distributeItemLocation(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		Vector<String> agentsId = null;
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,agentsId,MessageType.DISTRIBUTE_ITEM_LOCATION);
	}
	/**
	 * Distributes an item's state to all agents
	 * @param jsonObject a json object that holds the item id and the state of the item
	 */
	@PUT
	@Path("/distributeItemState")
	@Consumes(MediaType.APPLICATION_JSON)
	public void distributeItemState(String jsonObject){
		JsonArray ids = new JsonArray();
		Vector<String> agentsId = null;
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,agentsId,MessageType.DISTRIBUTE_ITEM_STATE);
	}
	/**
	 * Deletes an item from the system
	 * @param itemId the item id
	 */
	@DELETE
	@Path("/deleteItem/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteItem(@PathParam("id") String itemId){
		db.deleteItem(itemId);
		// Distribute to ALL agents
		Vector<String> agentsId = null;
		String jsonObject = new MovaJson().createJsonObj(itemId);
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,agentsId,MessageType.DELETE_ITEM);
	}
	/**
	 * Changes an item's state in the system
	 * @param jsonObject a json object that holds the item id, 
	 * the new item state and the agent id that changed the item's state
	 */
	@PUT
	@Path("/changeItemStatus")
	public void changeItemStatus(String jsonObject){
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String itemId = j.get("itemId").getAsString();
		String newStatus = j.get("newStatus").getAsString();
		String agentId = j.get("agentId").getAsString();
		db.updateItemState(itemId, newStatus, agentId);
		simulator.updateItemState(itemId, newStatus, agentId);
	}
	/**
	 * Creates a new item type
	 * @param jsonObject a json object that holds the new item type
	 */
	@PUT
	@Path("/createItemType")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createItemType(String jsonObject){
		db.insertItemType(jsonObject);
	}
	/**
	 * Deletes an existing item type
	 * @param jsonObject a json object that holds the item type
	 */
	@PUT
	@Path("/deleteItemType")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteItemType(String jsonObject){
		db.deleteItemType(jsonObject);
	}
	/**
	 * Returns all the items in the systems
	 * @return all the items in the systems
	 */
	@GET
	@Path("/getItems")
	public String getItems(String jsonObject){
		Vector<Item> items = db.getItems();
		MovaJson mj = new MovaJson();
		
		return mj.itemsToJson(items);
	}
}
