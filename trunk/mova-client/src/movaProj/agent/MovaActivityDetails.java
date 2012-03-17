package movaProj.agent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MovaActivityDetails extends Activity implements OnClickListener{
		
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mova_activity_details);
	        TextView severity = (TextView) findViewById(R.id.severityDetails);
	        TextView description = (TextView) findViewById(R.id.descriptionDetails);
	        TextView activityType = (TextView) findViewById(R.id.activityTypeDetails);
	        TextView activityName = (TextView) findViewById(R.id.activityNameDetails);
	        severity.setText(this.getIntent().getExtras().getString("severity"));
	        description.setText(this.getIntent().getExtras().getString("description"));
	        activityType.setText(this.getIntent().getExtras().getString("activityType"));
	        activityName.setText(this.getIntent().getExtras().getString("activityName"));
	        
	        Button okButton = (Button)this.findViewById(R.id.okButton);
	        okButton.setOnClickListener(this);
	        
	    }

	@Override
	public void onClick(View arg0) {
		 Intent i = new Intent(MovaActivityDetails.this, MovaAgentActivity.class);
         startActivity(i);
	}
}
