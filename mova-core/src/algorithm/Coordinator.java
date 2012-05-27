package algorithm;

import java.util.Vector;

//import c2dm.C2dmController;

import type.MessageType;

import actor.Activity;

public class Coordinator {

	public CSPAlgorithm mAlgorithm;

	public Coordinator() {
		mAlgorithm = new CBJ();
	}

	public void recalculate() {

		notifyAboutStartingTheRecalculate();

		waitForApprovalOrUnapproval();

		Vector<Activity> activities = getActivitiesFromDB();

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : activities)
			variables.add(new Variable(activity));

		try {

			mAlgorithm.solve(variables);
		}

		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mAlgorithm.isSolved()) {

			updateDatabaseWithNewSchecdule(mAlgorithm.getAssignment());
			notifyAboutFinishingTheRecalculate();
		}

		else
			; // TODO: what to do when there is no solution??..
	}

	private void notifyAboutStartingTheRecalculate() {
		// TODO: notify all other Agent that we are starting coordinating,
		// provide coordination-id (and order on the Agents?..) we are first..

//		C2dmController.getInstance().sendMessageToDevice("3", null,
//				getAllAgentsIDsBesidesMe(), MessageType.RECALCULATE_START);

	}

	private Vector<String> getAllAgentsIDsBesidesMe() {
		// TODO Auto-generated method stub
		return null;
	}

	private void waitForApprovalOrUnapproval() {
		// TODO Auto-generated method stub

	}

	private Vector<Activity> getActivitiesFromDB() {
		// TODO Auto-generated method stub
		return null;
	}

	private void updateDatabaseWithNewSchecdule(Vector<Value> pAssignment) {
		// TODO Auto-generated method stub

	}

	private void notifyAboutFinishingTheRecalculate() {
		// TODO Auto-generated method stub

//		C2dmController.getInstance().sendMessageToDevice("3", null,
//				getAllAgentsIDsBesidesMe(), MessageType.RECALCULATE_FINISH);
	}
}
