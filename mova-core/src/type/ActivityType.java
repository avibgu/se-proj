package type;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import actor.Activity;

public class ActivityType {

	public static final String CONFERENCE_REGISTRATION = "CONFERENCE_REGISTRATION";
	public static final String TOUR = "TOUR";
	public static final String LUNCH = "LUNCH";
	public static final String PRESENTATION = "PRESENTATION";
	public static final String DISCUSSION = "DISCUSSION";

	protected String mType;
	protected Map<AgentType, Integer> mRequiredAgents;
	protected Map<ItemType, Integer> mRequiredItems;

	public ActivityType(String pType) {

		mType = pType;
		mRequiredAgents = new HashMap<AgentType, Integer>();
		mRequiredItems = new HashMap<ItemType, Integer>();
	}
	
	public ActivityType(String pType, Map<AgentType, Integer> pRequiredAgents,
			Map<ItemType, Integer> pRequiredItems) {

		mType = pType;
		mRequiredAgents = pRequiredAgents;
		mRequiredItems = pRequiredItems;
	}
	
	public Activity getInstance(Timestamp pStartTime, Timestamp pEndTime, long pEstimateTime, Set<String> pRequiredActivities, String pDescription, String pName){
		return new Activity(mType, pStartTime, pEndTime, pEstimateTime, mRequiredAgents, mRequiredItems, pRequiredActivities, pDescription, pName);
	}

	public String getType() {
		return mType;
	}

	public void setType(String pType) {
		mType = pType;
	}

	@Deprecated
	public static String[] values() {
		return new String[]{CONFERENCE_REGISTRATION,TOUR,LUNCH,PRESENTATION,DISCUSSION};
	}
}
