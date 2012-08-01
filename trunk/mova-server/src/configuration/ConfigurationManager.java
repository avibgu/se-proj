package configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import simulator.NewDomain;
/**
 * The ConfigurationManager class is used to read
 * properties from the config.properties file 
 * such as: number of agents, items and sensors
 * and the scan distance of the sensors 
 */
public class ConfigurationManager {
	
	private static Properties mProp;
	/**
	 * Reads the number of agents, items and sensors configured in the config.properties file
	 * The file is to be placed inside the simulator package
	 */
	public NewDomain loadParameters() {
		int nAgents, nItems, nSensors;
		double nScanDistance;
		Properties prop = new Properties();
		try {
			InputStream in = getClass().getResourceAsStream("config.properties");
			prop.load(in);
			mProp = prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		nAgents = Integer.parseInt(prop.getProperty("nAgents"));
		nItems = Integer.parseInt(prop.getProperty("nItems"));
		nSensors = Integer.parseInt(prop.getProperty("nSensors"));
		nScanDistance = Double.parseDouble(prop.getProperty("nScanDistance"));
		
		NewDomain domain = new NewDomain(15, 35, nAgents, nItems, nSensors, nScanDistance);
		return domain;
	}
	public static String getDBURL(){
		return mProp.getProperty("nConnectionString");
	}
}
