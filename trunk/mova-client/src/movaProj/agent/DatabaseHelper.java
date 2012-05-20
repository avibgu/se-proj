package movaProj.agent;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName="movaDB";
	
		
	// Activities
	static final String activityTable="Activity";
	static final String activityColID="Id";
	static final String activityColName="Name";
	static final String activityColDescription="Description";
	static final String activityColType="Type";
	static final String activityColState="State";
	static final String activityColStartTime="StartTime";
	static final String activityColEndTime="EndTime";
	static final String activityColEstimatedTime="EstimatedTime";
	
	// SCHEDULE
	static final String scheduleTable="Schedule";
	static final String scheduleIndexIdCol="ScheduleIndex";
	static final String scheduleActivityIdCol="ScheduleActivityId";
	
	// Items
	static final String itemTable="Item";
	static final String itemColID="Id";
	static final String itemColType="Type";
	static final String itemColState="State";
	static final String itemColAgentId="AgentId";

	// Agents
	static final String agentTable="Agent";
	static final String agentColID="Id";
	static final String agentColType="Type";
	static final String agentColStatus="Status";
	static final String agentColActivityId="ActivityId";
	
	// ActivityType
	static final String activityTypeTable="ActivityType";
	static final String activityTypeColName="Name";
	
	// ItemType
	static final String itemTypeTable="ItemType";
	static final String itemTypeColName="ItemName";
	static final String itemLatitudeCol="ItemLatitude";
	static final String itemLongitudeCol="ItemLongitude";
	
	
	// AgentType
	static final String agentTypeTable="AgentType";
	static final String agentTypeColName="Name";
	
	// ActivityTypeItems
	static final String activityTypeItemsTable="ActivityTypeItem";
	static final String activityTypeItemsColActivityId="ActivityId";
	static final String activityTypeItemsColItemType="ItemType";
	static final String activityTypeItemsColNumberOfItems="NumberOfItems";
	
	// ActivityTypeAgents
	static final String activityTypeAgentsTable="ActivityTypeAgent";
	static final String activityTypeAgentsColActivityId="ActivityId";
	static final String activityTypeAgentsColAgentType="AgentType";
	static final String activityTypeAgentsColNumberOfAgents="NumberOfAgents";
	
	public DatabaseHelper(Context context) {
		super(context, dbName, null,3); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		  
		  db.execSQL("CREATE TABLE "+activityTable+" ("+activityColID+ " TEXT primary key , "
				  + activityColName + " TEXT , " + activityColDescription + " TEXT , " + activityColType+ " TEXT , " 
				  + activityColState + " TEXT , " 
				  + activityColStartTime + " TEXT , " + activityColEndTime + " TEXT , "
				  + activityColEstimatedTime + " INTEGER )" );
		  
		  db.execSQL("CREATE TABLE "+scheduleTable+" ("+scheduleIndexIdCol+ " integer primary key, "+
				  scheduleActivityIdCol+ " integer )" );
		  
		  db.execSQL("CREATE TABLE "+itemTable+" ("+itemColID+ " TEXT primary key , "
				  + itemColType + " TEXT , " + itemColState + " TEXT , "
				  + itemColAgentId + " TEXT )" );
		  
		  db.execSQL("CREATE TABLE "+agentTable+" ("+agentColID+ " TEXT primary key , "
				  + agentColType + " TEXT , " + agentColStatus + " TEXT , "
				  + agentColActivityId + " TEXT )" );
		  
		  db.execSQL("CREATE TABLE "+activityTypeTable+" ("+activityTypeColName+ " TEXT primary key )" );
		  
		  db.execSQL("CREATE TABLE "+itemTypeTable+" ("+itemTypeColName+ " TEXT primary key ," + itemLatitudeCol + " INTEGER ," + itemLongitudeCol + " INTEGER)" );
		  
		  db.execSQL("CREATE TABLE "+agentTypeTable+" ("+agentTypeColName+ " TEXT primary key )" );

		  db.execSQL("CREATE TABLE "+activityTypeItemsTable+" ("+activityTypeItemsColActivityId+ " TEXT , "
				  + activityTypeItemsColItemType + " TEXT , " + activityTypeItemsColNumberOfItems + " INTEGER )" );
		  
		  db.execSQL("CREATE TABLE "+activityTypeAgentsTable+" ("+activityTypeAgentsColActivityId+ " TEXT , "
				  + activityTypeAgentsColAgentType + " TEXT , " + activityTypeAgentsColNumberOfAgents + " INTEGER )" );
		  
		  
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + activityTable);
		db.execSQL("DROP TABLE IF EXISTS " + scheduleTable);
		db.execSQL("DROP TABLE IF EXISTS " + itemTable);
		db.execSQL("DROP TABLE IF EXISTS " + agentTable);
		db.execSQL("DROP TABLE IF EXISTS " + activityTypeTable);
		db.execSQL("DROP TABLE IF EXISTS " + itemTypeTable);
		db.execSQL("DROP TABLE IF EXISTS " + agentTypeTable);
		db.execSQL("DROP TABLE IF EXISTS " + activityTypeItemsTable);
		db.execSQL("DROP TABLE IF EXISTS " + activityTypeAgentsTable);
		onCreate(db);
	}
	
	

}
