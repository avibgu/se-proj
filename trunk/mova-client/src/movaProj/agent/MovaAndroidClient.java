package movaProj.agent;

import java.util.List;
import java.util.Observer;

import movaProj.algorithm.Coordinator;

import state.ActivityState;
import utilities.Location;
import actor.Agent;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import client.MovaClient;

public class MovaAndroidClient{
		
	/**
	 * Returns the location of the given item.
	 * @param itemId The id of the requested item.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @return The location of the requested item.
	 */
	public static Location findItemLocation(String itemId,Context context){
			return (new ItemDataSource(context).getItemLocation(itemId));
	}
	
	/**
	 * Add listener to the application.
	 * @param observer the observer that will get the events messages.
	 */
	public static void addListener(Observer observer){
		C2DMReceiver.addListener(observer);
	}
	
	
	/**
	 * Returns the current agent schedule.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @return List of the current agent activities.
	 */
	public static List<actor.Activity> getSchedule(Context context){
		return (new ActivityDataSource(context).getSchedule());
	}
	
	/**
	 * Mark the activity as completed.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @param activityId The id of the completed activity.
	 */
	public static void completeActivity(Context context, String activityId){
		new ActivityDataSource(context).completeActivity(activityId); // Remove from agent database
		new MovaClient().changeActivityStatus(activityId, ActivityState.COMPLETED); // Send to the server.
	}
	
	/**
	 * Postpone the activity according to the given added time. 
	 * Cause to recalculation of the event schedule.
	 * @param activity The android activity that the agent arrived from.
	 * @param activityId The id of the postponed activity.
	 * @param addedTime The time that the agent want to add to the activity in minutes.
	 */
	public static void postponeActivity(Activity activity, String activityId, long addedTime){
		new MovaClient().postponeActivity(activityId,addedTime);
		recalculate(activity);
	}
	
	/**
	 * Mark that the activity was started.
	 * @param activityId The id of the activity.
	 */
	public static void startActivity(String activityId){
		new MovaClient().changeActivityStatus(activityId, ActivityState.IN_PROGRESS);// Send to the server.
	}
	
	/**
	 * Register the agent to Google C2dm service by asking registrationId.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 */
	public static void register(Context context){
		// Ask registration Id from Google.
		Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
		intent.putExtra("app",
				PendingIntent.getBroadcast(context, 0, new Intent(), 0));
		intent.putExtra("sender", "movaC2DM@gmail.com");
		context.startService(intent);
		
		//MovaAndroidClient.recalculate(activity);
	}
	
	/**
	 * Register the agent to the communication server and add him to the server database and
	 * to the client database.
	 * In addition, asking for the list of items in the communication server in order to keep the
	 * items list. 
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @param RegistrationId C2dm registrationID of the agent.
	 * @param AgentType The new agent type.
	 */
	public static void registerAgent(Context context, String RegistrationId, String AgentType){
		Agent newAgent = new MovaClient().registerAgent(RegistrationId, AgentType);
		new AgentDataSource(context).insertAgent(newAgent);
		new MovaClient().getItems(newAgent.getId());
	}
	
	
	/**
	 * Recalculate all the activities in the event and send new schedule to every agent.
	 * @param activity The android activity that the agent arrived from.
	 */
	public static void recalculate(Activity activity){
		new Coordinator(activity).askRecalculate(new AgentDataSource(activity).getAgentId());
	}
	
	/**
	 * Change the status of the agent. The agent can be login or logout.
	 * Cause to recalculation of the event schedule.
	 * @param agentId The id of the agent that the agent wants to change its status.
	 * @param isLogin The new status of the agent. True if the agent is login now and false otherwise.
	 * @param activity The android activity that the agent arrived from.
	 */
	public static void changeAgentStatus(String agentId, boolean isLogin, Activity activity) {
		new MovaClient().changeAgentStatus(agentId, isLogin);
		recalculate(activity);
	}
	
	/**
	 * Returns all the event activities.
	 * @param agentId The id of the agent that request the activities.
	 * @return list of all the activities in the event.
	 */
	public static List<actor.Activity> getAllActivities(String agentId) {
		return new MovaClient().getAllActivities(agentId);
	}
	
	public static void createNewActivity(Activity activity, actor.Activity newActivity) {
		new MovaClient().sendActivity(newActivity);
		recalculate(activity);
	}
}
