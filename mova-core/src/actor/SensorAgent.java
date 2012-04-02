package actor;

import java.util.Vector;
import type.AgentType;

public class SensorAgent extends Agent {
	
	protected Vector<Entity> mVisibleItems;
	
	public SensorAgent(AgentType pType) {
		super(pType);
		mVisibleItems = new Vector<Entity>();
	}

	public void setVisibleItems(Vector<Entity> pItems){
		synchronized (mVisibleItems) {
			mVisibleItems = pItems;	
		}
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Entity> getVisibleItems(){
		
		Vector<Entity> visibleItems = new Vector<Entity>();
		
		synchronized (mVisibleItems) {
			visibleItems = (Vector<Entity>) mVisibleItems.clone();
		}
		
		return visibleItems;
	}
}
