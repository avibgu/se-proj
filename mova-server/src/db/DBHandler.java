package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DBHandler {
	
	private static String						mDbURL = "jdbc:derby://localhost:1527/ServerDB;create=true";
    private static String						mAgentTableName = "app.agents";
    private static String						mActivityTableName = "app.activities";
   
    // jdbc Connection
    
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
			sqlExcept.printStackTrace();
		}
		
		shutdown();
		mWrite.unlock();
	}
	
	public void insertActivity(String pActivityId, String pName, String pDescription, String pType, String pStartTime,
			String pEndTime, int pEstimatedTime) {
		
		mWrite.lock();
		createConnection();
		Timestamp startTime = Timestamp.valueOf(pStartTime);
		Timestamp endTime = Timestamp.valueOf(pEndTime);
		try {
			mStmt = mConn.createStatement();

			mStmt.execute("insert into " + mActivityTableName + " values (" + "'"
					+ pActivityId + "'" + "," + "'" + pName + "'" + "," + "'" + pDescription 
					+ "'" + "," + "'" + pType + "'" + "," + startTime + "," + endTime + 
					"," + pEstimatedTime + ")");
			mStmt.close();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
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
			System.out.println("database access error");
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
			System.out
					.println("database access error or no agents in database");
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
					.println("database access error, no activityId or no agent id in the system: "
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
					.println("database access error or no agent id in the system: "
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
			System.out
					.println("database access error, no registrationId, or no agent id in the system: "
							+ pAgentId);
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
			sqlExcept.printStackTrace();
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
			sqlExcept.printStackTrace();
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
			sqlExcept.printStackTrace();
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
			sqlExcept.printStackTrace();
		}
		shutdown();
		mWrite.unlock();
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
}
