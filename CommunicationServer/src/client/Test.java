package client;

import java.io.IOException;
import java.util.Vector;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import simulator.Location;
import type.AgentType;
import type.ItemType;
import utilities.MovaJson;


import State.ActivityState;
import actor.Activity;
import actor.Agent;
import actor.Item;


public class Test {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		MovaClient mc = new MovaClient();
		
		//Vector<Item> items = mc.findItem(ItemType.BOARD, 3, new Location(3, 3));
		
		//mc.distributeItemLocation("ad", new Location(2, 2));

		//mc.distributeItemState("as", ItemState.AVAILABLE);
		
		
		
		MovaJson mj = new MovaJson();
		
		Vector<String> ac = new Vector<String>();
		Agent a1 = new Agent(AgentType.Coordinator);
		Agent a2 = new Agent(AgentType.RFID);
		
		ac.add(a1.getId());
		ac.add(a2.getId());
		
		//mc.sendActivity(new Activity(), ac);
		
		mc.changeAgentLocation("213", new Location(1,1));
		
		mc.changeActivityStatus(new Activity().getId(), ActivityState.COMPLETED);
	}
}
