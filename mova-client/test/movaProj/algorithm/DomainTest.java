package movaProj.algorithm;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import movaProj.algorithm.Domain;
import movaProj.algorithm.Value;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import type.AgentType;
import type.ItemType;

import actor.Activity;

public class DomainTest {

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
		
//		requiredItems.put(new ItemType("IT1"), 2);
//		requiredItems.put(new ItemType("IT2"), 1);
		
		Set<String> requiredActivities = new HashSet<String>();

		return new Activity("KENES", startTime, endTime, HOUR * 2,
				requiredAgents, requiredItems, requiredActivities, "TEST",
				"TEST");
	}

	@Test
	public void test() {

		Activity activity = createSimpleActivity();
		
		Domain domain = new Domain(activity);
		
		Value value = null; 
		
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		value = domain.nextValue();
		
		domain.resetIndexes();
	}
}
