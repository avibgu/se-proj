package simulator;

public class Location implements Cloneable{
	private int latitude;
	private int longitude;
	
	public Location(int lon, int lat){
		latitude = lat;
		longitude = lon;
	}
	
	public synchronized int getLatitude(){
		return latitude;
	}
	
	public synchronized int getLongitude(){
		return longitude;
	}
	
	public synchronized void setLatitude(int lat){
		latitude = lat;
	}
	
	public synchronized void setLongitude(int lon){
		longitude = lon;
	}
	
	public Location clone(){
		int lat = latitude;
		int lon = longitude;
		Location loc = new Location(lon, lat);
		return loc;
	}
	
	public boolean equals(Object l){
		Location loc = (Location)l;
		if(loc != null){
			if(loc.getLatitude() != latitude || loc.getLongitude() != longitude)
				return false;
			else
				return true;
		}
		return false;
	}
}
