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
	public void complexTest() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getComplexSetActivities());
	}
	
	@Test
	public void schedulingActivitiesTest1() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getSchedulingActivitiesTestSet1());
	}
	
	@Test
	public void schedulingActivitiesTest2() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getSchedulingActivitiesTestSet2());
	}
	
	@Test
	public void problemSolvingTest1() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet1());
	}
	
	@Test
	public void problemSolvingTest2() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet2());
	}
	
	@Test
	public void problemSolvingTest3() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet3());
	}
	
	@Test
	public void problemSolvingTest4() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet4());
	}

	protected void runActivitiesTest(List<Activity> activities)
			throws Exception {
		
		List<Agent> agents = DBGeneratorForAlgorithmTests.getAgents();
		List<Item> items = DBGeneratorForAlgorithmTests.getItems();

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
