package resource;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import actor.Item;

import state.ItemState;
import type.ItemType;
import utilities.MovaJson;
import client.MovaClient;
import db.DBHandler;

public class ItemResourceTest {
	
	private MovaClient mc;
	private DBHandler db;
	private MovaJson mj;
	
	@Before
	public void setUp() throws Exception {
		mc = new MovaClient();
		db = DBHandler.getInstance();
		mj = new MovaJson();
	}

	@Test
	public void testFindItem() {
		fail("Not implementing at the moment");
	}

	@Test
	public void testDistributeItemState() {
		fail("Not yet implemented in the android client");
	}

	@Test
	public void testDeleteItem() {
		Vector<Item> items = db.getItems();
		Item item = null;
		if(!items.isEmpty()){
			item = items.elementAt(0);
			mc.deleteItem(item.getId());
		}
		items = db.getItems();
		
		assertFalse(items.contains(item));
	}

	@Test
	public void testChangeItemStatus() {
		Item item = db.getItems().elementAt(0);
		if(item != null){
			mc.changeItemStatus(item.getId(), ItemState.BUSY.toString(), "");
		}
		ItemState state = db.getItemState(item.getId());
		assertEquals(ItemState.BUSY, state);
	}

	@Test
	public void testCreateItemType() {
		mc.createItemType(ItemType.PROJECTOR.toString());
		Vector<String> types = db.getItemTypes();
		assertTrue(types.contains(ItemType.PROJECTOR.toString()));
	}

	@Test
	public void testDeleteItemType() {
		Vector<String> types = db.getItemTypes();
		Vector<Item> items = db.getItems();
		String type = "";
		if(!types.isEmpty()){
			type = types.elementAt(0);
			for (Item item : items) {
				if(item.getType().getType().equals(type))
					db.deleteItem(item.getId());
			}
			mc.deleteItemType(type);
		}
		types = db.getItemTypes();
		assertFalse(types.contains(type));
	}

	@Test
	public void testGetItems() {
		Vector<Item> items = mc.getItems("");
		assertTrue(!items.isEmpty());
	}

}
