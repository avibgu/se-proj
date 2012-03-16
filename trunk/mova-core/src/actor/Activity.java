package actor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import state.ActivityState;
import type.ActivityType;
import type.AgentType;
import type.ItemType;


public class Activity {
	
	// The ID of this Activity
	protected String					mId;

	// The Type of this Activity
	protected ActivityType				mType;
	
	// The State of this Activity
	protected ActivityState				mState;

	// The Priority of this Activity
	protected int						mPriority;
	
	// The Due-Date of this Activity
	protected Date						mDueDate; 
	
	// Map between the required Agent of a specific type and the desired quantity
	protected Map<AgentType,Integer>	mRequiredAents;
	
	// Map between the required Item of a specific type and the desired quantity
	protected Map<ItemType,Integer>		mRequiredItems; 
	
	public Activity(	ActivityType			pType,
						int						pPriority,
						Date					pDueDate,
						Map<AgentType,Integer>	pRequiredAents,
						Map<ItemType,Integer>	pRequiredItems	){
		
		mId = UUID.randomUUID().toString();
		mType = pType;
		mState = ActivityState.PENDING;
		mPriority = pPriority;
		mDueDate = pDueDate; 
		mRequiredAents = pRequiredAents;
		mRequiredItems = pRequiredItems; 
	}
	
	
	
	
	
	
	@Deprecated
	protected String id;
	@Deprecated
	protected int priority;
	@Deprecated
	protected ActivityState state;
	@Deprecated
	protected Vector<ItemType> requiredItems;	
	
	@Deprecated
	public Activity(){
		id = UUID.randomUUID().toString();
		requiredItems = new Vector<ItemType>();
	}
	
	@Deprecated
	public Vector<ItemType> getRequiredItems() {
		return requiredItems;
	}

	@Deprecated
	public void setRequiredItems(Vector<ItemType> requiredItems) {
		this.requiredItems = requiredItems;
	}

	@Deprecated
	public ActivityState getState() {
		return state;
	}

	@Deprecated
	public void setState(ActivityState state) {
		this.state = state;
	}

	@Deprecated
	public int getPriority() {
		return priority;
	}

	@Deprecated
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Deprecated
	public boolean isSatisfiedPreCond(){

		return true;
	}
	
	@Deprecated
	public String getId(){
		return id;
	}
}
