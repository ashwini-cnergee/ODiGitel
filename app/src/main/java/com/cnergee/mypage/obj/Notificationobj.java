package com.cnergee.mypage.obj;

public class Notificationobj {
	
	private String NotifyId;
	private String MemberId;
	private String NotificationMessage;
	private String IsNotify;
	private String unique_id;;
	
	private boolean isRead;
	private boolean isUpdated;
	private boolean isSclosed;
	private boolean isPush;
	
	private String Notification;
	
	private String dataFrom="";
	private String icard_id="";
	private String created_on="";
	public boolean is_red=false;
	public Notificationobj(){}
	
	public Notificationobj (String notification ){
		this.Notification = notification;
	}
	
	public String getNotifyId() {
		return NotifyId;
	}
	public void setNotifyId(String notifyId) {
		NotifyId = notifyId;
	}
	public String getMemberId() {
		return MemberId;
	}
	public void setMemberId(String memberId) {
		MemberId = memberId;
	}
	public String getNotificationMessage() {
		return NotificationMessage;
	}
	public void setNotificationMessage(String notificationMessage) {
		NotificationMessage = notificationMessage;
	}
	public String getIsNotify() {
		return IsNotify;
	}
	public void setIsNotify(String isNotify) {
		IsNotify = isNotify;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public boolean isSclosed() {
		return isSclosed;
	}

	public void setSclosed(boolean isSclosed) {
		this.isSclosed = isSclosed;
	}

	public boolean isPush() {
		return isPush;
	}

	public void setPush(boolean isPush) {
		this.isPush = isPush;
	}

	public String getNotification() {
		return Notification;
	}
	public void setNotification(String Notification) {
		this.Notification = Notification;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getIcard_id() {
		return icard_id;
	}

	public void setIcard_id(String icard_id) {
		this.icard_id = icard_id;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public boolean isIs_red() {
		return is_red;
	}

	public void setIs_red(boolean is_red) {
		this.is_red = is_red;
	}
	
	
	
}
