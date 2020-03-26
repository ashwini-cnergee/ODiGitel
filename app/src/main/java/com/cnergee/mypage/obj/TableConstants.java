package com.cnergee.mypage.obj;

import android.os.Environment;

public class TableConstants {
	public static String CHAT_TABLE = "chat_table"; //Table Name 
	
	public static String ROW_ID = "id";
	public static String MESSAGE = "message";
	public static String MESSAGE_TYPE = "message_type";
	public static String PATH = "path";
	public static String DATE = "date";
	public static String TIME = "time";
	public static String STATUS = "status";
	public static String SOURCE = "message_source";
	public static String DATE_CHANGE = "date_change";
	public static String SYNC_STATUS = "sync_status";
	public static String SOURCE_ID = "source_id";
	public static String URL = "url";
	public static String NotifYId = "notifyid";
	public static String IMG_VID_STATUS = "img_vid_status";
	public static String ADMIN_NAME = "admin_name";
	
	public static String PROFILE_TABLE = "profile_table";//Table Name 
	
	public static String PROFILE_ID = "profile_id";
	public static String PROFILE_FIRST_NAME = "profile_first_name";
	public static String PROFILE_LAST_NAME = "profile_last_name";
	public static String MOBILE_NUMBER = "mobile_number";
	public static String PROFILE_IMAGE = "profile_image";
	public static String PROFILE_DOB = "profile_dob";
	public static String PROFILE_BG = "profile_bg";
	public static String PROFILE_GENDER = "profile_gender";
	public static String PROFILE_ADDRESS = "profile_address";
	public static String PROFILE_OTP = "profile_otp";
	
	public static String INDI_GROUP_TABLE = "group_table";//Table Name 
	
	public static String GROUP_ID = "group_id";
	public static String GROUP_NAME = "group_name";
	public static String GROUP_ACTIVE = "group_status";
	
	
	public static String SD_CARD_IMAGE_PATH = Environment.getExternalStorageDirectory()+"/ChatApp/Image";
	public static String SD_CARD_VIDEO_PATH = Environment.getExternalStorageDirectory()+"/ChatApp/Video";	
	public static String SD_CARD_PROFILE_PATH = Environment.getExternalStorageDirectory()+"/ChatApp/Profile";
	public static boolean CHECK_APP_OPEN=false;
	
	//public static String SET_PROFILE_ID="4"; 	//from profile table
	//public static String SET_RECEIVER_ID="3";  //from group table
	
	public static boolean START_SERVICE=true;
	public static boolean UPDATE_START=true;
	public static int MESSAGE_COUNT=0; 
	
	
	//public static String FTP_IP = "114.79.181.53";
	public static String FTP_IP = "114.79.181.66";
	//insert into profile_table (profile_id,profile_first_name,profile_last_name,mobile_number) values('0', 'sa','surname','9870204648');
}
