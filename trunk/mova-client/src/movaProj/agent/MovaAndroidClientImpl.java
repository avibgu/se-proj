package movaProj.agent;

import java.util.List;
import java.util.Observer;

import movaProj.algorithm.Coordinator;

import state.ActivityState;
import utilities.ConfigurationManager;
import utilities.Location;
import actor.Agent;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import client.MovaClient;

public class MovaAndroidClientImpl implements MovaAndroidClient{
		
	public static final String PROJECT_ID = ConfigurationManager.getProjectID();
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#findItemLocation(java.lang.String, android.content.Context)
	 */
	@Override
	public Location findItemLocation(String itemId,Context context){
			return (new ItemDataSource(context).getItemLocation(itemId));
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#addListener(java.util.Observer)
	 */
	@Override
	public void addListener(Observer observer){
		C2DMReceiver.addListener(observer);
	}
	
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#getSchedule(android.content.Context)
	 */
	@Override
	public List<actor.Activity> getSchedule(Context context){
		return (new ActivityDataSource(context).getSchedule());
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#completeActivity(android.content.Context, java.lang.String)
	 */
	@Override
	public void completeActivity(Context context, String activityId){
		new ActivityDataSource(context).completeActivity(activityId); // Remove from agent database
		new MovaClient().changeActivityStatus(activityId, ActivityState.COMPLETED); // Send to the server.
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#postponeActivity(android.app.Activity, java.lang.String, long)
	 */
	@Override
	public  void postponeActivity(Activity activity, String activityId, long addedTime){
		new MovaClient().postponeActivity(activityId,addedTime);
		recalculate(activity);
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#startActivity(java.lang.String)
	 */
	@Override
	public void startActivity(String activityId){
		new MovaClient().changeActivityStatus(activityId, ActivityState.IN_PROGRESS);// Send to the server.
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#register(android.content.Context)
	 */
	@Override
	public void register(Context context){
		// Ask registration Id from Google.
		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
		intent.putExtra("app",
				PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		intent.putExtra("sender", PROJECT_ID);
		context.startService(intent);
		
		//MovaAndroidClient.recalculate(activity);
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#registerAgent(android.content.Context, java.lang.String, java.lang.String)
	 */
	@Override
	public void registerAgent(Context context, String RegistrationId, String AgentType){
		Agent newAgent = new MovaClient().registerAgent(RegistrationId, AgentType);
		new AgentDataSource(context).insertAgent(newAgent);
		new MovaClient().getItems(newAgent.getId());
	}
	
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#recalculate(android.app.Activity)
	 */
	@Override
	public void recalculate(Activity activity){
		new Coordinator(activity).askRecalculate(new AgentDataSource(activity).getAgentId());
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#changeAgentStatus(java.lang.String, boolean, android.app.Activity)
	 */
	@Override
	public void changeAgentStatus(String agentId, boolean isLogin, Activity activity) {
		new MovaClient().changeAgentStatus(agentId, isLogin);
		recalculate(activity);
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#getAllActivities(java.lang.String)
	 */
	@Override
	public List<actor.Activity> getAllActivities(String agentId) {
		return new MovaClient().getAllActivities(agentId);
	}
	
	/* (non-Javadoc)
	 * @see movaProj.agent.MovaAndroidClientI#createNewActivity(android.app.Activity, actor.Activity)
	 */
	@Override
	public void createNewActivity(Activity activity, actor.Activity newActivity) {
		new MovaClient().sendActivity(newActivity);
		recalculate(activity);
	}
}
