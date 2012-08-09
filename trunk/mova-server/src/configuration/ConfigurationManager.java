package configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.Application;

import simulator.NewDomain;
/**
 * The ConfigurationManager class is used to read
 * properties from the config.properties file 
 * such as: number of agents, items and sensors
 * and the scan distance of the sensors 
 */
public class ConfigurationManager {
	
	private static Properties mProp;
	private static String mAPIKey;
	private static String mConnectionString;
	private static double mScanDistance;
	/**
	 * Reads the number of agents, items and sensors configured in the config.properties file
	 * The file is to be placed inside the simulator package
	 */
	public static NewDomain loadParameters() {
		Properties prop = new Properties();
		try {
			InputStream in = ConfigurationManager.class.getResourceAsStream("../../config.properties");
			prop.load(in);
			mProp = prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mScanDistance = Double.parseDouble(prop.getProperty("scanDistance"));
		mConnectionString = prop.getProperty("connectionString");
		mAPIKey = prop.getProperty("apiKey");
		
		NewDomain domain = new NewDomain(15, 35, mScanDistance);
		return domain;
	}
	public static String getDBURL(){
		if(mProp == null)
			loadParameters();
		return mConnectionString;
	}
	public static String getAPIKey(){
		if(mProp == null)
			loadParameters();
		return mAPIKey;
	}
}
