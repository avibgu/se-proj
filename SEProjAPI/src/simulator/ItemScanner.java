package simulator;

import actor.Entity;
import actor.SensorAgent;

public class ItemScanner implements Runnable {
	
	protected boolean _dontStop;
	protected Domain _domain;
	protected SensorAgent _sensorAgent;
	protected Location _sensorLocation;
	protected final long _refreshRate = 60;
	protected final double _scanDistance = 2;
	
	public ItemScanner(Entity entity, Domain domain){
		_domain = domain;
		_sensorAgent = (SensorAgent)entity;
		_sensorLocation = _sensorAgent.getLocation();
		_dontStop = true;
	}
	
	@Override
	public void run() {
		while (_dontStop){
			synchronized (_sensorAgent) {
				_sensorAgent.setVisibleItems(_domain.scanForItems(_sensorLocation, _scanDistance));
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
