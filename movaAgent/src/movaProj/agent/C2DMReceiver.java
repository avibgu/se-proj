package movaProj.agent;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



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
       	
        	try {
        		Log.d("C2DM", "Sending registration ID to my application server");
           	 	HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://192.168.123.5:8080/CommunicationServer/agents/saveRegistrationId"));
				
                HttpResponse response = client.execute(request);
                System.out.println("AGENT SENT");
        	}catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
        		e.printStackTrace();
        	}
        }

}