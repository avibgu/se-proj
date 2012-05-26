package movaProj.sampleApplication;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import client.MovaClient;

import movaProj.agent.AgentDataSource;
import movaProj.agent.MovaAndroidClient;
import movaProj.agent.MovaMessage;
import movaProj.agent.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleListActivity extends Activity implements Observer,OnCreateContextMenuListener, OnClickListener {
	
	private List<actor.Activity> schedule1 = null; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this); 
		schedule1 = schedule;
		setContentView(R.layout.schedule_list);
		String[] displayedSchedule = prepareDisplayedSchedule(schedule);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,displayedSchedule);
		ListView listView = (ListView) findViewById(R.id.mylist);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
				actor.Activity activity = schedule.get(position);
				Intent i = new Intent(ScheduleListActivity.this,
						MovaActivityDetails.class);
				i.putExtra("description", activity.getDescription());
				i.putExtra("activityType", activity.getType());
				i.putExtra("activityName", activity.getName());
				i.putExtra("activityStartTime", activity.getStartTime());
				i.putExtra("activityEndTime", activity.getEndTime());
				i.putExtra("activityItems", activity.getParticipatingItemIds().toArray());
				startActivity(i);
			}
		});
		
		listView.setOnCreateContextMenuListener(this);
		
		Button logoutButton = (Button) this.findViewById(R.id.changeStatusButton);
		logoutButton.setOnClickListener(this);
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.mylist) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(schedule1.get(info.position).getName());
	    String[] menuItems = new String[] { "Postpone Activity", "Mark as Completed"};
	    for (int i = 0; i<menuItems.length; i++) {
	      menu.add(Menu.NONE, i, i, menuItems[i]);
	    }
	  }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  int menuItemIndex = item.getItemId();
	  
	  switch (menuItemIndex){
		case 0:
			break;
		case 1: // Complete Activity - Remove from the list and mark as completed.
			MovaAndroidClient.completeActivity(this, schedule1.get(info.position).getId());
			Toast.makeText(getApplicationContext(), "Activity Mark as Completed", Toast.LENGTH_LONG);
			ListView listView = (ListView) findViewById(R.id.mylist);
			final List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this); 
			String[] displayedSchedule = prepareDisplayedSchedule(schedule);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,displayedSchedule);
			listView.setAdapter(adapter);
			break;
		default:
			break;
		}
	   return true;
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


	@Override
	public void update(Observable observable, Object data) {
		MovaMessage message = (MovaMessage) data;
		Intent i;
		switch (message.getMessageType()) {
		case GOT_SCHEDULE:
			List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this);
			String[] displayedSchedule = prepareDisplayedSchedule(schedule);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,displayedSchedule);
			ListView listView = (ListView) findViewById(R.id.mylist);
			listView.setAdapter(adapter);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onClick(View v) {
		String agentId = new AgentDataSource(this).getAgentId();
		switch (v.getId()) {
		case R.id.changeStatusButton:
			new MovaClient().changeAgentStatus(agentId,false);
			setContentView(R.layout.login);
			Button loginButton = (Button) this.findViewById(R.id.loginButton);
			loginButton.setOnClickListener(this);
			break;
		case R.id.loginButton:
			new MovaClient().changeAgentStatus(agentId,true);
			Intent i = new Intent(ScheduleListActivity.this,
					ScheduleListActivity.class);
					startActivity(i);
			break;
			
		}
		
	}


}
