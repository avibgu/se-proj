package utilities;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import type.AgentType;
import type.ItemType;
import actor.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ActivitySerializer implements JsonSerializer<Activity>{

	@Override
	public JsonElement serialize(Activity activity, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonItem = new JsonObject();
		jsonItem.addProperty("id", activity.getId());
		jsonItem.addProperty("type", activity.getType());
		jsonItem.addProperty("state", activity.getState().toString());
		jsonItem.addProperty("startTime", activity.getStartTime().toString());
		jsonItem.addProperty("endTime", activity.getStartTime().toString());
		jsonItem.addProperty("estimatedTime", activity.getEstimateTime());
		
		JsonArray requiredAgents = new JsonArray();
		for (Map.Entry<AgentType, Integer> agentType : activity.getRequiredAgents().entrySet()) {
			AgentType type = agentType.getKey();
		    Integer num = agentType.getValue();
		    JsonPrimitive jType = new JsonPrimitive(type.toString());
		    JsonPrimitive jNum = new JsonPrimitive(num);
		    requiredAgents.add(jType);
		    requiredAgents.add(jNum);
		}
		jsonItem.add("requiredAgents", requiredAgents);
		
		JsonArray requiredItems = new JsonArray();
		for (Map.Entry<ItemType, Integer> agentType : activity.getRequiredItems().entrySet()) {
			ItemType type = agentType.getKey();
		    Integer num = agentType.getValue();
		    JsonPrimitive jType = new JsonPrimitive(type.getType());
		    JsonPrimitive jNum = new JsonPrimitive(num);
		    requiredItems.add(jType);
		    requiredItems.add(jNum);
		}
		jsonItem.add("requiredItems", requiredItems);
		
		JsonArray requiredActivityIds = serializeStringSet(activity.getRequiredActivityIds());
		jsonItem.add("requiredActivityIds", requiredActivityIds);
		JsonArray participatingAgentIds = serializeStringSet(activity.getParticipatingAgentIds());
		jsonItem.add("participatingAgentIds", participatingAgentIds);
		JsonArray participatingItemIds = serializeStringSet(activity.getParticipatingItemIds());
		jsonItem.add("participatingItemIds", participatingItemIds);
		
		jsonItem.addProperty("description", activity.getDescription());
		jsonItem.addProperty("name", activity.getName());
		
		return jsonItem;
	}
	
	private JsonArray serializeStringSet(Set<String> set){
		JsonArray jsonStringArray = new JsonArray();
		for (String element : set) {
			JsonPrimitive jElement = new JsonPrimitive(element);
			jsonStringArray.add(jElement);
		}
		return jsonStringArray;
	}
	
}
