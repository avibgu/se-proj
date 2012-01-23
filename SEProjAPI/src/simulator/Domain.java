package simulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import type.AgentType;
import type.ItemType;
import actor.Agent;
import actor.Entity;
import actor.Item;
import actor.SensorAgent;
import algorithm.LocationMonitor;

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
	/**
	 * @param id the Entity's id to look for
	 * @return if the entity is in the domain, returns its location, null otherwise
	 */
	public Location getLocationById(String id) {
		return _grid.get(id);
	}
	/**
	 * Initializes the agents, items and sensors and places them randomly inside the domain
	 * @return a vector of vectors of the entities where agents are the first vector,
	 * items are the second vector and sensors are the third vector
	 */
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
			agent.setLocation(l);
			_grid.put(agent.getId(), l);
			Runnable lm = new LocationMonitor(agent);
			new Thread(lm).start();
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
			item.setLocation(l);
			_grid.put(item.getId(), l);
//			Runnable lm = new LocationMonitor(item);
//			new Thread(lm).start();
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
			sensor.setLocation(l);
			_grid.put(sensor.getId(), l);
			Runnable lm = new LocationMonitor(sensor);
			new Thread(lm).start();
			Runnable is = new ItemScanner(sensor, this);
			new Thread(is).start();
		}
		entities.add(sensorAgents);
		return entities;
	}

	/**
	 * Reads the number of agents, items and sensors configured in the config.properties file
	 * The file is to be placed inside the simulator package
	 */
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

	/**
	 * @param entity the entity to calculate the distance from
	 * @param item the item type to find
	 * @return the closest item to the entity, 
	 * or null if there is no item inside the domain
	 */
	public Location findClosestItem(Entity entity, ItemType item) {
		Location entityLocation = getLocationById(entity.getId());
		Location closestItem = new Location(-1, -1);
		double closestLocation = Double.MAX_VALUE;
		synchronized (_grid) {
			for (Location location : _grid.values()) {
				if(location.getEntity(item) instanceof Item){
					double distance = calcDistance(entityLocation, location);
					if(distance < closestLocation){
						closestLocation = distance;
						closestItem = location;
					}
				}
			}
		}
		if(closestItem.getLatitude() != -1)
			return closestItem;
		return null;
	}
	private double calcDistance(Location entityLocation, Location location) {
		double latitudeDiff = entityLocation.getLatitude() - location.getLatitude();
		double longitudeDiff = entityLocation.getLongitude() - location.getLongitude();
		return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
	}
	
	public Vector<Entity> scanForItems(Location entityLocation, double distance){
		Vector<Entity> items = new Vector<Entity>();
		
		synchronized (_grid) {
			for (Location location : _grid.values()) {
				double dist = calcDistance(entityLocation, location);
				if(dist <= distance){
					items.addAll(location.getEntities());
				}
			}
		}
		
		return items;
	}
}
