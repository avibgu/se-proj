package movaProj.algorithm;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import movaProj.algorithm.CBJ;
import movaProj.algorithm.Variable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import state.ActivityState;
import type.AgentType;
import type.ItemType;
import actor.Activity;
import actor.Agent;
import actor.Item;

public class CBJTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public Activity createSimpleActivity(String pSomeActivityID) {

		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + Domain.HOUR
				* 24);

		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();

		requiredAgents.put(new AgentType("AT1"), 1);
		requiredAgents.put(new AgentType("AT2"), 1);

		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();

		requiredItems.put(new ItemType("IT1"), 1);
		requiredItems.put(new ItemType("IT2"), 1);

		Set<String> requiredActivities = new HashSet<String>();

		if (null != pSomeActivityID)
			requiredActivities.add(pSomeActivityID);

		return new Activity("KENES", startTime, endTime, Domain.HOUR * 2,
				requiredAgents, requiredItems, requiredActivities, "TEST",
				"TEST");
	}

	@Test
	public void test() throws Exception {

		Vector<Variable> variables = new Vector<Variable>();

		Activity activity = null;

		activity = createSimpleActivity(null);
		variables.add(new Variable(activity));
		variables.add(new Variable(createSimpleActivity(activity.getId())));

		activity = createSimpleActivity(null);
		variables.add(new Variable(activity));
		variables.add(new Variable(createSimpleActivity(activity.getId())));

		variables.add(new Variable(createSimpleActivity(null)));
		variables.add(new Variable(createSimpleActivity(null)));

		activity = createSimpleActivity(null);
		variables.add(new Variable(activity));
		variables.add(new Variable(createSimpleActivity(activity.getId())));

		activity = createSimpleActivity(null);
		variables.add(new Variable(activity));
		variables.add(new Variable(createSimpleActivity(activity.getId())));

		CBJ cbj = new CBJ(variables);

		cbj.solve();

		System.out.println();
	}

	@Test
	public void mainTest() throws Exception {
		
		List<Agent> mAgents = generateAgents();
		List<Item> mItems = generateItems();
		List<Activity> mActivities = generateActivities();		

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : mActivities)
			if (activity.getState() == ActivityState.PENDING)
				variables.add(new Variable(activity, mActivities,
						mAgents, mItems));

		CSPAlgorithm mAlgorithm = new CBJ(variables);

		mAlgorithm.solve();

		if (mAlgorithm.isSolved()) {

		}

		else
			;
	}

	private List<Agent> generateAgents() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Item> generateItems() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Activity> generateActivities() {
		// TODO Auto-generated method stub
		return null;
	}
}
