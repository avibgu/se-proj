package movaProj.agent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import state.ActivityState;
import actor.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ActivityDataSource {
		// Database fields
		private SQLiteDatabase database;
		private DatabaseHelper dbHelper;
		private String[] allColumns = { DatabaseHelper.activityColID,
				DatabaseHelper.activityColName,DatabaseHelper.activityColType , DatabaseHelper.activityColDescription};

		public ActivityDataSource(Context context) {
			dbHelper = new DatabaseHelper(context);
		}

		public void openToWrite() {
			database = dbHelper.getWritableDatabase();
		}
		
		public void openToRead(){
			database = dbHelper.getReadableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public void createActivity(actor.Activity movaAct) {
			ContentValues cv = new ContentValues();
		    cv.put(DatabaseHelper.activityColName, movaAct.getName());
		    cv.put(DatabaseHelper.activityColType, movaAct.getType().toString());
		    cv.put(DatabaseHelper.activityColDescription, movaAct.getDescription());
		    cv.put(DatabaseHelper.activityColID, movaAct.getId());
		    cv.put(DatabaseHelper.activityColStartTime, movaAct.getStartTime().getTime());
		    cv.put(DatabaseHelper.activityColEndTime, movaAct.getEndTime().getTime());
		    cv.put(DatabaseHelper.activityColEstimatedTime, movaAct.getEstimateTime());
		    cv.put(DatabaseHelper.activityColState, movaAct.getState().toString());
		    		    
		    openToWrite();
		    
		   	long insertId = database.insert(DatabaseHelper.activityTable, null,cv);
		
			close();
		}
		
		public void createSchedule(List<actor.Activity> movaSched) {
			removeOldSchedule();
			
			for (int i=0; i<movaSched.size() ; ++i){
				ContentValues cv = new ContentValues();
				createActivity(movaSched.get(i));
				cv.put(DatabaseHelper.scheduleIndexIdCol, i);
				cv.put(DatabaseHelper.scheduleActivityIdCol, movaSched.get(i).getId());
				openToWrite();
			   	database.insert(DatabaseHelper.scheduleTable, null,cv);
			   	close();
			}
			
		}
		
		private void removeOldSchedule() {
			openToWrite();
			int one = database.delete(DatabaseHelper.scheduleTable, null, null);
			int two = database.delete(DatabaseHelper.activityTable, null, null);
			close();
		}

		
		
		public List<actor.Activity> getAllActivities() {
			List<actor.Activity> activities = new ArrayList<actor.Activity>();
			Cursor cursor = database.query(DatabaseHelper.activityTable,
					allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				actor.Activity comment = cursorToMovaActivity(cursor);
				activities.add(comment);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return activities;
		}

		public List<actor.Activity> getSchedule() {
			insertDummyActivities();
			openToRead();
			List<actor.Activity> schedule = new ArrayList<actor.Activity>();
			Cursor cursor = database.query(DatabaseHelper.scheduleTable,
					new String[] {DatabaseHelper.scheduleIndexIdCol,
								  DatabaseHelper.scheduleActivityIdCol}, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Cursor cursor1 = database.query(DatabaseHelper.activityTable, null, DatabaseHelper.activityColID + "=?" , new String[] {cursor.getString(1)}, null, null, null);
				schedule.add(cursorToMovaActivity(cursor1)); 
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			close();
			return schedule;
		}
		
		private void insertDummyActivities(){
			List<Activity> schedule = new ArrayList<Activity>();
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,10,5,8,45), new Timestamp(2012,12,5,12,5,8,45), 0, null, null, null, "Hi Test activity bla bla bla bla bla bla bla bla", "Activity #1"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,12,5,8,45), new Timestamp(2012,12,5,13,5,8,45), 0, null, null, null,  "Hi Test activity bla bla bla bla bla bla bla bla", "Activity #2"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null,  "Hi Test activity bla bla bla bla bla bla bla bla", "Activity #3"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null,  "Hi Test activity bla bla bla bla bla bla bla bla", "Activity #4"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null, null, "Activity #5"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null, null, "Activity #6"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null, null, "Activity #7"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null, null, "Activity #8"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null, null, "Activity #9"));
			schedule.add(new Activity("BLA", new Timestamp(2012,12,5,13,5,8,45), new Timestamp(2012,12,5,15,5,8,45), 0, null, null, null, null, "Activity #10"));
			createSchedule(schedule);
		}
		
		private actor.Activity cursorToMovaActivity(Cursor cursor) {
			cursor.moveToFirst();
			actor.Activity ans = new actor.Activity("");
			ans.setId(cursor.getString(0));
			ans.setName(cursor.getString(1));
			ans.setDescription(cursor.getString(2));
			ans.setType(cursor.getString(3));
			ans.setState(ActivityState.valueOf((cursor.getString(4))));
			ans.setStartTime(new Timestamp(cursor.getLong(5)));
			ans.setEndTime(new Timestamp(cursor.getLong(6)));
			ans.setEstimateTime(cursor.getLong(7));
			return ans;
		}
		
		public void insertActivityTypes(Vector<String> types){
			openToWrite();
			database.delete(DatabaseHelper.activityTypeTable, null, null);
			close();
			for (int i=0; i<types.size() ; ++i){
				ContentValues cv = new ContentValues();
				cv.put(DatabaseHelper.activityTypeColName, types.get(i));
				openToWrite();
			   	database.insert(DatabaseHelper.activityTypeTable, null,cv);
			   	close();
			}
		}
		
		public void completeActivity(String activityId){
			openToWrite();
			database.beginTransaction();
			int one = database.delete(DatabaseHelper.activityTable, DatabaseHelper.activityColID + "=?", new String[] {activityId});
			int two = database.delete(DatabaseHelper.scheduleTable, DatabaseHelper.scheduleActivityIdCol + "=?" , new String[] {activityId});
			database.setTransactionSuccessful();
			database.endTransaction();
			close();
		}
}

