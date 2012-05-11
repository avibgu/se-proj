package simulator;

import java.util.Vector;

import utilities.Location;

import actor.Activity;
import actor.Agent;
import actor.Entity;


public class Simulator {

	private static NewDomain _domain;
	private static Vector<Entity> _agents;
	private static Vector<Activity> _activities;
	private static Vector<Entity> _items;
	private static Vector<Entity> _sensorAgents;
	private static Vector<Vector<Entity>> _entities;
	private static Simulator mSimulator = null;
	
	private Simulator(){
		_entities = _domain.getEntities();
		_agents = _entities.elementAt(0);
		_items = _entities.elementAt(1);
		_sensorAgents = _entities.elementAt(2);
	}
	
	public synchronized static Simulator getInstance(NewDomain domain){
		if (mSimulator == null)
		{
			_domain = domain;
			mSimulator = new Simulator();
		}

		return mSimulator;
	}
	
	public void start() {
		Agent agent = (Agent)_agents.elementAt(0);
		Location oldLocation = agent.getLocation();
        Location newLocation = new Location(2, 24);
        _domain.walkAgent(agent, oldLocation, newLocation);
	}
}
