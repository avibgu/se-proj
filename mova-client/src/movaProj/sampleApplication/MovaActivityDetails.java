package movaProj.sampleApplication;

import java.util.List;

import movaProj.agent.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class MovaActivityDetails extends Activity implements OnClickListener{
		
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.mova_activity_details);
	        TextView description = (TextView) findViewById(R.id.descriptionDetails);
	        TextView activityType = (TextView) findViewById(R.id.activityTypeDetails);
	        TextView activityName = (TextView) findViewById(R.id.activityNameDetails);
	        TextView activityStartTime= (TextView) findViewById(R.id.activitystartTimeDetails);
	        TextView activityEndTime = (TextView) findViewById(R.id.activityendTimeDetails);
	        description.setText(this.getIntent().getExtras().getString("description"));
	        activityType.setText(this.getIntent().getExtras().getString("activityType"));
	        activityName.setText(this.getIntent().getExtras().getString("activityName"));
	        activityStartTime.setText(this.getIntent().getExtras().getString("activityStartTime"));
	        activityEndTime.setText(this.getIntent().getExtras().getString("activityEndTime"));
	        
	        List<String> items = this.getIntent().getExtras().getStringArrayList("activityItems");
	        // Items List
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        		R.layout.activity_details_items_list,items);
	        ListView listView = (ListView) findViewById(R.id.activityDetailsItemslist);
	        listView.setVerticalScrollBarEnabled(false);
	        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			listView.setAdapter(adapter);
	        
	        Button okButton = (Button)this.findViewById(R.id.okButton);
	        okButton.setOnClickListener(this);
	        
	    }

	@Override
	public void onClick(View arg0) {
		 Intent i = new Intent(MovaActivityDetails.this, ScheduleListActivity.class);
         startActivity(i);
	}
}
