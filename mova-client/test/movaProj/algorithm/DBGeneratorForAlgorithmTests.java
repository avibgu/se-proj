package movaProj.algorithm;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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

	private static HashMap<AgentType, List<Agent>> sAgentsMap =
			new HashMap<AgentType, List<Agent>>();
	
	private static HashMap<ItemType, List<Item>> sItemsMap =
			new HashMap<ItemType, List<Item>>();

	public static List<Agent> getAllAgentsOfThisTypeFromDB(AgentType pAgentType) {

		List<Agent> tList = sAgentsMap.get(pAgentType);

		if (null == tList) {

			tList = new ArrayList<Agent>();

			tList.add(new Agent(pAgentType));
			tList.add(new Agent(pAgentType));
			tList.add(new Agent(pAgentType));
			tList.add(new Agent(pAgentType));

			sAgentsMap.put(pAgentType, tList);
		}

		return tList;
	}

	public static List<Item> getAllItemsOfThisTypeFromDB(ItemType pItemType) {

		List<Item> tList = sItemsMap.get(pItemType);

		if (null == tList) {

			tList = new ArrayList<Item>();

			tList.add(new Item(pItemType));
			tList.add(new Item(pItemType));
			tList.add(new Item(pItemType));
			tList.add(new Item(pItemType));
			tList.add(new Item(pItemType));

			sItemsMap.put(pItemType, tList);
		}

		return tList;
	}
	
	public static Activity createSimpleActivity() {

		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + Domain.HOUR * 8);

		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		
		requiredAgents.put(new AgentType("AT1"), 1);
//		requiredAgents.put(new AgentType("AT2"), 1);
		
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		
//		requiredItems.put(new ItemType("IT1"), 2);
//		requiredItems.put(new ItemType("IT2"), 1);
		
		Set<String> requiredActivities = new HashSet<String>();

		return new Activity("KENES", startTime, endTime, Domain.HOUR * 2,
				requiredAgents, requiredItems, requiredActivities, "TEST",
				"TEST");
	}
	
	public static List<Agent> generateAgents() {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Item> generateItems() {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Activity> generateActivities() {
		// TODO Auto-generated method stub
		return null;
	}
}
