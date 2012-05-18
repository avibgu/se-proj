package movaProj.agent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName="movaDB";
	
	// Activities
	static final String activityTable="Activity";
	static final String activityColID="Id";
	static final String activityColName="Name";
	static final String activityColType="Type";
	static final String activityColDescription="Description";
	
	// SCHEDULE
	static final String scheduleTable="Schedule";
	static final String scheduleIndexIdCol="ScheduleIndex";
	static final String scheduleActivityIdCol="ScheduleActivityId";
	
	// Items
	static final String itemTable="Item";
	static final String itemColID="Id";
	static final String itemColType="Type";

	
	
	public DatabaseHelper(Context context) {
		super(context, dbName, null,3); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		  
		  db.execSQL("CREATE TABLE "+activityTable+" ("+activityColID+ " integer primary key autoincrement, "+
				  activityColName+ " TEXT , " + activityColType+ " TEXT , " + activityColDescription + " TEXT)" );

		  db.execSQL("CREATE TABLE "+scheduleTable+" ("+scheduleIndexIdCol+ " integer primary key, "+
				  scheduleActivityIdCol+ " integer )" );

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + activityTable);
		db.execSQL("DROP TABLE IF EXISTS " + scheduleTable);
		onCreate(db);
		
	}
	
	

}
