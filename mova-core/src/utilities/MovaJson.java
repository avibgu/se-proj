package utilities;

import java.util.Vector;

import utilities.Location;
import actor.Activity;
import actor.Agent;
import actor.Item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class MovaJson {
	
	Gson _gson;
	
	public MovaJson(){
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Item.class, new ItemSerializer());
		gb.registerTypeAdapter(Location.class, new LocationSerializer());
		gb.registerTypeAdapter(Item.class, new ItemDeserializer());
		gb.registerTypeAdapter(Location.class, new LocationDeserializer());
		gb.registerTypeAdapter(Activity.class, new ActivitySerializer());
		gb.registerTypeAdapter(Activity.class, new ActivityDeserializer());
		
		_gson = gb.create();
	}
	
	/**
	 * @param item the item to serialize
	 * @return JSON represented string of the item
	 */
	public String itemToJson(Item item){
		return _gson.toJson(item);
	}
	
	/**
	 * @param items a vector of items to serialize
	 * @return JSON represented string of the items vector
	 */
	public String itemsToJson(Vector<Item> items){
		return _gson.toJson(items);
	}
	
	/**
	 * @param location the location to serialize
	 * @return JSON represented string of the location
	 */
	public String locationToJson(Location location){
		return _gson.toJson(location);
	}
	
	/**
	 * @param activity the activity to serialize
	 * @return JSON represented string of the activity
	 */
	public String activityToJson(Activity activity){
		return _gson.toJson(activity);
	}
	
	/**
	 * @param json the JSON represented string of the item
	 * @return a deserialized Item. Returns null if json is not a JSON Item
	 */
	public Item jsonToItem(String json){
		Item i = null;
		try{
			i = _gson.fromJson(json, Item.class);
		}
		catch(JsonSyntaxException e){
			
		}
		return i;
	}
	
	/**
	 * @param json the JSON represented string of the items array
	 * @return a vector of deserialized items. Returns an empty vector if json is not a JSON vector items
	 */
	public Vector<Item> jsonToItems(String json){
		Vector<Item> items = new Vector<Item>();
		Item[] itemsArray = null;
		try{
			itemsArray = _gson.fromJson(json, Item[].class);
		}
		catch(JsonSyntaxException e){
			return items;
		}
		
		for(int i = 0; i < itemsArray.length; i++){
			items.add(itemsArray[i]);
		}
		
		return items;
	}
	
	/**
	 * @param json the JSON represented string of the location
	 * @return a deserialized Location. Returns null if json is not a JSON Location
	 */
	public Location jsonToLocation(String json){
		Location l = null;
		try{
			l = _gson.fromJson(json, Location.class);
		}
		catch(JsonSyntaxException e){
			
		}
		return l;
	}
	
	/**
	 * @param json the JSON represented string of the activity
	 * @return a deserialized Activity. Returns null if json is not a JSON Activity
	 */
	public Agent jsonToAgent(String json){
		Agent a = null;
		try{
			a = _gson.fromJson(json, Agent.class);
		}
		catch(JsonSyntaxException e){
			e.printStackTrace();
			System.out.println("JsonToAgent failed");
		}
		return a;
	}
	
	/**
	 * @param json the JSON represented string of the activity
	 * @return a deserialized Activity. Returns null if json is not a JSON Activity
	 */
	public Activity jsonToActivity(String json){
		Activity a = null;
		try{
			a = _gson.fromJson(json, Activity.class);
		}
		catch(JsonSyntaxException e){
			
		}
		return a;
	}
	
	/**
	 * @param json the JSON represented string of the activity array
	 * @return a vector of deserialized activities. Returns an empty vector if json is not a JSON vector activities
	 */
	public Vector<Activity> jsonToActivities(String json){
		Vector<Activity> activities = new Vector<Activity>();
		Activity[] activityArray = null;
		try{
			activityArray = _gson.fromJson(json, Activity[].class);
		}
		catch(JsonSyntaxException e){
			return activities;
		}
		
		for(int i = 0; i < activityArray.length; i++){
			activities.add(activityArray[i]);
		}
		
		return activities;
	}
	
	/**
	 * @param agents a vector of agent ID's
	 * @return JSON represented string of the agents' ID's
	 */
	public String agentsToJson(Vector<String> agents){
		return _gson.toJson(agents);
	}
	
	public String agentToJson(Agent agent){
		return _gson.toJson(agent);
	}
}
