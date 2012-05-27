package movaProj.agent;

import java.util.Vector;

import client.MovaClient;

//import c2dm.C2dmController;

//import type.MessageType;

import actor.Activity;
import algorithm.CBJ;
import algorithm.CSPAlgorithm;
import algorithm.Value;
import algorithm.Variable;

public class Coordinator {

	public static final MovaClient sfMovaClient = new MovaClient();
	
	private Coordinator() {
	}

	public static void recalculate(String myID) {

		notifyAboutStartingTheRecalculate(myID);

		waitForApprovalOrUnapproval();

		Vector<Activity> activities = getActivitiesFromDB(myID);

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : activities)
			variables.add(new Variable(activity));

		CSPAlgorithm mAlgorithm = new CBJ(variables);
		
		try {

			mAlgorithm.solve();
			
			if (mAlgorithm.isSolved()) {

				updateDatabaseWithNewSchecdule(mAlgorithm.getAssignment());
				notifyAboutFinishingTheRecalculate(myID);
			}

			else
				; // TODO: what to do when there is no solution??..
		}

		catch (Exception e) {
			// TODO: what to do when the algorithm fails??..
			e.printStackTrace();
		}
	}

	private static void notifyAboutStartingTheRecalculate(String myID) {
		// TODO: notify all other Agent that we are starting coordinating,
		// provide coordination-id (and order on the Agents?..) we are first..
	}

	private static void waitForApprovalOrUnapproval() {
		// TODO Auto-generated method stub

	}

	private static Vector<Activity> getActivitiesFromDB(String myID) {
		
		sfMovaClient.getAllActivities(myID);
		
		//TODO
		
		return null;
	}

	private static void updateDatabaseWithNewSchecdule(Vector<Value> pAssignment) {
		// TODO Auto-generated method stub

	}

	private static void notifyAboutFinishingTheRecalculate(String myID) {
		// TODO Auto-generated method stub
	}
}
