package utilities;

import java.lang.reflect.Type;

import utilities.Location;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocationSerializer implements JsonSerializer<Location> {

	@Override
	public JsonElement serialize(Location location, Type locationType, JsonSerializationContext context) {
		JsonObject jsonItem = new JsonObject();
		jsonItem.addProperty("latitude", location.getLatitude());
		jsonItem.addProperty("longitude", location.getLongitude());
		return jsonItem;
	}

}
