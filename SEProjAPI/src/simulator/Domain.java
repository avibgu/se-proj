package simulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import type.AgentType;
import actor.Agent;
import actor.Entity;
import actor.Item;
import actor.SensorAgent;

public class Domain {

	private HashMap<String, Location> _grid;
	private int DOMAIN_SIZE;
	private int _nAgents;
	private int _nItems;
	private int _nSensors;
	
	public Domain(){
		loadParameters();
		_grid = new HashMap<String, Location>();
	}
	
	public Location getLocationById(String id) {
		return _grid.get(id);
	}
	
	public Vector<Vector<Entity>> initializeLocations() {
		Random r = new Random();
		Vector<Integer> locations = new Vector<Integer>(DOMAIN_SIZE * DOMAIN_SIZE);
		for(int i = 0 ; i < locations.capacity(); i++){
			locations.add(i);
		}
		Vector<Vector<Entity>> entities = new Vector<Vector<Entity>>();
		Vector<Entity> agents = new Vector<Entity>();
		for(int i = 0; i < _nAgents; i++){
			Agent agent = new Agent(AgentType.COORDINATOR);
			agents.add(agent);
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			l.placeEntity(agent);
			_grid.put(agent.getId(), l);
		}
		entities.add(agents);

		Vector<Entity> items = new Vector<Entity>();
		for(int i = 0; i < _nItems; i++){
			Item item = new Item();
			items.add(item);
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			l.placeEntity(item);
			_grid.put(item.getId(), l);
		}
		entities.add(items);
		Vector<Entity> sensorAgents = new Vector<Entity>();
		for(int i = 0; i < _nSensors; i++){
			SensorAgent sensor = new SensorAgent(AgentType.RFID);
			sensorAgents.add(sensor);
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			l.placeEntity(sensor);
			_grid.put(sensor.getId(), l);
		}
		entities.add(sensorAgents);
		return entities;
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
		_nItems = Integer.parseInt(prop.getProperty("nItems"));
		_nSensors = Integer.parseInt(prop.getProperty("nSensors"));
		
		DOMAIN_SIZE = (_nAgents + _nItems + _nSensors) / 2;
	}
	
}
