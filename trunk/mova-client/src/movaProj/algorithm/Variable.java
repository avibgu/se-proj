package movaProj.algorithm;

import java.util.List;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Variable implements Comparable<Variable> {

	protected Activity mActivity;
	protected Domain mDomain;

	public Variable(Activity pActivity) {

		mActivity = pActivity;
		mDomain = new Domain(mActivity);
	}

	public Variable(Activity pActivity, String pMyID,
			List<Activity> pActivities, List<Agent> pAgents, List<Item> pItems) {
		mActivity = pActivity;
		mDomain = new Domain(mActivity, pMyID, pActivities, pAgents, pItems);
	}

	public Domain getDomain() {
		// return (Domain) mDomain.clone(); TODO
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
		// TODO Auto-generated method stub
		return mActivity.compareTo(mActivity);
	}
}
