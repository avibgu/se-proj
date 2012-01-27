package simulator;

import java.util.HashMap;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;
import type.AgentType;
import actor.Agent;
import actor.Entity;
import actor.Item;
import actor.SensorAgent;
import algorithm.LocationMonitor;
import type.ItemType;

public class Domain {

	private HashMap<String, Location> _grid;
	
	private int DOMAIN_SIZE;
	private int _nAgents;
	private int _nItems;
	private int _nSensors;
	private Vector<Vector<Entity>> _entities;
	private Vector<Observer> _observers;
	
	public Domain(int domainSize, int nAgents, int nItems, int nSensors){
		DOMAIN_SIZE = domainSize;
		_nAgents = nAgents;
		_nItems = nItems;
		_nSensors = nSensors;
		_grid = new HashMap<String, Location>();
		_entities = new Vector<Vector<Entity>>();
		_observers = new Vector<Observer>();
	}

	public Vector<Observer> getObservers() {
		return _observers;
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

		Vector<Entity> agents = new Vector<Entity>();
		for(int i = 0; i < _nAgents; i++){
			Agent agent = new Agent(AgentType.Coordinator);
			agents.add(agent);
			
            agent.setRepresentation(agent.getType().toString() + (i+1));
                        
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			agent.setRepLocation(l);
			
			_grid.put(agent.getId(), l);
			
			Runnable lm = new LocationMonitor(agent, _observers);
			new Thread(lm).start();
		}
		_entities.add(agents);

		Vector<Entity> items = new Vector<Entity>();
		for(int i = 0; i < _nItems; i++){
			Item item = new Item();
                        item.setType(ItemType.Mouse);
			items.add(item);
			
                        item.setRepresentation(item.getType().toString() + (i+1));
                        
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			item.setRepLocation(l);
			
			_grid.put(item.getId(), l);
		}
		_entities.add(items);
		Vector<Entity> sensorAgents = new Vector<Entity>();
		for(int i = 0; i < _nSensors; i++){
			SensorAgent sensor = new SensorAgent(AgentType.RFID);
			sensorAgents.add(sensor);
			
                        sensor.setRepresentation("Sensor" + (i+1));
                        
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			sensor.setRepLocation(l);
			
			_grid.put(sensor.getId(), l);
			
			Runnable lm = new LocationMonitor(sensor, _observers);
			new Thread(lm).start();
			Runnable is = new ItemScanner(sensor, this, _observers);
			new Thread(is).start();
		}
		_entities.add(sensorAgents);
		return _entities;
	}

	public Vector<Entity> scanForItems(Location entityLocation, double distance){
		Vector<Entity> items = new Vector<Entity>();
		synchronized (_entities) {
			for (Entity item : _entities.get(1)) {
				Location itemLocation = item.getLocation();
				double dist = calcDistance(entityLocation, itemLocation);
				if(dist <= distance){
					items.add(item);
				}
			}
		}
		
		return items;
	}
	
	private double calcDistance(Location entityLocation, Location location) {
		if(entityLocation != null && location != null){
			double latitudeDiff = entityLocation.getLatitude() - location.getLatitude();
			double longitudeDiff = entityLocation.getLongitude() - location.getLongitude();
			return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
		}
		return -1;
	}
        
    public Vector<Vector<Entity>> getEntities(){
        return _entities;
    }
    
    public int getDomainSize(){
        return DOMAIN_SIZE;
    }
    
    public synchronized HashMap<String, Location> getGrid(){
    	return _grid;
    }

	public boolean checkNewLocation(Location newLocation) {
		int latitude = newLocation.getLatitude();
		int longitude = newLocation.getLongitude();
		if(latitude >= DOMAIN_SIZE || longitude >= DOMAIN_SIZE || latitude < 0 || longitude < 0)
			return false;
		return true;
	}
}
