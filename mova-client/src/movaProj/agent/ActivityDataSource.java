package movaProj.agent;

import java.util.ArrayList;
import java.util.List;

import priority.Priority;

import type.ActivityType;
import type.AgentType;
import actor.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ActivityDataSource {
	// Database fields
		private SQLiteDatabase database;
		private DatabaseHelper dbHelper;
		private String[] allColumns = { DatabaseHelper.colID,
				DatabaseHelper.colName,DatabaseHelper.colType , DatabaseHelper.colDescription,  DatabaseHelper.colSeverity};

		public ActivityDataSource(Context context) {
			dbHelper = new DatabaseHelper(context);
		}

		public void open() throws SQLException {
			database = dbHelper.getWritableDatabase();
		}

		public void close() {
			dbHelper.close();
		}

		public Activity createActivity(Activity movaAct) {
			ContentValues cv = new ContentValues();
			//cv.put(DatabaseHelper.colID, 1);
		    cv.put(DatabaseHelper.colName, movaAct.getName());
		    cv.put(DatabaseHelper.colType, movaAct.getType().toString());
		    cv.put(DatabaseHelper.colDescription, "fdfsdf fsd f");
		    cv.put(DatabaseHelper.colSeverity, "URGENT");
			long insertId = database.insert(DatabaseHelper.activityTable, null,cv);
			// To show how to query
			Cursor cursor = database.query(DatabaseHelper.activityTable,
					allColumns, DatabaseHelper.colID + " = " + insertId, null,
					null, null, null);
			cursor.moveToFirst();
			return cursorToMovaActivity(cursor);
		}

		/*public void deleteComment(Comment comment) {
			long id = comment.getId();
			System.out.println("Comment deleted with id: " + id);
			database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
					+ " = " + id, null);
		}*/

		public List<Activity> getAllComments() {
			List<Activity> comments = new ArrayList<Activity>();
			Cursor cursor = database.query(DatabaseHelper.activityTable,
					allColumns, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Activity comment = cursorToMovaActivity(cursor);
				comments.add(comment);
				cursor.moveToNext();
			}
			// Make sure to close the cursor
			cursor.close();
			return comments;
		}

		private Activity cursorToMovaActivity(Cursor cursor) {
			Activity ans = new Activity("");
			ans.setId(cursor.getString(0));
			ans.setName(cursor.getString(1));
			ans.setType(ActivityType.CONFERENCE_REGISTRATION);
//			ans.setAgentType(AgentType.COORDINATOR);
			ans.setPriority(Priority.LOW);
			return ans;
		}
}
