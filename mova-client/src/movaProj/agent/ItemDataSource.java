package movaProj.agent;

import java.util.Vector;

import utilities.Location;
import actor.Item;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ItemDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	public ItemDataSource(Context context) {
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
	
	public void createItem(Item movaItem) {
		ContentValues cv = new ContentValues();
	    cv.put(DatabaseHelper.itemColID, movaItem.getId());
	    cv.put(DatabaseHelper.itemColState, movaItem.getState().toString());
	    cv.put(DatabaseHelper.itemColType, movaItem.getType().toString());
	    cv.put(DatabaseHelper.itemLongitudeCol, movaItem.getLocation().getLongitude());
	    cv.put(DatabaseHelper.itemLatitudeCol, movaItem.getLocation().getLatitude());
	    openToWrite();
	    database.insert(DatabaseHelper.itemTable, null,cv);
	    close();
	}
	
	public void deleteItem(String itemId) {
		openToWrite();
		database.delete(DatabaseHelper.itemTable, DatabaseHelper.itemColID + "=" + itemId, null);
		close();
	}
	
	public void changeItemState(String itemId, String newState){
		ContentValues cv = new ContentValues();
	    cv.put(DatabaseHelper.itemColState, newState);
	    openToWrite();
	   	database.update(DatabaseHelper.itemTable, cv, DatabaseHelper.itemColID + "=" + itemId, null);
	   	close();
	}
	
	public void changeItemLocation(String itemId, Location newLocation){
		ContentValues cv = new ContentValues();
	    cv.put(DatabaseHelper.itemLongitudeCol, newLocation.getLongitude());
	    cv.put(DatabaseHelper.itemLatitudeCol, newLocation.getLatitude());
		openToWrite();
		database.update(DatabaseHelper.itemTable, cv, DatabaseHelper.itemColID + "=" + itemId, null);
		close();
	}
	
	public Location getItemLocation(String itemId){
		openToRead();
		Cursor itemLocationCur = database.query(DatabaseHelper.itemTable, 
					   new String[] {DatabaseHelper.itemLongitudeCol, DatabaseHelper.itemLatitudeCol},
					   				 DatabaseHelper.itemColID + "=" + itemId, null, null, null, null);
		if (itemLocationCur != null){
			itemLocationCur.moveToFirst();
		}
		
		Location ans = new Location(itemLocationCur.getInt(0), itemLocationCur.getInt(1));
		close();
		
		return ans;
	}
	
	public void insertItemTypes(Vector<String> types){
		openToWrite();
		database.delete(DatabaseHelper.itemTypeTable, null, null);
		close();
		for (int i=0; i<types.size() ; ++i){
			ContentValues cv = new ContentValues();
			cv.put(DatabaseHelper.itemTypeColName, types.get(i));
			openToWrite();
		   	database.insert(DatabaseHelper.itemTypeTable, null,cv);
		   	close();
		}
	}
}
