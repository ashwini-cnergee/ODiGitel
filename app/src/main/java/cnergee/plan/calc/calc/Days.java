package cnergee.plan.calc.calc;


import java.io.Serializable;
import java.util.HashMap;

public class Days implements Serializable {
	
	HashMap<String, DayRate_Plan> hashMapDays;
	int Multiplier;

	public HashMap<String, DayRate_Plan> getHashMapDays() {
		return hashMapDays;
	}

	public void setHashMapDays(HashMap<String, DayRate_Plan> hashMapDays) {
		this.hashMapDays = hashMapDays;
	}

	public int getMultiplier() {
		return Multiplier;
	}

	public void setMultiplier(int multiplier) {
		Multiplier = multiplier;
	}

	

}
