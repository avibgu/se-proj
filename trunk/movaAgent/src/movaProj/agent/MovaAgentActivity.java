package movaProj.agent;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import application.MovaClient;

public class MovaAgentActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        register();
    }
    
    public void register() {
    	Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
    	intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	intent.putExtra("sender", "movaC2DM@gmail.com");
    	startService(intent);
    }
    
 }