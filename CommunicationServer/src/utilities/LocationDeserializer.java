package utilities;

import java.lang.reflect.Type;

import simulator.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class LocationDeserializer implements JsonDeserializer<Location> {

	@Override
	public Location deserialize(JsonElement jsonLocation, Type locationType,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jobject = (JsonObject) jsonLocation;
		
		int latitude = jobject.get("latitude").getAsInt();
		int longitude = jobject.get("longitude").getAsInt();
		
		Location loc = new Location(latitude, longitude);
		
		return loc;
	}

}
