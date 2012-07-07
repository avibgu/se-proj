/**
 * 
 */
package db;

import static org.junit.Assert.*;

import java.sql.Timestamp;
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

import state.ActivityState;
import type.AgentType;
import type.ItemType;

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
	 * Test method for {@link db.DBHandler#deleteData()}.
	 */
	@Test
	public void testDeleteData() {
		fail("Not yet implemented");
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
		assertEquals("Hello", db.getActivityName(ac.getId()));
		
		db.deleteActivity(ac.getId());
		db.deleteActivityType("BLA");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityTypeAgents(java.lang.String)}.
	 */
	@Test
	public void testGetActivityTypeAgents() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityTypeItems(java.lang.String)}.
	 */
	@Test
	public void testGetActivityTypeItems() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAllActivities()}.
	 */
	@Test
	public void testGetAllActivities() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertItem(actor.Item)}.
	 */
	@Test
	public void testInsertItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteItem(java.lang.String)}.
	 */
	@Test
	public void testDeleteItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#updateItemState(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateItemState() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getItemState(java.lang.String)}.
	 */
	@Test
	public void testGetItemState() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#setItemHolder(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSetItemHolder() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getItemHolder(java.lang.String)}.
	 */
	@Test
	public void testGetItemHolder() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getItemIds()}.
	 */
	@Test
	public void testGetItemIds() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentItems(java.lang.String)}.
	 */
	@Test
	public void testGetAgentItems() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getItems()}.
	 */
	@Test
	public void testGetItems() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertItemType(java.lang.String)}.
	 */
	@Test
	public void testInsertItemType() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteItemType(java.lang.String)}.
	 */
	@Test
	public void testDeleteItemType() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getItemTypes()}.
	 */
	@Test
	public void testGetItemTypes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityAgent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testInsertActivityAgent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityAgent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityAgent() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityAgentIds(java.lang.String)}.
	 */
	@Test
	public void testGetActivityAgentIds() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentSchedule(java.lang.String)}.
	 */
	@Test
	public void testGetAgentSchedule() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityItem(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testInsertActivityItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityItem(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityItem() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityItemIds(java.lang.String)}.
	 */
	@Test
	public void testGetActivityItemIds() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertAgentLocation(java.lang.String, utilities.Location)}.
	 */
	@Test
	public void testInsertAgentLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#updateAgentLocation(java.lang.String, utilities.Location)}.
	 */
	@Test
	public void testUpdateAgentLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAgentLocation(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgentLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentLocation(java.lang.String)}.
	 */
	@Test
	public void testGetAgentLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertItemLocation(java.lang.String, utilities.Location)}.
	 */
	@Test
	public void testInsertItemLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#updateItemLocation(java.lang.String, utilities.Location)}.
	 */
	@Test
	public void testUpdateItemLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteItemLocation(java.lang.String)}.
	 */
	@Test
	public void testDeleteItemLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getItemLocation(java.lang.String)}.
	 */
	@Test
	public void testGetItemLocation() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#canStartNewRecalculte()}.
	 */
	@Test
	public void testCanStartNewRecalculte() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#finishRecalculte()}.
	 */
	@Test
	public void testFinishRecalculte() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertInitialData()}.
	 */
	@Test
	public void testInsertInitialData() {
		fail("Not yet implemented");
	}

}
