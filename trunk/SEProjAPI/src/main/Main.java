package main;

import configuration.ConfigurationManager;
import simulator.Simulator;

public class Main {

	public static void main(String[] args) {
		
		ConfigurationManager config = new ConfigurationManager();
		Simulator sim = new Simulator(config.loadParameters());
		
		sim.start();		
	}
}
