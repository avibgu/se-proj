package actor;

import java.util.Vector;

import client.MovaClient;

import type.AgentType;
import utilities.Location;

public class Agent extends Entity {

	private Vector<Activity>	mActivitiesToPerform;
	private AgentType			mType;
	private Activity			mCurrentActivity;
	private int					mCurrentActivityId;
	private String				mRegistrationId;

	// The list of known locations of the items.
	private Vector<Item> mItems;

	// The items the agent has.
	private Vector<Item> mMyItems;

	public Agent(AgentType pType) {

		mActivitiesToPerform = new Vector<Activity>();
		mType = pType;
		mCurrentActivity = null;
		mRegistrationId = "";

		mItems = new Vector<Item>();
		mMyItems = new Vector<Item>();
	}

	public int getCurrentActivityId() {
		return mCurrentActivityId;
	}

	public void setCurrentActivityId(int pCurrentActivityId) {
		mCurrentActivityId = pCurrentActivityId;
	}

	public void setCurrentActivity(Activity pActivity) {
		mCurrentActivity = pActivity;
	}

	public Vector<Activity> getActivities() {
		return mActivitiesToPerform;
	}

	public void setActivities(Vector<Activity> activities) {
		mActivitiesToPerform = activities;
	}

	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}

	public AgentType getType() {
		return mType;
	}

	public String getRegistrationId() {
		return mRegistrationId;
	}

	public void setRegistrationId(String pRegistrationId) {
		mRegistrationId = pRegistrationId;
	}

	public void addActivity(Activity pActivity) {
		new MovaClient().addActivity(pActivity);
	}

	public Activity getFirstActivity() {
		return mActivitiesToPerform.get(0);
	}

	public void cleanCurrentActivities() {
		mCurrentActivity = null;
	}

	public Location findItemLocation(String pItemId) {
		for (Item item : mItems) {
			if (item.getId().equals(pItemId)) {
				return item.getLocation();
			}
		}
		return null;
	}

	public Item getItemFromTable(String pItemId) {
		for (Item item : mItems) {
			if (item.getId().equals(pItemId)) {
				return item;
			}
		}
		return null;
	}

	public void foundNewItem(String pItemId, Location pLocation) {
		new MovaClient().distributeItemLocation(pItemId, pLocation);
	}

	public Vector<Item> getMyItems() {
		return mMyItems;
	}

	public void setMyItems(Vector<Item> pMyItems) {
		mMyItems = pMyItems;
	}

	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof Agent))
			return false;

		return mId.equals(((Agent) pObj).getId());
	}

	@Override
	public int hashCode() {
		return mId.hashCode();
	}

	@Override
	public String toString() {
		return "Agent-" + mId;
	}
}
