package simulator;

import java.util.Vector;
import actor.Entity;

public class LocationManager {
	
	private Domain _domain;
	
	public LocationManager(Domain domain){
		_domain = domain;
	}
	
	/**
	 * Initializes the agents, items and sensors and places them randomly inside the domain
	 * @return a vector of vectors of the entities where agents are the first vector,
	 * items are the second vector and sensors are the third vector
	 */
	public Vector<Vector<Entity>> initializeLocations() {
		return _domain.initializeLocations();
	}

	public boolean checkNewLocation(Location newLocation) {
		return _domain.checkNewLocation(newLocation);
	}
}
