package utilities;

import java.util.Vector;

import simulator.Location;
import actor.Activity;
import actor.Item;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MovaJson {
	
	Gson _gson;
	
	public MovaJson(){
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeAdapter(Item.class, new ItemSerializer());
		gb.registerTypeAdapter(Location.class, new LocationSerializer());
		gb.registerTypeAdapter(Item.class, new ItemDeserializer());
		gb.registerTypeAdapter(Location.class, new LocationDeserializer());
		
		_gson = gb.create();
	}
	
	/**
	 * @param item the item to serialize
	 * @return json represented string of the item
	 */
	public String itemToJson(Item item){
		return _gson.toJson(item);
	}
	
	/**
	 * @param items a vector of items to serialize
	 * @return json represented string of the items vector
	 */
	public String itemsToJson(Vector<Item> items){
		return _gson.toJson(items);
	}
	
	/**
	 * @param location the location to serialize
	 * @return json represented string of the locaion
	 */
	public String locationToJson(Location location){
		return _gson.toJson(location);
	}
	
	/**
	 * @param activity the activity to serialize
	 * @return json represented string of the activity
	 */
	public String activityToJson(Activity activity){
		return _gson.toJson(activity);
	}
	
	/**
	 * @param json the json represented string of the item
	 * @return a deserialized Item
	 */
	public Item jsonToItem(String json){
		return _gson.fromJson(json, Item.class);
	}
	
	/**
	 * @param json the json represented string of the items array
	 * @return a vector of deserialized items
	 */
	public Vector<Item> jsonToItems(String json){
		Vector<Item> items = new Vector<Item>();
		
		Item[] itemsArray = _gson.fromJson(json, Item[].class);
		
		for(int i = 0; i < itemsArray.length; i++){
			items.add(itemsArray[i]);
		}
		
		return items;
	}
	
	/**
	 * @param json the json represented string of the location
	 * @return a deserialized Location
	 */
	public Location jsonToLocation(String json){
		return _gson.fromJson(json, Location.class);
	}
	
	/**
	 * @param json the json represented string of the activity
	 * @return a deserialized Activity
	 */
	public Activity jsonToActivity(String json){
		return _gson.fromJson(json, Activity.class);
	}
	
	/**
	 * @param json the json represented string of the activity array
	 * @return a vector of deserialized activities
	 */
	public Vector<Activity> jsonToActivities(String json){
		Vector<Activity> activities = new Vector<Activity>();
		
		Activity[] activityArray = _gson.fromJson(json, Activity[].class);
		
		for(int i = 0; i < activityArray.length; i++){
			activities.add(activityArray[i]);
		}
		
		return activities;
	}
	
	/**
	 * @param agents a vector of agent ID's
	 * @return json represented string of the agents' ID's
	 */
	public String agentsToJson(Vector<String> agents){
		String s = _gson.toJson(agents); 
		return s;
	}
}
