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
		runActivitiesTest(DBGeneratorForAlgorithmTests.getComplexSetActivities(), "Complex Test");
		//success - legal schedule
	}
	
	@Test
	public void schedulingActivitiesTest1() throws Exception {	
		runActivitiesTest(DBGeneratorForAlgorithmTests.getSchedulingActivitiesTestSet1(), "Scheduling Activities Test 1");
		//success - 2 activities at the same time..
	}
	
	@Test
	public void schedulingActivitiesTest2() throws Exception {
		runActivitiesTest(DBGeneratorForAlgorithmTests.getSchedulingActivitiesTestSet2(), "Scheduling Activities Test 2");
		//success - only 1 activity (of 2)
	}
	
	@Test
	public void problemSolvingTest1() throws Exception {
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet1(), "Problem Solving Test 1");
		//success - 2 activities - one after one..
	}
	
	@Test
	public void problemSolvingTest2() throws Exception {
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet21(), "Problem Solving Test 2 - 1");
		//success - 2 activities at the same time..
		DBGeneratorForAlgorithmTests.setOneItem4Unavailable();
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet21(), "Problem Solving Test 2 - 2");
		//success - 2 activities - one after one..
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet22(), "Problem Solving Test 2 - 3");
		//success - 2 activities at the same time..
		DBGeneratorForAlgorithmTests.removeOneAgent4fromDB();
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet22(), "Problem Solving Test 2 - 4");
		//success - 2 activities - one after one..
	}
	
	@Test
	public void problemSolvingTest3() throws Exception {
		
		List<Activity> problemSolvingTestSet3 = DBGeneratorForAlgorithmTests.getProblemSolvingTestSet3();
		
		runActivitiesTest(problemSolvingTestSet3, "Problem Solving Test 3 - 1");
		//success - 2 activities at the same time..
		
		Activity activity = problemSolvingTestSet3.get(0);
		
		// set to two hours
		activity.setEstimateTime(1000 * 60 * 60 * 2);
		
		runActivitiesTest(problemSolvingTestSet3, "Problem Solving Test 3 - 2");
		//success - 2 activities at the same time, but one of them takes longer (double time)
	}
	
	@Test
	public void problemSolvingTest4() throws Exception {

		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet41(), "Problem Solving Test 4 - 1");
		// fails - there is not enough time
		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet42(), "Problem Solving Test 4 - 2");
		// fails - there is not enough items
		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet43(), "Problem Solving Test 4 - 3");
		// fails - there is not enough agents
		
		runActivitiesTest(DBGeneratorForAlgorithmTests.getProblemSolvingTestSet44(), "Problem Solving Test 4 - 4");
		// fails - activity1 and activity2 depends on each other
	}

	protected void runActivitiesTest(List<Activity> activities, String testName)
			throws Exception {

		System.out.println(testName);
		
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
