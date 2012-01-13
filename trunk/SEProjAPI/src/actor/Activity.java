package actor;

import java.util.Vector;

import type.ItemType;

import State.ActivityState;

public class Activity {
	protected int priority;
	protected ActivityState state;
	protected Vector<ItemType> requiredItems;
		
	

	public Vector<ItemType> getRequiredItems() {
		return requiredItems;
	}

	public void setRequiredItems(Vector<ItemType> requiredItems) {
		this.requiredItems = requiredItems;
	}

	public ActivityState getState() {
		return state;
	}

	public void setState(ActivityState state) {
		this.state = state;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public boolean isSatisfiedPreCond(){

		return true;
	}
	
}
