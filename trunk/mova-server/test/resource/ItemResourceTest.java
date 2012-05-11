package resource;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import actor.Item;

import utilities.Location;
import type.ItemType;

import client.MovaClient;

public class ItemResourceTest {
	
	private MovaClient _mc;
	@Before
	public void setUp() throws Exception {
		_mc = new MovaClient();
	}

	@Test
	public void testFindItem() {
		Vector<Item> items1 = _mc.findItem(new ItemType("BOARD"), 1, new Location(0, 0));
		Item i1 = new Item(new ItemType("BOARD"));
		assertEquals(i1.getType(), items1.elementAt(0).getType());
		
		Vector<Item> items2 = _mc.findItem(new ItemType("CHAIR"), 1, new Location(0, 0));
		assertEquals(0, items2.size());
	}

	@Test
	public void testDistributeItemLocation() {
		fail("Not yet implemented");
	}

	@Test
	public void testDistributeItemState() {
		fail("Not yet implemented");
	}

}
