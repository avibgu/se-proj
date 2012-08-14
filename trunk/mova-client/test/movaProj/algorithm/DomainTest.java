package movaProj.algorithm;

import java.util.List;

import org.junit.Test;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class DomainTest {

	@Test
	public void test() throws Exception {

		Activity activity = DBGeneratorForAlgorithmTests.getSomeComplexSetActivity(0);
		
		List<Agent> agents = DBGeneratorForAlgorithmTests.getAgents();
		List<Item> items = DBGeneratorForAlgorithmTests.getItems();
		List<Activity> activities = DBGeneratorForAlgorithmTests.getComplexSetActivities();
		
		Domain domain = new Domain(activity, activities, agents, items);

		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());
		System.out.println(domain.nextValue());

		domain.resetIndexes();
		
		System.out.println(domain.nextValue());
	}
}
