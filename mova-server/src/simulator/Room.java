package simulator;

import java.util.Vector;

import utilities.Location;
/**
 * The room class holds all the locations that correlate to that room.
 */
public class Room {
    private Vector<Location> _margins;
    private String _name;
    /**
     * Creates a new Room with a unique name and margins
     * @param uniqueName the unique name of the room
     * @param margins the margins of the room
     */
    public Room(String uniqueName, Vector<Location> margins){
        _name = uniqueName;
        _margins  = margins;
    }
    /**
     * Creates a new empty room
     * setMargins and setName are required to use the object 
     */
    public Room(){
        _margins  = new Vector<Location>();
        _name = "";
    }
    /**
     * Sets the room's margins
     * @param margins the room margins to set
     */
    public void setMargins(Vector<Location> margins){
        _margins  = margins;
    }
    /**
     * Returns the room margins
     * @return the room margins
     */
    public Vector<Location> getMargins(){
        return _margins;
    }
    /**
     * Returns the room name
     * @return the room name
     */
    public String getName(){
        return _name;
    }
    /**
     * Sets the room's unique name
     * @param uniqueName the unique name to set
     */
    public void setName(String uniqueName){
        _name = uniqueName;
    }
    /**
     * Checks if the location given is in the margins of the room
     * @param location the location to check
     * @return true if it is in the margins, false otherwise
     */
    public boolean isLocationInRoom(Location location){
        for (Location location1 : _margins) {
            if(location.equals(location1))
                return true;
        }
        return false;
    }
    /**
     * Checks to see if a room is equal to another room only 
     * by checking the room name
     */
    @Override
    public boolean equals(Object room){
        if(room != null && room instanceof Room)
            return _name.equals(((Room)room).getName());
        return false;
    }

//    public Location randomLocationInRoom(){
//        //Optional: add int array to randomize an unpopulated location in the room
//        Random r = new Random();
//        int l = r.nextInt(_margins.size());
//        return _margins.elementAt(l);
//    }
}
