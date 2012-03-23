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
	private double _nScanDistance;
	private Vector<Vector<Entity>> _entities;
	private Vector<Observer> _observers;
	private Vector<Runnable> _locationMonitors;
	private Vector<Runnable> _itemScanners;
	
	public Domain(int domainSize, int nAgents, int nItems, int nSensors, double nScanDistance){
		DOMAIN_SIZE = domainSize;
		_nAgents = nAgents;
		_nItems = nItems;
		_nSensors = nSensors;
		_nScanDistance = nScanDistance;
		_grid = new HashMap<String, Location>();
		_entities = new Vector<Vector<Entity>>();
		_observers = new Vector<Observer>();
		_locationMonitors = new Vector<Runnable>();
		_itemScanners = new Vector<Runnable>();
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
			Agent agent = new Agent(AgentType.COORDINATOR);
			agents.add(agent);
			
            agent.setRepresentation(agent.getType().toString() + (i+1));
                        
			int location = r.nextInt(locations.size());
			location = locations.remove(location);
			
			Location l = new Location(location / DOMAIN_SIZE, location % DOMAIN_SIZE);
			agent.setRepLocation(l);
			
			_grid.put(agent.getId(), l);
			
			Runnable lm = new LocationMonitor(agent, _observers);
			_locationMonitors.add(lm);
			new Thread(lm).start();
		}
		_entities.add(agents);

		Vector<Entity> items = new Vector<Entity>();
		for(int i = 0; i < _nItems; i++){
			Item item = new Item(ItemType.MOUSE);
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
			_locationMonitors.add(lm);
			new Thread(lm).start();
			//Runnable is = new ItemScanner(sensor, this, _observers, _nScanDistance);
			//_itemScanners.add(is);
			//new Thread(is).start();
		}
		_entities.add(sensorAgents);
		return _entities;
	}
	
	/**
	 * Initializes the agents, items and sensors and places them randomly inside the domain
	 * @return a vector of vectors of the entities where agents are the first vector,
	 * items are the second vector and sensors are the third vector
	 */
	public Vector<Vector<Entity>> manuallyInitializeLocations() {
		_entities.add(manuallyInitializeAgents());
		_entities.add(manuallyInitializeItems());
		//_entities.add(manuallyInitializeSensorAgents());
		
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
    
    public double getScanDistance(){
    	return _nScanDistance;
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

	public void stop() {
		for (Runnable listener : _locationMonitors) {
			((LocationMonitor)listener).stop();
		}
		
		for (Runnable listener : _itemScanners) {
			((ItemScanner)listener).stop();
		}
	}
	
	private Vector<Entity> manuallyInitializeAgents(){
		Vector<Entity> agents = new Vector<Entity>();
		
		Agent coordinatorAgent = new Agent(AgentType.COORDINATOR);
		Agent securityOfficerAgent = new Agent(AgentType.SECURITY_OFFICER);
		Agent secretaryAgent = new Agent(AgentType.SECRETARY);
		Agent networkManagerAgent = new Agent(AgentType.NETWORK_MANAGER);
		Agent SoundManagerAgent = new Agent(AgentType.SOUND_MANAGER);
		
		//agents.add(coordinatorAgent);
		agents.add(securityOfficerAgent);
		agents.add(secretaryAgent);
		//agents.add(networkManagerAgent);
		agents.add(SoundManagerAgent);
		
		//Location coordinatorAgentLocation = new Location(13, 12);
		Location securitOfficerAgentLocation = new Location(9, 3);
		Location secretaryAgentLocation = new Location(13, 27);
		//Location networkManagerAgentLocation = new Location(4, 14);
		Location SoundManagerAgentLocation = new Location(10, 27);
		
		coordinatorAgent.setRepresentation(coordinatorAgent.getType().toString());
		securityOfficerAgent.setRepresentation(securityOfficerAgent.getType().toString());
		secretaryAgent.setRepresentation(secretaryAgent.getType().toString());
		networkManagerAgent.setRepresentation(networkManagerAgent.getType().toString());
		SoundManagerAgent.setRepresentation(SoundManagerAgent.getType().toString());
		
		//coordinatorAgent.setRepLocation(coordinatorAgentLocation);
		securityOfficerAgent.setRepLocation(securitOfficerAgentLocation);
		secretaryAgent.setRepLocation(secretaryAgentLocation);
		//networkManagerAgent.setRepLocation(networkManagerAgentLocation);
		SoundManagerAgent.setRepLocation(SoundManagerAgentLocation);
		
		//_grid.put(coordinatorAgent.getId(), coordinatorAgentLocation);
		_grid.put(securityOfficerAgent.getId(), securitOfficerAgentLocation);
		_grid.put(secretaryAgent.getId(), secretaryAgentLocation);
		//_grid.put(networkManagerAgent.getId(), networkManagerAgentLocation);
		_grid.put(SoundManagerAgent.getId(), SoundManagerAgentLocation);
		
		Runnable lm1 = new LocationMonitor(coordinatorAgent, _observers);
		_locationMonitors.add(lm1);
		new Thread(lm1).start();
		
		Runnable lm2 = new LocationMonitor(securityOfficerAgent, _observers);
		_locationMonitors.add(lm2);
		new Thread(lm2).start();
		
		Runnable lm3 = new LocationMonitor(secretaryAgent, _observers);
		_locationMonitors.add(lm3);
		new Thread(lm3).start();
		
		Runnable lm4 = new LocationMonitor(networkManagerAgent, _observers);
		_locationMonitors.add(lm4);
		new Thread(lm4).start();
		
		Runnable lm5 = new LocationMonitor(SoundManagerAgent, _observers);
		_locationMonitors.add(lm5);
		new Thread(lm5).start();
		
		return agents;
	}
	
	private Vector<Entity> manuallyInitializeItems(){
		Vector<Entity> items = new Vector<Entity>();
		
		Item board = new Item(ItemType.BOARD); items.add(board);
		Item laptop = new Item(ItemType.LAPTOP); items.add(laptop);
		Item mouse = new Item(ItemType.MOUSE); items.add(mouse);
		Item cable = new Item(ItemType.CABLE); items.add(cable);
		Item speaker = new Item(ItemType.SPEAKER); items.add(speaker);
		Item stand = new Item(ItemType.STAND); items.add(stand);
		Item lazerCursor = new Item(ItemType.LAZER_CURSOR); items.add(lazerCursor);
		
		board.setRepresentation(board.getType().toString());
		laptop.setRepresentation(laptop.getType().toString());
		mouse.setRepresentation(mouse.getType().toString());
		cable.setRepresentation(cable.getType().toString());
		speaker.setRepresentation(speaker.getType().toString());
		stand.setRepresentation(stand.getType().toString());
		lazerCursor.setRepresentation(lazerCursor.getType().toString());
		
		Location boardLocation = new Location(12, 28);
		Location laptopLocation = new Location(12, 26);
		Location mouseLocation = new Location(13, 4);
		Location cableLocation = new Location(12,13);
		Location speakerLocation = new Location(8,6);
		Location standLocation = new Location(6, 3);
		Location lazerCursorLocation = new Location(14, 15);
		
		board.setRepLocation(boardLocation);
		laptop.setRepLocation(laptopLocation);
		mouse.setRepLocation(mouseLocation);
		cable.setRepLocation(cableLocation);
		speaker.setRepLocation(speakerLocation);
		stand.setRepLocation(standLocation);
		lazerCursor.setRepLocation(lazerCursorLocation);
		
		_grid.put(board.getId(), boardLocation);
		_grid.put(laptop.getId(), laptopLocation);
		_grid.put(mouse.getId(), mouseLocation);
		_grid.put(cable.getId(), cableLocation);
		_grid.put(speaker.getId(), speakerLocation);
		_grid.put(stand.getId(), standLocation);
		_grid.put(lazerCursor.getId(), lazerCursorLocation);
		
		return items;
	}
//	private Vector<Entity> manuallyInitializeSensorAgents(){
//		Vector<Entity> sensorAgents = new Vector<Entity>();
//		
//		SensorAgent rfidAgent1 = new SensorAgent(AgentType.RFID);
//		SensorAgent rfidAgent2 = new SensorAgent(AgentType.RFID);
//		SensorAgent rfidAgent3 = new SensorAgent(AgentType.RFID);
//		SensorAgent rfidAgent4 = new SensorAgent(AgentType.RFID);
//
//		sensorAgents.add(rfidAgent1);
//		sensorAgents.add(rfidAgent2);
//		sensorAgents.add(rfidAgent3);
//		sensorAgents.add(rfidAgent4);
//
//		rfidAgent1.setRepresentation("Sensor 1");
//		rfidAgent2.setRepresentation("Sensor 2");
//		rfidAgent3.setRepresentation("Sensor 3");
//		rfidAgent4.setRepresentation("Sensor 4");
//
//		Location rfidAgent1Location = new Location(10, 10);
//		Location rfidAgent2Location = new Location(11, 24);
//		Location rfidAgent3Location = new Location(8, 21);
//		Location rfidAgent4Location = new Location(3, 21);
//
//		rfidAgent1.setRepLocation(rfidAgent1Location);
//		rfidAgent2.setRepLocation(rfidAgent2Location);
//		rfidAgent3.setRepLocation(rfidAgent3Location);
//		rfidAgent4.setRepLocation(rfidAgent4Location);
//
//		_grid.put(rfidAgent1.getId(), rfidAgent1Location);
//		_grid.put(rfidAgent2.getId(), rfidAgent2Location);
//		_grid.put(rfidAgent3.getId(), rfidAgent3Location);
//		_grid.put(rfidAgent4.getId(), rfidAgent4Location);
//
//		Runnable lm1 = new LocationMonitor(rfidAgent1, _observers);
//		_locationMonitors.add(lm1);
//		new Thread(lm1).start();
//		Runnable is1 = new ItemScanner(rfidAgent1, this, _observers, _nScanDistance);
//		_itemScanners.add(is1);
//		new Thread(is1).start();
//
//		Runnable lm2 = new LocationMonitor(rfidAgent2, _observers);
//		_locationMonitors.add(lm2);
//		new Thread(lm2).start();
//		Runnable is2 = new ItemScanner(rfidAgent2, this, _observers, _nScanDistance);
//		_itemScanners.add(is2);
//		new Thread(is2).start();
//
//		Runnable lm3 = new LocationMonitor(rfidAgent3, _observers);
//		_locationMonitors.add(lm3);
//		new Thread(lm3).start();
//		Runnable is3 = new ItemScanner(rfidAgent3, this, _observers, _nScanDistance);
//		_itemScanners.add(is3);
//		new Thread(is3).start();
//
//		Runnable lm4 = new LocationMonitor(rfidAgent4, _observers);
//		_locationMonitors.add(lm4);
//		new Thread(lm4).start();
//		Runnable is4 = new ItemScanner(rfidAgent4, this, _observers, _nScanDistance);
//		_itemScanners.add(is4);
//		new Thread(is4).start();
//
//		return sensorAgents;
//	}
}
