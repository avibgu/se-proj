package movaProj.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import type.AgentType;
import type.ItemType;
import actor.Activity;
import actor.Agent;
import actor.Item;

public class DBGeneratorForAlgorithmTests {

	private static List<Agent> sAgents = new ArrayList<Agent>();
	private static List<Item> sItems = new ArrayList<Item>();
	private static List<Activity> sActivities = new ArrayList<Activity>();

	public static List<Agent> getAgents() {

		if (sAgents.isEmpty()) {

			sAgents.add(new Agent(new AgentType("AG1")));
			sAgents.add(new Agent(new AgentType("AG1")));
			
			sAgents.add(new Agent(new AgentType("AG2")));
			sAgents.add(new Agent(new AgentType("AG2")));
			sAgents.add(new Agent(new AgentType("AG2")));
		}

		return sAgents;
	}
	
	public static List<Item> getItems() {

		if (sItems.isEmpty()) {

			sItems.add(new Item(new ItemType("IT1")));
			sItems.add(new Item(new ItemType("IT1")));
			
			sItems.add(new Item(new ItemType("IT2")));
			sItems.add(new Item(new ItemType("IT2")));
			sItems.add(new Item(new ItemType("IT2")));
		}

		return sItems;
	}
	
	public static List<Activity> getActivities() {
		
		Map<AgentType, Integer> requiredAents = null;
		Map<ItemType, Integer> requiredItems = null;
		Set<String> requiredActivities = null;
		
		if (sActivities.isEmpty()) {

			getAgents();
			getItems();
			
			Activity activity1 = new Activity("Activity1");
			Activity activity2 = new Activity("activity2");
			Activity activity3 = new Activity("activity3");
			Activity activity4 = new Activity("activity4");
			Activity activity5 = new Activity("activity5");
			
			activity1.setRequiredAgents(requiredAents);

			activity1.setRequiredItems(requiredItems);
			
			activity1.setRequiredActivityIds(requiredActivities);
			
			sActivities.add(activity1);
			sActivities.add(activity2);
			sActivities.add(activity3);
			sActivities.add(activity4);
			sActivities.add(activity5);
		}

		return sActivities;
		
	}
	
	public static Activity getSomeActivity(int pIndex) {

		getActivities();
		
		return sActivities.get(pIndex);
	}
}
