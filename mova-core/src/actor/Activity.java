package actor;

import java.util.UUID;
import java.util.Vector;

import state.ActivityState;
import type.ItemType;


public class Activity {
	
	protected String id;
	protected int priority;
	protected ActivityState state;
	protected Vector<ItemType> requiredItems;
	
	public Activity(){
		id = UUID.randomUUID().toString();
		requiredItems = new Vector<ItemType>();
	}
	
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
	
	public String getId(){
		return id;
	}
}
