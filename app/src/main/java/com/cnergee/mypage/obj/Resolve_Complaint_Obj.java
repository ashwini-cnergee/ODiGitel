package com.cnergee.mypage.obj;

public class Resolve_Complaint_Obj {

	String Complaint_Id,Complaint_Name;
	
	

	public Resolve_Complaint_Obj(String complaint_Id, String complaint_Name) {
		
		Complaint_Id = complaint_Id;
		Complaint_Name = complaint_Name;
	}

	public String getComplaint_Id() {
		return Complaint_Id;
	}

	public void setComplaint_Id(String complaint_Id) {
		Complaint_Id = complaint_Id;
	}

	public String getComplaint_Name() {
		return Complaint_Name;
	}

	public void setComplaint_Name(String complaint_Name) {
		Complaint_Name = complaint_Name;
	}
	
	

}
