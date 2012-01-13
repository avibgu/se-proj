package simulator;

import actor.Entity;

public class Location {
	private int _latitude;
	private int _longitude;
	private Entity _entity;
	
	public Location(int latitude, int longitude){
		_latitude = latitude;
		_longitude = longitude;
		_entity = null;
	}
	
	public int getLatitude(){
		return _latitude;
	}
	
	public int getLongitude(){
		return _longitude;
	}
	
	public void setLatitude(int latitude){
		_latitude = latitude;
	}
	
	public void setLongitude(int longitude){
		_latitude = longitude;
	}
	
	public void placeEntity(Entity entity){
		_entity = entity;
	}
	
	public void removeEntity(){
		//TODO change method signature so it can return Entity
		//In order to do that, clone methods should be implemented
		_entity = null;
	}
	
	public Entity getEntity(){
		return _entity;
	}
}
