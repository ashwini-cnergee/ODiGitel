package com.cnergee.mypage.utils;

public class FinishEvent {

	public String ClassName="";

	public FinishEvent(String ClassName){
		this.ClassName=ClassName;
	}
	
	public String getMessage(){
		return ClassName;
	}
}
