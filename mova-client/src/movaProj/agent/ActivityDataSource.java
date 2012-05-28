package movaProj.agent;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
			ContentValues cv1 = new ContentValues();
		    cv1.put(DatabaseHelper.activityColName, movaAct.getName());
		    cv1.put(DatabaseHelper.activityColType, movaAct.getType().toString());
		    cv1.put(DatabaseHelper.activityColDescription, movaAct.getDescription());
		    cv1.put(DatabaseHelper.activityColID, movaAct.getId());
		    cv1.put(DatabaseHelper.activityColStartTime, movaAct.getStartTime().getTime());
		    cv1.put(DatabaseHelper.activityColEndTime, movaAct.getEndTime().getTime());
		    cv1.put(DatabaseHelper.activityColEstimatedTime, movaAct.getEstimateTime());
		    cv1.put(DatabaseHelper.activityColState, movaAct.getState().toString());
		    
		    openToWrite();	    
		    
		    Set<String> participatingItemIds = movaAct.getParticipatingItemIds();
		    for (String id : participatingItemIds){
			    ContentValues cv2 = new ContentValues();
			    cv2.put(DatabaseHelper.activityItemsColactivityID, movaAct.getId());
			    cv2.put(DatabaseHelper.activityItemsColItemsId, id);
			    database.insert(DatabaseHelper.activityItemsTable, null,cv2);
		    }
		    
		    
		   	database.insert(DatabaseHelper.activityTable, null,cv1);
		
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
			database.delete(DatabaseHelper.scheduleTable, null, null);
			database.delete(DatabaseHelper.activityTable, null, null);
			close();
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
				Cursor cursor2 = database.query(DatabaseHelper.activityItemsTable, new String[] {DatabaseHelper.activityItemsColItemsId}, DatabaseHelper.activityColID + "=?" , new String[] {cursor.getString(1)}, null, null, null);
				schedule.add(cursorToMovaActivity(cursor1,cursor2)); 
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			
			close();
			return schedule;
		}
		
		private void insertDummyActivities(){
			List<Activity> schedule = new ArrayList<Activity>();
			Activity act1 = new Activity("BLA", new Timestamp(2012,12,5,10,5,8,45), new Timestamp(2012,12,5,12,5,8,45), 0, null, null, null, "Hi Test activity bla bla bla bla bla bla bla bla", "Activity #1");
			Set<String> itemsids = new HashSet<String>();
			itemsids.add("5d367716-d973-4f6a-a528-938d32a428eb");
			itemsids.add("e815177d-9c77-4921-8281-b1f0164abafb");
//			itemsids.add("9120d700-ead6-4547-abf7-f9d686ef7860");
			act1.setParticipatingItemIds(itemsids);
			schedule.add(act1);
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
		
		private actor.Activity cursorToMovaActivity(Cursor cursor1, Cursor cursor2) {
			cursor1.moveToFirst();
			actor.Activity ans = new actor.Activity("");
			ans.setId(cursor1.getString(0));
			ans.setName(cursor1.getString(1));
			ans.setDescription(cursor1.getString(2));
			ans.setType(cursor1.getString(3));
			ans.setState(ActivityState.valueOf((cursor1.getString(4))));
			ans.setStartTime(new Timestamp(cursor1.getLong(5)));
			ans.setEndTime(new Timestamp(cursor1.getLong(6)));
			ans.setEstimateTime(cursor1.getLong(7));
			cursor2.moveToFirst();
			Set<String> participantsItems = new HashSet<String>();
			while (!cursor2.isAfterLast()){
				participantsItems.add(cursor2.getString(0));
				cursor2.moveToNext();
			}
			ans.setParticipatingItemIds(participantsItems);
			cursor1.close();
			cursor2.close();
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

