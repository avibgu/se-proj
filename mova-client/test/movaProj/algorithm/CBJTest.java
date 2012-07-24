package movaProj.algorithm;

import java.util.List;
import java.util.Vector;

import movaProj.algorithm.CBJ;
import movaProj.algorithm.Variable;

import org.junit.Test;

import state.ActivityState;
import actor.Activity;
import actor.Agent;
import actor.Item;

public class CBJTest {

	@Test
	public void oldTest() throws Exception {

//		Vector<Variable> variables = new Vector<Variable>();
//
//		List<Agent> agents = null;
//		List<Item> items = null;
//		List<Activity> activities = null;
//		
//		Activity activity = createSimpleActivity();
//		
//		Domain domain = new Domain(activity, activities, agents, items);
//
//		variables.add(new Variable(activity));
//		variables.add(new Variable(createSimpleActivity(activity.getId())));
//
//		CBJ cbj = new CBJ(variables);
//
//		cbj.solve();
//
//		System.out.println();
	}

	@Test
	public void test() throws Exception {
		
		List<Agent> agents = DBGeneratorForAlgorithmTests.generateAgents();
		List<Item> items = DBGeneratorForAlgorithmTests.generateItems();
		List<Activity> activities = DBGeneratorForAlgorithmTests.generateActivities();		

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : activities)
			if (activity.getState() == ActivityState.PENDING)
				variables.add(new Variable(activity, activities,
						agents, items));

		CSPAlgorithm algorithm = new CBJ(variables);

		algorithm.solve();

		if (algorithm.isSolved()) {

			for (Value value : algorithm.getAssignment())
				System.out.println(value);
		}

		else
			;
	}
}
