package movaProj.algorithm;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import state.ActivityState;
import utilities.MovaJson;

import movaProj.agent.C2DMReceiver;
import movaProj.agent.MovaMessage;

import client.MovaClient;

import actor.Activity;
import actor.Agent;
import actor.Item;

public class Coordinator implements Observer {

	private String mMyID;
	private MovaClient mMovaClient;
	private List<Activity> mActivities;
	private List<Agent> mAgents;
	private List<Item> mItems;

	public Coordinator() {

		mMovaClient = new MovaClient();
		C2DMReceiver.addListener(this);
	}

	public void askRecalculate(String myID) {

		mMovaClient.startRecalculate(myID);
		mMyID = myID;
	}

	private void recalculate() {

		Vector<Variable> variables = new Vector<Variable>();

		for (Activity activity : mActivities)
			if (activity.getState() == ActivityState.PENDING)
				variables.add(new Variable(activity, mMyID, mActivities,
						mAgents, mItems));

		CSPAlgorithm mAlgorithm = new CBJ(variables);

		try {

			mAlgorithm.solve();

			if (mAlgorithm.isSolved()) {

				updateDatabaseWithNewSchecdule(mMyID,
						mAlgorithm.getAssignment());
				mMovaClient.finishRecalculate(mMyID);
			}

			else
				; // TODO: what to do when there is no solution??..
		}

		catch (Exception e) {
			// TODO: what to do when the algorithm fails??..
			e.printStackTrace();
		}
	}

	private void updateDatabaseWithNewSchecdule(String myID,
			Vector<Value> pAssignment) {

		Vector<Activity> schedule = new Vector<Activity>();

		for (Value value : pAssignment)
			schedule.add(value.getActivity());

		mMovaClient.sendSchedule(schedule, myID);
	}

	@Override
	public void update(Observable pObservable, Object pData) {

		if (pData instanceof MovaMessage) {

			MovaMessage message = (MovaMessage) pData;

			switch (message.getMessageType()) {

			case RECALCULATE_APPROVEMENT:

				JsonParser jp = new JsonParser();
	      		JsonObject j = (JsonObject)jp.parse((String) message.getData());
	      		
	      		String activities = j.get("activities").getAsString();
	      		String agents = j.get("agents").getAsString();
	      		String items = j.get("items").getAsString();

	      		mActivities = new MovaJson().jsonToActivities(activities);
				mAgents = new MovaJson().jsonToAgents(agents);
				mItems = new MovaJson().jsonToItems(items);

				recalculate();

				break;
			}
		}
	}
}
