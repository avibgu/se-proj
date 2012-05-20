package algorithm;

import actor.Activity;

public class Variable {

	protected Activity	mActivity;
	protected Domain	mDomain;
	
	public Variable(Activity pActivity) {
		
		mActivity = pActivity;
		mDomain = new Domain(mActivity);
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
}
