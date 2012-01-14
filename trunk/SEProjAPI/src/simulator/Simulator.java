package simulator;

import java.util.Vector;
import type.ItemType;
import State.ActivityState;
import actor.Activity;
import actor.Agent;
import actor.Entity;

public class Simulator {

	private Vector<Entity> _agents;
	private Vector<Activity> _activities;
	private Vector<Entity> _items;
	private Vector<Entity> _sensorAgents;
	private Vector<Vector<Entity>> _entities;
	
	private LocationManager _locationManager;
	
	public Simulator() {
		_locationManager = new LocationManager(new Domain());
		_entities = _locationManager.initializeLocations();
		_agents = _entities.elementAt(0);
		_items = _entities.elementAt(1);
		_sensorAgents = _entities.elementAt(2);
	}
	
	public void start() {
		Agent agent = (Agent)_agents.elementAt(0);
		Activity activity = new Activity();
		Vector<ItemType> requiredItems = new Vector<ItemType>();
		//TODO enter item types
		activity.setRequiredItems(requiredItems);
		//TODO set required agents
		activity.setPriority(1);
		
		activity.setState(ActivityState.PENDING);
		agent.insertActivity(activity);
		
	}
}
