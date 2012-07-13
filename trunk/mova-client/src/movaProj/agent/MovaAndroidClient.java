package movaProj.agent;

import java.util.List;
import java.util.Observer;

import utilities.Location;
import android.app.Activity;
import android.content.Context;

public interface MovaAndroidClient {

	/**
	 * Returns the location of the given item.
	 * @param itemId The id of the requested item.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @return The location of the requested item.
	 */
	public abstract Location findItemLocation(String itemId, Context context);

	/**
	 * Add listener to the application.
	 * @param observer the observer that will get the events messages.
	 */
	public abstract void addListener(Observer observer);

	/**
	 * Returns the current agent schedule.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @return List of the current agent activities.
	 */
	public abstract List<actor.Activity> getSchedule(Context context);

	/**
	 * Mark the activity as completed.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @param activityId The id of the completed activity.
	 */
	public abstract void completeActivity(Context context, String activityId);

	/**
	 * Postpone the activity according to the given added time. 
	 * Cause to recalculation of the event schedule.
	 * @param activity The android activity that the agent arrived from.
	 * @param activityId The id of the postponed activity.
	 * @param addedTime The time that the agent want to add to the activity in minutes.
	 */
	public abstract void postponeActivity(Activity activity, String activityId,
			long addedTime);

	/**
	 * Mark that the activity was started.
	 * @param activityId The id of the activity.
	 */
	public abstract void startActivity(String activityId);

	/**
	 * Register the agent to Google C2dm service by asking registrationId.
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 */
	public abstract void register(Context context);

	/**
	 * Register the agent to the communication server and add him to the server database and
	 * to the client database.
	 * In addition, asking for the list of items in the communication server in order to keep the
	 * items list. 
	 * @param context The context that the agent arrived from (For example: the activity that call this function)
	 * @param RegistrationId C2dm registrationID of the agent.
	 * @param AgentType The new agent type.
	 */
	public abstract void registerAgent(Context context, String RegistrationId,
			String AgentType);

	/**
	 * Recalculate all the activities in the event and send new schedule to every agent.
	 * @param activity The android activity that the agent arrived from.
	 */
	public abstract void recalculate(Activity activity);

	/**
	 * Change the status of the agent. The agent can be login or logout.
	 * Cause to recalculation of the event schedule.
	 * @param agentId The id of the agent that the agent wants to change its status.
	 * @param isLogin The new status of the agent. True if the agent is login now and false otherwise.
	 * @param activity The android activity that the agent arrived from.
	 */
	public abstract void changeAgentStatus(String agentId, boolean isLogin,
			Activity activity);

	/**
	 * Returns all the event activities.
	 * @param agentId The id of the agent that request the activities.
	 * @return list of all the activities in the event.
	 */
	public abstract List<actor.Activity> getAllActivities(String agentId);

	public abstract void createNewActivity(Activity activity,
			actor.Activity newActivity);

}