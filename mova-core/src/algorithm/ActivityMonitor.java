package algorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import priority.Priority;

import actor.Activity;
import actor.Agent;

// Each Agent will monitor the Activities in his queue and will notify
// when the priority of an Activity is changing or when its due date is approaching.
// When it's happening, we will count it as a Problem and we will handle it as
// described in the following Problems Solving section.

public class ActivityMonitor implements Runnable {

	private static final long WAITING_TIME_IN_SECONDS = 2;
	private static final long WAITING_TIME_IN_MILISECONDS = WAITING_TIME_IN_SECONDS * 1000;
	
	private static final int NUM_OF_TOP_PRIORITY_ACTIVITIES_ALLOWED = 2;

	protected boolean mDontStop;

	protected Agent mAgent;

	protected PriorityQueue<Activity> mActivities;

	protected Map<Activity, Priority> mOldPriority;

	public ActivityMonitor(Agent pAgent) {
		mDontStop = true;
		mAgent = pAgent;
		mActivities = mAgent.getActivities();
		mOldPriority = new HashMap<Activity, Priority>();
	}

	@Override
	public void run() {

		int numOfTopPriorityActivities;
		int numOfActivitiesBeforeThisOne;

		while (mDontStop) {

			numOfTopPriorityActivities = 0;
			numOfActivitiesBeforeThisOne = 0;
			
			//TODO: maybe use read-write lock.. (should be located in Agent..)
			
			for (Activity activity : mActivities) {

				if (isThisActivityIsATopPriorityActivity(activity))
					numOfTopPriorityActivities++;

				if (isThisActivityChangedItsPriority(activity))
					mAgent.activityChangedHisPriority(activity);

				numOfActivitiesBeforeThisOne++;
				
				if (areWeGoingToMissThisActivity(activity, numOfActivitiesBeforeThisOne))
					mAgent.goigToMissThisActivity(activity);
			}

			if (numOfTopPriorityActivities > NUM_OF_TOP_PRIORITY_ACTIVITIES_ALLOWED)
				mAgent.tooManyTopPriorityActivities(numOfTopPriorityActivities
						- NUM_OF_TOP_PRIORITY_ACTIVITIES_ALLOWED);

			waitSomeTime();
		}
	}

	private boolean areWeGoingToMissThisActivity(Activity activity, int numOfActivitiesBeforeThisOne) {

		if (mAgent.getCurrentActivity().equals(activity))
			return false;
		
		long now = new Date().getTime();
		
		long expectedTime = mAgent.getAverageTimePerActivity() * (1 + numOfActivitiesBeforeThisOne);
		
		return now + expectedTime > activity.getDueDate();
	}

	protected boolean isThisActivityChangedItsPriority(Activity activity) {

		Priority oldPriority;

		if ((oldPriority = mOldPriority.get(activity)) != null
				&& activity.getPriority() != oldPriority)
			return true;

		else
			mOldPriority.put(activity, activity.getPriority());

		return false;
	}

	public void stop() {
		mDontStop = false;
	}

	protected boolean isThisActivityIsATopPriorityActivity(Activity activity) {
		return activity.getPriority() == Priority.HIGH;
	}

	protected void waitSomeTime() {

		try {
			Thread.sleep(WAITING_TIME_IN_MILISECONDS);
		} catch (Exception e) {
		}
	}
}
