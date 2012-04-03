package main;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import actor.Activity;
import actor.Manager;


public class MainTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {

		Manager manager = new Manager();
		
		manager.setAgentTypes(null);
		manager.setItemTypes(null);
		
		manager.setActivityTypes(null);
		
		Set<Activity> activitiesInstances = new HashSet<Activity>();
		
		// TODO: create dynamic activities..
		
		manager.addActivitiesInstances(activitiesInstances);
		
		manager.startApplication();
	}
}
