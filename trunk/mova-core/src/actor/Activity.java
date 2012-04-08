package actor;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import priority.Priority;

import state.ActivityState;
import type.ActivityType;
import type.AgentType;
import type.ItemType;


public class Activity {
	
	//TODO: use synchronized when changing the state..

	// The ID of this Activity
	protected String					mId;

	// The Type of this Activity
	protected ActivityType				mType;
	
	// The State of this Activity
	protected ActivityState				mState;

	// The Priority of this Activity
	protected Priority					mPriority;
	
	// The Lower bound time of this Activity
	protected Date						mStartTime; 
	
	// The Upper bound time of this Activity
	protected Date						mEndTime;

	// The time (in milliseconds) that we assume this Activity will takes
	protected long						mEstimateTime;
	
	// Map between the required Agent of a specific type and the desired quantity
	protected Map<AgentType,Integer>	mRequiredAgents;
	
	// Map between the required Item of a specific type and the desired quantity
	protected Map<ItemType,Integer>		mRequiredItems; 
	
	// the Activities which this Activity can be performed only after they finished
	protected Set<Activity>				mRequiredActivities;
	
	// The Description of this Activity
	protected String					mDescription;

	// The Name of this Activity
	protected String					mName;
	
	public Activity(String pName) {
		
		long estimateTime = 1000 * 60 * 60 * 1;
		
		Date startTime = new Date();
		Date endTime = new Date(startTime.getTime() + estimateTime * 5);
		
		Map<AgentType,Integer> requiredAgents = new HashMap<AgentType,Integer>();
		requiredAgents.put(AgentType.COORDINATOR, 1);
		
		Map<ItemType,Integer> requiredItems = new HashMap<ItemType,Integer>();
		requiredItems.put(ItemType.LAPTOP, 1);
		
		init(	ActivityType.PRESENTATION, Priority.MEDIUM, startTime,
				endTime, estimateTime, requiredAgents, requiredItems,
				new HashSet<Activity>(), "DEFAULT ACTIVITY", pName);
	}
	
	public Activity(	ActivityType			pType,
						Priority				pPriority,
						Date					pStartTime,
						Date					pEndTime,
						long					pEstimateTime,
						Map<AgentType,Integer>	pRequiredAgents,
						Map<ItemType, Integer>	pRequiredItems,
						Set<Activity>			pRequiredActivities,
						String					pDescription,
						String					pName){
		
		init(	pType, pPriority, pStartTime, pEndTime,
				pEstimateTime, pRequiredAgents, pRequiredItems,
				pRequiredActivities, pDescription, pName);
	}

	protected void init(	ActivityType			pType,
							Priority				pPriority,
							Date					pStartTime,
							Date					pEndTime,
							long					pEstimateTime,
							Map<AgentType,Integer>	pRequiredAgents,
							Map<ItemType, Integer>	pRequiredItems,
							Set<Activity>			pRequiredActivities,
							String					pDescription,
							String					pName) {
		
		mId = UUID.randomUUID().toString();
		mType = pType;
		mState = ActivityState.PENDING;
		mPriority = pPriority;
		mStartTime = pStartTime;
		mEndTime = pEndTime;
		mEstimateTime = pEstimateTime;
		mRequiredAgents = pRequiredAgents;
		mRequiredItems = pRequiredItems;
		mRequiredActivities = pRequiredActivities;
		mDescription = pDescription;
		mName = pName;
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		mId = pId;
	}

	public ActivityType getType() {
		return mType;
	}

	public void setType(ActivityType pType) {
		mType = pType;
	}

	public ActivityState getState() {
		return mState;
	}

	public void setState(ActivityState pState) {
		mState = pState;
	}

	public Priority getPriority() {
		return mPriority;
	}

	public void setPriority(Priority pPriority) {
		mPriority = pPriority;
	}

	public Date getStartTime() {
		return mStartTime;
	}

	public void setStartTime(Date pStartTime) {
		mStartTime = pStartTime;
	}

	public Date getEndTime() {
		return mEndTime;
	}

	public void setEndTime(Date pEndTime) {
		mEndTime = pEndTime;
	}

	public long getEstimateTime() {
		return mEstimateTime;
	}

	public void setEstimateTime(long pEstimateTime) {
		mEstimateTime = pEstimateTime;
	}

	public Map<AgentType, Integer> getRequiredAgents() {
		return mRequiredAgents;
	}

	public void setRequiredAgents(Map<AgentType, Integer> pRequiredAents) {
		mRequiredAgents = pRequiredAents;
	}

	public Map<ItemType, Integer> getRequiredItems() {
		return mRequiredItems;
	}

	public void setRequiredItems(Map<ItemType, Integer> pRequiredItems) {
		mRequiredItems = pRequiredItems;
	}

	public Set<Activity> getRequiredActivities() {
		return mRequiredActivities;
	}

	public void setRequiredActivities(Set<Activity> pRequiredActivities) {
		mRequiredActivities = pRequiredActivities;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String pDescription) {
		mDescription = pDescription;
	}

	public String getName() {
		return mName;
	}

	public void setName(String pName) {
		mName = pName;
	}
	
	public void markAsInProgress() {
		mState = ActivityState.IN_PROGRESS;
	}

	public void markAsCompleted() {
		mState = ActivityState.COMPLETED;
	}

	public boolean isTopPriority() {
		return mPriority == Priority.URGENT;
	}
}
