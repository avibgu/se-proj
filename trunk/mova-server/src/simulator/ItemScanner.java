package simulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.Vector;

import utilities.Location;

import actor.Entity;
import actor.Item;
import actor.SensorAgent;
/**
 * The ItemScanner Runnable is used to simulate a sensor actions. 
 * It scans according to a scan distance configured in the properties file
 * and identifies items that are at range. The Item scanner creates a new message when it
 * identifies new items at range.
 */
public class ItemScanner implements Runnable {
	
	protected boolean mDontStop;
	protected NewDomain mDomain;
	protected SensorAgent mSensorAgent;
	protected Location mSensorLocation;
	protected final long mRefreshRate = 1;
	protected final double mScanDistance;
	protected Vector<Item> mVisibleItems;
	protected Map<String, Item> mIsibleItems;
	protected Vector<Observer> mObservers;
	/**
	 * Creates a new ItemScanner Thread
	 * @param entity the sensor agent it correlates to
	 * @param domain the domain in which it lives in
	 * @param observers the observers that the are notified when new items are identified
	 * @param scanDistance the scan distance of the scanner
	 */
	public ItemScanner(Entity entity, NewDomain domain, Vector<Observer> observers, double scanDistance){
		mDomain = domain;
		mSensorAgent = (SensorAgent)entity;
		mSensorLocation = mSensorAgent.getLocation();
		mDontStop = true;
		mVisibleItems = new Vector<Item>();
		mIsibleItems = new HashMap<String, Item>();
		mObservers = observers;
		mScanDistance = scanDistance;
		
		mVisibleItems = mDomain.scanForItems(mSensorLocation, mScanDistance);
		mIsibleItems = vectorToHashMap(mDomain.scanForItems(mSensorLocation, mScanDistance));
		String message = mSensorAgent.getType().toString() + " Sensor identified items: ";
		if(mIsibleItems.size() > 0){
			createIdentifiedItemsMessage(message, mIsibleItems);
		}
	}
	
	@Override
	public void run() {
		while (mDontStop){
			synchronized (mDomain.getEntities()) {
				Map<String, Item> visible = vectorToHashMap(mDomain.scanForItems(mSensorLocation, mScanDistance));
				Map<String, Item> changedLocationItems = new HashMap<String, Item>(); 
				
				for (Map.Entry<String, Item> item : visible.entrySet()) {
					if(!mIsibleItems.containsKey(item.getKey()))
						changedLocationItems.put(item.getKey(), item.getValue());
				}
			
				mSensorAgent.setVisibleItems(visible);
				mIsibleItems = visible;
				if(changedLocationItems.size() > 0){
	//				for (Observer ob : _observers) {
	//					ob.update(null, changedLocationItems);
	//				}
	                mDomain.changeItemsLocation(changedLocationItems);
	                String message = mSensorAgent.getType().toString() + " Sensor identified new items: ";
	            	createIdentifiedItemsMessage(message, changedLocationItems);
				}
			}
			try {
				Thread.sleep(mRefreshRate * 200);
			} catch (InterruptedException e) {
				stop();
			}
		}
	}
	
	private void createIdentifiedItemsMessage(String initialMessage, Map<String, Item> changedLocationItems){
        for (Item item : changedLocationItems.values()) {
        	initialMessage = initialMessage.concat(item.getType().toString() 
        			+ " at ( " + item.getLocation().getLongitude() + "," 
        			+ item.getLocation().getLatitude() + "), ");
		}
        initialMessage = initialMessage.substring(0, initialMessage.length() - 2);
        mDomain.addMessage(initialMessage);
	}
	
	private Map<String, Item> vectorToHashMap(Vector<Item> items){
		Map<String, Item> visibleItems = new HashMap<String, Item>();
		for (Item item : items) {
			visibleItems.put(item.getId(), item);
		}
		return visibleItems;
	}
	/**
	 * Stops the ItemScanner
	 */
	public void stop() {
		mDontStop = false;
	}
}
