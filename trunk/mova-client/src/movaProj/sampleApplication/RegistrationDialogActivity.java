package movaProj.sampleApplication;

import java.util.Observable;
import java.util.Observer;

import client.MovaClient;

import movaProj.agent.MovaAndroidClient;
import movaProj.agent.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegistrationDialogActivity extends Activity implements OnClickListener,Observer{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_registration_page);
		Button registerButton = (Button) this.findViewById(R.id.registrationButton);
	
		registerButton.setOnClickListener(this);
		
		MovaAndroidClient.addListener(this);
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		MovaAndroidClient.register(this);
		setContentView(R.layout.progress_bar);
		
	}
}
