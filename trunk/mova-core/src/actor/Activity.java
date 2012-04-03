package actor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
	protected Map<AgentType,Integer>	mRequiredAents;
	
	// Map between the required Item of a specific type and the desired quantity
	protected Map<ItemType,Integer>		mRequiredItems; 
	
	// The Description of this Activity
	protected String					mDescription;

	// The Name of this Activity
	protected String					mName;
	
	public Activity(String pName) {
		
		long estimateTime = 1000 * 60 * 60 * 1;
		
		Date startTime = new Date();
		Date endTime = new Date(startTime.getTime() + estimateTime * 5);
		
		Map<AgentType,Integer> requiredAents = new HashMap<AgentType,Integer>();
		requiredAents.put(AgentType.COORDINATOR, 1);
		
		Map<ItemType,Integer> requiredItems = new HashMap<ItemType,Integer>();
		requiredItems.put(ItemType.LAPTOP, 1);
		
		init(ActivityType.PRESENTATION, Priority.MEDIUM, startTime,
				endTime, estimateTime, requiredAents, requiredItems,
				"DEFAULT ACTIVITY", pName);
	}
	
	public Activity(	ActivityType			pType,
						Priority				pPriority,
						Date					pStartTime,
						Date					pEndTime,
						long					pEstimateTime,
						Map<AgentType,Integer>	pRequiredAents,
						Map<ItemType, Integer>	pRequiredItems,
						String					pDescription,
						String					pName){
		
		init(pType, pPriority, pStartTime, pEndTime, pEstimateTime, pRequiredAents, pRequiredItems,
				pDescription, pName);
	}

	protected void init(	ActivityType			pType,
							Priority				pPriority,
							Date					pStartTime,
							Date					pEndTime,
							long					pEstimateTime,
							Map<AgentType,Integer>	pRequiredAents,
							Map<ItemType, Integer>	pRequiredItems,
							String					pDescription,
							String					pName) {
		
		mId = UUID.randomUUID().toString();
		mType = pType;
		mState = ActivityState.PENDING;
		mPriority = pPriority;
		mStartTime = pStartTime;
		mEndTime = pEndTime;
		mEstimateTime = pEstimateTime;
		mRequiredAents = pRequiredAents;
		mRequiredItems = pRequiredItems;
		mDescription = pDescription;
		mName = pName;
	}

	public Priority getPriority() {
		return mPriority;
	}

	public long getEndTime() {
		return mStartTime.getTime();
	}
	
	public void setPriority(Priority pPriority) {
		this.mPriority = pPriority;
	}

	public Map<ItemType,Integer> getRequiredItems() {
		return mRequiredItems;
	}

	public Map<AgentType,Integer> getRequiredAgents() {
		return mRequiredAents;
	}
	
	public ActivityType getType() {
		return mType;
	}

	public void setType(ActivityType pType) {
		this.mType = pType;
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		this.mId = pId;
	}
	
	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String pDescription) {
		this.mDescription = pDescription;
	}

	public String getName() {
		return mName;
	}

	public void setName(String pName) {
		this.mName = pName;
	}

	public boolean isTopPriority() {
		return mPriority == Priority.URGENT;
	}

	public void markAsInProgress() {
		mState = ActivityState.IN_PROGRESS;
	}

	public void markAsCompleted() {
		mState = ActivityState.COMPLETED;
	}
}
