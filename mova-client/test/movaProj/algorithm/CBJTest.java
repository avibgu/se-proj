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
		//success - legal schedule
	}
	
	@Test
	public void schedulingActivitiesTest1() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getSchedulingActivitiesTestSet1());
		//success - 2 activities at the same time..
	}
	
	@Test
	public void schedulingActivitiesTest2() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getSchedulingActivitiesTestSet2());
		//success - only 1 activity (of 2)
	}
	
	@Test
	public void problemSolvingTest1() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet1());
		//success - 2 activities - one after one..
	}
	
	@Test
	public void problemSolvingTest2() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet2());
		//success - 2 activities at the same time..
		DBGeneratorForAlgorithmTests.setOneItem4Unavailable();
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet2());
		//success - 2 activities - one after one..
	}
	
	@Test
	public void problemSolvingTest3() throws Exception {
		
		List<Activity> problemSolvingTestSet3 = DBGeneratorForAlgorithmTests.getProblemSolvingTestSet3();
		
		runActivitiesTest(problemSolvingTestSet3);
		//success - 2 activities at the same time..
		
		Activity activity = problemSolvingTestSet3.get(0);
		
		// set to two hours
		activity.setEstimateTime(activity.getEstimateTime() + 1000 * 60 * 60 * 2);
		
		runActivitiesTest(problemSolvingTestSet3);
		//success - 2 activities at the same time, but one of them takes longer (double time)
	}
	
	@Test
	public void problemSolvingTest4() throws Exception {		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet41());
		// fails - there is not enough time
		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet42());
		// fails - there is not enough items
		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet43());
		// fails - there is not enough agents
		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet44());
		// fails - activity1 and activity2 depends on each other
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
