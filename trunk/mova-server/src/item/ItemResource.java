package item;

import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import simulator.Location;
import state.ItemState;
import type.ItemType;
import utilities.MovaJson;

import actor.Item;

@Path("/items")
public class ItemResource {
	
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
		
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("id").getAsString(); 
		String jsonLocation = j.get("location").getAsString();
		
		MovaJson mj = new MovaJson();
		Location location = mj.jsonToLocation(jsonLocation);
		if(location != null){
			//add c2dm code here
		}
	}
	
	@PUT
	@Path("/distributeItemState")
	@Consumes(MediaType.APPLICATION_JSON)
	public void distributeItemState(String jsonObject){
		
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(jsonObject);
		String id = j.get("id").getAsString(); 
		ItemState state = ItemState.valueOf(j.get("state").getAsString());

		//add c2dm code here
	}
}
