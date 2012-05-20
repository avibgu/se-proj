/**
 * 
 */
package db;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import actor.Activity;
import actor.Agent;
import actor.Item;

import state.ActivityState;
import state.ItemState;
import type.ActivityType;
import type.AgentType;
import type.ItemType;
import utilities.Location;

/**
 * @author Shai Cantor
 *
 */
public class DBHandlerTest1 {
	
	private DBHandler _db;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		_db = DBHandler.getInstance();
	}

	/**
	 * Test method for {@link db.DBHandler#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		_db = DBHandler.getInstance();
		assertNotNull(_db);
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAgentType(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgentType() {
		_db.insertAgentType("COORDINATOR");
		_db.deleteAgentType("COORDINATOR");
	}

	@Test
	public void testGetAgentTypes() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgentType("SOUND_MANAGER");
		Vector<String> types = _db.getAgentTypes();
		assertEquals("COORDINATOR", types.elementAt(0));
		assertEquals("SOUND_MANAGER", types.elementAt(1));
		_db.deleteAgentType("COORDINATOR");
		_db.deleteAgentType("SOUND_MANAGER");
	}
	
	/**
	 * Test method for {@link db.DBHandler#deleteAgent(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgent() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		_db.deleteAgent("20");
		Vector<String> agentIds = _db.getAgentIds();
		assertEquals(0, agentIds.size());
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentIds()}.
	 */
	@Test
	public void testGetAgentIds() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR").toString(), true, "546");
		Vector<String> ids = _db.getAgentIds();
		assertEquals(2, ids.size());
		String registrationId1 = _db.getAgentRegistrationId("20");
		String registrationId2 = _db.getAgentRegistrationId("21");
		assertEquals("345345", registrationId1);
		assertEquals("546", registrationId2);
		_db.deleteAgent("20");
		_db.deleteAgent("21");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentActivityId(java.lang.String)}.
	 */
	@Test
	public void testGetAgentActivityId() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		_db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		_db.insertActivity(ac);
		_db.changeAgentActivityId("20", ac.getId());
		String activityId = _db.getAgentActivityId("20");
		assertEquals(ac.getId(), activityId);
		_db.deleteAgent("20");
		_db.deleteActivity(ac.getId());
		_db.deleteActivityType("BLA");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentStatus(java.lang.String)}.
	 */
	@Test
	public void testGetAgentStatus() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		boolean status = _db.getAgentStatus("20");
		assertEquals(true, status);
		_db.changeAgentStatus("20", false);
		status = _db.getAgentStatus("20");
		assertEquals(false, status);
		_db.deleteAgent("20");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentRegistrationId(java.lang.String)}.
	 */
	@Test
	public void testGetAgentRegistrationId() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		String regId = _db.getAgentRegistrationId("20");
		assertEquals("345345", regId);
		_db.deleteAgent("20");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentActivityId(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testChangeAgentActivityId() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		_db.changeAgentActivityId("20", "1111");
		String activityId1 = _db.getAgentActivityId("20");
		assertEquals("1111", activityId1);
		_db.deleteAgent("20");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentRegistrationID(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testChangeAgentRegistrationID() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		_db.changeAgentRegistrationID("20", "3333");
		String regId = _db.getAgentRegistrationId("20");
		assertEquals("3333", regId);
		_db.deleteAgent("20");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAllAgents()}.
	 */
	@Test
	public void testDeleteAllAgents() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR").toString(), true, "546");
		_db.deleteAllAgents();
		Vector<String> ids = _db.getAgentIds();
		assertEquals(0, ids.size());
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityType(java.lang.String)}.
	 */
	@Test
	public void testInsertActivityType() {
		_db.insertActivityType("BLA");
		Vector<String> activityTypes = _db.getActivityTypes();
		assertEquals(1, activityTypes.size());
		_db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityType(java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityType() {
		_db.insertActivityType("BLA");
		Vector<String> activityTypes = _db.getActivityTypes();
		assertEquals(1, activityTypes.size());
		_db.deleteActivityType("BLA");
		activityTypes = _db.getActivityTypes();
		assertEquals(0, activityTypes.size());
		
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivity(actor.Activity)}.
	 */
	@Test
	public void testInsertActivity() {
		_db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		_db.insertActivity(ac);
		Vector<String> names = _db.getActivityNames();
		assertEquals(1, names.size());
				
		_db.insertAgentType("COORDINATOR");
		_db.insertItemType("Stand");
		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		requiredAgents.put(new AgentType("COORDINATOR"), 2);
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		requiredItems.put(new ItemType("Stand"), 1);
		Set<String> acs = new HashSet<String>();
		acs.add(ac.getId());
		Timestamp pStartTime = new Timestamp(new Date().getTime());
		Timestamp pEndTime = new Timestamp(pStartTime.getTime() + (1000 * 60 * 60));
		long pEstimateTime = 1000 * 60 * 60;
		String pDescription = "desc";
		String pName = "name";
		Activity ac2 = new Activity("BLA", pStartTime, pEndTime, pEstimateTime, 
				requiredAgents, requiredItems, acs, pDescription, pName); 
		_db.insertActivity(ac2);
		
		names = _db.getActivityNames();
		assertEquals(2, names.size());
		
		_db.deleteActivity(ac.getId());
		_db.deleteActivity(ac2.getId());
		_db.deleteActivityType("BLA");
		_db.deleteAgentType("COORDINATOR");
		_db.deleteItemType("Stand");
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityDeadline(java.lang.String, java.sql.Timestamp)}.
	 */
	@Test
	public void testUpdateActivityDeadline() {
		_db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		Timestamp ts = ac.getEndTime();
		ac.setEndTime(new Timestamp(ac.getEndTime().getTime() + (1000 * 60 * 5)));
		_db.insertActivity(ac);
		Timestamp ts2 = _db.getActivityDeadline(ac.getId());
		assertEquals(ts.getTime()+ (1000 * 60 * 5), ts2.getTime());
		_db.deleteActivity(ac.getId());
		_db.deleteActivityType("BLA");
	}
	
	/**
	 * Test method for {@link db.DBHandler#updateActivityState(String, String)}.
	 */
	@Test
	public void testUpdateActivityState(){
		_db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		ac.setState(ActivityState.PENDING);
		_db.insertActivity(ac);
		assertEquals(ActivityState.PENDING, _db.getActivityState(ac.getId()));
		_db.updateActivityState(ac.getId(), ActivityState.IN_PROGRESS.toString());
		assertEquals(ActivityState.IN_PROGRESS, _db.getActivityState(ac.getId()));
		
		_db.deleteActivity(ac.getId());
		_db.deleteActivityType("BLA");
	}
	/**
	 * Test method for {@link db.DBHandler#getActivityNames()}
	 */
	@Test
	public void testGetActivityNames() {
		_db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setName("Hello");
		ac.setType("BLA");
		_db.insertActivity(ac);
		Vector<String> names = _db.getActivityNames();
		assertEquals(1, names.size());
		assertEquals("Hello", names.elementAt(0));
		_db.deleteActivity(ac.getId());
		_db.deleteActivityType("BLA");
	}
	
	/**
	 * Test method for {@link db.DBHandler#insertItem(actor.Item)}.
	 */
	@Test
	public void testInsertItem() {
		_db.insertItemType("Stand");
		Item item = new Item(new ItemType("Stand"));
		_db.insertItem(item);
		Vector<String> itemIds = _db.getItemIds();
		assertEquals(1, itemIds.size());
		_db.deleteItem(item.getId());
		_db.deleteItemType("Stand");
	}

	/**
	 * Test method for {@link db.DBHandler#updateItemState(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateItemState() {
		_db.insertItemType("Stand");
		Item item = new Item(new ItemType("Stand"));
		_db.insertItem(item);
		assertEquals(ItemState.AVAILABLE, item.getState());
		_db.updateItemState(item.getId(), ItemState.BUSY.toString());
		assertEquals(ItemState.BUSY, _db.getItemState(item.getId()));
		_db.deleteItem(item.getId());
		_db.deleteItemType("Stand");
	}

	/**
	 * Test method for {@link db.DBHandler#setItemHolder(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSetItemHolder() {
		_db.insertItemType("Stand");
		Item item = new Item(new ItemType("Stand"));
		_db.insertItem(item);
		
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		
		_db.setItemHolder(item.getId(), "20");
		String agentID = _db.getItemHolder(item.getId());
		assertEquals("20", agentID);
		
		_db.deleteItem(item.getId());
		_db.deleteItemType("Stand");
		_db.deleteAgent("20");
		_db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#insertItemType(java.lang.String)}.
	 */
	@Test
	public void testInsertItemType() {
		_db.insertItemType("Stand");
		Vector<String> itemTypes = _db.getItemTypes();
		assertEquals(1, itemTypes.size());
		_db.deleteItemType("Stand");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteItemType(java.lang.String)}.
	 */
	@Test
	public void testDeleteItemType() {
		_db.insertItemType("Stand");
		Vector<String> itemTypes = _db.getItemTypes();
		assertEquals(1, itemTypes.size());
		_db.deleteItemType("Stand");
		itemTypes = _db.getItemTypes();
		assertEquals(0, itemTypes.size());
	}
	
	/**
	 * Test method for {@link db.DBHandler#getActivityAgentIds(String)}.
	 */
	@Test
	public void testGetActivityAgentIds(){
		Activity ac = new Activity("Hey");
		ac.setType("BLA");
		_db.insertActivityType("BLA");
		_db.insertActivity(ac);
		
		AgentType type = new AgentType("COORDINATOR");
		Agent agent1 = new Agent(type);
		Agent agent2 = new Agent(type);
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent(agent1.getId(), agent1.getType().toString(), true, "1212");
		_db.insertAgent(agent2.getId(), agent2.getType().toString(), true, "1313");
		
		_db.insertActivityAgent(ac.getId(), agent1.getId());
		_db.insertActivityAgent(ac.getId(), agent2.getId());
		
		Vector<String> agentIds = _db.getActivityAgentIds(ac.getId());
		
		assertEquals(2, agentIds.size());
		
		_db.deleteActivity(ac.getId());
		_db.deleteActivityType(ac.getType());
		
		_db.deleteAgent(agent1.getId());
		_db.deleteAgent(agent2.getId());
		_db.deleteAgentType(type.toString());
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityItemIds(String)}.
	 */
	@Test
	public void testGetActivityItemIds(){
		Activity ac = new Activity("Hey");
		ac.setType("BLA");
		_db.insertActivityType("BLA");
		_db.insertActivity(ac);
		
		ItemType type = new ItemType("STAND");
		Item item1 = new Item(type);
		Item item2 = new Item(type);
		_db.insertItemType(type.toString());
		_db.insertItem(item1);
		_db.insertItem(item2);
		
		_db.insertActivityItem(ac.getId(), item1.getId());
		_db.insertActivityItem(ac.getId(), item2.getId());
		
		Vector<String> itemIds = _db.getActivityItemIds(ac.getId());
		
		assertEquals(2, itemIds.size());
		
		_db.deleteActivity(ac.getId());
		_db.deleteActivityType(ac.getType());
		
		_db.deleteItem(item1.getId());
		_db.deleteItem(item2.getId());
		_db.deleteItemType(type.toString());
	}
	
	/**
	 * Test method for {@link db.DBHandler#updateAgentLocation(String, utilities.Location)}.
	 */
	@Test
	public void testUpdateAgentLocation(){
		AgentType type = new AgentType("COORDINATOR");
		Agent agent1 = new Agent(type);
		Location location = new Location(3, 4);
		agent1.setLocation(location);
		_db.insertAgentType(type.toString());
		_db.insertAgent(agent1.getId(), agent1.getType().toString(), true, "1212");
		
		_db.insertAgentLocation(agent1.getId(), location);
		assertEquals(4, _db.getAgentLocation(agent1.getId()).getLatitude());
		assertEquals(3, _db.getAgentLocation(agent1.getId()).getLongitude());
		
		_db.updateAgentLocation(agent1.getId(), new Location(1, 1));
		assertEquals(1, _db.getAgentLocation(agent1.getId()).getLatitude());
		assertEquals(1, _db.getAgentLocation(agent1.getId()).getLongitude());
		_db.deleteAgentLocation(agent1.getId());
		
		_db.deleteAgent(agent1.getId());
		_db.deleteAgentType(type.toString());
	}
	
	/**
	 * Test method for {@link db.DBHandler#deleteAgentLocation(String)}.
	 */
	@Test
	public void testDeleteAgentLocation(){
		AgentType type = new AgentType("COORDINATOR");
		Agent agent1 = new Agent(type);
		Location location = new Location(3, 3);
		agent1.setLocation(location);
		_db.insertAgentType(type.toString());
		_db.insertAgent(agent1.getId(), agent1.getType().toString(), true, "1212");
		
		_db.insertAgentLocation(agent1.getId(), location);
		assertNotNull(_db.getAgentLocation(agent1.getId()));
		_db.deleteAgentLocation(agent1.getId());
		assertNull(_db.getAgentLocation(agent1.getId()));
		
		_db.deleteAgent(agent1.getId());
		_db.deleteAgentType(type.toString());
	}
	
	/**
	 * Test method for {@link db.DBHandler#updateItemLocation(String, Location)}.
	 */
	@Test
	public void testUpdateItemLocation(){
		ItemType type = new ItemType("STAND");
		Item item1 = new Item(type);
		Location location = new Location(3, 4);
		item1.setLocation(location);
		_db.insertItemType(type.toString());
		_db.insertItem(item1);
		
		_db.insertItemLocation(item1.getId(), location);
		assertEquals(4, _db.getItemLocation(item1.getId()).getLatitude());
		assertEquals(3, _db.getItemLocation(item1.getId()).getLongitude());
		
		_db.updateItemLocation(item1.getId(), new Location(1, 1));
		assertEquals(1, _db.getItemLocation(item1.getId()).getLatitude());
		assertEquals(1, _db.getItemLocation(item1.getId()).getLongitude());
		_db.deleteItemLocation(item1.getId());
		
		_db.deleteItem(item1.getId());
		_db.deleteItemType(type.toString());
	}
	
	/**
	 * Test method for {@link db.DBHandler#deleteItemLocation(String)}.
	 */
	@Test
	public void testDeleteItemLocation(){
		ItemType type = new ItemType("STAND");
		Item item1 = new Item(type);
		Location location = new Location(3, 4);
		item1.setLocation(location);
		_db.insertItemType(type.toString());
		_db.insertItem(item1);
		
		_db.insertItemLocation(item1.getId(), location);
		assertNotNull(_db.getItemLocation(item1.getId()));
		_db.deleteItemLocation(item1.getId());
		assertNull(_db.getItemLocation(item1.getId()));
		
		_db.deleteItem(item1.getId());
		_db.deleteItemType(type.toString());
	}
	
	/**
	 * Test method for {@link db.DBHandler#getAgentSchedule(String)}.
	 */
	@Test
	public void testGetAgentSchedule(){
		AgentType agentType = new AgentType("COORDINATOR");
		Agent agent = new Agent(agentType);
		_db.insertAgentType(agentType.toString());
		_db.insertAgent(agent.getId(), agent.getType().toString(), true, "1212");
		
		ActivityType activityType1 = new ActivityType("Lunch");
		ActivityType activityType2 = new ActivityType("Presentation");
		Activity activity1 = new Activity("Hey1");
		activity1.setType(activityType1.getType());
		Set<String> participatingAgentIds1 = new HashSet<String>();
		participatingAgentIds1.add(agent.getId());
		activity1.setParticipatingAgentIds(participatingAgentIds1);
		
		Activity activity2 = new Activity("Hey2");
		activity2.setType(activityType2.getType());
		Set<String> participatingAgentIds2 = new HashSet<String>();
		participatingAgentIds2.add(agent.getId());
		activity2.setParticipatingAgentIds(participatingAgentIds2);

		_db.insertActivityType(activityType1.getType());
		_db.insertActivityType(activityType2.getType());
		_db.insertActivity(activity1);
		_db.insertActivity(activity2);
		
		Vector<Activity> agentActivities = _db.getAgentSchedule(agent.getId());
		assertEquals(2, agentActivities.size());
		
		Activity serverActivity = agentActivities.elementAt(0);
		assertEquals(activity1.getId(), serverActivity.getId());
		assertEquals(activity1.getName(), serverActivity.getName());
		assertEquals(activity1.getDescription(), serverActivity.getDescription());
		assertEquals(activity1.getType(), serverActivity.getType());
		assertEquals(activity1.getState(), serverActivity.getState());
		assertEquals(activity1.getStartTime(), serverActivity.getStartTime());
		assertEquals(activity1.getEndTime(), serverActivity.getEndTime());
		assertEquals(activity1.getEstimateTime(), serverActivity.getEstimateTime());
		assertArrayEquals(activity1.getParticipatingAgentIds().toArray(), serverActivity.getParticipatingAgentIds().toArray());
		assertArrayEquals(activity1.getParticipatingItemIds().toArray(), serverActivity.getParticipatingItemIds().toArray());
		
		_db.deleteActivity(activity1.getId());
		_db.deleteActivity(activity2.getId());
		_db.deleteActivityType(activityType1.getType());
		_db.deleteActivityType(activityType2.getType());
		_db.deleteAgent(agent.getId());
		_db.deleteAgentType(agent.getType().toString());
	}

}
