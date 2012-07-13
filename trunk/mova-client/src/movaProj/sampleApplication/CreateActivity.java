package movaProj.sampleApplication;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import movaProj.agent.AgentDataSource;
import movaProj.agent.MovaAndroidClientImpl;
import movaProj.agent.R;
import type.ActivityType;
import type.AgentType;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TimePicker;
import client.MovaClient;

public class CreateActivity extends Activity implements OnClickListener,OnItemSelectedListener {
	  static final int DATE_DIALOG_ID = 0;
	  static final int START_TIME_DIALOG_ID = 1;
	  static final int END_TIME_DIALOG_ID = 2;
	  private String agentType = "";
	  private String activityType = "";
	  EditText activityName;
	  private int mYear,mMonth,mDay;
	  private int mStartHour,mStartMinute;
	  private int mEndHour,mEndMinute;
      private EditText mDateDisplay;
      private EditText mStartTimeDisplay;
      private EditText mEndTimeDisplay;
      List<actor.Activity> allActivities = new ArrayList<actor.Activity>();
      Set<String> dependenciesIds = new HashSet<String>();
      ArrayAdapter<String> dependenciesAdapter;
      List<String> addedDependenciesStrings=new ArrayList<String>();
      
	  @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.create_activity);
	        
	     }
	@Override
	public void onStart(){
	  super.onStart();
	  String agentId = new AgentDataSource(this).getAgentId();
	  allActivities = new MovaAndroidClientImpl().getAllActivities(agentId);
      List<String> allActivitiesStrings = getActivitiesNames(allActivities);
      Spinner s2 = (Spinner) findViewById(R.id.agentTypeSpinner);
      ArrayAdapter<String> adapter2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, AgentType.values());
      s2.setAdapter(adapter2);
      s2.setOnItemSelectedListener(this);
      
      Spinner s3 = (Spinner) findViewById(R.id.activityTypeSpinner);
      ArrayAdapter<String> adapter3 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, ActivityType.values());
      s3.setAdapter(adapter3);
      s3.setOnItemSelectedListener(this);
      
      Spinner s4 = (Spinner) findViewById(R.id.dependenciesSpinner);
      ArrayAdapter<String> adapter4 = new ArrayAdapter(this,android.R.layout.simple_spinner_item, allActivitiesStrings);
      s4.setAdapter(adapter4);
      s4.setOnItemSelectedListener(this);
      
      dependenciesAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,addedDependenciesStrings);
      ((ListView)findViewById(R.id.dependenciesList)).setAdapter(dependenciesAdapter);
      
      Button submitActivityButton = (Button)this.findViewById(R.id.submitActivityButton);
      submitActivityButton.setOnClickListener(this);
      
      Button addDependencyButton = (Button)this.findViewById(R.id.addDependencyButton);
      addDependencyButton.setOnClickListener(this);
      
      mDateDisplay = (EditText) findViewById(R.id.date);
      mStartTimeDisplay = (EditText) findViewById(R.id.startTime);
      mEndTimeDisplay = (EditText) findViewById(R.id.endTime);
      
      final Calendar c = Calendar.getInstance();  
      mYear = c.get(Calendar.YEAR);  
      mMonth = c.get(Calendar.MONTH);  
      mDay = c.get(Calendar.DAY_OF_MONTH);  
      updateDateDisplay();
      mDateDisplay.setOnClickListener(new View.OnClickListener() {  
          public void onClick(View v) {  
              showDialog(DATE_DIALOG_ID);  
          }  
      }); 
      mStartTimeDisplay.setOnClickListener(new View.OnClickListener() {  
          public void onClick(View v) {  
              showDialog(START_TIME_DIALOG_ID);  
          }  
      });
      mEndTimeDisplay.setOnClickListener(new View.OnClickListener() {  
          public void onClick(View v) {  
              showDialog(END_TIME_DIALOG_ID);  
          }  
      }); 
}
	  
	  private List<String> getActivitiesNames(List<actor.Activity> allActivities) {
		List<String> activities = new ArrayList<String>();
		for (actor.Activity act : allActivities){
			activities.add(act.getType() + "-" +act.getName());
		}
		return activities;
	}

		// the callback received when the user "sets" the date in the dialog
	    private DatePickerDialog.OnDateSetListener mDateSetListener =
	            new DatePickerDialog.OnDateSetListener() {

	                public void onDateSet(DatePicker view, int year, 
	                                      int monthOfYear, int dayOfMonth) {
	                    mYear = year;
	                    mMonth = monthOfYear;
	                    mDay = dayOfMonth;
	                    updateDateDisplay();
	                }
	            };
	    
     // the callback received when the user "sets" the startTime in the dialog
	    private TimePickerDialog.OnTimeSetListener mStartTimeSetListener =
	            new TimePickerDialog.OnTimeSetListener() {

	                public void onTimeSet(TimePicker view, int hour, 
	                                      int minute) {
	                    mStartHour = hour;
	                    mStartMinute = minute;
	                    updateStartTimeDisplay();
	                }
	            };        
        
        // the callback received when the user "sets" the startTime in the dialog
	    private TimePickerDialog.OnTimeSetListener mEndTimeSetListener =
	            new TimePickerDialog.OnTimeSetListener() {

	                public void onTimeSet(TimePicker view, int hour, 
	                                      int minute) {
	                    mEndHour = hour;
	                    mEndMinute = minute;
	                    updateEndTimeDisplay();
	                }
	            };          
	            
	            
        // updates the date in the TextView
        private void updateDateDisplay() {
            mDateDisplay.setText(new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(" "));
        }
        
        
        private String convertTimeToString(int time){
    		if (time >= 0 && time < 10){
    			return "0" + time;
    		}else{
    			return String.valueOf(time);
    		}
    	}
        
        // updates the start time in the TextView
        private void updateStartTimeDisplay() {
        	mStartTimeDisplay.setText(new StringBuilder()
                    .append(convertTimeToString(mStartHour)).append(":")
                    .append(convertTimeToString(mStartMinute)).append(" "));
        }
        
        // updates the end time in the TextView
        private void updateEndTimeDisplay() {
            mEndTimeDisplay.setText(new StringBuilder()
                        .append(convertTimeToString(mEndHour)).append(":")
                        .append(convertTimeToString(mEndMinute)).append(" "));
        }
        
		@Override
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
						mDay);
			case START_TIME_DIALOG_ID:
				return new TimePickerDialog(this, mStartTimeSetListener, mStartHour, mStartMinute,true);
			case END_TIME_DIALOG_ID:
				return new TimePickerDialog(this, mEndTimeSetListener, mEndHour, mEndMinute,true);
			}
			return null;
		}
		  public void onClick(View v){
			 switch (v.getId()) {
			 	case R.id.submitActivityButton:
					 activityName   = (EditText)findViewById(R.id.ActivityNameText);
					 actor.Activity newActivity = new actor.Activity(activityName.getText().toString());
					 newActivity.setType(((Spinner)findViewById(R.id.activityTypeSpinner)).getSelectedItem().toString());
					 Map<AgentType,Integer> requiredAgents= new HashMap<AgentType,Integer>();
					 requiredAgents.put(new AgentType(((Spinner)findViewById(R.id.agentTypeSpinner)).getSelectedItem().toString()), 1);
					 newActivity.setRequiredAgents(requiredAgents);
					 newActivity.setDescription(((EditText)findViewById(R.id.descriptionText)).getText().toString());
					 Timestamp startTime = new Timestamp(mYear-1900, mMonth, mDay, mStartHour, mStartMinute, 0, 0);
					 Timestamp endTime = new Timestamp(mYear-1900, mMonth, mDay, mEndHour, mEndMinute, 0, 0);
					 newActivity.setStartTime(startTime);
					 newActivity.setEndTime(endTime);
					 newActivity.setEstimateTime(Long.valueOf((((EditText)findViewById(R.id.estimatedTime)).getText()).toString()) * 60000);
					 newActivity.setRequiredActivityIds(dependenciesIds);
					 
					 Intent i = new Intent(CreateActivity.this, ScheduleListActivity.class);
					 startActivity(i);
					 
					 new MovaAndroidClientImpl().createNewActivity(this,newActivity);
			         break;
			 	case R.id.addDependencyButton:
			 		long dependencyId = ((Spinner)findViewById(R.id.dependenciesSpinner)).getSelectedItemId();
			 		addedDependenciesStrings.add(allActivities.get((int)dependencyId).getName());
			 		dependenciesAdapter.notifyDataSetChanged();
			 		LinearLayout.LayoutParams mParam =  new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			        ((ListView)findViewById(R.id.dependenciesList)).setLayoutParams(mParam);
			  		break;
			 }
		  }


		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch(arg0.getId()){
	    	 	case R.id.activityTypeSpinner :
	    	 		this.activityType = (String)arg0.getItemAtPosition(arg2);
	    	 		break;	
	    	 	case R.id.agentTypeSpinner :
	    	 		this.agentType = (String)arg0.getItemAtPosition(arg2);
	    	 		break;	
	    	}
		}


		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
}
