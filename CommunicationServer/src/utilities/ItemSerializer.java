package utilities;

import java.lang.reflect.Type;

import actor.Item;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ItemSerializer implements JsonSerializer<Item> {

	@Override
	public JsonElement serialize(Item item, Type typeOfSrc,	JsonSerializationContext context) {
		JsonObject jsonItem = new JsonObject();
		jsonItem.addProperty("id", item.getId());
		jsonItem.add("location", context.serialize(item.getLocation()));
		jsonItem.add("old location", context.serialize(item.getOldLocation()));
		jsonItem.addProperty("state", item.getState().toString());
		jsonItem.addProperty("type", item.getType().toString());
		
		return jsonItem;
	}

}
