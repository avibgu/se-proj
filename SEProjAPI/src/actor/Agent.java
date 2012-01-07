package actor;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

import type.AgentType;
import type.ItemType;

import State.ActivityState;
import State.ItemState;
import algorithm.ActivityMonitor;

public class Agent implements Runnable{

	protected ActivityMonitor	activityMonitor;
	protected PriorityQueue<Activity>	activities;
	protected AgentType			type;
	protected String			id;
	protected boolean			dontStop;
	protected Vector<Item> 		items; // The list of known locations of the items.
	protected Vector<Item>		myItems; // The items the agent has.
	protected Activity currentActivity;
	
	public ActivityMonitor getActivityMonitor() {
		return activityMonitor;
	}

	public void setActivityMonitor(ActivityMonitor activityMonitor) {
		this.activityMonitor = activityMonitor;
	}

	public PriorityQueue<Activity> getActivities() {
		return activities;
	}

	public void setActivities(PriorityQueue<Activity> activities) {
		this.activities = activities;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDontStop() {
		return dontStop;
	}

	public void setDontStop(boolean dontStop) {
		this.dontStop = dontStop;
	}

	public Vector<Item> getItems() {
		return items;
	}

	public void setItems(Vector<Item> items) {
		this.items = items;
	}

	public Vector<Item> getMyItems() {
		return myItems;
	}

	public void setMyItems(Vector<Item> myItems) {
		this.myItems = myItems;
	}

	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		this.currentActivity = currentActivity;
	}

	public Agent(AgentType type) {

		activities = new PriorityQueue<Activity>(11,new Comparator<Activity>() {

			@Override
			public int compare(Activity o1,Activity  o2) {
				if (o1.getPriority() < o2.getPriority())
					return -1;
				else if(o1.getPriority() > o2.getPriority())
					return 1;
				return 0;
			}
		});
		this.activityMonitor = new ActivityMonitor(activities);
		this.type = type;
		this.dontStop = true;
	}

	@Override
	public void run() { 
		
		new Thread(activityMonitor).start();
		
		while (dontStop){
			
			Activity activity = findNextActivity();
			
			currentActivity = activity;
			
			performActivity(activity);
		}
		
		activityMonitor.stop();		
	}

	protected Activity findNextActivity() {
		for (Activity act : activities){
			if (!act.isSatisfiedPreCond()){
				continue;
			}
			Vector<ItemType> requiredItems = act.getRequiredItems();
			for (ItemType req_item : requiredItems){
				for (Item item : items){
					if (item.getType().equals(req_item) && (item.state == ItemState.AVAILABLE)){
						item.setState(ItemState.BUSY);
						myItems.add(item);
						break;
					}
				}
				// If we got here, it means we didn't find any item from the required item type which available. We will release all the other
				// items we've captured, and look for another activity.
				releaseMyItems();
				continue;
			}
			return act;
			
		}
		return null; // No activity was found.
	}
	
	private void releaseMyItems() {
		for (Item item : myItems){
			item.setState(ItemState.AVAILABLE);
		}
		
	}

	protected void performActivity(Activity activity) {
		 activity.setState(ActivityState.IN_PROGRESS);
	}
	
	protected void completeCurrentActivity(){
		releaseMyItems();
		currentActivity.setState(ActivityState.COMPLETED);
		currentActivity = null;
	}
	
	public void stop() {
		dontStop = false;
	}
	
	
	public void insertActivity(Activity newActivity){
		activities.add(newActivity);
	}
}
