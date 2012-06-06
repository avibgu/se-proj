package movaProj.sampleApplication;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Vector;

import movaProj.agent.AgentDataSource;
import movaProj.agent.C2DMReceiver;
import movaProj.agent.ItemDataSource;
import movaProj.agent.MovaAndroidClient;
import movaProj.agent.MovaMessage;
import movaProj.agent.R;
import actor.Item;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import client.MovaClient;

public class ScheduleListActivity extends Activity implements Observer,OnCreateContextMenuListener, OnClickListener {
	
	private List<actor.Activity> schedule1 = null; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this); 
		String app_name = (String)this.getText(R.string.app_name);
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Create a notification using android stat_notify_chat icon. 
        Notification notification = new Notification(android.R.drawable.stat_notify_chat,"New Schedule", 0);

        // Create a pending intent to call the HomeActivity when the notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT); // 
        notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_AUTO_CANCEL; 
        // Set the notification and register the pending intent to it
        notification.setLatestEventInfo(this, app_name, "Your schedule has been changed", pendingIntent); //

        // Trigger the notification
        notificationManager.notify(0, notification);
		schedule1 = schedule;
		setContentView(R.layout.schedule_list);
		String[] displayedSchedule = prepareDisplayedSchedule(schedule);
		ArrayAdapter<String> adapter = new MyArrayAdapter(this,
				R.layout.list_item,displayedSchedule);
		ListView listView = (ListView) findViewById(R.id.mylist);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
//				view.setBackgroundColor(Color.parseColor("#75D1FF"));
				actor.Activity activity = schedule.get(position);
				Set<String> itemsId = activity.getParticipatingItemIds();
				Vector<String> displayedItems = new Vector<String>();
				for (String itemId : itemsId){
					ItemDataSource itemDataSource = new ItemDataSource(parent.getContext());
					Item item = itemDataSource.getItem(itemId);
					displayedItems.add(item.getType() + "  : Conference Room A" );
				}
				Intent i = new Intent(ScheduleListActivity.this,
						MovaActivityDetails.class);
				i.putExtra("description", activity.getDescription());
				i.putExtra("activityType", activity.getType());
				i.putExtra("activityName", activity.getName());
				i.putExtra("activityStartTime", activity.getActualStartTime().getHours() + ":" + 
						 convertTimeToString(activity.getActualStartTime().getMinutes()));
				i.putExtra("activityEndTime", activity.getActualEndTime().getHours() + ":" + 
						 convertTimeToString(activity.getActualEndTime().getMinutes()));
				i.putExtra("activityItems", displayedItems);
				startActivity(i);
			}
		});
		
		listView.setOnCreateContextMenuListener(this);
		
		Button logoutButton = (Button) this.findViewById(R.id.changeStatusButton);
		logoutButton.setOnClickListener(this);
		Button recalculateButton = (Button) this.findViewById(R.id.initialButton);
		recalculateButton.setOnClickListener(this);
		C2DMReceiver.addListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	  if (v.getId()==R.id.mylist) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(schedule1.get(info.position).getName());
	    String[] menuItems = new String[] { "Start Activity" , "Postpone Activity", "Mark as Completed"};
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
	  	case 0: // Start Activity
			MovaAndroidClient.startActivity(this, schedule1.get(info.position).getId());
			Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG);
			break;
		case 1:
			// custom dialog
			final Dialog dialog = new Dialog(this);
			final String activityId = schedule1.get(info.position).getId();
			final long activityOldEndTime = schedule1.get(info.position).getEndTime().getTime();
			dialog.setContentView(R.layout.picker);
			dialog.setTitle("Postpone Activity");
 
			Button postponeButton = (Button) dialog.findViewById(R.id.numberPickerOkButton);
			// if button is clicked, close the custom dialog
			postponeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					NumberPicker picker = (NumberPicker) dialog.findViewById(R.id.numberPickerComponent);
					MovaAndroidClient.postponeActivity(activityId, (picker.mCurrent)*60*1000, activityOldEndTime);
				}
			});
			Button cancelButton = (Button) dialog.findViewById(R.id.numberPickerCancelButton);
			// if button is clicked, close the custom dialog
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
 
			dialog.show();
			break;
		case 2: // Complete Activity - Remove from the list and mark as completed.
			MovaAndroidClient.completeActivity(this, schedule1.get(info.position).getId());
			Toast.makeText(getApplicationContext(), "Activity Mark as Completed", Toast.LENGTH_LONG);
			ListView listView = (ListView) findViewById(R.id.mylist);
			final List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this); 
			String[] displayedSchedule = prepareDisplayedSchedule(schedule);
			ArrayAdapter<String> adapter = new MyArrayAdapter(this,
					R.layout.list_item,displayedSchedule);
			listView.setAdapter(adapter);
			break;
		default:
			break;
		}
	   return true;
	}
	
	private String convertTimeToString(int time){
		if (time >= 0 && time < 10){
			return "0" + time;
		}else{
			return String.valueOf(time);
		}
	}
	
	private String[] prepareDisplayedSchedule(List<actor.Activity> schedule) {
		String[] ans = new String[schedule.size()];
		for (int i=0 ; i < schedule.size() ; ++i){
			actor.Activity curActivity = schedule.get(i);
			ans[i] = "< " +curActivity.getActualStartTime().getHours() + ":" + 
					 convertTimeToString(curActivity.getActualStartTime().getMinutes()) + " - " +
					 convertTimeToString(curActivity.getActualEndTime().getHours())+ ":" + 
					 convertTimeToString(curActivity.getActualEndTime().getMinutes()) + " >  \n" + 
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
			ArrayAdapter<String> adapter = new MyArrayAdapter(this,
					R.layout.list_item,displayedSchedule);
			ListView listView = (ListView) findViewById(R.id.mylist);
			listView.setAdapter(adapter);
			String app_name = (String)this.getText(R.string.app_name);
			NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            // Create a notification using android stat_notify_chat icon. 
            Notification notification = new Notification(android.R.drawable.stat_notify_chat,"New Schedule", 0);
 
            // Create a pending intent to call the HomeActivity when the notification is clicked
            PendingIntent pendingIntent = PendingIntent.getActivity(this, -1, new Intent(this, MovaAgentActivity.class), PendingIntent.FLAG_UPDATE_CURRENT); // 
            notification.when = System.currentTimeMillis();
            notification.flags |= Notification.FLAG_AUTO_CANCEL; 
            // Set the notification and register the pending intent to it
            notification.setLatestEventInfo(this, app_name, "Your schedule has been changed", pendingIntent); //
 
            // Trigger the notification
            notificationManager.notify(0, notification);
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
		case R.id.initialButton:
			MovaAndroidClient.recalculate(this);
			break;
		case R.id.numberPickerOkButton:
			NumberPicker numberPicker = (NumberPicker) this.findViewById(R.id.numberPickerComponent);
			int addedTime = numberPicker.getCurrent();
			long addedTimeInMilliseconds = addedTime * 60000;
//			mEstimateTime = mEstimateTime + addedTime;
//			mEndTime.setTime(mEndTime.getTime() + addedTimeInMilliseconds);
//			new MovaClient().postponeActivity(activityId, newFinishTime);
			break;
		}
	

	}


}
