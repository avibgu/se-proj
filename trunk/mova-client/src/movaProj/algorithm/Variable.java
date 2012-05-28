package movaProj.algorithm;

import actor.Activity;

public class Variable implements Comparable<Variable>{

	protected Activity	mActivity;
	protected Domain	mDomain;
	
	public Variable(Activity pActivity) {
		
		mActivity = pActivity;
		mDomain = new Domain(mActivity);
	}
	
	public Variable(Activity pActivity, String pMyID) {
		mActivity = pActivity;
		mDomain = new Domain(mActivity, pMyID);
	}

	public Domain getDomain() {
//		return (Domain) mDomain.clone(); TODO
		return mDomain;
	}

	@Override
	public boolean equals(Object pOther) {

		if (!(pOther instanceof Variable))
			return false;
		
		return ((Variable)pOther).mActivity.equals(mActivity);
	}

	@Override
	public int compareTo(Variable pO) {
		// TODO Auto-generated method stub
		return mActivity.compareTo(mActivity);
	}
}
