package movaProj.algorithm;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import movaProj.agent.C2DMReceiver;
import movaProj.agent.ItemDataSource;
import movaProj.agent.MovaMessage;
import state.ActivityState;
import utilities.Location;
import utilities.MovaJson;
import actor.Activity;
import actor.Agent;
import actor.Item;
import client.MovaClient;

public class Coordinator implements Observer {

	private String mMyID;
	private MovaClient mMovaClient;
	private List<Activity> mActivities;
	private List<Agent> mAgents;
	private List<Item> mItems;
	private boolean mGotActivities;
	private boolean mGotAgents;
	private android.app.Activity mActivity;

	public Coordinator(android.app.Activity activity) {
		mActivity = activity;
		mMovaClient = new MovaClient();
		C2DMReceiver.addListener(this);
	}

	public void askRecalculate(String myID) {
		mGotActivities = false;
		mGotAgents = false;
		mMyID = myID;
		String activitiesAndAgents = mMovaClient.startRecalculate(myID);
		initActivitiesAndAgents(activitiesAndAgents);
		recalculate();
	}

	private void initActivitiesAndAgents(String pActivitiesAndAgents) {
		
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(pActivitiesAndAgents);

		MovaJson mj = new MovaJson();
		
		mActivities = mj.jsonToActivities(j.get("activities").getAsString());
		mAgents = mj.jsonToAgents(j.get("agents").getAsString());
	}

	private void recalculate() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					
					mItems = new ItemDataSource(mActivity).getItems();

					Vector<Variable> variables = new Vector<Variable>();

					for (Activity activity : mActivities)
						if (activity.getState() == ActivityState.PENDING)
							variables.add(new Variable(activity, mMyID,
								mActivities, mAgents, mItems));

					CSPAlgorithm mAlgorithm = new CBJ(variables);

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
		}).start();
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
			String messageData = (String) (message.getData());

			switch (message.getMessageType()) {

			case GOT_ACTIVITIES:

				mActivities = new MovaJson().jsonToActivities(messageData);

				mGotActivities = true;

				if (mGotAgents) {
//					recalculate();
				}

				break;

			case GOT_AGENTS:

				mAgents = new MovaJson().jsonToAgents(messageData);

				mGotAgents = true;

				if (mGotActivities) {
//					recalculate();
				}
				break;
			}
		}
	}
}
