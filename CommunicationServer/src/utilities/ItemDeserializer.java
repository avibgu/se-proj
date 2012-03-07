package utilities;

import java.lang.reflect.Type;

import simulator.Location;
import type.ItemType;

import State.ItemState;
import actor.Item;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ItemDeserializer implements JsonDeserializer<Item> {
	
	@Override
	public Item deserialize(JsonElement jsonItem, Type itemType, 
			JsonDeserializationContext context) {
		
		JsonObject jobject = (JsonObject) jsonItem;
		
		String id = jobject.get("id").getAsString();
		Location location = context.deserialize(jobject.get("location"), Location.class);
		Location oldLocation = context.deserialize(jobject.get("old location"), Location.class);
		ItemState state = ItemState.valueOf(jobject.get("state").getAsString().toUpperCase());
		ItemType type = ItemType.valueOf(jobject.get("type").getAsString().toUpperCase());
		
		Item i = new Item();
		i.setId(id);
		i.setLocation(oldLocation);
		i.setLocation(location);
		i.setState(state);
		i.setType(type);
		
		return i;
	}

}
