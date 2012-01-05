package actor;

import java.util.Vector;

import type.AgentType;

import algorithm.ActivityMonitor;

public class Agent implements Runnable{

	protected ActivityMonitor	_activityMonitor;
	protected Vector<Activity>	_activities;
	protected AgentType			_type;
	protected String			_id;
	protected boolean			_dontStop;
	
	public Agent(AgentType type) {

		_activities = new Vector<Activity>();
		_activityMonitor = new ActivityMonitor(_activities);
		_type = type;
		_dontStop = true;
	}

	@Override
	public void run() { 
		
		new Thread(_activityMonitor).start();
		
		while (_dontStop){
			
			Activity activity = findNextActivity();
			
			performActivity(activity);
		}
		
		_activityMonitor.stop();		
	}

	protected Activity findNextActivity() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void performActivity(Activity activity) {
		// TODO Auto-generated method stub
	}
	
	public void stop() {
		_dontStop = false;
	}
}
