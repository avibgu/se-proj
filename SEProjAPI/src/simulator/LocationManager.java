package simulator;

import java.util.Vector;

import actor.Entity;

public class LocationManager {
	
	private Domain _domain;
	
	public LocationManager(Domain domain){
		_domain = domain;
	}
	
	/**
	 * 
	 * @param entity the entity to return its location
	 * @return the last known location of the entity
	 */
	public Location getLastKnownLocation(Entity entity){
		return _domain.getLocationById(entity.getId());
	}
	
	/**
	 * 
	 * @param entities the agents and items to initialize their locations
	 * @param scenario the scenario to simulate
	 */
	public void initializeLocations(Vector<Vector<Entity>> entities, Scenario scenario) {
		// TODO Auto-generated method stub
		
	}
}
