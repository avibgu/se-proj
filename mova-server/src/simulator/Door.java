

package simulator;

import utilities.Location;

public class Door {
    Location mLocation;
    Room mRoom1;
    Room mRoom2;

    public Door(Location location, Room room1, Room room2){
        mLocation = location;
        mRoom1 = room1;
        mRoom2 = room2;
    }

    public Location getLocation(){
        return mLocation;
    }

    public Room getRoom1(){
        return mRoom1;
    }

    public Room getRoom2(){
        return mRoom2;
    }
}
