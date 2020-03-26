package com.cnergee.mypage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.cnergee.mypage.obj.TableConstants;
import com.cnergee.mypage.utils.Utils;



public class DatabaseAdapter {
	Context ctx;
	public static int DATABASE_VERSION = 1;
	public static String DATABASE_NAME = "chat_database";
	SQLiteDatabase db;
	public DatabaseHelper helper;
	
	
	
	public String CREATE_CHAT_TABLE=
			"CREATE TABLE IF NOT EXISTS "+TableConstants.CHAT_TABLE+" ("+
					TableConstants.ROW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
					TableConstants.MESSAGE+" TEXT NOT NULL, "+
					TableConstants.MESSAGE_TYPE+" TEXT NOT NULL, "+
					TableConstants.DATE+" TEXT, "+
					TableConstants.TIME+" TEXT, "+
					TableConstants.SOURCE+" TEXT,"+
					TableConstants.PATH+" TEXT, "+
					TableConstants.STATUS+" TEXT, "+					
					TableConstants.DATE_CHANGE+" TEXT,"+
					TableConstants.SOURCE_ID+" TEXT,"+
					TableConstants.SYNC_STATUS+" TEXT,"+					
					TableConstants.URL+" TEXT, "+
					TableConstants.IMG_VID_STATUS+" TEXT, "+
					TableConstants.ADMIN_NAME+" TEXT, "+
					TableConstants.NotifYId+" TEXT );";
	

	public String CREATE_PROFILE_TABLE=
			"CREATE TABLE IF NOT EXISTS "+TableConstants.PROFILE_TABLE+" ("+
					TableConstants.ROW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
					TableConstants.PROFILE_ID+" TEXT NOT NULL, "+
					TableConstants.PROFILE_FIRST_NAME+" TEXT NOT NULL, "+
					TableConstants.PROFILE_LAST_NAME+" TEXT NOT NULL, "+					
					TableConstants.PROFILE_IMAGE+" TEXT  , "+
					TableConstants.PROFILE_DOB+" TEXT  , "+
					TableConstants.PROFILE_BG+" TEXT  , "+
					TableConstants.PROFILE_ADDRESS+" TEXT , "+
					TableConstants.DATE+" TEXT, "+
					TableConstants.TIME+" TEXT, "+
					TableConstants.PROFILE_GENDER+" TEXT, "+
					TableConstants.PROFILE_OTP+" TEXT, "+
					TableConstants.MOBILE_NUMBER+" TEXT NOT NULL);";
			
		
	public String CREATE_GROUP_TABLE=
			"CREATE TABLE IF NOT EXISTS "+TableConstants.INDI_GROUP_TABLE+" ("+
					TableConstants.ROW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
					TableConstants.GROUP_ID+" TEXT NOT NULL, "+
					TableConstants.GROUP_NAME+" TEXT NOT NULL, "+
					TableConstants.GROUP_ACTIVE+" TEXT );";
			

	public DatabaseAdapter(Context context) {
		this.ctx=context;
		helper= new DatabaseHelper(context);
	}
	public class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try{
				db.execSQL(CREATE_CHAT_TABLE);
				db.execSQL(CREATE_PROFILE_TABLE);
				db.execSQL(CREATE_GROUP_TABLE);
				Utils.log("Table created"," successfully");
			}catch(SQLException e){
				Utils.log("Error in Create table","is: "+e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(CREATE_CHAT_TABLE);
			db.execSQL(CREATE_PROFILE_TABLE);
			db.execSQL(CREATE_GROUP_TABLE);
			Utils.log("Table created"," successfully");
		}
	}

	public void open() throws SQLiteException{
		db=helper.getWritableDatabase();
	}
	
	public void close() {
			if(db.isOpen()){
			helper.close();
		}
	}
	
	public boolean isOpen() {
		return db.isOpen();
	}


	public long insertProfileData(String profile_id,String str_f_Name,String str_l_Name,String address,String gender,String BloodGroup, String str_MobileNumber,String OTP,String Dob,String image,String str_Date,String str_Time){

		try{
		long i=0;		
		ContentValues cv = new ContentValues();
		cv.put(TableConstants.PROFILE_ID, profile_id);
		cv.put(TableConstants.PROFILE_FIRST_NAME, str_f_Name);
		cv.put(TableConstants.PROFILE_LAST_NAME, str_l_Name);		
		cv.put(TableConstants.PROFILE_ADDRESS, address);
		cv.put(TableConstants.PROFILE_GENDER, gender);
		cv.put(TableConstants.PROFILE_BG, BloodGroup);
		cv.put(TableConstants.MOBILE_NUMBER, str_MobileNumber);
		cv.put(TableConstants.PROFILE_OTP, OTP);
		cv.put(TableConstants.PROFILE_DOB, Dob);
		cv.put(TableConstants.PROFILE_IMAGE, image);				
		cv.put(TableConstants.DATE, str_Date);
		cv.put(TableConstants.TIME, str_Time);
		
		i=db.insert(TableConstants.PROFILE_TABLE, null, cv);
		Utils.log("Row is inserted",": "+i);
		return i;
		}
		catch(Exception e){
			Utils.log("Exception in insert","is :"+e);
			return -1;
		}
	}
	
	public long UpdateProfileData(String profile_id,String str_f_Name,String str_l_Name,String address,String gender,String BloodGroup, String str_MobileNumber,String OTP,String Dob,String image,String str_Date,String str_Time){
		try{
		long i=0;		
		ContentValues cv = new ContentValues();
		cv.put(TableConstants.PROFILE_ID, profile_id);
		cv.put(TableConstants.PROFILE_FIRST_NAME, str_f_Name);
		cv.put(TableConstants.PROFILE_LAST_NAME, str_l_Name);		
		cv.put(TableConstants.PROFILE_ADDRESS, address);
		cv.put(TableConstants.PROFILE_GENDER, gender);
		cv.put(TableConstants.PROFILE_BG, BloodGroup);
		cv.put(TableConstants.MOBILE_NUMBER, str_MobileNumber);
		cv.put(TableConstants.PROFILE_OTP, OTP);
		cv.put(TableConstants.PROFILE_DOB, Dob);
		cv.put(TableConstants.PROFILE_IMAGE, image);				
		cv.put(TableConstants.DATE, str_Date);
		cv.put(TableConstants.TIME, str_Time);
		//i=db.insert(TableConstants.PROFILE_TABLE, null, cv);
		i=db.update(TableConstants.PROFILE_TABLE, cv, TableConstants.ROW_ID+"=?", new String[]{"1"});
		Utils.log("Row is Updated",": "+i);
		return i;
		}
		catch(Exception e){
			Utils.log("Exception in insert","is :"+e);
			return -1;
		}
		
	}
	
	public long insertMessgae(String message,String type,String date,String time,String source,String path,String status,String date_change,String source_id,String sync_status,String url,String notifyId,String img_vid_status,String admin_name){		
		
		long i=0;		
		ContentValues cv = new ContentValues();
		
		cv.put(TableConstants.MESSAGE, message);
		cv.put(TableConstants.MESSAGE_TYPE, type);
		cv.put(TableConstants.DATE, date);
		cv.put(TableConstants.TIME, time);
		cv.put(TableConstants.SOURCE, source);
		cv.put(TableConstants.PATH, path);
		cv.put(TableConstants.STATUS, status);		
		cv.put(TableConstants.DATE_CHANGE, date_change);
		cv.put(TableConstants.SOURCE_ID, source_id);
		cv.put(TableConstants.SYNC_STATUS, sync_status);		
		cv.put(TableConstants.URL, url);
		cv.put(TableConstants.NotifYId, notifyId);
		cv.put(TableConstants.IMG_VID_STATUS, img_vid_status);
		cv.put(TableConstants.ADMIN_NAME, admin_name);
		i=db.insert(TableConstants.CHAT_TABLE, null, cv);
		Utils.log("Row is inserted",": "+i);
		return i;
	}
	
	public Cursor getAllChatMessgaes(){
		Cursor cur_Messages=null;
		
		cur_Messages=db.query(TableConstants.CHAT_TABLE, null, null, null, null, null, TableConstants.ROW_ID+" DESC");
		return cur_Messages;
	}
	
	public Cursor getAllMedia(String str_img_name,String source){
		Cursor mCur=null;
		mCur=db.query(TableConstants.CHAT_TABLE, new String[]{TableConstants.MESSAGE_TYPE,TableConstants.PATH,TableConstants.STATUS}, TableConstants.MESSAGE_TYPE+"=? AND "+TableConstants.STATUS+"=? AND "+TableConstants.SOURCE+"=?", new String[]{str_img_name,"View",source }, null, null, null);
		return mCur;
	}
	
	public long insertDateMessgae(String str_Date,String str_State){		
		long i=0;		
		ContentValues cv = new ContentValues();
		cv.put(TableConstants.MESSAGE, "date_cahnge");
		cv.put(TableConstants.MESSAGE_TYPE, "date_cahnge");
		cv.put(TableConstants.DATE, str_Date);
		cv.put(TableConstants.TIME, "date_cahnge");
		cv.put(TableConstants.SOURCE, "date_cahnge");
		cv.put(TableConstants.PATH, "date_cahnge");
		cv.put(TableConstants.STATUS, "date_cahnge");		
		cv.put(TableConstants.DATE_CHANGE, str_State);
		cv.put(TableConstants.SOURCE_ID, "date_cahnge");
		cv.put(TableConstants.SYNC_STATUS, "date_cahnge");		
		cv.put(TableConstants.URL, "date_cahnge");
	
		i=db.insert(TableConstants.CHAT_TABLE, null, cv);
		Utils.log("Row is inserted",": "+i);
		return i;
	}
	
	public Cursor getMessagesToSend(){
		Cursor mCur=null;
		//mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=?", new String[]{"client","no"}, null, null, TableConstants.ROW_ID+" DESC" );
		mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.IMG_VID_STATUS+"=?", new String[]{"client","no","uploaded"}, null, null, null );
		
		return mCur;
	}
	
	public long UpdateSyncStatus(String row_id){
		long i = 0;
		ContentValues cv= new ContentValues();
		cv.put(TableConstants.SYNC_STATUS, "yes");
		i=db.update(TableConstants.CHAT_TABLE, cv, TableConstants.ROW_ID+"=?", new String []{row_id});
	//	String strSQL = "UPDATE " +TableConstants.CHAT_TABLE+" SET "+ TableConstants.SYNC_STATUS +"= 'yes' WHERE "+ TableConstants.ROW_ID+" = "+ row_id;
		//db.execSQL(strSQL);
		Utils.log("Update","status: "+i);
		return i;
	}
	
	public Cursor getImagesToSend(String upload_status){
		Cursor mCur=null;
		mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.MESSAGE_TYPE+"=? AND "+TableConstants.IMG_VID_STATUS+"=?", new String[]{"client","no","Image",upload_status}, null, null, TableConstants.ROW_ID+" DESC" );
	//	mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.MESSAGE_TYPE+"=?  ", new String[]{"client","no","Image"}, null, null, TableConstants.ROW_ID+" DESC" );
		/* running */
		//mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.MESSAGE_TYPE+"=?  ", new String[]{"client","no","Image"}, null, null, null );
		return mCur;
	}
	
	public Cursor getVideosToSend(){
		Cursor mCur=null;
		mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.MESSAGE_TYPE+"=? AND "+TableConstants.IMG_VID_STATUS+"=?", new String[]{"client","no","Video","no_uploaded"}, null, null, TableConstants.ROW_ID+" DESC" );
		//mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.MESSAGE_TYPE+"=?  ", new String[]{"client","no","Video"}, null, null, TableConstants.ROW_ID+" DESC" );
		//mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=? AND "+TableConstants.MESSAGE_TYPE+"=?  ", new String[]{"client","no","Video"}, null, null, null );
		return mCur;
	}
	
/*
 * getNotifyCount(String notify) method is used to prevent from inserting duplicate notifyId in database.
 * 
 */	
	
	public int getNotifyCount(String notify){
		int i=0;
		Cursor mCur=null;
		
		mCur=db.query(TableConstants.CHAT_TABLE, null, TableConstants.NotifYId+"=?", new String[]{notify}, null, null, null );
		i=mCur.getCount();
		Utils.log("NotifyId Count","database: "+i);
		return i;
	}
	
	public long UpdateDownloadStatus(String row_id,String status){
		long i = 0;
		//ContentValues cv= new ContentValues();
		//cv.put(TableConstants.SYNC_STATUS, "yes");
		//i=db.update(TableConstants.CHAT_TABLE, cv, TableConstants.ROW_ID, new String []{row_id});
		String strSQL = "UPDATE " +TableConstants.CHAT_TABLE+" SET "+ TableConstants.STATUS +"='"+status+ "' WHERE "+ TableConstants.ROW_ID+" = "+ row_id;
		db.execSQL(strSQL);
		Utils.log("Update","status: "+i);
		return i;
	}
	
	public long UpdateNotifyIdStatus(String notifyId){
		long i = 0;
		//ContentValues cv= new ContentValues();
		//cv.put(TableConstants.SYNC_STATUS, "yes");
		//i=db.update(TableConstants.CHAT_TABLE, cv, TableConstants.ROW_ID, new String []{row_id});
		String strSQL = "UPDATE " +TableConstants.CHAT_TABLE+" SET "+ TableConstants.SYNC_STATUS +"= 'yes' WHERE "+ TableConstants.NotifYId+" = "+ notifyId;
		db.execSQL(strSQL);
		Utils.log("Update NotifyId ","database: "+i);
		return i;
	}
	
	public Cursor getNotifyId(){
		Cursor mCur=null;
		
		mCur=db.query(TableConstants.CHAT_TABLE, new String[]{TableConstants.ROW_ID,TableConstants.NotifYId}, TableConstants.SOURCE+"=? AND "+TableConstants.SYNC_STATUS+"=?", new String[]{"server","no"}, null, null, TableConstants.ROW_ID +" DESC" );
		Utils.log("pending NotifyId","are:"+mCur.getCount());
		return mCur;
	}

	public void deleteRow(String rowId){
		int i=0;
		i=db.delete(TableConstants.CHAT_TABLE, TableConstants.ROW_ID+" =?", new String[]{rowId});
		Utils.log("Row","deleted"+i);
	}
	
	public String getProfileId(){
		String profile_id="";
		Cursor mCur=db.query(TableConstants.PROFILE_TABLE, new String[]{TableConstants.PROFILE_ID}, null, null, null, null, null);
		if(mCur.getCount()>0){
			while(mCur.moveToNext()){
				profile_id=mCur.getString(mCur.getColumnIndex(TableConstants.PROFILE_ID));
			}
		}
		return profile_id;
	}
	
	public Cursor getProfileData(){
		
		Cursor mCur=db.query(TableConstants.PROFILE_TABLE, null, null, null, null, null, null);
	
		return mCur;
	}
	
	public long updateProfile1(String row_id,String f_name,String l_name,String mob_number,String gender){
		long i=-1;
		ContentValues cv= new ContentValues();
		
		cv.put(TableConstants.PROFILE_FIRST_NAME, f_name);
		cv.put(TableConstants.PROFILE_LAST_NAME, l_name);
		cv.put(TableConstants.MOBILE_NUMBER, mob_number);
		cv.put(TableConstants.PROFILE_GENDER, gender);
		
		i=db.update(TableConstants.PROFILE_TABLE, cv, TableConstants.ROW_ID+"=? ", new String[]{row_id});
		
		return i;
	}
	
	public long updateuploadstatus(String row_id,String status){
		long i= -1;
		Utils.log("IMAGE UPDATING STATUS",":"+status);
		ContentValues cv= new ContentValues();
		cv.put(TableConstants.IMG_VID_STATUS, status);
		i=db.update(TableConstants.CHAT_TABLE, cv, TableConstants.ROW_ID+" =? ", new String []{row_id});
		//String str_update="UPDATE " +TableConstants.CHAT_TABLE+" SET "+ TableConstants.IMG_VID_STATUS +"= '"+status+"' WHERE "+ TableConstants.ROW_ID+" = "+ row_id;
		//db.execSQL(str_update);
		Utils.log("IMAGE UPDATING RESULT",":"+i);
		return i;
	}


	
	public void updateOrInsertGroup(String groupId,String groupName){
		Cursor mCur= db.query(TableConstants.INDI_GROUP_TABLE, null, null, null, null, null, null);
		ContentValues cv= new ContentValues();
		cv.put(TableConstants.GROUP_ID, groupId);
		cv.put(TableConstants.GROUP_NAME, groupName);
		if(mCur.getCount()>0){
			db.update(TableConstants.INDI_GROUP_TABLE, cv, TableConstants.ROW_ID+" =?", new String[]{"1"});
		}
		else{
			db.insert(TableConstants.INDI_GROUP_TABLE, null, cv);
		}
	}

	
	public String getGroupId(){
		String groupId="";
		Cursor mCur=db.query(TableConstants.INDI_GROUP_TABLE, new String[]{TableConstants.GROUP_ID}, null, null, null, null, null);
		if(mCur.getCount()>0){
			while(mCur.moveToNext()){
				groupId=mCur.getString(mCur.getColumnIndex(TableConstants.GROUP_ID));
			}
		}
		return groupId;
	}

  }
