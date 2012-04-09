package algorithm;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Value {

	private Date				mActualStartTime;
	private Date				mActualEndTime;
	private Map<Agent, Integer>	mRequiredAents;
	private Map<Item, Integer>	mRequiredItems;
	private Set<Activity>		mRequiredActivities;

	public Value(Date pActualStartTime, Date pActualEndTime,
			Map<Agent, Integer> pRequiredAents,
			Map<Item, Integer> pRequiredItems, Set<Activity> pRequiredActivities) {

		mActualStartTime = pActualStartTime;
		mActualEndTime = pActualEndTime;
		mRequiredAents = pRequiredAents;
		mRequiredItems = pRequiredItems;
		mRequiredActivities = pRequiredActivities;
	}

	// true - bad for us..
	public boolean isOverlap(Value pOther) {
		// TODO consider times... it's a complex task..
		return false;
	}

	public Date getActualStartTime() {
		return mActualStartTime;
	}

	public void setActualStartTime(Date pActualStartTime) {
		this.mActualStartTime = pActualStartTime;
	}

	protected Date getActualEndTime() {
		return mActualEndTime;
	}

	protected void setActualEndTime(Date pActualEndTime) {
		this.mActualEndTime = pActualEndTime;
	}

	public Map<Agent, Integer> getRequiredAents() {
		return mRequiredAents;
	}

	public void setRequiredAents(Map<Agent, Integer> pRequiredAents) {
		mRequiredAents = pRequiredAents;
	}

	public Map<Item, Integer> getRequiredItems() {
		return mRequiredItems;
	}

	public void setRequiredItems(Map<Item, Integer> pRequiredItems) {
		mRequiredItems = pRequiredItems;
	}

	public Set<Activity> getRequiredActivities() {
		return mRequiredActivities;
	}

	public void setRequiredActivities(Set<Activity> pRequiredActivities) {
		mRequiredActivities = pRequiredActivities;
	}
}
