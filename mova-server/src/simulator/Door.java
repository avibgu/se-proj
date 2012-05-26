

package simulator;

import utilities.Location;
/**
 * The Door class is used as a simulated object
 * that separates two rooms.
 * It has a location and holds a reference two the two rooms
 */
public class Door {
    Location mLocation;
    Room mRoom1;
    Room mRoom2;

    /**
     * Creates a new door that separates two rooms and has a location
     * @param location the door's location
     * @param room1 the first room
     * @param room2 the second room
     */
    public Door(Location location, Room room1, Room room2){
        mLocation = location;
        mRoom1 = room1;
        mRoom2 = room2;
    }

    /**
     * Returns the door's location
     * @return the door's location
     */
    public Location getLocation(){
        return mLocation;
    }

    /**
     * Returns the first room
     * @return the first room
     */
    public Room getRoom1(){
        return mRoom1;
    }

    /**
     * Returns the first room
     * @return the first room
     */
    public Room getRoom2(){
        return mRoom2;
    }
}
