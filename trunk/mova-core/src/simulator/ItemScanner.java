package simulator;

import java.util.Observer;
import java.util.Vector;

import actor.Entity;
import actor.SensorAgent;

public class ItemScanner implements Runnable {
	
	protected boolean mDontStop;
	protected NewDomain mDomain;
	protected SensorAgent mSensorAgent;
	protected Location mSensorLocation;
	protected final long mRefreshRate = 3;
	protected final double mScanDistance;
	protected Vector<Entity> mVisibleItems;
	protected Vector<Observer> mObservers;
	
	public ItemScanner(Entity entity, NewDomain domain, Vector<Observer> observers, double scanDistance){
		mDomain = domain;
		mSensorAgent = (SensorAgent)entity;
		mSensorLocation = mSensorAgent.getLocation();
		mDontStop = true;
		mVisibleItems = new Vector<Entity>();
		mObservers = observers;
		mScanDistance = scanDistance;
	}
	
	@Override
	public void run() {
		while (mDontStop){
			Vector<Entity> visible = mDomain.scanForItems(mSensorLocation, mScanDistance);
			Vector<Entity> changedLocationItems = new Vector<Entity>(); 
			for (Entity entity : visible) {
				if(!entity.getLocation().equals(entity.getOldLocation()) && entity.getOldLocation() != null)
					changedLocationItems.add(entity);
			}
			mSensorAgent.setVisibleItems(visible);
			mVisibleItems = visible;
			if(changedLocationItems.size() > 0){
//				for (Observer ob : _observers) {
//					ob.update(null, changedLocationItems);
//				}
                mDomain.changeItemsLocation(changedLocationItems);
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
