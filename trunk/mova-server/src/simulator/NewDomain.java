
package simulator;

import actor.Agent;
import actor.Entity;
import actor.Item;
import actor.SensorAgent;
import gui.WalkingAgentWorker;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import type.AgentType;
import type.ItemType;
import utilities.Location;
/**
 * The NewDomain class is used to hold the Mova map, entities, item scanners
 * and all the logic for representing the simulated world
 */
public class NewDomain extends DefaultTableModel{
    
	private static final long serialVersionUID = 3391358106917744757L;
	private MovaMap mMovaMap; //stores doors, rooms and nodes for route finding
	private double _nScanDistance;
	private Vector<Vector<Entity>> _entities;
    private String[][] _locations; //Data Vector needed for the table
    private HashMap<Entity, Location> _entitiesMap;
	private Vector<Observer> _observers;
	private Vector<Runnable> _itemScanners; //Simulates the sensor scans. One for each Sensor
    private Vector<Door> _doors;
    /**
     * Creates a new domain according configurable variables
     * @param height the domain height
     * @param width the domain width
     * @param nScanDistance the scan distance of the item scanners
     */
    public NewDomain (int height, int width, double nScanDistance){
        _nScanDistance = nScanDistance;
        _entities = new Vector<Vector<Entity>>();
        _locations = new String[height][width];
        _entitiesMap = new HashMap<Entity, Location>();
        _observers = new Vector<Observer>();
        _itemScanners = new Vector<Runnable>();
        mMovaMap = new MovaMap(height, width);
        initializeLocations(height, width);//Initialize data vector to an empty string
        initializeMap(); //Initialize the mova map
        manuallyInitializeLocations(); // Initialize the entities on the map
        initializeDataVector(width); //initialize the data vector needed for the table model
    }

    private void initializeMap() {
        initializeRooms();
        initializeDoors();
        mMovaMap.initializeLocations();
        mMovaMap.setLocationNodesNeighbors();
    }

    private void initializeRooms() {
        //Entrance and Lobby
        Vector<Location> lobbyMargins = new Vector<Location>();
        String roomName = "Lobby";
        lobbyMargins.addAll(initializeLocationRange(10, 21, 34, roomName));
        lobbyMargins.addAll(initializeLocationRange(11, 21, 22, roomName));
        lobbyMargins.addAll(initializeLocationRange(11, 25, 30, roomName));
        lobbyMargins.addAll(initializeLocationRange(11, 33, 33, roomName));
        lobbyMargins.addAll(initializeLocationRange(12, 21, 22, roomName));
        lobbyMargins.addAll(initializeLocationRange(12, 26, 29, roomName));
        lobbyMargins.addAll(initializeLocationRange(13, 26, 29, roomName));
        Room lobby = new Room("Lobby", lobbyMargins);

        //Air Court
        Vector<Location> courtMargins = new Vector<Location>();
        roomName = "Air Court";
        courtMargins.addAll(initializeLocationRange(4, 23, 32, roomName));
        courtMargins.addAll(initializeLocationRange(5, 23, 32, roomName));
        courtMargins.addAll(initializeLocationRange(6, 23, 32, roomName));
        courtMargins.addAll(initializeLocationRange(7, 23, 32, roomName));
        courtMargins.addAll(initializeLocationRange(8, 23, 32, roomName));
        courtMargins.addAll(initializeLocationRange(9, 23, 32, roomName));
        Room court = new Room("Air Court", courtMargins);

        //ExecutiveBoardroom
        Vector<Location> executiveBoardroomMargins = new Vector<Location>();
        roomName = "Executive Boardroom";
        executiveBoardroomMargins.addAll(initializeLocationRange(1, 23, 32, roomName));
        executiveBoardroomMargins.addAll(initializeLocationRange(2, 23, 32, roomName));
        executiveBoardroomMargins.addAll(initializeLocationRange(3, 24, 32, roomName));
        Room executiveBoardroom = new Room("Executive Boardroom", executiveBoardroomMargins);

        //Conference Room B
        Vector<Location> conferenceRoomBMargins = new Vector<Location>();
        roomName = "Conference Room B";
        conferenceRoomBMargins.addAll(initializeLocationRange(10, 15, 19, roomName));
        conferenceRoomBMargins.addAll(initializeLocationRange(11, 16, 19, roomName));
        conferenceRoomBMargins.addAll(initializeLocationRange(12, 14, 19, roomName));
        conferenceRoomBMargins.addAll(initializeLocationRange(13, 14, 19, roomName));
        conferenceRoomBMargins.addAll(initializeLocationRange(14, 14, 19, roomName));
        Room conferenceRoomB = new Room("Conference Room B", conferenceRoomBMargins);

        //Conference Lobby
        Vector<Location> conferenceLobbyMargins = new Vector<Location>();
        roomName = "Conference Lobby";
        conferenceLobbyMargins.addAll(initializeLocationRange(4, 9, 12, roomName));
        conferenceLobbyMargins.addAll(initializeLocationRange(5, 11, 21, roomName));
        conferenceLobbyMargins.addAll(initializeLocationRange(6, 12, 21, roomName));
        conferenceLobbyMargins.addAll(initializeLocationRange(7, 11, 21, roomName));
        conferenceLobbyMargins.addAll(initializeLocationRange(8, 10, 11, roomName));
        conferenceLobbyMargins.addAll(initializeLocationRange(8, 15, 21, roomName));
        conferenceLobbyMargins.addAll(initializeLocationRange(9, 15, 21, roomName));
        Room conferenceLobby = new Room("Conference Lobby", conferenceLobbyMargins);

         //Lounge
        Vector<Location> loungeMargins = new Vector<Location>();
        roomName = "Lounge";
        loungeMargins.addAll(initializeLocationRange(3, 16, 21, roomName));
        loungeMargins.addAll(initializeLocationRange(4, 16, 21, roomName));
        Room lounge = new Room("Lounge", loungeMargins);

        //Main Office
        Vector<Location> mainOfficeMargins = new Vector<Location>();
        roomName = "Main Office";
        mainOfficeMargins.addAll(initializeLocationRange(2, 12, 15, roomName));
        mainOfficeMargins.addAll(initializeLocationRange(3, 12, 15, roomName));
        mainOfficeMargins.addAll(initializeLocationRange(4, 13, 15, roomName));
        Room mainOffice = new Room("Main Office", mainOfficeMargins);

        //Auditorium
        Vector<Location> auditoriumMargins = new Vector<Location>();
        roomName = "Auditorium";
        auditoriumMargins.addAll(initializeLocationRange(8, 2, 10, roomName));
        auditoriumMargins.addAll(initializeLocationRange(9, 2, 10, roomName));
        auditoriumMargins.addAll(initializeLocationRange(10, 2, 14, roomName));
        auditoriumMargins.addAll(initializeLocationRange(11, 2, 13, roomName));
        auditoriumMargins.addAll(initializeLocationRange(12, 2, 13, roomName));
        auditoriumMargins.addAll(initializeLocationRange(13, 2, 12, roomName));
        Room auditorium = new Room("Auditorium", auditoriumMargins);

        //Conference Room A
        Vector<Location> conferenceRoomAMargins = new Vector<Location>();
        roomName = "Conference Room A";
        conferenceRoomAMargins.addAll(initializeLocationRange(5, 2, 10, roomName));
        conferenceRoomAMargins.addAll(initializeLocationRange(6, 2, 10, roomName));
        conferenceRoomAMargins.addAll(initializeLocationRange(7, 2, 9, roomName));
        Room conferenceRoomA = new Room("Conference Room A", conferenceRoomAMargins);

        Vector<Room> rooms = new Vector<Room>();
        rooms.add(lobby);
        rooms.add(court);
        rooms.add(executiveBoardroom);
        rooms.add(conferenceRoomB);
        rooms.add(conferenceLobby);
        rooms.add(lounge);
        rooms.add(mainOffice);
        rooms.add(auditorium);
        rooms.add(conferenceRoomA);
        //_rooms = rooms;
        mMovaMap.setRooms(rooms);
    }

    private void initializeDoors() {
        Door door1 = new Door(new Location(13,27), null, mMovaMap.getRoom("Lobby"));
        Door door2 = new Door(new Location(9,30), mMovaMap.getRoom("Lobby"), mMovaMap.getRoom("Air Court"));
        Door door3 = new Door(new Location(9,24), mMovaMap.getRoom("Lobby"), mMovaMap.getRoom("Air Court"));
        Door door4 = new Door(new Location(10,21), mMovaMap.getRoom("Lobby"), mMovaMap.getRoom("Conference Lobby"));
        Door door5 = new Door(new Location(10,15), mMovaMap.getRoom("Conference Lobby"), mMovaMap.getRoom("Conference Room B"));
        Door door6 = new Door(new Location(10,14), mMovaMap.getRoom("Conference Lobby"), mMovaMap.getRoom("Auditorium"));
        Door door7 = new Door(new Location(4,24), mMovaMap.getRoom("Air Court"), mMovaMap.getRoom("Executive Boardroom"));
        Door door8 = new Door(new Location(4,30), mMovaMap.getRoom("Air Court"), mMovaMap.getRoom("Executive Boardroom"));
        Door door9 = new Door(new Location(3,22), mMovaMap.getRoom("Executive Boardroom"), mMovaMap.getRoom("Lounge"));
        Door door10 = new Door(new Location(8,10), mMovaMap.getRoom("Conference Lobby"), mMovaMap.getRoom("Auditorium"));
        Door door11 = new Door(new Location(7,10), mMovaMap.getRoom("Conference Lobby"), mMovaMap.getRoom("Conference Room A"));
        Door door12 = new Door(new Location(4,12), mMovaMap.getRoom("Conference Lobby"), mMovaMap.getRoom("Main Office"));
        Door door13 = new Door(new Location(5,20), mMovaMap.getRoom("Conference Lobby"), mMovaMap.getRoom("Lounge"));
        Vector<Door> doors = new Vector<Door>();
        doors.add(door1); doors.add(door2);doors.add(door3);
        doors.add(door4); doors.add(door5);doors.add(door6);
        doors.add(door7); doors.add(door8);doors.add(door9);
        doors.add(door10); doors.add(door11);doors.add(door12);doors.add(door13);
        _doors = doors;
        mMovaMap.setDoors(doors);
    }

    private Vector<Location> initializeLocationRange(int row, int start, int end, String roomName){
        Vector<Location> locationRange = new Vector<Location>();
        for (int i = start; i <= end; i++) {
            Location location = new Location(row, i);
            location.setRoom(roomName);
        	locationRange.add(location);
        }
        return locationRange;
    }
    /**
     * Changes an entity's location from one location to another
     * @param entity the entity to change its location
     * @param from the start location
     * @param to the new location
     */
    public void changeAgentLocation(Entity entity, Location from, Location to){
        entity.setLocation(to);
        updateEntityLocation(entity);
    }
    /**
     * Changes all items locations in the domain
     * @param changedLocationItems the map of the items where the String key is
     * the items' id
     */
    public void changeItemsLocation(Map<String, Item> changedLocationItems){
        if(changedLocationItems != null){//itemScanner has executed this method
            //update table for each visible item
            for (Item item : changedLocationItems.values()) {
            	updateEntityLocation(item);
			}
        }
    }
    /**
     * Creates a new Worker thread to simulate an agent's movement and executes the thread
     * @param entity the agent entity to simulate its movement
     * @param from the start location
     * @param to the goal location
     */
    public void walkAgent(Entity entity, Location from, Location to) {
           WalkingAgentWorker worker = new WalkingAgentWorker(this, entity, from, to, _doors);
           worker.execute();
    }
    /**
     * Stops all item scanner threads execution
     */
    public void stop() {
		for (Runnable listener : _itemScanners) {
			((ItemScanner)listener).stop();
		}
	}
    /**
     * Returns the scan distance of all the item scanners
     * @return the scan distance of all the item scanners
     */
    public double getScanDistance(){
    	return _nScanDistance;
    }

//    public Location findClosestRoomDoor(Location from, Room fromRoom) {
//        Door closestDoor = null;
//        int shortestPath = Integer.MAX_VALUE;
//        for (Door door : _doors) {
//            Room room1 = door.getRoom1();
//            Room room2 = door.getRoom2();
//            if(room1 == null || room2 == null)
//                continue;
//            if(room1.equals(fromRoom) || room2.equals(fromRoom)){
//                Stack<Location> path = mMovaMap.calculateShortestPath(from, door.getLocation());
//                if(path.size() < shortestPath){
//                    shortestPath = path.size();
//                    closestDoor = door;
//                }
//            }
//        }
//        if(closestDoor != null)
//            return closestDoor.getLocation();
//        else
//            return null;
//    }
    /**
     * Returns the Mova map
     * @return the Mova map
     */
    public MovaMap getMovaMap(){
        return mMovaMap;
    }
    /**
     * Returns the Observers of the domain
     * @return the Observers of the domain
     */
    public Vector<Observer> getObservers() {
		return _observers;
	}
    /**
     * Returns the entities located in the domain
     * @return the entities located in the domain
     */
    public Vector<Vector<Entity>> getEntities(){
        return _entities;
    }
    /**
     * Returns the entities map where the key is entity and the value is its location
     * @return the entities map where the key is entity and the value is its location
     */
    public HashMap<Entity, Location> getEntitiesMap(){
        return _entitiesMap;
    }
    /**
     * Scans for items according to a scan distance
     * @param entityLocation the location of the sensor
     * @param distance the scan distance
     * @return a Vector of items identified in the scan range
     */
    public Vector<Item> scanForItems(Location entityLocation, double distance){
		Vector<Item> items = new Vector<Item>();
		synchronized (_entities) {
			Vector<Entity> entityItems = _entities.elementAt(1);
			for (Entity item : entityItems) {
				Location itemLocation = item.getLocation();
				double dist = calcDistance(entityLocation, itemLocation);
				if(dist <= distance){
					items.add((Item) item);
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

    public boolean checkNewLocation(Location newLocation) {
		int latitude = newLocation.getLatitude();
		int longitude = newLocation.getLongitude();
		if(latitude >= mMovaMap.getWidth() || longitude >= mMovaMap.getHeight() || latitude < 0 || longitude < 0)
			return false;
		return true;
	}
    
    /**
     * Initializes the agents, items and sensors and places them randomly inside the domain
     * @return a vector of vectors of the entities where agents are the first vector,
     * items are the second vector and sensors are the third vector
     */
    private void manuallyInitializeLocations() {
        _entities.add(manuallyInitializeAgents());
        _entities.add(manuallyInitializeItems());
        _entities.add(manuallyInitializeSensorAgents());
    }
    private Vector<Entity> manuallyInitializeAgents(){
        Vector<Entity> agents = new Vector<Entity>();

//        Agent coordinatorAgent = new Agent(new AgentType("COORDINATOR"));
//        Agent securityOfficerAgent = new Agent(new AgentType("SECURITY_OFFICER"));
//        Agent secretaryAgent = new Agent(new AgentType("SECRETARY"));
//        Agent networkManagerAgent = new Agent(new AgentType("NETWORK_MANAGER"));
//        Agent SoundManagerAgent = new Agent(new AgentType("SOUND_MANAGER"));
        Agent coordinatorAgent = new Agent(new AgentType(""));
        Agent securityOfficerAgent = new Agent(new AgentType(""));
        Agent secretaryAgent = new Agent(new AgentType(""));
        Agent networkManagerAgent = new Agent(new AgentType(""));
        Agent SoundManagerAgent = new Agent(new AgentType(""));

        agents.add(coordinatorAgent);
        agents.add(securityOfficerAgent);
        agents.add(secretaryAgent);
        agents.add(networkManagerAgent);
        agents.add(SoundManagerAgent);

        coordinatorAgent.setRepresentation(coordinatorAgent.getType().toString());
        securityOfficerAgent.setRepresentation(securityOfficerAgent.getType().toString());
        secretaryAgent.setRepresentation(secretaryAgent.getType().toString());
        networkManagerAgent.setRepresentation(networkManagerAgent.getType().toString());
        SoundManagerAgent.setRepresentation(SoundManagerAgent.getType().toString());

        Location coordinatorAgentLocation = new Location(13, 12); _locations[13][12] = "";
        Location securitOfficerAgentLocation = new Location(9, 3); _locations[9][3] = "";
        Location secretaryAgentLocation = new Location(13, 27); _locations[13][27] = "";
        Location networkManagerAgentLocation = new Location(4, 14); _locations[4][14] = "";
        Location SoundManagerAgentLocation = new Location(10, 27); _locations[10][27] = "";

        coordinatorAgent.setRepLocation(coordinatorAgentLocation);
        securityOfficerAgent.setRepLocation(securitOfficerAgentLocation);
        secretaryAgent.setRepLocation(secretaryAgentLocation);
        networkManagerAgent.setRepLocation(networkManagerAgentLocation);
        SoundManagerAgent.setRepLocation(SoundManagerAgentLocation);

        _entitiesMap.put(coordinatorAgent, coordinatorAgentLocation);
        _entitiesMap.put(securityOfficerAgent , securitOfficerAgentLocation);
        _entitiesMap.put(secretaryAgent, secretaryAgentLocation);
        _entitiesMap.put(networkManagerAgent, networkManagerAgentLocation);
        _entitiesMap.put(SoundManagerAgent, SoundManagerAgentLocation);

        return agents;
    }

    private Vector<Entity> manuallyInitializeItems(){
            Vector<Entity> items = new Vector<Entity>();

            Item board = new Item(new ItemType("BOARD")); items.add(board);
            Item laptop = new Item(new ItemType("LAPTOP")); items.add(laptop);
            Item mouse = new Item(new ItemType("MOUSE")); items.add(mouse);
            Item cable = new Item(new ItemType("CABLE")); items.add(cable);
            Item speaker = new Item(new ItemType("SPEAKER")); items.add(speaker);
            Item stand = new Item(new ItemType("STAND")); items.add(stand);
            Item lazerCursor = new Item(new ItemType("LAZER_CURSOR")); items.add(lazerCursor);

            board.setRepresentation(board.getType().toString());
            laptop.setRepresentation(laptop.getType().toString());
            mouse.setRepresentation(mouse.getType().toString());
            cable.setRepresentation(cable.getType().toString());
            speaker.setRepresentation(speaker.getType().toString());
            stand.setRepresentation(stand.getType().toString());
            lazerCursor.setRepresentation(lazerCursor.getType().toString());

            Location boardLocation = new Location(12, 28);  _locations[12][28] = "available " + board.toString();
            Location laptopLocation = new Location(12, 26);  _locations[12][26] = "available " + laptop.toString();
            Location mouseLocation = new Location(13, 4);  _locations[13][4] = "available " + mouse.toString();
            Location cableLocation = new Location(12,13);  _locations[12][13] = "available " + cable.toString();
            Location speakerLocation = new Location(8,6);  _locations[8][6] = "available " + speaker.toString();
            Location standLocation = new Location(6, 3);  _locations[6][3] = "available " + stand.toString();
            Location lazerCursorLocation = new Location(14, 15);  _locations[14][15] = "available " + lazerCursor.toString();

            board.setRepLocation(boardLocation);
            laptop.setRepLocation(laptopLocation);
            mouse.setRepLocation(mouseLocation);
            cable.setRepLocation(cableLocation);
            speaker.setRepLocation(speakerLocation);
            stand.setRepLocation(standLocation);
            lazerCursor.setRepLocation(lazerCursorLocation);

		_entitiesMap.put(board, boardLocation);
                _entitiesMap.put(laptop, laptopLocation);
                _entitiesMap.put(mouse, mouseLocation);
                _entitiesMap.put(cable, cableLocation);
                _entitiesMap.put(speaker, speakerLocation);
                _entitiesMap.put(stand, standLocation);
                _entitiesMap.put(lazerCursor, lazerCursorLocation);

            return items;
    }
	private Vector<Entity> manuallyInitializeSensorAgents(){
		Vector<Entity> sensorAgents = new Vector<Entity>();

		SensorAgent rfidAgent1 = new SensorAgent(new AgentType("RFID"));
		SensorAgent rfidAgent2 = new SensorAgent(new AgentType("RFID"));
		SensorAgent rfidAgent3 = new SensorAgent(new AgentType("RFID"));
		SensorAgent rfidAgent4 = new SensorAgent(new AgentType("RFID"));

		sensorAgents.add(rfidAgent1);
		sensorAgents.add(rfidAgent2);
		sensorAgents.add(rfidAgent3);
		sensorAgents.add(rfidAgent4);

		rfidAgent1.setRepresentation("Sensor 1");
		rfidAgent2.setRepresentation("Sensor 2");
		rfidAgent3.setRepresentation("Sensor 3");
		rfidAgent4.setRepresentation("Sensor 4");

		Location rfidAgent1Location = new Location(10, 10); _locations[10][10] = "Sensor 1";
		Location rfidAgent2Location = new Location(11, 24); _locations[11][24] = "Sensor 2";
		Location rfidAgent3Location = new Location(8, 21); _locations[8][21] = "Sensor 3";
		Location rfidAgent4Location = new Location(3, 21); _locations[3][21] = "Sensor 4";

		rfidAgent1.setRepLocation(rfidAgent1Location);
		rfidAgent2.setRepLocation(rfidAgent2Location);
		rfidAgent3.setRepLocation(rfidAgent3Location);
		rfidAgent4.setRepLocation(rfidAgent4Location);

		_entitiesMap.put(rfidAgent1, rfidAgent1Location);
        _entitiesMap.put(rfidAgent2, rfidAgent2Location);
        _entitiesMap.put(rfidAgent3, rfidAgent3Location);
        _entitiesMap.put(rfidAgent4, rfidAgent4Location);

		Runnable is1 = new ItemScanner(rfidAgent1, this, _observers, _nScanDistance);
		_itemScanners.add(is1);
		//new Thread(is1).start();

		Runnable is2 = new ItemScanner(rfidAgent2, this, _observers, _nScanDistance);
		_itemScanners.add(is2);
		//new Thread(is2).start();

		Runnable is3 = new ItemScanner(rfidAgent3, this, _observers, _nScanDistance);
		_itemScanners.add(is3);
		//new Thread(is3).start();

		Runnable is4 = new ItemScanner(rfidAgent4, this, _observers, _nScanDistance);
		_itemScanners.add(is4);
		//new Thread(is4).start();

		return sensorAgents;
	}

    private void initializeLocations(int height, int width) {
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                _locations[i][j] = "";
            }
        }
    }
    
    public Vector<Runnable> getItemScanners(){
    	return _itemScanners;
    }

    // Overridden Methods
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int columnIndex) {
        return String.class;
      }

    @SuppressWarnings("unchecked")
	@Override
    public void setValueAt(Object aValue, int row, int column) {
        @SuppressWarnings("rawtypes")
		Vector rowVector = (Vector)dataVector.elementAt(row);
        rowVector.setElementAt(aValue, column);
        fireTableCellUpdated(row, column);
    }

    private void initializeDataVector(int width) {
        String[] names = new String[width];
        for(int i = 0; i < names.length; i++)
            names[i] = String.valueOf(i);
        setDataVector(_locations,names );
    }
    /**
     * Updates an entity's representation in the domain
     * @param entity the entity to update
     */
    public synchronized void updateEntityLocation(Entity entity){
        Location oldLocation = entity.getLastRepLocation();
        Location newLocation = entity.getLocation();
        if(!newLocation.equals(oldLocation) && oldLocation != null && !entity.updated()){
        	String oldLocationRep = getValueAt(oldLocation.getLongitude(), oldLocation.getLatitude()).toString();
            String newLocationRep = getValueAt(newLocation.getLongitude(), newLocation.getLatitude()).toString();

            if(newLocationRep.equals("")){
                setValueAt(entity.toString(), newLocation.getLongitude(), newLocation.getLatitude());
            }
            else{
                newLocationRep = newLocationRep.concat("\n" + entity.toString());
                setValueAt(newLocationRep, newLocation.getLongitude(), newLocation.getLatitude());
            }
            if(oldLocation != null){
                String[] entitiesRep = oldLocationRep.split("\n");
                if(entitiesRep.length == 1){
                        setValueAt("", oldLocation.getLongitude(), oldLocation.getLatitude());
                }
                else{
                    String newRep = "";
                    for (int i = 0; i < entitiesRep.length - 1; i++) {
                        if(!entitiesRep[i].equals(entity.toString()))
                            newRep = newRep.concat(entitiesRep[i] + "\n");
                    }
                    if(!entitiesRep[entitiesRep.length - 1].equals(entity.toString()))
                        newRep = newRep.concat(entitiesRep[entitiesRep.length - 1]);
                    else{
                        newRep = newRep.substring(0, newRep.length() - 1);
                    }
                    setValueAt(newRep, oldLocation.getLongitude(), oldLocation.getLatitude());
                }
            }
            entity.update();
            //fireTableDataChanged();
        }
    }
    /**
     * Adds a new message to the message board in the domain
     * @param message the message to add
     */
    public void addMessage(String message){
    	message = message.concat("\n");
    	for (Observer ob : _observers) {
            ob.update(null, message);
        }
    }
}
