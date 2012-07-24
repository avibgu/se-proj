package movaProj.sampleApplication;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import movaProj.agent.AgentDataSource;
import movaProj.agent.MovaAndroidClientImpl;
import movaProj.agent.MovaMessage;
import movaProj.agent.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class InsertAgentTypeActivity extends Activity implements OnClickListener,OnItemSelectedListener,Observer{
	
	 private String agentType;
	 private String registrationId="";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.insert_agent_type);
	       
	       // Set the registration Id.
	       registrationId = getIntent().getStringExtra("registrationId");
	       
	       // Get agent Types.
	       Vector<String> agentTypes = new AgentDataSource(this).getAgentTypes();
	       
	       Spinner agentTypesSpinner = (Spinner) findViewById(R.id.insertAgentTypeSpinner);
	       ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, agentTypes);
	       agentTypesSpinner.setAdapter(adapter);
	       agentTypesSpinner.setOnItemSelectedListener(this);
	       
	       Button agentTypeButton = (Button)this.findViewById(R.id.agentTypeButton);
	       agentTypeButton.setOnClickListener(this);
	       new MovaAndroidClientImpl().addListener(this);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		this.agentType = arg0.getItemAtPosition(arg2).toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		setContentView(R.layout.progress_bar);
		new MovaAndroidClientImpl().registerAgent(this,registrationId, agentType);
	}

	@Override
	public void update(Observable observable, Object data) {
		MovaMessage message = (MovaMessage) data;
		Intent i;
		switch (message.getMessageType()) {
		case REGISTER_SUCCESS:
			i = new Intent(InsertAgentTypeActivity.this,
					ScheduleListActivity.class);
			startActivity(i);
			break;
		case REGISTER_FAILED:
			i = new Intent(InsertAgentTypeActivity.this,
					RegistrationDialogActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
		
	}

}
