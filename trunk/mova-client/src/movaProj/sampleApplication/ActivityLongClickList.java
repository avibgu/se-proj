package movaProj.sampleApplication;

import movaProj.agent.ActivityDataSource;
import movaProj.agent.MovaAndroidClientImpl;
import movaProj.agent.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityLongClickList extends Activity {
	
	private String activityId = "";
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityId = getIntent().getStringExtra("activityId");
		setContentView(R.layout.activity_long_click_list);
		String[] values = new String[] { "Postpone Activity", "Complete"};
		ListView listView = (ListView) findViewById(R.id.longClickList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.id.longClickList, values);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				switch (position){
				case 0:
					break;
				case 1: // Complete Activity - Remove from the list and mark as completed.
					new MovaAndroidClientImpl().completeActivity(getParent(), activityId);
					Intent i = new Intent(ActivityLongClickList.this,
							ScheduleListActivity.class);
					startActivity(i);
					break;
				default:
					break;
				}
			}
		});
	}
}
