package cnergee.plan.calc.calc;

import java.io.Serializable;

public class DataLimit implements Serializable {

	String Data_Gb_Value;
	Double Data_Gb_Rate;
	public String getData_Gb_Value() {
		return Data_Gb_Value;
	}
	public void setData_Gb_Value(String data_Gb_Value) {
		Data_Gb_Value = data_Gb_Value;
	}
	public Double getData_Gb_Rate() {
		return Data_Gb_Rate;
	}
	public void setData_Gb_Rate(Double data_Gb) {
		Data_Gb_Rate = data_Gb;
	}
	
	
}
