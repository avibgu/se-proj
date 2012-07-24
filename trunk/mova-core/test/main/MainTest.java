package main;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.MovaClient;

import type.AgentType;

import actor.Activity;
import actor.Agent;


public class MainTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

//		Manager manager = new Manager();
//		
//		manager.setAgentTypes(null);
//		manager.setItemTypes(null);
//		
//		manager.setActivityTypes(null);
//		
//		Set<Activity> activitiesInstances = new HashSet<Activity>();
//		
//		// TODO: create dynamic activities..
//		
//		manager.addActivitiesInstances(activitiesInstances);
//		
//		manager.startApplication();
	}
	
	@Test
	public void testCreateAgent(){
		Agent agent = new Agent(new AgentType(AgentType.COORDINATOR));
		agent.setRegistrationId("64638947389430gfdgdfg");
//		new MovaClient().registerAgent(agent);
		//TODO ...
	}
	
	@Test
	public void testAddAgent(){
		Activity activity = new Activity("TestActivity");
		new MovaClient().addActivity(activity);
	}
	

	@Test
	public void testSendSchedule(){
		Vector<Activity> schedule = new Vector<Activity>();
		schedule.add(new Activity("Activity1"));
		schedule.add(new Activity("Activity2"));
		schedule.add(new Activity("Activity3"));
		schedule.add(new Activity("Activity4"));
		new MovaClient().sendSchedule(schedule, "1111");
	}
	
}
