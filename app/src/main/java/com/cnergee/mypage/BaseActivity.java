package com.cnergee.mypage;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;

import all.interface_.IError;
import androidx.fragment.app.FragmentActivity;
import cnergee.krishna.ookla.odigitel.R;

public class BaseActivity extends FragmentActivity implements OnClickListener,IError {
	String class_name="";
	SharedPreferences sharedPreferences1;
	public static boolean isResume=false;
	public static boolean isCompulsory=false;
	public static String is_app_version_checked="";
	Dialog dialog ;
	public static IError iError;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//if(Utils.resideMenu==null)
		Utils.setupMenu(BaseActivity.this, this);
		sharedPreferences1 = getApplicationContext().getSharedPreferences(
				getString(R.string.shared_preferences_renewal), 0);
		Utils.log("Check","Class:"+this.getClass().getSimpleName());
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==Utils.itemProfile){
			Utils.log("Profile","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("Profile")){
				BaseActivity.this.finish();
				Intent i = new Intent(BaseActivity.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		if(v==Utils.itemRenewal){
			Utils.log("Renewal","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("RenewPackage")){
			if (sharedPreferences1.getString("IsFreePackage", "false")
					.equalsIgnoreCase("false")) {
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//BaseActivity.this.finish();
				Intent i = new Intent(BaseActivity.this, RenewPackage.class);
				i.putExtra("renew", "Home");
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {				
				AlertsBoxFactory.showAlert2("This Facility is not available.", BaseActivity.this);
			}
			
				Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}		
		}
		
		if(v==Utils.itemSelf_Res){
			Utils.log("Resolution","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("SelfResolution")){
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//this.finish();
			Intent i = new Intent(BaseActivity.this,SelfResolution.class);
			i.putParcelableArrayListExtra("complaintcategorylist",null );
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		
		if(v==Utils.itemCall_to_CC){
			Utils.log("CC","Clicked");
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
			
			Utils utils=new Utils();
			utils.setSharedPreferences(sharedPreferences);
			String DefaultCustomerCareNo="01140115150";
			String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
			Intent intent = new Intent(Intent.ACTION_CALL); 
			if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
			intent.setData(Uri.parse("tel:"+CustomerCareNo));
			else
			intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
		    startActivity(intent);
		}
		/*if(v==Utils.itemChat){
			Utils.log("Chat","Clicked");
			//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
			//	this.finish();
			Intent i = new Intent(BaseActivity.this,ChatActivity.class);
			startActivity(i);
		}*/
		
		if(v==Utils.itemAlerts){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("NotificationListActivity")){
				this.finish();
				Intent i = new Intent(BaseActivity.this,NotificationListActivity.class);
				startActivity(i);
				Utils.resideMenu.closeMenu();
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		if(v==Utils.itemHelp){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("HelpActivity")){
				this.finish();
				Intent i = new Intent(BaseActivity.this,HelpActivity.class);
				startActivity(i);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}


		if(v==Utils.itemlogout){
			try {
				// clearing app data
				if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
					((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
				} else {
					String packageName = getApplicationContext().getPackageName();
					Runtime runtime = Runtime.getRuntime();
					runtime.exec("pm clear "+packageName);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(v==Utils.itemlogout1 ){
			try {
				// clearing app data
				if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
					((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
				} else {
					String packageName = getApplicationContext().getPackageName();
					Runtime runtime = Runtime.getRuntime();
					runtime.exec("pm clear "+packageName);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(v==Utils.itemShare){
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		       sharingIntent.setType("plain/text");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link));
			startActivity(Intent.createChooser(sharingIntent,"ODiGitel Broadband App"));
		       Utils.resideMenu.closeMenu();
		}
		
		if(v==Utils.itemProfile1){
			Utils.log("Profile","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("Profile")){
				BaseActivity.this.finish();
				Intent i = new Intent(BaseActivity.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		if(v==Utils.itemRenewal1){
			Utils.log("Renewal","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("RenewPackage")){
			if (sharedPreferences1.getString("IsFreePackage", "false")
					.equalsIgnoreCase("false")) {
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//BaseActivity.this.finish();
				Intent i = new Intent(BaseActivity.this, RenewPackage.class);
				Utils.pg_sms_request=false;
				Utils.pg_sms_uniqueid="";
				i.putExtra("renew", "Home");
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {				
				AlertsBoxFactory.showAlert2("This Facility is not available.", BaseActivity.this);
			}
			
				Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		
		}
		
		if(v==Utils.itemSelf_Res1){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("SelfResolution")){
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//this.finish();
			Intent i = new Intent(BaseActivity.this,SelfResolution.class);
			i.putParcelableArrayListExtra("complaintcategorylist",null );
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		
		if(v==Utils.itemCall_to_CC1){
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
			
			Utils utils=new Utils();
			utils.setSharedPreferences(sharedPreferences);
			String DefaultCustomerCareNo="01140115150";
			String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
			Intent intent = new Intent(Intent.ACTION_CALL); 
			if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
			intent.setData(Uri.parse("tel:"+CustomerCareNo));
			else
			intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
		    startActivity(intent);
		}
		/*if(v==Utils.itemChat1){
			//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
			//	this.finish();
			Intent i = new Intent(BaseActivity.this,ChatActivity.class);
			startActivity(i);
		}*/
		
		if(v==Utils.itemAlerts1){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("NotificationListActivity")){
				this.finish();
				Intent i = new Intent(BaseActivity.this,NotificationListActivity.class);
				startActivity(i);
				Utils.resideMenu.closeMenu();
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		if(v==Utils.itemHelp1){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("HelpActivity")){
				this.finish();
				Intent i = new Intent(BaseActivity.this,HelpActivity.class);
				startActivity(i);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		}
		if(v==Utils.itemShare1){
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("plain/text");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link ));
			startActivity(Intent.createChooser(sharingIntent,"ODiGitel Broadband App"));
		    Utils.resideMenu.closeMenu();
		}
		
		if(v==Utils.itemRefer){
			Intent i = new Intent(BaseActivity.this,Refer_FrndActivity.class);
			startActivity(i);
			//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			Utils.resideMenu.closeMenu();
		     
		}
		
		if(v==Utils.itemRefer1){
			
			if(!this.getClass().getSimpleName().equalsIgnoreCase("Refer_FrndActivity")){
				this.finish();
			Intent i = new Intent(BaseActivity.this,Refer_FrndActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			Utils.resideMenu.closeMenu();
			}
			else{
				Utils.resideMenu.closeMenu();
			}
		     
		}
		
		Utils.resideMenu.closeMenu();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(Utils.resideMenu.isOpened()){
			Utils.resideMenu.closeMenu();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Utils.setupMenu(BaseActivity.this, this);
		isResume=true;
		if(isCompulsory){
			showUpdateDialog(true);
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//Utils.resideMenu=null;
		isResume=false;
		if(dialog!=null){
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
	}
	
	public void showUpdateDialog(boolean isCompulsory){
		dialog	= new Dialog(BaseActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.update_dialog);
		/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);*/
		
	
		int width = 0;
		int height =0;
		dialog.setCancelable(false);
		
		    Point size = new Point();
		    WindowManager w =((Activity)BaseActivity.this).getWindowManager();

		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		        w.getDefaultDisplay().getSize(size);
		        width = size.x;
		        height = size.y;
		    } else {
		        Display d = w.getDefaultDisplay();
		        width = d.getWidth();
		        height   = d.getHeight();;
		    }
		
		TextView	tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);

		TextView txt = (TextView) dialog.findViewById(R.id.tvMessage);
		LinearLayout llUpdate=(LinearLayout)dialog.findViewById(R.id.llUpdate);
		LinearLayout llCompulsoryUpdate=(LinearLayout)dialog.findViewById(R.id.llCompulsoryUpdate);
		Button btnUpdate=(Button)dialog.findViewById(R.id.btnUpdate);
		Button btnCancel=(Button)dialog.findViewById(R.id.btnCancel);
		Button btnCompulsoryUpdate=(Button)dialog.findViewById(R.id.btnCompulsoryUpdate);
		
		if(isCompulsory){
			tvTitle.setText(Html.fromHtml("Mandatory Update"));
			txt.setText((getString(R.string.compulsory_update_msg)));
			llUpdate.setVisibility(View.GONE);
			llCompulsoryUpdate.setVisibility(View.VISIBLE);
			dialog.setCancelable(false);
		}
		else{
			tvTitle.setText(Html.fromHtml("INFO"));
			txt.setText((getString(R.string.update_msg)));
			llCompulsoryUpdate.setVisibility(View.GONE);
			llUpdate.setVisibility(View.VISIBLE);
			dialog.setCancelable(true);
		}
		

		
		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
			}
		});
	
		btnCompulsoryUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
				try {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});
		
		dialog.show();
		//(width/2)+((width/2)/2)
		//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);	
	}
	
	@Override
	public void display() {
		// TODO Auto-generated method stub
		Utils.log("Error", "view");
		LayoutInflater inflater= (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(R.layout.activity_error_page,null);
	
		
		setContentView(view);
		TextView tv_go_back=(TextView)view.findViewById(R.id.tv_go_back);
		tv_go_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseActivity.this.finish();
			}
		});
	}
}
