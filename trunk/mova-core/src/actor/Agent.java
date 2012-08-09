package actor;

import java.util.Vector;

import client.MovaClient;

import type.AgentType;
import utilities.Location;
/**
 * An event will be managed using agents.
 * Each Agent has a queue of activities to perform, 
 * a set of old activities for better analyzing future activities 
 * and the current activity it is participating in.
 * Each agent has an agent type and a registration id that it receives from the Google's GCM servers for push messages. 
 */
public class Agent extends Entity {
	/**
	 * The agent's activities to perform
	 */
	private Vector<Activity>	mActivitiesToPerform;
	/**
	 * The agent type
	 */
	private AgentType			mType;
	/**
	 * The current activity the agent is participating in
	 */
	private Activity			mCurrentActivity;
	/**
	 * The current activity id the agent is participating in
	 */
	private int					mCurrentActivityId;
	/**
	 * The agent's registration id
	 */
	private String				mRegistrationId;

	// The list of known locations of the items.
	/**
	 * The list of items that the agent is aware about
	 */
	private Vector<Item> mItems;

	// The items the agent has.
	/**
	 * The list of items currently being held by the agent
	 */
	private Vector<Item> mMyItems;
	/**
	 * Creates a new Agent with the given agent type.
	 * @param pType the type to associate the agent with.
	 */
	public Agent(AgentType pType) {

		mActivitiesToPerform = new Vector<Activity>();
		mType = pType;
		mCurrentActivity = null;
		mRegistrationId = "";

		mItems = new Vector<Item>();
		mMyItems = new Vector<Item>();
	}
	/**
	 * Returns the id of the current activity the agent is participating in.
	 * @return the id of the current activity the agent is participating in.
	 */
	public int getCurrentActivityId() {
		return mCurrentActivityId;
	}
	/**
	 * Sets the id of the current activity the agent is participating in.
	 * @param pCurrentActivityId the id of the current activity the agent is participating in.
	 */
	public void setCurrentActivityId(int pCurrentActivityId) {
		mCurrentActivityId = pCurrentActivityId;
	}
	/**
	 * Sets the current activity the agent is participating in.
	 * @param pActivity the current activity the agent is participating in.
	 */
	public void setCurrentActivity(Activity pActivity) {
		mCurrentActivity = pActivity;
	}
	/**
	 * Returns the agent's activities to perform.
	 * @return the agent's activities to perform.
	 */
	public Vector<Activity> getActivities() {
		return mActivitiesToPerform;
	}
	/**
	 * Sets the agent's activities to perform.
	 * @param activities the agent's activities to perform.
	 */
	public void setActivities(Vector<Activity> activities) {
		mActivitiesToPerform = activities;
	}
	/**
	 * Returns the current activity the agent is participating in.
	 * @return the current activity the agent is participating in.
	 */
	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}
	/**
	 * Returns the agent's type.
	 * @return the agent's type.
	 */
	public AgentType getType() {
		return mType;
	}
	/**
	 * Returns the agent's registration id.
	 * @return the agent's registration id.
	 */
	public String getRegistrationId() {
		return mRegistrationId;
	}
	/**
	 * Sets the agent's registration id.
	 * @param pRegistrationId the agent's registration id.
	 */
	public void setRegistrationId(String pRegistrationId) {
		mRegistrationId = pRegistrationId;
	}
	/**
	 * Adds a new activity to the system.
	 * @param pActivity the new activity to add.
	 */
	public void addActivity(Activity pActivity) {
		new MovaClient().addActivity(pActivity);
	}
	/**
	 * Returns the next activity.
	 * @return the next activity.
	 */
	public Activity getFirstActivity() {
		return mActivitiesToPerform.get(0);
	}
	/**
	 * Clears the current activity
	 */
	public void cleanCurrentActivities() {
		mCurrentActivity = null;
	}
	/**
	 * Returns the item current location.
	 * @param pItemId the item id to search for.
	 * @return the item current location.
	 */
	public Location findItemLocation(String pItemId) {
		for (Item item : mItems) {
			if (item.getId().equals(pItemId)) {
				return item.getLocation();
			}
		}
		return null;
	}
	/**
	 * Returns the item with the given id.
	 * @param pItemId the id to search for.
	 * @return the item with the given id.
	 */
	public Item getItemFromTable(String pItemId) {
		for (Item item : mItems) {
			if (item.getId().equals(pItemId)) {
				return item;
			}
		}
		return null;
	}
	/**
	 * Distribute an item's new location to other agents
	 * @param pItemId the item id
	 * @param pLocation the new location found
	 */
	public void foundNewItem(String pItemId, Location pLocation) {
		new MovaClient().distributeItemLocation(pItemId, pLocation);
	}
	/**
	 * Returns the items that are held by the agent.
	 * @return the items that are held by the agent.
	 */
	public Vector<Item> getMyItems() {
		return mMyItems;
	}
	/**
	 * Sets the items that are to be held by the agent.
	 * @param pMyItems the items that are to be held by the agent.
	 */
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
