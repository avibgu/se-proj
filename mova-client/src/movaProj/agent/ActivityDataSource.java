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
		    if (movaAct.getActualStartTime() != null && movaAct.getActualEndTime() != null){
			    cv1.put(DatabaseHelper.activityColStartTime, movaAct.getActualStartTime().getTime());
			    cv1.put(DatabaseHelper.activityColEndTime, movaAct.getActualEndTime().getTime());
		    }
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
		
		public void removeOldSchedule() {
			openToWrite();
			database.delete(DatabaseHelper.scheduleTable, null, null);
			database.delete(DatabaseHelper.activityTable, null, null);
			database.delete(DatabaseHelper.activityItemsTable, null, null);
			close();
		}

		
		
		public List<actor.Activity> getSchedule() {
//			insertDummyActivities();	TODO: Shiran...
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
			
			Activity act0 = new Activity("Lecture", new Timestamp(2012,6,7,10,0,8,45), new Timestamp(2012,6,7,11,0,8,45), 0, null, null, null,  "Lecturer: Prof. Lee James\nProfessor James discuss the popular recent reseaches about global warming. ", "Recent Researches overview");
			Set<String> itemsids = new HashSet<String>();
			itemsids.add("fe708c5b-d182-4703-b41f-37daa0f477fa");
			itemsids.add("7178f3de-d6bf-4448-a1fe-88467f7d5f44");
			act0.setParticipatingItemIds(itemsids);
			act0.setActualStartTime(new Timestamp(2012,6,7,10,0,8,45));
			act0.setActualEndTime(new Timestamp(2012,6,7,11,0,8,45));
			schedule.add(act0);
			schedule.add(new Activity("Breakfast", new Timestamp(2012,6,7,11,0,8,45), new Timestamp(2012,6,7,12,0,8,45), 0, null, null, null,  "Breakfast at the conference lobby", "Breakfast"));
//			Activity act1 = new Activity("Lecture", new Timestamp(2012,6,7,12,0,8,45), new Timestamp(2012,6,7,13,0,8,45), 0, null, null, null,  "Lecturer: Prof. Dana Schneider\nProfessor Schneider discusses the local, regional, and international actions that are already beginning to address global warming and describe other actions that could be taken, if there were political will to substantially reduce the magnitude of the risks.", "Global Warming Lecture");
//			itemsids = new HashSet<String>();
//			itemsids.add("fe708c5b-d182-4703-b41f-37daa0f477fa");
//			itemsids.add("6f0c984f-b6bf-4d8f-8bf4-ae1d780215c9");
//			itemsids.add("7f3e33e0-1f46-42bd-9a00-95a1ce6e5be0");
//			itemsids.add("7178f3de-d6bf-4448-a1fe-88467f7d5f44");
//			act1.setParticipatingItemIds(itemsids);
//			act1.setActualStartTime(new Timestamp(2012,6,7,10,0,8,45));
//			act1.setActualEndTime(new Timestamp(2012,6,7,11,0,8,45));
//			schedule.add(act1);
			schedule.add(new Activity("Lunch", new Timestamp(2012,6,7,14,0,8,45), new Timestamp(2012,6,7,15,30,8,45), 0, null, null, null, "Lunch at the conference lobby", "Lunch"));
//			schedule.add(new Activity("Lecture", new Timestamp(2012,6,7,15,0,8,45), new Timestamp(2012,6,7,16,0,8,45), 0, null, null, null, "Questions to the Global Warming experts", "Experts Panel"));
//			schedule.add(new Activity("Break", new Timestamp(2012,6,7,16,0,8,45), new Timestamp(2012,6,7,16,15,8,45), 0, null, null, null, null, "Coffe break"));
//			schedule.add(new Activity("Lecture", new Timestamp(2012,6,7,16,15,8,45), new Timestamp(2012,6,7,17,0,8,45), 0, null, null, null, "Conference Summary", "Summary"));
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
			ans.setActualStartTime(new Timestamp(cursor1.getLong(5)));
			ans.setActualEndTime(new Timestamp(cursor1.getLong(6)));
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

