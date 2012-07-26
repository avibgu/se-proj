package movaProj.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import type.AgentType;
import type.ItemType;
import actor.Activity;
import actor.Agent;
import actor.Item;

public class DBGeneratorForAlgorithmTests {

	private static final AgentType sAG1 = new AgentType("AG1");
	private static final AgentType sAG2 = new AgentType("AG2");
	private static final AgentType sAG3 = new AgentType("AG3");

	private static final ItemType sIT1 = new ItemType("IT1");
	private static final ItemType sIT2 = new ItemType("IT2");
	private static final ItemType sIT3 = new ItemType("IT3");
	
	private static List<Agent> sAgents = new ArrayList<Agent>();
	private static List<Item> sItems = new ArrayList<Item>();
	private static List<Activity> sActivities = new ArrayList<Activity>();

	public static List<Agent> getAgents() {

		if (sAgents.isEmpty()) {

			sAgents.add(new Agent(sAG1));
			sAgents.add(new Agent(sAG1));
			
			sAgents.add(new Agent(sAG2));
			sAgents.add(new Agent(sAG2));
			
			sAgents.add(new Agent(sAG3));
			sAgents.add(new Agent(sAG3));
		}

		return sAgents;
	}
	
	public static List<Item> getItems() {

		if (sItems.isEmpty()) {

			sItems.add(new Item(sIT1));
			sItems.add(new Item(sIT1));
			
			sItems.add(new Item(sIT2));
			sItems.add(new Item(sIT2));
			
			sItems.add(new Item(sIT3));
			sItems.add(new Item(sIT3));
		}

		return sItems;
	}
	
	public static List<Activity> getActivitiesSet1() {
		
		Map<AgentType, Integer> requiredAents = null;
		Map<ItemType, Integer> requiredItems = null;
		Set<String> requiredActivities = null;
		
		if (sActivities.isEmpty()) {

			getAgents();
			getItems();
			
			Activity activity1 = new Activity("Activity1");
	
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG1, 1);
			activity1.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT1, 1);
			activity1.setRequiredItems(requiredItems);
			
			sActivities.add(activity1);
			
			Activity activity2 = new Activity("activity2");
			
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG2, 1);
			activity2.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT2, 1);
			activity2.setRequiredItems(requiredItems);
			
			sActivities.add(activity2);

			Activity activity3 = new Activity("activity3");
			
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG1, 1);
			activity3.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT2, 1);
			activity3.setRequiredItems(requiredItems);
			
			requiredActivities = new HashSet<String>();
			requiredActivities.add(activity1.getId());
			requiredActivities.add(activity2.getId());
			activity3.setRequiredActivityIds(requiredActivities);

			sActivities.add(activity3);
				
			Activity activity4 = new Activity("activity4");
			
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG1, 2);
			activity4.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT1, 1);
			activity4.setRequiredItems(requiredItems);
			
			requiredActivities = new HashSet<String>();
			requiredActivities.add(activity2.getId());
			activity4.setRequiredActivityIds(requiredActivities);
			
			sActivities.add(activity4);
			
			Activity activity5 = new Activity("activity5");
			
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG2, 1);
			activity5.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT1, 2);
			activity5.setRequiredItems(requiredItems);
			
			requiredActivities = new HashSet<String>();
			requiredActivities.add(activity1.getId());
			activity5.setRequiredActivityIds(requiredActivities);
			
			sActivities.add(activity5);
			
			Activity activity6 = new Activity("activity6");
			
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG3, 2);
			activity6.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT3, 1);
			activity6.setRequiredItems(requiredItems);
			
			requiredActivities = new HashSet<String>();
			requiredActivities.add(activity4.getId());
			requiredActivities.add(activity5.getId());
			activity6.setRequiredActivityIds(requiredActivities);
			
			sActivities.add(activity6);
			
			Activity activity7 = new Activity("activity7");
			
			requiredAents = new HashMap<AgentType, Integer>();
			requiredAents.put(sAG3, 1);
			activity7.setRequiredAgents(requiredAents);

			requiredItems = new HashMap<ItemType, Integer>();
			requiredItems.put(sIT3, 1);
			activity7.setRequiredItems(requiredItems);
			
			requiredActivities = new HashSet<String>();
			requiredActivities.add(activity4.getId());
			requiredActivities.add(activity5.getId());
			activity7.setRequiredActivityIds(requiredActivities);
			
			sActivities.add(activity7);
		}

		return sActivities;
		
	}
	
	public static Activity getSomeActivity(int pIndex) {

		getActivitiesSet1();
		
		return sActivities.get(pIndex);
	}
}
