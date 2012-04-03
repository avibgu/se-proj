package algorithm;

import java.util.Vector;

import type.AgentType;

import actor.Activity;
import actor.Agent;

public class Domain {

	protected Activity		mActivity;
	protected Vector<Value>	mValues;
	
	public Domain(Activity pActivity) {

		mActivity = pActivity;
		
		initValues();
	}

	protected void initValues() {
		
		mValues = new Vector<Value>();
		
//		mValues.add(new Value(..));
		
		for (AgentType agentType : mActivity.getRequiredAgents().keySet()){
			
			Integer numOfrequiredAgents = mActivity.getRequiredAgents().get(agentType);
			
			Vector<Agent> allAgentsOfThisType /* = get it from db */;
//			
//			TODO
//			
//			for (/* all agent:Agent such that its Type is type */){
//				;
//			}
		}
	}
}
