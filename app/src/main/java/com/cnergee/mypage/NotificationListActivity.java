package com.cnergee.mypage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cnergee.mypage.adapter.NotificationAdapter;
import com.cnergee.mypage.adapter.NotificationInterface;
import com.cnergee.mypage.caller.DeleteNotificationCaller;
import com.cnergee.mypage.caller.NotificationCaller;
import com.cnergee.mypage.obj.AuthenticationMobile;
import com.cnergee.mypage.obj.Notificationobj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;
import com.squareup.otto.Subscribe;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import all.interface_.IError;
import cnergee.krishna.ookla.odigitel.R;
import common.SOAP.CommonAsyncTask;
import common.SOAP.CommonSOAP;

public class NotificationListActivity extends BaseActivity implements NotificationInterface,OnCancelListener {

	Utils utils;
	public static Context context;
	public static String rslt = "";
	public static String Deleterslt = "";
	public static String DeleteResponse = "";
	
	 public static String TAG = "NotifiactaionListActivity";
	private boolean flag = true;
	boolean isLogout = false;
	private String logtag = getClass().getSimpleName();
	private String sharedPreferences_name,
			sharedPreferences_payment_notification;
	private ListView notificationlistview;
	public String Complain = null;
	public String Memberid = null;
	public static int ComplaintCount = 0;
	public int ListSize = 0;
	public static ArrayList<Notificationobj> notificationtList = new ArrayList<Notificationobj>();
	private boolean isFinish = false;
	private Map notificationMap;
	LinearLayout linnhome, linnprofile, linnnotification, linnhelp;
	String Notifyid = "";
	ArrayList<String> deltlist;
	CheckBox checkBox ,checkbx;
	Button deltbtn;
	LinearLayout llDeleteBtn;
	static int notListSize = 0;
	public boolean is_activity_running=false;
	IError iError;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iError=(IError)this;
		setContentView(R.layout.activity_notification_list);
		is_activity_running=true;
		BaseApplication.getEventBus().register(this);
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);
		 checkBox = (CheckBox) findViewById(R.id.chkbx);
		 llDeleteBtn=(LinearLayout) findViewById(R.id.llDeleteBtn);
		// checkbx = (CheckBox)findViewById(R.id.chk);
		//deltbtn=(Button)findViewById(R.id.button);
		// updateStatus()
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NotificationListActivity.this.finish();
				Intent i = new Intent(NotificationListActivity.this,
						IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
	/*	checkbx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
			}
		});*/

		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NotificationListActivity.this.finish();
				Intent i = new Intent(NotificationListActivity.this,
						Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
	                    R.anim.slide_out_right);
			}
		});

		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			/*	NotificationListActivity.this.finish();
				Intent i = new Intent(NotificationListActivity.this,
						NotificationListActivity.class);
				startActivity(i);*/
			}
		});

		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				NotificationListActivity.this.finish();
				Intent i = new Intent(NotificationListActivity.this, HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		

		
	
	
		
		utils = new Utils();
		context = this;
		notificationlistview = (ListView)findViewById(R.id.notificationListView);
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0);
		utils.setSharedPreferences(sharedPreferences);			
		

		/*
		 * This SharedPrefernce used to check there is change in profile data
		 */
		SharedPreferences sharedPreferences1 = getApplicationContext()
				.getSharedPreferences(
						getString(R.string.shared_preferences_notification_list),
						0);

		if (sharedPreferences1.getBoolean("notification_list", true)) {
			Utils.log("Data From server Notification_list ", "yes"
					+ sharedPreferences1.getBoolean("notification_list", true));
			if (Utils.isOnline(NotificationListActivity.this)){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new NotificationListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				else
				new NotificationListWebService().execute();
			}
			else{
				setOfflineNotificationList();
				Toast.makeText(getApplicationContext(),
						"Please connect to internet and try again!!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			 Utils.log("Data From server Notification_list","offline :"+sharedPreferences1.getBoolean("notification_list", true));
				setOfflineNotificationList();
			String versionName="";
			try {
				versionName = context.getPackageManager()
					    .getPackageInfo(context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setOfflineNotificationList();
		}

		/*
		 * This SharedPrefernce used to check there is change in profile data
		 */

		/*
		 * This is onClickListener of Notification******starts here**********
		 */
		// if(sharedPreferences1.getInt("NotificationListSize", 0)>0){
		
	
		/*notificationlistview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				if (utils.isOnline(NotificationListActivity.this)) {
					final Notificationobj selectedFromList = (Notificationobj) notificationlistview
							.getItemAtPosition(position);

					if (selectedFromList.getNotifyId() != null) {
						// Utils.log("notify Id","check"+selectedFromList.getNotifyId());
						if (!selectedFromList.getNotifyId().equalsIgnoreCase(
								"-")) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									context);
							builder.setMessage(
									Html.fromHtml("Are you sure you want to delete?"))
									.setIcon(android.R.drawable.ic_dialog_alert)
									.setTitle(Html.fromHtml("Confirmation"))
									.setCancelable(false)

									.setNegativeButton(
											"No",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// Toast.makeText(NotificationListActivity.this,
													// ""+selectedFromList.getNotifyId(),
													// Toast.LENGTH_SHORT).show();
													Utils.log(TAG+"id"+"position"," for position on Deleting "+"id"+"view"+"selectedFromList"+":");
												}
											})

									.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// Toast.makeText(NotificationListActivity.this,
													// ""+selectedFromList.getNotifyId(),
													// Toast.LENGTH_SHORT).show();
													Notifyid = selectedFromList
															.getNotifyId();
													new DeleteNotificationWebService()
															.execute();
													Utils.log(TAG+"getNotifyId","On Position View"+"view");
												}
											});

							AlertDialog alert = builder.create();
							alert.show();

						}

						// getPaymentDetailsWebService = new
						// GetPaymentDetailsWebService();
						// getPaymentDetailsWebService.execute((String) null);

					}
					
					 * }else{ Toast.makeText(getApplicationContext(),
					 * "Sorry for your Inconvinence", Toast.LENGTH_LONG).show();
					 * 
					 * }
					 

				} else {
					Toast.makeText(getApplicationContext(),
							"Please connect to internet and try again",
							Toast.LENGTH_LONG).show();
				}
			}

		});*/
		// }
		// else{
		// Toast.makeText(getApplicationContext(),
		// "There is nothing to delete!!", Toast.LENGTH_LONG).show();
		// }

		/*
		 * This is onClickListener of Notification******ends here*************
		 */
		
		notificationlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.log("Item ","Clicked");
				Notificationobj notificationobj=(Notificationobj) arg0.getItemAtPosition(arg2);
				
			//	Intent i1 = new  Intent(NotificationListActivity.this, IdCardActivity.class);
			//	startActivity(i1);
				if(notificationobj.getDataFrom()!=null){
					if(notificationobj.getDataFrom().length()>0){
						if(notificationobj.getDataFrom().equalsIgnoreCase("Identity Notification")){
							
						String id[]=	notificationobj.getNotificationMessage().split("/=");
						Utils.log("id","is"+id[1]);
						
							Intent i = new  Intent(NotificationListActivity.this, IdCardActivity.class);
							i.putExtra("icard_id", id[1]);
							startActivity(i);
							
							/*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ccrm.i-on.in/Idcard/identity.aspx?id=353318"));
							startActivity(browserIntent);*/
						}
						
						if(notificationobj.getDataFrom().equalsIgnoreCase("SMSPG")){
							
							if(notificationobj.isIs_red()){
								AlertsBoxFactory.showAlert("This link is expired", NotificationListActivity.this);
							}
							else{
								check_valid_pg_sms_request( notificationobj);
							}
							
						}
					}
				}
			}
		});
	}

	protected class NotificationListWebService extends
			AsyncTask<String, Void, Void>  implements OnCancelListener{
		
		ProgressHUD mProgressHUD;

	//	private ProgressDialog Dialog = new ProgressDialog(
		//		NotificationListActivity.this);

		protected void onPreExecute() {
		/*	Dialog.setMessage(getString(R.string.app_please_wait_label));
			Dialog.setCancelable(false);
			Dialog.show();*/
			if(is_activity_running)
				mProgressHUD = ProgressHUD.show(NotificationListActivity.this,getString(R.string.app_please_wait_label), true,true,this);
		}

		protected void onPostExecute(Void unused) {
			if(is_activity_running)
				mProgressHUD.dismiss();
			
			if (rslt.trim().equalsIgnoreCase("ok")) {

				boolean is_red_done=false; 
				Notificationobj notification_data[] = new Notificationobj[notificationtList
						.size()];
				
				Iterator iter = notificationtList.iterator();
				int i = 0;
				notificationMap = new HashMap();

				sharedPreferences_payment_notification = getString(R.string.shared_preferences_notification_list);
				SharedPreferences sharedPreferences1 = getApplicationContext()
						.getSharedPreferences(
								sharedPreferences_payment_notification, 0); // 0
																			// -
				notificationlistview.setVisibility(View.VISIBLE);													// for
																			// private
																			// mode
				//sharedPreferences1.edit().clear();
				SharedPreferences.Editor editor = sharedPreferences1.edit();
				editor.putInt("NotificationListSize", notificationtList.size());
				editor.putBoolean("notification_list", false);
				
				
				Utils.log("Check","notification value "+sharedPreferences1.getBoolean("notification_list", true));
				int j = 1;
				// editor.commit();
				notListSize = notificationtList.size();
				for (int k = 0; k < notificationtList.size(); k++) {
					Notificationobj obj1 = notificationtList.get(k);
					// Utils.log("PaymentDate and amount",obj1.getNotifyId()+" and "+obj1.getNotificationMessage());
					editor.putString("NotifyId" + j, obj1.getNotifyId());
					editor.putString("NotificationMessage" + j,
							obj1.getNotificationMessage());
					editor.putString("CreatedBy" + j,
							obj1.getIcard_id());
					
					editor.putString("dataFrom" + j,
							obj1.getDataFrom());
					
					boolean is_red=false;
					
					if(obj1.getDataFrom().equalsIgnoreCase("SMSPG")){
						if(!is_red_done){
							is_red_done=true;
							obj1.setIs_red(false);
							is_red=false;
						}
						else{
							obj1.setIs_red(true);
							is_red=true;
						}
					}
					else{
						obj1.setIs_red(false);
						is_red=false;
					}
					Utils.log("set is_red"+j, ""+is_red);
					editor.putBoolean("is_red" + j,
							is_red);
					j++;
				}
				
				editor.commit();

				while (iter.hasNext()) {
					Notificationobj obj = (Notificationobj) iter.next();
					String id = obj.getNotifyId();
					Complain = obj.getNotificationMessage();

					String MemberId = id + "," + Complain;
					// Log.i(" >> TEST <<< ",""+MemberId);

					obj.setNotification(id);
					notificationMap.put(id, obj);
					// notification_data[i] = new Notificationobj(id);
					notification_data[i] = obj;
					// complaint_id[i] = new ComplaintListObj(Complain);

					// System.out.println("Noti--"+(Notificationobj)notificationMap.get(notification_data[i]).);
					i++;
				}
				NotificationAdapter adapter = new NotificationAdapter(
						NotificationListActivity.this,
						R.layout.notification_listview_item_row,
						notification_data);
				notificationlistview.setAdapter(adapter);
				
				setOfflineNotificationList();
			} else {
				if(is_activity_running)
					AlertsBoxFactory.showAlert(rslt, context);
				return;
			}

		}

		@Override
		protected Void doInBackground(String... params) {

			Utils.log("Get Notification", "Do in bg");
			try {
				NotificationCaller caller = new NotificationCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.MTHOD_GET_NOTIFICATION));

				caller.setMemberId(utils.getMemberId());

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
				if(is_activity_running)
					AlertsBoxFactory.showErrorAlert(e.toString(), context);

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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		is_activity_running=false;
		// this.finish();
	}

	public void setOfflineNotificationList() {
		sharedPreferences_payment_notification = getString(R.string.shared_preferences_notification_list);
		SharedPreferences shared_preferencess = getApplicationContext()
				.getSharedPreferences(sharedPreferences_payment_notification, 0);
		int i = shared_preferencess.getInt("NotificationListSize", 0);
		// Utils.log("NotificationListSize",""+i);
		Notificationobj[] notificationobj1 = null;
		// Utils.log("ListSize",shared_preferencess.getString("NotifyId"+1, "-"));
		if (i > 0) {
			notificationobj1= new Notificationobj[i];
			for (int j = 1; j <= i; j++) {
				 Utils.log("ListSize in for",""+j);
				Notificationobj obj = new Notificationobj();
				obj.setNotifyId(shared_preferencess.getString("NotifyId" + j,
						"-"));
				obj.setNotificationMessage(shared_preferencess.getString(
						"NotificationMessage" + j, "-"));
				
				obj.setIs_red(shared_preferencess.getBoolean(
						"is_red" + j, false));
				
				obj.setDataFrom(shared_preferencess.getString(
						"dataFrom" + j, "-"));
				
				Utils.log("Notification Message",""+shared_preferencess.getString(
						"NotificationMessage" + j, "-"));
				
				Utils.log("Is Red"+j,""+(shared_preferencess.getBoolean(
						"is_red"+ j, false)));
				int k = j - 1;
				if(obj.getNotificationMessage().equalsIgnoreCase("-")){
					notificationobj1=null;
					Utils.log("NotificationListSize","null");
				}
				else{
					Utils.log("NotificationListSize","not null");
				notificationobj1[k] = obj;
				}
			}
		} else {
			Notificationobj obj = new Notificationobj();
			obj.setNotifyId("0");
			obj.setNotificationMessage("No Payments");
			// notificationobj1[0] = obj;
		}
		// Utils.log("p history","size"+notificationobj1.length);
		if(notificationobj1!=null){
			if(notificationobj1.length>0){
				
				Utils.log("Notification array","size"+notificationobj1.length);
				
				NotificationAdapter adapter = new NotificationAdapter(
				NotificationListActivity.this,
				R.layout.notification_listview_item_row, notificationobj1);
				adapter.notifyDataSetChanged();
				notificationlistview.setAdapter(adapter);
				notificationlistview.setVisibility(View.VISIBLE);
			}
		}
		else{
			notificationlistview.setVisibility(View.GONE);
		}
		// adapter.setNotifyOnChange(true);

		
	}

	public class DeleteNotificationWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener{
		//ProgressDialog prg = new ProgressDialog(NotificationListActivity.this);
		ProgressHUD	mProgressHUD ;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		/*	prg.show();
			prg.setMessage(getString(R.string.app_please_wait_label));
			prg.setCancelable(false);
*/
			mProgressHUD = ProgressHUD.show(NotificationListActivity.this,getString(R.string.app_please_wait_label), true,true,this);
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				DeleteNotificationCaller caller = new DeleteNotificationCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_DELETE_NOTIFICATION));

				caller.setNotifyIdCaller(Notifyid);
				caller.setMemberIdCaller(utils.getMemberId());

				caller.join();
				caller.start();
				Deleterslt = "START";

				while (Deleterslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}
			} catch (Exception e) {
				AlertsBoxFactory.showErrorAlert(e.toString(), context);

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressHUD.dismiss();
			if (DeleteResponse.equalsIgnoreCase("Updated Sucessfully")) {
				new NotificationListWebService().execute();
			} else {
				// Toast.makeText(NotificationListActivity.this, "Not Deleted",
				// Toast.LENGTH_LONG).show();
			}
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

	@Override
	protected void onResume() {
		super.onResume();
		is_activity_running=true;
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

	
	


	

	@Override
	public void showDelete(ArrayList<String> alNotifyId) {
		// TODO Auto-generated method stub
		deltlist = alNotifyId;
		
		
		if(alNotifyId!=null){
			Utils.log("Size","::"+alNotifyId.size());
			if(alNotifyId.size()>0){
				Utils.log("In Visible","Show Delete Executed");
				llDeleteBtn.setVisibility(View.VISIBLE);
				
			}
			else{
				
				Utils.log("In Gone ","Show Delt Executed");
				llDeleteBtn.setVisibility(View.GONE);
			}
		}
		else{
			llDeleteBtn.setVisibility(View.GONE);
		}

	
		llDeleteBtn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			for(int i=0;i<deltlist.size();i++){
				if(Notifyid.length()>0){
					Notifyid+=","+deltlist.get(i);
				}
				else{
					Notifyid=deltlist.get(i);
				}
			}
			Utils.log("Notify Id are",":"+Notifyid);
			if(Utils.isOnline(NotificationListActivity.this)){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				new DeleteNotificationWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			else
				new DeleteNotificationWebService().execute();
			}
			else{
				Toast.makeText(NotificationListActivity.this,
						"Please connect to internet and try again!!",
						Toast.LENGTH_LONG).show();
			}
		}
	});
	
	}

	@Override
	public void showIcard(String id,String datafrom,Notificationobj notificationobj) {
		// TODO Auto-generated method stub
		Utils.log("Id Card","id:"+id);
		if(datafrom.equalsIgnoreCase("Identity Notification")){
			Intent i = new  Intent(NotificationListActivity.this, IdCardActivity.class);
			i.putExtra("icard_id", id);
			startActivity(i);
		}
		
		if(datafrom.equalsIgnoreCase("SMSPG")){
			if(!notificationobj.isIs_red())
			check_valid_pg_sms_request(notificationobj);
			else
			AlertsBoxFactory.showAlert("This link is expired."+"\n"+" Please try another link", NotificationListActivity.this);	
		}
	}
	
	public void check_valid_pg_sms_request(final Notificationobj notificationobj){

		CommonSOAP commonSOAP= new CommonSOAP(
				getApplicationContext().getResources().getString(
						R.string.SOAP_URL),
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_CHECK_VALID_SMS_PG_REQUEST));
		
		SoapObject request = new SoapObject(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.METHOD_CHECK_VALID_SMS_PG_REQUEST));
		
		PropertyInfo pi = new PropertyInfo();
		pi.setName("MemberId");
		pi.setValue(utils.getMemberId());
		pi.setType(String.class);
		request.addProperty(pi);
		commonSOAP.setRequest(request);
		
		pi = new PropertyInfo();
		pi.setName(AuthenticationMobile.CliectAccessName);
		pi.setValue(AuthenticationMobile.CliectAccessId);
		pi.setType(String.class);
		request.addProperty(pi);
		commonSOAP.setRequest(request);
						
		
	
		commonSOAP.setRequest(request);
		
		//final ProgressDialog prgDialog= new ProgressDialog(NotificationListActivity.this);
		final ProgressHUD	prg_bar = ProgressHUD.show(NotificationListActivity.this,getString(R.string.app_please_wait_label), true,true,this);
		new CommonAsyncTask(NotificationListActivity.this){
			
			
			@Override
			protected void onPostExecute(ArrayList<String> result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				prg_bar.dismiss();
				if(result.get(0).equalsIgnoreCase("OK")){
					
					if(result.get(1).length()>0){
						if(result.get(1).equalsIgnoreCase("0")){
							Utils.pg_sms_uniqueid="";
							Utils.pg_sms_request=false;
							AlertsBoxFactory.showAlert("This Link is Expired"+"\n"+"Please call customer care to get a new link.", NotificationListActivity.this);
						}
						else{
							
							String sms_link="";
							String arr_split[]=notificationobj.getNotificationMessage().split(" ");
							for(int i=0;i<arr_split.length;i++){
								if(arr_split[i].contains("http:")){
									sms_link=arr_split[i];
								}
							}
							if(sms_link.length()>0){
							Intent renew_intent=new Intent(NotificationListActivity.this, PGSMS_Webview_Activity.class);
							renew_intent.putExtra("SMS_LINK", sms_link);
							startActivity(renew_intent);
							Utils.pg_sms_request=true;
							Utils.pg_sms_uniqueid=result.get(1);
							}
							else{
								Utils.pg_sms_request=false;
								Utils.pg_sms_uniqueid="";
							}
							
							//int indx=notificationobj.getNotificationMessage().indexOf("http");
							
							
						}
						
					}
					else{
						Utils.pg_sms_request=false;
						Utils.pg_sms_uniqueid="";	
					}
					
					
					
				}
				else{
					if(result.get(0).equalsIgnoreCase("slow")){
						iError.display();
					}
					else{
						AlertsBoxFactory.showAlert("We are unable to process"+"\n"+"Please try again!", NotificationListActivity.this);
					}
					Utils.pg_sms_request=false;
					Utils.pg_sms_uniqueid="";
					
				}
			}
		}.execute(commonSOAP);
		
	
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	@Subscribe
	public void	onFinishEvent(FinishEvent event){
		Utils.log(""+this.getClass().getSimpleName(),"finish");
		Utils.log(""+this.getClass().getSimpleName(),"::"+Utils.Last_Class_Name);
		if(this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			this.finish();
			
		}
		
	}
}
