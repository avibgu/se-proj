package simulator;

import java.util.Vector;
import actor.Activity;
import actor.Agent;
import actor.Entity;
import actor.Item;

public class Simulator {

	private Vector<Entity> _agents;
	private Vector<Activity> _activities;
	private Vector<Entity> _items;
	private Vector<Entity> _sensorAgents;
	private Vector<Vector<Entity>> _entities;
	
	private LocationManager _locationManager;
	
	public Simulator(Domain domain) {
		_locationManager = new LocationManager(domain);
		_entities = _locationManager.manuallyInitializeLocations();
		_agents = _entities.elementAt(0);
		_items = _entities.elementAt(1);
		_sensorAgents = _entities.elementAt(2);
	}
	
	public void start() {
		/*Agent agent = (Agent)_agents.elementAt(0);
		Activity activity = new Activity();
		Vector<ItemType> requiredItems = new Vector<ItemType>();
		//TODO enter item types
		activity.setRequiredItems(requiredItems);
		//TODO set required agents
		activity.setPriority(1);
		
		activity.setState(ActivityState.PENDING);
		agent.insertActivity(activity);*/
		
		Agent agent = (Agent)_agents.elementAt(0);
		Location oldLocation = agent.getLocation();
		Location newLocation = new Location(oldLocation.getLatitude() + 1, oldLocation.getLongitude());
		if(_locationManager.checkNewLocation(newLocation))
			agent.setLocation(newLocation);
		
		Agent agent1 = (Agent)_agents.elementAt(1);
		Location agentOldLocation1 = agent1.getLocation();
		Location agentNewLocation1 = new Location(agentOldLocation1.getLatitude() + 1, agentOldLocation1.getLongitude());
		if(_locationManager.checkNewLocation(agentNewLocation1))
			agent1.setLocation(agentNewLocation1);
		
		Item i1 = (Item) _items.elementAt(0);
		Item i2 = (Item) _items.elementAt(1);
		Item i3 = (Item) _items.elementAt(2);
		Location oldLocation1 = i1.getLocation();
		Location newLocation1 = new Location(oldLocation1.getLatitude() + 1, oldLocation1.getLongitude());
		if(_locationManager.checkNewLocation(newLocation1))
			i1.setLocation(newLocation1);
		
		Location oldLocation2 = i2.getLocation();
		Location newLocation2 = new Location(oldLocation2.getLatitude() + 1, oldLocation2.getLongitude());
		if(_locationManager.checkNewLocation(newLocation2))
			i2.setLocation(newLocation2);
		
		Location oldLocation3 = i3.getLocation();
		Location newLocation3 = new Location(oldLocation3.getLatitude() + 1, oldLocation3.getLongitude());
		if(_locationManager.checkNewLocation(newLocation3))
			i3.setLocation(newLocation3);
	}
}
