package simulator;

import java.util.Vector;

import utilities.Location;
/**
 * The LocationNode class is used for 
 * calculating the shortest path used by
 * simulated agents
 */
public class LocationNode {
    private Location mLocation;
    private boolean mMarked;
    private Vector<LocationNode> mNeighbors;
    private LocationNode mParent;
    /**
     * Creates a new LocationNode with a location
     * @param location the location of the node
     */
    public LocationNode(Location location){
        mLocation = location;
        mMarked = false;
        mNeighbors = new Vector<LocationNode>();
        mParent = null;
    }
    /**
     * Returns true if the location has been visited
     * @return true if the location has been visited
     */
    public boolean isMarked(){
        return mMarked;
    }
    /**
     * Marks the node as visited
     */
    public void mark(){
        mMarked= true;
    }
    /**
     * unmarks the node
     */
    public void unMark(){
        mMarked= false;
    }
    /**
     * Returns the node's neighbors
     * @return the node's neighbors
     */
    public Vector<LocationNode> getNeighbors(){
        return mNeighbors;
    }
    /**
     * Returns the node's location
     * @return the node's location
     */
    public Location getLocation(){
        return mLocation;
    }
    /**
     * Returns the node's parent
     * @return the node's parent
     */
    public LocationNode getParent(){
        return mParent;
    }
    /**
     * Sets the node's parent
     * @param parent the node's parent
     */
    public void setParent(LocationNode parent){
        mParent = parent;
    }
}
