package movaProj.algorithm;

import java.util.List;
import java.util.Vector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import movaProj.agent.ItemDataSource;
import state.ActivityState;
import utilities.MovaJson;
import actor.Activity;
import actor.Agent;
import actor.Item;
import android.content.Context;
import client.MovaClient;

public class Coordinator {

	private String mMyID;
	private MovaClient mMovaClient;
	private List<Activity> mActivities;
	private List<Agent> mAgents;
	private List<Item> mItems;
	private Context mAndroidActivity;

	public Coordinator(Context activity) {
		mAndroidActivity = activity;
		mMovaClient = new MovaClient();
	}

	/**
	 * This method should be called when an Agent want to initiate
	 * Activities recalculation (rescheduling). 
	 * @param myID The ID of the Agent who asks to recalculate
	 */
	public void askRecalculate(String myID) {
		mMyID = myID;
		String activitiesAndAgents = mMovaClient.startRecalculate(myID);
		initActivitiesAndAgents(activitiesAndAgents);
		recalculate();
	}

	/*
	 * initializing the data structures which hold all the Activities and Agents
	 * that we have in our System
	 */
	private void initActivitiesAndAgents(String pActivitiesAndAgents) {
		
		JsonParser jp = new JsonParser();
		JsonObject j = (JsonObject) jp.parse(pActivitiesAndAgents);

		MovaJson mj = new MovaJson();
		
		mActivities = mj.jsonToActivities(j.get("activities").getAsString());
		mAgents = mj.jsonToAgents(j.get("agents").getAsString());
	}

	/*
	 * private method to initiate the scheduling Algorithm.
	 * it initializes all the required objects for the algorithm,
	 * runs the algorithm itself and publish the result (if there is any).
	 */
	private void recalculate() {

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					
					mItems = new ItemDataSource(mAndroidActivity).getItems();

					Vector<Variable> variables = new Vector<Variable>();

					for (Activity activity : mActivities)
						if (activity.getState() == ActivityState.PENDING)
							variables.add(new Variable(activity,
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

	/*
	 * this method called when the recalculation ended successfully
	 * and the Agent want to update the new schedule in the Server DB.
	 */
	private void updateDatabaseWithNewSchecdule(String myID,
			Vector<Value> pAssignment) {

		Vector<Activity> schedule = new Vector<Activity>();

		for (Value value : pAssignment)
			schedule.add(value.getActivity());

		mMovaClient.sendSchedule(schedule, myID);
	}
}
