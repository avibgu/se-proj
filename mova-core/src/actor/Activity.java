package actor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import state.ActivityState;
import type.AgentType;
import type.ItemType;

/**
 * Each event consists of different activities. The Activity class holds
 * information such as: type, state, name, description, start time, end time,
 * required agents, required items, prerequisites activities, participating
 * agents and items and more. Each Agent receives a schedule that consists of
 * activities arranged according to start time and that do not overlap
 */
public class Activity implements Cloneable {

	// TODO: use synchronized when changing the state..

	// The ID of this Activity
	protected String mId;

	// The Type of this Activity
	protected String mType;

	// The State of this Activity
	protected ActivityState mState;

	// The Lower bound time of this Activity
	protected Timestamp mStartTime;

	// The Upper bound time of this Activity
	protected Timestamp mEndTime;

	protected Timestamp mActualStartTime;

	protected Timestamp mActualEndTime;

	// The time (in milliseconds) that we assume this Activity will take
	protected long mEstimateTime;

	// Map between the required Agent of a specific type and the desired
	// quantity
	protected Map<AgentType, Integer> mRequiredAgents;

	// Map between the required Item of a specific type and the desired quantity
	protected Map<ItemType, Integer> mRequiredItems;

	// the Activities which this Activity can be performed only after they
	// finished
	protected Set<String> mRequiredActivityIds;

	// the Agents that assigned to this Activity
	protected Set<String> mParticipatingAgentIds;

	// the Items that assigned to this Activity
	protected Set<String> mParticipatingItemIds;

	// The Description of this Activity
	protected String mDescription;

	// The Name of this Activity
	protected String mName;

	/**
	 * Creates a new empty activity
	 * 
	 * @param pName
	 *            the activity name
	 */
	public Activity(String pName) {

		long estimateTime = 1000 * 60 * 60 * 1; // HOUR

		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + estimateTime
				* 5);

		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		// requiredAgents.put(new AgentType("DEFAULT"), 1);
		// requiredAgents.put(new AgentType("BLA"), 1);

		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		// requiredItems.put(new ItemType("DEFAULT"), 1);

		init("DEFAULT", startTime, endTime, estimateTime, requiredAgents,
				requiredItems, new HashSet<String>(), "DEFAULT ACTIVITY", pName);
	}

	/**
	 * Creates a new activity with the given parameters
	 * 
	 * @param pType
	 *            the type of the activity
	 * @param pStartTime
	 *            the activity start time
	 * @param pEndTime
	 *            the activity end time
	 * @param pEstimateTime
	 *            the activity estimated duration
	 * @param pRequiredAgents
	 *            the activity map of agent type to the number of agents for
	 *            each type
	 * @param pRequiredItems
	 *            the activity map of item type to the number of items for each
	 *            type
	 * @param pRequiredActivities
	 *            the activity prerequisites
	 * @param pDescription
	 *            the activity description
	 * @param pName
	 *            the activity name
	 */
	public Activity(String pType, Timestamp pStartTime, Timestamp pEndTime,
			long pEstimateTime, Map<AgentType, Integer> pRequiredAgents,
			Map<ItemType, Integer> pRequiredItems,
			Set<String> pRequiredActivities, String pDescription, String pName) {

		init(pType, pStartTime, pEndTime, pEstimateTime, pRequiredAgents,
				pRequiredItems, pRequiredActivities, pDescription, pName);
	}

	protected void init(String pType, Timestamp pStartTime, Timestamp pEndTime,
			long pEstimateTime, Map<AgentType, Integer> pRequiredAgents,
			Map<ItemType, Integer> pRequiredItems,
			Set<String> pRequiredActivities, String pDescription, String pName) {

		mId = UUID.randomUUID().toString();
		mType = pType;
		mState = ActivityState.PENDING;
		mStartTime = pStartTime;
		mEndTime = pEndTime;
		mActualStartTime = pStartTime;
		mActualEndTime = pEndTime;
		mEstimateTime = pEstimateTime;
		mRequiredAgents = pRequiredAgents;
		mRequiredItems = pRequiredItems;
		mRequiredActivityIds = pRequiredActivities;
		mParticipatingAgentIds = new HashSet<String>();
		mParticipatingItemIds = new HashSet<String>();
		mDescription = pDescription;
		mName = pName;
	}

	/**
	 * Returns the activity id
	 * 
	 * @return the activity id
	 */
	public String getId() {
		return mId;
	}

	/**
	 * Sets the activity id
	 * 
	 * @param pId
	 *            the id to set
	 */
	public void setId(String pId) {
		mId = pId;
	}

	/**
	 * Returns the activity type
	 * 
	 * @return the activity type
	 */
	public String getType() {
		return mType;
	}

	/**
	 * Sets the activity type
	 * 
	 * @param pType
	 *            the type to set
	 */
	public void setType(String pType) {
		mType = pType;
	}

	/**
	 * Returns the activity state
	 * 
	 * @return the activity state
	 */
	public ActivityState getState() {
		return mState;
	}

	/**
	 * Sets the activity state
	 * 
	 * @param pState
	 *            the state to set
	 */
	public void setState(ActivityState pState) {
		mState = pState;
	}

	/**
	 * Returns the activity start time
	 * 
	 * @return the activity start time
	 */
	public Timestamp getStartTime() {
		return mStartTime;
	}

	/**
	 * Sets the activity start time
	 * 
	 * @param pStartTime
	 *            the start time to set
	 */
	public void setStartTime(Timestamp pStartTime) {
		mStartTime = pStartTime;
	}

	/**
	 * Returns the activity end time
	 * 
	 * @return the end time to set
	 */
	public Timestamp getEndTime() {
		return mEndTime;
	}

	/**
	 * Sets activity end time
	 * 
	 * @param pEndTime
	 *            the end time to set
	 */
	public void setEndTime(Timestamp pEndTime) {
		mEndTime = pEndTime;
	}

	public Timestamp getActualEndTime() {
		return mActualEndTime;
	}

	public void setActualEndTime(Timestamp pActualEndTime) {
		mActualEndTime = pActualEndTime;
	}

	/**
	 * Returns the activity estimated time in milliseconds
	 * 
	 * @return the activity estimated time in milliseconds
	 */
	public long getEstimateTime() {
		return mEstimateTime;
	}

	/**
	 * Sets the activity estimated time
	 * 
	 * @param pEstimateTime
	 *            the estimated time to set
	 */
	public void setEstimateTime(long pEstimateTime) {
		mEstimateTime = pEstimateTime;
	}

	/**
	 * Returns the required agents map for the activity
	 * 
	 * @return the required agents map for the activity
	 */
	public Map<AgentType, Integer> getRequiredAgents() {
		return mRequiredAgents;
	}

	/**
	 * Sets the required agents map
	 * 
	 * @param pRequiredAents
	 *            the agents map to set
	 */
	public void setRequiredAgents(Map<AgentType, Integer> pRequiredAents) {
		mRequiredAgents = pRequiredAents;
	}

	/**
	 * Returns the required items map for the activity
	 * 
	 * @return the required items map for the activity
	 */
	public Map<ItemType, Integer> getRequiredItems() {
		return mRequiredItems;
	}

	/**
	 * Sets the required items map
	 * 
	 * @param pRequiredItems
	 *            the items map to set
	 */
	public void setRequiredItems(Map<ItemType, Integer> pRequiredItems) {
		mRequiredItems = pRequiredItems;
	}

	/**
	 * Returns the participating agents in the activity
	 * 
	 * @return the participating agents in the activity
	 */
	public Set<String> getParticipatingAgentIds() {
		return mParticipatingAgentIds;
	}

	/**
	 * Sets the participating agents
	 * 
	 * @param pParticipatingAgentIds
	 *            the agents to set
	 */
	public void setParticipatingAgentIds(Set<String> pParticipatingAgentIds) {
		mParticipatingAgentIds = pParticipatingAgentIds;
	}

	/**
	 * Returns the participating items in the activity
	 * 
	 * @return the participating items in the activity
	 */
	public Set<String> getParticipatingItemIds() {
		return mParticipatingItemIds;
	}

	/**
	 * Sets the participating items
	 * 
	 * @param pParticipatingItemIds
	 *            the items to set
	 */
	public void setParticipatingItemIds(Set<String> pParticipatingItemIds) {
		mParticipatingItemIds = pParticipatingItemIds;
	}

	/**
	 * Returns the prerequisites activities
	 * 
	 * @return the prerequisites activities
	 */
	public Set<String> getRequiredActivityIds() {
		return mRequiredActivityIds;
	}

	/**
	 * Sets the prerequisites activities
	 * 
	 * @param pRequiredActivities
	 *            the prerequisites to set
	 */
	public void setRequiredActivityIds(Set<String> pRequiredActivities) {
		mRequiredActivityIds = pRequiredActivities;
	}

	/**
	 * Returns the activity description
	 * 
	 * @return the activity description
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * Sets the activity description
	 * 
	 * @param pDescription
	 *            the description to set
	 */
	public void setDescription(String pDescription) {
		mDescription = pDescription;
	}

	/**
	 * Returns the activity name
	 * 
	 * @return the activity name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the activity name
	 * 
	 * @param pName
	 *            the name to set
	 */
	public void setName(String pName) {
		mName = pName;
	}

	/**
	 * Changes to activity state to IN PROGRESS
	 */
	public void markAsInProgress() {
		mState = ActivityState.IN_PROGRESS;
	}

	/**
	 * Changes to activity state to COMPLETED
	 */
	public void markAsCompleted() {
		mState = ActivityState.COMPLETED;
	}

	/**
	 * Sets the participating agents in the activity
	 * 
	 * @param pAgents
	 *            the agents to set
	 */
	public void assignAgents(Set<String> pAgents) {
		mParticipatingAgentIds = pAgents;
	}

	/**
	 * Sets the participating items in the activity
	 * 
	 * @param pItems
	 *            the items to set
	 */
	public void assignItems(Set<String> pItems) {
		mParticipatingItemIds = pItems;
	}

	public Timestamp getActualStartTime() {
		return mActualStartTime;
	}

	public void setActualStartTime(Timestamp pActualStartTime) {
		mActualStartTime = pActualStartTime;
	}

	/**
	 * Compares to activities according to activity id
	 * 
	 * @param pActivity
	 *            the activity to compare
	 * @return 0 if equal, -1 otherwise
	 */
	public int compareTo(Activity pActivity) {
		return mId.equals(pActivity.getId()) ? 0 : -1;
	}

	@Override
	public Activity clone() throws CloneNotSupportedException {

		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		
		for (AgentType agentType : mRequiredAgents.keySet())
			requiredAgents.put(agentType, mRequiredAgents.get(agentType));
		
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		
		for (ItemType itemType : mRequiredItems.keySet())
			requiredItems.put(itemType, mRequiredItems.get(itemType));
		
		Set<String> requiredActivities = new HashSet<String>();
		
		for (String activityID : mRequiredActivityIds)
			requiredActivities.add(activityID);
		
		Activity activity = new Activity(mType, (Timestamp)mStartTime.clone(), (Timestamp)mEndTime.clone(),
				mEstimateTime, requiredAgents, requiredItems,
				requiredActivities, mDescription, mName);
		
		activity.setId(mId);
		
		return activity;
	}
	
	@Override
	public boolean equals(Object pActivity) {
		return mId.equals(((Activity)pActivity).getId());
	}
}
