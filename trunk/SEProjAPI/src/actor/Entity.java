package actor;

import java.util.Observable;
import java.util.UUID;

import simulator.Location;

public class Entity extends Observable{
	protected String id;
	protected Location location;
	protected Location oldLocation;
	private transient Location lastRepesentedLocation;
    private transient String representation;
    private transient boolean updated;
	
	public Entity(){
		id = UUID.randomUUID().toString();
		location = null;
		oldLocation = null;
		lastRepesentedLocation = null;
        representation = "";
        updated = true;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public synchronized Location getLocation(){
		return location;
	}
	
	public synchronized Location getOldLocation(){
		return oldLocation;
	}
	
	public synchronized Location getLastRepLocation(){
		return lastRepesentedLocation;
	}
	
	public synchronized void setLocation(Location loc){
		if(location != null)
			oldLocation = location.clone();
		location = loc;
		updated = false;
	}
	
	public void setRepLocation(Location l){
		lastRepesentedLocation = l;
		setLocation(l);
	}
	
    @Override
    public String toString(){
    	return representation;
    }
    public void setRepresentation(String rep){
        representation = rep;
    }
    
    public synchronized void update(){
    	updated = true;
    	lastRepesentedLocation = location;
    }
    public synchronized boolean updated(){
    	return updated;
    }
}
