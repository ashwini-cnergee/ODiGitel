package cnergee.plan.calc;

import java.io.Serializable;
import java.util.ArrayList;

public class DayRate_Plan implements Serializable{

	int multiple;
	
	ArrayList<DataLimit> alDataLimits;
	ArrayList<Speed> alSpeeds;
	ArrayList<Speed_After_Vol> alSpeed_After_Vols;
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}
	public ArrayList<DataLimit> getAlDataLimits() {
		return alDataLimits;
	}
	public void setAlDataLimits(ArrayList<DataLimit> alDataLimits) {
		this.alDataLimits = alDataLimits;
	}
	public ArrayList<Speed> getAlSpeeds() {
		return alSpeeds;
	}
	public void setAlSpeeds(ArrayList<Speed> alSpeeds) {
		this.alSpeeds = alSpeeds;
	}
	public ArrayList<Speed_After_Vol> getAlSpeed_After_Vols() {
		return alSpeed_After_Vols;
	}
	public void setAlSpeed_After_Vols(ArrayList<Speed_After_Vol> alSpeed_After_Vols) {
		this.alSpeed_After_Vols = alSpeed_After_Vols;
	}
	

}
