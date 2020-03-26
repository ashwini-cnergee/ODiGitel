package com.cnergee.mypage;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Map;

import all.interface_.IError;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.ResetPasswordCaller;
import com.cnergee.mypage.caller.ComplaintCaller;
import com.cnergee.mypage.caller.ComplaintCategoryListCaller;
import com.cnergee.mypage.caller.ComplaintsStatusCaller;
import com.cnergee.mypage.caller.InsertComplaintCaller;
import com.cnergee.mypage.caller.LogOutCaller;
import com.cnergee.mypage.caller.ReleaseMacCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.ComplaintCategoryList;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import androidx.core.app.ActivityCompat;
import cnergee.krishna.ookla.odigitel.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.otto.Subscribe;

public class Complaints extends BaseActivity implements OnCancelListener{
	
	public static Context context;
	private boolean flag=true;
	Utils utils = new Utils();
	public static String rslt = "";
	public static String responseMsg ="";
	public static String strXML = "";
	public String selDateTime = null;
	String SetDateTime="",otp_password="";
	EditText comments;
	TextView txtcomplaintno;
	//RelativeLayout btncallcustomercare,btnReleaseMac,btnResetPwd,btnLogout;
	RelativeLayout btncallcustomercare,btnResetPwd,btnChat,btnSelfResl;
	Button btnsubmit,btncancel;
	ImageView btnhome,btnprofile,btnnotification,btnhelp;
	private String sharedPreferences_name;
	public String memberid;
	public static String complaintno = "";
	public static String statusRslt="";
	public static String statusResponse="";
	public static String statusResponseForPwd="We are unable to process your request. \n Please try again later";
	public static String statusResponseForMac="We are unable to process your request. \n Please try again later";
	public static String statusResponseForLogOut="We are unable to process your request. \n Please try again later";
	public static String rsltLogOut = "";
	boolean isLogout = false;
	ArrayList<String> ComplaintName;
	ArrayList<String> ComplaintId;
	Spinner spinnerList;
	public static Map<String, ComplaintCategoryList> mapComplaintCategoryList;
	public static ArrayList<ComplaintCategoryList>complaintcategorylist;
	
	public static Map<String, ComplaintObj> mapComplaintNo;
	/*private GetComplaintNoWebService getComplaintNoWebService = null;*/
	private InsertComplaintWebService InsertComplaintWebService = null;
	
	GetComplaintNoWebService GetComplaintNoWebService = null;
	//ValidUserWebService validUserWebService;
	ComplaintCategoryListWebService ComplaintCategoryListWebService = null;
	SelectedWebService SelectedWebService= null;
	public boolean isValid=false;
	public static boolean isVaildUser=false;
	//ProgressDialog mainDialog;
	ProgressHUD mProgressHUD ;
	String CustomerCareNo="0";
	String DefaultCustomerCareNo="01140115150";
	private static boolean  is_running=false;
	LinearLayout ll_24_ol;
	RelativeLayout btn_reset_mac,btn_logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
	//	setContentView(R.layout.complaints);
		setContentView(R.layout.new_service_request);
		iError=(IError)this;
		
		ImageView ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		
		
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		Utils.log("In Compalint","Executed");
		comments = (EditText)findViewById(R.id.comments);
		//txtcomplaintno = (TextView)findViewById(R.id.txtcomplaintno);
		btnsubmit = (Button)findViewById(R.id.btnsubmit);
		btnhome = (ImageView)findViewById(R.id.btnhome);
		btnprofile = (ImageView)findViewById(R.id.btnprofile);
		btnnotification = (ImageView)findViewById(R.id.btnnotification);
		btnResetPwd = (RelativeLayout)findViewById(R.id.btnResetPwd);
		btnSelfResl = (RelativeLayout)findViewById(R.id.selfrsoluton);
		btnChat = (RelativeLayout)findViewById(R.id.chat_customer);
		btn_reset_mac = (RelativeLayout)findViewById(R.id.btnresetMac);
		btn_logout = (RelativeLayout)findViewById(R.id.btn_logoff);
		
		spinnerList = (Spinner) this.findViewById(R.id.spinnerList);
		ll_24_ol = (LinearLayout)findViewById(R.id.ll_24_ol);
		context = this;
		//mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true,this);
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		
		CustomerCareNo=sharedPreferences.getString("CustomerCareNo", "0");
		otp_password= sharedPreferences.getString("otp_password", "0");
		memberid = (utils.getMemberId());
		comments = (EditText)findViewById(R.id.comments);
		//txtcomplaintno = (TextView)findViewById(R.id.txtcomplaintno);
		
		if(memberid!=null){
			if(memberid.length()>0){
				
			}
			else{
				this.finish();
			}
		}
		else{
			this.finish();
		}
		//Utils.log("MemberId","s"+memberid);
		//Utils.log("otp_password",""+otp_password);
		Utils.log("CustomerCare Number",":"+CustomerCareNo);
		btnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Complaints.this.finish();
				//Intent i = new Intent(Complaints.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		btnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Complaints.this.finish();
				Intent i = new Intent(Complaints.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		btnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Complaints.this.finish();
				Intent i = new Intent(Complaints.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		btnhelp = (ImageView)findViewById(R.id.btnhelp);
		
		btnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Complaints.this.finish();
				Intent i = new Intent(Complaints.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		btncancel = (Button)findViewById(R.id.btncancel);
		btncancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Complaints.this.finish();
				//Intent i = new Intent(Complaints.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		SharedPreferences	sharedPreferences1 = this.getSharedPreferences(
				this.getString(R.string.shared_preferences_renewal), 0);
		
		if(sharedPreferences1.getBoolean("is_24ol", true)){
			btnSelfResl.setVisibility(View.GONE);
			ll_24_ol.setVisibility(View.VISIBLE);
        }
        else{
        	btnSelfResl.setVisibility(View.VISIBLE);
        	ll_24_ol.setVisibility(View.GONE);
        }
		
		btnSelfResl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				
			//Complaints.this.finish();
			
			Intent i = new Intent(Complaints.this,SelfResolution.class);
			i.putParcelableArrayListExtra("complaintcategorylist",complaintcategorylist );
		
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
		});
		
		
		btnChat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				//Complaints.this.finish();
				
				//AlertsBoxFactory.showAlert("This facility is currently not available", Complaints.this);
				
			/*	Intent i = new Intent(Complaints.this,ChatActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
			}
		});
		
		btnResetPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*Complaints.this.finish();
			Intent i = new Intent(Complaints.this,ResetPwdActivity.class);
			startActivity(i);*/
				
				try{
					if(Utils.isOnline(Complaints.this)){
					
					
						
						 final Dialog dialog = new Dialog(context);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							//tell the Dialog to use the dialog.xml as it's layout description
							dialog.setContentView(R.layout.reset_pass);
							/*Display display = ((Activity)ctx). getWindowManager().getDefaultDisplay();
							Point size = new Point();
							display.getSize(size);*/
							int width = 0;
							int height =0;
							
							
							    Point size = new Point();
							    WindowManager w =((Activity)context).getWindowManager();

							    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							        w.getDefaultDisplay().getSize(size);
							        width = size.x;
							        height = size.y;
							    } else {
							        Display d = w.getDefaultDisplay();
							        width = d.getWidth();
							        height   = d.getHeight();;
							    }
							
							TextView	dtv = (TextView) dialog.findViewById(R.id.txt);

							TextView txt = (TextView) dialog.findViewById(R.id.commentss);

							txt.setText(Html.fromHtml("Are you sure you want to Reset the Password?"));

							Button dialogButtonc = (Button) dialog.findViewById(R.id.btn_res_cancell);
							Button dialogButtons = (Button)dialog.findViewById(R.id.btn_res_okk);

							
							dialogButtons.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									
									dialog.dismiss();
									new ResetPasswordWebService().execute();
								}
							});
							
							
							
							dialogButtonc.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
									
								}
							});
							dialog.show();
							//(width/2)+((width/2)/2)
							//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
							dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
							dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
					   }
						
						
						
						
						
						
						
						
						
						
					/*	final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					//tell the Dialog to use the dialog.xml as it's layout description
					dialog.setContentView(R.layout.reset_pass);
					Display display = getWindowManager().getDefaultDisplay();
					Point size = new Point();
					display.getSize(size);
					int width = size.x;
					int height = size.y;
					
					
					
					//dtv = (TextView) dialog.findViewById(R.id.tv1);

					TextView txt = (TextView) dialog.findViewById(R.id.tv);

					txt.setText(Html.fromHtml(message));

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
				*/

					
					else if (rslt.trim().equalsIgnoreCase("not")) {
					mProgressHUD.dismiss();
					AlertsBoxFactory.showAlert("NO DATA FOUND !!! ",context );
				}
			}catch(Exception e){
				Utils.log("Error","is"+e);
				mProgressHUD.dismiss();
				AlertsBoxFactory.showAlert(rslt,context );}	
				
		}
		
		});		
					
				
		/*			
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(Html.fromHtml("Are you sure you want to Reset the Password?"))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(Html.fromHtml("Confirmation"))
				.setCancelable(false)
				
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
					//Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
	        	   
	           }
		     })
	       
		       .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
					//	Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
					new ResetPasswordWebService().execute();
		           }
		       });
		
		
		AlertDialog alert = builder.create();
		alert.show();
			
				}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		*/
		btn_reset_mac.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Utils.isOnline(Complaints.this)){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(Html.fromHtml("Are you sure you want to Release MAC?"))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(Html.fromHtml("Confirmation"))
				.setCancelable(false)
				
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
					//Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
	        	 
	           }
		     })
	       
		       .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
					//	Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
		        	   Utils.log("mob number","s"+utils.getMobileNoPrimary());
		        	   Utils.log("MemberId","s"+utils.getMemberId());
		        	   Utils.log("MemberLoginId","s"+utils.getMemberLoginID());
		        	   new ReleaseMacWebService().execute();
		           }
		       });
		
		
		AlertDialog alert = builder.create();
		alert.show();
			}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(Utils.isOnline(Complaints.this)){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage(Html.fromHtml("Are you sure you want to end your current ION Internet session from Server?"))
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(Html.fromHtml("Confirmation"))
					.setCancelable(false)
					
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
						//Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();		        	 
		           }
			     })
		       
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
						//	Toast.makeText(NotificationListActivity.this, ""+selectedFromList.getNotifyId(), Toast.LENGTH_SHORT).show();
			        	   Utils.log("mob number","s"+utils.getMobileNoPrimary());
			        	   Utils.log("MemberId","s"+utils.getMemberId());
			        	   Utils.log("MemberLoginId","s"+utils.getMemberLoginID());
			        	   new LogOutWebService().execute();
			           }
			       });
			
			
			AlertDialog alert = builder.create();
			alert.show();
				}
					else{
						Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_SHORT).show();
					}
				
			}
		});
		
		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(Utils.isOnline(Complaints.this)){
					Utils.log("submit Button","Submiit Button Executed");
				
				
					if(spinnerList.getSelectedItemPosition()!=0){
						Utils.log("spinner executed","Spinner Executed");
				if(TextUtils.isEmpty(comments.getText().toString().trim()))
				{	
					Utils.log("spinner  empty Text executed","Spinner Emptytext Executed");
					AlertsBoxFactory.showAlert(" Please enter valid comments.",context );
					
				}
				else
				{
					Utils.log("on else ", "selet item selsted Executed");
										
					if((statusResponse.equalsIgnoreCase("DC")||statusResponse.equalsIgnoreCase("AR")))
					{
						Utils.log("status Login","Spinner Executed");
						if(statusResponse.equalsIgnoreCase("DC")){
							AlertsBoxFactory.showAlertOffer("Package Alert","Package has been expired. \n Please renew your package.",context);
						}
						else if(statusResponse.equalsIgnoreCase("AR")&&spinnerList.getSelectedItem().toString().equalsIgnoreCase("Slow Browsing")){						
							AlertsBoxFactory.showAlertOffer("Package Alert","You are on complementary 128kbps plan. \n Please renew your package.",context);
						}
						else {
							new InsertComplaintWebService().execute();
						}
						
					}
					else if((statusResponse.equalsIgnoreCase("AR")|| statusResponse.equalsIgnoreCase("DC")))
					{
							if(statusResponse.equalsIgnoreCase("DC"))
							AlertsBoxFactory.showAlertOffer("Package Alert","Package has been expired. \n Please renew your package.",context);
							else if(statusResponse.equalsIgnoreCase("AR")&&spinnerList.getSelectedItem().toString().equalsIgnoreCase("Slow Browsing")){
							AlertsBoxFactory.showAlertOffer("Package Alert","You are on complementary 128kbps plan. \n Please renew your package.",context);
							}
							else{
								new InsertComplaintWebService().execute();
							}
					}
					else{
						InsertComplaintWebService = new InsertComplaintWebService();
						InsertComplaintWebService.execute((String) null);
					}
					
				}
					}else{
						//AlertsBoxFactory.showAlertOffer("Package Alert","Please select Complaint category type.",context);
						
						AlertsBoxFactory.showAlert2("Please select Complaint category type.",context );
					}
			
				}
				else{
						Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		btncallcustomercare = (RelativeLayout)findViewById(R.id.btncallcustomercare);
		btncallcustomercare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {			
				/*Intent intent = new Intent(Intent.ACTION_CALL);
				if(!CustomerCareNo.equalsIgnoreCase("0")&&!CustomerCareNo.equalsIgnoreCase("anyType{}"))
				intent.setData(Uri.parse("tel:"+CustomerCareNo));
				else
				intent.setData(Uri.parse("tel:"+DefaultCustomerCareNo));
			    startActivity(intent);*/
				isPermissionGranted();

			}
		});
		
		if(Utils.isOnline(Complaints.this)){
			//GetComplaintNoWebService = new GetComplaintNoWebService();
			//GetComplaintNoWebService.execute((String) null);	
			//ComplaintCategoryListWebService =new ComplaintCategoryListWebService();
			//ComplaintCategoryListWebService.execute((String) null);
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new ValidUserWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			else
			new ValidUserWebService().execute();
			
		}
		else{
			Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
		}
		 BaseApplication.getEventBus().register(this);
		// Utils.Last_Class_Name=this.getClass().getSimpleName();
	}

	public  boolean isPermissionGranted() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v("TAG","Permission is granted");

				Intent intent = new Intent(Intent.ACTION_CALL);
				if (!CustomerCareNo.equalsIgnoreCase("0") && !CustomerCareNo.equalsIgnoreCase("anyType{}"))
					intent.setData(Uri.parse("tel:" + CustomerCareNo));
				else
					intent.setData(Uri.parse("tel:" + DefaultCustomerCareNo));


				startActivity(intent);
				return true;
			} else {

				Log.v("TAG","Permission is revoked");
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
				return false;
			}
		}
		else { //permission is automatically granted on sdk<23 upon installation
			Log.v("TAG","Permission is granted");
			return true;
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {

			case 1: {

				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}
	private class GetComplaintNoWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
		//private ProgressDialog Dialog = new ProgressDialog(
		//		Complaints.this);

		protected void onPreExecute() {
			//Dialog.setMessage(getString(R.string.app_please_wait_label));
			mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true,this);
		}

		protected void onPostExecute(Void unused) {
			
			//DiaUtils.dismiss();
			GetComplaintNoWebService = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					txtcomplaintno.setText(complaintno);
					
					ComplaintCategoryListWebService =new ComplaintCategoryListWebService();
					ComplaintCategoryListWebService.execute((String) null);
					
				} catch (NumberFormatException nue) {
					
					
				}

			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
			
			
			
			
			
			
			/*GetComplaintNoWebService = null;
			DiaUtils.dismiss();

			Log.i(">>>>.MemberDetails<<<<<<", mapComplaintNo.toString());
				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapComplaintNo != null) {
					
					Set<String> keys = mapComplaintNo.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					}
					String selItem = str_keyVal.trim();
					isLogout = false;
					//finish();
					ComplaintObj ComplaintNo = mapComplaintNo.get(selItem);
					
					
					txtcomplaintno.setText(ComplaintNo.getMemberComplaintNo());					
					
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){AlertsBoxFactory.showAlert(rslt,context );}	*/
		}
		@Override
		protected Void doInBackground(String... params) {
			try {
				ComplaintCaller complaintnoCaller = new ComplaintCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_COMPLAINTNO)
										);

				complaintnoCaller.memberid = memberid;
				//memberdetailCaller.setAllData(true);
				
				complaintnoCaller.join();
				complaintnoCaller.start();
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
				//DiaUtils.dismiss();
				GetComplaintNoWebService = null;
			}

		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
		}
	
	
	private class InsertComplaintWebService extends AsyncTask<String, Void, Void>implements OnCancelListener {

		ProgressHUD mProgressHUD;
		ComplaintObj complaintobj = new ComplaintObj();

		protected void onPreExecute() {
		
			mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true, this);
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			InsertComplaintWebService = null;
			//submit.setClickable(true);
		}

		
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			//submit.setClickable(true);
			InsertComplaintWebService = null;

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
						Intent i = new Intent(Complaints.this, IONHome.class);
						startActivity(i);
					}
				});
				
				dialog.show();
				//(width/2)+((width/2)/2)
				//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
				
			} else {
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				//Log.i("ComplaintId",""+ComplaintId.get(spinnerList.getSelectedItemPosition()));
				//complaintobj.setMemberComplaintNo(txtcomplaintno.getText().toString());
				complaintobj.setComplaintId(ComplaintId.get(spinnerList.getSelectedItemPosition()));
				complaintobj.setMemberId(memberid);
				complaintobj.setMessage(comments.getText().toString());
				
				InsertComplaintCaller caller = new InsertComplaintCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_INSERT_COMPLAINTS),"complaint");
				
				
				caller.setcomplaintobj(complaintobj);
	
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				//AlertsBoxFactory.showAlert(rslt,context );
				
				Utils.log("Error Complaint","596"+e);
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class ComplaintCategoryListWebService extends AsyncTask<String, Void, Void> {
		//private ProgressDialog Dialog = new ProgressDialog(
			//	Complaints.this);

		protected void onPreExecute() {
			//mainDialog.setMessage(getString(R.string.app_please_wait_label));
			//mainDialog.show();
		}
		@Override
		protected void onCancelled() {
			//DiaUtils.dismiss();
			ComplaintCategoryListWebService = null;
		}
		protected void onPostExecute(Void unused) {
			
			mProgressHUD.dismiss();
			ComplaintCategoryListWebService = null;
		//DiaUtils.dismiss();
		
		if (rslt.trim().equalsIgnoreCase("ok")) {
			ComplaintName = new ArrayList<String>();
			ComplaintId = new ArrayList<String>();
			for(int i=0; i< complaintcategorylist.size(); i++ )
			{
					ComplaintName.add(complaintcategorylist.get(i).getComnplaintName());
					ComplaintId.add(complaintcategorylist.get(i).getComplaintId());
			}
			ArrayAdapter ComplainArray = new ArrayAdapter(Complaints.this, android.R.layout.simple_spinner_item, ComplaintName);
			spinnerList.setAdapter(ComplainArray);
			} else {
				if(is_running)
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
		//new SelectedWebService().execute();
		SelectedWebService= new SelectedWebService();
		SelectedWebService.execute((String) null);
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				ComplaintCategoryListCaller caller = new ComplaintCategoryListCaller(getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_COMPLAINT_CATEGORY_LIST));
				caller.join();
				caller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
			}
			return null;
		}

	}
	
	
	private class SelectedWebService extends AsyncTask<String, Void, Void> {
		//private ProgressDialog Dialog1 = new ProgressDialog(
		//	Complaints.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		//	Dialog1.setCancelable(false);
		//	Dialog1.setMessage(getString(R.string.app_please_wait_label));
		//	Dialog1.show();
			
		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
			ComplaintsStatusCaller caller = new ComplaintsStatusCaller(getApplicationContext()
					.getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_GET_CURRENT_STATUS));
			
			caller.setMemberIdCaller(utils.getMemberId());
			caller.join();
			caller.start();
			statusRslt = "START";

			while (statusRslt == "START") {
				try {
					Thread.sleep(10);
				} catch (Exception ex) {
				}
			}
			} catch (Exception e) {
			/*	AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//Utils.log("Post Execute called","yes");
			mProgressHUD.dismiss();
			
			//Utils.log("Response for status",""+statusResponse);
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
				this.finish();
				//Intent i = new Intent(Complaints.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		is_running=false;
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		is_running=true;
	}
	


	/*protected class ValidUserWebService extends AsyncTask<String, Void, Void> {

		// final AlertDialog alert =new
		// AlertDialog.Builder(Login.this).create();

		private ProgressDialog Dialog = new ProgressDialog(Complaints.this);

		protected void onPreExecute() {
			Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.setCancelable(false);
			Dialog.show();
		
		}

		protected void onPostExecute(Void unused) {
			DiaUtils.dismiss();
			//btnsubmit.setClickable(true);
			validUserWebService = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				//isVaildUser = true;
				if (isVaildUser) {
					//isFinish = true;
					//System.out.println("Count :" + getAuthcount);
					//finish();
					if(getAuthcount.equals("1"))
					{
					Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);
						Login.this.finish();
					Intent intent = new Intent(Login.this,
							Confirmation.class);
					intent.putExtra("mobilenumber",mobilenumber.getText().toString());
					//bundle.putString("password",password.getText().toString());
					//intent.putExtra("com.cnergee.service.home.screen.INTENT", bundle);
					startActivity(intent);
					
					
					}
					else
					{
						
						
					}
				} else {
					Toast.makeText(Complaints.this,getString(R.string.login_invalid),
							Toast.LENGTH_LONG).show();
					return;
				}
			
			} 
			
		

		@Override
		protected Void doInBackground(String... params) {
			try {
				
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
				smsCaller.MemberId = MemberId;
				smsCaller.OneTimePwd=etPassword.getText().toString();
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
				AlertsBoxFactory.showErrorAlert(e.toString(),SMSAuthenticationActivity.this );
			}
			return null;
		}
		@Override
		protected void onCancelled() {
			DiaUtils.dismiss();
			
			validUserWebService = null;
		}
		
	}
*/
	
	/*@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);
	}
*/
	
//*******************************ReleaseMac Web Service*********starts here**************************	
	private class ReleaseMacWebService extends AsyncTask<String, Void, Void>  implements OnCancelListener{
		//private ProgressDialog Dialog1 = new ProgressDialog(
		//	Complaints.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true,this);
			
		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				ReleaseMacCaller caller = new ReleaseMacCaller(getApplicationContext()
					.getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_RESET_MAC),true);
			
			caller.setMemberId(utils.getMemberId());
			caller.setMemberLoginId(utils.getMemberLoginID());
			caller.setMobileNumber(utils.getMobileNoPrimary());
			caller.join();
			caller.start();
			rslt = "START";

			while (rslt == "START") {
				try {
					Thread.sleep(10);
				} catch (Exception ex) {
				}
			}
			} catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), Complaints.this);*/
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//Utils.log("Post Execute called","yes");
			mProgressHUD.dismiss();
			AlertsBoxFactory.showAlert(statusResponseForMac, Complaints.this);
			//Utils.log("Response for status",""+statusResponse);
		}
		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}
	
//*******************************ReleaseMac Web Service*********ends here**************************	
	
//*******************************ResetPassword Web Service*********starts here**************************	
	
	private class ResetPasswordWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{
		//private ProgressDialog Dialog1 = new ProgressDialog(
		//	Complaints.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true,this);
			
		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				ResetPasswordCaller caller = new ResetPasswordCaller(getApplicationContext()
					.getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_RESET_PASSWORD),"complaints");
			
			caller.setMemberId(utils.getMemberId());
			caller.setMemberLoginId(utils.getMemberLoginID());
			
			caller.join();
			caller.start();
			rslt = "START";

			while (rslt == "START") {
				try {
					Thread.sleep(10);
				} catch (Exception ex) {
				}
			}
			} catch (Exception e) {
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), Complaints.this);*/
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//Utils.log("Post Execute called","yes");
			mProgressHUD.dismiss();
			AlertsBoxFactory.showAlert(statusResponseForPwd, Complaints.this);
			//Utils.log("Response for status",""+statusResponse);
		}
		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}
	
//*******************************ResetPassword Web Service*********ends here**************************	
	
	
		
	//*******************************Check Valid User Web Service*********starts here**************************
		protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{

			// final AlertDialog alert =new
			// AlertDialog.Builder(Login.this).create();

			//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);

			protected void onPreExecute() {
				mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true,this);
			
			}

			@SuppressLint("CommitPrefEdits")
		protected void onPostExecute(Void unused) {
				
				mProgressHUD.dismiss();
			
			if (rslt.trim().equalsIgnoreCase("ok")) {

				if (isVaildUser) {
					ComplaintCategoryListWebService =new ComplaintCategoryListWebService();
					ComplaintCategoryListWebService.execute((String) null);
				} else {
					
					BaseApplication.getEventBus().post(
							new FinishEvent(IONHome.class.getSimpleName()));
					
					mProgressHUD.dismiss();
					SharedPreferences sharedPreferences1 = getApplicationContext()
							.getSharedPreferences(sharedPreferences_name, 0); // 0
																				// -
																				// for
																				// private
																				// mode
					SharedPreferences.Editor edit1 = sharedPreferences1.edit();
					edit1.clear();
					edit1.commit();

					SharedPreferences sharedPreferences2 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_renewal),
									0); // 0 - for private mode
					SharedPreferences.Editor edit2 = sharedPreferences2.edit();
					edit2.clear();
					edit2.commit();
					SharedPreferences sharedPreferences3 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_notification_list),
									0); // 0 - for private mode
					SharedPreferences.Editor edit3 = sharedPreferences3.edit();
					edit3.clear();
					edit3.commit();
					SharedPreferences sharedPreferences4 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_payment_history),
									0); // 0 - for private mode
					SharedPreferences.Editor edit4 = sharedPreferences4.edit();
					edit4.clear();
					edit4.commit();
					SharedPreferences sharedPreferences5 = getApplicationContext()
							.getSharedPreferences(
									getString(R.string.shared_preferences_profile),
									0); // 0 - for private mode
					SharedPreferences.Editor edit5 = sharedPreferences5.edit();
					edit5.clear();
					edit5.commit();
					//Utils.log("Data","cleared");

					sharedPreferences1.edit().clear().commit();
					
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
							Complaints.this.finish();
							Intent intent = new Intent(
									Complaints.this,
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
				if(rslt.trim().equalsIgnoreCase("error")){
					iError.display();
				}
			}

		} 
				
			

			@Override
			protected Void doInBackground(String... params) {
				try {
					
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
					smsCaller.setCallData("complaints");
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
		//*******************************Check Valid User Web Service*********ends here**************************
		
		
		//*******************************Logout Web Service*********starts here**************************	
			private class LogOutWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{
				//private ProgressDialog Dialog1 = new ProgressDialog(
				//	Complaints.this);
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					mProgressHUD = ProgressHUD.show(Complaints.this,getString(R.string.app_please_wait_label), true,true,this);
					
				}
				@Override
				protected Void doInBackground(String... arg0) {
					// TODO Auto-generated method stub
					try {
						LogOutCaller caller = new LogOutCaller(getApplicationContext()
							.getResources().getString(
									R.string.WSDL_TARGET_NAMESPACE),
							getApplicationContext().getResources().getString(
									R.string.SOAP_URL), getApplicationContext()
									.getResources().getString(
											R.string.METHOD_LOG_OUT_BROWSER),true);
					
					caller.setMemberId(utils.getMemberId());
					caller.setMemberLoginId(utils.getMemberLoginID());
					
					caller.join();
					caller.start();
					rsltLogOut = "START";

					while (rsltLogOut == "START") {
						try {
							Thread.sleep(10);
						} catch (Exception ex) {
						}
					}
					} catch (Exception e) {
						/*AlertsBoxFactory.showErrorAlert("Error web-service response "
								+ e.toString(), Complaints.this);*/
					}
					return null;
				}
				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					Utils.log("Log outPost Execute called","yes");
					mProgressHUD.dismiss();
					AlertsBoxFactory.showAlert(statusResponseForLogOut, Complaints.this);
					//Utils.log("Response for status",""+statusResponse);
				}
				/* (non-Javadoc)
				 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
				 */
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					
				}
			}

			/* (non-Javadoc)
			 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				
			}
			
		//*******************************LogOut Web Service*********ends here**************************	
			
			@Subscribe
			public void	onFinishEvent(FinishEvent event){
				if(Complaints.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
					Complaints.this.finish();
				}
				
			}
}
