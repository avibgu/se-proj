package movaProj.algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.internal.Pair;

import state.ActivityState;
import state.ItemState;
import type.AgentType;
import type.ItemType;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Domain implements Cloneable {

	public static final long HOUR = 1000 * 60 * 60;
	public static final long QUARTER_HOUR = HOUR / 4;

	// the Activity which this Domain belongs to
	protected Activity mActivity;

	// lists of all the Agents and Items in the System
	protected Map<AgentType, List<Agent>> mAgentsMap;
	protected Map<ItemType, List<Item>> mItemsMap;
		
	// lists of all the available Agents and Items that the Activity needs some of them.
	// access them using the smart indexes
	protected List<List<Agent>> mAgents;
	protected List<List<Item>> mItems;

	// data-structures which represent the smart-indexes
	protected List<List<Integer>> mAgentsIndexes;
	protected List<List<Integer>> mItemsIndexes;
	
	// data-structures which hold the number of Agents and Items of each Type
	protected List<Integer> mAgentsSizes;
	protected List<Integer> mItemsSizes;
	
	// all the possible time windows for the Activity
	// of this Domain and the index for accessing this list 
	protected List<Pair<Date, Date>> mTimes;
	protected Integer mTimesIndex;

	// indicates that the Domain is empty
	protected boolean mEmpty;

	// data-structures which hold the time for every Item and Agent
	// of starting being available for the Activity of this Domain
	protected Map<String, Date> mAgentsAvailability;
	protected Map<String, Date> mItemsAvailability;

	/**
	 * Constructor - builds the Domain
	 * 
	 * @param pActivity The Activity which this Domain belongs to
	 * @param pActivities All the Activities in the System
	 * @param pAgents All the Agents in the System
	 * @param pItems All the Items in the System
	 */
	public Domain(Activity pActivity, List<Activity> pActivities,
			List<Agent> pAgents, List<Item> pItems) {

		mActivity = pActivity;
		initItemsAndAgents(pActivities, pAgents, pItems);
		initValues();
	}

	/**
	 * Initialized the data structures wthathich hold information about which Agents
	 * and the Items we have in our System and when they will be available.
	 * 
	 * @param pActivities All the Activities in the System
	 * @param pAgents All the Agents in the System
	 * @param pItems All the Items in the System
	 */
	private void initItemsAndAgents(List<Activity> pActivities,
			List<Agent> pAgents, List<Item> pItems) {

		mAgentsMap = new HashMap<AgentType, List<Agent>>();

		for (Agent agent : pAgents) {

			List<Agent> tAgents = mAgentsMap.get(agent.getType());

			if (null == tAgents) {

				tAgents = new ArrayList<Agent>();
				mAgentsMap.put(agent.getType(), tAgents);
			}

			tAgents.add(agent);
		}

		mItemsMap = new HashMap<ItemType, List<Item>>();

		for (Item item : pItems) {
			
			if (item.getState() == ItemState.UNAVAILABLE)
				continue;
			
			List<Item> tItems = mItemsMap.get(item.getType());

			if (null == tItems) {

				tItems = new ArrayList<Item>();
				mItemsMap.put(item.getType(), tItems);
			}

			tItems.add(item);
		}

		mAgentsAvailability = new HashMap<String, Date>();
		mItemsAvailability = new HashMap<String, Date>();

		for (Activity activity : pActivities) {

			if (activity.getState() == ActivityState.IN_PROGRESS) {

				for (String agentID : activity.getParticipatingAgentIds())
					mAgentsAvailability.put(agentID,
							activity.getActualEndTime());

				for (String itemID : activity.getParticipatingItemIds())
					mItemsAvailability.put(itemID, activity.getActualEndTime());
			}
		}
	}

	/**
	 * Initialized the data structures that represent the smart indexes
	 * and the lists of all available Agents and Items of the Types
	 * that the Activity requires.
	 */
	protected void initValues() {

		mAgents = new ArrayList<List<Agent>>();
		mAgentsIndexes = new ArrayList<List<Integer>>();
		mAgentsSizes = new ArrayList<Integer>();

		for (AgentType agentType : mActivity.getRequiredAgents().keySet()) {

			List<Agent> allAgentsOfThisType = mAgentsMap.get(agentType);

			mAgents.add(allAgentsOfThisType);

			List<Integer> tList = new ArrayList<Integer>();

			Integer numOfrequiredAgents = mActivity.getRequiredAgents().get(
					agentType);

			mAgentsSizes.add(allAgentsOfThisType.size());

			for (int i = 0; i < numOfrequiredAgents; i++)
				tList.add(Integer.valueOf(i));

			mAgentsIndexes.add(tList);
		}

		mItems = new ArrayList<List<Item>>(/* numOfItemsTypes */);
		mItemsIndexes = new ArrayList<List<Integer>>();
		mItemsSizes = new ArrayList<Integer>();

		for (ItemType itemType : mActivity.getRequiredItems().keySet()) {

			List<Item> allItemsOfThisType = mItemsMap.get(itemType);

			mItems.add(allItemsOfThisType);

			List<Integer> tList = new ArrayList<Integer>();

			Integer numOfrequiredItems = mActivity.getRequiredItems().get(
					itemType);

			mItemsSizes.add(allItemsOfThisType.size());

			for (int i = 0; i < numOfrequiredItems; i++)
				tList.add(Integer.valueOf(i));

			mItemsIndexes.add(tList);
		}

		mTimes = new ArrayList<Pair<Date, Date>>();
		mTimesIndex = 0;

		for (long i = Math.max(mActivity.getStartTime().getTime(),
				new Date().getTime()); i + mActivity.getEstimateTime() < mActivity
				.getEndTime().getTime(); i += QUARTER_HOUR) {

			mTimes.add(new Pair<Date, Date>(new Date(i), new Date(i
					+ mActivity.getEstimateTime())));
		}

		mEmpty = false;
	}

	/**
	 * This method is the heart of the Domain Object - it iterates over
	 * all the Values that this Domain have.
	 * It builds Values <b>on-demand</b>!
	 * 
	 * @return The next Value of the Domain
	 * @throws Exception in case there is a problem to construct values - i.e. there is no solution
	 */
	public Value nextValue() throws Exception{

		Value value = null;

		do {
			value = constructValueFromIndexes();

			incrementIndexes();
		}
		
		while (null == value);

		return value;
	}
	
	/*
	 * helper method for the above
	 */
	private Value constructValueFromIndexes() throws Exception {
		
		Set<Agent> requiredAgents = new HashSet<Agent>();
		Set<Item> requiredItems = new HashSet<Item>();
		
		for (int i = 0; i < mAgentsIndexes.size(); i++)
			for (Integer index : mAgentsIndexes.get(i))
				requiredAgents.add(mAgents.get(i).get(index));
		
		for (int i = 0; i < mItemsIndexes.size(); i++)
			for (Integer index : mItemsIndexes.get(i))
				requiredItems.add(mItems.get(i)
						.get(index));

		for (Agent agent : requiredAgents)
			if (mAgentsAvailability.containsKey(agent.getId())
					&& mAgentsAvailability.get(agent.getId()).after(
							mTimes.get(mTimesIndex).first))
				return null;

		for (Item item : requiredItems)
			if (mItemsAvailability.containsKey(item.getId())
					&& mItemsAvailability.get(item.getId()).after(
							mTimes.get(mTimesIndex).first))
				return null;

		return new Value(mActivity, mActivity.getId(),
				mTimes.get(mTimesIndex).first, mTimes.get(mTimesIndex).second,
				requiredAgents, requiredItems,
				mActivity.getRequiredActivityIds());
	}

	/**
	 * This method increments all the Indexes by 1 (can be refer as the global index)
	 */
	private void incrementIndexes() {

		boolean incremented = incrementAgentsOrItemsIndexes(mAgentsSizes,
				mAgentsIndexes);

		if (!incremented) {

			incremented = incrementAgentsOrItemsIndexes(mItemsSizes,
					mItemsIndexes);

			if (!incremented) {

				mTimesIndex++;

				if (mTimes.size() == mTimesIndex)
					mEmpty = true;
			}
		}
	}

	
	/*
	 * helper method for the above.  
	 */
	private boolean incrementAgentsOrItemsIndexes(List<Integer> pEntities,
			List<List<Integer>> pEntitiesIndexes) {

		boolean incremented = false;

		for (int i = pEntitiesIndexes.size() - 1; i >= 0 && !incremented; i--) {

			int lastIndex = pEntities.get(i) - 1;

			int numOfIndexes = pEntitiesIndexes.get(i).size();

			for (int j = numOfIndexes - 1; j >= 0 && !incremented; j--) {

				if (pEntitiesIndexes.get(i).get(j) + (numOfIndexes - 1 - j) < lastIndex) {

					int newIndexValue = pEntitiesIndexes.get(i).get(j) + 1;

					pEntitiesIndexes.get(i).set(j, newIndexValue);

					for (int k = j + 1; k < numOfIndexes; k++)
						pEntitiesIndexes.get(i).set(k, newIndexValue + (k - j));

					incremented = true;
				}
			}
		}

		return incremented;
	}

	/**
	 * reset all the indexes to their initial posistion (global index == 0)
	 */
	public void resetIndexes() {

		for (int i = 0; i < mAgentsIndexes.size(); i++)
			for (int j = 0; j < mAgentsIndexes.get(i).size(); j++)
				mAgentsIndexes.get(i).set(j, j);

		for (int i = 0; i < mItemsIndexes.size(); i++)
			for (int j = 0; j < mItemsIndexes.get(i).size(); j++)
				mItemsIndexes.get(i).set(j, j);

		mTimesIndex = 0;

		mEmpty = false;
	}

	/**
	 * @return <i>true</i> if the Domain is empty.
	 */
	public boolean isEmpty() {
		return mEmpty;
	}
}
