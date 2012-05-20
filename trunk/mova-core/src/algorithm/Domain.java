package algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

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

	// public Domain(Activity pActivity, Vector<Value> pValues) {
	//
	// mActivity = pActivity;
	// mValues = pValues;
	// }

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

			Vector<Item> allItemsOfThisType = getAllItemsOfThisTypeFromDB(itemType);

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
		
		resetIndexes();
	}

	private Vector<Item> getAllItemsOfThisTypeFromDB(ItemType pItemType) {
		// TODO Auto-generated method stub
		
		Vector<Item> items = new Vector<Item>();
		
		items.add(new Item(pItemType));
		items.add(new Item(pItemType));
		items.add(new Item(pItemType));
		items.add(new Item(pItemType));
		
		return items;
	}

	private List<Agent> getAllAgentsOfThisTypeFromDB(AgentType pAgentType) {
		// TODO Auto-generated method stub
		
		Vector<Agent> agents = new Vector<Agent>();
		
		agents.add(new Agent(pAgentType));
		agents.add(new Agent(pAgentType));
		agents.add(new Agent(pAgentType));
		
		return agents;
	}

	public Value nextValue() {

		String hashKey = getHashKeyOfCurrentIndexes();

		Value value = mValues.get(hashKey);

		if (null == value) {

			value = constructValueFromIndexes();
			mValues.put(hashKey, value);
		}

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

				if (pEntitiesIndexes.get(i).get(j) + (numOfIndexes - 1 - j) <= lastIndex) {

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

	private String getHashKeyOfCurrentIndexes() {
		// TODO Auto-generated method stub
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

		// TODO: ...
		return null;

		// Vector<Value> tValues = new Vector<Value>(mValues.size());
		//
		// for (Value value : mValues)
		// tValues.add(value);
		//
		// return new Domain(mActivity, tValues);
	}

	public boolean isEmpty() {
		return mEmpty;
	}
}
