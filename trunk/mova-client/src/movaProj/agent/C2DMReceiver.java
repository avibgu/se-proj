package movaProj.agent;

import java.util.List;
import java.util.Observer;
import java.util.Vector;

import movaProj.sampleApplication.InsertAgentTypeActivity;
import type.MessageType;
import utilities.MovaJson;
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
		private static Vector<Observer> internalMessageListeners = new Vector<Observer>();
		private MovaClient movaClient = new MovaClient(); 
		private MovaJson movaJson = new MovaJson();
		
	    private static final String TAG = "C2DMReciever";
	    String agent_id="";
	    
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
    			
    			new MovaClient().sendRegistrationId(registrationId);
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
		         	case GOT_SCHEDULE:
		         		gotSchedule(context, intent, message);
		         		break;
		         	case GOT_ACTIVITIES:
		         		//TODO Avi..
		         		break;
		         	case REGISTER_SUCCESS:
		         		notifyObservers(new MovaMessage(MessageType.REGISTER_SUCCESS, agent_id));
		         		break;
		         	case REGISTER_FAILED:
		         		notifyObservers(new MovaMessage(MessageType.REGISTER_FAILED, agent_id));
		         		break;
		         	case RECALCULATE_START:
		         		//TODO Avi..
		         		notifyObservers(new MovaMessage(MessageType.RECALCULATE_START, null));
		         		break;
		         	case RECALCULATE_FINISH:
		         		//TODO Avi..
		         		getNewSchedule(context);
		         		break;
		         	case STATIC_TYPES:
		         		insertStaticTypes(message,context);
		         		break;
		         	default:
		         		System.out.println("Unsupported message");
		         }
	       	}
        }
                     
 		private void insertStaticTypes(String message, Context context) {
 			JsonParser jp = new JsonParser();
      		JsonObject j = (JsonObject)jp.parse(message);
      		String registrationId = j.get("registrationId").getAsString();
      		String activityTypes = j.get("activityTypes").getAsString();
      		String agentTypes = j.get("agentTypes").getAsString();
      		String itemTypes = j.get("itemTypes").getAsString();
			new ActivityDataSource(context).insertActivityTypes(movaJson.jsonToVectorTypes(activityTypes));
			new AgentDataSource(context).insertAgentTypes(movaJson.jsonToVectorTypes(agentTypes));
			new ItemDataSource(context).insertItemTypes(movaJson.jsonToVectorTypes(itemTypes));
			Intent i = new Intent();
			i.setClass(context, InsertAgentTypeActivity.class);
			i.putExtra("registrationId", registrationId);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}

		private void getNewSchedule(Context context) {
			movaClient.getSchedule(new AgentDataSource(context).getAgentId());
		}

		private void distributeItemState(Context context, Intent intent,
				String message) {
 			JsonParser jp = new JsonParser();
      		JsonObject j = (JsonObject)jp.parse(message);
      		String itemId = j.get("itemId").getAsString();
      		String newState = j.get("newStatus").getAsString();
      		
      		new ItemDataSource(context).changeItemState(itemId, newState);
      		      		
            notifyObservers(new MovaMessage(MessageType.DISTRIBUTE_ITEM_STATE, itemId)); 
		}

		private void distributeItemLocation(Context context, Intent intent,
				String message) {
			// TODO Auto-generated method stub
			
		}

		private void deleteItem(Context context, Intent intent, String message) {
			JsonParser jp = new JsonParser();
      		String itemId = jp.parse(message).getAsString();
      	
      		new ItemDataSource(context).deleteItem(itemId);
      		
            notifyObservers(new MovaMessage(MessageType.DELETE_ITEM, itemId)); 
		}

		private void gotSchedule(Context context, Intent intent, String message) {
			JsonParser jp = new JsonParser();
      		String j = jp.parse(message).getAsString();
      	
            List<actor.Activity> schedule = new MovaJson().jsonToActivities(j);
            new ActivityDataSource(context).createSchedule(schedule);
            notifyObservers(new MovaMessage(MessageType.GOT_SCHEDULE, null)); 
		}

		public void sendActivity(Context context, Intent intent,String message){
			JsonParser jp = new JsonParser();
      		JsonObject j = (JsonObject) jp.parse(message);
      	
      		actor.Activity activity = new MovaJson().jsonToActivity(j.get("activity").getAsString());
            new ActivityDataSource(context).createActivity(activity);
        }

		// Better do this in an asynchronous thread
        public void sendRegistrationIdToServer(String registrationId,String agentType,Context context) {
       		Log.d("C2DM", "Sending registration ID to my application server");
       		new MovaClient().registerAgent(registrationId, agentType);
        }
  }