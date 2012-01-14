package actor;

import java.util.UUID;

import simulator.Location;

public class Entity {
	protected String id;
	protected Location location;
	
	public Entity(){
		id = UUID.randomUUID().toString();
		location = null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public void setLocation(Location loc){
		location = loc;
	}
}
