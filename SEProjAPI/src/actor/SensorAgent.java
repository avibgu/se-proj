package actor;

import java.util.Vector;

import type.AgentType;

public class SensorAgent extends Agent {
	
	protected Vector<Entity> _visibleItems;
	
	public SensorAgent(AgentType type) {
		super(type);
		_visibleItems = new Vector<Entity>();
	}

	public void setVisibleItems(Vector<Entity> items){
		_visibleItems = items;
	}
	
	public Vector<Entity> getVisibleItems(){
		return _visibleItems;
	}
}
