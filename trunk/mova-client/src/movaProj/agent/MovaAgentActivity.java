package movaProj.agent;


import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MovaAgentActivity extends Activity implements OnClickListener{
	
	private ActivityDataSource datasource;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mova);
        Button button1 = (Button)this.findViewById(R.id.button1);
        Button button2 = (Button)this.findViewById(R.id.button2);
        Button button3 = (Button)this.findViewById(R.id.button3);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }
    
    public void register() {
    	Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
    	intent.putExtra("app",PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    	intent.putExtra("sender", "movaC2DM@gmail.com");
    	startService(intent);
    }
    
    public void onClick(View v){
    	 switch(v.getId()){
    	 	case R.id.button1 :
    	 		register();
    	 		break;
    	 	case R.id.button2 :	
    	 		 Intent i1 = new Intent(MovaAgentActivity.this, CreateActivity.class);
    	         startActivity(i1);
    	 		 break;
    	 	case R.id.button3 :	
    	 		datasource = new ActivityDataSource(this);
    			datasource.open();

    			 List<actor.Activity> values = datasource.getAllComments();
    			 actor.Activity item = values.get(0);
	   	 		 Intent i2 = new Intent(MovaAgentActivity.this, MovaActivityDetails.class);
		   	 	 Bundle bundle = new Bundle();
//		   	     bundle.putString("severity", item.getPriority().toString());
		   	     bundle.putString("description", item.getDescription());
		   	     bundle.putString("activityType", item.getType().toString());
		   	     bundle.putString("activityName", item.getName());
	   	         startActivity(i2.putExtras(bundle));
	   	 		 break;
    	 }
    }
    
 }