/**
 * 
 */
package com.cnergee.calculator.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Sandip
 *
 */
public class Calci_Days implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HashMap<String, ArrayList<Calci_Speed>> hm_calci_speeds;

	public HashMap<String, ArrayList<Calci_Speed>> getHm_calci_speeds() {
		return hm_calci_speeds;
	}

	public void setHm_calci_speeds(
			HashMap<String, ArrayList<Calci_Speed>> hm_calci_speeds) {
		this.hm_calci_speeds = hm_calci_speeds;
	}
	
	
}
