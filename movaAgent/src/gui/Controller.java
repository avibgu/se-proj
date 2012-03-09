/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import simulator.Domain;
import simulator.Simulator;

/**
 *
 * @author Shai Cantor
 */
public class Controller {
    
    private Domain _domain;
    private Simulator _sim;
    
    public Controller(Domain domain){
        _domain = domain;
        _sim = new Simulator(_domain);
    }

    public void start() {
        _sim.start();
    }
    
    public Domain getDomain(){
        return _domain;
    }

	public void stop() {
		_domain.stop();
	}
}
