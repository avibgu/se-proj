/**
 * 
 */
package simulator;

import gui.World;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import db.DBHandler;

/**
 * @author Shai Cantor
 *
 */
public class SimulatorListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		//DBHandler db = DBHandler.getInstance();
		//db.deleteData();
		World.InitWorld();
	}

}
