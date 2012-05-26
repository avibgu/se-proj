
package simulator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import utilities.Location;
/**
 * The MovaMap class holds the map locations,
 * including rooms and doors. Calculates the shortest path
 * from one location to another that passes rooms only via doors
 */
public class MovaMap {
    private Map<String, Room> mRooms;
    private Map<Location, Door> mDoors;
    private Location[][] mLocations;
    private LocationNode[][] mLocationNodes;
    private int mHeight;
    private int mWidth;
    /**
     * Creates a new MovaMap class
     * @param height the height of the Mova map
     * @param width the width of the Mova map
     */
    public  MovaMap(int height, int width){
        mHeight = height;
        mWidth = width;
        mLocations = new Location[height][width];
        mLocationNodes = new LocationNode[height][width];
        //initializeLocations();
        //setLocationNodesNeighbors();
    }
    /**
     * Sets the rooms of the map
     * @param rooms the rooms of the map
     */
    public void setRooms(Vector<Room> rooms){
        mRooms = Collections.synchronizedMap(new HashMap<String, Room>());
        for (Room room : rooms) {
            mRooms.put(room.getName(), room);
        }
    }
    /**
     * Sets the doors of the map
     * @param doors the doors of the map
     */
    public void setDoors(Vector<Door> doors){
        mDoors = Collections.synchronizedMap(new HashMap<Location, Door>());
        for (Door door : doors) {
            mDoors.put(door.getLocation(), door);
        }
    }
    /**
     * Calculates the shortest path from start location to goal location
     * that passes rooms only via doors
     * @param start the start location
     * @param goal the goal location
     * @return the shortest path in a stack. The stack top is the next location to move to.
     */
    public  Stack<Location> calculateShortestPath(Location start, Location goal){
        Stack<Location> path = new Stack<Location>();
        Queue<LocationNode> queue = new LinkedList<LocationNode>();
        LocationNode startNode = mLocationNodes[start.getLongitude()][start.getLatitude()];
        //LocationNode startNode = new LocationNode(start);
        Vector<LocationNode> marked = new Vector<LocationNode>();
        queue.add(startNode);

        while(!queue.isEmpty()){
            LocationNode currentNode = queue.remove();
            if(currentNode.getLocation().equals(goal))
                break;
            for (LocationNode locationNode : currentNode.getNeighbors()) {
                if(!locationNode.isMarked()){
                    locationNode.mark();
                    queue.add(locationNode);
                    locationNode.setParent(currentNode);
                    marked.add(locationNode);
                }
            }
        }
        LocationNode currentNode = mLocationNodes[goal.getLongitude()][goal.getLatitude()];
        while(!currentNode.getLocation().equals(start)){
            path.push(currentNode.getLocation());
            currentNode = currentNode.getParent();
        }
        path.push(start);

        for (LocationNode locationNode : marked) {
            locationNode.unMark();
        }
        return path;
    }
    /**
     * Initialize rooms and doors locations
     */
    public void initializeLocations() {
        for (Object room : mRooms.values()) {
            for(Location location : ((Room)room).getMargins()){
                mLocations[location.getLongitude()][location.getLatitude()] = location;
                mLocationNodes[location.getLongitude()][location.getLatitude()] = new LocationNode(location);
            }
        }
        for (Object dr : mDoors.values()) {
            Door door = (Door) dr;
            mLocations[door.getLocation().getLongitude()][door.getLocation().getLatitude()] = door.getLocation();
            mLocationNodes[door.getLocation().getLongitude()][door.getLocation().getLatitude()] = new LocationNode(door.getLocation());
        }
    }
    /**
     * Returns the room that has the name roomName
     * @param roomName the room name to search for
     * @return the associated Room
     */
    public Room getRoom(String roomName){
        return (Room)mRooms.get(roomName);
    }
    /**
     * Returns the location of the room in the given location
     * @param location the location to search for
     * @return the Room associated to the location, null if no such room exists 
     */
    public Room getRoom(Location location){
        for (Object room : mRooms.values()) {
            if(((Room)room).isLocationInRoom(location))
                return (Room)room;
        }
        return null;
    }
    /**
     * Sets the location node neighbors for shortest path calculation
     */
    public void setLocationNodesNeighbors() {
        for(int i = 0; i < mHeight; i++){
        	for(int j = 0; j < mWidth; j++){
                if(mLocationNodes[i][j] != null){
                    Vector<LocationNode> neighbors = mLocationNodes[i][j].getNeighbors();
                    if(nodeAccesable(i - 1, j -1) && !differentRooms(i, j, i-1, j-1))
                        neighbors.add(mLocationNodes[i - 1][j - 1]);
                    if(nodeAccesable(i - 1, j) && !differentRooms(i, j, i-1, j))
                        neighbors.add(mLocationNodes[i - 1][j]);
                    if(nodeAccesable(i - 1, j +1) && !differentRooms(i, j, i-1, j+1))
                        neighbors.add(mLocationNodes[i - 1][j + 1]);
                    if(nodeAccesable(i, j -1) && !differentRooms(i, j, i, j-1))
                        neighbors.add(mLocationNodes[i][j - 1]);
                    if(nodeAccesable(i, j +1) && !differentRooms(i, j, i, j+1))
                        neighbors.add(mLocationNodes[i][j + 1]);
                    if(nodeAccesable(i + 1, j -1) && !differentRooms(i, j, i+1, j-1))
                        neighbors.add(mLocationNodes[i + 1][j - 1]);
                    if(nodeAccesable(i + 1, j) && !differentRooms(i, j, i+1, j))
                        neighbors.add(mLocationNodes[i+1][j]);
                    if(nodeAccesable(i + 1, j +1) && !differentRooms(i, j, i+1, j+1))
                        neighbors.add(mLocationNodes[i + 1][j + 1]);
                }
            }
        }
    }
    
    private boolean nodeAccesable(int row, int column) {
        return row >= 0 && row < mHeight && column >= 0 && column < mWidth && mLocations[row][column] != null;
    }

    private boolean differentRooms(int i, int j, int i1, int j1) {
        Location l1 = mLocations[i][j];
        Location l2 = mLocations[i1][j1];

        Room room1 = getRoom(l1);
        Room room2 = getRoom(l2);
        
        if(room1 == null || room2 == null)
            return true;
        if(!room1.equals(room2)){
            if(mDoors.containsKey(l1) || mDoors.containsKey(l2))
                return false;
            else
                return true;
        }
        else
            return false;
    }
    /**
     * Returns the map height
     * @return the map height
     */
    public int getHeight(){
        return mHeight;
    }
    /**
     * Returns the map width
     * @return the map width
     */
    public int getWidth(){
        return mWidth;
    }
}
