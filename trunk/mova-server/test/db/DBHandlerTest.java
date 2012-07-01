/**
 * 
 */
package db;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import type.AgentType;

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
	 * Test method for {@link db.DBHandler#insertAgentType(java.lang.String)}.
	 */
	@Test
	public void testInsertAgentType() {
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
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentIds()}.
	 */
	@Test
	public void testGetAgentIds() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentActivityId(java.lang.String)}.
	 */
	@Test
	public void testGetAgentActivityId() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentStatus(java.lang.String)}.
	 */
	@Test
	public void testGetAgentStatus() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentRegistrationId(java.lang.String)}.
	 */
	@Test
	public void testGetAgentRegistrationId() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAgentType(java.lang.String)}.
	 */
	@Test
	public void testGetAgentType() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentStatus(java.lang.String, java.lang.Boolean)}.
	 */
	@Test
	public void testChangeAgentStatus() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentActivityId(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testChangeAgentActivityId() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#changeAgentRegistrationID(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testChangeAgentRegistrationID() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteAllAgents()}.
	 */
	@Test
	public void testDeleteAllAgents() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getAllAgents()}.
	 */
	@Test
	public void testGetAllAgents() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivityType(java.lang.String)}.
	 */
	@Test
	public void testInsertActivityType() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivityType(java.lang.String)}.
	 */
	@Test
	public void testDeleteActivityType() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityTypes()}.
	 */
	@Test
	public void testGetActivityTypes() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#insertActivity(actor.Activity)}.
	 */
	@Test
	public void testInsertActivity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#deleteActivity(java.lang.String)}.
	 */
	@Test
	public void testDeleteActivity() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityDeadline(java.lang.String, java.sql.Timestamp)}.
	 */
	@Test
	public void testUpdateActivityDeadline() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityDuration(java.lang.String, long)}.
	 */
	@Test
	public void testUpdateActivityDuration() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#updateActivityState(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateActivityState() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityNames()}.
	 */
	@Test
	public void testGetActivityNames() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityDeadline(java.lang.String)}.
	 */
	@Test
	public void testGetActivityDeadline() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityDuration(java.lang.String)}.
	 */
	@Test
	public void testGetActivityDuration() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityState(java.lang.String)}.
	 */
	@Test
	public void testGetActivityState() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link db.DBHandler#getActivityName(java.lang.String)}.
	 */
	@Test
	public void testGetActivityName() {
		fail("Not yet implemented");
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
