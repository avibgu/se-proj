
package algorithm;


import java.util.PriorityQueue;

import actor.Activity;

public class ActivityMonitor implements Runnable {

	protected boolean _dontStop;

	public ActivityMonitor(PriorityQueue<Activity> _activities) {
		_dontStop = true;
	}

	@Override
	public void run() {

		while (_dontStop){
			
		}
	}

	public void stop() {
		_dontStop = false;
	}

}
