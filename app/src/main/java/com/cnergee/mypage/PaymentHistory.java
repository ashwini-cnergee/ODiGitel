package com.cnergee.mypage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.adapter.PaymentHistoryAdapter;
import com.cnergee.mypage.caller.PaymentDetailsCaller;
import com.cnergee.mypage.caller.PaymentHistoryCaller;
import com.cnergee.mypage.obj.PaymentHistoryObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import all.interface_.IError;
import cnergee.krishna.ookla.odigitel.R;

public class PaymentHistory extends BaseActivity{
			
	TextView dtv;
	Utils utils;
	//ImageView btnhomeimg;
	public static Context context;
	public static String rslt = "";
	private boolean flag=true;
	public int ListSize = 0;
	public static ArrayList<PaymentHistoryObj> paymentlist = new ArrayList<PaymentHistoryObj>();
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	private boolean isFinish = false;
	private String sharedPreferences_name,sharedPreferences_payment_history;
	private Map paymenthistorymap;
	private ListView paymentListView;
	public String PaymentHistory = null;
	private PaymentListWebService getpaymentlistwebservice = null;
	private GetPaymentDetailsWebService getPaymentDetailsWebService = null;
	private String PaymentDate="";
	public static String paymentDetails = "";
	private boolean isActivityrunning=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_list);
		iError=(IError)this;
		utils = new Utils();
		context = this;
		isActivityrunning=true;
		// btnhomeimg = (ImageView)findViewById(R.id.img_header);
		linnhome =(LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile =(LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp =(LinearLayout)findViewById(R.id.inn_banner_help);
		
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentHistory.this.finish();
				//Intent i = new Intent(PaymentHistory.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentHistory.this.finish();
				Intent i = new Intent(PaymentHistory.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentHistory.this.finish();
				Intent i = new Intent(PaymentHistory.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PaymentHistory.this.finish();
				Intent i = new Intent(PaymentHistory.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		
	 
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		paymentListView = (ListView)findViewById(R.id.paymentListView);
		/*
		 * This SharedPrefernce used to check there is change in profile data
		 */		
		SharedPreferences sharedPreferences1 = getApplicationContext()
				.getSharedPreferences(getString(R.string.shared_preferences_payment_history), 0);
		
		if(sharedPreferences1.getBoolean("payment_history", true))
		{
			Utils.log("Data From server PaymentHistory ","yes"+sharedPreferences1.getBoolean("payment_history", true));
			if(Utils.isOnline(PaymentHistory.this)){
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 {
					 getpaymentlistwebservice = new PaymentListWebService();
					 getpaymentlistwebservice .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				 }
				 else{
					 getpaymentlistwebservice = new PaymentListWebService();
					 getpaymentlistwebservice.execute((String) null);
				 }
			}
			else{
				Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
			}
		}
		else{
			Utils.log("Data From server PaymentHistory","offline"+sharedPreferences1.getBoolean("payment_history", true));
			
			String versionName="";
			try {
				versionName = context.getPackageManager()
					    .getPackageInfo(context.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Double.valueOf(versionName)<1.9){
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 {
					 getpaymentlistwebservice = new PaymentListWebService();
					 getpaymentlistwebservice .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				 }
				 else{
					 getpaymentlistwebservice = new PaymentListWebService();
					 getpaymentlistwebservice.execute((String) null);
				 }
			}
			else{
				setOfflinePaymentHistory();
			}
			
			
		}
		/*
		 * 
		 */	
		
		/*getpaymentlistwebservice = new PaymentListWebService();
		getpaymentlistwebservice.execute((String) null);*/
		
		//PaymentHistoryObj payment_history[] = new PaymentHistoryObj[]
		//		{new PaymentHistoryObj("No Payment")};
		//PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(this,R.layout.paymentlistview_row, payment_history);
				

		
				//View header = (View)getLayoutInflater().inflate(R.layout.complaint_listview_header_row, null);
				//paymentListView.addHeaderView(header);
		
			//	paymentListView.setAdapter(adapter);
				//Intent intentService = new Intent(PaymentHistory.this,PaymentHistory.class);
				
				paymentListView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					    // When clicked, show a toast with the TextView text
						if(Utils.isOnline(PaymentHistory.this)){
						PaymentHistoryObj  selectedFromList = (PaymentHistoryObj) paymentListView.getItemAtPosition(position);
						
						if( selectedFromList.getPaymentDate() != null){
							String Paydate = selectedFromList.getPaymentDate();
							SimpleDateFormat dateFormat = new SimpleDateFormat(
					                "dd-MM-yyyy");
					        Date myDate = null;
					        try {
					            myDate = dateFormat.parse(Paydate);

					        } catch (ParseException e) {
					            e.printStackTrace();
					        }

					        SimpleDateFormat timeFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
					        PaymentDate = timeFormat.format(myDate);

					        //System.out.println(PaymentDate);
							
							getPaymentDetailsWebService = new GetPaymentDetailsWebService();
							getPaymentDetailsWebService.execute((String) null);

						}
						}
						else{
							Toast.makeText(PaymentHistory.this,
									"Please connect to internet and try again!!",
									Toast.LENGTH_LONG).show();
						}
					}
				});
				
				
			//	new PaymentListWebService().execute();
				
				
				
	}
	
	
	
	
protected class PaymentListWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{
		
	ProgressHUD mProgressHUD;

		protected void onPreExecute() {
			if(isActivityrunning)			
			mProgressHUD = ProgressHUD.show(PaymentHistory.this,getString(R.string.app_please_wait_label), true,true,this);
			Utils.log("Line193 PaymentHistory","OnPreExceAyncTask");
		}
		protected void onPostExecute(Void unused) {
			if(isActivityrunning)
				mProgressHUD.dismiss();
			if (rslt.trim().equalsIgnoreCase("ok")) {
				
				
				Utils.log("Line 201 PaymentHistory", "OnPostExecuted");
				//Log.i("",""+complaintList.size());
				ListSize = paymentlist.size();
				PaymentHistoryObj payment_history[] = new PaymentHistoryObj[paymentlist.size()];
				//PaymentHistoryObj complaint_id[] = new PaymentHistoryObj[paymentlist.size()];
				Iterator iter = paymentlist.iterator();
				int i = 0;
				paymenthistorymap = new HashMap();
				
				sharedPreferences_payment_history = getString(R.string.shared_preferences_payment_history);
				SharedPreferences sharedPreferences1 = getApplicationContext()
						.getSharedPreferences(sharedPreferences_payment_history, 0); // 0 - for private mode
				
				SharedPreferences.Editor editor = sharedPreferences1.edit();
				editor.putInt("ListSize",paymentlist.size() );
				int j=1;
				//editor.commit();
				if(paymentlist.size()>0){
				for(int k=0;k<paymentlist.size();k++){
					PaymentHistoryObj obj1=paymentlist.get(k);
					//Utils.log("PaymentDate andamount",obj1.getPaymentDate()+" and "+obj1.getAmount());
					editor.putString("PaymentDate"+j,obj1.getPaymentDate());
					editor.putString("Amount"+j,obj1.getAmount());	
					j++;
				}
				}
				else{
					PaymentHistoryObj obj= new PaymentHistoryObj();
					obj.setPaymentDate("No Payments")	;
					obj.setAmount("-");
					//payment_history[0] = obj;
				}
				while(iter.hasNext()){
					PaymentHistoryObj obj = (PaymentHistoryObj)iter.next();
					String id = obj.getPaymentDate();
					String Amount = obj.getAmount();
					
		
					
					
							obj.setPayment(id);
							paymenthistorymap.put(id,obj);
							payment_history[i] = obj;
							//editor.putString("PaymentDate"+j,obj.getPaymentDate());
							//editor.putString("Amount"+j,obj.getAmount());			
							//payment_history[i] = new PaymentHistoryObj(id);
							//complaint_id[i] = new ComplaintListObj(Complain);
							i++;
					
				}
				editor.putBoolean("payment_history",false );
				editor.commit();
				//System.out.println(payment_history);
				PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(PaymentHistory.this,R.layout.paymentlistview_row, payment_history);
				paymentListView.setAdapter(adapter);
			} else {
				if(isActivityrunning){
				//AlertsBoxFactory.showAlert(rslt,context );
					if (rslt.trim().equalsIgnoreCase("error")) {						
						iError.display();
					}
				}
				return;
			}
		}
		@Override
		protected Void doInBackground(String... params) {
			
			try{
				Utils.log("Line 201 PaymentHistory", "On Do inBcknd");
				PaymentHistoryCaller  caller = new PaymentHistoryCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_PAYMENTHISTORY));
				caller.setMemberid(Long.parseLong(utils.getMemberId()));
				
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
				e.printStackTrace();
			}
			
			return null;
		}
		
		 @Override
			protected void onCancelled() {
				if(isActivityrunning)
					mProgressHUD.dismiss();
				getpaymentlistwebservice = null;
			}
		/* (non-Javadoc)
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}

private class GetPaymentDetailsWebService extends
AsyncTask<String, Void, Void> implements OnCancelListener {
	
	ProgressHUD mProgressHUD;
	
	protected void onPreExecute() {
		if(isActivityrunning)
		mProgressHUD = ProgressHUD.show(PaymentHistory.this,getString(R.string.app_please_wait_label), true,true,this);
	}
	
	protected void onPostExecute(Void unused) {
		getPaymentDetailsWebService = null;
		//collectBtn.setClickable(true);
		if(isActivityrunning)
		mProgressHUD.dismiss();
		
		try{
			if (rslt.trim().equalsIgnoreCase("ok")) {
			//	AlertsBoxFactory.showAlert("PAYMENT DETAILS",paymentDetails,context );
				if(isActivityrunning){
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
				
				
				
				dtv = (TextView) dialog.findViewById(R.id.tv1);

				TextView txt = (TextView) dialog.findViewById(R.id.tv);

				txt.setText(Html.fromHtml(paymentDetails));

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
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				if(isActivityrunning){
				mProgressHUD.dismiss();
			
					AlertsBoxFactory.showAlert("NO DATA FOUND !!! ",context );
				}
			}
			}
		}catch(Exception e){
			Utils.log("Error","is"+e);
			if(isActivityrunning){
			mProgressHUD.dismiss();
			
			AlertsBoxFactory.showAlert(rslt,context );
			}}	
			
	}
	
	@Override
	protected Void doInBackground(String... arg0) {
		try {
			
			PaymentDetailsCaller pdCaller = new PaymentDetailsCaller(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_GET_MEMBER_PAYMENTS_DETAILS));
			pdCaller.setPaymentDate(PaymentDate);
			pdCaller.setMemberid(Long.parseLong(utils.getMemberId()));
			pdCaller.join();
			pdCaller.start();
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
	
	 @Override
		protected void onCancelled() {
		 if(isActivityrunning)
		 mProgressHUD.dismiss();
			/*collectBtn.setClickable(true);
			getTodaysCollectionWebService = null;*/
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
	
	this.finish();
	//Intent i= new Intent(PaymentHistory.this,IONHome.class);
	//startActivity(i);
	overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
}

@Override
protected void onPause() {

	super.onPause();
	isActivityrunning=false;
	//this.finish();
}

	public void setOfflinePaymentHistory(){
		sharedPreferences_payment_history=getString(R.string.shared_preferences_payment_history);
		SharedPreferences shared_preferencess= getApplicationContext().getSharedPreferences(sharedPreferences_payment_history, 0);
		int i=shared_preferencess.getInt("ListSize", 0);
		//Utils.log("ListSize",""+i);
		PaymentHistoryObj payment_history1[] = new PaymentHistoryObj[i];
		//Utils.log("ListSize",shared_preferencess.getString("PaymentDate"+1, "-"));
		if(i>0){
			for(int j=1;j<=i;j++){
				//Utils.log("ListSize in for",""+j);
				PaymentHistoryObj obj= new PaymentHistoryObj();
			obj.setPaymentDate(shared_preferencess.getString("PaymentDate"+j, "-"))	;
			obj.setAmount(shared_preferencess.getString("Amount"+j, "-"));
			int k=j-1;
			payment_history1[k] = obj;
			}
		}else
		{
			PaymentHistoryObj obj= new PaymentHistoryObj();
			obj.setPaymentDate("No Payments")	;
			obj.setAmount("-");
			//payment_history1[0] = obj;
		}
		//Utils.log("p history","size"+payment_history1.length);
		PaymentHistoryAdapter adapter = new PaymentHistoryAdapter(PaymentHistory.this,R.layout.paymentlistview_row, payment_history1);
		//adapter.setNotifyOnChange(true);
		adapter.notifyDataSetChanged();
		paymentListView.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		isActivityrunning=true;
		/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
}
