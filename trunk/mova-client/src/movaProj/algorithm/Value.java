package movaProj.algorithm;

import java.util.Date;
import java.util.Set;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Value {

	private String		mActivitiyID;
	private Date		mActualStartTime;
	private Date		mActualEndTime;
	private Set<Agent>	mRequiredAgents;
	private Set<Item>	mRequiredItems;
	private Set<String>	mRequiredActivities;

	public Value(String pActivitiyID, Date pActualStartTime,
			Date pActualEndTime, Set<Agent> pRequiredAgents,
			Set<Item> pRequiredItems, Set<String> pRequiredActivities) {

		mActivitiyID = pActivitiyID;
		mActualStartTime = pActualStartTime;
		mActualEndTime = pActualEndTime;
		mRequiredAgents = pRequiredAgents;
		mRequiredItems = pRequiredItems;
		mRequiredActivities = pRequiredActivities;
	}

	// true - bad for us..
	public boolean areConflicting(Value pOther) {

		if (mActualStartTime.getTime() >= pOther.getActualEndTime().getTime()
				|| mActualEndTime.getTime() <= pOther.getActualStartTime()
						.getTime())
			return arePreRequirmentsConflicting(pOther);

		for (Agent agent : pOther.getRequiredAgents())
			if (mRequiredAgents.contains(agent))
				return true;

		for (Item item : pOther.getRequiredItems())
			if (mRequiredItems.contains(item))
				return true;

		return arePreRequirmentsConflicting(pOther);
	}

	// return true if one of the activities is required by the other activity
	// and they are not in the proper order
	private boolean arePreRequirmentsConflicting(Value pOther) {

		if (mRequiredActivities.contains(pOther.getActivitiyID())
				&& pOther.getActualEndTime().getTime() > mActualStartTime
						.getTime())
			return true;

		if (pOther.getRequiredActivities().contains(mActivitiyID)
				&& mActualEndTime.getTime() > pOther.getActualStartTime()
						.getTime())
			return true;

		return false;
	}
	
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		
		sb.append("\nActivitiy ID: " + mActivitiyID + "\n");
		sb.append("Actual Start Time: " + mActualStartTime + "\n");
		sb.append("Actual End Time: " + mActualEndTime + "\n");
		sb.append("Required Aents: " + mRequiredAgents + "\n");
		sb.append("Required Items: " + mRequiredItems + "\n");
		sb.append("Required Activities: " + mRequiredActivities + "\n");
		
		return sb.toString();
	}

	public String getActivitiyID() {
		return mActivitiyID;
	}

	public void setActivitiyID(String pActivitiyID) {
		mActivitiyID = pActivitiyID;
	}

	public Date getActualStartTime() {
		return mActualStartTime;
	}

	public void setActualStartTime(Date pActualStartTime) {
		this.mActualStartTime = pActualStartTime;
	}

	public Date getActualEndTime() {
		return mActualEndTime;
	}

	public void setActualEndTime(Date pActualEndTime) {
		this.mActualEndTime = pActualEndTime;
	}

	public Set<Agent> getRequiredAgents() {
		return mRequiredAgents;
	}

	public void setRequiredAgents(Set<Agent> pRequiredAgents) {
		mRequiredAgents = pRequiredAgents;
	}

	public Set<Item> getRequiredItems() {
		return mRequiredItems;
	}

	public void setRequiredItems(Set<Item> pRequiredItems) {
		mRequiredItems = pRequiredItems;
	}

	public Set<String> getRequiredActivities() {
		return mRequiredActivities;
	}

	public void setRequiredActivities(Set<String> pRequiredActivities) {
		mRequiredActivities = pRequiredActivities;
	}

	public Activity getActivity() {
		// TODO Auto-generated method stub
//		return new Activity(pType, mActualStartTime, mActualEndTime, mActualEndTime.getTime() - mActualStartTime.getTime(), mRequiredAgents, mRequiredItems, mRequiredActivities, pDescription, pName);
		return null;
	}
}
