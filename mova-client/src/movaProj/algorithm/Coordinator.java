package movaProj.algorithm;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import state.ActivityState;
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
	private Boolean recalculateApprovement;

	public Coordinator() {

		mMovaClient = new MovaClient();
		C2DMReceiver.addListener(this);
	}

	public boolean recalculate(String myID) {

		notifyAboutStartingTheRecalculate(myID);

		// TODO: what to do when we shouldn't perform recalculate
		if (!recalculateApprovement)
			return false;					
		
		List<Activity> activities = getActivitiesFromDB(myID);

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : activities)
			if (activity.getState() == ActivityState.PENDING)
				variables.add(new Variable(activity, myID, activities));

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
		
		return true;
	}

	private void notifyAboutStartingTheRecalculate(String myID) {
		// notify all other Agent that we are starting coordinating,
		// (provide coordination-id and order on the Agents?.. we are first..)

		recalculateApprovement = null;

		mMovaClient.startRecalculate(myID);

		synchronized (this) {

			while (null == recalculateApprovement) {

				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private List<Activity> getActivitiesFromDB(String myID) {

		mActivities = null;

		mMovaClient.getAllActivities(myID);

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

	private void updateDatabaseWithNewSchecdule(String myID,
			Vector<Value> pAssignment) {

		Vector<Activity> schedule = new Vector<Activity>();

		for (Value value : pAssignment)
			schedule.add(value.getActivity());

		mMovaClient.sendSchedule(schedule, myID);
	}

	private void notifyAboutFinishingTheRecalculate(String myID) {
		mMovaClient.finishRecalculate(myID);
	}

	@Override
	public void update(Observable pObservable, Object pData) {

		if (pData instanceof MovaMessage) {

			MovaMessage message = (MovaMessage) pData;

			switch (message.getMessageType()) {

			case GOT_ACTIVITIES:

				synchronized (this) {

					mActivities = new MovaJson()
							.jsonToActivities((String) message.getData());
					this.notifyAll();
				}

				break;

			case RECALCULATE_APPROVEMENT:

				synchronized (this) {

					recalculateApprovement = new MovaJson()
							.jsonToBoolean((String) message.getData());
					this.notifyAll();
				}

				break;
			}
		}
	}
}
