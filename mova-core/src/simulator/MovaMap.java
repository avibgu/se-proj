
package simulator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class MovaMap {
    private Map<String, Room> _rooms;
    private Map<Location, Door> _doors;
    private Location[][] _locations;
    private LocationNode[][] _locationNodes;
    private int _height;
    private int _width;

    public  MovaMap(int height, int width){
        _height = height;
        _width = width;
        _locations = new Location[height][width];
        _locationNodes = new LocationNode[height][width];
        //initializeLocations();
        //setLocationNodesNeighbors();
    }

    public void setRooms(Vector<Room> rooms){
        _rooms = Collections.synchronizedMap(new HashMap<String, Room>());
        for (Room room : rooms) {
            _rooms.put(room.getName(), room);
        }
    }

    public void setDoors(Vector<Door> doors){
        _doors = Collections.synchronizedMap(new HashMap<Location, Door>());
        for (Door door : doors) {
            _doors.put(door.getLocation(), door);
        }
    }

    public  Stack<Location> calculateShortestPath(Location start, Location goal){
        Stack<Location> path = new Stack<Location>();
        Queue<LocationNode> queue = new LinkedList<LocationNode>();
        LocationNode startNode = _locationNodes[start.getLongitude()][start.getLatitude()];
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
        LocationNode currentNode = _locationNodes[goal.getLongitude()][goal.getLatitude()];
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
        for (Object room : _rooms.values()) {
            for(Location location : ((Room)room).getMargins()){
                _locations[location.getLongitude()][location.getLatitude()] = location;
                _locationNodes[location.getLongitude()][location.getLatitude()] = new LocationNode(location);
            }
        }
        for (Object dr : _doors.values()) {
            Door door = (Door) dr;
            _locations[door.getLocation().getLongitude()][door.getLocation().getLatitude()] = door.getLocation();
            _locationNodes[door.getLocation().getLongitude()][door.getLocation().getLatitude()] = new LocationNode(door.getLocation());
        }
    }

    public Room getRoom(String roomName){
        return (Room)_rooms.get(roomName);
    }

    public Room getRoom(Location location){
        for (Object room : _rooms.values()) {
            if(((Room)room).isLocationInRoom(location))
                return (Room)room;
        }
        return null;
    }

    public void setLocationNodesNeighbors() {
        for(int i = 0; i < _height; i++){
        	for(int j = 0; j < _width; j++){
                if(_locationNodes[i][j] != null){
                    Vector<LocationNode> neighbors = _locationNodes[i][j].getNeighbors();
                    if(nodeAccesable(i - 1, j -1) && !differentRooms(i, j, i-1, j-1))
                        neighbors.add(_locationNodes[i - 1][j - 1]);
                    if(nodeAccesable(i - 1, j) && !differentRooms(i, j, i-1, j))
                        neighbors.add(_locationNodes[i - 1][j]);
                    if(nodeAccesable(i - 1, j +1) && !differentRooms(i, j, i-1, j+1))
                        neighbors.add(_locationNodes[i - 1][j + 1]);
                    if(nodeAccesable(i, j -1) && !differentRooms(i, j, i, j-1))
                        neighbors.add(_locationNodes[i][j - 1]);
                    if(nodeAccesable(i, j +1) && !differentRooms(i, j, i, j+1))
                        neighbors.add(_locationNodes[i][j + 1]);
                    if(nodeAccesable(i + 1, j -1) && !differentRooms(i, j, i+1, j-1))
                        neighbors.add(_locationNodes[i + 1][j - 1]);
                    if(nodeAccesable(i + 1, j) && !differentRooms(i, j, i+1, j))
                        neighbors.add(_locationNodes[i+1][j]);
                    if(nodeAccesable(i + 1, j +1) && !differentRooms(i, j, i+1, j+1))
                        neighbors.add(_locationNodes[i + 1][j + 1]);

//                    if(nodeAccesable(i - 1, j -1) )
//                        neighbors.add(_locationNodes[i - 1][j - 1]);
//                    if(nodeAccesable(i - 1, j) )
//                        neighbors.add(_locationNodes[i - 1][j]);
//                    if(nodeAccesable(i - 1, j +1) )
//                        neighbors.add(_locationNodes[i - 1][j + 1]);
//                    if(nodeAccesable(i, j -1) )
//                        neighbors.add(_locationNodes[i][j - 1]);
//                    if(nodeAccesable(i, j +1) )
//                        neighbors.add(_locationNodes[i][j + 1]);
//                    if(nodeAccesable(i + 1, j -1) )
//                        neighbors.add(_locationNodes[i + 1][j - 1]);
//                    if(nodeAccesable(i + 1, j) )
//                        neighbors.add(_locationNodes[i+1][j]);
//                    if(nodeAccesable(i + 1, j +1) )
//                        neighbors.add(_locationNodes[i + 1][j + 1]);
                }
            }
        }
    }

    private boolean nodeAccesable(int row, int column) {
        return row >= 0 && row < _height && column >= 0 && column < _width && _locations[row][column] != null;
    }

    private boolean differentRooms(int i, int j, int i1, int j1) {
        Location l1 = _locations[i][j];
        Location l2 = _locations[i1][j1];

        Room room1 = getRoom(l1);
        Room room2 = getRoom(l2);

        if(room1 == null || room2 == null)
            return true;
        if(!room1.equals(room2)){
            if(_doors.containsKey(l1) || _doors.containsKey(l2))
                return false;
            else
                return true;
        }
        else
            return false;
    }

    public int getHeight(){
        return _height;
    }

    public int getWidth(){
        return _width;
    }
}
