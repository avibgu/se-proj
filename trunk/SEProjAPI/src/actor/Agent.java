package actor;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;

import type.AgentType;
import type.ItemType;

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
		 // TODO Auto-generated method stub
	}
	
	public void stop() {
		dontStop = false;
	}
	
	
	public void insertActivity(Activity newActivity){
		activities.add(newActivity);
	}
}
