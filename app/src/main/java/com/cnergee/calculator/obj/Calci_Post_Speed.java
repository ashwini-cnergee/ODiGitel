/**
 * 
 */
package com.cnergee.calculator.obj;

import java.io.Serializable;

/**
 * @author Sandip
 *
 */
public class Calci_Post_Speed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String calc_post_speed_value,calc_post_speed_rate;

	public String getCalc_post_speed_value() {
		return calc_post_speed_value;
	}

	public void setCalc_post_speed_value(String calc_post_speed_value) {
		this.calc_post_speed_value = calc_post_speed_value;
	}

	public String getCalc_post_speed_rate() {
		return calc_post_speed_rate;
	}

	public void setCalc_post_speed_rate(String calc_post_speed_rate) {
		this.calc_post_speed_rate = calc_post_speed_rate;
	}


	
}
