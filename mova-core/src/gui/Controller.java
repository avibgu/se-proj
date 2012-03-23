/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import simulator.NewDomain;
import simulator.Simulator;

/**
 *
 * @author Shai Cantor
 */
public class Controller {
    
    private NewDomain _domain;
    private Simulator _sim;
    
    public Controller(NewDomain domain){
        _domain = domain;
        _sim = new Simulator(_domain);
    }

    public void start() {
        _sim.start();
    }
    
    public NewDomain getDomain(){
        return _domain;
    }

	public void stop() {
		_domain.stop();
	}
}
