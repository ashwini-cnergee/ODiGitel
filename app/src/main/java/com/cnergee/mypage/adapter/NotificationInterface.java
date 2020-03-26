package com.cnergee.mypage.adapter;

import java.util.ArrayList;

import com.cnergee.mypage.obj.Notificationobj;



public interface NotificationInterface {
	public  void showDelete(ArrayList<String> alNotifyId);
	
	public void showIcard(String id, String datafrom, Notificationobj notificationobj);
	
}