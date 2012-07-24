package movaProj.algorithm;

import java.util.List;

import movaProj.algorithm.Value;

import org.junit.Test;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class DomainTest {

	@Test
	public void test() {

		Activity activity = DBGeneratorForAlgorithmTests.createSimpleActivity();
		
		List<Agent> agents = null;
		List<Item> items = null;
		List<Activity> activities = null;
		
		Domain domain = new Domain(activity, activities, agents, items);
		
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
		
		value.toString();
		
		domain.resetIndexes();
	}
}
