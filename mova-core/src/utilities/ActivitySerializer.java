package utilities;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Map;

import type.AgentType;
import type.ItemType;

import actor.Activity;
import actor.Item;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ActivitySerializer implements JsonSerializer<Activity>{

	@Override
	public JsonElement serialize(Activity activity, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonItem = new JsonObject();
		jsonItem.addProperty("id", activity.getId());
		jsonItem.addProperty("name", activity.getName());
		jsonItem.addProperty("description", activity.getDescription());
		jsonItem.addProperty("type", activity.getType());
		jsonItem.addProperty("state", activity.getState().toString());
		jsonItem.addProperty("startTime", activity.getStartTime().toString());
		jsonItem.addProperty("endTime", activity.getStartTime().toString());
		jsonItem.addProperty("estimatedTime", activity.getEstimateTime());
		
		JsonArray requiredAgents = new JsonArray();
		for (Map.Entry<AgentType, Integer> agentType : activity.getRequiredAgents().entrySet()) {
			AgentType type = agentType.getKey();
		    Integer num = agentType.getValue();
		    JsonObject jAgentType = new JsonObject();
		    jAgentType.addProperty(type.toString(), num);
		    requiredAgents.add(jAgentType);
		}
		jsonItem.add("requiredAgents", requiredAgents);
		
		JsonArray requiredItems = new JsonArray();
		for (Map.Entry<ItemType, Integer> agentType : activity.getRequiredItems().entrySet()) {
			ItemType type = agentType.getKey();
		    Integer num = agentType.getValue();
		    JsonObject jItemType = new JsonObject();
		    jItemType.addProperty(type.toString(), num);
		    requiredItems.add(jItemType);
		}
		jsonItem.add("requiredItems", requiredItems);
		//TODO continue from here
		return jsonItem;
	}

}
