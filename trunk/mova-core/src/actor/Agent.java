package actor;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;

import simulator.Location;
import state.ActivityState;
import state.ItemState;
import type.AgentType;
import type.ItemType;

import algorithm.ActivityMonitor;

public class Agent extends Entity implements Runnable {

	protected ActivityMonitor			mActivityMonitor;
	protected PriorityQueue<Activity>	mActivitiesToPerform;
	protected Set<Activity>				mOldActivities;
	protected AgentType					mType;
	protected boolean					mDontStop;
	protected Activity					mCurrentActivity;
	
	public Agent(AgentType pType) {

		mActivityMonitor = new ActivityMonitor(this);
		
		mActivitiesToPerform = new PriorityQueue<Activity>(7, new Comparator<Activity>() {

			@Override
			public int compare(Activity o1,Activity  o2) {
				return o1.getPriority().compareTo(o2.getPriority());
			}
		});
		
		mOldActivities = new HashSet<Activity>();
		mType = pType;
		mDontStop = true;
		mCurrentActivity = null;
	}
	
	@Override
	public void run() { 
		
		new Thread(mActivityMonitor).start();
		
		while (mDontStop){
			
			setCurrentActivity(findNextActivity());
			performCurrentActivity();
		}
		
		mActivityMonitor.stop();
	}
	
	public void stop() {
		mDontStop = false;
	}
	
	public void setCurrentActivity(Activity activity) {
		mCurrentActivity = activity;
	}
	
	
	
	// From Here: methods that others uses to notify this agent about things:

	
	public void tooManyTopPriorityActivities(int howManyOverTheLimit) {
		// TODO Auto-generated method stub
	}

	public void activityChangedHisPriority(Activity activity) {
		// TODO Auto-generated method stub
	}

	public void goigToMissThisActivity(Activity activity) {
		// TODO Auto-generated method stub
	}

	public long getAverageTimePerActivity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// TODO: check if there are duplication between Agent and Entity
	
	@Deprecated
	protected ActivityMonitor			activityMonitor;
	
	@Deprecated
	protected PriorityQueue<Activity>	activities;
	
	@Deprecated
	protected AgentType					type;
	
	@Deprecated
	protected boolean					dontStop;
	
	 // The list of known locations of the items.
	@Deprecated
	protected Vector<Item> 				items;
	
	 // The items the agent has.
	@Deprecated
	protected Vector<Item>				myItems;
	
	@Deprecated
	protected Activity					currentActivity;
	
	@Deprecated
	protected String					registrationId;
	
	@Deprecated
	public ActivityMonitor getActivityMonitor() {
		return activityMonitor;
	}

	@Deprecated
	public void setActivityMonitor(ActivityMonitor activityMonitor) {
		this.activityMonitor = activityMonitor;
	}

	@Deprecated
	public PriorityQueue<Activity> getActivities() {
		return activities;
	}

	@Deprecated
	public void setActivities(PriorityQueue<Activity> activities) {
		this.activities = activities;
	}

	@Deprecated
	public AgentType getType() {
		return type;
	}

	@Deprecated
	public void setType(AgentType type) {
		this.type = type;
	}

	@Deprecated
	public boolean isDontStop() {
		return dontStop;
	}

	@Deprecated
	public void setDontStop(boolean dontStop) {
		this.dontStop = dontStop;
	}

	@Deprecated
	public Vector<Item> getItems() {
		return items;
	}

	@Deprecated
	public void setItems(Vector<Item> items) {
		this.items = items;
	}

	@Deprecated
	public Vector<Item> getMyItems() {
		return myItems;
	}

	@Deprecated
	public void setMyItems(Vector<Item> myItems) {
		this.myItems = myItems;
	}

	@Deprecated
	public Activity getCurrentActivity() {
		return currentActivity;
	}



	@Deprecated
	public Agent(AgentType type, boolean DONT_USE_IT) {

		activities = new PriorityQueue<Activity>(11,new Comparator<Activity>() {
			
			//TODO
			@Override
			public int compare(Activity o1,Activity  o2) {
//				if (o1.getPriority() < o2.getPriority())
//					return -1;
//				else if(o1.getPriority() > o2.getPriority())
//					return 1;
				return 0;
			}
		});
		
		this.activityMonitor = new ActivityMonitor(this);
		this.type = type;
		this.dontStop = true;
		this.registrationId = "";
	}

	@Deprecated
	protected Activity findNextActivity() {
		
		for (Activity act : activities){
			
			if (!act.isSatisfiedPreCond())
				continue;
			
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
	
	@Deprecated
	private void releaseMyItems() {
		
		for (Item item : myItems)
			item.setState(ItemState.AVAILABLE);
	}

	@Deprecated
	protected void performCurrentActivity() {
		 currentActivity.setState(ActivityState.IN_PROGRESS);
	}
	
	@Deprecated
	protected void completeCurrentActivity(){
		
		releaseMyItems();
		currentActivity.setState(ActivityState.COMPLETED);
		currentActivity = null;
	}

	@Deprecated
	public void insertActivity(Activity newActivity){
		activities.add(newActivity);
	}
	
	@Deprecated
	public void setRegistrationId(String registrationId){
		this.registrationId = registrationId;
	}
	
	@Deprecated
	public String getRegistrationId(){
		return this.registrationId;
	}
	
	/**
	 * @param itemType the item type to find
	 * @return the closest item to the entity, 
	 * or null if there is no item inside the domain
	 */
	@Deprecated
	private Location findClosestItem(ItemType itemType) {
		
		Location entityLocation = getLocation();//TODO Should be changed to android getLastKnownLocation(String provider) in LocationManager class
		Location closestItem = new Location(-1, -1);
		
		double closestLocation = Double.MAX_VALUE;
		
		synchronized (items) {
			
			for (Item it : items) {
				
				Location itemLocation = it.getLocation();
				
				double distance = calcDistance(entityLocation, itemLocation);//TODO should be changed to android "distanceTo(Location dest)" in Location Class
				
				if(distance >= 0 && distance < closestLocation){
					
					closestLocation = distance;
					closestItem = itemLocation;
				}
			}
		}
		
		if(closestItem.getLatitude() != -1)
			return closestItem;
		
		return null;
	}
	
	@Deprecated
	private double calcDistance(Location entityLocation, Location location) {
		
		if(entityLocation != null && location != null){
			
			double latitudeDiff = entityLocation.getLatitude() - location.getLatitude();
			double longitudeDiff = entityLocation.getLongitude() - location.getLongitude();
			return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
		}
		return -1;
	}
}
