package configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import simulator.NewDomain;

public class ConfigurationManager {

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
}
