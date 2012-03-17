package movaProj.agent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import actor.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import client.MovaClient;
import utilities.MovaJson;



public class C2DMReceiver extends BroadcastReceiver
{
		private ActivityDataSource datasource;
	
	    private static final String TAG = "C2DMReciever";
        public C2DMReceiver()
        {
        	
           	super();
                
        }
        @Override
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		Log.w("C2DM", "Registration Receiver called");
    		if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
    			Log.w("C2DM", "Received registration ID");
    			final String registrationId = intent
    					.getStringExtra("registration_id");
    			String error = intent.getStringExtra("error");

    			Log.d("C2DM", "dmControl: registrationId = " + registrationId
    					+ ", error = " + error);
    			sendRegistrationIdToServer("test",registrationId,context);
    		}
    		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
    			
    			handleMessage(context,intent);
    		}
    	}
        
        public void handleMessage(Context context, Intent intent){
       	Log.d("CD2M","GOT MESSAGE");
        	 Bundle extras = intent.getExtras();
             if (extras != null) {
                     //parse the message and do something with it.
                     //For example, if the server sent the payload as "data.message=xxx", here you would have an extra called "message"
                     String message = extras.getString("message");
                     
                     Log.d("CD2M", "received message: " + message);
                     
                    JsonParser jp = new JsonParser();
             		JsonObject j = (JsonObject) jp.parse(message);
             	
                     Activity activity = new MovaJson().jsonToActivity(j.get("activity").getAsString());
                     datasource = new ActivityDataSource(context);
             		 datasource.open();
                     datasource.createActivity(activity);
                     
                     String app_name = (String)context.getText(R.string.app_name);
                                
                     // Use the Notification manager to send notification
                     NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                     // Create a notification using android stat_notify_chat icon. 
                 	Notification notification = new Notification(android.R.drawable.stat_notify_chat, app_name + ": " + "New Activity", 0);
            
                 	// Create a pending intent to call the HomeActivity when the notification is clicked
                 	PendingIntent pendingIntent = PendingIntent.getActivity(context, -1, new Intent(context, MovaAgentActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); // 
                 	notification.when = System.currentTimeMillis();
                 	notification.flags |= Notification.FLAG_AUTO_CANCEL; 
                 	// Set the notification and register the pending intent to it
                 	notification.setLatestEventInfo(context, app_name, activity.getName(), pendingIntent); //
            
                 	// Trigger the notification
                 	notificationManager.notify(0, notification);
                 	
                 	// Add the message to the activities list
                 	
             	}
  
        }
        
        public void saveActivity(Activity item){
        	String[] comments = new String[] { item.getId().toString(),item.getName(),item.getType().toString(),
        			"DESCRIPTION",item.getPriority().toString()};
			// Save the new activity to the database
			datasource.createActivity(item);
	   }
        
     // Better do this in an asynchronous thread
        public void sendRegistrationIdToServer(String deviceId, String registrationId,Context context) {
       		Log.d("C2DM", "Sending registration ID to my application server");
       		 
    		new MovaClient().saveRegistrationId(registrationId,Secure.getString(context.getContentResolver(),Secure.ANDROID_ID) );
        }
  }