package actor;

import state.ItemState;
import type.ItemType;

public class Item extends Entity{
	
	protected ItemType mType;
	protected ItemState mState;

	public Item(){}
	
	public Item(ItemType pItemType){
		mType = pItemType;
	}
	
	public ItemState getState() {
		synchronized (mState) {
			return mState;
		}
	}

	public void setState(ItemState pState) {
		synchronized (mState) {
			this.mState = pState;
		}
	}

	public ItemType getType() {
		synchronized (mType) {
			return mType;
		}
	}

	public void setType(ItemType pType) {
		synchronized (mType) {
			this.mType = pType;
		}
	}
	
	public void markAsBusy() {
		synchronized (mState) {
			mState = ItemState.BUSY;
		}
	}
	
	public void markAsAvailable() {
		synchronized (mState) {
			mState = ItemState.AVAILABLE;
		}
	}
}
