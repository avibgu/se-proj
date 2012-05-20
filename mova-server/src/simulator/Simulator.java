package simulator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import db.DBHandler;

import state.ActivityState;
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
	private static Map<String, Agent> mAgents;
	private static Simulator mSimulator = null;
	private static DBHandler db;
	private static int agentIndex = 0;
	
	private Simulator(){
		_entities = _domain.getEntities();
		_agents = _entities.elementAt(0);
		_items = _entities.elementAt(1);
		_sensorAgents = _entities.elementAt(2);
		mAgents = new HashMap<String, Agent>();
		db = DBHandler.getInstance();
//		for (Entity agent : _agents) {
//			mAgents.put(agent.getId(), (Agent) agent);
//		}
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
	
	public void registerAgentMessage(String pId){
		if(agentIndex < _agents.size()){
			Agent agent = (Agent) _agents.elementAt(agentIndex);
			String type = db.getAgentType(pId);
			agent.setId(pId);
			agent.setRepresentation(type);
			db.insertAgentLocation(pId, agent.getLocation());
			mAgents.put(pId, agent);
			
			String message = "An new " + type + " agent registered to the system"; 
			_domain.addMessage(message);
			_domain.setValueAt(type, agent.getLocation().getLongitude(), agent.getLocation().getLatitude());
			
			agentIndex++;
		}
	}
	
	public void changeActivityStatusMessage(String pId, ActivityState pState){
		String name = db.getActivityName(pId);
		String message = "The activity \"" + name + "\"" + " status changed to " + pState;
		_domain.addMessage(message);
	}
	
	public void addActivityMessage(Activity pActivity){
		String message = "A new activity \"" + pActivity.getName() + "\"" + " was created";
		_domain.addMessage(message);
	}
	
	public void postponeActivityMessage(String activityId, String newEndTime){
		String name = db.getActivityName(activityId);
		String message = "The activity \"" + name + "\"" + " was postponed to " + newEndTime;
		_domain.addMessage(message);
	}
	
	public void changeAgentLocationMessage(String pId, Location pNewLocation){
		Location oldLocation = db.getAgentLocation(pId);
		Agent agent = mAgents.get(pId);
		_domain.walkAgent(agent, oldLocation, pNewLocation);
	}
	
	public void changeAgentStatus(String agentId, String newStatus){
		
	}
}
