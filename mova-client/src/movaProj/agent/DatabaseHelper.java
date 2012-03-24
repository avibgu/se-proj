package movaProj.agent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName="movaDB";
	static final String activityTable="Activity";
	static final String colID="Id";
	static final String colName="Name";
	static final String colType="Type";
	static final String colDescription="Description";
	static final String colSeverity="Severity";

	
	public DatabaseHelper(Context context) {
		super(context, dbName, null,3); 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		  
		  db.execSQL("CREATE TABLE "+activityTable+" ("+colID+ " integer primary key autoincrement, "+
				  colName+ " TEXT , " + colType+ " TEXT , " + colDescription + " TEXT , " + colSeverity + " TEXT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + activityTable);
		onCreate(db);
		
	}
	
	

}
