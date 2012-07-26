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
	public void test() throws Exception {
		
		List<Agent> agents = DBGeneratorForAlgorithmTests.getAgents();
		List<Item> items = DBGeneratorForAlgorithmTests.getItems();
		List<Activity> activities = DBGeneratorForAlgorithmTests.getActivitiesSet1();		

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : activities)
			if (activity.getState() == ActivityState.PENDING)
				variables.add(new Variable(activity, activities,
						agents, items));

		CSPAlgorithm algorithm = new CBJ(variables);

		algorithm.solve();

		if (algorithm.isSolved())
			for (Value value : algorithm.getAssignment())
				System.out.println(value);

		else
			System.out.println("No Solution!");
	}
}
