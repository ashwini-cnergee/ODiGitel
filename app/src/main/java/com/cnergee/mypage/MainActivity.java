package com.cnergee.mypage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;

import android.text.Html;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cnergee.krishna.ookla.odigitel.R;
import com.cnergee.mypage.SOAP.UpdatePhoneDetailSOAP;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.database.DatabaseHandler;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;




public class MainActivity extends Activity {

	public static Context context;
	public String app_ver="";
	private String logtag = getClass().getSimpleName();
	private String sharedPreferences_name;
	ProgressBar prgBar;

	private String memberlogiid="";
	private String memberid="";
	private String mobilenumber="",otp_password="";
	Utils utils = new Utils();
	ImageView imageView1;
	public boolean isNetConnect = true;
	public static String rslt;
	public static boolean isVaildUser = false;

	String AppVersion,MemberId;

	private static final String PERMISSIONS_REQUIRED[] = new String[]{
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.ACCESS_WIFI_STATE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_NETWORK_STATE,
			Manifest.permission.CALL_PHONE
	};

	private static final int REQUEST_PERMISSIONS = 110 ;

	String reg_id="";
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.runFinalizersOnExit(true);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		checkPermissions();

		context = this;
		Utils.log("Shared Prefrences ","Executed");
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		prgBar=(ProgressBar) findViewById(R.id.progressBar1);
		isNetConnect = utils.isOnline(context);
		prgBar.setVisibility(View.GONE);

		memberlogiid = utils.getMemberLoginID();
		memberid = utils.getMemberId();
		mobilenumber = utils.getMobileNoPrimary();
		otp_password= sharedPreferences.getString("otp_password", "0");
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		imageView1.setAdjustViewBounds(false);
		imageView1.setVisibility(View.VISIBLE);

		Utils.open_once=true;


		String sharedPreferences_name = context.getString(R.string.shared_preferences_name);
		/*SharedPreferences	sharedPreferences_ = context
   				.getSharedPreferences(context.getString(R.string.shared_preferences_name), 0);

		reg_id=sharedPreferences_.getString("Gcm_reg_id", "");

		 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
       		 new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
          else
           	new InsertPhoneDetailsWebService().execute();*/

		if(Utils.checkPlayServices(context)){

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new GetGCMIDAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
			else
				new GetGCMIDAsyncTask().execute((String)null);
		}else{
			Toast.makeText(getApplicationContext(),
					"Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
		}

		if(sharedPreferences.getBoolean(Utils.APP_FIRST_TIME, true)){
			Editor editor=sharedPreferences.edit();
			editor.putBoolean(Utils.APP_FIRST_TIME, false);
			editor.putBoolean(Utils.APP_SECOND_TIME, true);
			editor.commit();
		}
		else{
			Editor editor=sharedPreferences.edit();
			editor.putBoolean(Utils.APP_SECOND_TIME, true);
			editor.commit();
		}

		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			AppVersion= pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		utils.setSharedPreferences(sharedPreferences);
		MemberId = utils.getMemberId();


		/*Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 2500) {
						sleep(200);
						waited += 300;
					}
				} catch (Exception e) {
					// do nothing
				} finally {
					finish();
					if (utils.getMemberLoginID().equals("")	|| utils.getMemberId().equals("")||
							 utils.getMobileNoPrimary().equals("")) {

						//Intent intent = new Intent(MainActivity.this, Login.class);
						Intent intent = new Intent(MainActivity.this, LoginFragmentActivity.class);
						intent.putExtra("from", "1");
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						MainActivity.this.finish();
					}
					else
					{
						MainActivity.this.finish();
						//System.out.println(utils.getMemberId());
						Intent intent = new Intent(MainActivity.this, IONHome.class);
						//startActivityForResult(intent, 0);
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}
				}
			}
		};
		splashThread.start();*/

		Utils.log("Member Login 84 ","Executed");
		if (utils.getMemberLoginID().equals("")	|| utils.getMemberId().equals("")||
				utils.getMobileNoPrimary().equals("")) {
		}
		else
		{
			DatabaseHandler	 databaseHandler = new DatabaseHandler(MainActivity.this);
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy");
			String strDate = sdf.format(c.getTime());

			Utils.log("Current Date", ""+strDate);
			String stored_date="",flag="";

			try{
				Cursor cur = databaseHandler.readingEzetap();

				if (cur != null) {
					if (cur.moveToFirst()) {
						do {
							Utils.log("First "," record");
							stored_date = cur.getString(cur.getColumnIndex(DatabaseHandler.UPDATE_DATE));
							flag=cur.getString(cur.getColumnIndex(DatabaseHandler.FLAG));
							Utils.log("Stored Date","is:"+stored_date);
							Utils.log("Flag","is:"+flag );
						} while (cur.moveToNext());
					}
				}

				Utils.log("StroredDate", "Stored Date:"+stored_date);

			}catch (Exception e) {
				Utils.log("Error", "Error:"+e);
			}


			if (stored_date != null) {

				if (stored_date.length() > 0) {

					if (strDate.equalsIgnoreCase(stored_date)) {

						Utils.log("Data matched", "dataMatched Executed");
						if(flag.equalsIgnoreCase("1")){

						}
						else{
							Utils.log("Eztap","executed");
							Utils.log("Update Date", "UpdatingDate:"+databaseHandler+strDate+stored_date);

							if(Utils.checkPlayServices(context)){

								if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
									new GetGCMIDAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
								else
									new GetGCMIDAsyncTask().execute((String)null);

							}
						/*else{
							if(Utils.isOnline(MainActivity.this)){
				           		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				           			new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
				           		else
				           			new InsertPhoneDetailsWebService().execute();

				           	}
						}*/
							databaseHandler.updateEzetap(strDate);
							Utils.log("UpDate Data",""+databaseHandler);
							Utils.log("Updatedate 1 st time", "1 st Executed");
						}
					} else {

						Utils.log("Eztap","executed");
						Utils.log("Update Date", "UpdatingDate:"+databaseHandler+strDate+stored_date);
						if(Utils.checkPlayServices(context)){

							if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
								new GetGCMIDAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
							else
								new GetGCMIDAsyncTask().execute((String)null);

						}
					/*else{
						if(Utils.isOnline(MainActivity.this)){
			           		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			           			new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
			           		else
			           			new InsertPhoneDetailsWebService().execute();

			           	}
					}*/
						databaseHandler.updateEzetap(strDate);
						Utils.log("UpDate Data",""+databaseHandler);
						Utils.log("Updatedate 1 st time", "1 st Executed");
					}
				} else {

					Utils.log("Eztap","executed");
					Utils.log("Update Date", "UpdatingDate:"+databaseHandler+strDate+stored_date);
					if(Utils.checkPlayServices(context)){

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
							new GetGCMIDAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
						else
							new GetGCMIDAsyncTask().execute((String)null);

					}
				/*else{
					if(Utils.isOnline(MainActivity.this)){
		           		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		           			new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
		           		else
		           			new InsertPhoneDetailsWebService().execute();

		           	}
				}*/
					databaseHandler.updateEzetap(strDate);
					Utils.log("Updated 2nd ", "Updated 2 time");

				}

			} else {
				Utils.log("Eztap","executed");
				Utils.log("Inseret Date", "InsertingDate:"+databaseHandler+strDate+stored_date);
				if(Utils.checkPlayServices(context)){

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						new GetGCMIDAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
					else
						new GetGCMIDAsyncTask().execute((String)null);

				}
			/*else{
				if(Utils.isOnline(MainActivity.this)){
	           		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	           			new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
	           		else
	           			new InsertPhoneDetailsWebService().execute();

	           	}
			}*/
				databaseHandler.addEzetap(strDate);
				Utils.log("Inseret Date", "InsertingDate:"+databaseHandler+strDate+stored_date);

			}
		}

		/*if(utils.isOnline(MainActivity.this)){
			new ValidUserWebService().execute();
		}
		else{
			prgBar.setVisibility(View.INVISIBLE);
			splashThread.start();
		}*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		/*Animation animation1=new TranslateAnimation(200.0f, 0.0f, 0.0f, 0.0f);
		animation1.setDuration(1000);
		imageView1.startAnimation(animation1);
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 2100) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();

					if (utils.getMemberLoginID().equals("")	|| utils.getMemberId().equals("")||
							 utils.getMobileNoPrimary().equals("")) {
						Intent intent = new Intent(MainActivity.this, Login.class);
						intent.putExtra("from", "1");
						startActivity(intent);
					}
					else
					{
						System.out.println(utils.getMemberId());
						Intent intent = new Intent(MainActivity.this, IONHome.class);
						//startActivityForResult(intent, 0);
						startActivity(intent);

					}


				}
			}
		};
		splashThread.start();*/
	}

	private void checkPermissions() {
		boolean permissionsGranted = checkPermission(PERMISSIONS_REQUIRED);
		if (permissionsGranted) {
			Thread splashThread = new Thread() {
				@Override
				public void run() {
					try {
						int waited = 0;
						while (waited < 2000) {
							sleep(100);
							waited += 100;
						}
					} catch (InterruptedException e) {
						// do nothing
					} finally {
						finish();
						if (utils.getMemberLoginID().equals("")	|| utils.getMemberId().equals("")||
								utils.getMobileNoPrimary().equals("")) {

							//Intent intent = new Intent(MainActivity.this, Login.class);
							Intent intent = new Intent(MainActivity.this, LoginFragmentActivity.class);
							intent.putExtra("from", "1");
							startActivity(intent);
							overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
							MainActivity.this.finish();
						}
						else
						{
							MainActivity.this.finish();
							//System.out.println(utils.getMemberId());
							Intent intent = new Intent(MainActivity.this, IONHome.class);
							//startActivityForResult(intent, 0);
							startActivity(intent);
							overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
						}
					}
				}
			};
			splashThread.start();

		} else {
			boolean showRationale = true;
			for (String permission: PERMISSIONS_REQUIRED) {
				showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
				if (!showRationale) {
					break;
				}
			}

                String dialogMsg = showRationale ? "We need some permissions to run this APP!" : "You've declined the required permissions, please grant them from your phone settings";

			/*new AlertDialog.Builder(this)
					.setTitle("Permissions Required")
					.setMessage(dialogMsg)
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//dialog.dismiss();
							ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
						}
					}).create().show();*/

			showAlert(dialogMsg, MainActivity.this);
		}
	}

	public void showAlert(String message, Context ctx){
		final Dialog dialog = new Dialog(ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.custom_dialog_box);
			/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);*/
		int width = 0;
		int height =0;
		dialog.setCancelable(false);

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

		TextView dtv = (TextView) dialog.findViewById(R.id.tv1);

		TextView txt = (TextView) dialog.findViewById(R.id.tv);

		txt.setText(Html.fromHtml(message));

		Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);


		dialogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
			}
		});
		if(isRunning((Activity)ctx))
			dialog.show();
		//(width/2)+((width/2)/2)
		//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setLayout((width/2)+(width/2)/2, LinearLayout.LayoutParams.WRAP_CONTENT);
	}
	public static boolean isRunning(Context ctx) {
		ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

		for (ActivityManager.RunningTaskInfo task : tasks) {
			if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
				return true;
		}

		return false;
	}


	private boolean checkPermission(String permissions[]) {
		for (String permission : permissions) {
			if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == REQUEST_PERMISSIONS) {

			boolean hasGrantedPermissions = true;
			for (int i=0; i<grantResults.length; i++) {
				if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
					hasGrantedPermissions = false;
					break;
				}
			}

			if (!hasGrantedPermissions) {
				finish();
			}else {
				Thread splashThread = new Thread() {
					@Override
					public void run() {
						try {
							int waited = 0;
							while (waited < 2000) {
								sleep(100);
								waited += 100;
							}
						} catch (InterruptedException e) {
							// do nothing
						} finally {
							finish();
							if (utils.getMemberLoginID().equals("")	|| utils.getMemberId().equals("")||
									utils.getMobileNoPrimary().equals("")) {

								//Intent intent = new Intent(MainActivity.this, Login.class);
								Intent intent = new Intent(MainActivity.this, LoginFragmentActivity.class);
								intent.putExtra("from", "1");
								startActivity(intent);
								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
								MainActivity.this.finish();
							}
							else
							{
								MainActivity.this.finish();
								//System.out.println(utils.getMemberId());
								Intent intent = new Intent(MainActivity.this, IONHome.class);
								//startActivityForResult(intent, 0);
								startActivity(intent);
								overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
							}
						}
					}
				};
				splashThread.start();

			}
		} else {
			finish();
		}
	}

	protected class ValidUserWebService extends AsyncTask<String, Void, Void> {

		// final AlertDialog alert =new
		// AlertDialog.Builder(Login.this).create();

		//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);

		protected void onPreExecute() {
			//Dialog.setMessage(getString(R.string.app_please_wait_label));
			//Dialog.setCancelable(false);
			//Dialog.show();
			prgBar.setProgress(0);

		}

		@SuppressLint("CommitPrefEdits")
		protected void onPostExecute(Void unused) {

			prgBar.setVisibility(View.INVISIBLE);
			if (rslt.trim().equalsIgnoreCase("ok")) {

				if (isVaildUser) {
					//System.out.println(utils.getMemberId());
					MainActivity.this.finish();
					Intent intent = new Intent(MainActivity.this, IONHome.class);
					//startActivityForResult(intent, 0);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

				}
				else
				{
					try{

						Utils.log("Shared Prefrences 200 ","Executed");
						SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
						Editor edit1=sharedPreferences1.edit();
						edit1.clear();
						edit1.commit();

						SharedPreferences sharedPreferences2 = getApplicationContext()
								.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0); // 0 - for private mode
						Editor edit2=sharedPreferences2.edit();
						edit2.clear();
						edit2.commit();

						SharedPreferences sharedPreferences3 = getApplicationContext()
								.getSharedPreferences(getString(R.string.shared_preferences_notification_list), 0); // 0 - for private mode
						Editor edit3=sharedPreferences3.edit();
						edit3.clear();
						edit3.commit();

						SharedPreferences sharedPreferences4 = getApplicationContext()
								.getSharedPreferences(getString(R.string.shared_preferences_payment_history), 0); // 0 - for private mode
						Editor edit4=sharedPreferences4.edit();
						edit4.clear();
						edit4.commit();

						SharedPreferences sharedPreferences5 = getApplicationContext()
								.getSharedPreferences(getString(R.string.shared_preferences_profile), 0); // 0 - for private mode
						Editor edit5=sharedPreferences5.edit();
						edit5.clear();
						edit5.commit();
						MainActivity.this.finish();
						//Intent intent = new Intent(MainActivity.this, Login.class);
						Intent intent = new Intent(MainActivity.this, LoginFragmentActivity.class);
						intent.putExtra("from", "1");
						startActivity(intent);
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					}
					catch(Exception e){
						//Utils.log("Exception","is "+e);
					}

				}
			} else {
				//System.out.println(utils.getMemberId());
				MainActivity.this.finish();
				//Intent intent = new Intent(MainActivity.this, Login.class);
				Intent intent = new Intent(MainActivity.this, LoginFragmentActivity.class);
				intent.putExtra("from", "1");
				//startActivityForResult(intent, 0);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				return;
			}
		}



		@Override
		protected Void doInBackground(String... params) {
			try {

				Utils.log("Sms Authentication "," SMS Executed");
				//	Log.i("START",">>>>>>>START<<<<<<<<");
				SMSAuthenticationCaller smsCaller = new SMSAuthenticationCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_GET_SMS_AUTHENTICATION)
				);
				smsCaller.PhoneUniqueId=Secure.getString(getContentResolver(), Secure.ANDROID_ID);
				smsCaller.MemberId = memberid;
				smsCaller.OneTimePwd=otp_password;
				//smsCaller.setAllData(true);
				smsCaller.setCallData("splash");
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

	}


	public class GetGCMIDAsyncTask extends AsyncTask<String, Void, String>{

		GoogleCloudMessaging gcm;
		@Override
		protected String doInBackground(String... params) {
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(context);
				}
				reg_id = gcm.register(AuthenticationMobile.PROJECT_NUMBER);
				msg = "Device registered, registration ID=" + reg_id;
				Utils.log("GCM",  msg);

				Utils.log("RegID",":"+reg_id);

			} catch (Exception ex) {
				msg = "error";
				Utils.log("GCM error",  ""+ex);
			}
			return msg;
		}


		protected void onPostExecute(String msg) {

			if(msg.equalsIgnoreCase("error")){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				reg_id="No";
			}
			else{
				SharedPreferences	sharedPreferences_ = context
						.getSharedPreferences(context.getString(R.string.shared_preferences_name), 0); // 0 - for private mode
				Editor editor=sharedPreferences_.edit();
				editor.putString("Gcm_reg_id", reg_id);
				editor.commit();

			}
			if(Utils.isOnline(MainActivity.this)){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					new InsertPhoneDetailsWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
				else
					new InsertPhoneDetailsWebService().execute();
			}
		}
	}

	private class InsertPhoneDetailsWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {

		//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);
		UpdatePhoneDetailSOAP  updatePhoneDetailSOAP;

		protected void onPreExecute() {
			//Dialog.setMessage(getString(R.string.app_please_wait_label));

		}


		protected void onPostExecute(Void unused) {


		}

		@Override
		protected Void doInBackground(String... params) {
			try {

				updatePhoneDetailSOAP= new UpdatePhoneDetailSOAP(context.getResources().getString(
						R.string.WSDL_TARGET_NAMESPACE), context.getResources().getString(R.string.SOAP_URL), context
						.getResources().getString(R.string.METHOD_UPDATE_PHONE_DETAILS_));

				updatePhoneDetailSOAP.updateDetails(MemberId, Secure.getString(context.getContentResolver(), Secure.ANDROID_ID), reg_id,AppVersion);

			} catch (Exception e) {
				//AlertsBoxFactory.showAlert(rslt,SMSAuthenticationActivity.this );
			}
			return null;
		}

		/* (non-Javadoc)
         * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
         */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}
	}
}
