package com.cnergee.mypage;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import java.util.Calendar;


import com.cnergee.mypage.SOAP.AreaWiseSettingSOAP;
import com.cnergee.mypage.caller.PaymentPickUpCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.PaymentPickUpObj;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import all.interface_.IError;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Html;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import cnergee.krishna.ookla.odigitel.R;
import com.special.ResideMenu.ResideMenu;

public class PaymentPickup_New extends BaseActivity  {
	
	
	public static Context context;
	//ProgressDialog mainDialog;
	ProgressHUD mProgressHUD;
	boolean check_intent = false;
	public static String responseMsg = "";
	private String sharedPreferences_name; 
	Utils utils = new Utils();
	LinearLayout linhome ,linprofile,linnotification,linhelp;
	Button btnSubmit,btnCancel;
	EditText Message;
	WheelView month,year,day;
	WheelView hours,mins,ampm;
	String pickUpDate="";
	public static boolean  isVaildUser;
	String otp_password = "";
	PaymentPickupWebService PaymentPickupWebService = null;
	public long memberid; 
	public static String rslt =""; 
	public static String settingResult = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_pickup_myapp);
		
		iError=(IError) this;
		
		
		Calendar calendar = Calendar.getInstance();
		context = this;
		month = (WheelView) findViewById(R.id.month);
		year = (WheelView) findViewById(R.id.year);
		day = (WheelView) findViewById(R.id.day);
		 
		hours = (WheelView) findViewById(R.id.hour);
		mins = (WheelView) findViewById(R.id.mins);
		ampm = (WheelView) findViewById(R.id.ampm);
		 
		Message = (EditText)findViewById(R.id.message);
		 
		linhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		linhelp = (LinearLayout)findViewById(R.id.inn_banner_home);
		btnSubmit= (Button)findViewById(R.id.btnSubmit);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0);
		utils.setSharedPreferences(sharedPreferences);
		otp_password = sharedPreferences.getString("otp_password", "0");
		Utils.log("MemberId",":"+utils.getMemberId());
		memberid = Long.parseLong(utils.getMemberId());
		
		

		linhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentPickup_New.this.finish();
				//Intent i = new Intent(PaymentPickup_New.this, IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});

		linprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentPickup_New.this.finish();
				Intent i = new Intent(PaymentPickup_New.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentPickup_New.this.finish();
				Intent i = new Intent(PaymentPickup_New.this,
						NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linhelp = (LinearLayout) findViewById(R.id.inn_banner_help);

		linhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentPickup_New.this.finish();
				Intent i = new Intent(PaymentPickup_New.this, HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		
		
		btnCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
				PaymentPickup_New.this.finish();
				if (check_intent) {
					//Intent i = new Intent(PaymentPickup_New.this, RenewPackage.class);
					//startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				} else {
					
					//Intent i = new Intent(PaymentPickup_New.this, IONHome.class);
					//startActivity(i);
					overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				}
			}
			
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(Message.length()==0){

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					//tell the Dialog to use the dialog.xml as it's layout description
					dialog.setContentView(R.layout.dialog_box);
					int width = 0;
					int height =0;
					
					
					    Point size = new Point();
					    WindowManager w =getWindowManager();

					    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					        w.getDefaultDisplay().getSize(size);
					        width = size.x;
					        height = size.y;
					    } else {
					        Display d = w.getDefaultDisplay();
					        width = d.getWidth();
					        height   = d.getHeight();;
					    }

					TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

					TextView txt = (TextView) dialog.findViewById(R.id.tv);

					txt.setText(Html.fromHtml("Please enter your Message"));
					dtv.setText(Html.fromHtml("Alert"));
					Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();

						}
					});
					
					dialog.show();
					//(width/2)+((width/2)/2)
					//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
				
					
				}else{
				
				
				
					updateDays(year, month, day);
					if(Utils.isOnline(PaymentPickup_New.this)){
						PaymentPickupWebService = new PaymentPickupWebService();
						PaymentPickupWebService.execute((String) null);
						Utils.log("In Submit 140 ","Webservice Executed");
						}
						else{
							Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
							}
		
						return;
						}
		}
	});
		if(Utils.isOnline(PaymentPickup_New.this)){
			new ValidUserWebService().execute();
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PaymentPickup_New.this);
			builder.setMessage(
					"Please connect to internet and try again!!")
					.setTitle("Alert")
					.setCancelable(false)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// Toast.makeText(NotificationListActivity.this,
									// ""+selectedFromList.getNotifyId(),
									// Toast.LENGTH_SHORT).show();
									
									PaymentPickup_New.this.finish();
									Intent intent = new Intent(
											PaymentPickup_New.this,
											IONHome.class);
									
									startActivity(intent);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
			
			
		
				OnWheelChangedListener listener = new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day);
			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] {"January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November", "December"};
		month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);
		Utils.log("In Submit 168 ","Webservice Executed");
		
		// year
		int curYear = calendar.get(Calendar.YEAR);
		year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear, 0));
		year.setCurrentItem(curYear);
		year.addChangingListener(listener);

		//day
		updateDays(year, month, day);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
				
		// hours
		hours.setViewAdapter(new DateNumericAdapter(this, 0, 12, calendar.get(Calendar.HOUR_OF_DAY)));
		hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
	
		// mins
		mins.setViewAdapter(new DateNumericAdapter(this, 0, 59, calendar.get(Calendar.MINUTE)));
		mins.setCurrentItem(calendar.get(Calendar.MINUTE));	
		mins.setCyclic(true);
		
		// AM/PM
		int cur_am_pm = calendar.get(Calendar.AM_PM);
		ampm.setViewAdapter(new DateArrayAdapter(this, new String[] {"AM", "PM"}, cur_am_pm));
		ampm.setCurrentItem(cur_am_pm);
		
		Utils.log("In Submit 194  ","Webservice Executed");
		
		day.setOnTouchListener(onTouchListener);
		month.setOnTouchListener(onTouchListener);
		year.setOnTouchListener(onTouchListener);
		ampm.setOnTouchListener(onTouchListener);
		hours.setOnTouchListener(onTouchListener);
		mins.setOnTouchListener(onTouchListener);
		
	}

OnTouchListener onTouchListener= new OnTouchListener() {
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		  int action = event.getAction();
          switch (action) {
          case MotionEvent.ACTION_DOWN:
              // Disallow ScrollView to intercept touch events.
              v.getParent().requestDisallowInterceptTouchEvent(true);
              break;

          case MotionEvent.ACTION_UP:
              // Allow ScrollView to intercept touch events.
              v.getParent().requestDisallowInterceptTouchEvent(false);
              break;
          }

          // Handle ListView touch events.
          v.onTouchEvent(event);
          return true;
	}
};	
	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	void updateDays(WheelView year, WheelView month, WheelView day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());
		
		if(month.getCurrentItem()==0){
			Calendar calendar1 = Calendar.getInstance();
			
			int curYear = calendar1.get(Calendar.YEAR);
			Utils.log("January",":"+curYear);
			year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear+1, 0));
		}
		else{
			Calendar calendar1 = Calendar.getInstance();
			
			int curYear = calendar1.get(Calendar.YEAR);
			Utils.log("Others",":"+curYear);
			year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear, 0));
		}
		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		String setDay="",setMonth="",setYear,setMin="",setHour="";
		if(curDay <=9){
			
			Utils.log("Day",":"+String.valueOf("0"+curDay));
			setDay=String.valueOf("0"+curDay);
		}
		else{
			
			Utils.log("Day",":"+String.valueOf(curDay));
			setDay=String.valueOf(curDay);
			
		}
		if((calendar.get(Calendar.MONTH)+1) <=9){
			
			Utils.log("Month",":"+"0"+String.valueOf(String.valueOf(calendar.get(Calendar.MONTH)+1)));
			setMonth="0"+String.valueOf(String.valueOf(calendar.get(Calendar.MONTH)+1));
		} 
		else{
			int j=calendar.get(Calendar.MONTH)+1;
			 Utils.log("Month",":"+String.valueOf(j));
			 setMonth=String.valueOf(j);
		}
		
				
		//Utils.log("Month",":"+String.valueOf(calendar.get(Calendar.MONTH)+1));
		Utils.log("Year",":"+calendar.get(Calendar.YEAR));
		
		setYear=String.valueOf(calendar.get(Calendar.YEAR));
		setHour=String.valueOf(hours.getCurrentItem());
		setMin=String.valueOf(mins.getCurrentItem());
		if(Integer.valueOf(setHour)<=9){
			setHour="0"+setHour;
		}
		if(Integer.valueOf(setMin)<=9){
			setMin="0"+setMin;
		}
		
		Utils.log("Hour",":"+setHour);
		Utils.log("Min",":"+setMin);
		pickUpDate=setDay+setMonth+setYear+setHour+setMin+"00";
		Utils.log("PickUp","Date:"+pickUpDate);
	}
	

	private class PaymentPickupWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{

		PaymentPickUpObj  paymentpickUpObj = new PaymentPickUpObj();
		ProgressHUD	mProgressHUD ;
		@Override
		protected void onPostExecute(Void unused) {
		
			mProgressHUD.dismiss();
			
			Utils.log("273 AskTask", "Executed");
			PaymentPickupWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {

				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				//tell the Dialog to use the dialog.xml as it's layout description
				dialog.setContentView(R.layout.dialog_box);
				int width = 0;
				int height =0;
				
				
				    Point size = new Point();
				    WindowManager w =getWindowManager();

				    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				        w.getDefaultDisplay().getSize(size);
				        width = size.x;
				        height = size.y;
				    } else {
				        Display d = w.getDefaultDisplay();
				        width = d.getWidth();
				        height   = d.getHeight();;
				    }
				
				
				
				    TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

				TextView txt = (TextView) dialog.findViewById(R.id.tv);

				txt.setText(Html.fromHtml(responseMsg));
				dtv.setText(Html.fromHtml("Confirmation"));
				Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

				
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
						Intent i = new Intent(PaymentPickup_New.this, IONHome.class);
						startActivity(i);
					}
				});
				
				dialog.show();
				//(width/2)+((width/2)/2)
				//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		

			} else {
				Utils.log("315","ONPost Executed");
				
				AlertsBoxFactory.showAlert(rslt, context);
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
	
			try{
				/*
				Calendar c = Calendar.getInstance();
				
				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"ddMMyyyyHHmmss");
				*/
				Utils.log("In Submit332 ","AynckTask Executed");
			
					//pickUpDate = dateFormatter.format(c.getTime());
				paymentpickUpObj.setMemberId(memberid);
				paymentpickUpObj.setMessage(Message.getText().toString());
				paymentpickUpObj.setVisitDateTime(pickUpDate);
				
				Utils.log("In Submit 340","AsynckTask Executed");
				
				PaymentPickUpCaller caller = new PaymentPickUpCaller(getApplicationContext().getResources().getString(R.string.WSDL_TARGET_NAMESPACE),getApplicationContext().getResources().getString(R.string.SOAP_URL),getApplicationContext().getResources().getString(R.string.METHOD_INSERT_PAYMENTPICKUP));
				Utils.log("In Submit 343 ","Aynk Task Executed");
				
				caller.setPaymentpickupobj(paymentpickUpObj);
				//caller.setMemberId(Long.parseLong(utils.getMemberId()));
				
	
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
			
			}catch (Exception e) {
					e.printStackTrace();			
			}
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(PaymentPickup_New.this,getString(R.string.app_please_wait_label), true,true,this);
		}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
	}
	
	/*setting AynckTask starts here */
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		this.finish();
		if (check_intent) {
			//Intent i = new Intent(PaymentPickup_New.this, RenewPackage.class);
			//startActivity(i);
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		} else {
			
			//Intent i = new Intent(PaymentPickup_New.this, IONHome.class);
			//startActivity(i);
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*ImageView ivLogo = (ImageView) findViewById(R.id.imgdvois);

		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
																	// private
																	// mode
		if (sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}

	// *******************************Check Valid User Web
	// Service*********starts here**************************

	protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {

		// final AlertDialog alert =new
		// AlertDialog.Builder(Login.this).create();

		// private ProgressDialog Dialog = new
		// ProgressDialog(SMSAuthenticationActivity.this);

		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PaymentPickup_New.this,getString(R.string.app_please_wait_label), true,true,this);

		}

		@SuppressLint("CommitPrefEdits")
		protected void onPostExecute(Void unused) {

			if (rslt.trim().equalsIgnoreCase("ok")) {

				if (isVaildUser) {
					// mainDiaUtils.dismiss();
					new SettingResultAsyncTask().execute();
				} else {
					mProgressHUD.dismiss();
					
					BaseApplication.getEventBus().post(
							new FinishEvent(IONHome.class.getSimpleName()));
					SharedPreferences sharedPreferences1 = getApplicationContext()
							.getSharedPreferences(sharedPreferences_name, 0); // 0
																				// -
																				// for
																				// private
																				// mode
					SharedPreferences.Editor edit1 = sharedPreferences1.edit();
					edit1.clear();

					SharedPreferences sharedPreferences2 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_renewal),
									0); // 0 - for private mode
					SharedPreferences.Editor edit2 = sharedPreferences2.edit();
					edit2.clear();

					SharedPreferences sharedPreferences3 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_notification_list),
									0); // 0 - for private mode
					SharedPreferences.Editor edit3 = sharedPreferences3.edit();
					edit3.clear();

					SharedPreferences sharedPreferences4 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_payment_history),
									0); // 0 - for private mode
					SharedPreferences.Editor edit4 = sharedPreferences4.edit();
					edit4.clear();

					SharedPreferences sharedPreferences5 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_profile),
									0); // 0 - for private mode
					SharedPreferences.Editor edit5 = sharedPreferences5.edit();
					edit5.clear();
					
					sharedPreferences1.edit().clear().commit();
					sharedPreferences2.edit().clear().commit();
					sharedPreferences3.edit().clear().commit();
					sharedPreferences4.edit().clear().commit();
					sharedPreferences5.edit().clear().commit();
					// Utils.log("Data","cleared");

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					//tell the Dialog to use the dialog.xml as it's layout description
					dialog.setContentView(R.layout.dialog_box);
					int width = 0;
					int height =0;
					
					
					    Point size = new Point();
					    WindowManager w =getWindowManager();

					    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					        w.getDefaultDisplay().getSize(size);
					        width = size.x;
					        height = size.y;
					    } else {
					        Display d = w.getDefaultDisplay();
					        width = d.getWidth();
					        height   = d.getHeight();;
					    }
					
					
					
					    TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

					TextView txt = (TextView) dialog.findViewById(R.id.tv);

					txt.setText(Html.fromHtml("You are allowed to use app only on one device"));
					dtv.setText(Html.fromHtml("Alert"));
					Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

					
					dialogButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							PaymentPickup_New.this.finish();
							Intent intent = new Intent(
									PaymentPickup_New.this,
									LoginFragmentActivity.class);
							intent.putExtra("from", "2");
							startActivity(intent);
							dialog.dismiss();
						}
					});
					
					dialog.show();
					//(width/2)+((width/2)/2)
					//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
					dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		

				}
			} else {
				mProgressHUD.dismiss();
				// mainDiaUtils.dismiss();
				//new SettingResultAsyncTask().execute();
				if (rslt.trim().equalsIgnoreCase("error")) {
					
					iError.display();
				}
			}
			
		}

		@Override
		protected Void doInBackground(String... params) {
			try {

				// Log.i("START",">>>>>>>START<<<<<<<<");
				SMSAuthenticationCaller smsCaller = new SMSAuthenticationCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_SMS_AUTHENTICATION));
				smsCaller.PhoneUniqueId = Secure.getString(
						getContentResolver(), Secure.ANDROID_ID);
				smsCaller.MemberId = String.valueOf(memberid);
				smsCaller.OneTimePwd = otp_password;
				// smsCaller.setAllData(true);
				smsCaller.setCallData("pickup");
				smsCaller.join();
				smsCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onCancelled() {

		}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}

	}
	
	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
		
		
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}

	}
	

	public class SettingResultAsyncTask extends AsyncTask<String, Void, Void> {
		String Arearslt = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			settingResult = "";
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Utils.log("doInBackground","executed");
			AreaWiseSettingSOAP areaWiseSettingSOAP = new AreaWiseSettingSOAP(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_GET_AREA_SETTING));
			SharedPreferences sharedPreferences1 = getApplicationContext()
					.getSharedPreferences(
							getString(R.string.shared_preferences_renewal), 0);
			String areaCode = sharedPreferences1.getString("AreaCode", "0");

			try {
				Arearslt = areaWiseSettingSOAP.getAreaWiseSetting(areaCode,
						"PPR");
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Utils.log("onPostExecute","executed");
			mProgressHUD.dismiss();
			if (Arearslt.length() > 0) {
				if (Arearslt.equalsIgnoreCase("ok")) {
					if (settingResult.equalsIgnoreCase("0")) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								PaymentPickup_New.this);
						builder.setMessage("This Facility is not available !! ")
								.setTitle("Alert")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// Toast.makeText(NotificationListActivity.this,
												// ""+selectedFromList.getNotifyId(),
												// Toast.LENGTH_SHORT).show();

												PaymentPickup_New.this
														.finish();
												Intent intent = new Intent(
														PaymentPickup_New.this,
														IONHome.class);

												startActivity(intent);
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					} else {

					}
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							PaymentPickup_New.this);
					builder.setMessage("Please try again!! ")
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();

											PaymentPickup_New.this.finish();
											Intent intent = new Intent(
													PaymentPickup_New.this,
													IONHome.class);

											startActivity(intent);
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PaymentPickup_New.this);
				builder.setMessage("Please try again!! ")
						.setTitle("Alert")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Toast.makeText(NotificationListActivity.this,
										// ""+selectedFromList.getNotifyId(),
										// Toast.LENGTH_SHORT).show();

										PaymentPickup_New.this.finish();
										/*Intent intent = new Intent(
												PaymentPickup_New.this,
												IONHome.class);*/
										overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

										//startActivity(intent);
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}

	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mProgressHUD != null) {
			if (mProgressHUD.isShowing()) {
				mProgressHUD.dismiss();
			}
		}
	}	
}
	
	

