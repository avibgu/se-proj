package simulator;

import java.util.Vector;

import actor.Entity;

public class LocationManager {
	
	private Domain _domain;
	
	public LocationManager(Domain domain){
		_domain = domain;
	}
	
	/**
	 * @param entity the entity to return its location
	 * @return the last known location of the entity
	 */
	public Location getLastKnownLocation(Entity entity){
		return _domain.getLocationById(entity.getId());
	}

	/**
	 * @return returns all the entities(e.g. agents, items and sensors) 
	 * after initializing them and placing them in the domain
	 */
	public Vector<Vector<Entity>> initializeLocations() {
		return _domain.initializeLocations();
	}


	
}
