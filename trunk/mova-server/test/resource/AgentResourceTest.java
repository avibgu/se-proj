package resource;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import actor.Activity;
import actor.Agent;

import type.ActivityType;
import type.AgentType;
import type.ItemType;
import utilities.Location;
import utilities.MovaJson;
import client.MovaClient;
import db.DBHandler;

public class AgentResourceTest {
	
	private MovaClient mc;
	private DBHandler db;
	private MovaJson mj;
	
	@Before
	public void setUp() throws Exception {
		mc = new MovaClient();
		db = DBHandler.getInstance();
		mj = new MovaJson();
	}

	@Test//Passed
	public void testChangeAgentLocation() {
		Agent agent1 = createTestAgent();
		mc.registerAgent(agent1);
		mc.changeAgentLocation(agent1.getId(), new Location(2, 24));
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Location l = db.getAgentLocation(agent1.getId());
		assertEquals(l, new Location(2, 24));
		
		deleteTestAgent(agent1);
	}

	@Test//Passed
	public void testRegisterAgent() {
		Agent agent1 = createTestAgent();
		mc.registerAgent(agent1);
		assertTrue(db.getAgentStatus(agent1.getId()));
		deleteTestAgent(agent1);
	}

	@Test//Passed
	public void testDeleteAgent() {
		Agent agent1 = createTestAgent();
		mc.registerAgent(agent1);
		mc.deleteAgent(agent1.getId());
		Vector<String> ids = db.getAgentIds();
		assertFalse(ids.contains(agent1.getId()));
		db.deleteAgentType(agent1.getType().getType());
	}

	@Test
	public void testChangeAgentStatus() {
		Agent agent1 = createTestAgent();
		mc.registerAgent(agent1);
		boolean status = db.getAgentStatus(agent1.getId());
		assertTrue(status);
		mc.changeAgentStatus(agent1.getId(), false);
		status = db.getAgentStatus(agent1.getId());
		assertFalse(status);
	}

	@Test
	public void testCreateAgentType() {
		mc.createAgentType("Type1");
		Vector<String> types = db.getAgentTypes();
		assertTrue(types.contains("Type1"));
		db.deleteAgentType("Type1");
	}

	@Test
	public void testDeleteAgentType() {
		mc.createAgentType("Type1");
		Vector<String> types = db.getAgentTypes();
		assertTrue(types.contains("Type1"));
		mc.deleteAgentType("Type1");
		types = db.getAgentTypes();
		assertFalse(types.contains("Type1"));
	}

	@Test
	public void testGetAllAgentsHTTP() {
		Agent agent1 = createTestAgent();
		Agent agent2 = createTestAgent();
		
		Vector<Agent> agents = new Vector<Agent>();
		agents.add(agent1);
		agents.add(agent2);
		Vector<Agent> allAgents = mc.getAllAgents("");
		
		assertTrue(allAgents.containsAll(agents));
		
		deleteTestAgent(agent1);
		deleteTestAgent(agent2);
	}

	@Test
	public void testSetCurrentActivityId() {
				
	}

	@Test
	public void testStartRecalculate() {
		fail("Don't know how to test this");
	}

	@Test
	public void testFinishRecalculate() {
		fail("Don't know how to test this");
	}
	
	private Activity createTestActivity(){
		ActivityType type = new ActivityType(ActivityType.CONFERENCE_REGISTRATION);
		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + 1000*60*60*3);
		long estimatedTime = 1000*60*60*2;
		Map<AgentType, Integer> pRequiredAgents = new HashMap<AgentType, Integer>();
		pRequiredAgents.put(new AgentType(AgentType.LECTURER.toString()), 3);
		db.insertAgentType(AgentType.LECTURER.toString());
		Map<ItemType, Integer> pRequiredItems = new HashMap<ItemType, Integer>();
		pRequiredItems.put(new ItemType(ItemType.BOARD.toString()), 1);
		//db.insertItemType(ItemType.BOARD.toString());
		Set<String> pRequiredActivities =  new HashSet<String>();
		pRequiredActivities.add("a2");
		String description = "testActivity";
		String name = "testName";
		Activity a1 = new Activity(type.getType(), startTime, endTime, estimatedTime, pRequiredAgents, pRequiredItems, pRequiredActivities, description, name);
		
		db.insertActivityType(a1.getType());
		db.insertActivity(a1);
		
		return a1;
	}
	
	private void deleteTestActivity(Activity activity){
		db.deleteActivity(activity.getId());
		db.deleteActivityType(activity.getType());
	}
	
	private Agent createTestAgent(){
		AgentType pType = new AgentType(AgentType.LECTURER);
		Agent a1 = new Agent(pType);
		
		db.insertAgentType(pType.getType());
		db.insertAgent(a1.getId(), pType.getType(), true, "121");
		
		return a1;
	}
	
	private void deleteTestAgent(Agent agent){
		db.deleteAgent(agent.getId());
		db.deleteAgentType(agent.getType().getType());
	}
}
