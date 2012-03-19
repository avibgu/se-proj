package actor;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.MovaClient;

import priority.ActivitiesComarator;

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
	protected String					mRegistrationId;
	protected long						mStartTime;
	
	// These Locks protect the mActivitiesToPerform Collection
	protected ReadWriteLock				mRWLock;
	protected Lock						mReadLock;
	protected Lock						mWriteLock;
	
	// These Collections hold activities that should be fixed
	protected Set<Activity>				mActivitiesThatChangedTheirPriority;
	protected Vector<Activity>			mActivitiesThatShouldBeTransffered;
	protected Integer					mHowManyOverTheLimit;
	
	public Agent(AgentType pType) {

		mActivityMonitor = new ActivityMonitor(this);
		mActivitiesToPerform = new PriorityQueue<Activity>(7, new ActivitiesComarator());
		mOldActivities = new HashSet<Activity>();
		mType = pType;
		mDontStop = true;
		mCurrentActivity = null;
		mRegistrationId = "";
		mStartTime = 0;
		mRWLock = new ReentrantReadWriteLock(true);
		mReadLock = mRWLock.readLock();
		mWriteLock = mRWLock.writeLock();
		mActivitiesThatChangedTheirPriority = new HashSet<Activity>();
		mActivitiesThatShouldBeTransffered = new Vector<Activity>();
		mHowManyOverTheLimit = 0;
	}
	
	
	// From Here: Algorithm's methods:
	
	
	@Override
	public void run() { 
		
		new Thread(mActivityMonitor).start();
		
		mStartTime = new Date().getTime();
		
		while (mDontStop){
			
			resolveIssuesThatOthersToldMeAbout();
			setCurrentActivity(findNextActivity());
			performCurrentActivity();
		}
		
		mActivityMonitor.stop();
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
		
		mReadLock.lock();
		
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
				
				for (Item item : items){
					
					if (item.getType().equals(itemType) && (item.state == ItemState.AVAILABLE)){

						item.markAsBusy();
						
						//TODO: tell server that we took this item
						
						myItems.add(item);
						
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
			
			if (ok){
				mReadLock.unlock();
				return activity;
			}
			
			else releaseMyItems();
		}
		
		mReadLock.unlock();
		
		return null; // No activity was found.
	}
	
	@Deprecated
	protected void performCurrentActivity() {
		 currentActivity.setState(ActivityState.IN_PROGRESS);
		//TODO: tell server that we perform this activity
		//TODO: when completing the activity, remove it from mActivitiesToPerform and add it to mOldActivities
	}
	
	
	// From Here: Constraints Processing methods:  
	
	
	private void resolveIssuesThatOthersToldMeAbout() {
	
		maintainActivitiesThatChangedTheirPriority();
		maintainActivitiesToSend();
		sendActivitiesToOtherAgents();
	}
	
	private void maintainActivitiesThatChangedTheirPriority() {

		mWriteLock.lock();
		
		synchronized (mActivitiesThatChangedTheirPriority) {
		
			for (Activity activity : mActivitiesThatChangedTheirPriority){

				mActivitiesToPerform.remove(activity);
				mActivitiesToPerform.add(activity);
			}
		}
		
		mWriteLock.unlock();
	}

	private void maintainActivitiesToSend() {
		
		mWriteLock.lock();
		
		synchronized (mHowManyOverTheLimit) {
			
			if (0 == mHowManyOverTheLimit){
				
				mWriteLock.unlock();
				return;					//TODO: return releases the synchronized, right??..
			}
			
			synchronized (mActivitiesThatShouldBeTransffered) {
				
				Activity[] sortedActivities = (Activity[]) mActivitiesToPerform.toArray();
				
				Arrays.sort(sortedActivities);
				
				for (Activity activity : sortedActivities)
					if (!activity.equals(mCurrentActivity) && activity.isTopPriority())
						mActivitiesThatShouldBeTransffered.add(activity);

				int toKeep = mActivitiesThatShouldBeTransffered.size() - mHowManyOverTheLimit;
				
				while(toKeep-- > 0)
					mActivitiesThatShouldBeTransffered.removeElementAt(0);
				
				for (Activity activity : mActivitiesThatShouldBeTransffered)
					mActivitiesToPerform.remove(activity);
			}
		}
		
		mWriteLock.unlock();
	}
	
	//TODO: improve it badly!!..
	private void sendActivitiesToOtherAgents() {

		synchronized (mActivitiesThatShouldBeTransffered) {
			
			for (Activity activity : mActivitiesThatShouldBeTransffered){
				
				String agentID = findAnAgentWhoCanReceiveThisActivity(activity);
				MovaClient mc = new MovaClient();
				Vector<String> tVec = new Vector<String>(1);
				tVec.add(agentID);
				mc.sendActivity(activity, tVec);
			}
		}
	}
	
	private String findAnAgentWhoCanReceiveThisActivity(Activity activity) {
		
		// TODO Broadcast Agents of my type
		//      choose the first that response with a positive answer
		//		return his ID.
		
		return "";
	}
	
	
	// From Here: methods that others use to notify this agent about things:


	public void tooManyTopPriorityActivities(int pHowManyOverTheLimit) {
	
		synchronized (mHowManyOverTheLimit) {
		
			mHowManyOverTheLimit = pHowManyOverTheLimit;
		}	
	}

	public void activityChangedHisPriority(Activity activity) {
		
		synchronized (mActivitiesThatChangedTheirPriority) {
			
			mActivitiesThatChangedTheirPriority.add(activity);
		}
	}

	public void goigToMissThisActivity(Activity activity) {

		//TODO: increase priority first..
		
		synchronized (mActivitiesThatShouldBeTransffered) {
			
			mActivitiesThatShouldBeTransffered.add(activity);
		}
	}
	
	private void notifyThisAgentAboutLocationOfItem() {
		// TODO Auto-generated method stub
	}
	
	
	// From Here: Getters & Setters
	
	
	public void setCurrentActivity(Activity activity) {
		mCurrentActivity = activity;
	}
	
	public PriorityQueue<Activity> getActivities() {
		return mActivitiesToPerform;
	}
	
	public Lock getReadLock() {
		return mReadLock;
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

	public long getAverageTimePerActivity() {
		//TODO: improve it - calc also idle time..
		return (new Date().getTime() - mStartTime) / mOldActivities.size();
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
	protected void completeCurrentActivity(){
		
		releaseMyItems();
		currentActivity.setState(ActivityState.COMPLETED);
		currentActivity = null;
	}
	
	@Deprecated
	private void releaseMyItems() {
		
		for (Item item : myItems){
			
			item.markAsAvailable();
			//TODO: tell server that we released this item
		}
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
