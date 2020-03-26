package com.cnergee.mypage.obj;

public class Chat {

	
	String row_id, message, type, source, date, time, path, status,date_change,source_id,sync_status,url,notifyid,admin_name;

	
	public Chat() {

	}

	public Chat(String row_id,String message, String type, String date, String time,String source,
			 String path, String status,String date_change,String source_id,String sync_status,String url,String notifyid,String admin_name) {
		this.row_id = row_id;		
		this.message = message;
		this.type = type;
		this.source = source;
		this.date = date;
		this.time = time;
		this.path = path;
		this.status = status;
		this.date_change = date_change;
		this.source_id = source_id;
		this.sync_status = sync_status;
		this.url=url;
		this.notifyid=notifyid;
		this.admin_name=admin_name;
	}

	

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate_change() {
		return date_change;
	}

	public void setDate_change(String date_change) {
		this.date_change = date_change;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getSync_status() {
		return sync_status;
	}

	public void setSync_status(String sync_status) {
		this.sync_status = sync_status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getNotifyid() {
		return notifyid;
	}

	public void setNotifyid(String notifyid) {
		this.notifyid = notifyid;
	}


}
