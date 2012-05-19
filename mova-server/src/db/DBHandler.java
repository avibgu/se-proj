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
import state.ActivityState;
import state.ItemState;
import type.AgentType;
import type.ItemType;
import actor.Activity;
import actor.Item;

/**
 * DBHandler is Singleton object that interacts with the system's database.
 * It is referenced by the server's resources.
 * It is responsible to hold the information of all the agents, items and activities in the system
 */
public class DBHandler {
	
	private static String						mDbURL = "jdbc:derby://localhost:1527/ServerDB;create=true";
    private static String						mAgentTableName = "app.agents";
    private static String						mAgentTypeTableName = "app.agentTypes";
    private static String						mActivityTableName = "app.activities";
    private static String						mActivityTypeTableName = "app.activityTypes";
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
    /**
     * Gets the DBHandler only instance
     * @return the DBHandler only instance
     */
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
    /**
     * Inserts a new Agent Type.
     * @param pName the Agent Type to insert
     */
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
    
	/**
	 * Deletes an Agent Type.
	 * @pre all Agents with the type pNname must be deleted prior to using this method
	 * the type pName must be in the system.
	 * @param pName the Agent Type to delete
	 */
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
    /**
     * Inserts a new Agent.
     * @param pAgentId the agent id
     * @param pType the agent type
     * @param pLoggedIn the agent boolean status
     * @param pRegistrationId the agent c2dm registration id
     */
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
	
    /**
     * Deletes an Agent
     * @pre the id pAgentId must be in the system.
     * @param pAgentId the agent id to delete
     */
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
	
	/**
	 * Gets all the agent ids currently in the system
	 * @return the agent ids currently in the system
	 */
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
	/**
	 * Gets the activity id of the activity the agent is currently participating in
	 * @param pAgentId the agent id of the wanted agent
	 * @return activity id of the activity the agent is currently participating in
	 */
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
	/**
	 * Gets the agents current status: Logged in or Logged out
	 * @pre the id pAgentId must be in the system.
	 * @param pAgentId the agent id to search for
	 * @return true of the agent is logged in, false otherwise
	 */
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
	/**
	 * Gets the agent's c2dm registration id
	 * @param pAgentId the agent id to search for
	 * @return the agent's c2dm registration id
	 */
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
	/**
	 * Changes the agent's status
	 * @pre the id pAgentId must be in the system.
	 * @param pAgentId the agent id to changes its status
	 * @param pLoggedIn the new agent status
	 */
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
	/**
	 * Changes the agent's activity id
	 * @pre the id pAgentId must be in the system.
	 * the activity id pActivityId must be in the system.
	 * @param pAgentId the agent id to change its activity id
	 * @param pActivityId the new activity id
	 */
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
	/**
	 * Changes the agent's registration id
	 * @pre the id pAgentId must be in the system.
	 * @param pAgentId the agent id to change its registration id
	 * @param pRegistrationID the new registration id
	 */
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
	/**
	 * Deletes all agent ids from the system
	 */
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


//----------------------------ActivityTypes Table Handling----------------------------	
    /**
     * Inserts a new activity type
     * @param pName the new activity type to insert
     */
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
    /**
     * Deletes an activity type
     * @param pName the activity type to delete
     */
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
	/**
	 * Gets all the activity types in the system
	 * @return all the activity types in the system
	 */
	public Vector<String> getActivityTypes() {
		
		mRead.lock();
		createConnection();
		
		Vector<String> activityTypes = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			
			ResultSet results = mStmt.executeQuery("select NAME from "
					+ mActivityTypeTableName);

			while (results.next()) {
				String name = results.getString(1);
				activityTypes.add(name);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityTypes - database access error or no activity types in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return activityTypes;
	}
//----------------------------Activities Table Handling----------------------------	
	/**
	 * Inserts a new activity
	 * @param activity the new activity to insert
	 */
	public void insertActivity(Activity activity) {
		
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();
			int estimatedTime = (int) activity.getEstimateTime();
			mStmt.execute("insert into " + mActivityTableName + " values (" + "'"
					+ activity.getId() + "'" + "," + "'" + activity.getName() + "'" + "," + "'" + activity.getDescription() 
					+ "'" + "," + "'" + activity.getType() + "'" + "," + "'" + activity.getState().toString()
					+ "'" + "," + "'" + activity.getStartTime() + "'" + "," + "'" + activity.getEndTime() + 
					"'" + "," + estimatedTime + ")");
			mStmt.close();
			
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertActivity - database access error");
		}
		
		shutdown();
		mWrite.unlock();
		
		insertActivityTypeAgent(activity);
		insertActivityTypeItem(activity);
	}
	/**
	 * Deletes an activity from the system
	 * @pre the id pActivityId must be in the system.
	 * @param pActivityId the id of the activity to delete
	 */
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
	/**
	 * Updates the activity deadline
	 * @pre the id pActivityId must be in the system.
	 * @param pActivityId the id of the activity to update
	 * @param newEndTime the new deadline of the activity
	 */
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
	/**
	 * Updates the activity's state: IN_PROGRESS, COMPLETED or PENDING 
	 * @param pActivityId the id of the activity to update it's state
	 * @param newState the new activity state
	 */
	public void updateActivityState(String pActivityId, String newState) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mActivityTableName + " set ACTIVITY_STATE = " + "'"
					+ newState + "'" + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("updateActivityState - database access error" +
					", no item id in the system: " + pActivityId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Gets all the activity names in the system
	 * @return all the activity names in the system
	 */
	public Vector<String> getActivityNames() {
		
		mRead.lock();
		createConnection();
		
		Vector<String> activityNames = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select NAME from "
					+ mActivityTableName);

			while (results.next()) {
				String name = results.getString(1);
				activityNames.add(name);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityNames - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return activityNames;
	}
	/**
	 * Gets the activity's deadline
	 * @pre the id pActivityId must be in the system.
	 * @param pActivityId the id of the activity to search for
	 * @return the activity's deadline
	 */
	public Timestamp getActivityDeadline(String pActivityId) {
		
		mRead.lock();
		createConnection();
		
		Timestamp deadline = new Timestamp(0);
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select END_TIME from "
					+ mActivityTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			results.next();
			deadline = results.getTimestamp(1);
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityDeadline - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return deadline;
	}
	/**
	 * Gets the activity state
	 * @param pActivityId the id of the activity to search for
	 * @return the activity state
	 */
	public ActivityState getActivityState(String pActivityId) {
		
		mRead.lock();
		createConnection();
		
		ActivityState state = null;
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select ACTIVITY_STATE from "
					+ mActivityTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			results.next();
			String stringState = results.getString(1);
			
			state = ActivityState.valueOf(stringState);
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityState - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return state;
	}
	/**
	 * Gets the activity name
	 * @param pActivityId the activity to search for
	 * @return the activity name
	 */
	public String getActivityName(String pActivityId) {
		
		mRead.lock();
		createConnection();
		
		String name = "";
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select NAME from "
					+ mActivityTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			results.next();
			name = results.getString(1);
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityName - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return name;
	}
	
//----------------------------ActivityTypeAgents Table Handling----------------------------
	private void insertActivityTypeAgent(Activity activity) {
		mWrite.lock();
		createConnection();
		
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
		
		shutdown();
		mWrite.unlock();
	}
	
//----------------------------ActivityTypeItems Table Handling----------------------------	
	private void insertActivityTypeItem(Activity activity) {
		mWrite.lock();
		createConnection();
		
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
		
		shutdown();
		mWrite.unlock();
	}
	
//----------------------------Items Table Handling----------------------------
	/**
	 * Inserts a new item
	 * @param item the new item to insert
	 */
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
	/**
	 * Deletes an item from the system
	 * @pre the id pItemId must be in the system.
	 * @param pItemId the id of the item to delete
	 */
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
	/**
	 * Updates the item state: AVAILABLE, UNAVAILABLE or BUSY
	 * @pre the id pItemId must be in the system.
	 * @param pItemId the id of the item to update its state
	 * @param newState the item state
	 */
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
	/**
	 * Gets the item's state
	 * @pre the id pItemId must be in the system.
	 * @param pItemId the id of the item to search for
	 * @return the item's state
	 */
	public ItemState getItemState(String pItemId) {
		
		mRead.lock();
		createConnection();
		
		String status = "";
		ItemState state = null;
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select ITEM_STATE from "
					+ mItemTableName + " WHERE ITEM_ID = " + "'" + pItemId + "'");
			results.next();
			status = results.getString(1);
			
			state = ItemState.valueOf(status);
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out
					.println("getItemState - database access error or no item id in the system: "
							+ pItemId);
		}
		
		shutdown();
		mRead.unlock();
		
		return state;
	}
	/**
	 * Sets the item with an id of the agent holding/using it
	 * @pre the id pItemId must be in the system.
	 * the id pAgentId must be in the system.
	 * @param pItemId the id of the used item
	 * @param pAgentId the id of the agent using the item
	 */
	public void setItemHolder(String pItemId, String pAgentId) {
		mWrite.lock();
		createConnection();
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mItemTableName + " set AGENT_ID = " + "'"
					+ pAgentId + "'" + " WHERE ITEM_ID = " + "'" + pItemId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("setItemHolder - database access error");
		}
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Gets the id of the agent holding/using the item
	 * @pre the id pItemId must be in the system.
	 * @param pItemId the id of the item to search for
	 * @return the id of the agent holding/using the item
	 */
	public String getItemHolder(String pItemId) {
		
		mRead.lock();
		createConnection();
		
		String agentId = "";
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select AGENT_ID from "
					+ mItemTableName + " WHERE ITEM_ID = " + "'" + pItemId + "'");
			results.next();
			agentId = results.getString(1);
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out
					.println("getItemHolder - database access error or no item id in the system: "
							+ pItemId);
		}
		
		shutdown();
		mRead.unlock();
		
		return agentId;
	}
	/**
	 * Gets all item ids in the system
	 * @return all item ids in the system
	 */
	public Vector<String> getItemIds() {
		
		mRead.lock();
		createConnection();
		
		Vector<String> itemIds = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select ITEM_ID from "
					+ mItemTableName);

			while (results.next()) {
				String id = results.getString(1);
				itemIds.add(id);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getItemIds - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return itemIds;
	}
//----------------------------ItemTypes Table Handling----------------------------	
    /**
     * Inserts a new item type
     * @param pName the new item type
     */
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
	/**
	 * Deletes an item type from the system
	 * @pre the name pName must be in the system.
	 * @param pName the item type to delete
	 */
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
	/**
	 * Gets all the item types in the system
	 * @return all the item types in the system
	 */
	public Vector<String> getItemTypes() {
		
		mRead.lock();
		createConnection();
		
		Vector<String> itemTypes = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			
			ResultSet results = mStmt.executeQuery("select NAME from "
					+ mItemTypeTableName);

			while (results.next()) {
				String name = results.getString(1);
				itemTypes.add(name);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getItemTypes - database access error or no item types in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return itemTypes;
	}
}
