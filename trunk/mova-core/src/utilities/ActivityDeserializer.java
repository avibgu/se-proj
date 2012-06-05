package utilities;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import state.ActivityState;
import type.AgentType;
import type.ItemType;

import actor.Activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ActivityDeserializer implements JsonDeserializer<Activity> {

	@Override
	public Activity deserialize(JsonElement pJsonItem, Type pItemType,
			JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jobject = (JsonObject) pJsonItem;
		
		String id = jobject.get("id").getAsString();
		String type = jobject.get("type").getAsString();
		String activityState = jobject.get("state").getAsString();
		String jStartTime = jobject.get("startTime").getAsString();
		Timestamp startTime = Timestamp.valueOf(jStartTime);
		String jEndTime = jobject.get("endTime").getAsString();
		Timestamp endTime = Timestamp.valueOf(jEndTime);
		long estimateTime = jobject.get("estimatedTime").getAsLong();
		
		JsonArray jRequiredAgents = jobject.get("requiredAgents").getAsJsonArray();
		Map<AgentType, Integer> requiredAgents = toRequiredAgents(jRequiredAgents);
		
		JsonArray jRequiredItems = jobject.get("requiredItems").getAsJsonArray();
		Map<ItemType, Integer> requiredItems = toRequiredItems(jRequiredItems);
		
		JsonArray jRequiredActivityIds = jobject.get("requiredActivityIds").getAsJsonArray();
		Set<String> requiredActivityIds = toStringSet(jRequiredActivityIds);
		
		JsonArray jParticipatingAgentIds = jobject.get("participatingAgentIds").getAsJsonArray();
		Set<String> participatingAgentIds = toStringSet(jParticipatingAgentIds);
		
		JsonArray jParticipatingItemIds = jobject.get("participatingItemIds").getAsJsonArray();
		Set<String> participatingItemIds = toStringSet(jParticipatingItemIds);
		
		String description = jobject.get("description").getAsString();
		String name = jobject.get("name").getAsString();
		
		Timestamp actualStartTime = Timestamp.valueOf(jobject.get("actualStartTime").getAsString());
		Timestamp actualEndTime = Timestamp.valueOf(jobject.get("actualEndTime").getAsString());
		
		Activity a = new Activity(type, startTime, endTime, estimateTime, requiredAgents, requiredItems, requiredActivityIds, description, name);
		a.setId(id);
		a.setState(ActivityState.valueOf(activityState));
		a.assignAgents(participatingAgentIds);
		a.assignItems(participatingItemIds);
		a.setActualStartTime(actualStartTime);
		a.setActualEndTime(actualEndTime);
		
		return a;
	}
	
	private Set<String> toStringSet(JsonArray jsonSet){
		Set<String> set = new HashSet<String>(); 
		for (JsonElement jsonElement : jsonSet) {
			set.add(jsonElement.getAsString());
		}
		return set;
	}

	private Map<AgentType, Integer> toRequiredAgents(JsonArray jsonArray){
		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		for(int i = 0; i < jsonArray.size() - 1; i+=2)
		{
			AgentType type = new AgentType(jsonArray.get(i).getAsString());
			Integer num = new Integer(jsonArray.get(i + 1).getAsInt());
			requiredAgents.put(type, num);
		}
		return requiredAgents;
	}
	
	private Map<ItemType, Integer> toRequiredItems(JsonArray jsonArray){
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		for(int i = 0; i < jsonArray.size() - 1; i+=2)
		{
			ItemType type = new ItemType(jsonArray.get(i).getAsString());
			Integer num = new Integer(jsonArray.get(i + 1).getAsInt());
			requiredItems.put(type, num);
		}
		return requiredItems;
	}
}
