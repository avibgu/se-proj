package simulator;

import java.util.Random;
import java.util.Vector;

public class Room {
    private Vector<Location> _margins;
    private String _name;

    public Room(String uniqueName, Vector<Location> margins){
        _name = uniqueName;
        _margins  = margins;
    }

    public Room(){
        _margins  = new Vector<Location>();
        _name = "";
    }

    public void setMargins(Vector<Location> margins){
        _margins  = margins;
    }

    public Vector<Location> getMargins(){
        return _margins;
    }
    
    public String getName(){
        return _name;
    }

    public void setName(String uniqueName){
        _name = uniqueName;
    }

    public boolean isLocationInRoom(Location location){
        for (Location location1 : _margins) {
            if(location.equals(location1))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object room){
        if(room != null && room instanceof Room)
            return _name.equals(((Room)room).getName());
        return false;
    }

    public Location randomLocationInRoom(){
        //Optional: add int array to randomize an unpopulated location in the room
        Random r = new Random();
        int l = r.nextInt(_margins.size());
        return _margins.elementAt(l);
    }
}
