package simulator;

import java.util.Collection;
import java.util.HashMap;
import type.ItemType;
import actor.Entity;
import actor.Item;

public class Location implements Cloneable{
	private int _latitude;
	private int _longitude;
	private HashMap<String, Entity> _entities;
	
	public Location(int latitude, int longitude){
		_latitude = latitude;
		_longitude = longitude;
		_entities = new HashMap<String, Entity>();
	}
	
	public int getLatitude(){
		return _latitude;
	}
	
	public int getLongitude(){
		return _longitude;
	}
	
	public void setLatitude(int latitude){
		_latitude = latitude;
	}
	
	public void setLongitude(int longitude){
		_latitude = longitude;
	}
	
	public void placeEntity(Entity entity){
		_entities.put(entity.getId(), entity);
	}
	
	public Entity removeEntity(Entity entity){
		return _entities.remove(entity.getId());
	}
	
	/**
	 * @param itemType the type of item to get
	 * @return the first item that corresponds to the item type
	 */
	public Entity getEntity(ItemType itemType){
		for (Entity entity : _entities.values()) {
			if(entity instanceof Item)
				return entity;
		}
		return null;
	}
	/**
	 * @param id the id to look for in the location
	 * @return the relevant entity
	 */
	public Entity getEntityById(String id){
		return _entities.get(id);
	}
	
	/**
	 * @return the entities in the location
	 */
	public Collection<Entity> getEntities(){
		return _entities.values();
	}
	
	public Location clone(){
		int latitude = _latitude;
		int longitude = _longitude;
		Location loc = new Location(latitude, longitude);		
		return loc;
	}
}
