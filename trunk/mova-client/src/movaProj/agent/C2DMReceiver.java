package movaProj.agent;

import java.util.List;

import type.AgentType;
import type.MessageType;
import utilities.MovaJson;

import actor.Agent;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import client.MovaClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class C2DMReceiver extends BroadcastReceiver
{
		private ActivityDataSource datasource;
		private static final int REGISTRATION_DIALOG = 0;
		
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

    			Log.d("C2DM", "c2dmControl: registrationId = " + registrationId
    					+ ", error = " + error);
    			sendRegistrationIdToServer("test",registrationId,context);
    			//start activity
//    	        Intent i = new Intent();
//    	        i.setClassName("movaProj.agent", "movaProj.agent.RegistrationDialogActivity");
//    	       	Bundle bundle = new Bundle();
//		   	    bundle.putString("registration_id", registrationId);
//		        context.startActivity(i.putExtras(bundle));
    		}
    		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
    			handleMessage(context,intent);
    		}
    	}
        
        public void handleMessage(Context context, Intent intent){
	       	Log.d("CD2M","GOT MESSAGE");
			Bundle extras = intent.getExtras();
		    if (extras != null) {
	                     
			 	 String message = extras.getString("message");
		         Log.d("CD2M", "received message: " + message);
		         MessageType messageType = MessageType.valueOf(extras.getString("messageType"));
		         
		         switch (messageType){
		         	case DELETE_ITEM:
		         		deleteItem(context, intent, message);
		         		break;
		         	case DISTRIBUTE_ITEM_LOCATION:
		         		distributeItemLocation(context, intent, message);
		         		break;
		         	case DISTRIBUTE_ITEM_STATE:
		         		distributeItemState(context, intent, message);
		         		break;
		         	case SEND_SCHEDULE:
		         		sendSchedule(context, intent, message);
		         		break;
		         }
	       	}
        }
                     
 		private void distributeItemState(Context context, Intent intent,
				String message) {
			// TODO Auto-generated method stub
			
		}

		private void distributeItemLocation(Context context, Intent intent,
				String message) {
			// TODO Auto-generated method stub
			
		}

		private void deleteItem(Context context, Intent intent, String message) {
			// TODO Auto-generated method stub
			
		}

		private void sendSchedule(Context context, Intent intent, String message) {
			JsonParser jp = new JsonParser();
      		String j = jp.parse(message).getAsString();
      	
            List<actor.Activity> schedule = new MovaJson().jsonToActivities(j);
            datasource = new ActivityDataSource(context);
      		datasource.open();
            datasource.createSchedule(schedule);
		}

		public void sendActivity(Context context, Intent intent,String message){
			JsonParser jp = new JsonParser();
      		JsonObject j = (JsonObject) jp.parse(message);
      	
      		actor.Activity activity = new MovaJson().jsonToActivity(j.get("activity").getAsString());
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

		// Better do this in an asynchronous thread
        public void sendRegistrationIdToServer(String deviceId, String registrationId,Context context) {
       		Log.d("C2DM", "Sending registration ID to my application server");
       		// Create Agent and send to server
       		      		
       		Agent agent = new Agent(new AgentType(AgentType.COORDINATOR));
       		agent.setRegistrationId(registrationId);
       		new MovaClient().registerAgent(agent);
        }
  }