package gui;

import actor.Entity;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import simulator.Door;
import simulator.Location;
import simulator.MovaMap;
import simulator.NewDomain;
import simulator.Room;

public class WalkingAgentWorker extends SwingWorker<Object, Object>{

    private NewDomain _domain;
    private MovaMap _movaMap;
    private Entity _entity;
    private Location _from;
    private Location _to;
    private Vector<Door> _doors;

    public WalkingAgentWorker(NewDomain domain, Entity entity, Location from, Location to, Vector<Door> doors){
        _domain = domain;
        _movaMap = _domain.getMovaMap();
        _entity = entity;
        _from = from;
        _to = to;
        _doors = doors;
    }
    @Override
    protected Object doInBackground() throws Exception {
        Room fromRoom = _movaMap.getRoom(_from);
        Room toRoom = _movaMap.getRoom(_to);
        if(fromRoom.getName().equals(toRoom.getName())){
            Stack<Location> path = _movaMap.calculateShortestPath(_from, _to);
            for (int i = 0; i < path.size() - 1; i++) {
                Location oldLocation = path.pop();
                Location newLocation = path.pop();
                _domain.changeAgentLocation(_entity, oldLocation, newLocation);
            }
        }
        else{
            Location roomDoor = findClosestRoomDoor(_from, fromRoom);
            walkAgentToClosestDoor(_entity, _from, roomDoor);
            Stack<Location> path = _movaMap.calculateShortestPath(roomDoor, _to);
            Location oldLocation = roomDoor;
            while(path.size() > 0){
                Location newLocation = path.pop();
                _domain.changeAgentLocation(_entity, oldLocation, newLocation);
				Thread.sleep(800);
                oldLocation = newLocation;
            }
        }
        return null;
    }

    private void walkAgentToClosestDoor(Entity entity, Location from, Location roomDoor) {
        Stack<Location> path = _movaMap.calculateShortestPath(from, roomDoor);
        Location oldLocation = from;
        while(path.size() > 0){
            Location newLocation = path.pop();
            _domain.changeAgentLocation(entity, oldLocation, newLocation);
            try {
				Thread.sleep(800);
            } catch (InterruptedException ex) {
                Logger.getLogger(WalkingAgentWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
            oldLocation = newLocation;
        }
    }

    private Location findClosestRoomDoor(Location from, Room fromRoom) {
        Door closestDoor = null;
        int shortestPath = Integer.MAX_VALUE;
        for (Door door : _doors) {
            Room room1 = door.getRoom1();
            Room room2 = door.getRoom2();
            if(room1 == null || room2 == null)
                continue;
            if(room1.equals(fromRoom) || room2.equals(fromRoom)){
                Stack<Location> path = _movaMap.calculateShortestPath(from, door.getLocation());
                if(path.size() < shortestPath){
                    shortestPath = path.size();
                    closestDoor = door;
                }
            }
        }
        if(closestDoor != null)
            return closestDoor.getLocation();
        else
            return null;
    }
}
