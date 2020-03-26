/**
 * 
 */
package com.cnergee.calculator.obj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Sandip
 *
 */
public class Calci_Speed implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String calc_speed_value,calc_speed_rate;
	
	ArrayList<Calci_Data_Limit> alCalci_Data_Limits;
	ArrayList<Calci_Post_Speed> alCalci_Post_Speeds;

	public String getCalc_speed_value() {
		return calc_speed_value;
	}

	public void setCalc_speed_value(String calc_speed_value) {
		this.calc_speed_value = calc_speed_value;
	}

	public String getCalc_speed_rate() {
		return calc_speed_rate;
	}

	public void setCalc_speed_rate(String calc_speed_rate) {
		this.calc_speed_rate = calc_speed_rate;
	}

	public ArrayList<Calci_Data_Limit> getAlCalci_Data_Limits() {
		return alCalci_Data_Limits;
	}

	public void setAlCalci_Data_Limits(
			ArrayList<Calci_Data_Limit> alCalci_Data_Limits) {
		this.alCalci_Data_Limits = alCalci_Data_Limits;
	}

	public ArrayList<Calci_Post_Speed> getAlCalci_Post_Speeds() {
		return alCalci_Post_Speeds;
	}

	public void setAlCalci_Post_Speeds(
			ArrayList<Calci_Post_Speed> alCalci_Post_Speeds) {
		this.alCalci_Post_Speeds = alCalci_Post_Speeds;
	}
	
	
}
