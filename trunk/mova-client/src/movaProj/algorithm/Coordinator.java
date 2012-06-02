package movaProj.algorithm;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import movaProj.agent.C2DMReceiver;
import movaProj.agent.ItemDataSource;
import movaProj.agent.MovaMessage;
import state.ActivityState;
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
		mMovaClient.startRecalculate(myID);
		mMyID = myID;
	}

	private void recalculate() {
		
		mItems = new ItemDataSource(mActivity).getItems();
		
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
			String messageData = (String) (message.getData());
					
 			switch (message.getMessageType()) {
			
			case GOT_ACTIVITIES:
				
				mActivities = new MovaJson().jsonToActivities(messageData);
				
				mGotActivities = true;
				 
				if (mGotAgents){
					recalculate();
				}

				break;
						
			case GOT_AGENTS:
				
		   		mAgents = new MovaJson().jsonToAgents(messageData);
				
		   		mGotAgents = true;
		   		
				if (mGotActivities){
					recalculate();
				}
				break;
			}
		}
	}
}
