package resource;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import actor.Activity;
import actor.Agent;

import state.ActivityState;
import type.ActivityType;
import type.AgentType;
import type.ItemType;
import utilities.MovaJson;

import client.MovaClient;
import db.DBHandler;

public class ActivityResourceTest {
	
	private MovaClient mc;
	private DBHandler db;
	private MovaJson mj;
	
	@Before
	public void setUp() throws Exception {
		mc = new MovaClient();
		db = DBHandler.getInstance();
		mj = new MovaJson();
	}

	@Test//Passed
	public void testSendActivityFirstConstructor() {
		/* 
		protected String mId;
		protected String mType; +//DEFAULT
		protected ActivityState mState;+ //pending
		protected Timestamp mStartTime;+//now
		protected Timestamp mEndTime;+// 5 hours from now
		protected Timestamp mActualStartTime;+//startTime
		protected Timestamp mActualEndTime;+//endtime
		protected long mEstimateTime;+//1 hour
		protected Map<AgentType, Integer> mRequiredAgents;+//empty map
		protected Map<ItemType, Integer> mRequiredItems;+//empty map
		protected Set<String> mRequiredActivityIds;+//empty hashSet
		protected Set<String> mParticipatingAgentIds;+//empty hashSet
		protected Set<String> mParticipatingItemIds;+//empty hashSet
		protected String mDescription;+//DEFAULT ACTIVITY
		protected String mName;+//parameter given
		 */
		Activity a1 = new Activity("a1");
		Vector<String> agentIds = new Vector<String>();
		agentIds.add("1");
		mc.sendActivity(a1, agentIds);
	}
	
	@Test//Passed
	public void testSendActivitySecondConstructor() {
		Activity a1 = createTestActivity();
		Vector<String> agentIds = new Vector<String>();
		agentIds.add("1");
		mc.sendActivity(a1, agentIds);
	}
	
	@Test//Didn't Pass
	public void testSendScheduledActivities() {
		Activity a1 = createTestActivity();
		db.deleteActivity(a1.getId());
		Activity a2 = createTestActivity();
		db.deleteActivity(a2.getId());
		
		Set<String> pParticipatingAgentIds = new HashSet<String>();
		Agent agent1 = createTestAgent();
		pParticipatingAgentIds.add(agent1.getId());
		a1.setParticipatingAgentIds(pParticipatingAgentIds);
		a2.setParticipatingAgentIds(pParticipatingAgentIds);
		
		db.insertActivity(a1);
		db.insertActivity(a2);
		
		Vector<Activity> activities = new Vector<Activity>();
		activities.add(a1);
		activities.add(a2);
		
		mc.sendSchedule(activities, agent1.getId());
		String stringSchedule = mc.getSchedule(agent1.getId());
		List<Activity> acs = mj.jsonToActivities(stringSchedule);
		
		deleteTestAgent(agent1);
		deleteTestActivity(a1);
		deleteTestActivity(a2);
		db.deleteItem(ItemType.BOARD);
		assertEquals(a1.getId(), acs.get(0).getId());
		assertEquals(a2.getId(), acs.get(1).getId());

	}

	@Test //Passed
	public void testChangeActivityStatus() {
		Activity a1 = createTestActivity();
				
		ActivityState status = db.getActivityState(a1.getId());
		assertEquals(ActivityState.PENDING, status);
		
		mc.changeActivityStatus(a1.getId(), ActivityState.IN_PROGRESS);
		status = db.getActivityState(a1.getId());
		assertEquals(ActivityState.IN_PROGRESS, status);
		
		deleteTestActivity(a1);
	}

	@Test
	public void testAddActivity() {
		Activity a1 = createTestActivity();
		
		mc.addActivity(a1);
		
		List<Activity> acs = db.getAllActivities();
		
		assertEquals(a1.getId(), acs.get(0).getId());
		
		deleteTestActivity(a1);
	}

	@Test//Passed
	public void testPostponeActivity() {
		Activity a1 = createTestActivity();
		
		Timestamp a1DeadLine = db.getActivityDeadline(a1.getId());
		mc.postponeActivity(a1.getId(), 30);
		
		Timestamp a1NewDeadLine = db.getActivityDeadline(a1.getId());
		
		assertEquals(a1DeadLine.getTime() + 1000*60*30, a1NewDeadLine.getTime());
		
		deleteTestActivity(a1);
	}

	@Test//Passed
	public void testCreateActivityType() {
		mc.createActivityType(ActivityType.CONFERENCE_REGISTRATION);
		Vector<String> types = db.getActivityTypes();
		assertTrue(types.contains(ActivityType.CONFERENCE_REGISTRATION));
		db.deleteActivityType(ActivityType.CONFERENCE_REGISTRATION);
	}

	@Test
	public void testDeleteActivityType() {
		mc.createActivityType(ActivityType.CONFERENCE_REGISTRATION);
		Vector<String> types = db.getActivityTypes();
		assertTrue(types.contains(ActivityType.CONFERENCE_REGISTRATION));
		mc.deleteActivityType(ActivityType.CONFERENCE_REGISTRATION);
		types = db.getActivityTypes();
		assertFalse(types.contains(ActivityType.CONFERENCE_REGISTRATION));
	}

	@Test//Passed
	public void testGetAgentSchedule() {
		Activity a1 = createTestActivity();
		db.deleteActivity(a1.getId());
		Activity a2 = createTestActivity();
		db.deleteActivity(a2.getId());
		
		Set<String> pParticipatingAgentIds = new HashSet<String>();
		Agent agent1 = createTestAgent();
		pParticipatingAgentIds.add(agent1.getId());
		a1.setParticipatingAgentIds(pParticipatingAgentIds);
		a2.setParticipatingAgentIds(pParticipatingAgentIds);
		
		db.insertActivity(a1);
		db.insertActivity(a2);
		Vector<Activity> schedule = db.getAgentSchedule(agent1.getId());
		assertEquals(a1.getId(), schedule.elementAt(0).getId());
		assertEquals(a2.getId(), schedule.elementAt(1).getId());
		
		deleteTestAgent(agent1);
		deleteTestActivity(a1);
		deleteTestActivity(a2);
	}

	@Test//Passed
	public void testGetAllActivities() {
		Activity a1 = createTestActivity();
		Activity a2 = createTestActivity();
		
		List<Activity> acs = db.getAllActivities();
		
		assertEquals(a1.getId(), acs.get(0).getId());
		assertEquals(a2.getId(), acs.get(1).getId());
		
		deleteTestActivity(a1);
		deleteTestActivity(a2);
	}
	
	private Activity createTestActivity(){
		ActivityType type = new ActivityType(ActivityType.CONFERENCE_REGISTRATION);
		Timestamp startTime = new Timestamp(new Date().getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + 1000*60*60*3);
		long estimatedTime = 1000*60*60*2;
		Map<AgentType, Integer> pRequiredAgents = new HashMap<AgentType, Integer>();
		pRequiredAgents.put(new AgentType(AgentType.LECTURER.toString()), 3);
		db.insertAgentType(AgentType.LECTURER.toString());
		Map<ItemType, Integer> pRequiredItems = new HashMap<ItemType, Integer>();
		pRequiredItems.put(new ItemType(ItemType.BOARD.toString()), 1);
		//db.insertItemType(ItemType.BOARD.toString());
		Set<String> pRequiredActivities =  new HashSet<String>();
		pRequiredActivities.add("a2");
		String description = "testActivity";
		String name = "testName";
		Activity a1 = new Activity(type.getType(), startTime, endTime, estimatedTime, pRequiredAgents, pRequiredItems, pRequiredActivities, description, name);
		
		db.insertActivityType(a1.getType());
		db.insertActivity(a1);
		
		return a1;
	}
	
	private void deleteTestActivity(Activity activity){
		db.deleteActivity(activity.getId());
		db.deleteActivityType(activity.getType());
	}
	
	private Agent createTestAgent(){
		AgentType pType = new AgentType(AgentType.LECTURER);
		Agent a1 = new Agent(pType);
		
		db.insertAgentType(pType.getType());
		db.insertAgent(a1.getId(), pType.getType(), true, "121");
		
		return a1;
	}
	
	private void deleteTestAgent(Agent agent){
		db.deleteAgent(agent.getId());
		db.deleteAgentType(agent.getType().getType());
	}
}
