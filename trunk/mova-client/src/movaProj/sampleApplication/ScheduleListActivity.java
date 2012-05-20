package movaProj.sampleApplication;

import java.util.List;

import movaProj.agent.MovaAndroidClient;
import movaProj.agent.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleListActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_list);
		List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this);
		String[] displayedSchedule = prepareDisplayedSchedule(schedule);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,displayedSchedule);
		setListAdapter(adapter);
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
//		String item = (String) getListAdapter().getItem(position);
//		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}
	
	private String convertTimeToString(int time){
		if (time > 0 && time < 10){
			return "0" + time;
		}else{
			return String.valueOf(time);
		}
	}
	
	private String[] prepareDisplayedSchedule(List<actor.Activity> schedule) {
		String[] ans = new String[schedule.size()];
		for (int i=0 ; i < schedule.size() ; ++i){
			actor.Activity curActivity = schedule.get(i);
			ans[i] = "< " +curActivity.getStartTime().getHours() + ":" + 
					 convertTimeToString(curActivity.getStartTime().getMinutes()) + " - " +
					 convertTimeToString(curActivity.getEndTime().getHours())+ ":" + 
					 convertTimeToString(curActivity.getEndTime().getMinutes()) + " >  " + 
					 curActivity.getName();
		}
		
		return ans;
		
	}

}
