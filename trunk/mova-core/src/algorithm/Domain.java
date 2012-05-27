package algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.internal.Pair;

import type.AgentType;
import type.ItemType;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Domain implements Cloneable {

	private static final long HOUR = 1000 * 60 * 60;

	protected Activity mActivity;
	protected Map<String, Value> mValues;

	protected List<List<Agent>> mAgents;
	protected List<List<Item>> mItems;

	protected List<List<Integer>> mAgentsIndexes;
	protected List<List<Integer>> mItemsIndexes;

	protected List<Pair<Date, Date>> mTimes;
	protected Integer mTimesIndex;

	protected List<Integer> mAgentsSizes;
	protected List<Integer> mItemsSizes;

	protected boolean mEmpty;

	public Domain(Activity pActivity) {

		mActivity = pActivity;
		initValues();
	}

	public Domain(Activity pActivity, Map<String, Value> pValues,
			List<List<Agent>> pAgents, List<List<Item>> pItems,
			List<List<Integer>> pAgentsIndexes,
			List<List<Integer>> pItemsIndexes, List<Pair<Date, Date>> pTimes,
			Integer pTimesIndex, List<Integer> pAgentsSizes,
			List<Integer> pItemsSizes, boolean pEmpty) {

		// TODO Auto-generated constructor stub
	}

	protected void initValues() {

		mValues = new HashMap<String, Value>();

		mAgents = new ArrayList<List<Agent>>();
		mAgentsIndexes = new ArrayList<List<Integer>>();
		mAgentsSizes = new ArrayList<Integer>();

		for (AgentType agentType : mActivity.getRequiredAgents().keySet()) {

			List<Agent> allAgentsOfThisType = getAllAgentsOfThisTypeFromDB(agentType);

			mAgents.add(allAgentsOfThisType);

			List<Integer> tList = new ArrayList<Integer>();

			Integer numOfrequiredAgents = mActivity.getRequiredAgents().get(
					agentType);

			mAgentsSizes.add(allAgentsOfThisType.size());

			for (int i = 0; i < numOfrequiredAgents; i++)
				tList.add(new Integer(i));

			mAgentsIndexes.add(tList);
		}

		mItems = new ArrayList<List<Item>>(/* numOfItemsTypes */);
		mItemsIndexes = new ArrayList<List<Integer>>();
		mItemsSizes = new ArrayList<Integer>();

		for (ItemType itemType : mActivity.getRequiredItems().keySet()) {

			List<Item> allItemsOfThisType = getAllItemsOfThisTypeFromDB(itemType);

			mItems.add(allItemsOfThisType);

			List<Integer> tList = new ArrayList<Integer>();

			Integer numOfrequiredItems = mActivity.getRequiredItems().get(
					itemType);

			mItemsSizes.add(allItemsOfThisType.size());

			for (int i = 0; i < numOfrequiredItems; i++)
				tList.add(new Integer(i));

			mItemsIndexes.add(tList);
		}

		mTimes = new ArrayList<Pair<Date, Date>>();
		mTimesIndex = 0;

		for (long i = mActivity.getStartTime().getTime(); i
				+ mActivity.getEstimateTime() < mActivity.getEndTime()
				.getTime(); i += HOUR) {

			mTimes.add(new Pair<Date, Date>(new Date(i), new Date(i
					+ mActivity.getEstimateTime())));
		}

		mEmpty = false;
	}

	private List<Agent> getAllAgentsOfThisTypeFromDB(AgentType pAgentType) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Item> getAllItemsOfThisTypeFromDB(ItemType pItemType) {
		// TODO Auto-generated method stub
		return null;
	}

	public Value nextValue() {

//		String hashKey = getHashKeyOfCurrentIndexes();
//
//		Value value = mValues.get(hashKey);

		Value value = null;
				
//		if (null == value) {

			value = constructValueFromIndexes();
//			mValues.put(hashKey, value);
//		}

		incrementIndexes();

		return value;
	}

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

	private Value constructValueFromIndexes() {

		Set<Agent> requiredAgents = new HashSet<Agent>();

		for (int i = 0; i < mAgentsIndexes.size(); i++)
			for (Integer index : mAgentsIndexes.get(i))
				requiredAgents.add(mAgents.get(i).get(index));

		Set<Item> requiredItems = new HashSet<Item>();

		for (int i = 0; i < mItemsIndexes.size(); i++)
			for (Integer index : mItemsIndexes.get(i))
				requiredItems.add(mItems.get(i).get(index));

		return new Value(mActivity.getId(), mTimes.get(mTimesIndex).first,
				mTimes.get(mTimesIndex).second, requiredAgents, requiredItems,
				mActivity.getRequiredActivityIds());
	}

	public String getHashKeyOfCurrentIndexes() {

		// StringBuilder sb = new StringBuilder();
		//
		// for (int i = 0; i < mAgentsIndexes.size(); i++)
		// for (int j = 0; j < mAgentsIndexes.get(i).size(); j++)
		// sb.append(mAgentsIndexes.get(i).get(j) + ":");
		//
		// for (int i = 0; i < mItemsIndexes.size(); i++)
		// for (int j = 0; j < mItemsIndexes.get(i).size(); j++)
		// sb.append(mItemsIndexes.get(i).get(j) + ":");
		//
		// sb.append(mTimesIndex);
		//
		// return sb.toString();

		// TODO
		return null;
	}

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

	@Override
	protected Domain clone() {

		// TODO
		return null;
		
//		Map<String, Value> values = new HashMap<String, Value>();
//		
//		for (String key : mValues.keySet())
//			values.put(key, values.get(key).clone());
//
//		List<List<Agent>> agents = ;
//		List<List<Item>> items;
//
//		List<List<Integer>> agentsIndexes;
//		List<List<Integer>> itemsIndexes;
//
//		List<Pair<Date, Date>> times;
//		Integer timesIndex;
//
//		List<Integer> agentsSizes;
//		List<Integer> itemsSizes;
//
//		return new Domain(mActivity, values, agents, items, agentsIndexes,
//				itemsIndexes, times, timesIndex, agentsSizes, itemsSizes,
//				mEmpty);
	}

	public boolean isEmpty() {
		return mEmpty;
	}
}
