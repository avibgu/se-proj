package simulator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import c2dm.C2dmController;

import db.DBHandler;

import state.ActivityState;
import state.ItemState;
import type.MessageType;
import utilities.Location;
import utilities.MovaJson;

import actor.Activity;
import actor.Agent;
import actor.Entity;
import actor.Item;

/**
 * The singleton simulator is reference by the server's resources
 * It is used to query the database and present changes and activities 
 * in the simulated world
 */
public class Simulator {

	private static NewDomain _domain;
	private static Vector<Entity> _agents;
	private static Vector<Activity> _activities;
	private static Vector<Entity> _items;
	private static Vector<Entity> _sensorAgents;
	private static Vector<Vector<Entity>> _entities;
	private static Map<String, Agent> mAgents;
	private static Map<String, Item> mItems;
	private static Simulator mSimulator = null;
	private static DBHandler db;
	private static int agentIndex = 0;
	private static DateFormat format = new SimpleDateFormat("HH:mm");
	private static Date date = new Date();
	private static Vector<Integer> _agentIndexes;
	
	private Simulator(){
		_entities = _domain.getEntities();
		_agents = _entities.elementAt(0);
		_items = _entities.elementAt(1);
		_sensorAgents = _entities.elementAt(2);
		mAgents = new HashMap<String, Agent>();
		mItems = new HashMap<String, Item>();
		db = DBHandler.getInstance();
		_agentIndexes = new Vector<Integer>();
		for (Entity item : _items) {
			//db.insertItemType(((Item) item).getType().toString());
			//db.insertItem((Item) item);
			mItems.put(item.getId(), (Item) item);
		}
		//db.insertInitialData();
	}
	/**
	 * Returns the only instance of the simulator
	 * @param domain the domain of the simulator. Used only for the first call.
	 * @return a new Simulator object if not created, the current simulator if was created 
	 */
	public synchronized static Simulator getInstance(NewDomain domain){
		if (mSimulator == null)
		{
			_domain = domain;
			mSimulator = new Simulator();
		}
		
		return mSimulator;
	}
	
	public void start() {
		for (Runnable itemScanner : _domain.getItemScanners()) {
			new Thread(itemScanner).start();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<Location> locations = new ArrayList<Location>();
				locations.add(new Location(11,11));
				locations.add(new Location(7,7));
				locations.add(new Location(8,8));
				locations.add(new Location(9,9));
				Item item = (Item) _items.elementAt(0);
				int index = 0;
				while(true){
					Location l = locations.get(index);
					item.setLocation(l);
					db.updateItemLocation(item.getId(), l);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("Item changing location thread was interrupted");
					}
					index++;
					index = index % locations.size();
				}
				
			}
		}).start();
	}
	/**
	 * Registers a new agent in the simulator and presents it in the virtual world
	 * @param pId the id of the new agent
	 */
	public void registerAgentMessage(String pId){
		/*if(agentIndex < _agents.size()){
			Agent agent = (Agent) _agents.elementAt(agentIndex);
			String type = db.getAgentType(pId);
			agent.setId(pId);
			agent.setRepresentation(type);
			db.insertAgentLocation(pId, agent.getLocation());
			mAgents.put(pId, agent);
			
			String message = "An new " + type + " agent registered to the system at " + format.format(date); 
			_domain.addMessage(message);
			_domain.setValueAt(agent.toString(), agent.getLocation().getLongitude(), agent.getLocation().getLatitude());
			
			agentIndex++;
		}*/
		
		if(!_agents.isEmpty()){
			Agent agent = (Agent) _agents.remove(0);
			String type = db.getAgentType(pId);
			agent.setId(pId);
			agent.setRepresentation(type);
			db.insertAgentLocation(pId, agent.getLocation());
			mAgents.put(pId, agent);
			
			String message = "An new " + type + " agent registered to the system at " + format.format(date); 
			_domain.addMessage(message);
			_domain.setValueAt(agent.toString(), agent.getLocation().getLongitude(), agent.getLocation().getLatitude());
		}
	}
	
	
	public void deleteAgent(String pAgentId){
		Agent agent = mAgents.remove(pAgentId);
		String type = db.getAgentType(pAgentId);
		_agents.add(agent);
		db.deleteAgentLocation(pAgentId);
		
		String message = "The " + type + " agent was deleted from the system at " + format.format(date); 
		_domain.addMessage(message);
		_domain.setValueAt("", agent.getLocation().getLongitude(), agent.getLocation().getLatitude());
		db.deleteAgent(pAgentId);
	}
	
	/**
	 * Changes the activity in the simulator and adds a new relevant message log
	 * @param pId the id of the activity
	 * @param pState the new state of the activity
	 */
	public void changeActivityStatusMessage(String pId, ActivityState pState){
		String name = db.getActivityName(pId);
		String message = "The activity \"" + name + "\"" + " status changed to " + pState;
		_domain.addMessage(message);
		//changeAgentLocationMessage(((Agent) _agents.elementAt(agentIndex-1)).getId(), new Location(2, 24));
	}
	/**
	 * Creates a new activity in the simulator and adds a new relevant message log
	 * @param pActivity the activity to add
	 */
	public void addActivityMessage(Activity pActivity){
		String message = "A new activity \"" + pActivity.getName() + "\"" + " was created at " + format.format(date);
		_domain.addMessage(message);
	}
	/**
	 * Creates a new relevant message log of the postponed activity
	 * @param activityId the activity id of the postponed Activity
	 * @param newEndTime the new end time of the activity
	 */
	public void postoneActivityMessage(String activityId, String newEndTime){
		String name = db.getActivityName(activityId);
		String message = "The activity \"" + name + "\"" + " was postponed to " + newEndTime;
		_domain.addMessage(message);
	}
	/**
	 * Simulates the movement of the agent to the new location. Adds a new relevant message log
	 * @param pId the agent id
	 * @param pNewLocation the new location of the agent
	 */
	public void changeAgentLocationMessage(String pId, Location pNewLocation){
		Location oldLocation = db.getAgentLocation(pId);
		Vector<Item> items = db.getAgentItems(pId);
		Vector<Item> simulatedItems = new Vector<Item>();

		for (Item item1 : items) {
			if(mItems.containsKey(item1.getId()))
				simulatedItems.add(mItems.get(item1.getId()));
		}
		
		Agent agent = mAgents.get(pId);
		//agent.setMyItems(simulatedItems);
		_domain.walkAgent(agent, oldLocation, pNewLocation);
		db.updateAgentLocation(pId, pNewLocation);
	}
	/**
	 * Creates a new relevant message log of the agent's status change
	 * @param agentId the agent id
	 * @param newStatus the new agent status
	 */
	public void changeAgentStatus(String agentId, String newStatus){
		String agentType = db.getAgentType(agentId);
		String message = "The " + agentType + " agent changed its status to " + newStatus;
		_domain.addMessage(message);
	}
	/**
	 * Updates the item's representation in the simulated world according to the new status
	 * @param itemId the item id
	 * @param newStatus the new status of the item
	 * @param agentId the id of the agent that changed the item's status
	 */
	public void addItemStateMessage(String itemId, String newStatus, String agentId) {
		Item item = mItems.get(itemId);
		String message = "";
		if(agentId.equals("")){
			message = "The state of Item " + item.getType().toString()
					+ " was changed to " + newStatus + " at "
					+ format.format(date);
		}
		else
		{
			String agentType = db.getAgentType(agentId);
			message = "The state of Item " + item.getType().toString()
					+ " was changed to " + newStatus + " by the " + agentType + " Agent at "
					+ format.format(date);
		}
		_domain.addMessage(message);
	}
	
	public void updateItemState(String itemId, String newStatus, String agentId){
		Item item = mItems.get(itemId);
		ItemState newState = ItemState.valueOf(newStatus);
		Location itemLocation = item.getLocation();
		String stringItem = (String) _domain.getValueAt(itemLocation.getLongitude(), itemLocation.getLatitude());
		switch (newState) {
		case AVAILABLE:
			if(stringItem.contains("disabled")){
				stringItem = stringItem.substring(9, stringItem.length());
				_domain.setValueAt("available " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			else if(stringItem.contains("busy")){
				stringItem = stringItem.substring(5, stringItem.length());
				_domain.setValueAt("available " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			else{
				_domain.setValueAt(stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			
			//_domain.setValueAt("disabled " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			break;
			
		case BUSY:
			if(stringItem.contains("disabled")){
				stringItem = stringItem.substring(9, stringItem.length());
				_domain.setValueAt("busy " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			else if(stringItem.contains("available ")){
				stringItem = stringItem.substring(10, stringItem.length());
				_domain.setValueAt("busy " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			else{
				_domain.setValueAt(stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			break;
		case UNAVAILABLE:
			if(stringItem.contains("busy")){
				stringItem = stringItem.substring(5, stringItem.length());
				_domain.setValueAt("disabled " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			else if(stringItem.contains("available ")){
				stringItem = stringItem.substring(10, stringItem.length());
				_domain.setValueAt("disabled " + stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			else{
				_domain.setValueAt(stringItem, itemLocation.getLongitude(), itemLocation.getLatitude());
			}
			break;
		}
			addItemStateMessage(itemId, newStatus, "");
	}
	
	public void updateItemState(String itemId){
		ItemState state = db.getItemState(itemId);
		switch (state) {
		case AVAILABLE:
			state = ItemState.UNAVAILABLE;
			break;
		case UNAVAILABLE:
			state = ItemState.BUSY;
			break;
		case BUSY:
			state = ItemState.AVAILABLE;
			break;
		}
		db.updateItemState(itemId, state.toString(), "");
		updateItemState(itemId, state.toString(), "");
	}
	
	public static void deleteSimulator(){
		mSimulator = null;
	}

	public void distributeItems(Map<String, Item> changedLocationItems){
		
		String items = new MovaJson().itemsToJson(changedLocationItems.values());
		C2dmController.getInstance().sendMessageToDevice("3", items,null,MessageType.DISTRIBUTE_ITEM_LOCATION);
	}
}
