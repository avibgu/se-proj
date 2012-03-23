

package simulator;

public class Door {
    Location _location;
    Room _room1;
    Room _room2;

    public Door(Location location, Room room1, Room room2){
        _location = location;
        _room1 = room1;
        _room2 = room2;
    }

    public Location getLocation(){
        return _location;
    }

    public Room getRoom1(){
        return _room1;
    }

    public Room getRoom2(){
        return _room2;
    }
}
