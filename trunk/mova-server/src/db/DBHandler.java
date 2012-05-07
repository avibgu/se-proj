package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

import type.AgentType;
import type.ItemType;

import actor.Activity;
import actor.Item;


public class DBHandler {
	
	private static String						mDbURL = "jdbc:derby://localhost:1527/ServerDB;create=true";
    private static String						mAgentTableName = "app.agents";
    private static String						mAgentTypeTableName = "app.agentTypes";
    private static String						mActivityTableName = "app.activities";
    private static String						mActivityTypeTableName = "app.activitiesTypes";
    private static String						mActivityTypeAgentsTableName = "app.activityTypeAgents";
    private static String						mActivityTypeItemsTableName = "app.activityTypeItems";
    private static String						mItemTableName = "app.items";
    private static String						mItemTypeTableName = "app.itemTypes";
   
    private static Connection					mConn = null;
    private static Statement					mStmt = null;
    
    private static final ReentrantReadWriteLock	mRwl = new ReentrantReadWriteLock();
    private static final Lock					mRead = mRwl.readLock();
    private static final Lock					mWrite = mRwl.writeLock();
    
    private static DBHandler					mDbHandler = null;
    
    private DBHandler(){}
    
	public synchronized static DBHandler getInstance() {

		if (mDbHandler == null)
			mDbHandler = new DBHandler();

		return mDbHandler;
	}

//----------------------------Connection Handling----------------------------
    private static void createConnection(){
        
    	try{
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            mConn = DriverManager.getConnection(mDbURL); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
	private void shutdown() {
		try {
			if (mStmt != null)
				mStmt.close();
			if (mConn != null) {
				DriverManager.getConnection(mDbURL + ";shutdown=true");
				mConn.close();
			}
		} catch (SQLException sqlExcept) {
		}
	}

//----------------------------AgentTypes Table Handling----------------------------    
    public void insertAgentType(String pName){
    	mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mAgentTypeTableName + " values (" + "'" + pName + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertAgentType - database access error");
		}
		
		shutdown();
		mWrite.unlock();
    }
    
	public void deleteAgentType(String pName) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mAgentTypeTableName + " WHERE NAME = "
					+ "'" + pName + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteAgentType - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}

//----------------------------Agents Table Handling---------------------------- 
	public void insertAgent(String pAgentId, String pType, Boolean pLoggedIn,
			String pRegistrationId) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			char logged;
			if (pLoggedIn)
				logged = 't';
			else
				logged = 'f';
			mStmt.execute("insert into " + mAgentTableName + " values (" + "'"
					+ pAgentId + "'" + "," + "'" + pType + "'" + ","
					+ "'" + logged + "'" + "," + "'" + "" + "'" + "," + "'"
					+ pRegistrationId + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertAgent - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void deleteAgent(String pAgentId) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mAgentTableName + " WHERE AGENT_ID = "
					+ "'" + pAgentId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteAgent - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public Vector<String> getAgentIds() {
		
		mRead.lock();
		createConnection();
		
		Vector<String> agentIds = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select AGENT_ID from "
					+ mAgentTableName);

			while (results.next()) {
				String id = results.getString(1);
				agentIds.add(id);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentIds - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return agentIds;
	}
	
	public String getAgentActivityId(String pAgentId) {
		
		mRead.lock();
		createConnection();
		
		String ip = "";
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select ACTIVITY_ID from "
					+ mAgentTableName + " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			results.next();
			ip = results.getString(1);

			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out
					.println("getAgentActivityId - database access error, no activityId or no agent id in the system: "
							+ pAgentId);
		}
		
		shutdown();
		mRead.unlock();
		
		return ip;
	}
	
	public boolean getAgentStatus(String pAgentId) {
		
		mRead.lock();
		createConnection();
		
		String status = "";
		boolean stat = false;
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select AGENT_STATUS from "
					+ mAgentTableName + " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			results.next();
			status = results.getString(1);

			if (status.equals("t"))
				stat = true;

			if (status.equals("f"))
				stat = false;

			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out
					.println("getAgentStatus - database access error or no agent id in the system: "
							+ pAgentId);
		}
		
		shutdown();
		mRead.unlock();
		
		return stat;
	}
	
	public String getAgentRegistrationId(String pAgentId) {
		
		mRead.lock();
		createConnection();
		
		String regristrationId = "";
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt
					.executeQuery("select REGISTRATION_ID from " + mAgentTableName
							+ " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			results.next();
			regristrationId = results.getString(1);

			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentRegistrationId - database access error" +
					", no registrationId, or no agent id in the system: " + pAgentId);
		}
		
		shutdown();
		mRead.unlock();
		
		return regristrationId;
	}

	public void changeAgentStatus(String pAgentId, Boolean pLoggedIn) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			char logged;
			if (pLoggedIn)
				logged = 't';
			else
				logged = 'f';
			mStmt.execute("update " + mAgentTableName + " set AGENT_STATUS = " + "'"
					+ logged + "'" + " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("changeAgentStatus - database access error" +
					", no agent id in the system: " + pAgentId);
		}
		
		shutdown();
		mWrite.unlock();
	}

	public void changeAgentActivityId(String pAgentId, String pActivityId) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mAgentTableName + " set ACTIVITY_ID = " + "'" + pActivityId
					+ "'" + " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("changeAgentActivityId - database access error" +
					", no agent id or no activity id in the system: " + pAgentId + ", " + pActivityId);
		}
		shutdown();
		mWrite.unlock();
	}

	public void changeAgentRegistrationID(String pAgentId, String pRegistrationID) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mAgentTableName + " set REGISTRATION_ID = "
					+ "'" + pRegistrationID + "'" + " WHERE AGENT_ID = " + "'"
					+ pAgentId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("changeAgentRegistrationID - database access error" +
					", no agent id in the system: " + pAgentId);
		}
		
		shutdown();
		mWrite.unlock();
	}

	public void deleteAllAgents() {
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mAgentTableName);
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteAllAgents - database access error");
		}
		shutdown();
		mWrite.unlock();
	}
	
	public void setCurrentActivity(String pAgentId, String pActivityId) {
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mAgentTableName + " set ACTIVITY_ID = " + "'"
					+ pActivityId + "'" + " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("setCurrentActivity - database access error");
		}
		shutdown();
		mWrite.unlock();
	}
	

//----------------------------ActivityTypes Table Handling----------------------------	
    public void insertActivityType(String pName){
    	mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mActivityTypeTableName + " values (" + "'" + pName + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertActivityType - database access error");
		}
		
		shutdown();
		mWrite.unlock();
    }
    
	public void deleteActivityType(String pName) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mActivityTypeTableName + " WHERE NAME = "
					+ "'" + pName + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteActivityType - database access error " +
					", no activity type in the system: " + pName);
		}
		
		shutdown();
		mWrite.unlock();
	}
//----------------------------Activities Table Handling----------------------------	
	
	public void insertActivity(Activity activity) {
		
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();

			mStmt.execute("insert into " + mActivityTableName + " values (" + "'"
					+ activity.getId() + "'" + "," + "'" + activity.getName() + "'" + "," + "'" + activity.getDescription() 
					+ "'" + "," + "'" + activity.getType() + "'" + "," + activity.getStartTime() + "," + activity.getEndTime() + 
					"," + activity.getEstimateTime() + ")");
			mStmt.close();
			
			insertActivityTypeAgent(activity);
			insertActivityTypeItem(activity);
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertActivity - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void deleteActivity(String pActivityId) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mActivityTableName + " WHERE ACTIVITY_ID = "
					+ "'" + pActivityId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteActivity - database access error" +
					", no activity id in the system: " + pActivityId);
		}
		
		shutdown();
		mWrite.unlock();
	}

	public void updateActivityDeadline(String pActivityId, Timestamp newEndTime) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mActivityTableName + " set END_TIME = " + "'"
					+ newEndTime + "'" + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("updateActivityDeadline - database access error" +
					", no activity id in the system: " + pActivityId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	
//----------------------------ActivityTypeAgents Table Handling----------------------------
	private void insertActivityTypeAgent(Activity activity) {
		Map<AgentType, Integer> requiredAgents = activity.getRequiredAgents();
		for (Map.Entry<AgentType, Integer> requiredAgent : requiredAgents.entrySet()) {
			try {
				mStmt = mConn.createStatement();

				mStmt.execute("insert into " + mActivityTypeAgentsTableName + " values (" + "'"
						+ activity.getId() + "'" + "," + "'" + requiredAgent.getKey().getType() + "'" + "," 
						+ requiredAgent.getValue().intValue() + ")");
				mStmt.close();
			} catch (SQLIntegrityConstraintViolationException e) {
				System.out.println(e.getMessage());
			} catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
			}
		}
	}
	
//----------------------------ActivityTypeItems Table Handling----------------------------	
	private void insertActivityTypeItem(Activity activity) {
		Map<ItemType, Integer> requiredItems = activity.getRequiredItems();
		for (Map.Entry<ItemType, Integer> requiredItem : requiredItems.entrySet()) {
			try {
				mStmt = mConn.createStatement();

				mStmt.execute("insert into " + mActivityTypeItemsTableName + " values (" + "'"
						+ activity.getId() + "'" + "," + "'" + requiredItem.getKey().getType() + "'" + "," 
						+ requiredItem.getValue().intValue() + ")");
				mStmt.close();
			} catch (SQLIntegrityConstraintViolationException e) {
				System.out.println(e.getMessage());
			} catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
			}
		}
	}
	
//----------------------------Items Table Handling----------------------------
	public void insertItem(Item item) {
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();

			mStmt.execute("insert into " + mItemTableName + " values (" + "'"
					+ item.getId() + "'" + "," + "'" + item.getType().getType() + "'" + "," + "'" 
					+ item.getState().toString() + "'" + "," + "'" + "" + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertItem - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void deleteItem(String pItemId) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mItemTableName + " WHERE ITEM_ID = "
					+ "'" + pItemId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteItem - database access error" +
					", no item id in the system: " + pItemId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void updateItemState(String pItemId, String newState) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mItemTableName + " set ITEM_STATE = " + "'"
					+ newState + "'" + " WHERE ITEM_ID = " + "'" + pItemId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("updateItemState - database access error" +
					", no item id in the system: " + pItemId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void setItemHolder(String pItemId, String pAgentId) {
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mItemTableName + " set AGENT_ID = " + "'"
					+ pAgentId + "'" + " WHERE ITEM_ID = " + "'" + pItemId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("setCurrentActivity - database access error");
		}
		shutdown();
		mWrite.unlock();
	}
//----------------------------ItemTypes Table Handling----------------------------	
    public void insertItemType(String pName){
    	mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mItemTypeTableName + " values (" + "'" + pName + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertItemType - database access error");
		}
		
		shutdown();
		mWrite.unlock();
    }
    
	public void deleteItemType(String pName) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mItemTypeTableName + " WHERE NAME = "
					+ "'" + pName + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteItemType - database access error " +
					", no item type in the system: " + pName);
		}
		
		shutdown();
		mWrite.unlock();
	}
}
