package movaProj.agent;

import java.util.List;
import java.util.Observer;
import java.util.Vector;

import movaProj.sampleApplication.MovaAgentActivity;
import type.AgentType;
import type.MessageType;
import utilities.MovaJson;
import actor.Agent;
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
		private ActivityDataSource activityDatasource;
		private ItemDataSource itemDataSource;
		private static Vector<Observer> internalMessageListeners = new Vector<Observer>();
		
	    private static final String TAG = "C2DMReciever";
	    
	    
        public C2DMReceiver()
        {
           	super();
        }
  
        public static void addListener(Observer observer){
        	internalMessageListeners.add(observer);
        }
        
        public void notifyObservers(MovaMessage msg){
        	for (Observer observer : internalMessageListeners){
        		observer.update(null, msg);
        	}
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
    			
    			String app_name = (String)context.getText(R.string.app_name);
    			// Use the Notification manager to send notification
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // Create a notification using android stat_notify_chat icon. 
                Notification notification = new Notification(android.R.drawable.stat_notify_chat, app_name + ": " + "Got Registration Id", 0);
     
                // Create a pending intent to call the HomeActivity when the notification is clicked
                PendingIntent pendingIntent = PendingIntent.getActivity(context, -1, new Intent(context, MovaAgentActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); // 
                notification.when = System.currentTimeMillis();
                notification.flags |= Notification.FLAG_AUTO_CANCEL; 
                // Set the notification and register the pending intent to it
                notification.setLatestEventInfo(context, app_name, registrationId, pendingIntent); //
     
                // Trigger the notification
                notificationManager.notify(0, notification);
    			sendRegistrationIdToServer(registrationId,context);
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
		         	case REGISTER_SUCCESS:
		         		notifyObservers(new MovaMessage(MessageType.REGISTER_SUCCESS, null));
		         		break;
		         	case REGISTER_FAILED:
		         		notifyObservers(new MovaMessage(MessageType.REGISTER_FAILED, null));
		         		break;
		         	case RECALCULATE_START:
		         		notifyObservers(new MovaMessage(MessageType.RECALCULATE_START, null));
		         		break;
		         	case RECALCULATE_FINISH:
		         		notifyObservers(new MovaMessage(MessageType.RECALCULATE_FINISH, null));
		         		break;
		         	default:
		         		System.out.println("Unsupported message");
		         }
	       	}
        }
                     
 		private void distributeItemState(Context context, Intent intent,
				String message) {
 			JsonParser jp = new JsonParser();
      		JsonObject j = (JsonObject)jp.parse(message);
      		String itemId = j.get("itemId").getAsString();
      		String newState = j.get("newStatus").getAsString();
      		
      		itemDataSource.changeItemState(itemId, newState);
      		      		
            notifyObservers(new MovaMessage(MessageType.DISTRIBUTE_ITEM_STATE, itemId)); 
			
		}

		private void distributeItemLocation(Context context, Intent intent,
				String message) {
			// TODO Auto-generated method stub
			
		}

		private void deleteItem(Context context, Intent intent, String message) {
			JsonParser jp = new JsonParser();
      		String itemId = jp.parse(message).getAsString();
      	
      		itemDataSource.deleteItem(itemId);
      		
            notifyObservers(new MovaMessage(MessageType.DELETE_ITEM, itemId)); 
		}

		private void sendSchedule(Context context, Intent intent, String message) {
			JsonParser jp = new JsonParser();
      		String j = jp.parse(message).getAsString();
      	
            List<actor.Activity> schedule = new MovaJson().jsonToActivities(j);
            activityDatasource = new ActivityDataSource(context);
            activityDatasource.createSchedule(schedule);
            notifyObservers(new MovaMessage(MessageType.SEND_SCHEDULE, null)); 
		}

		public void sendActivity(Context context, Intent intent,String message){
			JsonParser jp = new JsonParser();
      		JsonObject j = (JsonObject) jp.parse(message);
      	
      		actor.Activity activity = new MovaJson().jsonToActivity(j.get("activity").getAsString());
            activityDatasource = new ActivityDataSource(context);
            activityDatasource.createActivity(activity);
        }

		// Better do this in an asynchronous thread
        public void sendRegistrationIdToServer(String registrationId,Context context) {
       		Log.d("C2DM", "Sending registration ID to my application server");
       		// Create Agent and send to server
       		      		
       		Agent agent = new Agent(new AgentType(AgentType.COORDINATOR));
       		agent.setRegistrationId(registrationId);
       		new MovaClient().registerAgent(agent);
        }
  }