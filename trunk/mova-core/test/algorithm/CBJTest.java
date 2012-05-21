package algorithm;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import type.AgentType;
import type.ItemType;
import actor.Activity;

public class CBJTest {

	private static final int HOUR = 1000 * 60 * 60;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public Activity createSimpleActivity() {

		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + HOUR * 8);

		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		
		requiredAgents.put(new AgentType("AT1"), 1);
//		requiredAgents.put(new AgentType("AT2"), 1);
		
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		
//		requiredItems.put(new ItemType("IT1"), 1);
//		requiredItems.put(new ItemType("IT2"), 1);
		
		Set<String> requiredActivities = new HashSet<String>();

		return new Activity("KENES", startTime, endTime, HOUR * 2,
				requiredAgents, requiredItems, requiredActivities, "TEST",
				"TEST");
	}
	
	@Test
	public void test() throws Exception {

		Vector<Variable> variables = new Vector<Variable>();
		
		variables.add(new Variable(createSimpleActivity()));
		variables.add(new Variable(createSimpleActivity()));
		
		CBJ cbj = new CBJ(variables);
		
		cbj.solve();
		
		System.out.println();
	}
}
