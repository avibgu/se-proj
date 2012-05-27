package algorithm;

import java.util.Vector;

//import c2dm.C2dmController;

//import type.MessageType;

import actor.Activity;

public class Coordinator {

	private Coordinator() {
	}

	public static void recalculate(String myID) {

		notifyAboutStartingTheRecalculate(myID);

		waitForApprovalOrUnapproval();

		Vector<Activity> activities = getActivitiesFromDB();

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

//		C2dmController.getInstance().sendMessageToDevice("3", null,
//				getAllAgentsIDsBesidesMe(myID), MessageType.RECALCULATE_START);

	}

	private Vector<String> getAllAgentsIDsBesidesMe(String myID) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void waitForApprovalOrUnapproval() {
		// TODO Auto-generated method stub

	}

	private static Vector<Activity> getActivitiesFromDB() {
		// TODO Auto-generated method stub
		return null;
	}

	private static void updateDatabaseWithNewSchecdule(Vector<Value> pAssignment) {
		// TODO Auto-generated method stub

	}

	private static void notifyAboutFinishingTheRecalculate(String myID) {
		// TODO Auto-generated method stub

//		C2dmController.getInstance().sendMessageToDevice("3", null,
//				getAllAgentsIDsBesidesMe(myID), MessageType.RECALCULATE_FINISH);
	}
}
