package algorithm;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import type.AgentType;
import type.ItemType;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class OldDomain implements Cloneable {

	private static final long HOUR = 1000 * 60 * 60;

	protected Activity mActivity;
	protected Vector<Value> mValues;

	public OldDomain(Activity pActivity) {

		mActivity = pActivity;
		initValues();
	}

	public OldDomain(Activity pActivity, Vector<Value> pValues) {

		mActivity = pActivity;
		mValues = pValues;
	}

	protected void initValues() {

		setValues(new Vector<Value>());

		for (AgentType agentType : mActivity.getRequiredAgents().keySet()) {

			// Integer numOfrequiredAgents =
			// mActivity.getRequiredAgents().get(agentType);

			Vector<Agent> allAgentsOfThisType /* = get it from db */;

			for (ItemType itemType : mActivity.getRequiredItems().keySet()) {

				// Integer numOfrequiredItems =
				// mActivity.getRequiredItems().get(itemType);

				Vector<Item> allItemsOfThisType /* = get it from db */;

				for (long i = mActivity.getStartTime().getTime(); i
						+ mActivity.getEstimateTime() < mActivity.getEndTime()
						.getTime(); i += HOUR) {

//					 mValues.add(new Value(mActivity.getId(), new Date(i), new Date( i + mActivity.getEstimateTime()), ));
				}
			}
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

	public Vector<Value> getValues() {
		return mValues;
	}

	public void setValues(Vector<Value> pValues) {
		mValues = pValues;
	}

	@Override
	protected OldDomain clone() {

		Vector<Value> tValues = new Vector<Value>(mValues.size());

		for (Value value : mValues)
			tValues.add(value);

		return new OldDomain(mActivity, tValues);
	}

	public boolean isEmpty() {
		return mValues.isEmpty();
	}

	public Value firstElement() {
		
		// TODO: return generated value which haven't been yet..
		
		return mValues.firstElement();
	}

	public void removeAll(Vector<Value> pValues) {
		mValues.removeAll(pValues);
	}

	public void remove(int pIndex) {
		mValues.remove(pIndex);
	}

	public void remove(Value pValue) {
		mValues.remove(pValue);
	}

	public void addAll(Vector<Value> pValues) {
		mValues.addAll(pValues);
	}

	public int size() {
		return mValues.size();
	}

	public Value get(int pIndex) {
		return mValues.get(pIndex);
	}
}
