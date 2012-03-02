package simulator;

public class Location implements Cloneable{
	private int latitude;
	private int longitude;
	
	public Location(){
		
	}
	
	public Location(int lat, int lon){
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
	
	public synchronized void setLongitude(int longitude){
		latitude = longitude;
	}
	
//	public void placeEntity(Entity entity){
//		_entities.put(entity.getId(), entity);
//	}
//	
//	public Entity removeEntity(Entity entity){
//		return _entities.remove(entity.getId());
//	}
//	
//	/**
//	 * @param itemType the type of item to get
//	 * @return the first item that corresponds to the item type
//	 */
//	public Entity getEntity(ItemType itemType){
//		for (Entity entity : _entities.values()) {
//			if(entity instanceof Item)
//				return entity;
//		}
//		return null;
//	}
//	/**
//	 * @param id the id to look for in the location
//	 * @return the relevant entity
//	 */
//	public Entity getEntityById(String id){
//		return _entities.get(id);
//	}
//	
//	/**
//	 * @return the entities in the location
//	 */
//	public Collection<Entity> getEntities(){
//		return _entities.values();
//	}
	
	public Location clone(){
		int lat = latitude;
		int lon = longitude;
		Location loc = new Location(lat, lon);		
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
