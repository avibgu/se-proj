
package simulator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import utilities.Location;

public class MovaMap {
    private Map<String, Room> mRooms;
    private Map<Location, Door> mDoors;
    private Location[][] mLocations;
    private LocationNode[][] mLocationNodes;
    private int mHeight;
    private int mWidth;

    public  MovaMap(int height, int width){
        mHeight = height;
        mWidth = width;
        mLocations = new Location[height][width];
        mLocationNodes = new LocationNode[height][width];
        //initializeLocations();
        //setLocationNodesNeighbors();
    }

    public void setRooms(Vector<Room> rooms){
        mRooms = Collections.synchronizedMap(new HashMap<String, Room>());
        for (Room room : rooms) {
            mRooms.put(room.getName(), room);
        }
    }

    public void setDoors(Vector<Door> doors){
        mDoors = Collections.synchronizedMap(new HashMap<Location, Door>());
        for (Door door : doors) {
            mDoors.put(door.getLocation(), door);
        }
    }

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

    public Room getRoom(String roomName){
        return (Room)mRooms.get(roomName);
    }

    public Room getRoom(Location location){
        for (Object room : mRooms.values()) {
            if(((Room)room).isLocationInRoom(location))
                return (Room)room;
        }
        return null;
    }

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

    public int getHeight(){
        return mHeight;
    }

    public int getWidth(){
        return mWidth;
    }
}
