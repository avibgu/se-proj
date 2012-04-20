package actor;

import java.util.Observable;
import java.util.UUID;

import simulator.Location;

public class Entity extends Observable{
	
	protected String				mId;
	protected Location				mLocation;
	protected Location				mOldLocation;
	protected boolean				mLoggedIn;
	private transient Location		mLastRepesentedLocation;
	private transient String		mRepresentation;
	private transient boolean		mUpdated;
	
	public Entity(){
		
		mId = UUID.randomUUID().toString();
		mLocation = null;
		mOldLocation = null;
		mLoggedIn = false;
		mLastRepesentedLocation = null;
        mRepresentation = "";
        mUpdated = true;
	}
	
	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		this.mId = pId;
	}
	
	public synchronized Location getLocation(){
		return mLocation;
	}
	
	public synchronized Location getOldLocation(){
		return mOldLocation;
	}
	
	public synchronized Location getLastRepLocation(){
		return mLastRepesentedLocation;
	}

	public synchronized void setLocation(Location pLocation){
		if(mLocation != null)
			mOldLocation = mLocation.clone();
		mLocation = pLocation;
		mUpdated = false;
	}

	public synchronized void setRepLocation(Location pLocation){
		mLastRepesentedLocation = pLocation;
		setLocation(pLocation);
	}
	
    @Override
    public String toString(){
    	return mRepresentation;
    }
    public void setRepresentation(String pRepresentation){
        mRepresentation = pRepresentation;
    }
    
    public synchronized void update(){
    	mUpdated = true;
    	mLastRepesentedLocation = mLocation;
    }
    public synchronized boolean updated(){
    	return mUpdated;
    }
    
    public void logIn(){
    	mLoggedIn = true;
    }
    
    public void logOff(){
    	mLoggedIn = false;
    }
    
    public boolean isLoggedIn(){
    	return mLoggedIn;
    }
}
