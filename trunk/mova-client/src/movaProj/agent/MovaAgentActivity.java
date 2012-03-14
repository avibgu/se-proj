package movaProj.agent;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MovaAgentActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mova);
        Button button = (Button)this.findViewById(R.id.button1);
        button.setOnClickListener(this);
    }
    
    public void register() {
    	Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
    	intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	intent.putExtra("sender", "movaC2DM@gmail.com");
    	startService(intent);
    }
    
    public void onClick(View v){
    	register();
    }
    
 }