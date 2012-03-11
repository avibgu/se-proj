package algorithm;

import java.util.Observer;
import java.util.Vector;

import actor.Entity;
import simulator.Location;

public class LocationMonitor implements Runnable{
	
	protected boolean _dontStop;
	protected Entity _entity;
	protected Location _lastKnownLocation;
	protected final long _refreshRate = 3;
	protected Vector<Observer> _observers;
	
	public LocationMonitor(Entity entity, Vector<Observer> observers){
		_entity = entity;
		_lastKnownLocation = _entity.getLocation();
		_dontStop = true;
		_observers = observers;
	}
	@Override
	public void run() {
		while (_dontStop){
			Location entityLocation = _entity.getLocation();
			if(entityLocation != null &&(_lastKnownLocation.getLatitude() != entityLocation.getLatitude() || _lastKnownLocation.getLongitude() != entityLocation.getLongitude())){
					//TODO send to relevant agents in the current activity
					//((Agent)entity).getCurrentActivity(); 
					synchronized (_observers) {
						for (Observer ob : _observers) {
							ob.update(_entity, null);
						}
					}
					_lastKnownLocation = entityLocation;
			}
			
			try {
				Thread.sleep(_refreshRate * 1000);
			} catch (InterruptedException e) {
				stop();
			}
		}
	}
	
	public void stop() {
		_dontStop = false;
	}
	
}
