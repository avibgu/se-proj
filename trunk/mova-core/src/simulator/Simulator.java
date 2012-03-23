package simulator;

import java.util.Vector;
import actor.Activity;
import actor.Agent;
import actor.Entity;


public class Simulator {

	private NewDomain _domain;
	private Vector<Entity> _agents;
	private Vector<Activity> _activities;
	private Vector<Entity> _items;
	private Vector<Entity> _sensorAgents;
	private Vector<Vector<Entity>> _entities;
	
	public Simulator(NewDomain domain) {
		_domain = domain;
		_entities = domain.getEntities();
		_agents = _entities.elementAt(0);
		_items = _entities.elementAt(1);
		_sensorAgents = _entities.elementAt(2);
	}
	
	public void start() {
		Agent agent = (Agent)_agents.elementAt(0);
		Location oldLocation = agent.getLocation();
        Location newLocation = new Location(2, 24);
        _domain.walkAgent(agent, oldLocation, newLocation);
	}
}
