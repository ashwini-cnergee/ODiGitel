package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.GetCurrentVersionSOAP;
import com.cnergee.mypage.SOAP.PAckageDetailSOAP;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.sys.AlarmBroadcastReceiver;
import com.cnergee.mypage.sys.ExpiryBroadcastReceiver;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.krishna.ookla.odigitel.R;


@SuppressLint("CommitPrefEdits")
public class MyAppHome extends Activity implements OnClickListener  {

	public static Context context;
	Utils utils = new Utils();
	private boolean flag = true;
	public static String rslt = "";
	public String memberloginid;
	TextView txtPackage, txtamount, txtvalidity, txtpkgexpiry, txtUser,
			txtdayremaing;
	ImageView btnrenewal;
	LinearLayout linnhome, linnprofile, linnnotification, linnhelp;

	/*LinearLayout relativepaymenthistory, relativedatausage,
			relativeservicerequest, relativeupgradepackage,
			relativepickuprequest,  relativeTopup;*/
	
	RelativeLayout relativepaymenthistory, relativedatausage,
	relativeservicerequest, relativeupgradepackage,
	relativepickuprequest,  relativeTopup;
	public boolean check_call=true;
	//relativerenewal
	LinearLayout relativerenewal;
	//ProgressDialog mProgressDialog;
	ProgressHUD	mProgressHUD ;
	private String sharedPreferences_name;
	private String sharedPreferences_renewal;
	public static String strXML = "";
	public static String TAG = "IONHome";
	public static String rsltUsername = "";
	public static String Username = "";
	public static String Password = "";
	// private boolean isFinish = false;
	public String s = "http://ionhungama.com/api_login.php";

	static String extStorageDirectory = Environment
			.getExternalStorageDirectory().toString();
	final static String xml_folder = "mypage";
	final static String TARGET_BASE_PATH = extStorageDirectory + "/"
			+ xml_folder + "/";

	public String xml_file_postFix = "PackageList.xml";
	public String xml_file;
	public String xml_file_with_path;

	public static Map<String, PackageDetails> mapPackageDetails;
	private GetMemberDetailWebService getMemberDetailWebService = null;

	boolean isLogout = false;
	IntentFilter intentFilter = new IntentFilter();
	BroadcastReceiver broadcastReceiver;
	SharedPreferences sharedPreferences1;
/*private ResideMenu resideMenu;
	
	private ResideMenuItem itemProfile;
	private ResideMenuItem itemRenewal;
	private ResideMenuItem itemSelf_Res;
	private ResideMenuItem itemCall_to_CC;	
	private ResideMenuItem itemChat;
	private ResideMenuItem itemAlerts;
	private ResideMenuItem itemHelp;
	private ResideMenuItem itemShare;*/

	ImageView ivMenuDrawer;
	String AppVersion="0";
	public static boolean is_home_running=false;
	
	public static ResideMenu resideMenu;
	public static Boolean showLog=true;;
		
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
	String MemberId="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.test_myapp_home);
		
		Utils.log("Density","is:"+getResources().getDisplayMetrics().density);

		Utils.log(""+this.getClass().getSimpleName(),"is:");
		intentFilter.addAction("com.package.ACTION_LOGOUT");
		txtUser = (TextView) findViewById(R.id.tv_usertext);
		txtPackage = (TextView) findViewById(R.id.txtPackage);
		txtamount = (TextView) findViewById(R.id.txtamount);
		txtvalidity = (TextView) findViewById(R.id.txtvalidity);
		txtpkgexpiry = (TextView) findViewById(R.id.txtpkgexpiry);
		txtdayremaing = (TextView) findViewById(R.id.txtdayremainig);

		linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);
		ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		// btnrenewal = (ImageView)findViewById(R.id.btnrenewal);
		context = this;
		
		setupMenu(MyAppHome.this, this);
		
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(resideMenu.isOpened()){
					Utils.log(this.getClass().getSimpleName()+"","close");
					resideMenu.closeMenu();
				}
				else{
					Utils.log(this.getClass().getSimpleName()+"","open");
					resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
				}
			}
		});
		
		Intent i = getIntent();
		SharedPreferences sharedPreferences2 = context.getSharedPreferences(
				"CNERGEE_BILLING", 0);
		/*
		 * if(sharedPreferences2.getBoolean("check_expiry", true)){
		 * //Utils.log("start","service home"); Intent startServiceIntent=new
		 * Intent(this,CheckExpiryService.class);
		 * startService(startServiceIntent); }
		 */
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			AppVersion= pInfo.versionName;				
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
											// private
																	// mode
		
		utils.setSharedPreferences(sharedPreferences);
		memberloginid = utils.getMemberLoginID();
		MemberId=utils.getMemberId();
		Utils.log("MemberId",":"+memberloginid);
		
		/*
		 * This SharedPrefernce used to check there is change in profile data
		 */
		sharedPreferences1 = getApplicationContext().getSharedPreferences(
				getString(R.string.shared_preferences_renewal), 0);

		if (sharedPreferences1.getBoolean("renewal", true)) {
			 Utils.log("Data From server IONHome ","yes"+sharedPreferences1.getBoolean("renewal",
			 true));
			if (Utils.isOnline(MyAppHome.this)) {
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 {
					 getMemberDetailWebService = new GetMemberDetailWebService();
					 getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						
				 }
				 else{
					 getMemberDetailWebService = new GetMemberDetailWebService();
					 getMemberDetailWebService.execute((String) null);
				 }
				
			} else {
				Toast.makeText(getApplicationContext(),
						"Please connect to internet and try again!!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			 Utils.log("Data From server IONHome","offline "+sharedPreferences1.getBoolean("renewal",
			 true));
			setOfflineRenewalData();
			if(Utils.isOnline(MyAppHome.this)){
				
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				    else
				    new GetCalciVersion().execute();
				 
				SharedPreferences	sharedPreferences_ = getApplicationContext()
	    				.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
				if(sharedPreferences_.getString("Gcm_reg_id", "").length()>0){
					Utils.log("Reg id ","SharedPreference:"+sharedPreferences_.getString("Gcm_reg_id", ""));
					
				}
				else{
					//Utils.getRegId(MyAppHome.this);
				}
			}
		
			
			// *********************for expiry service****************

			SharedPreferences sharedPreferences3 = getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
																		// private
																		// mode
			if (sharedPreferences3.getBoolean("check_expiry", true)) {
				Intent intentService2 = new Intent(MyAppHome.this,
						ExpiryBroadcastReceiver.class);

				PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
						intentService2, 0);
				AlarmManager alarm2 = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);

				int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
				Calendar calendar1 = new GregorianCalendar();
				calendar1.set(Calendar.HOUR_OF_DAY, 8);
				calendar1.set(Calendar.MINUTE, 05);
				calendar1.set(Calendar.SECOND, 0);
				calendar1.set(Calendar.MILLISECOND, 0);

				long triggerMillis = calendar1.getTimeInMillis();

				if (calendar1.getTimeInMillis() < Calendar.getInstance()
						.getTimeInMillis()) {
					triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
					//System.out.println("Alarm will go off next day");
				}

				alarm2.cancel(pintent2);
				if (sharedPreferences3.getBoolean("check_expiry", true)) {
					alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
							calendar1.getTimeInMillis(),
							AlarmManager.INTERVAL_DAY, pintent2);
				} else {

					alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
							AlarmManager.INTERVAL_DAY, pintent2);
				}
			}
			// *********************for expiry service****************

			// *************** for daysRemaining*****************

			/*
			 * else{
			 * 
			 * setOfflineRenewalData();
			 * 
			 * SharedPreferences sharedPreferences1 = context
			 * .getSharedPreferences
			 * (context.getString(R.string.shared_preferences_renewal), 0);
			 * String daysremaining
			 * =sharedPreferences1.getString("DaysRemaining", "0");
			 * txtdayremaing.setText(daysremaining);
			 * 
			 * }
			 */

		}

		/*
		 * 
		 */
		/*
		 * getMemberDetailWebService = new GetMemberDetailWebService();
		 * getMemberDetailWebService.execute((String) null);
		 */

		xml_file_with_path = TARGET_BASE_PATH + xml_file_postFix;
		File file = new File(xml_file_with_path);
		file.delete();
		// boolean deleted = file.delete();

		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*IONHome.this.finish();
				Intent i = new Intent(IONHome.this, IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);*/
			}
		});

		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyAppHome.this.finish();
				Intent i = new Intent(MyAppHome.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyAppHome.this.finish();
				Intent i = new Intent(MyAppHome.this,
						NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});

		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				MyAppHome.this.finish();
				Intent i = new Intent(MyAppHome.this, HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		BaseApplication.getEventBus().register(this);
		// Utils.Last_Class_Name=this.getClass().getSimpleName();
		/*
		 * btnrenewal.setOnClickListener(new OnClickListener() { public void
		 * onClick(View v) {
		 * 
		 * Intent i = new Intent(IONHome.this,RenewPackage.class);
		 * startActivity(i); } });
		 */

		relativepaymenthistory = (RelativeLayout) findViewById(R.id.relativepaymenthistory);
		relativedatausage = (RelativeLayout) findViewById(R.id.relativedatausage);
		relativeservicerequest = (RelativeLayout) findViewById(R.id.relativeservicerequest);
		relativeupgradepackage = (RelativeLayout) findViewById(R.id.relativeupgradepdackage);
		relativepickuprequest = (RelativeLayout) findViewById(R.id.relativepickuprequest);
		relativerenewal = (LinearLayout) findViewById(R.id.relativerenewal);

		/*relativeTopup = (RelativeLayout) findViewById(R.id.relativeTopup);

		relativeTopup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(sharedPreferences1.getString("isFreePackage" , "false").equalsIgnoreCase("false")){
					if(Utils.isOnline(MyAppHome.this)){
					//IONHome.this.finish();
					Intent i = new Intent(MyAppHome.this,TopupActivity.class);
					i.putExtra("check_intent", false);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
					}else{
						
						Toast.makeText(getApplicationContext(), "Please coonect to internet and try again !!", Toast.LENGTH_LONG).show();
					}
					
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(MyAppHome.this);
					
				  builder.setMessage("This Facility is not available.")
					.setTitle("Alert")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener(){
						public void onClick(
							DialogInterface dialog,int id){
						}
					});
				AlertDialog alert = builder.create();
				}
			}
		});*/
		relativedatausage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//	IONHome.this.finish();
					Intent i = new Intent(MyAppHome.this, MyappDataUsage.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
				
			}
		});

		relativepaymenthistory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//IONHome.this.finish();
				Intent i = new Intent(MyAppHome.this, PaymentHistory.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
			}
		});

		relativeservicerequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			if(sharedPreferences1.getString("isFreePackage" , "false").equalsIgnoreCase("false")){
			if(Utils.isOnline(MyAppHome.this)){
			//IONHome.this.finish();
			Intent i = new Intent(MyAppHome.this,Complaints.class);
			i.putExtra("check_intent", false);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
			
			}else{
				
				Toast.makeText(getApplicationContext(), "Please coonect to internet and try again !!", Toast.LENGTH_LONG).show();
			}
			
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(MyAppHome.this);
			
		  builder.setMessage("This Facility is not available.")
			.setTitle("Alert")
			.setCancelable(false)
			.setPositiveButton("OK",
					new DialogInterface.OnClickListener(){
				public void onClick(
					DialogInterface dialog,int id){
				}
			});
		AlertDialog alert = builder.create();
		}
			}
			});

		relativeupgradepackage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sharedPreferences1.getString("IsFreePackage", "false")
						.equalsIgnoreCase("false")) {
					if (Utils.isOnline(MyAppHome.this)) {
						//IONHome.this.finish();
						Intent i = new Intent(MyAppHome.this,PlanCalculatorActivity.class);
						i.putExtra("check_intent", false);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
					} else {
						Toast.makeText(getApplicationContext(),
								"Please connect to internet and try again!!",
								Toast.LENGTH_LONG).show();
					}
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MyAppHome.this);
					builder.setMessage("This Facility is not available.")
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();

										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

		relativepickuprequest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sharedPreferences1.getString("IsFreePackage", "false")
						.equalsIgnoreCase("false")) {
					if (Utils.isOnline(MyAppHome.this)) {
						//IONHome.this.finish();
						// Intent i = new
						// Intent(IONHome.this,PymentPickupRequest.class);
						Intent i = new Intent(MyAppHome.this,
								PaymentPickup_New.class);
						i.putExtra("check_intent", false);
						startActivity(i);
						overridePendingTransition(R.anim.slide_in_right,
								R.anim.slide_out_left);
						
						Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
						// new firstRunTask().execute();
					} else {
						Toast.makeText(getApplicationContext(),
								"Please connect to internet and try again!!",
								Toast.LENGTH_LONG).show();
					}
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							MyAppHome.this);
					builder.setMessage("This Facility is not available.")
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();

										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
		});

		relativerenewal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sharedPreferences1.getString("IsFreePackage", "false")
						.equalsIgnoreCase("false")) {
					//IONHome.this.finish();
					Intent i = new Intent(MyAppHome.this, RenewPackage.class);
					i.putExtra("renew", "Home");
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					Utils.Last_Class_Name=MyAppHome.this.getClass().getSimpleName();
				} else {
					/*AlertDialog.Builder builder = new AlertDialog.Builder(
							IONHome.this);
					builder.setMessage("This Facility is not available.")
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();

										}
									});
					AlertDialog alert = builder.create();
					alert.show();*/
					AlertsBoxFactory.showAlert2("This Facility is not available.", MyAppHome.this);
				}
			}
		});

		/*
		 * Intent intentService = new Intent(IONHome.this,MyPageService.class);
		 * 
		 * Calendar cal = Calendar.getInstance(); PendingIntent pintent =
		 * PendingIntent.getService(context, 0, intentService, 0);
		 * 
		 * AlarmManager alarm =
		 * (AlarmManager)getSystemService(Context.ALARM_SERVICE); // Start every
		 * 30 seconds //alarm.setRepeating(AlarmManager.RTC_WAKEUP,
		 * cal.getTimeInMillis(), 30*1000, pintent); // Start every 1mon..
		 * alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		 * 60000*2, pintent);
		 */

		// ***********************
		Intent intentService1 = new Intent(MyAppHome.this,
				AlarmBroadcastReceiver.class);

		PendingIntent pintent1 = PendingIntent.getBroadcast(context, 0,
				intentService1, 0);
		Calendar cal = Calendar.getInstance();
		AlarmManager alarm1 = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// Start every 30 seconds
		// alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// 30*1000, pintent);
		// Start every 1mon..
		alarm1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				60000 * 2, pintent1);
		// ********************************
		check_call=false;
	}

	
	
	
	 
	private class GetMemberDetailWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener {
		//private ProgressDialog Dialog = new ProgressDialog(IONHome.this);

		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(MyAppHome.this,getString(R.string.app_please_wait_label), true,true,this);
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			mProgressHUD.dismiss();

			try {
				if (rslt.trim().equalsIgnoreCase("ok")) {
					if (mapPackageDetails != null) {

						Set<String> keys = mapPackageDetails.keySet();
						String str_keyVal = "";

						for (Iterator<String> i = keys.iterator(); i.hasNext();) {
							str_keyVal = (String) i.next();

						}
						String selItem = str_keyVal.trim();
						isLogout = false;
						// finish();
						
						
						PackageDetails packageDetails = mapPackageDetails
								.get(selItem);
						
						txtUser.setText(packageDetails.getMemberName());
						txtPackage.setText(packageDetails.getPackageName());
						txtamount.setText(packageDetails.getAmount());
						txtvalidity.setText(packageDetails.getPackageValidity()
								+ " Days");
						
					// String PackageExpiry=packageDetails.getExpiryDate();
					// String pac
					 
					 txtpkgexpiry.setText(packageDetails.getExpiryDate());
						 txtUser.setText(packageDetails.getSubscriberName());

						sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
						SharedPreferences sharedPreferences1 = getApplicationContext()
								.getSharedPreferences(
										sharedPreferences_renewal, 0); // 0 -
																		// for
																		// private
																		// mode

						SharedPreferences.Editor editor = sharedPreferences1
								.edit();
						editor.putString("SubscriberName",
								packageDetails.getSubscriberName());
						editor.putString("MemberName",
								packageDetails.getMemberName());
						editor.putString("PackageName",
								packageDetails.getPackageName());
						editor.putString("Amount", packageDetails.getAmount());
						editor.putString("PackageValidity",
								packageDetails.getPackageValidity());
						editor.putString("ExpiryDate",
								packageDetails.getExpiryDate());
						editor.putString("DaysRemaining",
								packageDetails.getDaysRemaining());
						editor.putString("ServiceTax",
								packageDetails.getServiceTax());
						editor.putString("AreaCode",
								packageDetails.getAreaCode());
						editor.putString("IsFreePackage",
								packageDetails.getIsFreePackage());
						editor.putBoolean("renewal", false);
						editor.commit();

						if (packageDetails.getIsFreePackage().equalsIgnoreCase(
								"1")) {

						} else {

						}
						getRemainigDays();
						
						 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							 new GetCalciVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
						    else
						    new GetCalciVersion().execute();

					}
				} else if (rslt.trim().equalsIgnoreCase("not")) {
					AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",
							context);
				} else {
					AlertsBoxFactory.showAlert(rslt, context);
				}

			} catch (Exception e) {
				AlertsBoxFactory.showAlert(rslt, context);
			}
			
			
		SharedPreferences	sharedPreferences_ = getApplicationContext()
    				.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
			if(sharedPreferences_.getString("Gcm_reg_id", "").length()>0){
				Utils.log("Reg id","is:"+sharedPreferences_.getString("Gcm_reg_id", ""));
				
			}
			else{
				//Utils.getRegId(MyAppHome.this);
			}
			/*
			 * Intent intentService1 = new
			 * Intent(IONHome.this,ExpiryBroadcastReceiver.class);
			 * 
			 * SharedPreferences sharedPreferences = getApplicationContext()
			 * .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
			 * private mode
			 * 
			 * PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
			 * intentService1, 0); AlarmManager alarm2 =
			 * (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			 * 
			 * int INTERVAL_DAY1 = 24 * 60 * 60 * 1000; Calendar calendar1 = new
			 * GregorianCalendar(); calendar1.set(Calendar.HOUR_OF_DAY, 8);
			 * calendar1.set(Calendar.MINUTE, 05);
			 * calendar1.set(Calendar.SECOND, 0);
			 * calendar1.set(Calendar.MILLISECOND, 0);
			 * 
			 * long triggerMillis = calendar1.getTimeInMillis();
			 * 
			 * 
			 * if (calendar1.getTimeInMillis() < Calendar.getInstance()
			 * .getTimeInMillis()) { triggerMillis = calendar1.getTimeInMillis()
			 * + INTERVAL_DAY1;
			 * System.out.println("Alarm will go off next day"); }
			 * 
			 * alarm2.cancel(pintent2);
			 * if(sharedPreferences.getBoolean("check_expiry", true)) {
			 * alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
			 * calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
			 * pintent2); } else {
			 * 
			 * alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
			 * AlarmManager.INTERVAL_DAY, pintent2); }
			 */
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				/*PackageDetailCaller packagedetailCaller = new PackageDetailCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_PACKAGE_DETAILS));

				packagedetailCaller.memberloginid = memberloginid;

				packagedetailCaller.join();
				packagedetailCaller.start();*/
				
				PAckageDetailSOAP packageDetailSOAP= new PAckageDetailSOAP(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_PACKAGE_DETAILS));
				
				MyAppHome.rslt = packageDetailSOAP.CallSearchMemberSOAP(memberloginid);
				MyAppHome.mapPackageDetails = packageDetailSOAP.getMapPackageDetails();
				
				//rslt = "START";

			/*	while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			getMemberDetailWebService = null;
		}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		is_home_running=true;
		memberloginid = utils.getMemberLoginID();
		if(memberloginid.length()>0){
			
		}
		else{
			this.finish();
		}
		  try {
			  
			
		/*if(!check_call)
		Utils.setupMenu(IONHome.this, this);*/
			  
	//	else
	//		Utils.log("Reside Menu","not null");
		ImageView ivLogo = (ImageView) findViewById(R.id.imgdvois);

		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
																	// private
																	// mode
		/*if (sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/

		// if(sharedPreferences.getBoolean("check_expiry", true))
		// {
		Intent intentService1 = new Intent(MyAppHome.this,
				ExpiryBroadcastReceiver.class);

		PendingIntent pintent2 = PendingIntent.getBroadcast(context, 0,
				intentService1, 0);
		/*
		 * Calendar calendar = Calendar.getInstance();
		 * calendar.set(Calendar.HOUR_OF_DAY, 8); calendar.set(Calendar.MINUTE,
		 * 05); calendar.set(Calendar.SECOND, 00);
		 * 
		 * AlarmManager alarm2 =
		 * (AlarmManager)context.getSystemService(Context.ALARM_SERVICE); //
		 * Start every 30 seconds //alarm.setRepeating(AlarmManager.RTC_WAKEUP,
		 * cal.getTimeInMillis(), 30*1000, pintent 24*60*60*1000); // Start
		 * every 1mon.. //alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
		 * calendar.getTimeInMillis(), 24*60*60*1000, pintent2);
		 * alarm2.cancel(pintent2); alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
		 * calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent2);
		 */

		AlarmManager alarm2 = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		int INTERVAL_DAY1 = 24 * 60 * 60 * 1000;
		Calendar calendar1 = new GregorianCalendar();
		calendar1.set(Calendar.HOUR_OF_DAY, 8);
		calendar1.set(Calendar.MINUTE, 05);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		long triggerMillis = calendar1.getTimeInMillis();

		if (calendar1.getTimeInMillis() < Calendar.getInstance()
				.getTimeInMillis()) {
			triggerMillis = calendar1.getTimeInMillis() + INTERVAL_DAY1;
			//System.out.println("Alarm will go off next day");
		}

		alarm2.cancel(pintent2);
		if (sharedPreferences.getBoolean("check_expiry", true)) {
			alarm2.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
					pintent2);
		} else {
			alarm2.setRepeating(AlarmManager.RTC_WAKEUP, triggerMillis,
					AlarmManager.INTERVAL_DAY, pintent2);
		}
		  }catch(StackOverflowError t) {
	            // more general: catch(Error t)
	            // anything: catch(Throwable t)
	           // System.out.println("Caught "+t);
	            t.printStackTrace();
	        }
	}

	

	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// this.finish();
		is_home_running=false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (flag) {
			flag = false;
			Toast.makeText(getApplicationContext(),
					"Press back again to exit.", Toast.LENGTH_LONG).show();
		} else {
			this.finish();
		}
	}

	/*
* 
*/

	private void setOfflineRenewalData() {
		// TODO Auto-generated method stub
		sharedPreferences_renewal = getString(R.string.shared_preferences_renewal);
		SharedPreferences sharedPreferences2 = getApplicationContext()
				.getSharedPreferences(sharedPreferences_renewal, 0); // 0 - for
																		// private
																		// mode

		txtPackage.setText(sharedPreferences2.getString("PackageName", "-"));
		txtUser.setText(sharedPreferences2.getString("SubscriberName", "-"));

		txtamount.setText(sharedPreferences2.getString("Amount", "-"));
		txtvalidity.setText(sharedPreferences2
				.getString("PackageValidity", "-") + "  Days");

		// String PackageExpiry=packageDetails.getExpiryDate();
		txtpkgexpiry.setText(sharedPreferences2.getString("ExpiryDate", "-"));

		txtdayremaing.setText(sharedPreferences2
				.getString("DaysRemaining", "-"));

		String sharedPreferences_profile = getString(R.string.shared_preferences_profile);

		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_profile, 0); // 0 - for
																		// private
																		// mode
		//txtUser.setText(sharedPreferences.getString("MemberName", "-"));
		getRemainigDays();
		// txtUser.setText(sharedPreferences2.getString("MemberName", "-"));

	}
	
	public void getRemainigDays() {
		int expDay, expMonth, expYear;
		SharedPreferences sharedPreferences6 = context.getSharedPreferences(
				context.getString(R.string.shared_preferences_renewal), 0);
		String expDate = sharedPreferences6.getString("ExpiryDate", "0");
		if (expDate != "0") {
			String arrDate[] = expDate.split("-");
			expDay = Integer.valueOf(arrDate[0]);
			expMonth = Integer.valueOf(arrDate[1]);
			expYear = Integer.valueOf(arrDate[2]);

			SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date d = null;
			java.util.Date d1 = null;
			Calendar cal1 = Calendar.getInstance();
			try {
				d = dfDate.parse(expDay + "/" + expMonth + "/" + expYear);
				Utils.log("expiry date", "is:" + d);

				d1 = dfDate.parse(dfDate.format(cal1.getTime()));// Returns
																	// 15/10/2012
				Utils.log("todays date", "is:" + d1);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

			int diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));

			Utils.log("Remaining Days", "are:" + diffInDays);
			if (diffInDays > 0) {
				txtdayremaing.setText(diffInDays + " Days");
				txtdayremaing.setTextColor(Color.parseColor("#006633"));
			} else {
				txtdayremaing.setText("Expired");
				txtdayremaing.setTextColor(Color.parseColor("#FF0000"));
			}
		}
	}





	

	/*private class GetUsernamePwdWebService extends
			AsyncTask<String, Void, Void> {
		//private ProgressDialog Dialog = new ProgressDialog(IONHome.this);

		protected void onPreExecute() {
			Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.show();
		}

		protected void onPostExecute(Void unused) {
			// Utils.log("Status","onPostExecute");
			mProgressHUD.dismiss();

			try {
				if (rsltUsername.trim().equalsIgnoreCase("ok")) {
					IONHome.this.finish();
					Intent i = new Intent(IONHome.this,
							SpecialOfferActivity.class);
					i.putExtra("username", Username);
					i.putExtra("password", Password);
					startActivity(i);
				} else if (rsltUsername.trim().equalsIgnoreCase("not")) {
					IONHome.this.finish();
					Intent i = new Intent(IONHome.this,
							SpecialOfferActivity.class);
					i.putExtra("username", Username);
					i.putExtra("password", Password);
					startActivity(i);
				}
			} catch (Exception e) {
				IONHome.this.finish();
				Intent i = new Intent(IONHome.this, SpecialOfferActivity.class);
				i.putExtra("username", Username);
				i.putExtra("password", Password);
				startActivity(i);
			}

		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {

				GetUsernamePwdCaller getUsernamePwdCaller = new GetUsernamePwdCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_USERNAME_PASSWORD));

				getUsernamePwdCaller.setMemberId(utils.getMemberId());
				getUsernamePwdCaller.join();
				getUsernamePwdCaller.start();
				rsltUsername = "START";

				while (rsltUsername == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				Utils.log("i m Catch", "Aync task mainbck");
			}
			// Utils.log("Status","doInBackground");
			return null;
		}

		@Override
		protected void onCancelled() {
			DiaUtils.dismiss();
			
			 * collectBtn.setClickable(true); getTodaysCollectionWebService =
			 * null;
			 
		}

	}*/

	// }
		/*
		 * @Override public void onBackPressed() { // TODO Auto-generated method
		 * stub //super.onBackPressed(); this.finish(); }
		 */

		/*
		 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
		 * (keyCode == KeyEvent.KEYCODE_BACK) { this.finish();
		 * android.os.Process.killProcess(android.os.Process.myPid());
		 * moveTaskToBack(true);
		 * 
		 * 
		 * return true; } return super.onKeyDown(keyCode, event); }
		 */

	@Subscribe
	public void	onFinishEvent(FinishEvent event){
		Utils.log(""+MyAppHome.this.getClass().getSimpleName(),":"+event.getMessage());
		Utils.log(""+MyAppHome.this.getClass().getSimpleName(),"::"+Utils.Last_Class_Name);
		if(MyAppHome.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			MyAppHome.this.finish();
		}
		else{
			
		}
		
	}
	
	
	public class GetCalciVersion extends AsyncTask<String, Void, Void>  {
		
		String calc_version_result="",calc_version_response="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetCurrentVersionSOAP getCurrentVersionsoap= new GetCurrentVersionSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_CURRENT_VERSION));
			try {
				calc_version_result=getCurrentVersionsoap.getCurrentVersion("A", AppVersion,MemberId);
				calc_version_response=getCurrentVersionsoap.getResponse();
				
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				calc_version_result="Internet is too slow";
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				calc_version_result="Internet is too slow";
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				calc_version_result="Please try again!!";
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(sharedPreferences_name, 0);
			if(calc_version_result.length()>0){
				if(calc_version_result.equalsIgnoreCase("ok")){
					Utils.log("Version",":"+calc_version_response);
					String version_response[]=calc_version_response.split("#");
					if(version_response.length>1){
						
						if(version_response[1].equalsIgnoreCase(AppVersion)){
							
						}
						else{
														
							if(version_response[1].equalsIgnoreCase("True")){
							/*	SharedPreferences.Editor edit=sharedPreferences.edit();
								edit.putString("App_Version", version_response[1]);
								edit.commit();*/
								
								BaseActivity.isCompulsory=true;
								showUpdateDialog(true);
							}
							if(version_response[1].equalsIgnoreCase("False")){
								
								/*SharedPreferences.Editor edit=sharedPreferences.edit();
								edit.putString("App_Version", version_response[1]);
								edit.commit();*/
								
								BaseActivity.isCompulsory=false;
								showUpdateDialog(false);
								
							}
						}
					}
				}
			}
		}

	}
	
	public void showUpdateDialog(boolean isCompulsory){
		final Dialog	dialog	= new Dialog(MyAppHome.this);
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
		    WindowManager w =((Activity)MyAppHome.this).getWindowManager();

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
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);	
	}
	
	public static void setupMenu(Context ctx, OnClickListener clk){
		
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
        itemSelf_Res = new ResideMenuItem(ctx, R.drawable.self, "Resolution",true);
        itemCall_to_CC = new ResideMenuItem(ctx, R.drawable.call, "Call Us",true);
        //itemChat = new ResideMenuItem(ctx, R.drawable.chat, "Chat",true);
        itemAlerts     = new ResideMenuItem(ctx, R.drawable.alerts,  "Alerts",true);
        itemHelp = new ResideMenuItem(ctx, R.drawable.help,  "Help",true);
        itemShare = new ResideMenuItem(ctx, R.drawable.share, "Share ",true);
        itemRefer = new ResideMenuItem(ctx, R.drawable.refer_frnd, "Refer a friend ",false);
        
        
        itemProfile1     = new ResideMenuItem(ctx, R.drawable.profile,  "Profile",true);
        itemRenewal1  = new ResideMenuItem(ctx, R.drawable.rene,  "Renewal",true);
        itemSelf_Res1 = new ResideMenuItem(ctx, R.drawable.self, "Resolution",true);
        itemCall_to_CC1 = new ResideMenuItem(ctx, R.drawable.call, "Call Us ",true);
       // itemChat1 = new ResideMenuItem(ctx, R.drawable.chat, "Chat",true);
        itemAlerts1    = new ResideMenuItem(ctx, R.drawable.alerts,  "Alerts",true);
        itemHelp1 = new ResideMenuItem(ctx, R.drawable.help,  "Help",true);
        itemShare1 = new ResideMenuItem(ctx, R.drawable.share, "Share ",true);
        itemRefer1 = new ResideMenuItem(ctx, R.drawable.refer_frnd, "Refer a friend ",false);
        

        itemProfile.setOnClickListener(clk);
        itemRenewal.setOnClickListener(clk);
       // itemChat.setOnClickListener(clk);
        itemShare.setOnClickListener(clk);
        
        itemAlerts.setOnClickListener(clk);
        itemHelp.setOnClickListener(clk);
        itemCall_to_CC.setOnClickListener(clk);
        itemSelf_Res.setOnClickListener(clk);
        itemRefer.setOnClickListener(clk);
        
        itemProfile1.setOnClickListener(clk);
        itemRenewal1.setOnClickListener(clk);
        //itemChat1.setOnClickListener(clk);
        itemShare1.setOnClickListener(clk);
        
        itemAlerts1.setOnClickListener(clk);
        itemHelp1.setOnClickListener(clk);
        itemCall_to_CC1.setOnClickListener(clk);
        itemSelf_Res1.setOnClickListener(clk);
        itemRefer1.setOnClickListener(clk);

        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRenewal, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSelf_Res, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCall_to_CC, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(itemChat, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAlerts, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemHelp, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemRefer, ResideMenu.DIRECTION_LEFT);
        
        resideMenu.addMenuItem(itemProfile1, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemRenewal1, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemSelf_Res1, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemCall_to_CC1, ResideMenu.DIRECTION_RIGHT);
        //resideMenu.addMenuItem(itemChat1, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemAlerts1, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemHelp1, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemShare1, ResideMenu.DIRECTION_RIGHT);
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





	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==itemProfile){
			Utils.log("Profile","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("Profile")){
				MyAppHome.this.finish();
				Intent i = new Intent(MyAppHome.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		}
		if(v==itemRenewal){
			Utils.log("Renewal","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("RenewPackage")){
			if (sharedPreferences1.getString("IsFreePackage", "false")
					.equalsIgnoreCase("false")) {
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//BaseActivity.this.finish();
				Intent i = new Intent(MyAppHome.this, RenewPackage.class);
				i.putExtra("renew", "Home");
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {				
				AlertsBoxFactory.showAlert2("This Facility is not available.", MyAppHome.this);
			}
			
				resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		
		}
		
		if(v==itemSelf_Res){
			Utils.log("Resolution","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("SelfResolution")){
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//this.finish();
			Intent i = new Intent(MyAppHome.this,SelfResolution.class);
			i.putParcelableArrayListExtra("complaintcategorylist",null );
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		}
		
		if(v==itemCall_to_CC){
			Utils.log("CC","Clicked");
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
			
			Utils utils=new Utils();
			utils.setSharedPreferences(sharedPreferences);
			String DefaultCustomerCareNo="01140115150";
			String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
			Intent intent = new Intent(Intent.ACTION_CALL); 
			if(!CustomerCareNo.equalsIgnoreCase("0"))
			intent.setData(Uri.parse("tel:"+CustomerCareNo));
			else
			intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
		    startActivity(intent);
		}
		/*if(v==Utils.itemChat){
			Utils.log("Chat","Clicked");
			//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
			//	this.finish();
			Intent i = new Intent(MyAppHome.this,ChatActivity.class);
			startActivity(i);
		}*/
		
		if(v==Utils.itemAlerts){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("NotificationListActivity")){
				this.finish();
				Intent i = new Intent(MyAppHome.this,NotificationListActivity.class);
				startActivity(i);
				resideMenu.closeMenu();
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
			}
			else{
				resideMenu.closeMenu();
			}
		}
		if(v==itemHelp){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("HelpActivity")){
				this.finish();
				Intent i = new Intent(MyAppHome.this,HelpActivity.class);
				startActivity(i);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		}
		if(v==itemShare){
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		       sharingIntent.setType("plain/text");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link ));
			startActivity(Intent.createChooser(sharingIntent,"ODiGitel Broadband App"));
		       resideMenu.closeMenu();
		}
		
		if(v==Utils.itemProfile1){
			Utils.log("Profile","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("Profile")){
				MyAppHome.this.finish();
				Intent i = new Intent(MyAppHome.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		}
		if(v==itemRenewal1){
			Utils.log("Renewal","Clicked");
			if(!this.getClass().getSimpleName().equalsIgnoreCase("RenewPackage")){
			if (sharedPreferences1.getString("IsFreePackage", "false")
					.equalsIgnoreCase("false")) {
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//BaseActivity.this.finish();
				Intent i = new Intent(MyAppHome.this, RenewPackage.class);
				i.putExtra("renew", "Home");
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {				
				AlertsBoxFactory.showAlert2("This Facility is not available.", MyAppHome.this);
			}
			
				resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		
		}
		
		if(v==itemSelf_Res1){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("SelfResolution")){
				//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
				//this.finish();
			Intent i = new Intent(MyAppHome.this,SelfResolution.class);
			i.putParcelableArrayListExtra("complaintcategorylist",null );
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		}
		
		if(v==itemCall_to_CC1){
			SharedPreferences sharedPreferences = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_name), 0); // 0 - for private mode
			
			Utils utils=new Utils();
			utils.setSharedPreferences(sharedPreferences);
			String DefaultCustomerCareNo="01140115150";
			String	CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
			Intent intent = new Intent(Intent.ACTION_CALL); 
			if(!CustomerCareNo.equalsIgnoreCase("0"))
			intent.setData(Uri.parse("tel:"+CustomerCareNo));
			else
			intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
		    startActivity(intent);
		}
		/*if(v==itemChat1){
			//if(!this.getClass().getSimpleName().equalsIgnoreCase("IONHome"))
			//	this.finish();
			Intent i = new Intent(MyAppHome.this,ChatActivity.class);
			startActivity(i);
		}*/
		
		if(v==itemAlerts1){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("NotificationListActivity")){
				this.finish();
				Intent i = new Intent(MyAppHome.this,NotificationListActivity.class);
				startActivity(i);
				resideMenu.closeMenu();
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
			}
			else{
				resideMenu.closeMenu();
			}
		}
		if(v==itemHelp1){
			if(!this.getClass().getSimpleName().equalsIgnoreCase("HelpActivity")){
				this.finish();
				Intent i = new Intent(MyAppHome.this,HelpActivity.class);
				startActivity(i);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		}
		if(v==itemShare1){
			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("plain/text");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString( R.string.playstore_link ));
			startActivity(Intent.createChooser(sharingIntent,"ODiGitel Broadband App"));
		       resideMenu.closeMenu();
		}
		
		if(v==Utils.itemRefer){
			Intent i = new Intent(MyAppHome.this,Refer_FrndActivity.class);
			startActivity(i);
			//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			Utils.resideMenu.closeMenu();
		     
		}
		
		if(v==itemRefer1){
			
			if(!this.getClass().getSimpleName().equalsIgnoreCase("Refer_FrndActivity")){
				this.finish();
			Intent i = new Intent(MyAppHome.this,Refer_FrndActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			resideMenu.closeMenu();
			}
			else{
				resideMenu.closeMenu();
			}
		     
		}
		
		resideMenu.closeMenu();
	}
}
