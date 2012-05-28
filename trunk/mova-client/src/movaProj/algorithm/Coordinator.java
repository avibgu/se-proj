package movaProj.algorithm;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import utilities.MovaJson;

import movaProj.agent.C2DMReceiver;
import movaProj.agent.MovaMessage;

import client.MovaClient;

//import c2dm.C2dmController;

//import type.MessageType;

import actor.Activity;

public class Coordinator implements Observer {

	private MovaClient mMovaClient;
	private List<Activity> mActivities;

	public Coordinator() {

		mMovaClient = new MovaClient();
		C2DMReceiver.addListener(this);
	}

	public void recalculate(String myID) {

		notifyAboutStartingTheRecalculate(myID);

		waitForApprovalOrUnapproval();

		List<Activity> activities = getActivitiesFromDB(myID);

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : activities)
			variables.add(new Variable(activity, myID));

		CSPAlgorithm mAlgorithm = new CBJ(variables);

		try {

			mAlgorithm.solve();

			if (mAlgorithm.isSolved()) {

				updateDatabaseWithNewSchecdule(myID, mAlgorithm.getAssignment());
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

	private void notifyAboutStartingTheRecalculate(String myID) {
		// TODO: notify all other Agent that we are starting coordinating,
		// provide coordination-id (and order on the Agents?..) we are first..
	}

	private void waitForApprovalOrUnapproval() {
		// TODO Auto-generated method stub

	}

	private List<Activity> getActivitiesFromDB(String myID) {

		mActivities = null;

		mMovaClient.getAllActivities(myID);

		// TODO

		synchronized (this) {

			while (null == mActivities) {
				
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return mActivities;
	}

	private void updateDatabaseWithNewSchecdule(String myID, Vector<Value> pAssignment) {
		// TODO Auto-generated method stub

		Vector<Activity> schedule = new Vector<Activity>();
		
		for (Value value : pAssignment)
			schedule.add(value.getActivity());
		
//		mMovaClient.putNewSchedule(myID, schedule);	TODO
		
		/**
		 * TODO Question
		 * how do we know to distinct between Activity and instance of Activity?..
		 * do we use Activity and Activity Type?........
		 */
	}

	private void notifyAboutFinishingTheRecalculate(String myID) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(Observable pObservable, Object pData) {

		if (pData instanceof MovaMessage) {

			MovaMessage message = (MovaMessage) pData;

			switch (message.getMessageType()) {

			case GOT_ACTIVITIES:

				synchronized (this) {

					mActivities = new MovaJson().jsonToActivities((String)message.getData());
					this.notifyAll();
				}
				
				break;
			}

		}
	}
}
