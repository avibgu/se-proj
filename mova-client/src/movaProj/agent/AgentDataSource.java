package movaProj.agent;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AgentDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	public AgentDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
}
