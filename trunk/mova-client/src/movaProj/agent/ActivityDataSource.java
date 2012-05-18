package movaProj.agent;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public actor.Activity createActivity(actor.Activity movaAct) {
			ContentValues cv = new ContentValues();
		    cv.put(DatabaseHelper.activityColName, movaAct.getName());
		    cv.put(DatabaseHelper.activityColType, movaAct.getType().toString());
		    cv.put(DatabaseHelper.activityColDescription, "fdfsdf fsd f");
		   	long insertId = database.insert(DatabaseHelper.activityTable, null,cv);
		
			Cursor cursor = database.query(DatabaseHelper.activityTable,
					allColumns, DatabaseHelper.activityColID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			return cursorToMovaActivity(cursor);
		}
		
		public void createSchedule(List<actor.Activity> movaSched) {
			removeOldSchedule();
			for (int i=0; i<movaSched.size() ; ++i){
				ContentValues cv = new ContentValues();
				cv.put(DatabaseHelper.scheduleIndexIdCol, i);
				cv.put(DatabaseHelper.scheduleActivityIdCol, movaSched.get(i).getId());
			   	database.insert(DatabaseHelper.scheduleTable, null,cv);
			}
		}
		

		/*public void deleteComment(Comment comment) {
			long id = comment.getId();
			System.out.println("Comment deleted with id: " + id);
			database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
					+ " = " + id, null);
		}*/

		private void removeOldSchedule() {
			database.delete(DatabaseHelper.scheduleTable, null, null);			
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
			List<actor.Activity> schedule = new ArrayList<actor.Activity>();
			Cursor cursor = database.query(DatabaseHelper.scheduleTable,
					allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				actor.Activity activity = cursorToMovaActivity(cursor);
				schedule.add(activity);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return schedule;
		}
		
		private actor.Activity cursorToMovaActivity(Cursor cursor) {
			actor.Activity ans = new actor.Activity("");
			ans.setId(cursor.getString(0));
			ans.setName(cursor.getString(1));
//			ans.setType(ActivityType.CONFERENCE_REGISTRATION);
//			ans.setAgentType(AgentType.COORDINATOR);
//			ans.setPriority(Priority.LOW);
			return ans;
		}
}
