package resource;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import type.ItemType;
import utilities.MovaJson;
import actor.Item;
import c2dm.C2dmController;

import com.google.gson.JsonArray;

import db.DBHandler;

@Path("/items")
public class ItemResource {
	
	DBHandler db = DBHandler.getInstance();
	
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
	@PUT
	@Path("/distributeItemLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	public void distributeItemLocation(String jsonObject){
		
//		JsonParser jp = new JsonParser();
//		JsonObject j = (JsonObject) jp.parse(jsonObject);
//		String id = j.get("id").getAsString(); 
//		String jsonLocation = j.get("location").getAsString();
//		
//		MovaJson mj = new MovaJson();
//		Location location = mj.jsonToLocation(jsonLocation);
//		if(location != null){
		JsonArray ids = new JsonArray();
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,ids,"DISTRIBUTE_ITEM_LOCATION");
//		}
	}
	

	@PUT
	@Path("/distributeItemState")
	@Consumes(MediaType.APPLICATION_JSON)
	public void distributeItemState(String jsonObject){
		JsonArray ids = new JsonArray();
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,ids,"DISTRIBUTE_ITEM_STATE");
	}
	
	@DELETE
	@Path("/deleteItem/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteItem(@PathParam("id") String itemId){
		db.deleteItem(itemId);
		// Distribute to the agents
		JsonArray ids = new JsonArray();
		String jsonObject = new MovaJson().createJsonObj(itemId);
		C2dmController.getInstance().sendMessageToDevice("3", jsonObject,ids,"DELETE_ITEM");
	}
	
	@POST
	@Path("/changeItemStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changeItemStatus(@QueryParam("itemId") String itemId,
			 					  @QueryParam("newStatus") String newStatus){
		db.updateItemState(itemId, newStatus);
	}
}
