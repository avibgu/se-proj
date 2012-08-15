package movaProj.algorithm;

import java.util.List;

import actor.Activity;
import actor.Agent;
import actor.Item;

/**
 * This class represents an Activity for the scheduling Algorithm.
 * It holds the Activity which it belongs to, and the Domain of the possible Values.
 */
public class Variable implements Comparable<Variable> {

	protected Activity mActivity;
	protected Domain mDomain;

	public Variable(Activity pActivity,
			List<Activity> pActivities, List<Agent> pAgents, List<Item> pItems) {
		mActivity = pActivity;
		mDomain = new Domain(mActivity, pActivities, pAgents, pItems);
	}

	public Domain getDomain() {
		// TODO: return (Domain) mDomain.clone(); 
		return mDomain;
	}

	@Override
	public boolean equals(Object pOther) {

		if (!(pOther instanceof Variable))
			return false;

		return ((Variable) pOther).mActivity.equals(mActivity);
	}

	@Override
	public int compareTo(Variable pO) {
		return mActivity.compareTo(mActivity);
	}
}
