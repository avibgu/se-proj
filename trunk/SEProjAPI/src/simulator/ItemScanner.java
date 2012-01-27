package simulator;

import java.util.Observer;
import java.util.Vector;

import actor.Entity;
import actor.SensorAgent;

public class ItemScanner implements Runnable {
	
	protected boolean _dontStop;
	protected Domain _domain;
	protected SensorAgent _sensorAgent;
	protected Location _sensorLocation;
	protected final long _refreshRate = 3;
	protected final double _scanDistance = 2;
	protected Vector<Entity> _visibleItems;
	protected Vector<Observer> _observers;
	
	public ItemScanner(Entity entity, Domain domain, Vector<Observer> observers){
		_domain = domain;
		_sensorAgent = (SensorAgent)entity;
		_sensorLocation = _sensorAgent.getLocation();
		_dontStop = true;
		_visibleItems = new Vector<Entity>();
		_observers = observers;
	}
	
	@Override
	public void run() {
		while (_dontStop){
			Vector<Entity> visible = _domain.scanForItems(_sensorLocation, _scanDistance);
			Vector<Entity> changedLocationItems = new Vector<Entity>(); 
			for (Entity entity : visible) {
				if(!entity.getLocation().equals(entity.getOldLocation()) && entity.getOldLocation() != null)
					changedLocationItems.add(entity);
			}
			_sensorAgent.setVisibleItems(visible);
			_visibleItems = visible;
			if(changedLocationItems.size() > 0){
				for (Observer ob : _observers) {
					ob.update(null, changedLocationItems);
				}
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
