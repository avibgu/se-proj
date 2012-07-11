package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import state.ActivityState;
import state.ItemState;
import type.ActivityType;
import type.AgentType;
import type.ItemType;
import utilities.Location;
import actor.Activity;
import actor.Agent;
import actor.Item;

/**
 * DBHandler is Singleton object that interacts with the system's database.
 * It is referenced by the server's resources.
 * It is responsible to hold the information of all the agents, items and activities in the system
 */
public class DBHandler {
	
//	private static String						mDbURL = "jdbc:derby://localhost:1527/ServerDB;create=true";
//    private static String						mAgentTableName = "app.agents";
//    private static String						mAgentTypeTableName = "app.agentTypes";
//    private static String						mActivityTableName = "app.activities";
//    private static String						mActivityTypeTableName = "app.activityTypes";
//    private static String						mActivityTypeAgentsTableName = "app.activityTypeAgents";
//    private static String						mActivityTypeItemsTableName = "app.activityTypeItems";
//    private static String						mItemTableName = "app.items";
//    private static String						mItemTypeTableName = "app.itemTypes";
//    private static String						mActivityAgentsTableName = "app.activityAgents";
//    private static String						mActivityItemsTableName = "app.activityItems";
//    private static String						mAgentLocationsTableName = "app.agentLocations";
//    private static String						mItemLocationsTableName = "app.itemLocations";
    
    private static String						mDbURL = "jdbc:mysql://localhost:3306/mysqldatabase";
    private static String						mAgentTableName = "agents";
    private static String						mAgentTypeTableName = "agentTypes";
    private static String						mActivityTableName = "activities";
    private static String						mActivityTypeTableName = "activityTypes";
    private static String						mActivityTypeAgentsTableName = "activityTypeAgents";
    private static String						mActivityTypeItemsTableName = "activityTypeItems";
    private static String						mItemTableName = "items";
    private static String						mItemTypeTableName = "itemTypes";
    private static String						mActivityAgentsTableName = "activityAgents";
    private static String						mActivityItemsTableName = "activityItems";
    private static String						mAgentLocationsTableName = "agentLocations";
    private static String						mItemLocationsTableName = "itemLocations";
    private static String						mC2dmTable = "c2dm";
   
    private static Connection					mConn = null;
    private static Statement					mStmt = null;
    
    private static final ReentrantReadWriteLock	mRwl = new ReentrantReadWriteLock();
    private static final Lock					mRead = mRwl.readLock();
    private static final Lock					mWrite = mRwl.writeLock();
    private static final Lock 					mWriteLock2 = new ReentrantReadWriteLock().writeLock();
    
    private static Boolean[]					isRecalculating = new Boolean[]{false};
    
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
    		mWriteLock2.lock();
            //Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Get a connection
            mConn = DriverManager.getConnection(mDbURL, "mova", "mova"); 
            mWriteLock2.unlock();
        }
        catch (Exception e){
            e.printStackTrace();
            mWriteLock2.unlock();
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
	/**
	 * Deletes all database data, not the tables
	 */
	public void deleteData(){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mAgentTableName);
			mStmt.execute("delete from " + mActivityTableName);
			mStmt.execute("delete from " + mItemTableName);
			mStmt.execute("delete from " + mItemTypeTableName);
			mStmt.execute("delete from " + mActivityTypeTableName);
			mStmt.execute("delete from " + mAgentTypeTableName);
			mStmt.execute("delete from " + mActivityTypeItemsTableName);
			mStmt.execute("delete from " + mActivityTypeAgentsTableName);
			mStmt.execute("delete from " + mActivityAgentsTableName);
			mStmt.execute("delete from " + mActivityItemsTableName);
			mStmt.execute("delete from " + mAgentLocationsTableName);
			mStmt.execute("delete from " + mItemLocationsTableName);
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("deleteData - database access error");
		}
		
		shutdown();
		mWrite.unlock();
		System.out.println("Deleted Database data");
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
	/**
	 * Returns all the agent types in the system
	 * @return all the agent types in the system
	 */
	public Vector<String> getAgentTypes() {
		
		mRead.lock();
		createConnection();
		
		Vector<String> agentTypes = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select NAME from "
					+ mAgentTypeTableName);

			while (results.next()) {
				String name = results.getString(1);
				agentTypes.add(name);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentTypes - database access error or no agent types in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return agentTypes;
	}
	
//----------------------------Agents Table Handling---------------------------- 
    /**
     * Inserts a new Agent.
     * @param pAgentId the agent id
     * @param pType the agent type
     * @param pLoggedIn the agent boolean status
     * @param pRegistrationId the agent c2dm registration id
     */
    public boolean insertAgent(String pAgentId, String pType, Boolean pLoggedIn,
			String pRegistrationId) {
		
    	boolean ans = true;
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
			ans=false;
		} catch (SQLException sqlExcept) {
			System.out.println("insertAgent - database access error");
			ans=false;
		}
		
		shutdown();
		mWrite.unlock();
		return ans;
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
	 * Returns the type of the agent
	 * @param pAgentId the agent id to search for
	 * @return the agent type of the agent
	 */
	public String getAgentType(String pAgentId) {
		
		mRead.lock();
		createConnection();
		
		String type = "";
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt
					.executeQuery("select AGENT_TYPE from " + mAgentTableName
							+ " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			results.next();
			type = results.getString(1);

			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentType - database access error" +
					", no agent type, or no agent id in the system: " + pAgentId);
		}
		
		shutdown();
		mRead.unlock();
		
		return type;
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
	
	/**
	 * Returns a list of all agents
	 * It returns a list of all agents with the data: id, type and registration id only!
	 * @return a list of all agents
	 */
	public List<Agent> getAllAgents() {

		mRead.lock();
		createConnection();
		
		List<Agent> agents = new ArrayList<Agent>();
		
		try {
			mStmt = mConn.createStatement();

			ResultSet results = mStmt.executeQuery("select * from "
					+ mAgentTableName + " WHERE agent_status = 't'");

			while (results.next()) {

				Agent agent = new Agent(new AgentType(results.getString("agent_type")));
				agent.setId(results.getString("agent_id"));
				agent.setRegistrationId(results.getString("registration_id"));
				//TODO
				//agent.setCurrentActivityId(Integer.valueOf(results.getString("activity_id")));
				agents.add(agent);
			}
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAllAgents - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return agents;
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
					"'" + "," + estimatedTime + "," + "'" + activity.getActualStartTime() + "'" + "," + "'" 
					+ activity.getActualEndTime() + "'" + ")");
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
		for (String participatingAgentId : activity.getParticipatingAgentIds()) {
			insertActivityAgent(activity.getId(), participatingAgentId);
		}
		for (String participatingItemId : activity.getParticipatingItemIds()) {
			insertActivityItem(activity.getId(), participatingItemId);
		}
		
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
			mStmt.execute("update " + mActivityTableName + " set ACTUAL_END_TIME = " + "'"
					+ newEndTime + "'" + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("updateActivityDeadline - database access error" +
					", no activity id in the system: " + pActivityId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void updateActivityDuration(String pActivityId, long newDuration) {
		
		mWrite.lock();
		createConnection();
		
		try {
			int duration = (int)newDuration;
			mStmt = mConn.createStatement();
			mStmt.execute("update " + mActivityTableName + " set ESTIMATE_TIME = "
					+ duration + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");
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
			ResultSet results = mStmt.executeQuery("select ACTUAL_END_TIME from "
					+ mActivityTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			results.next();
			deadline = results.getTimestamp("ACTUAL_END_TIME");
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityDeadline - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return deadline;
	}
	
	public int getActivityDuration(String pActivityId) {
		
		mRead.lock();
		createConnection();
		
		int ans=0;	
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select ESTIMATE_TIME from "
					+ mActivityTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			results.next();
			ans = results.getInt("ESTIMATE_TIME");
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityDuration - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return ans;
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
	
	public Map<AgentType, Integer> getActivityTypeAgents(String pActivityId) {
		
		mRead.lock();
		createConnection();
		
		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from "
					+ mActivityTypeAgentsTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			while (results.next()) {
				String agentType = results.getString("AGENT_TYPE");
				int numberOfAgents = results.getInt("NUMBER_OF_AGENTS");
				requiredAgents.put(new AgentType(agentType), numberOfAgents);
			}
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityTypeAgents - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return requiredAgents;
	}
	
	public Map<ItemType, Integer> getActivityTypeItems(String pActivityId) {
		
		mRead.lock();
		createConnection();
		
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from "
					+ mActivityTypeItemsTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			while (results.next()) {
				String itemType = results.getString("ITEM_TYPE");
				int numberOfItems = results.getInt("NUMBER_OF_ITEMS");
				requiredItems.put(new ItemType(itemType), numberOfItems);
			}
			
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityTypeAgents - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return requiredItems;
	}
	
	public List<Activity> getAllActivities() {
		
		mRead.lock();
		createConnection();
		
		List<Activity> activities = new ArrayList<Activity>();
		
		try {
			
			mStmt = mConn.createStatement();
//			ResultSet results = mStmt.executeQuery("select * from "
//					+ mActivityTableName + "," + mActivityAgentsTableName +"," + mActivityItemsTableName +
//					" where " + mActivityTableName+".ACTIVITY_ID = " + mActivityAgentsTableName+".ACTIVITY_ID AND "
//					+ mActivityTableName+".ACTIVITY_ID = " + mActivityItemsTableName+".ACTIVITY_ID ");
			
			ResultSet results = mStmt.executeQuery("select * from "
					+ mActivityTableName + " where ACTIVITY_STATE <> 'COMPLETED'"  );
			
			
			while (results.next()) {
				
				String activityId = results.getString("ACTIVITY_ID");
				String name = results.getString("NAME");
				String description = results.getString("DESCRIPTION");
				String activityType = results.getString("ACTIVITY_TYPE");
				String activityState = results.getString("ACTIVITY_STATE");
				Timestamp startTime = results.getTimestamp("START_TIME");
				Timestamp endTime = results.getTimestamp("END_TIME");
				int estimatedTime = results.getInt("ESTIMATE_TIME");
				Timestamp actualStartTime = results.getTimestamp("ACTUAL_START_TIME");
				Timestamp actualEndTime = results.getTimestamp("ACTUAL_END_TIME");
				Activity activity = new Activity(name);
				activity.setId(activityId);
				activity.setDescription(description);
				activity.setType(activityType);
				activity.setState(ActivityState.valueOf(activityState));
				activity.setStartTime(startTime);
				activity.setEndTime(endTime);
				activity.setEstimateTime(estimatedTime);
				activity.setActualStartTime(actualStartTime);
				activity.setActualEndTime(actualEndTime);
				activity.setRequiredAgents(getActivityTypeAgents(activity.getId()));
				activity.setRequiredItems(getActivityTypeItems(activity.getId()));
				
				activities.add(activity);
			}

			results.close();
			mStmt.close();
		}
		
		catch (SQLException sqlExcept) {
			System.out.println("getAllActivities - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return activities;
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
						+ activity.getId() + "'" + "," + "'" + requiredAgent.getKey().toString() + "'" + "," 
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
					+ item.getState().toString() + "'" + "," + "'" + " " + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertItem - database access error");
		}
		
		shutdown();
		mWrite.unlock();
		
		insertItemLocation(item.getId(), item.getLocation());
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
	 * Deletes the auth key in c2dm table
	 */
	public void deleteAuthKey() {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mC2dmTable );
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteC2dm - database access error" + sqlExcept.getMessage());
					
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
	public void updateItemState(String pItemId, String pNewState, String pAgentId) {
		
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			String id = " ";
			if(pNewState.equals("BUSY")){
				id = pAgentId;
			}
			mStmt.execute("update " + mItemTableName + " set ITEM_STATE = " + "'"
					+ pNewState + "'" + " , AGENT_ID = " + "'" + id + "'" + " WHERE ITEM_ID = " + "'" + pItemId + "'");
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
	/**
	 * Returns all items that the agent holds
	 * @param pAgentId the agent id to search for
	 * @return all items that the agent holds
	 */
	public Vector<Item> getAgentItems(String pAgentId) {
		
		mRead.lock();
		createConnection();
		
		Vector<Item> agentItems = new Vector<Item>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from "
					+ mItemTableName + " WHERE AGENT_ID = " + "'" + pAgentId + "'");

			while (results.next()) {
				String itemId = results.getString("ITEM_ID");
				String itemType = results.getString("ITEM_TYPE");
				String itemState = results.getString("ITEM_STATE");
				//String agentId = results.getString("AGENT_ID");
				Item item = new Item(new ItemType(itemType));
				item.setId(itemId);
				item.setState(ItemState.valueOf(itemState));
				item.setRepresentation(itemType);
				agentItems.add(item);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentItems - database access error or no agent in database: " + pAgentId);
		}
		
		shutdown();
		mRead.unlock();
		
		return agentItems;
	}
	/**
	 * Returns all items in the system
	 * @return all items in the system
	 */
	public Vector<Item> getItems() {
		
		mRead.lock();
		createConnection();
		
		Vector<Item> items = new Vector<Item>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from " + mItemTableName);

			while (results.next()) {
				String itemId = results.getString("ITEM_ID");
				String itemType = results.getString("ITEM_TYPE");
				String itemState = results.getString("ITEM_STATE");
				//String agentId = results.getString("AGENT_ID");
				Item item = new Item(new ItemType(itemType));
				item.setId(itemId);
				item.setState(ItemState.valueOf(itemState));
				item.setRepresentation(itemType);
				items.add(item);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getItems - database access error");
		}
		
		shutdown();
		mRead.unlock();
		
		for (Item item : items) {
			item.setLocation(getItemLocation(item.getId()));
		}
		return items;
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
	
//	----------------------------ActivityAgents Table Handling----------------------------
	/**
	 * Adds an agent to the activity agent list
	 * @param pActivityId the activity id
	 * @param pAgentId the agent id to add
	 */
	public void insertActivityAgent(String pActivityId, String pAgentId){
    	mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mActivityAgentsTableName + " values (" + "'" + pActivityId + "'" + "," + "'" + pAgentId + "'"+ ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertActivityAgent - database access error");
		}
		
		shutdown();
		mWrite.unlock();
    }
	/**
	 * Deletes an agent from the activity agent list
	 * @param pActivityId the activity id
	 * @param pAgentId the agent id to remove
	 */
	public void deleteActivityAgent(String pActivityId, String pAgentId){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mActivityAgentsTableName + " WHERE ACTIVITY_ID = "
					+ "'" + pActivityId + "'" + " AND " + "AGENT_ID = " + "'" + pAgentId + "'");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("deleteActivityAgent - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Returns all agent ids relevant to the given activity
	 * @param pActivityId the activity id to search for
	 * @return all agent ids relevant to the given activity
	 */
	public Vector<String> getActivityAgentIds(String pActivityId){
		mRead.lock();
		createConnection();
		
		Vector<String> activityAgentIds = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select AGENT_ID from "
					+ mActivityAgentsTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			while (results.next()) {
				String id = results.getString(1);
				activityAgentIds.add(id);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityAgentIds - database access error or no agents in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return activityAgentIds;
	}
	/**
	 * Returns the activity schedule of the given agent id
	 * @param pAgentId the agent id to search for
	 * @return the activity schedule of the given agent id
	 */
	public Vector<Activity> getAgentSchedule(String pAgentId){
		mRead.lock();
		createConnection();
		
		Vector<Activity> schedule = new Vector<Activity>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from "
					+ mActivityAgentsTableName + "," + mActivityTableName 
					+ " WHERE " + mActivityAgentsTableName + ".ACTIVITY_ID = " 
					+ mActivityTableName + ".ACTIVITY_ID AND " 
					+ mActivityAgentsTableName + ".AGENT_ID = " + "'" + pAgentId + "'"
					+ " ORDER BY ACTUAL_START_TIME ASC");

			while (results.next()) {
				String activityId = results.getString("ACTIVITY_ID");
				String name = results.getString("NAME");
				String description = results.getString("DESCRIPTION");
				String activityType = results.getString("ACTIVITY_TYPE");
				String activityState = results.getString("ACTIVITY_STATE");
				Timestamp startTime = results.getTimestamp("START_TIME");
				Timestamp endTime = results.getTimestamp("END_TIME");
				int estimatedTime = results.getInt("ESTIMATE_TIME");
				Timestamp actualStartTime = results.getTimestamp("ACTUAL_START_TIME");
				Timestamp actualEndTime = results.getTimestamp("ACTUAL_END_TIME");
				Activity activity = new Activity(name);
				activity.setId(activityId);
				activity.setDescription(description);
				activity.setType(activityType);
				activity.setState(ActivityState.valueOf(activityState));
				activity.setStartTime(startTime);
				activity.setEndTime(endTime);
				activity.setEstimateTime(estimatedTime);
				activity.setActualStartTime(actualStartTime);
				activity.setActualEndTime(actualEndTime);
				
				schedule.add(activity);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentSchedule - database access error or no agent id in database");
		}
		
		shutdown();
		mRead.unlock();
		for (Activity activity : schedule) {
			Vector<String> activityAgentIds = getActivityAgentIds(activity.getId());
			Vector<String> activityItemsIds = getActivityItemIds(activity.getId());
			activity.setParticipatingAgentIds(new HashSet<String>(activityAgentIds));
			activity.setParticipatingItemIds(new HashSet<String>(activityItemsIds));
		}
		return schedule;
	}
	
//	----------------------------ActivityItems Table Handling----------------------------
	/**
	 * Inserts a new item that is used in the activity
	 * @param pActivityId the activity id
	 * @param pItemId the item id inserted
	 */
	public void insertActivityItem(String pActivityId, String pItemId){
    	mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mActivityItemsTableName + " values (" + "'" + pActivityId + "'" + "," + "'" + pItemId + "'"+ ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertActivityItem - database access error");
		}
		
		shutdown();
		mWrite.unlock();
    }
	/**
	 * Deletes an existing item from the activity
	 * @param pActivityId the activity id
	 * @param pItemId the item to delete
	 */
	public void deleteActivityItem(String pActivityId, String pItemId){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mActivityItemsTableName + " WHERE ACTIVITY_ID = "
					+ "'" + pActivityId + "'" + " AND " + "ITEM_ID = " + "'" + pItemId + "'");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("deleteActivityItem - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Returns all item ids relevant to the given activity id
	 * @param pActivityId the activity id
	 * @return all item ids relevant to the given activity id
	 */
	public Vector<String> getActivityItemIds(String pActivityId){
		mRead.lock();
		createConnection();
		
		Vector<String> activityItemIds = new Vector<String>();
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select ITEM_ID from "
					+ mActivityItemsTableName + " WHERE ACTIVITY_ID = " + "'" + pActivityId + "'");

			while (results.next()) {
				String id = results.getString(1);
				activityItemIds.add(id);
			}
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getActivityItemIds - database access error or no items in database");
		}
		
		shutdown();
		mRead.unlock();
		
		return activityItemIds;
	}
	
//	----------------------------AgentLocations Table Handling----------------------------
	/**
	 * Inserts the location of an agent
	 * @param pAgentId the agent id
	 * @param location the agent's location
	 */
	public void insertAgentLocation(String pAgentId, Location location){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mAgentLocationsTableName + " values (" + "'" + pAgentId + "'" + "," + location.getLatitude() 
					+ "," + location.getLongitude() + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertAgentLocation - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Updates the agent's location
	 * @param pAgentId the agent id
	 * @param newLocation the new location of the agent
	 */
	public void updateAgentLocation(String pAgentId, Location newLocation){
		mWrite.lock();
		createConnection();
		if(newLocation != null){
			try {
				mStmt = mConn.createStatement();
				mStmt.execute("update " + mAgentLocationsTableName + " set LATITUDE = " + newLocation.getLatitude()
						+ " , " + "LONGITUDE = " + newLocation.getLongitude() +  " WHERE AGENT_ID = " + "'" + pAgentId + "'");
				mStmt.close();
			} catch (SQLException sqlExcept) {
				System.out.println("updateAgentLocation - database access error" +
						", no agent id in the system: " + pAgentId);
			}
		}
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Deletes an existing location of an agent
	 * @param pAgentId the agent id
	 */
	public void deleteAgentLocation(String pAgentId){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mAgentLocationsTableName + " WHERE AGENT_ID = "
					+ "'" + pAgentId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteAgentLocation - database access error" +
					", no agent id in the system: " + pAgentId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Returns the location of the given agent id
	 * @param pAgentId the agent id
	 * @return the location of the given agent id
	 */
	public Location getAgentLocation(String pAgentId){
		mRead.lock();
		createConnection();
		
		Location location = null;
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from "
					+ mAgentLocationsTableName + " WHERE AGENT_ID = " + "'" + pAgentId + "'");
			results.next();
			int latitude = results.getInt("LATITUDE");
			int longitude = results.getInt("LONGITUDE");

			location = new Location(longitude, latitude);

			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out
					.println("getAgentLocation - database access error or no agent id in the system: "
							+ pAgentId);
		}
		
		shutdown();
		mRead.unlock();
		
		return location;
	}
//	----------------------------ItemLocations Table Handling----------------------------
	/**
	 * Inserts the location of an item
	 * @param pItemId the item id
	 * @param location the item's location
	 */
	public void insertItemLocation(String pItemId, Location location){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mItemLocationsTableName + " values (" + "'" + pItemId + "'" + "," + location.getLatitude() 
					+ "," + location.getLongitude() + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertItemLocation - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Updates the item's location
	 * @param pItemId the item id
	 * @param newLocation the new location of the item
	 */
	public void updateItemLocation(String pItemId, Location newLocation){
		mWrite.lock();
		createConnection();
		if(newLocation != null){
			try {
				mStmt = mConn.createStatement();
				mStmt.execute("update " + mItemLocationsTableName + " set LATITUDE = " + newLocation.getLatitude()
						+ " , " + "LONGITUDE = " + newLocation.getLongitude() +  " WHERE ITEM_ID = " + "'" + pItemId + "'");
				mStmt.close();
			} catch (SQLException sqlExcept) {
				System.out.println("updateItemLocation - database access error" +
						", no item id in the system: " + pItemId);
			}
		}
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Deletes an existing location of an item
	 * @param pItemId the item id
	 */
	public void deleteItemLocation(String pItemId){
		mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("delete from " + mItemLocationsTableName + " WHERE ITEM_ID = "
					+ "'" + pItemId + "'");
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("deleteItemLocation - database access error" +
					", no agent id in the system: " + pItemId);
		}
		
		shutdown();
		mWrite.unlock();
	}
	/**
	 * Returns the location of the given item id
	 * @param pItemId the item id
	 * @return the location of the given item id
	 */
	public Location getItemLocation(String pItemId){
		mRead.lock();
		createConnection();
		
		Location location = null;
		
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select * from "
					+ mItemLocationsTableName + " WHERE ITEM_ID = " + "'" + pItemId + "'");
			results.next();
			int latitude = results.getInt("LATITUDE");
			int longitude = results.getInt("LONGITUDE");

			location = new Location(longitude, latitude);

			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out
					.println("getItemLocation - database access error or no item id in the system: "
							+ pItemId);
		}
		
		shutdown();
		mRead.unlock();
		
		return location;
	}
	public static Boolean canStartNewRecalculte() {

		synchronized (isRecalculating) {
		
			if (!isRecalculating[0]){
				
				isRecalculating[0] = true;
				return true;
			}
			
			else return false;
		}		
	}
	public static void finishRecalculte() {
	
		synchronized (isRecalculating) {
		
			isRecalculating[0] = false;
		}
	}
	
	public String getAuthKey(){
		mRead.lock();
		createConnection();
		String auth_key = null;
		try {
			mStmt = mConn.createStatement();
			ResultSet results = mStmt.executeQuery("select AUTH_KEY from " + mC2dmTable);

			results.next();
			if (results.getRow() == 0)
				return null;
			auth_key = results.getString(1);
			results.close();
			mStmt.close();
		} catch (SQLException sqlExcept) {
			System.out.println("getAgentTypes - database access error or no agent types in database");
		}
		
		shutdown();
		mRead.unlock();
		return auth_key;
	}
	
	public void insertAuthKey(String auth_key){
	 	mWrite.lock();
		createConnection();
		
		try {
			mStmt = mConn.createStatement();
			mStmt.execute("insert into " + mC2dmTable + " values (" + "'" + auth_key + "'" + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			System.out.println("insertAgentType - database access error");
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	
	public void insertInitialData(){
		Date date = new Date();
		Timestamp startTime = new Timestamp(date.getTime());
		Timestamp endTime = new Timestamp(startTime.getTime() + 1000*60*60*7);
		long estimateTime = 1000*60*60;
		
		// Conference Regsitration
		Map<AgentType, Integer> requiredAgents = new HashMap<AgentType, Integer>();
		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
//		requiredAgents.put(new AgentType(AgentType.SECRETARY), 1);
//		requiredAgents.put(new AgentType(AgentType.SECURITY_OFFICER), 1);
		Map<ItemType, Integer> requiredItems = new HashMap<ItemType, Integer>();
		Set<String> requiredActivities = new HashSet<String>();
		String description = "Registration";
		String name = "Conference Regsitration";
		
		Activity a1 = new Activity(ActivityType.CONFERENCE_REGISTRATION, startTime,
				endTime, estimateTime, requiredAgents, requiredItems, 
				requiredActivities, description, name);
		
		insertAgentType(AgentType.COORDINATOR);
//		insertAgentType(AgentType.SECRETARY);
//		insertAgentType(AgentType.SECURITY_OFFICER);
		insertActivityType(ActivityType.CONFERENCE_REGISTRATION);
		insertActivity(a1);
		
		// Presentation 1
		requiredAgents = new HashMap<AgentType, Integer>();
		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
//		requiredAgents.put(new AgentType(AgentType.LECTURER), 1);
		requiredItems = new HashMap<ItemType, Integer>();
		requiredItems.put(new ItemType(ItemType.LAPTOP), 1);
		requiredActivities = new HashSet<String>();
		requiredActivities.add(a1.getId());
		description = "Presentation 1";
		name = "Presentation 1";
		
		Activity a2 = new Activity(ActivityType.PRESENTATION, startTime,
				endTime, estimateTime, requiredAgents, requiredItems, 
				requiredActivities, description, name);
		
//		insertAgentType(AgentType.LECTURER);
		insertActivityType(ActivityType.PRESENTATION);
		insertActivity(a2);
				
		// Lunch
		requiredAgents = new HashMap<AgentType, Integer>();
//		requiredAgents.put(new AgentType(AgentType.LOGISTIC_MANAGER), 1);
		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
		requiredItems = new HashMap<ItemType, Integer>();
		requiredActivities = new HashSet<String>();
		requiredActivities.add(a1.getId());
		requiredActivities.add(a2.getId());
		description = "Lunch";
		name = "Lunch";
		
		Activity a3 = new Activity(ActivityType.LUNCH, startTime,
				endTime, estimateTime, requiredAgents, requiredItems, 
				requiredActivities, description, name);
		
//		insertAgentType(AgentType.LOGISTIC_MANAGER);
		insertActivityType(ActivityType.LUNCH);
		insertActivity(a3);
		
		// Discussion 1
		requiredAgents = new HashMap<AgentType, Integer>();
		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
//		requiredAgents.put(new AgentType(AgentType.LECTURER), 1);
		requiredItems = new HashMap<ItemType, Integer>();
		requiredActivities = new HashSet<String>();
		requiredActivities.add(a1.getId());
		description = "Discussion 1";
		name = "Discussion 1";
		
//		Activity a4 = new Activity(ActivityType.DISCUSSION, startTime,
//				endTime, estimateTime, requiredAgents, requiredItems, 
//				requiredActivities, description, name);
//		
//		insertActivityType(ActivityType.DISCUSSION);
//		insertActivity(a4);
//		
//		// Presentation 2
//		requiredAgents = new HashMap<AgentType, Integer>();
//		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
//		requiredAgents.put(new AgentType(AgentType.LECTURER), 1);
//		requiredItems = new HashMap<ItemType, Integer>();
//		requiredItems.put(new ItemType(ItemType.LAPTOP), 1);
//		requiredItems.put(new ItemType(ItemType.LAZER_CURSOR), 1);
//		requiredItems.put(new ItemType(ItemType.MOUSE), 1);
//		requiredActivities = new HashSet<String>();
//		requiredActivities.add(a1.getId());
//		requiredActivities.add(a2.getId());
//		description = "Presentation 2";
//		name = "Presentation 2";
//		
//		Activity a5 = new Activity(ActivityType.PRESENTATION, startTime,
//				endTime, estimateTime, requiredAgents, requiredItems, 
//				requiredActivities, description, name);
//		
//		insertActivity(a5);
//		
//		// Discussion 2
//		requiredAgents = new HashMap<AgentType, Integer>();
//		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
//		requiredAgents.put(new AgentType(AgentType.LECTURER), 1);
//		requiredItems = new HashMap<ItemType, Integer>();
//		requiredActivities = new HashSet<String>();
//		requiredActivities.add(a1.getId());
//		requiredActivities.add(a4.getId());
//		description = "Discussion 2";
//		name = "Discussion 2";
//		
//		Activity a6 = new Activity(ActivityType.DISCUSSION, startTime,
//				endTime, estimateTime, requiredAgents, requiredItems, 
//				requiredActivities, description, name);
//		
//		insertActivity(a6);
//		
//		// Tour
//		requiredAgents = new HashMap<AgentType, Integer>();
//		requiredAgents.put(new AgentType(AgentType.COORDINATOR), 1);
//		requiredAgents.put(new AgentType(AgentType.SECURITY_OFFICER), 1);
//		requiredAgents.put(new AgentType(AgentType.LOGISTIC_MANAGER), 1);
//		requiredItems = new HashMap<ItemType, Integer>();
//		requiredActivities = new HashSet<String>();
//		requiredActivities.add(a1.getId());
//		requiredActivities.add(a2.getId());
//		description = "Tour";
//		name = "Tour";
//		
//		Activity a7 = new Activity(ActivityType.TOUR, startTime,
//				endTime, estimateTime, requiredAgents, requiredItems, 
//				requiredActivities, description, name);
//		
//		insertActivityType(ActivityType.TOUR);
//		insertActivity(a7);
		
		System.out.println("Inserted initial data - 7 activities and 7 items");
	}
}
