package com.cnergee.mypage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.special.ResideMenu.ResideMenu;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.krishna.ookla.odigitel.R;


public class Profile extends BaseActivity{
	
	public static Context context;
	private boolean flag=true;
	Utils utils = new Utils();
	public static String rslt = "";
	public long memberid;
	
	
	
	TableRow tgoneActivationd,tgoneDoB,tgoneEmailId,tgoneBilgAd,tgonePermaneAdd;
	Button imgcomplete ,imgupdate;
	TextView lblNameValue,lblLoginIDValue,
	lblAlternateNovalue,lblMobileNovalue,
	lblEmailvalue,lblActivationDateValue,
	lblDOBValue,StatusValue,ParmanentAddressValue,
	BillingAddressValue,PackageValue;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private boolean isFinish = false;
	private String sharedPreferences_name;
	public static Map<String, MemberDetailsObj> mapMemberDetails;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	
	boolean isLogout = false;
	public String sharedPreferences_profile;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.myapp_profile);
		
	
		imgcomplete = (Button)findViewById(R.id.img_cmp_profile);
		imgupdate = (Button)findViewById(R.id.img_upd_profile);
		lblNameValue = (TextView)findViewById(R.id.txtname);
		lblLoginIDValue = (TextView)findViewById(R.id.txtloginid);
		lblAlternateNovalue = (TextView)findViewById(R.id.txtalternateno);
		lblMobileNovalue = (TextView)findViewById(R.id.txtmobno);
		lblEmailvalue = (TextView)findViewById(R.id.txtemail);
		lblActivationDateValue = (TextView)findViewById(R.id.txtactivationdate);
		lblDOBValue = (TextView)findViewById(R.id.txtdob);
		StatusValue = (TextView)findViewById(R.id.txtstatus);
		BillingAddressValue = (TextView)findViewById(R.id.txtaddress);
		PackageValue = (TextView)findViewById(R.id.txtpackage);
		ParmanentAddressValue =(TextView)findViewById(R.id.txtparmanentadd);
		linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		tgoneActivationd =(TableRow)findViewById(R.id.tableRow3);
		tgoneDoB = (TableRow)findViewById(R.id.tableRow4);
		tgoneBilgAd = (TableRow)findViewById(R.id.tableRow9);
		tgoneEmailId = (TableRow)findViewById(R.id.tableRow7);
		tgonePermaneAdd = (TableRow)findViewById(R.id.tableRow11);
		context = this;
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		memberid = Long.parseLong(utils.getMemberId());
		
		/*
		 * This SharedPrefernce used to check there is change in profile data
		 */		
		
		  
		
		
		
		SharedPreferences sharedPreferences1 = getApplicationContext()
				.getSharedPreferences(getString(R.string.shared_preferences_profile), 0);
		
		if(sharedPreferences1.getBoolean("profile", true))
		{
			
			
			Utils.log("Profile"," If");
			//Utils.log("Data From server","yes"+sharedPreferences1.getBoolean("profile", true));
			
			if(Utils.isOnline(Profile.this)){
				
				
				
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
					 getMemberDetailWebService = new GetMemberDetailWebService();
					 getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				 }				 
				 else{
				    	getMemberDetailWebService = new GetMemberDetailWebService();
				    	getMemberDetailWebService.execute();
				    }
			}
			
			else{
			Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
			}
		}
		else{
			
			Utils.log("Profile"," else");
			//Utils.log("Data From server","offline"+sharedPreferences1.getBoolean("profile", true));
			String versionName="";
			try {
				versionName = context.getPackageManager()
					    .getPackageInfo(context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setOfflineProfile();
		}
		/*
		 * 
		 */	
		
		
	imgcomplete.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		
		
			tgoneActivationd.setVisibility(View.VISIBLE);
			tgoneDoB.setVisibility(View.VISIBLE);
			tgoneEmailId.setVisibility(View.VISIBLE);
			tgoneBilgAd.setVisibility(View.VISIBLE);
			tgonePermaneAdd.setVisibility(View.VISIBLE);
			
			imgcomplete.setText("Profile Summary");
			
			
			
			if(flag){
				tgoneActivationd.setVisibility(View.VISIBLE);
				tgoneDoB.setVisibility(View.VISIBLE);
				tgoneEmailId.setVisibility(View.VISIBLE);
				tgoneBilgAd.setVisibility(View.VISIBLE);
				tgonePermaneAdd.setVisibility(View.VISIBLE);
				
				
			
				
				flag=false;
			}
			else{
				
				tgoneActivationd.setVisibility(View.GONE);
				tgoneDoB.setVisibility(View.GONE);
				tgoneEmailId.setVisibility(View.GONE);
				tgoneBilgAd.setVisibility(View.GONE);
				tgonePermaneAdd.setVisibility(View.GONE);
				imgcomplete.setText("Complete Profile");
				
				flag=true;
			}
			
		}
	});
		
	imgupdate.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Profile.this.finish();
		Intent i = new Intent(Profile.this,UpdateProfile.class);
		i.putExtra("Mobile_no",lblAlternateNovalue.getText().toString());
		i.putExtra("DoB",lblDOBValue.getText().toString());
		i.putExtra("Email-Id", lblEmailvalue.getText().toString());
		i.putExtra("Address", BillingAddressValue.getText().toString());
		i.putExtra("MobileNo",lblMobileNovalue.getText().toString());
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			
		}
	});
	
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Profile.this.finish();
				Intent i = new Intent(Profile.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
	                    R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*Profile.this.finish();
				Intent i = new Intent(Profile.this,Profile.class);
				startActivity(i);*/
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Profile.this.finish();
				Intent i = new Intent(Profile.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Profile.this.finish();
				Intent i = new Intent(Profile.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		
	
	}
	
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	this.finish();
	    	Intent broadcastIntent=new Intent("com.package.ACTION_LOGOUT");
	    	
	    	sendBroadcast(broadcastIntent);
	        moveTaskToBack(true);
	        
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}*/
	private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> {
		private ProgressDialog Dialog = new ProgressDialog(
				Profile.this);

		protected void onPreExecute() {
			Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.setCancelable(false);
			Dialog.show();
		}


		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			Dialog.dismiss();
			
			if((lblNameValue.length()>0)){
				Utils.log("Onpost", "Visibility Executed");
				imgcomplete.setVisibility(View.VISIBLE);
				lblActivationDateValue.setEnabled(true);
				lblDOBValue.setEnabled(false);
			}
			else{
				Utils.log("Onpost", "Visibility else Executed");	
				tgoneActivationd.setVisibility(View.VISIBLE);
				lblActivationDateValue.setEnabled(true);
				lblDOBValue.setEnabled(false);
			
			}
			
				
			
			//
				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				
				if (mapMemberDetails != null) {
					
					Set<String> keys = mapMemberDetails.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					}
					
					
					String selItem = str_keyVal.trim();
					isLogout = false;

							
					//finish();
					MemberDetailsObj memberDetails = mapMemberDetails.get(selItem);
					
					
/*
					String datedob = memberDetails.getDateofBirth();
					SimpleDateFormat dateFormat = new SimpleDateFormat(
			                "dd-MM-yyyy");
					*/
					
					lblNameValue.setText(memberDetails.getMemberName());
					lblLoginIDValue.setText(memberDetails.getMemberLoginId());
					lblAlternateNovalue.setText(memberDetails.getAlternateNo());

					lblMobileNovalue.setText(memberDetails.getMobileNo());
					lblEmailvalue.setText(memberDetails.getEmailId());
				/*	
					Date date = new Date(memberDetails.getActivationDate());
					java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
					lblActivationDateValue.setText("Time: " + dateFormat.format(date));*/

					String ActivationDate=memberDetails.getActivationDate();
					lblActivationDateValue.setText(memberDetails.getActivationDate());
					lblDOBValue.setText(memberDetails.getDateofBirth());
				
					
					StatusValue.setText(memberDetails.getStatus());
					BillingAddressValue.setText(memberDetails.getInstLocAddressLine1());
					PackageValue.setText(memberDetails.getPackageName());
					ParmanentAddressValue.setText(memberDetails.getInstLocAddressLine2());
					
				
					
					sharedPreferences_profile = getString(R.string.shared_preferences_profile);
					SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(sharedPreferences_profile, 0); // 0 - for private mode
					
					SharedPreferences.Editor editor = sharedPreferences1.edit();
					editor.putString("MemberName",memberDetails.getMemberName());
					editor.putString("MemberLoginId", memberDetails.getMemberLoginId());
					editor.putString("MemberAlternateNo",memberDetails.getAlternateNo());
					
					editor.putString("EmailId",memberDetails.getEmailId() );
					editor.putString("MobileNo",memberDetails.getMobileNo() );
					editor.putString("ActivationDate", memberDetails.getActivationDate());
			//		editor.putString("ActivationDate", memberDetails.getActivationDate());
					
					editor.putString("DateofBirth",memberDetails.getDateofBirth());
					
					editor.putString("Status",memberDetails.getStatus() );
					editor.putString("InstLocAddressLine1", memberDetails.getInstLocAddressLine1());
					editor.putString("InstLocAddressLine2", memberDetails.getInstLocAddressLine2());
					editor.putString("PackageName",memberDetails.getPackageName());
					editor.putBoolean("profile", false);
					editor.commit();
					//Utils.log("saving in",""+sharedPreferences1.getString("MemberName", "not saved"));
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
	
			}catch(Exception e){
					AlertsBoxFactory.showAlert(rslt,context );}
		}
		
	
	
		
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				MemberDetailCaller memberdetailCaller = new MemberDetailCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_SUBSCRIBER_DETAILS)
										);

				memberdetailCaller.memberid = memberid;
				memberdetailCaller.setAllData(true);
				
				memberdetailCaller.join();
				memberdetailCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		 @Override
			protected void onCancelled() {
				Dialog.dismiss();
				getMemberDetailWebService = null;
			}
		}
	
	
	
	
	
	public void setOfflineProfile(){
		sharedPreferences_profile = getString(R.string.shared_preferences_profile);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_profile
						, 0); // 0 - for private mode
		lblNameValue.setText(sharedPreferences.getString("MemberName", "-"));
		lblLoginIDValue.setText(sharedPreferences.getString("MemberLoginId", "-"));
		lblAlternateNovalue.setText(sharedPreferences.getString("MemberAlternateNo", "-"));
		lblMobileNovalue.setText(sharedPreferences.getString("MobileNo", "-"));
		lblEmailvalue.setText(sharedPreferences.getString("EmailId", "-"));
		lblActivationDateValue.setText(sharedPreferences.getString("ActivationDate", "-"));
		lblDOBValue.setText(sharedPreferences.getString("DateofBirth", "-"));
		StatusValue.setText(sharedPreferences.getString("Status", "-"));
		BillingAddressValue.setText(sharedPreferences.getString("InstLocAddressLine1", "-"));
		PackageValue.setText(sharedPreferences.getString("PackageName", "-"));
		ParmanentAddressValue.setText(sharedPreferences.getString("InstLocAddressLine2", "-"));
		
	
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
			
		/*Intent i = new Intent(Profile.this,IONHome.class);
		startActivity(i);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
		if(flag){
			flag=false;
			Toast.makeText(getApplicationContext(), "Press back again to exit.", Toast.LENGTH_LONG).show();
		}
		else{
			this.finish();
		}
		//AlertsBoxFactory.showExitAlert("Do you really want to Exit App", Profile.this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//this.finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
}
