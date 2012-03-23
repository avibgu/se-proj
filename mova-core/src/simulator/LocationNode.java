package simulator;

import java.util.Vector;

public class LocationNode {
    private Location _location;
    private boolean _marked;
    private Vector<LocationNode> _neighbors;
    private LocationNode _parent;

    public LocationNode(Location location){
        _location = location;
        _marked = false;
        _neighbors = new Vector<LocationNode>();
        _parent = null;
    }

    public boolean isMarked(){
        return _marked;
    }

    public void mark(){
        _marked= true;
    }

    public void unMark(){
        _marked= false;
    }

    public Vector<LocationNode> getNeighbors(){
        return _neighbors;
    }

    public Location getLocation(){
        return _location;
    }

    public LocationNode getParent(){
        return _parent;
    }

    public void setParent(LocationNode parent){
        _parent = parent;
    }
}
