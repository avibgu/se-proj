package actor;

import state.ItemState;
import type.ItemType;
/**
 * 
 * The Item class represents the items used in the system.
 * Each Item has a state describing its availability.
 *
 */
public class Item extends Entity{
	/**
	 * The item's type
	 */
	protected ItemType mType;
	/**
	 * The item's current state
	 */
	protected ItemState mState;
	
	/**
	 * Constructs an empty item. Type and state need to be set manually
	 */
	public Item(){
		mState = ItemState.AVAILABLE;
	}
	/**
	 * Constructs an item with the given item type
	 * @param pItemType the item type
	 */
	public Item(ItemType pItemType){
		mType = pItemType;
		mState = ItemState.AVAILABLE;
	}
	
	/**
	 * Returns the item's current state
	 * @return current state of the item
	 */
	public ItemState getState() {
		synchronized (mState) {
			return mState;
		}
	}
	/**
	 * Sets the state of the item
	 * @param pState the state to set
	 */
	public void setState(ItemState pState) {
		synchronized (mState) {
			this.mState = pState;
		}
	}
	/**
	 * Returns the item's type
	 * @return the item's type
	 */
	public ItemType getType() {
		synchronized (mType) {
			return mType;
		}
	}
	/**
	 * Sets the type of the item
	 * @param pType the type to set
	 */
	public void setType(ItemType pType) {
		synchronized (mType) {
			this.mType = pType;
		}
	}
	/**
	 * Changes the item's state to BUSY
	 */
	public void markAsBusy() {
		synchronized (mState) {
			mState = ItemState.BUSY;
		}
	}
	/**
	 * Changes the item's state to AVAILABLE
	 */
	public void markAsAvailable() {
		synchronized (mState) {
			mState = ItemState.AVAILABLE;
		}
	}
}
