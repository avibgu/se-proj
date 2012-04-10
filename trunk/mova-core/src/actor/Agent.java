package actor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import state.ItemState;
import type.AgentType;
import type.ItemType;


public class Agent extends Entity implements Runnable {

	// TODO: check if there are duplication between Agent and Entity
	
	protected Vector<Activity>			mActivitiesToPerform;
	protected Set<Activity>				mOldActivities;
	protected AgentType					mType;
	protected boolean					mDontStop;
	protected Activity					mCurrentActivity;
	protected String					mRegistrationId;
	
	// The list of known locations of the items.
	protected Vector<Item> 				mItems;
	
	 // The items the agent has.
	protected Vector<Item>				mMyItems;
	
	public Agent(AgentType pType) {

		mActivitiesToPerform = new Vector<Activity>();
		mOldActivities = new HashSet<Activity>();
		mType = pType;
		mDontStop = true;
		mCurrentActivity = null;
		mRegistrationId = "";
		
		mItems = new Vector<Item>();
		mMyItems = new Vector<Item>();
	}
	
	
	// From Here: Algorithm's methods:
	
	
	@Override
	public void run() { 
		
		while (mDontStop){
			
			resolveProblems();
			setCurrentActivity(findNextActivity());
			performCurrentActivity();
		}
	}
	
	public void stop() {
		mDontStop = false;
	}

	protected Activity findNextActivity() {
		
		Map<ItemType,Integer> requiredItems = null;
		
		//TODO: we are currently ignoring other Agents - just for the prototype
		Map<AgentType,Integer> requiredAgents = null;
		
		int numOfItems = 0;
		
		boolean ok = false;
		
		Activity[] sortedActivities = (Activity[]) mActivitiesToPerform.toArray();
		
		Arrays.sort(sortedActivities);
		
		for (Activity activity : sortedActivities){
			
			ok = true;
			
//			if (!activity.isSatisfiedPreCond())
//				continue;
		
//			TODO: we are currently ignoring other Agents - just for the prototype
//			requiredAgents = activity.getRequiredAgents();
			
			requiredItems = activity.getRequiredItems();
			
			for (ItemType itemType : requiredItems.keySet()){
				
				numOfItems = requiredItems.get(itemType);
				
				for (Item item : mItems){
					
					if (item.getType().equals(itemType) && (item.mState == ItemState.AVAILABLE)){

						item.markAsBusy();
						
						//TODO: tell server that we took this item
						
						mMyItems.add(item);
						
						numOfItems--;
						
						if (0 == numOfItems)
							break;
					}
				}
				
				// If we got here, it means we didn't find any item from the required item type which available. We will release all the other
				// items we've captured, and look for another activity.
				if (0 != numOfItems){
					
					ok = false;
					break;
				}
			}
			
			if (ok) return activity;
			
			else releaseMyItems();
		}
		
		return null; // No activity was found.
	}
	
	// TODO: improve it..
	protected void performCurrentActivity() {
		 mCurrentActivity.markAsInProgress();
		//TODO: tell server that we perform this activity
		//TODO: when completing the activity, remove it from mActivitiesToPerform and add it to mOldActivities
	}
	
	// TODO: improve it..
	protected void completeCurrentActivity(){
		
		releaseMyItems();
		mCurrentActivity.markAsCompleted();
		mCurrentActivity = null;
	}
	
	// TODO: improve it..
	private void releaseMyItems() {
		
		for (Item item : mMyItems){
			
			item.markAsAvailable();
			//TODO: tell server that we released this item
		}
	}
	
	private void resolveProblems() {
	
		// TODO: recalculate when needed..
	}
	
	private void notifyThisAgentAboutLocationOfItem() {
		// TODO Auto-generated method stub
	}
	
	
	// From Here: Getters & Setters
	
	
	public void setCurrentActivity(Activity pActivity) {
		mCurrentActivity = pActivity;
	}
	
	public Vector<Activity> getActivities() {
		return mActivitiesToPerform;
	}
	
	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}
	
	public AgentType getType() {
		return mType;
	}
	
	public String getRegistrationId(){
		return mRegistrationId;
	}

	//TODO: what to do with that??..

//
//	/**
//	 * @param itemType the item type to find
//	 * @return the closest item to the entity, 
//	 * or null if there is no item inside the domain
//	 */
//	@Deprecated
//	private Location findClosestItem(ItemType itemType) {
//		
//		Location entityLocation = getLocation();//TODO Should be changed to android getLastKnownLocation(String provider) in LocationManager class
//		Location closestItem = new Location(-1, -1);
//		
//		double closestLocation = Double.MAX_VALUE;
//		
//		synchronized (items) {
//			
//			for (Item it : items) {
//				
//				Location itemLocation = it.getLocation();
//				
//				double distance = calcDistance(entityLocation, itemLocation);//TODO should be changed to android "distanceTo(Location dest)" in Location Class
//				
//				if(distance >= 0 && distance < closestLocation){
//					
//					closestLocation = distance;
//					closestItem = itemLocation;
//				}
//			}
//		}
//		
//		if(closestItem.getLatitude() != -1)
//			return closestItem;
//		
//		return null;
//	}
//	
//	@Deprecated
//	private double calcDistance(Location entityLocation, Location location) {
//		
//		if(entityLocation != null && location != null){
//			
//			double latitudeDiff = entityLocation.getLatitude() - location.getLatitude();
//			double longitudeDiff = entityLocation.getLongitude() - location.getLongitude();
//			return Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff);
//		}
//		return -1;
//	}
}
