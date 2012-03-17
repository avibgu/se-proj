package actor;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

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
	
	// The Due-Date of this Activity
	protected Date						mDueDate; 
	
	// Map between the required Agent of a specific type and the desired quantity
	protected Map<AgentType,Integer>	mRequiredAents;
	
	// Map between the required Item of a specific type and the desired quantity
	protected Map<ItemType,Integer>		mRequiredItems; 
	
	public Activity(	ActivityType			pType,
						Priority				pPriority,
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
	
	public Priority getPriority() {
		return mPriority;
	}

	public long getDueDate() {
		return mDueDate.getTime();
	}
	
	
	
	@Deprecated
	public void setState(ActivityState pState) {
		this.mState = pState;
	}

	@Deprecated
	public Vector<ItemType> getRequiredItems() {
		return new Vector<ItemType>();
	}

	@Deprecated
	public boolean isSatisfiedPreCond(){
		return true;
	}
}
