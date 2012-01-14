package simulator;

import java.util.Vector;

import type.ItemType;

import actor.Entity;

public class LocationManager {
	
	private Domain _domain;
	
	public LocationManager(Domain domain){
		_domain = domain;
	}
	
	/**
	 * @param id the Entity's id to look for
	 * @return if the entity is in the domain, returns its location, null otherwise
	 */
	public Location getLastKnownLocation(Entity entity){
		return _domain.getLocationById(entity.getId());
	}

	/**
	 * Initializes the agents, items and sensors and places them randomly inside the domain
	 * @return a vector of vectors of the entities where agents are the first vector,
	 * items are the second vector and sensors are the third vector
	 */
	public Vector<Vector<Entity>> initializeLocations() {
		return _domain.initializeLocations();
	}
	
	/**
	 * @param entity the entity to calculate the distance from
	 * @param item the item type to find
	 * @return the closest item to the entity, 
	 * or null if there is no item inside the domain
	 */
	public Location findClosestItem(Entity entity, ItemType item){
		return _domain.findClosestItem(entity, item);
	}
	
}
