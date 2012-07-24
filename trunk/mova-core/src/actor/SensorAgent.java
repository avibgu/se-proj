package actor;

import java.util.HashMap;
import java.util.Map;
import type.AgentType;

public class SensorAgent extends Agent {
	
	protected Map<String, Item> mVisibleItems;
	
	public SensorAgent(AgentType pType) {
		super(pType);
		mVisibleItems = new HashMap<String, Item>();
	}

	public void setVisibleItems(Map<String, Item> pItems){
		synchronized (mVisibleItems) {
			mVisibleItems = pItems;	
		}
	}

	public Map<String, Item> getVisibleItems(){
		
		Map<String, Item> visibleItems = new HashMap<String, Item>();
		
		synchronized (mVisibleItems) {
			for (Map.Entry<String, Item> item : mVisibleItems.entrySet()) {
				visibleItems.put(item.getKey(), item.getValue());
			}
		}
		
		return visibleItems;
	}
}
