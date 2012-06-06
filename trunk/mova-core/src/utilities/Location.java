package utilities;

public class Location implements Cloneable{
	private int mLatitude;
	private int mLongitude;
	private String mRoom;
	
	public Location(int longitude, int latitude){
		mLatitude = latitude;
		mLongitude = longitude;
		mRoom = "";
	}
	
	public synchronized int getLatitude(){
		return mLatitude;
	}
	
	public synchronized int getLongitude(){
		return mLongitude;
	}
	
	public synchronized void setLatitude(int latitude){
		mLatitude = latitude;
	}
	
	public synchronized void setLongitude(int longitude){
		mLongitude = longitude;
	}
	
	public void setRoom(String room){
		mRoom = room;
	}
	
	public String getRoom(){
		return mRoom;
	}
	
	public Location clone(){
		int lat = mLatitude;
		int lon = mLongitude;
		Location loc = new Location(lon, lat);
		return loc;
	}
	
	public boolean equals(Object l){
		Location loc = (Location)l;
		if(loc != null){
			if(loc.getLatitude() != mLatitude || loc.getLongitude() != mLongitude)
				return false;
			else
				return true;
		}
		return false;
	}
}
