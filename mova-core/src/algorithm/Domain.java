package algorithm;

import java.util.Vector;

import actor.Activity;

public class Domain implements Cloneable {

	protected Activity		mActivity;
	protected Vector<Value>	mValues;
	
	public Domain(Activity pActivity) {

		mActivity = pActivity;
		initValues();
	}
	
	public Domain(Activity pActivity, Vector<Value>	pValues) {

		mActivity = pActivity;
		mValues = pValues;
	}

	protected void initValues() {
		
		setValues(new Vector<Value>());
		
//		mValues.add(new Value(..));
		
//		for (AgentType agentType : mActivity.getRequiredAgents().keySet()){
//			
//			Integer numOfrequiredAgents = mActivity.getRequiredAgents().get(agentType);
//			
//			Vector<Agent> allAgentsOfThisType /* = get it from db */;
//			
//			TODO
//			
//			for (/* all agent:Agent such that its Type is type */){
//				;
//			}
//		}
	}

	public Vector<Value> getValues() {
		return mValues;
	}

	public void setValues(Vector<Value> pValues) {
		mValues = pValues;
	}
	
	@Override
	protected Domain clone() {

		Vector<Value> tValues = new Vector<Value>(mValues.size());
		
		for (Value value : mValues)
			tValues.add(value);
		
		return new Domain(mActivity, tValues);
	}

	public boolean isEmpty() {
		return mValues.isEmpty();
	}

	public Value firstElement() {
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
