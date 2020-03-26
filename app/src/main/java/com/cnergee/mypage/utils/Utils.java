
package com.cnergee.mypage.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.cnergee.mypage.obj.AuthenticationMobile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import cnergee.krishna.ookla.odigitel.R;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Utils {

	public static Style style= new Style.Builder()
	.setBackgroundColor(R.color.plan_calci)
	
	.setGravity(Gravity.CENTER)
	.setTextColor(android.R.color.white)
	.setHeight(48)
	.build();
public static final String VIEW_DATE_FORMAT = "dd-MMM-yyyy";
public static final String DATE_FORMAT = "dd-MM-yyyy";
public String MemberLoginID, MemberId, MobileNoPrimary,MobileNoSecondary,EmailId;
	
private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
public static String Last_Class_Name="";
	
public static ResideMenu resideMenu;
public static Boolean showLog=true;;
public static boolean is_CCAvenue=false;
public static boolean is_PayUmoney=true;
	public static boolean is_aggrepay=true;
	public static String IS_RENEWAL="is_renewal";

	public static boolean is_atom=false;
public static boolean self_res_question_show=false;
public static boolean open_once=true;
public static boolean pg_sms_request=false;

public static String pg_sms_uniqueid="";
public static ResideMenuItem itemProfile;
public static ResideMenuItem itemRenewal;
public static ResideMenuItem itemSelf_Res;
public static ResideMenuItem itemCall_to_CC;	
//public static ResideMenuItem itemChat;
public static ResideMenuItem itemAlerts;
public static ResideMenuItem itemHelp;
public static ResideMenuItem itemShare;
public static ResideMenuItem itemRefer;

public static ResideMenuItem itemProfile1;
public static ResideMenuItem itemRenewal1;
public static ResideMenuItem itemSelf_Res1;
public static ResideMenuItem itemCall_to_CC1;	
//public static ResideMenuItem itemChat1;
public static ResideMenuItem itemAlerts1;
public static ResideMenuItem itemHelp1;
public static ResideMenuItem itemShare1;
public static ResideMenuItem itemRefer1;
	public static ResideMenuItem itemlogout;
	public static ResideMenuItem itemlogout1;


public static String APP_FIRST_TIME="app_first_time";
public static String APP_SECOND_TIME="app_second_time";
public static String APP_RATE_STATUS="app_rate_status";
public static String CCAvenue_Message="We currently facing problem \n We will fix this soon!";
public static String CITRUS_MESSAGE="Please select payment option 2 \n" + "We will fix this problem soon. ";
public static String Atom_Message="We currently facing problem \n We will fix this soon!";

public static String FTP_IP="";
public static String FTP_USERNAME="";
public static String FTP_PASSWORD="";

static SharedPreferences sharedPreferences_app_rate;
	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		MemberLoginID = sharedPreferences.getString("MemberLoginID", "");
		MemberId = sharedPreferences.getString("MemberId", "");
		MobileNoPrimary = sharedPreferences.getString("MobileNoPrimary", "");
		MobileNoSecondary = sharedPreferences.getString("MobileNoSecondary", "");
		EmailId = sharedPreferences.getString("EmailId", "");
		Utils.log("EmailId", ""+EmailId);
	}
	
	
	public void clearSharedPreferences(SharedPreferences sharedPreferences) {
		 
		SharedPreferences.Editor editor = sharedPreferences.edit();
	     //editor.remove("USER_NAME");
	    //editor.remove("USER_PASSWORD");
	    //editor.remove("USER_ID");
	    //editor.clear();

	    editor.commit();
	}
	
	
	public String getMemberLoginID() {
		return MemberLoginID;
	}

	public void setMemberLoginID(String memberLoginID) {
		MemberLoginID = memberLoginID;
	}

	public String getMemberId() {
		return MemberId;
	}

	public void setMemberId(String memberId) {
		MemberId = memberId;
	}

	public String getMobileNoPrimary() {
		return MobileNoPrimary;
	}

	public void setMobileNoPrimary(String mobileNoPrimary) {
		MobileNoPrimary = mobileNoPrimary;
	}

	public String getMobileNoSecondary() {
		return MobileNoSecondary;
	}

	public void setMobileNoSecondary(String mObileNoSecondary) {
		MobileNoSecondary = mObileNoSecondary;
	}

	public String getEmailId() {
		return EmailId;
	}

	public void setEmailId(String emailId) {EmailId = emailId;
	}

	public static boolean isOnline(Context context) {
	    
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
		    
	    // or if function is out side of your Activity then you need context of your Activity
	    // and code will be as following
	    // (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo)
	    {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	        {
	            if (ni.isConnected())
	            {
	                haveConnectedWifi = true;
	                System.out.println("WIFI CONNECTION AVAILABLE");
	            } else
	            {
	                System.out.println("WIFI CONNECTION NOT AVAILABLE");
	            }
	        }
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	        {
	            if (ni.isConnected())
	            {
	                haveConnectedMobile = true;
	                System.out.println("MOBILE INTERNET CONNECTION AVAILABLE");
	            } else
	            {
	                System.out.println("MOBILE INTERNET CONNECTION NOT AVAILABLE");
	            }
	        }
	        
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	    
	    /*NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;*/
	    
	   /* if (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	        return true;
	    } else {
	        Log.v("Utils : ", "Internet Connection Not Present");
	        return false;
	    }*/
	    
	}
	
	/*public static void getRegId(final Context ctx){
	   	 Log.d("GCM",  "Start");
	   	if(checkPlayServices(ctx)){
	   	new AsyncTask<Void, Void, String>() {
	   		String reg_id="";
	   	  GoogleCloudMessaging gcm;
	           @Override
	           protected String doInBackground(Void... params) {
	               String msg = "";
	               try {
	                   if (gcm == null) {
	                       gcm = GoogleCloudMessaging.getInstance(ctx);
	                   }
	                   reg_id = gcm.register(AuthenticationMobile.PROJECT_NUMBER);
	                   msg = "Device registered, registration ID=" + reg_id;
	                   Log.d("GCM",  msg);
	                   
	                   Log.d("RegID",":"+reg_id);
	                  
	               } catch (Exception ex) {
	                   msg = "error";
	                   Log.d("GCM error",  ""+ex);
	               }
	               return msg;
	           }

	           @Override
	           protected void onPostExecute(String msg) {
	           	if(msg.equalsIgnoreCase("error")){
	           		try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	           		
	           		
	           	}
	           	else{
	           SharedPreferences	sharedPreferences_ = ctx
	           				.getSharedPreferences(ctx.getString(R.string.shared_preferences_name), 0); // 0 - for private mode
	           		SharedPreferences.Editor editor=sharedPreferences_.edit();
	           		editor.putString("Gcm_reg_id", reg_id);
	           		editor.commit();
	           		
	           	}
	              
	           }
	       }.execute(null, null, null);
	   	}
	   }
		*/
		public static void setupMenu(Context ctx, OnClickListener clk){
								
			SharedPreferences	sharedPreferences1 = ctx.getSharedPreferences(
					ctx.getString(R.string.shared_preferences_renewal), 0);
			
			        // attach to current activity;
			        resideMenu = new ResideMenu(ctx);
			        resideMenu.setBackground(R.drawable.orange_bg);
			        resideMenu.attachToActivity((Activity)ctx);
			       // resideMenu.setMenuListener(menuListener);
			        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
			        resideMenu.setScaleValue(0.6f);

			        // create menu items;
			        itemProfile     = new ResideMenuItem(ctx, R.drawable.profile,  "Profile",true);
			        itemRenewal  = new ResideMenuItem(ctx, R.drawable.rene,  "Renewal",true);
			        if(sharedPreferences1.getBoolean("is_24ol", true)){
			        	
			        }
			        else{
			        	itemSelf_Res = new ResideMenuItem(ctx, R.drawable.self, "Resolution",true);
			        }
			        itemCall_to_CC = new ResideMenuItem(ctx, R.drawable.call, "Call Us",true);
			       // itemChat = new ResideMenuItem(ctx, R.drawable.chat, "Chat",true);
			        itemAlerts     = new ResideMenuItem(ctx, R.drawable.alerts,  "Alerts",true);
			        itemHelp = new ResideMenuItem(ctx, R.drawable.help,  "Help",true);
			        itemShare = new ResideMenuItem(ctx, R.drawable.share, "Share ",true);
			        itemRefer = new ResideMenuItem(ctx, R.drawable.refer_frnd, "Refer a friend ",false);
			        
			        
			        itemProfile1     = new ResideMenuItem(ctx, R.drawable.profile,  "Profile",true);
			        itemRenewal1  = new ResideMenuItem(ctx, R.drawable.rene,  "Renewal",true);
			        if(sharedPreferences1.getBoolean("is_24ol", true)){
			        	
			        }
			        else{
			        	itemSelf_Res1 = new ResideMenuItem(ctx, R.drawable.self, "Resolution",true);
			        }
			        itemCall_to_CC1 = new ResideMenuItem(ctx, R.drawable.call, "Call Us ",true);
			        //itemChat1 = new ResideMenuItem(ctx, R.drawable.chat, "Chat",true);
			        itemAlerts1    = new ResideMenuItem(ctx, R.drawable.alerts,  "Alerts",true);
			        itemHelp1 = new ResideMenuItem(ctx, R.drawable.help,  "Help",true);
			        itemShare1 = new ResideMenuItem(ctx, R.drawable.share, "Share ",true);
			        itemRefer1 = new ResideMenuItem(ctx, R.drawable.refer_frnd, "Refer a friend ",false);

					itemlogout = new ResideMenuItem(ctx,R.drawable.logout, "Logout", true);
					itemlogout1 = new ResideMenuItem(ctx,R.drawable.logout, "Logout", true);

					itemlogout.setOnClickListener(clk);
					itemlogout1.setOnClickListener(clk);


					itemProfile.setOnClickListener(clk);
			        itemRenewal.setOnClickListener(clk);
			       // itemChat.setOnClickListener(clk);
			        itemShare.setOnClickListener(clk);
			        
			        itemAlerts.setOnClickListener(clk);
			        itemHelp.setOnClickListener(clk);
			        itemCall_to_CC.setOnClickListener(clk);
			        if(sharedPreferences1.getBoolean("is_24ol", true)){
			        	
			        }
			        else{
			        	 itemSelf_Res.setOnClickListener(clk);
			        	
			        }
			        itemRefer.setOnClickListener(clk);
			        
			        itemProfile1.setOnClickListener(clk);
			        itemRenewal1.setOnClickListener(clk);
			        //itemChat1.setOnClickListener(clk);
			        itemShare1.setOnClickListener(clk);
			        
			        itemAlerts1.setOnClickListener(clk);
			        itemHelp1.setOnClickListener(clk);
			        itemCall_to_CC1.setOnClickListener(clk);
			        if(sharedPreferences1.getBoolean("is_24ol", true)){
			        	
			        }
			        else{
			        	 itemSelf_Res1.setOnClickListener(clk);
			        	 
			        }
			        itemRefer1.setOnClickListener(clk);

			        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
			        resideMenu.addMenuItem(itemRenewal, ResideMenu.DIRECTION_LEFT);
			        if(sharedPreferences1.getBoolean("is_24ol", true)){
			        	
			        }
			        else{     	
			        	 resideMenu.addMenuItem(itemSelf_Res, ResideMenu.DIRECTION_LEFT);
			        }
			        resideMenu.addMenuItem(itemCall_to_CC, ResideMenu.DIRECTION_LEFT);
			        //resideMenu.addMenuItem(itemChat, ResideMenu.DIRECTION_LEFT);
			        resideMenu.addMenuItem(itemAlerts, ResideMenu.DIRECTION_LEFT);
			        resideMenu.addMenuItem(itemHelp, ResideMenu.DIRECTION_LEFT);
			        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
			        resideMenu.addMenuItem(itemlogout, ResideMenu.DIRECTION_LEFT);

			        resideMenu.addMenuItem(itemRefer, ResideMenu.DIRECTION_LEFT);
			        
			        resideMenu.addMenuItem(itemProfile1, ResideMenu.DIRECTION_RIGHT);
			        resideMenu.addMenuItem(itemRenewal1, ResideMenu.DIRECTION_RIGHT);
			        if(sharedPreferences1.getBoolean("is_24ol", true)){
			        	
			        }
			        else{        	
			        	 resideMenu.addMenuItem(itemSelf_Res1, ResideMenu.DIRECTION_RIGHT);
			        }
			        resideMenu.addMenuItem(itemCall_to_CC1, ResideMenu.DIRECTION_RIGHT);
			       // resideMenu.addMenuItem(itemChat1, ResideMenu.DIRECTION_RIGHT);
			        resideMenu.addMenuItem(itemAlerts1, ResideMenu.DIRECTION_RIGHT);
			        resideMenu.addMenuItem(itemHelp1, ResideMenu.DIRECTION_RIGHT);
			        resideMenu.addMenuItem(itemShare1, ResideMenu.DIRECTION_RIGHT);
			        resideMenu.addMenuItem(itemlogout1, ResideMenu.DIRECTION_RIGHT);
			        resideMenu.addMenuItem(itemRefer1, ResideMenu.DIRECTION_RIGHT);
			       
			        			        
			        // You can disable a direction by setting ->
			        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
			        
			      /*  findViewById(R.id.ivMenuDrawer).setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View view) {
			                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			            }
			        });*/
			       /* findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
			            @Override
			            public void onClick(View view) {
			                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
			            }
			        });*/

			   
		}
		
		public static boolean checkPlayServices(Context ctx) {
		    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(ctx);
		    if (resultCode != ConnectionResult.SUCCESS) {
		        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
		         //   GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity)ctx,
		            //        PLAY_SERVICES_RESOLUTION_REQUEST).show();
		        } else {
		          //  Log.i(TAG, "This device is not supported.");
		           // finish();
		        }
		        return false;
		    }
		    return true;
		}
		
		public static void log(String name,String value){
			if(showLog){
				//Log.d(name,value);
				
				if(value.length() > 4000) {
			        Log.e(name, value.substring(0, 4000));
			       // log(name,value.substring(4000));
			    } else
			        Log.e(name, value);
			}
		}
		
		public static void showAppRater(final Context ctx){
			sharedPreferences_app_rate = ctx.getSharedPreferences(
					ctx.getString(R.string.shared_preferences_name), 0);
			
			
			
			   final Dialog dialog = new Dialog(ctx);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//tell the Dialog to use the dialog.xml as it's layout description
				dialog.setContentView(R.layout.dialog_app_rater);
				
				int width = 0;
				int height =0;
				dialog.setCancelable(true);
				
				    Point size = new Point();
				    WindowManager w =((Activity)ctx).getWindowManager();

				    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				        w.getDefaultDisplay().getSize(size);
				        width = size.x;
				        height = size.y;
				    } else {
				        Display d = w.getDefaultDisplay();
				        width = d.getWidth();
				        height   = d.getHeight();;
				    }
				
				
				

				Button btn_no = (Button) dialog.findViewById(R.id.btn_no);
				Button btn_later = (Button) dialog.findViewById(R.id.btn_later);
				Button btn_rate_now = (Button) dialog.findViewById(R.id.btn_rate_now);

				
				btn_no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences.Editor editor=sharedPreferences_app_rate.edit();
						editor.putString(APP_RATE_STATUS, "no");						
						editor.commit();
						dialog.dismiss();
					}
				});
				
				btn_later.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {						
						dialog.dismiss();
						SharedPreferences.Editor editor=sharedPreferences_app_rate.edit();
						editor.putString(APP_RATE_STATUS, "later");						
						editor.commit();
					}
				});
				
				btn_rate_now.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences.Editor editor=sharedPreferences_app_rate.edit();
						editor.putString(APP_RATE_STATUS, "rate");						
						editor.commit();
						dialog.dismiss();
						final String appPackageName = ctx.getPackageName(); // getPackageName() from Context or Activity object
						try {
							ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
						} catch (android.content.ActivityNotFoundException anfe) {
							ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
						}
					}
				});
			
				dialog.show();
				//(width/2)+((width/2)/2)
				//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
		   
		}
}
