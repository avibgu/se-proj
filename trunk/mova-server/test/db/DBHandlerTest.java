/**
 * 
 */
package db;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
 * @author Shai
 *
 */
public class DBHandlerTest {
	
	private DBHandler db;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		db = DBHandler.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link db.DBHandler#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		db = DBHandler.getInstance();
		assertNotNull(db);
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAgentType(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgentType() {
		db.insertAgentType("COORDINATOR");
		Vector<String> types = db.getAgentTypes();
		assertTrue(types.contains("COORDINATOR"));
		db.deleteAgentType("COORDINATOR");
		types = db.getAgentTypes();
		assertFalse(types.contains("COORDINATOR"));
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentTypes()}.
	 */
	@Test
	public void testGetAgentTypes() {
		db.insertAgentType("COORDINATOR");
		db.insertAgentType("SOUND_MANAGER");
		Vector<String> types = db.getAgentTypes();
		assertTrue(types.contains("COORDINATOR"));
		assertTrue(types.contains("SOUND_MANAGER"));
		db.deleteAgentType("COORDINATOR");
		db.deleteAgentType("SOUND_MANAGER");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAgent(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgent() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", AgentType.COORDINATOR, true, "345345");
		Vector<String> ids = db.getAgentIds();
		assertTrue(ids.contains("20"));
		db.deleteAgent("20");
		ids = db.getAgentIds();
		assertFalse(ids.contains("20"));
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentIds()}.
	 */
	@Test
	public void testGetAgentIds() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		db.insertAgent("21", new AgentType("COORDINATOR").toString(), true, "546");
		Vector<String> ids = db.getAgentIds();
		assertTrue(ids.contains("20"));
		assertTrue(ids.contains("21"));
		db.deleteAgent("20");
		db.deleteAgent("21");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentActivityId(java.lang.String)}.
	 */
	@Test
	public void testGetAgentActivityId() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		db.insertActivity(ac);
		db.changeAgentActivityId("20", ac.getId());
		String activityId = db.getAgentActivityId("20");
		assertEquals(ac.getId(), activityId);
		db.deleteAgent("20");
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentStatus(java.lang.String)}.
	 */
	@Test
	public void testGetAgentStatus() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		boolean status = db.getAgentStatus("20");
		assertEquals(true, status);
		db.changeAgentStatus("20", false);
		status = db.getAgentStatus("20");
		assertEquals(false, status);
		db.deleteAgent("20");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentRegistrationId(java.lang.String)}.
	 */
	@Test
	public void testGetAgentRegistrationId() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		String regId = db.getAgentRegistrationId("20");
		assertEquals("345345", regId);
		db.deleteAgent("20");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentType(java.lang.String)}.
	 */
	@Test
	public void testGetAgentType() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		String type = db.getAgentType("20");
		assertEquals("COORDINATOR", type);
		db.deleteAgent("20");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentStatus(java.lang.String, java.lang.Boolean)}.
	 */
	@Test
	public void testChangeAgentStatus() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		assertTrue(db.getAgentStatus("20"));
		db.changeAgentStatus("20", false);
		assertFalse(db.getAgentStatus("20"));
		db.deleteAgent("20");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentActivityId(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testChangeAgentActivityId() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		db.changeAgentActivityId("20", "1111");
		String activityId1 = db.getAgentActivityId("20");
		assertEquals("1111", activityId1);
		db.deleteAgent("20");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentRegistrationID(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testChangeAgentRegistrationID() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		db.changeAgentRegistrationID("20", "3333");
		String regId = db.getAgentRegistrationId("20");
		assertEquals("3333", regId);
		db.deleteAgent("20");
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAllAgents()}.
	 */
	@Test
	public void testDeleteAllAgents() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		db.insertAgent("21", new AgentType("COORDINATOR").toString(), true, "546");
		db.deleteAllAgents();
		Vector<String> ids = db.getAgentIds();
		assertEquals(0, ids.size());
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#getAllAgents()}.
	 */
	@Test
	public void testGetAllAgents() {
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		db.insertAgent("21", new AgentType("COORDINATOR").toString(), true, "546");
		List<Agent> agents = db.getAllAgents();
		assertEquals("20", agents.get(0).getId());
		assertEquals("COORDINATOR", agents.get(0).getType().getType());
		assertEquals("345345", agents.get(0).getRegistrationId());
		assertEquals("21", agents.get(1).getId());
		assertEquals("COORDINATOR", agents.get(1).getType().getType());
		assertEquals("546", agents.get(1).getRegistrationId());
		db.deleteAllAgents();
		db.deleteAgentType("COORDINATOR");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityType(java.lang.String)}.
	 */
	@Test
	public void testInsertActivityType() {
		db.insertActivityType("BLA");
		Vector<String> activityTypes = db.getActivityTypes();
		assertTrue(activityTypes.contains("BLA"));
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityType(java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityType() {
		db.insertActivityType("BLA");
		Vector<String> activityTypes = db.getActivityTypes();
		assertTrue(activityTypes.contains("BLA"));
		db.deleteActivityType("BLA");
		activityTypes = db.getActivityTypes();
		assertFalse(activityTypes.contains("BLA"));
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityTypes()}.
	 */
	@Test
	public void testGetActivityTypes() {
		db.insertActivityType("type1");
		db.insertActivityType("type2");
		Vector<String> activityTypes = db.getActivityTypes();
		assertTrue(activityTypes.contains("type1"));
		assertTrue(activityTypes.contains("type2"));
		db.deleteActivityType("type1");
		db.deleteActivityType("type2");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivity(actor.Activity)}.
	 */
	@Test
	public void testInsertActivity() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		db.insertActivity(ac);
		Vector<String> names = db.getActivityNames();
		assertTrue(names.contains("hey"));
				
		db.insertAgentType("COORDINATOR");
		db.insertItemType("Stand");
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
		db.insertActivity(ac2);
		
		names = db.getActivityNames();
		assertTrue(names.contains("name"));
		
		db.deleteActivity(ac.getId());
		db.deleteActivity(ac2.getId());
		db.deleteActivityType("BLA");
		db.deleteAgentType("COORDINATOR");
		db.deleteItemType("Stand");
		//db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityDeadline(java.lang.String, java.sql.Timestamp)}.
	 */
	@Test
	public void testUpdateActivityDeadline() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		db.insertActivity(ac);
		Timestamp ts2 = db.getActivityDeadline(ac.getId());
		long postponeBy = ts2.getTime() + 30*60*1000;
		db.updateActivityDeadline(ac.getId(), new Timestamp(postponeBy));
		ts2 = db.getActivityDeadline(ac.getId());
		
		assertEquals(postponeBy, ts2.getTime());
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityDuration(java.lang.String, long)}.
	 */
	@Test
	public void testUpdateActivityDuration() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		db.insertActivity(ac);
		db.updateActivityDuration(ac.getId(), 1000*60*60*2);
		
		assertEquals(1000*60*60*2, db.getActivityDuration(ac.getId()));
		
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityState(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateActivityState() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		ac.setState(ActivityState.PENDING);
		db.insertActivity(ac);
		assertEquals(ActivityState.PENDING, db.getActivityState(ac.getId()));
		db.updateActivityState(ac.getId(), ActivityState.IN_PROGRESS.toString());
		assertEquals(ActivityState.IN_PROGRESS, db.getActivityState(ac.getId()));
		
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityNames()}.
	 */
	@Test
	public void testGetActivityNames() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setName("Hello");
		ac.setType("BLA");
		db.insertActivity(ac);
		Vector<String> names = db.getActivityNames();
		assertTrue(names.contains("Hello"));
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityName(java.lang.String)}.
	 */
	@Test
	public void testGetActivityName() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		ac.setName("Hello");
		db.insertActivity(ac);
		assertEquals("Hello", db.getActivityName(ac.getId()));
		
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityTypeAgents(java.lang.String)}.
	 */
	@Test
	public void testGetActivityTypeAgents() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		db.insertActivity(ac);
		Vector<String> names = db.getActivityNames();
		assertTrue(names.contains("hey"));
				
		db.insertAgentType("COORDINATOR");
		db.insertItemType("Stand");
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
		db.insertActivity(ac2);
		
		Map<AgentType, Integer> activityTypeAgents = db.getActivityTypeAgents(ac2.getId());
		assertTrue(activityTypeAgents.containsKey(new AgentType("COORDINATOR")));
		Integer num = (Integer)activityTypeAgents.get(new AgentType("COORDINATOR"));
		assertEquals(2, num.intValue());

		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityTypeItems(java.lang.String)}.
	 */
	@Test
	public void testGetActivityTypeItems() {
		db.insertActivityType("BLA");
		Activity ac = new Activity("hey");
		ac.setType("BLA");
		db.insertActivity(ac);
		Vector<String> names = db.getActivityNames();
		assertTrue(names.contains("hey"));
				
		db.insertAgentType("COORDINATOR");
		db.insertItemType("Stand");
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
		db.insertActivity(ac2);
		
		Map<ItemType, Integer> activityTypeItems = db.getActivityTypeItems(ac2.getId());
		assertTrue(activityTypeItems.containsKey(new ItemType("Stand")));
		Integer num = (Integer)activityTypeItems.get(new ItemType("Stand"));
		assertEquals(1, num.intValue());

		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getAllActivities()}.
	 */
	@Test
	public void testGetAllActivities() {
		Activity a1 = new Activity("a1");
		a1.setType("Type1");
		Activity a2 = new Activity("a2");
		a2.setType("Type2");
		db.insertActivityType(a1.getType());
		db.insertActivityType(a2.getType());
		db.insertActivity(a1);
		db.insertActivity(a2);
		
		List<Activity> acs = db.getAllActivities();
		List<String> acsIds = new ArrayList<String>();
		for(Activity a : acs){
			acsIds.add(a.getId());
		}
		assertTrue(acsIds.contains(a1.getId()));
		assertTrue(acsIds.contains(a2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#insertItem(actor.Item)}.
	 */
	@Test
	public void testInsertItem() {
		db.insertItemType("Stand");
		Item item = new Item(new ItemType("Stand"));
		item.setLocation(new Location(11, 11));
		db.insertItem(item);
		Vector<String> itemIds = db.getItemIds();
		assertTrue(itemIds.contains(item.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#updateItemState(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateItemState() {
		Agent agent = new Agent(new AgentType("COORDINATOR"));
		db.insertAgentType(agent.getType().toString());
		db.insertAgent(agent.getId(), agent.getType().toString(), true, "1212");
		
		db.insertItemType("Stand");
		Item item = new Item(new ItemType("Stand"));
		item.setLocation(new Location(11, 11));
		db.insertItem(item);
		assertEquals(ItemState.AVAILABLE, item.getState());
		db.updateItemState(item.getId(), ItemState.BUSY.toString(), agent.getId());
		assertEquals(ItemState.BUSY, db.getItemState(item.getId()));
		assertEquals(agent.getId(), db.getItemHolder(item.getId()));
		db.updateItemState(item.getId(), ItemState.AVAILABLE.toString(), agent.getId());
		assertEquals(" ", db.getItemHolder(item.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getItemState(java.lang.String)}.
	 */
	@Test
	public void testGetItemState() {
		Item i1 = new Item(new ItemType("Stand"));
		i1.setLocation(new Location(11,11));
		
		db.insertItemType("Stand");
		db.insertItem(i1);
		
		assertEquals(i1.getState(), db.getItemState(i1.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#setItemHolder(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSetItemHolder() {
		db.insertItemType("Stand");
		Item item = new Item(new ItemType("Stand"));
		item.setLocation(new Location(11,11));
		db.insertItem(item);
		
		db.insertAgentType("COORDINATOR");
		db.insertAgent("20", new AgentType("COORDINATOR").toString(), true, "345345");
		
		db.setItemHolder(item.getId(), "20");
		String agentID = db.getItemHolder(item.getId());
		assertEquals("20", agentID);
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getItemIds()}.
	 */
	@Test
	public void testGetItemIds() {
		Item i1 = new Item(new ItemType("Stand"));
		i1.setLocation(new Location(11,11));
		Item i2 = new Item(new ItemType("Cable"));
		i2.setLocation(new Location(12,12));
		
		db.insertItemType("Stand");
		db.insertItemType("Cable");
		db.insertItem(i1);
		db.insertItem(i2);
		
		Vector<String> itemIds = db.getItemIds();

		assertTrue(itemIds.contains(i1.getId()));
		assertTrue(itemIds.contains(i2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentItems(java.lang.String)}.
	 */
	@Test
	public void testGetAgentItems() {
		Agent agent = new Agent(new AgentType("COORDINATOR"));
		db.insertAgentType(agent.getType().toString());
		db.insertAgent(agent.getId(), agent.getType().toString(), true, "1212");
		
		db.insertItemType("Stand");
		db.insertItemType("Laptop");
		Item item1 = new Item(new ItemType("Stand"));
		item1.setLocation(new Location(11, 11));
		Item item2 = new Item(new ItemType("Laptop"));
		item2.setLocation(new Location(12, 12));
		db.insertItem(item1);
		db.insertItem(item2);
		db.updateItemState(item1.getId(), ItemState.BUSY.toString(), agent.getId());
		db.updateItemState(item2.getId(), ItemState.BUSY.toString(), agent.getId());
		Vector<Item> agentItems = db.getAgentItems(agent.getId());
		Vector<String> agentItemsIds = new Vector<String>();
		for (Item item : agentItems) {
			agentItemsIds.add(item.getId());
		}

		assertTrue(agentItemsIds.contains(item1.getId()));
		assertTrue(agentItemsIds.contains(item2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getItems()}.
	 */
	@Test
	public void testGetItems() {
		Item i1 = new Item(new ItemType("Stand"));
		i1.setLocation(new Location(11,11));
		Item i2 = new Item(new ItemType("Cable"));
		i2.setLocation(new Location(12,12));
		
		db.insertItemType("Stand");
		db.insertItemType("Cable");
		db.insertItem(i1);
		db.insertItem(i2);
		
		Vector<Item> itemTypes = db.getItems();
		Vector<String> itemIds = new Vector<String>();
		for(Item item : itemTypes){
			itemIds.add(item.getId());
		}
		assertTrue(itemIds.contains(i1.getId()));
		assertTrue(itemIds.contains(i2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#insertItemType(java.lang.String)}.
	 */
	@Test
	public void testInsertItemType() {
		db.insertItemType("Stand");
		Vector<String> itemTypes = db.getItemTypes();
		assertTrue(itemTypes.contains("Stand"));
		db.deleteItemType("Stand");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteItemType(java.lang.String)}.
	 */
	@Test
	public void testDeleteItemType() {
		db.insertItemType("Stand");
		Vector<String> itemTypes = db.getItemTypes();
		assertTrue(itemTypes.contains("Stand"));
		db.deleteItemType("Stand");
		itemTypes = db.getItemTypes();
		assertFalse(itemTypes.contains("Stand"));
	}

	/**
	 * Test method for {@link db.DBHandler#getItemTypes()}.
	 */
	@Test
	public void testGetItemTypes() {
		db.insertItemType("Stand");
		db.insertItemType("Cable");
		
		Vector<String> itemTypes = db.getItemTypes();
		assertTrue(itemTypes.contains("Stand"));
		assertTrue(itemTypes.contains("Cable"));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityAgent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testInsertActivityAgent() {
		Activity ac = new Activity("Hey");
		ac.setType("BLA");
		db.insertActivityType("BLA");
		Agent agent = new Agent(new AgentType("COORDINATOR"));
		db.insertAgentType(agent.getType().toString());
		db.insertAgent(agent.getId(), agent.getType().toString(), true, "12");
		Set<String> participatingAgentIds = new HashSet<String>();
		participatingAgentIds.add(agent.getId());
		ac.setParticipatingAgentIds(participatingAgentIds);
		db.insertActivity(ac);
		Vector<String> agentIds = db.getActivityAgentIds(ac.getId());
		assertTrue(agentIds.contains(agent.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityAgent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityAgent() {
		Activity ac = new Activity("Hey");
		ac.setType("BLA");
		db.insertActivityType("BLA");
		Agent agent = new Agent(new AgentType("COORDINATOR"));
		db.insertAgentType(agent.getType().toString());
		db.insertAgent(agent.getId(), agent.getType().toString(), true, "12");
		Set<String> participatingAgentIds = new HashSet<String>();
		participatingAgentIds.add(agent.getId());
		ac.setParticipatingAgentIds(participatingAgentIds);
		db.insertActivity(ac);
		Vector<String> agentIds = db.getActivityAgentIds(ac.getId());
		assertTrue(agentIds.contains(agent.getId()));
		db.deleteActivityAgent(ac.getId(), agent.getId());
		agentIds = db.getActivityAgentIds(ac.getId());
		assertFalse(agentIds.contains(agent.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityAgentIds(java.lang.String)}.
	 */
	@Test
	public void testGetActivityAgentIds() {
		Activity ac = new Activity("Hey");
		ac.setType("BLA");
		db.insertActivityType("BLA");
		db.insertActivity(ac);
		
		AgentType type = new AgentType("COORDINATOR");
		Agent agent1 = new Agent(type);
		Agent agent2 = new Agent(type);
		db.insertAgentType("COORDINATOR");
		db.insertAgent(agent1.getId(), agent1.getType().toString(), true, "1212");
		db.insertAgent(agent2.getId(), agent2.getType().toString(), true, "1313");
		
		db.insertActivityAgent(ac.getId(), agent1.getId());
		db.insertActivityAgent(ac.getId(), agent2.getId());
		
		Vector<String> agentIds = db.getActivityAgentIds(ac.getId());
		
		assertTrue(agentIds.contains(agent1.getId()) && agentIds.contains(agent2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentSchedule(java.lang.String)}.
	 */
	@Test
	public void testGetAgentSchedule() {
		AgentType agentType = new AgentType("COORDINATOR");
		Agent agent = new Agent(agentType);
		db.insertAgentType(agentType.toString());
		db.insertAgent(agent.getId(), agent.getType().toString(), true, "1212");
		
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

		db.insertActivityType(activityType1.getType());
		db.insertActivityType(activityType2.getType());
		db.insertActivity(activity1);
		db.insertActivity(activity2);
		
		Vector<Activity> agentActivities = db.getAgentSchedule(agent.getId());
		Vector<String> agentActivitiesIds = new Vector<String>();
		for (Activity activity : agentActivities) {
			agentActivitiesIds.add(activity.getId());
		}
		assertTrue(agentActivitiesIds.contains(activity1.getId()) && agentActivitiesIds.contains(activity2.getId()));
		
		int index = agentActivitiesIds.indexOf(activity1.getId());
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(activity1.getStartTime().getTime()));
		Activity serverActivity = agentActivities.elementAt(index);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(serverActivity.getStartTime().getTime()));

		assertEquals(activity1.getId(), serverActivity.getId());
		assertEquals(activity1.getName(), serverActivity.getName());
		assertEquals(activity1.getDescription(), serverActivity.getDescription());
		assertEquals(activity1.getType(), serverActivity.getType());
		assertEquals(activity1.getState(), serverActivity.getState());
		assertEquals(c1.getTime().toString(), c2.getTime().toString());//Start Time
		c1.setTime(new Date(activity1.getEndTime().getTime()));
		c2.setTime(new Date(serverActivity.getEndTime().getTime()));
		assertEquals(c1.getTime().toString(), c2.getTime().toString());//End Time
		assertEquals(activity1.getEstimateTime(), serverActivity.getEstimateTime());
		assertArrayEquals(activity1.getParticipatingAgentIds().toArray(), serverActivity.getParticipatingAgentIds().toArray());
		assertArrayEquals(activity1.getParticipatingItemIds().toArray(), serverActivity.getParticipatingItemIds().toArray());
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityItem(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testInsertActivityItem() {
		Activity a1 = new Activity("Hello");
		a1.setType(new ActivityType("Trip").toString());
		Item i1 = new Item(new ItemType("Stand"));
		i1.setLocation(new Location(11,11));
		Item i2 = new Item(new ItemType("Cable"));
		i2.setLocation(new Location(12,12));
		db.insertActivityType(a1.getType());
		db.insertItemType(i1.getType().toString());
		db.insertItemType(i2.getType().toString());
		db.insertItem(i1);
		db.insertItem(i2);
		Set<String> participatingItemIds = new HashSet<String>();
		participatingItemIds.add(i1.getId());
		participatingItemIds.add(i2.getId());
		a1.setParticipatingItemIds(participatingItemIds);
		db.insertActivity(a1);
		
		Vector<String> activityItemIds = db.getActivityItemIds(a1.getId());
		assertTrue(activityItemIds.contains(i1.getId()) && activityItemIds.contains(i2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityItem(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityItem() {
		Activity a1 = new Activity("Hello");
		a1.setType(new ActivityType("Trip").toString());
		Item i1 = new Item(new ItemType("Stand"));
		i1.setLocation(new Location(11,11));
		Item i2 = new Item(new ItemType("Cable"));
		i2.setLocation(new Location(12,12));
		db.insertActivityType(a1.getType());
		db.insertItemType(i1.getType().toString());
		db.insertItemType(i2.getType().toString());
		db.insertItem(i1);
		db.insertItem(i2);
		Set<String> participatingItemIds = new HashSet<String>();
		participatingItemIds.add(i1.getId());
		participatingItemIds.add(i2.getId());
		a1.setParticipatingItemIds(participatingItemIds);
		db.insertActivity(a1);
		
		Vector<String> activityItemIds = db.getActivityItemIds(a1.getId());
		assertTrue(activityItemIds.contains(i1.getId()) && activityItemIds.contains(i2.getId()));
		db.deleteActivityItem(a1.getId(), i1.getId());
		db.deleteActivityItem(a1.getId(), i2.getId());
		activityItemIds = db.getActivityItemIds(a1.getId());
		assertFalse(activityItemIds.contains(i1.getId()));
		assertFalse(activityItemIds.contains(i2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityItemIds(java.lang.String)}.
	 */
	@Test
	public void testGetActivityItemIds() {
		Activity ac = new Activity("Hey");
		ac.setType("BLA");
		db.insertActivityType("BLA");
		db.insertActivity(ac);
		
		ItemType type = new ItemType("STAND");
		Item item1 = new Item(type);
		item1.setLocation(new Location(11,11));
		Item item2 = new Item(type);
		item2.setLocation(new Location(12,12));
		db.insertItemType(type.toString());
		db.insertItem(item1);
		db.insertItem(item2);
		
		db.insertActivityItem(ac.getId(), item1.getId());
		db.insertActivityItem(ac.getId(), item2.getId());
		
		Vector<String> itemIds = db.getActivityItemIds(ac.getId());
		
		assertTrue(itemIds.contains(item1.getId()) && itemIds.contains(item2.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#updateAgentLocation(java.lang.String, utilities.Location)}.
	 */
	@Test
	public void testUpdateAgentLocation() {
		AgentType type = new AgentType("COORDINATOR");
		Agent agent1 = new Agent(type);
		Location location = new Location(3, 4);
		agent1.setLocation(location);
		db.insertAgentType(type.toString());
		db.insertAgent(agent1.getId(), agent1.getType().toString(), true, "1212");
		
		db.insertAgentLocation(agent1.getId(), location);
		assertEquals(4, db.getAgentLocation(agent1.getId()).getLatitude());
		assertEquals(3, db.getAgentLocation(agent1.getId()).getLongitude());
		
		db.updateAgentLocation(agent1.getId(), new Location(1, 1));
		assertEquals(1, db.getAgentLocation(agent1.getId()).getLatitude());
		assertEquals(1, db.getAgentLocation(agent1.getId()).getLongitude());
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAgentLocation(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgentLocation() {
		AgentType type = new AgentType("COORDINATOR");
		Agent agent1 = new Agent(type);
		Location location = new Location(3, 3);
		agent1.setLocation(location);
		db.insertAgentType(type.toString());
		db.insertAgent(agent1.getId(), agent1.getType().toString(), true, "1212");
		
		db.insertAgentLocation(agent1.getId(), location);
		assertNotNull(db.getAgentLocation(agent1.getId()));
		db.deleteAgentLocation(agent1.getId());
		assertNull(db.getAgentLocation(agent1.getId()));
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#updateItemLocation(java.lang.String, utilities.Location)}.
	 */
	@Test
	public void testUpdateItemLocation() {
		ItemType type = new ItemType("STAND");
		Item item1 = new Item(type);
		Location location = new Location(3, 4);
		item1.setLocation(location);
		db.insertItemType(type.toString());
		db.insertItem(item1);
		
		assertEquals(4, db.getItemLocation(item1.getId()).getLatitude());
		assertEquals(3, db.getItemLocation(item1.getId()).getLongitude());
		
		db.updateItemLocation(item1.getId(), new Location(1, 1));
		assertEquals(1, db.getItemLocation(item1.getId()).getLatitude());
		assertEquals(1, db.getItemLocation(item1.getId()).getLongitude());
		
		db.deleteData();
	}

	/**
	 * Test method for {@link db.DBHandler#deleteItemLocation(java.lang.String)}.
	 */
	@Test
	public void testDeleteItemLocation() {
		ItemType type = new ItemType("STAND");
		Item item1 = new Item(type);
		Location location = new Location(3, 4);
		item1.setLocation(location);
		db.insertItemType(type.toString());
		db.insertItem(item1);
		
		assertNotNull(db.getItemLocation(item1.getId()));
		db.deleteItemLocation(item1.getId());
		assertNull(db.getItemLocation(item1.getId()));
		
		db.deleteData();
	}
}
