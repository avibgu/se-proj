package utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The ConfigurationManager class is used to read
 * properties from the config.properties file 
 */
public class ConfigurationManager {
	
	private static Properties mProp;
	private static String mProjectID;
	private static String mServerURL;
	/**
	 * Reads the number of agents, items and sensors configured in the config.properties file
	 * The file is to be placed inside the simulator package
	 */
	private static void loadParameters() {
		Properties prop = new Properties();
		try {
			InputStream in = ConfigurationManager.class.getResourceAsStream("config.properties");
			prop.load(in);
			mProp = prop;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mProjectID = prop.getProperty("projectID");
		mServerURL = prop.getProperty("serverURL");

	}
	public static String getServerURL(){
		if(mProp == null)
			loadParameters();
		return mServerURL;
	}
	public static String getProjectID(){
		if(mProp == null)
			loadParameters();
		return mProjectID;
	}
}
