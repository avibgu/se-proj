package movaProj.agent;

import java.util.Vector;

import actor.Agent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AgentDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	public AgentDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public void openToWrite() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void openToRead() throws SQLException {
		database = dbHelper.getReadableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public void insertAgent(Agent agent){
		openToWrite();
		database.delete(DatabaseHelper.agentTable, null, null);
		close();
		ContentValues cv = new ContentValues();
	    cv.put(DatabaseHelper.agentColID, agent.getId());
	    cv.put(DatabaseHelper.agentColStatus, "true");
	    cv.put(DatabaseHelper.agentColType, agent.getType().toString());
		   	    		    
	    openToWrite();
	    
	   	database.insert(DatabaseHelper.agentTable, null,cv);
	
		close();
	}
	
	public String getAgentId(){
		openToRead();
		Cursor cursor = database.query(DatabaseHelper.agentTable,
				new String[] {DatabaseHelper.agentColID}, null, null, null, null, null);
		cursor.moveToFirst();
		
		String agentId = cursor.getString(0);
		// Make sure to close the cursor
		cursor.close();
		close();
		return agentId;
	}
	
	public void insertAgentTypes(Vector<String> types){
		openToWrite();
		database.delete(DatabaseHelper.agentTypeTable, null, null);
		close();
		openToWrite();
		database.beginTransaction();
		
		for (int i=0; i<types.size() ; ++i){
			ContentValues cv = new ContentValues();
			cv.put(DatabaseHelper.agentTypeColName, types.get(i));
			
		   	database.insert(DatabaseHelper.agentTypeTable, null,cv);
	
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		close();
	}
	
	public Vector<String> getAgentTypes(){
		openToRead();
		Cursor cursor = database.query(DatabaseHelper.agentTypeTable, new String[] {DatabaseHelper.agentTypeColName}, null, null, null, null, null);
		Vector<String> types = new Vector<String>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			types.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		close();
		return types;
	}
}
