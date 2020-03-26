package com.cnergee.mypage.obj;

import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class ComplaintCategoryList implements Parcelable {
	
	private String ComplaintId;
	private String ComnplaintName;
	
	private Map<String, ComplaintCategoryList> mapComplaintCategoryList;

	public String getComplaintId() {
		return ComplaintId;
	}

	public void setComplaintId(String complaintId) {
		ComplaintId = complaintId;
	}

	public String getComnplaintName() {
		return ComnplaintName;
	}

	public void setComnplaintName(String comnplaintName) {
		ComnplaintName = comnplaintName;
	}

	public Map<String, ComplaintCategoryList> getMapComplaintCategoryList() {
		return mapComplaintCategoryList;
	}

	public void setMapComplaintCategoryList(
			Map<String, ComplaintCategoryList> mapComplaintCategoryList) {
		this.mapComplaintCategoryList = mapComplaintCategoryList;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(ComnplaintName);  
		dest.writeString(ComplaintId);  
	}
	
	 public static final Creator<ComplaintCategoryList> CREATOR = new Creator<ComplaintCategoryList>() {
		 public ComplaintCategoryList createFromParcel(Parcel source) {  
			 ComplaintCategoryList c= new ComplaintCategoryList();
		     c.ComnplaintName = source.readString();  
		     c.ComplaintId = source.readString();  
		     
		     return c;  
		 }  
		 public ComplaintCategoryList[] newArray(int size) {  
		     return new ComplaintCategoryList[size];  
		 } 
	 };

}
