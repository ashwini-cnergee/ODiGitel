
package com.cnergee.mypage.utils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.util.Log;

import com.cnergee.mypage.SOAP.NotificationStatusUpdateSOAP;
import com.cnergee.mypage.obj.NotificationUpdateStatusObj;

public class StatusUpdateHelper {

	private NotificationStatusUpdateSOAP soap;
	private String memberid;
	private String notifyid;
	private boolean isnotify;
	
	public StatusUpdateHelper (NotificationStatusUpdateSOAP soap,String memberid,String notifyid,boolean isnotify){
		this.soap = soap;
		this.memberid = memberid;
		this.notifyid = notifyid;
		this.isnotify = isnotify;
	}
	
	public void updateStatus() throws SocketTimeoutException, SocketException,Exception{
	    	
		
	     	NotificationUpdateStatusObj obj = new NotificationUpdateStatusObj();

	    	/*obj.setComment("");
			Calendar c = Calendar.getInstance();
			SimpleDateFormat viewDateFormatter = new SimpleDateFormat("ddMMyyyy");
			String currentDate = viewDateFormatter.format(c.getTime());
			obj.setComplaintDate(currentDate+"000000");*/
			obj.setNotifyid(notifyid);
			obj.setMemberid(memberid);
			obj.setNotifystatus(isnotify);
			
	    	try {
				String res = soap.updateComplaintStatus(obj);
				if(!res.equalsIgnoreCase("ok") ){
					Log.i("*********** STATUS NOT CHANGE... ",""+notifyid);
				}
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
	    	
	    	
	    }
	  
}
