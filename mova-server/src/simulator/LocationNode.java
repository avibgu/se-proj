package simulator;

import java.util.Vector;

import utilities.Location;

public class LocationNode {
    private Location mLocation;
    private boolean mMarked;
    private Vector<LocationNode> mNeighbors;
    private LocationNode mParent;

    public LocationNode(Location location){
        mLocation = location;
        mMarked = false;
        mNeighbors = new Vector<LocationNode>();
        mParent = null;
    }

    public boolean isMarked(){
        return mMarked;
    }

    public void mark(){
        mMarked= true;
    }

    public void unMark(){
        mMarked= false;
    }

    public Vector<LocationNode> getNeighbors(){
        return mNeighbors;
    }

    public Location getLocation(){
        return mLocation;
    }

    public LocationNode getParent(){
        return mParent;
    }

    public void setParent(LocationNode parent){
        mParent = parent;
    }
}
