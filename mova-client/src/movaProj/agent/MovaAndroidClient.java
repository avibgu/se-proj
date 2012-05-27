package movaProj.agent;

import java.util.List;
import java.util.Observer;

import state.ActivityState;
import utilities.Location;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import client.MovaClient;

public class MovaAndroidClient {
		
	
	public static Location findItemLocation(String itemId,Activity activity){
			return (new ItemDataSource(activity).getItemLocation(itemId));
	}
	
	public static void addListener(Observer observer){
		C2DMReceiver.addListener(observer);
	}
	
	public static List<actor.Activity> getSchedule(Activity activity){
		return (new ActivityDataSource(activity).getSchedule());
	}
	
	public static void completeActivity(Activity activity, String activityId){
		new ActivityDataSource(activity).completeActivity(activityId); // Remove from agent database
		new MovaClient().changeActivityStatus(activityId, ActivityState.COMPLETED);// Send to the server.
	}
	
	public static void register(Activity activity){
		// Ask registration Id from Google.
				Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
				intent.putExtra("app",
						PendingIntent.getBroadcast(activity, 0, new Intent(), 0));
				intent.putExtra("sender", "movaC2DM@gmail.com");
				activity.startService(intent);
	}
}