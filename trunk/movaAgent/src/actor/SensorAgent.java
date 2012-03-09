package actor;

import java.util.Vector;
import type.AgentType;

public class SensorAgent extends Agent {
	
	protected Vector<Entity> _visibleItems;
	
	public SensorAgent(AgentType type) {
		super(type);
		_visibleItems = new Vector<Entity>();
	}

	public synchronized void setVisibleItems(Vector<Entity> items){
		_visibleItems = items;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Entity> getVisibleItems(){
		Vector<Entity> visibleItems = new Vector<Entity>();
		synchronized (_visibleItems) {
			visibleItems = (Vector<Entity>) _visibleItems.clone();
		}
		return visibleItems;
	}
}
