package movaProj.agent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import client.MovaClient;



public class C2DMReceiver extends BroadcastReceiver
{
	
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
    			sendRegistrationIdToServer("test",registrationId);
    		}
    	}
        
     // Better do this in an asynchronous thread
        public void sendRegistrationIdToServer(String deviceId, String registrationId) {
       		Log.d("C2DM", "Sending registration ID to my application server");
    		new MovaClient().saveRegistrationId(registrationId);
        }

}