package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DBHandler {
	
	private static String dbURL = "jdbc:derby://localhost:1527/ServerDB;create=true";
    private static String tableName = "app.agents";
    // jdbc Connection
    private static Connection conn = null;
    private static Statement stmt = null;
    
    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final Lock read = rwl.readLock();
    private static final Lock write = rwl.writeLock();
    
    private static DBHandler dbHandler = null;
    
    private DBHandler(){}
    
    public synchronized static DBHandler getInstance() {
        if(dbHandler == null) {
        	dbHandler = new DBHandler();
        }
        return dbHandler;
     }
    
    private static void createConnection(){
        try{
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void insertAgent(String agentId, String type, Boolean loggedIn, String ip, String registrationId){
    	write.lock();
    	createConnection();
        try{
            stmt = conn.createStatement();
            char logged;
			if(loggedIn)
            	logged = 't';
			else
				logged = 'f';
            stmt.execute("insert into " + tableName + " values (" +
                     "'"+agentId+"'" + "," + "'"+type+"'" + "," + "'"+logged+"'" + "," + "'"+ip+"'" + "," + "'"+registrationId+"'" + ")");
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	write.unlock();
    }
    
    public void deleteAgent(String agentId){
    	write.lock();
    	createConnection();
        try{
            stmt = conn.createStatement();
            stmt.execute("delete from " + tableName + " WHERE AGENT_ID = " + "'"+agentId+"'");
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	write.unlock();
    }
    
    public Vector<String> getAgentIds(){
    	read.lock();
    	createConnection();
    	Vector<String> agentIds = new Vector<String>();
        try{
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select AGENT_ID from " + tableName);

            while(results.next()){
                String id = results.getString(1);
                agentIds.add(id);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
        shutdown();
        read.unlock();
        return agentIds;
    }
    
    public String getAgentIP(String agentId){
    	read.lock();
    	createConnection();
    	String ip = "";
    	try{
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select IP from " + tableName + " WHERE AGENT_ID = " + "'"+agentId+"'");
            results.next();
            ip = results.getString(1);
            
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	read.unlock();
        return ip;
    }
    
    public String getAgentRegistrationId(String agentId){
    	read.lock();
    	createConnection();
    	String regristrationId = "";
    	try{
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select REGISTRATION_ID from " + tableName + " WHERE AGENT_ID = " + "'"+agentId+"'");
            results.next();
            regristrationId = results.getString(1);
            
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	read.unlock();
        return regristrationId;
    }
    
    public void changeAgentStatus(String agentId, Boolean loggedIn){
    	write.lock();
    	createConnection();
        try{
            stmt = conn.createStatement();
            char logged;
			if(loggedIn)
            	logged = 't';
			else
				logged = 'f';
            stmt.execute("update " + tableName + " set AGENT_STATUS = " + "'"+logged+"'" + " WHERE AGENT_ID = " + "'"+agentId+"'");
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	write.unlock();
    }
    
    public void changeAgentIP(String agentId, String newIP){
    	write.lock();
    	createConnection();
        try{
            stmt = conn.createStatement();
            stmt.execute("update " + tableName + " set IP = " + "'"+newIP+"'" + " WHERE AGENT_ID = " + "'"+agentId+"'");
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	write.unlock();
    }
    
    public void changeAgentRegistrationID(String agentId, String registrationID){
    	write.lock();
    	createConnection();
        try{
            stmt = conn.createStatement();
            stmt.execute("update " + tableName + " set REGISTRATION_ID = " + "'"+registrationID+"'" + " WHERE AGENT_ID = " + "'"+agentId+"'");
            stmt.close();
        }
        catch (SQLException sqlExcept){
            sqlExcept.printStackTrace();
        }
    	shutdown();
    	write.unlock();
    }
    
    private void shutdown(){
        try{
	        if (stmt != null)
	            stmt.close();
	        if (conn != null){
	            DriverManager.getConnection(dbURL + ";shutdown=true");
	            conn.close();
	        }           
        }
        catch (SQLException sqlExcept){}
    }
}
