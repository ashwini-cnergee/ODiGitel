package com.cnergee.mypage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	
	 // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ionmypage";
 
    // Contacts table name
    private static final String TABLE_MEMBERDETAILS = "mypage";
 
    // Contacts Table Columns names
   // private static final String docket_no = "docketno";
    private static final String member_id = "memberid";
    //private static final String compl_det = "complaint";
    private static final String memberloginid = "memberloginid";
    private static final String member_name = "membername";
    private static final String mobile_no_primary = "mobilenoprimary";
    private static final String mobile_no_secondary = "mobilenosecondary";
    private static final String dateof_birth = "dateofbirth";
    private static final String activation_date = "activationdate";
    private static final String email_id = "emailid";
    private static final String installation_address = "installationaddress";
    
    private static final String status = "status";
    private static final String package_name = "packagename";
    
    private static final String KEY_ID = "_id";
    
    public static final String UPDATE_DATE = "update_date";
    public static final String FLAG = "flag";
    
    public static final String TABLE_UPDATE_STATUS  = "update_status";
    
    private String CREATE_UPDATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_UPDATE_STATUS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+  UPDATE_DATE +" TEXT,"+ FLAG +" TEXT )";
    
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		 String CREATE_MEMBERDETAILS_TABLE = "CREATE TABLE " + TABLE_MEMBERDETAILS + "("
	                + member_id + "TEXT," 
	                + memberloginid + " TEXT,"
	                + mobile_no_primary + " TEXT,"
	                + mobile_no_secondary + " TEXT)";
	                 db.execSQL(CREATE_MEMBERDETAILS_TABLE);
	                 db.execSQL(CREATE_UPDATE_TABLE);
	                 
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		 // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERDETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPDATE_STATUS);
        // Create tables again
        onCreate(db);
	}

	
	
	//Ezetap Inserting 
	   public void addEzetap(String date) {  
	        SQLiteDatabase db = this.getWritableDatabase();  
	   
	        ContentValues values = new ContentValues();  
	        values.put(UPDATE_DATE,date); // Contact Name  
	        values.put(FLAG, 1); // Contact Phone  
	   
	        
	        db.insert(TABLE_UPDATE_STATUS, null, values);  
	        
	        
	        db.close(); // Closing database connection  
	    } 
	  
	   
	   public void updateEzetap(String date){
		   
		   SQLiteDatabase db = this.getWritableDatabase();  
		   
	        ContentValues values = new ContentValues();  
	        values.put(UPDATE_DATE,date); // Contact Name  
	        values.put(FLAG, 1); // Contact Phone  
	   
	       
	        db.update(TABLE_UPDATE_STATUS, values, null, null);
	        
	        db.close(); 
	   
	   }
	   
	   
	   
	   
	   public Cursor readingEzetap() {
		    SQLiteDatabase db = this.getReadableDatabase();

		    Cursor cursor = db.query(TABLE_UPDATE_STATUS,null,null,null,null,null,null);
		  
	
		    return cursor;
		}
}
