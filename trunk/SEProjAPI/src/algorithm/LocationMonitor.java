package algorithm;

import actor.Entity;
import simulator.Location;

public class LocationMonitor implements Runnable{
	
	protected boolean _dontStop;
	protected Entity _entity;
	protected Location _lastKnownLocation;
	protected final long _refreshRate = 20;
	
	public LocationMonitor(Entity entity){
		_entity = entity;
		synchronized (_entity.getLocation()) {
			_lastKnownLocation = _entity.getLocation().clone();
		}
		_dontStop = true;
	}
	@Override
	public void run() {
		while (_dontStop){
			Location entityLocation = null;
			synchronized (_entity.getLocation()) {
				entityLocation = _entity.getLocation();
			}
			if(entityLocation != null &&(_lastKnownLocation.getLatitude() != entityLocation.getLatitude() || _lastKnownLocation.getLongitude() != entityLocation.getLongitude())){
					//TODO send to relevant agents in the current activity
					//((Agent)entity).getCurrentActivity(); 
			}
			
			try {
				Thread.sleep(_refreshRate * 1000);
			} catch (InterruptedException e) {
				stop();
			}
			synchronized (_entity.getLocation()) {
				_lastKnownLocation = _entity.getLocation().clone();
			}
		}
	}
	
	public void stop() {
		_dontStop = false;
	}
	
}
