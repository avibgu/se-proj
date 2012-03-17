package priority;

import java.util.Comparator;

import actor.Activity;

public class ActivitiesComarator implements Comparator<Activity> {
	
	@Override
	public int compare(Activity activity1, Activity activity2) {
		return activity1.getPriority().compareTo(activity2.getPriority());
	}
}
