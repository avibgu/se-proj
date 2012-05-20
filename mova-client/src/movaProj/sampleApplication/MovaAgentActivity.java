package movaProj.sampleApplication;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import movaProj.agent.ActivityDataSource;
import movaProj.agent.MovaAndroidClient;
import movaProj.agent.MovaMessage;
import movaProj.agent.R;
import movaProj.agent.R.id;
import movaProj.agent.R.layout;

import type.MessageType;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MovaAgentActivity extends Activity implements OnClickListener,Observer {

	private ActivityDataSource datasource;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mova);
		Button button1 = (Button) this.findViewById(R.id.button1);
		Button button2 = (Button) this.findViewById(R.id.button2);
		Button button3 = (Button) this.findViewById(R.id.button3);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		MovaAndroidClient.addListener(this);
	}

	public void register() {
		Intent i = new Intent(MovaAgentActivity.this,
				InsertAgentTypeActivity.class);
		startActivity(i);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			register();
			break;
		case R.id.button2:
			Intent i1 = new Intent(MovaAgentActivity.this, CreateActivity.class);
			startActivity(i1);
			break;
		case R.id.button3:
			//List<actor.Activity> schedule = MovaAndroidClient.getSchedule(this);
			Intent i = new Intent(MovaAgentActivity.this,
			ScheduleListActivity.class);
			startActivity(i);
//			datasource = new ActivityDataSource(this);
//			datasource.openToWrite();
//
//			List<actor.Activity> values = datasource.getSchedule();
//			actor.Activity item = values.get(0);
//			Intent i2 = new Intent(MovaAgentActivity.this,
//					MovaActivityDetails.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("description", item.getDescription());
//			bundle.putString("activityType", item.getType().toString());
//			bundle.putString("activityName", item.getName());
//			startActivity(i2.putExtras(bundle));
			break;
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		MovaMessage message = (MovaMessage) data;
		switch (message.getMessageType()) {
		case REGISTER_SUCCESS:
			
			break;
		case REGISTER_FAILED:
			
			break;
		default:
			break;
		}
	}

}