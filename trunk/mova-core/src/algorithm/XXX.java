package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import type.AgentType;
import type.ItemType;
import actor.Agent;
import actor.Item;

public class XXX {

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
}
