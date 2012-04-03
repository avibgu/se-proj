package movaProj.agent;

import java.util.Vector;

import priority.Priority;

import type.ActivityType;
import type.AgentType;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import client.MovaClient;

public class CreateActivity extends Activity implements OnClickListener,OnItemSelectedListener {
	  private Priority severity = Priority.URGENT;
	  private AgentType agentType = AgentType.COORDINATOR;
	  private ActivityType activityType = ActivityType.CONFERENCE_REGISTRATION;
	  EditText activityName;
	  
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.create_activity);
	        Spinner s1 = (Spinner) findViewById(R.id.severitySpinner);
	        ArrayAdapter<String> adapter1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, Priority.values());
	        s1.setAdapter(adapter1);
        	s1.setOnItemSelectedListener(this);
	        
	        Spinner s2 = (Spinner) findViewById(R.id.agentTypeSpinner);
	        ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, AgentType.values());
	        s2.setAdapter(adapter2);
	        s2.setOnItemSelectedListener(this);
	        
	        Spinner s3 = (Spinner) findViewById(R.id.activityTypeSpinner);
	        ArrayAdapter<String> adapter3 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, ActivityType.values());
	        s3.setAdapter(adapter3);
	        s3.setOnItemSelectedListener(this);
	        
	        Button submitActivityButton = (Button)this.findViewById(R.id.submitActivityButton);
	        submitActivityButton.setOnClickListener(this);
	       
	    }
	  
	  
		  public void onClick(View v){
			 activityName   = (EditText)findViewById(R.id.ActivityNameText);
			 actor.Activity newActivity = new actor.Activity(activityName.getText().toString()); 
			 newActivity.setPriority(severity);
			 newActivity.setType(activityType);
			 //newActivity.setAgentType(agentType);
			 newActivity.setDescription(((EditText)findViewById(R.id.descriptionText)).getText().toString());
			 Vector<String> agentIds = new Vector<String>();
			 agentIds.add("dedda213fd5b8a00");
			 new MovaClient().sendActivity(newActivity, agentIds);
			 Intent i = new Intent(CreateActivity.this, MovaAgentActivity.class);
	         startActivity(i);
		  }


		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch(arg0.getId()){
	    	 	case R.id.severitySpinner :
	    	 		this.severity = (Priority)arg0.getItemAtPosition(arg2);
	    	 		break;
	    	 	case R.id.activityTypeSpinner :
	    	 		this.activityType = (ActivityType)arg0.getItemAtPosition(arg2);
	    	 		break;	
	    	 	case R.id.agentTypeSpinner :
	    	 		this.agentType = (AgentType)arg0.getItemAtPosition(arg2);
	    	 		break;	
	    	 	
			}
		
			
		}


		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
}
