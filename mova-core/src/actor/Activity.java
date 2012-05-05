package actor;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import state.ActivityState;
import type.AgentType;
import type.ItemType;

public class Activity {
	
	//TODO: use synchronized when changing the state..

	// The ID of this Activity
	protected String					mId;

	// The Type of this Activity
	protected String					mType;
	
	// The State of this Activity
	protected ActivityState				mState;

	// The Priority of this Activity
//	protected Priority					mPriority;
	
	// The Lower bound time of this Activity
	protected Timestamp					mStartTime; 
	
	// The Upper bound time of this Activity
	protected Timestamp					mEndTime;

	// The time (in milliseconds) that we assume this Activity will takes
	protected long						mEstimateTime;
	
	// Map between the required Agent of a specific type and the desired quantity
	protected Map<AgentType,Integer>	mRequiredAgents;
	
	// Map between the required Item of a specific type and the desired quantity
	protected Map<ItemType,Integer>		mRequiredItems; 
	
	// the Activities which this Activity can be performed only after they finished
	protected Set<String>				mRequiredActivityIds;
	
	// the Agents that assigned to this Activity
	protected Set<String>				mParticipatingAgentIds;
	
	// the Items that assigned to this Activity
	protected Set<String>				mParticipatingItemIds; 
	
	// The Description of this Activity
	protected String					mDescription;

	// The Name of this Activity
	protected String					mName;
	
	public Activity(String pName) {
		
		long estimateTime = 1000 * 60 * 60 * 1;
		
		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + estimateTime * 5);
		
		Map<AgentType,Integer> requiredAgents = new HashMap<AgentType,Integer>();
		requiredAgents.put(new AgentType("DEFAULT"), 1);
		requiredAgents.put(new AgentType("BLA"), 1);
		
		Map<ItemType,Integer> requiredItems = new HashMap<ItemType,Integer>();
		requiredItems.put(new ItemType("DEFAULT"), 1);
		
		init(	"DEFAULT", startTime,
				endTime, estimateTime, requiredAgents, requiredItems,
				new HashSet<String>(), "DEFAULT ACTIVITY", pName);
	}
	
	public Activity(	String					pType,
						Timestamp				pStartTime,
						Timestamp				pEndTime,
						long					pEstimateTime,
						Map<AgentType,Integer>	pRequiredAgents,
						Map<ItemType, Integer>	pRequiredItems,
						Set<String>				pRequiredActivities,
						String					pDescription,
						String					pName){
		
		init(	pType, pStartTime, pEndTime,
				pEstimateTime, pRequiredAgents, pRequiredItems,
				pRequiredActivities, pDescription, pName);
	}

	protected void init(	String					pType,
							Timestamp				pStartTime,
							Timestamp				pEndTime,
							long					pEstimateTime,
							Map<AgentType,Integer>	pRequiredAgents,
							Map<ItemType, Integer>	pRequiredItems,
							Set<String>				pRequiredActivities,
							String					pDescription,
							String					pName) {
		
		mId = UUID.randomUUID().toString();
		mType = pType;
		mState = ActivityState.PENDING;
		mStartTime = pStartTime;
		mEndTime = pEndTime;
		mEstimateTime = pEstimateTime;
		mRequiredAgents = pRequiredAgents;
		mRequiredItems = pRequiredItems;
		mRequiredActivityIds = pRequiredActivities;
		mParticipatingAgentIds = new HashSet<String>();
		mParticipatingItemIds = new HashSet<String>(); 
		mDescription = pDescription;
		mName = pName;
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		mId = pId;
	}

	public String getType() {
		return mType;
	}

	public void setType(String pType) {
		mType = pType;
	}

	public ActivityState getState() {
		return mState;
	}

	public void setState(ActivityState pState) {
		mState = pState;
	}

	public Timestamp getStartTime() {
		return mStartTime;
	}

	public void setStartTime(Timestamp pStartTime) {
		mStartTime = pStartTime;
	}

	public Timestamp getEndTime() {
		return mEndTime;
	}

	public void setEndTime(Timestamp pEndTime) {
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

	public Set<String> getParticipatingAgentIds() {
		return mParticipatingAgentIds;
	}

	public void setParticipatingAgentIds(Set<String> pParticipatingAgentIds) {
		mParticipatingAgentIds = pParticipatingAgentIds;
	}
	
	public Set<String> getParticipatingItemIds() {
		return mParticipatingItemIds;
	}

	public void setParticipatingItemIds(Set<String> pParticipatingItemIds) {
		mParticipatingItemIds = pParticipatingItemIds;
	}
	
	public Set<String> getRequiredActivityIds() {
		return mRequiredActivityIds;
	}

	public void setRequiredActivityIds(Set<String> pRequiredActivities) {
		mRequiredActivityIds = pRequiredActivities;
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
	
	public void assignAgents(Set<String> pAgents){
		mParticipatingAgentIds = pAgents;
	}
	
	public void assignItems(Set<String> pItems){
		mParticipatingItemIds = pItems;
	}
}
