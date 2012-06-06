package utilities;

import java.lang.reflect.Type;

import utilities.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LocationDeserializer implements JsonDeserializer<Location> {

	@Override
	public Location deserialize(JsonElement jsonLocation, Type locationType,
			JsonDeserializationContext context) {
		
		JsonObject jobject = (JsonObject) jsonLocation;
		
		int latitude = jobject.get("latitude").getAsInt();
		int longitude = jobject.get("longitude").getAsInt();
		String room = jobject.get("room").getAsString();
		
		Location loc = new Location(longitude, latitude);
		loc.setRoom(room);
		
		return loc;
	}

}
