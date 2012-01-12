package simulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import type.AgentType;

import actor.Activity;
import actor.Agent;
import actor.Entity;
import actor.Item;
import actor.SensorAgent;

public class Simulator {
	
	private int _nAgents;
	private int _nActivities;
	private int _nItems;
	private int _nSensors;
	
	private Vector<Entity> _agents;
	private Vector<Activity> _activities;
	private Vector<Entity> _items;
	private Vector<Entity> _sensorAgents;
	
	private Vector<Vector<Entity>> _entities;
	
	private LocationManager _locationManager;
	
	public Simulator() {
		loadParameters();
		initializeEntities();
		_locationManager = new LocationManager(new Domain());
		_locationManager.initializeLocations(_entities, new Scenario());
	}
	
	private void initializeEntities() {
		//all vectors didn't receive capacities in order to dynamically add a certain entity
		
		_entities = new Vector<Vector<Entity>>();
		_agents = new Vector<Entity>();
		for(int i = 0; i < _nAgents; i++){
			_agents.add(new Agent(AgentType.DOCTOR));
		}
		_entities.add(_agents);
		_activities = new Vector<Activity>();
		for(int i = 0; i < _nActivities; i++){
			_activities.add(new Activity());
		}
		_items = new Vector<Entity>();
		for(int i = 0; i < _nItems; i++){
			_items.add(new Item());
		}
		_entities.add(_items);
		_sensorAgents = new Vector<Entity>();
		for(int i = 0; i < _nSensors; i++){
			_sensorAgents.add(new SensorAgent(AgentType.RFID));
		}
		_entities.add(_sensorAgents);
	}

	private void loadParameters() {
		Properties prop = new Properties();
		try {
			InputStream in = getClass().getResourceAsStream("config.properties");
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_nAgents = Integer.parseInt(prop.getProperty("nAgents"));
		_nActivities = Integer.parseInt(prop.getProperty("nActivities"));
		_nItems = Integer.parseInt(prop.getProperty("nItems"));
		_nSensors = Integer.parseInt(prop.getProperty("nSensors"));
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}
}
