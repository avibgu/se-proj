package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import type.AgentType;
import type.ItemType;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Domain implements Cloneable {

	private static final long HOUR = 1000 * 60 * 60;

	protected Activity mActivity;
	protected Map<String, Value> mValues;

	protected List<List<Agent>> mAgentsDB;
	protected List<List<Item>> mItemsDB;

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

		// setValues(new Vector<Value>());

		int numOfAgentsTypes = mActivity.getRequiredAgents().keySet().size();
		int numOfItemsTypes = mActivity.getRequiredItems().keySet().size();

		mAgentsDB = new ArrayList<List<Agent>>(numOfAgentsTypes);  
		mItemsDB = new ArrayList<List<Item>>(numOfItemsTypes);
		
		for (AgentType agentType : mActivity.getRequiredAgents().keySet()) {

			// Integer numOfrequiredAgents =
			// mActivity.getRequiredAgents().get(agentType);

			List<Agent> allAgentsOfThisType /* = get it from db */ = null;

			mAgentsDB.add(allAgentsOfThisType);
		}

		for (ItemType itemType : mActivity.getRequiredItems().keySet()) {

			// Integer numOfrequiredItems =
			// mActivity.getRequiredItems().get(itemType);

			Vector<Item> allItemsOfThisType /* = get it from db */ = null;

			mItemsDB.add(allItemsOfThisType);
		}
		
		for (long i = mActivity.getStartTime().getTime(); i
				+ mActivity.getEstimateTime() < mActivity.getEndTime()
				.getTime(); i += HOUR) {

			// mValues.add(new Value(mActivity.getId(), new Date(i), new Date( i
			// + mActivity.getEstimateTime()), ));
		}

	}

	/*
	 * (N choose K) = N! / (N-K)!K! (N choose K+1) = (N choose K) * (N-K)/(K+1)
	 * (N choose 0) = 1 ????
	 */
	public long choose(long total, long choose) {

		if (total < choose)
			return 0;

		if (choose == 0 || choose == total)
			return 1;

		return choose(total - 1, choose - 1) + choose(total - 1, choose);
	}

	//
	// public Vector<Value> getValues() {
	// return mValues;
	// }
	//
	// public void setValues(Vector<Value> pValues) {
	// mValues = pValues;
	// }

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
		return mValues.isEmpty();
	}

	public Value firstElement() {

		// TODO: return generated value which haven't been yet..
		return null;

		// return mValues.firstElement();
	}

	public void removeAll(Vector<Value> pValues) {
		// TODO: ..
		// mValues.removeAll(pValues);
	}

	public void remove(int pIndex) {
		mValues.remove(pIndex);
	}

	public void remove(Value pValue) {
		mValues.remove(pValue);
	}

	public void addAll(Vector<Value> pValues) {
		// TODO: ..
		// mValues.addAll(pValues);
	}

	public int size() {
		return mValues.size();
	}

	public Value get(int pIndex) {
		return mValues.get(pIndex);
	}
}
