package algorithm;

import java.util.Observer;
import java.util.Vector;

import actor.Entity;
import simulator.Location;

public class LocationMonitor implements Runnable{
	
	protected boolean			mDontStop;
	protected Entity			mEntity;
	protected Location			mLastKnownLocation;
	protected final long		mRefreshRate = 3;
	protected Vector<Observer>	mObservers;
	
	public LocationMonitor(Entity pEntity, Vector<Observer> pObservers){
		mEntity = pEntity;
		mLastKnownLocation = mEntity.getLocation();
		mDontStop = true;
		mObservers = pObservers;
	}
	
	@Override
	public void run() {
		
		while (mDontStop){
			
			Location entityLocation = mEntity.getLocation();
			
			if(entityLocation != null &&(mLastKnownLocation.getLatitude() != entityLocation.getLatitude() || mLastKnownLocation.getLongitude() != entityLocation.getLongitude())){
					
				//TODO send to relevant agents in the current activity
					//((Agent)entity).getCurrentActivity(); 
					synchronized (mObservers) {
						for (Observer ob : mObservers) {
							ob.update(mEntity, null);
						}
					}
					
					mLastKnownLocation = entityLocation;
			}
			
			try {
				Thread.sleep(mRefreshRate * 1000);
			} catch (InterruptedException e) {
				stop();
			}
		}
	}
	
	public void stop() {
		mDontStop = false;
	}
	
}
