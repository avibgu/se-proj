package movaProj.algorithm;

import java.util.List;
import java.util.Vector;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import movaProj.agent.ItemDataSource;
import movaProj.agent.R;
import state.ActivityState;
import utilities.MovaJson;
import actor.Activity;
import actor.Agent;
import actor.Item;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
	 * This method should be called when an Agent want to initiate Activities
	 * recalculation (rescheduling).
	 * 
	 * @param myID
	 *            The ID of the Agent who asks to recalculate
	 */
	public void askRecalculate(String myID) {

		mMyID = myID;

		String activitiesAndAgents = mMovaClient.startRecalculate(myID);

		if (null == activitiesAndAgents || activitiesAndAgents.equals("")) {
			notifyAgent("Other Agent perform rescheduling right now, try again later if necessary");
			return;
		}

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
	 * private method to initiate the scheduling Algorithm. it initializes all
	 * the required objects for the algorithm, runs the algorithm itself and
	 * publish the result (if there is any).
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
							variables.add(new Variable(activity, mActivities,
									mAgents, mItems));

					CSPAlgorithm mAlgorithm = new CBJ(variables);

					mAlgorithm.solve();

					if (mAlgorithm.isSolved()) {

						updateDatabaseWithNewSchecdule(mMyID,
								mAlgorithm.getAssignment());
					}

					else
						notifyAgent("There is no legal Scheduling to your Activities");
				}

				catch (Exception e) {
					notifyAgent("The Scheduling Algorithm has failed");
					e.printStackTrace();
				}
				
				finally {
					mMovaClient.finishRecalculate(mMyID);
				}
			}
		}).start();
	}

	/*
	 * this method called when the recalculation ended successfully and the
	 * Agent want to update the new schedule in the Server DB.
	 */
	private void updateDatabaseWithNewSchecdule(String myID,
			Vector<Value> pAssignment) {

		Vector<Activity> schedule = new Vector<Activity>();

		for (Value value : pAssignment)
			schedule.add(value.getActivity());

		mMovaClient.sendSchedule(schedule, myID);
	}

	/*
	 * this method generates status-notification for the Agent's user
	 */
	private void notifyAgent(String pMessage) {

		// Get a reference to the NotificationManager:
		NotificationManager mNotificationManager = (NotificationManager) mAndroidActivity
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Instantiate the Notification:
		Notification notification = new Notification(
				android.R.drawable.stat_notify_chat, pMessage,
				System.currentTimeMillis());

		// Define the notification's message and PendingIntent:
		notification.setLatestEventInfo(mAndroidActivity,
				(String) mAndroidActivity.getText(R.string.app_name), pMessage,
				PendingIntent.getActivity(mAndroidActivity, 0, new Intent(
						mAndroidActivity, Coordinator.class), 0));

		// Pass the Notification to the NotificationManager:
		mNotificationManager.notify(1, notification);
	}
}
