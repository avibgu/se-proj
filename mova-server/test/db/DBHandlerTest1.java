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
import actor.Item;

import state.ActivityState;
import state.ItemState;
import type.AgentType;
import type.ItemType;

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

	/**
	 * Test method for {@link db.DBHandler#deleteAgent(java.lang.String)}.
	 */
	@Test
	public void testDeleteAgent() {
		_db.insertAgentType("COORDINATOR");
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR").getType(), true, "546");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR").getType(), true, "546");
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
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		
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

}
