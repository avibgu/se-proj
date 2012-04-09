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
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		String ip = _db.getAgentIP("20");
		assertEquals("123123", ip);
		_db.deleteAgent("20");
	}

	@Test
	public void testDeleteAgent() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		_db.deleteAgent("20");
		String ip = _db.getAgentIP("20");
		assertEquals("", ip);
		_db.deleteAgent("20");
	}

	@Test
	public void testGetAgentIds() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR"), true, "1232", "546");
		Vector<String> ids = _db.getAgentIds();
		assertEquals(2, ids.size());
		String ip1 = _db.getAgentIP("20");
		String ip2 = _db.getAgentIP("21");
		assertEquals("123123", ip1);
		assertEquals("1232", ip2);
	}

	@Test
	public void testGetAgentIP() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		String ip1 = _db.getAgentIP("20");
		assertEquals("123123", ip1);
		_db.deleteAgent("20");
	}

	@Test
	public void testGetAgentRegistrationId() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		String regId = _db.getAgentRegistrationId("20");
		assertEquals("345345", regId);
		_db.deleteAgent("20");
	}

	@Test
	public void testChangeAgentStatus() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		boolean status1 = _db.getAgentStatus("20");
		assertEquals(true, status1);
		_db.deleteAgent("20");
		
		_db.insertAgent("20", new AgentType("COORDINATOR"), false, "123123", "345345");
		boolean status2 = _db.getAgentStatus("20");
		assertEquals(false, status2);
		_db.deleteAgent("20");
	}

	@Test
	public void testChangeAgentIP() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		_db.changeAgentIP("20", "1111");
		String ip1 = _db.getAgentIP("20");
		assertEquals("1111", ip1);
		_db.deleteAgent("20");
	}

	@Test
	public void testChangeAgentRegistrationID() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		_db.changeAgentRegistrationID("20", "3333");
		String regId = _db.getAgentRegistrationId("20");
		assertEquals("3333", regId);
		_db.deleteAgent("20");
	}
	
	@Test
	public void testDeleteAllAgents() {
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		_db.insertAgent("21", new AgentType("COORDINATOR"), true, "1232", "546");
		_db.deleteAllAgents();
		Vector<String> ids = _db.getAgentIds();
		assertEquals(0, ids.size());
	}
	
	@Test
	public void testGetAgentStatus(){
		_db.insertAgent("20", new AgentType("COORDINATOR"), true, "123123", "345345");
		_db.changeAgentStatus("20", false);
		boolean status = _db.getAgentStatus("20");
		assertEquals(false, status);
	}
}
