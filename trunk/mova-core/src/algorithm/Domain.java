package algorithm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	protected List<List<Agent>> mAgentsDB;
	protected List<List<Item>> mItemsDB;

	protected List<List<Integer>> mAgentsIndexes;
	protected List<List<Integer>> mItemsIndexes;

	protected List<Pair<Date, Date>> mTimes;
	protected Integer mTimesIndex;

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

		mAgentsDB = new ArrayList<List<Agent>>();
		mAgentsIndexes = new ArrayList<List<Integer>>();

		for (AgentType agentType : mActivity.getRequiredAgents().keySet()) {

			List<Agent> allAgentsOfThisType /* = get it from db */= null;

			mAgentsDB.add(allAgentsOfThisType);

			List<Integer> tList = new ArrayList<Integer>();

			Integer numOfrequiredAgents = mActivity.getRequiredAgents().get(
					agentType);

			for (int i = 0; i < numOfrequiredAgents; i++)
				tList.add(new Integer(i));

			mAgentsIndexes.add(tList);
		}

		mItemsDB = new ArrayList<List<Item>>(/* numOfItemsTypes */);
		mItemsIndexes = new ArrayList<List<Integer>>();

		for (ItemType itemType : mActivity.getRequiredItems().keySet()) {

			Vector<Item> allItemsOfThisType /* = get it from db */= null;

			mItemsDB.add(allItemsOfThisType);

			List<Integer> tList = new ArrayList<Integer>();

			Integer numOfrequiredItems = mActivity.getRequiredItems().get(
					itemType);

			for (int i = 0; i < numOfrequiredItems; i++)
				tList.add(new Integer(i));

			mAgentsIndexes.add(tList);
		}

		mTimes = new ArrayList<Pair<Date, Date>>();
		mTimesIndex = 0;
		
		for (long i = mActivity.getStartTime().getTime(); i
				+ mActivity.getEstimateTime() < mActivity.getEndTime()
				.getTime(); i += HOUR) {

			mTimes.add(new Pair<Date, Date>(new Date(i), new Date(i
					+ mActivity.getEstimateTime())));
		}
	}

	public Value nextValue() {

		String hashKey = getHashKeyOfCurrentIndexes();
		
		Value value = mValues.get(hashKey);
		
		if (null == value){
			
			value = constructValueFromIndexes();
			mValues.put(hashKey, value);
		}

		incrementIndexes();
		
		return value;
	}
	
	private void incrementIndexes() {
		// TODO Auto-generated method stub
		
	}

	private Value constructValueFromIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getHashKeyOfCurrentIndexes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetIndexes(){
		//TODO
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
