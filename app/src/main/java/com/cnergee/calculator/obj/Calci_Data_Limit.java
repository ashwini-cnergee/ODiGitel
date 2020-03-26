/**
 * 
 */
package com.cnergee.calculator.obj;

import java.io.Serializable;

/**
 * @author Sandip
 *
 */
public class Calci_Data_Limit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String calc_data_limit_value,calc_data_limit_rate;

	public String getCalc_data_limit_value() {
		return calc_data_limit_value;
	}

	public void setCalc_data_limit_value(String calc_data_limit_value) {
		this.calc_data_limit_value = calc_data_limit_value;
	}

	public String getCalc_data_limit_rate() {
		return calc_data_limit_rate;
	}

	public void setCalc_data_limit_rate(String calc_data_limit_rate) {
		this.calc_data_limit_rate = calc_data_limit_rate;
	}
	
	
}
