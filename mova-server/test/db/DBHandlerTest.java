package db;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;


import type.AgentType;

public class DBHandlerTest {

	private DBHandler _db;
	
	@Before
	public void setUp() throws Exception {
		_db = DBHandler.getInstance();
	}

	@Test
	public void testGetInstance() {
		_db = DBHandler.getInstance();
		assertNotNull(_db);
	}

	@Test
	public void testInsertAgent() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		String registrationId = _db.getAgentRegistrationId("20");
		assertEquals("345345", registrationId);
		_db.deleteAgent("20");
	}

	@Test
	public void testDeleteAgent() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.deleteAgent("20");
		String registrationId = _db.getAgentRegistrationId("20");
		assertEquals("", registrationId);
		_db.deleteAgent("20");
	}

	@Test
	public void testGetAgentIds() {
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
	}

	@Test
	public void testGetAgentRegistrationId() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		String regId = _db.getAgentRegistrationId("20");
		assertEquals("345345", regId);
		_db.deleteAgent("20");
	}

	@Test
	public void testChangeAgentStatus() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		boolean status1 = _db.getAgentStatus("20");
		assertEquals(true, status1);
		_db.deleteAgent("20");
		
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), false, "345345");
		boolean status2 = _db.getAgentStatus("20");
		assertEquals(false, status2);
		_db.deleteAgent("20");
	}

	@Test
	public void testChangeAgentActivityId() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.changeAgentActivityId("20", "1111");
		String activityId1 = _db.getAgentActivityId("20");
		assertEquals("1111", activityId1);
		_db.deleteAgent("20");
	}

	@Test
	public void testChangeAgentRegistrationID() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.changeAgentRegistrationID("20", "3333");
		String regId = _db.getAgentRegistrationId("20");
		assertEquals("3333", regId);
		_db.deleteAgent("20");
	}
	
	@Test
	public void testDeleteAllAgents() {
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR").getType(), true, "546");
		_db.deleteAllAgents();
		Vector<String> ids = _db.getAgentIds();
		assertEquals(0, ids.size());
	}
	
	@Test
	public void testGetAgentStatus(){
		_db.insertAgent("20", new AgentType("COORDINATOR").getType(), true, "345345");
		_db.changeAgentStatus("20", false);
		boolean status = _db.getAgentStatus("20");
		assertEquals(false, status);
		_db.deleteAgent("20");
	}
}
