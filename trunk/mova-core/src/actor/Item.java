package actor;

import state.ItemState;
import type.ItemType;

public class Item extends Entity{
	protected ItemType type;
	protected ItemState state;

	public Item(){}
	
	public Item(ItemType itemType){
		type = itemType;
	}
	
	public ItemState getState() {
		return state;
	}

	public void setState(ItemState state) {
		this.state = state;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}
	
	public void markAsBusy() {
		
		synchronized (state) {
		
			state = ItemState.BUSY;
		}
	}
	
	public void markAsAvailable() {
		
		synchronized (state) {
		
			state = ItemState.AVAILABLE;
		}
	}
}
