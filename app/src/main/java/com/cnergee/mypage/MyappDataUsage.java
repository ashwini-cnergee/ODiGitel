package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnergee.mypage.caller.DataUsageCaller;
import com.cnergee.mypage.obj.CircularProgressBar;
import com.cnergee.mypage.obj.DataUsageObj;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.cnergee.widgets.ProgressPieView;
import com.special.ResideMenu.ResideMenu;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.krishna.ookla.odigitel.R;



public class MyappDataUsage extends BaseActivity {

	
	private static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	
	private String SharedPreferences_name;
	private boolean flag=true;
	public String memberloginid;
	public long memberid;
	ImageView btnhome,btnprofile,btnnotification,btnhelp;
	TextView tvUsed;
	TextView txtused,txtallotedused,txtalotted,txttotalallotted,txtremainig,txttotalremainig;
	private boolean isFinish = false;
	private String sharedPreferences_name;
	
	public static Map<String ,PackageDetails> mapPackageDetails;
	public static Map<String ,DataUsageObj> mapdatausage;
	
	
	private  GetDataUsageWebServices getdatausagewebservice = null;
	
	//public static Map<String, PackageDetails> mapPackageDetails;
	//public static Map<String, DataUsageObj> mapdatausage;
	//private GetMemberDetailWebService getMemberDetailWebService = null; 
	//private GetDataUsageWebServices getdatausagewebservice = null; 
	CircularProgressBar circularBar;
	
	ProgressPieView mProgressPieView;
	boolean isLogout = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.new_myapp_datausage);
	//	txtloginid = (TextView)findViewById(R.id.txtloginid);
		//txtpackage = (TextView)findViewById(R.id.txtpackage);
		
		
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});
		tvUsed=(TextView) findViewById(R.id.tvUsedPercentage);
		mProgressPieView = (ProgressPieView) findViewById(R.id.progressPieViewXml);
		mProgressPieView.setProgressColor(getResources().getColor(R.color.color_red));
		mProgressPieView.setBackgroundColor(getResources().getColor(R.color.color_green));

		txtused = (TextView)findViewById(R.id.txtused);
		txtallotedused = (TextView)findViewById(R.id.txtallotedused);
		txtalotted =(TextView)findViewById(R.id.txtalloted);
		txttotalallotted = (TextView)findViewById(R.id.txttotaslalotted);
		txtremainig = (TextView)findViewById(R.id.txtremaining);
		txttotalremainig =(TextView)findViewById(R.id.txtallotedremain);
		
		btnhome = (ImageView)findViewById(R.id.btnhome);
		btnprofile = (ImageView)findViewById(R.id.btnprofile);
		btnnotification = (ImageView)findViewById(R.id.btnnotification);
		//if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
		     // only for gingerbread and newer versions
			circularBar= (CircularProgressBar) findViewById(R.id.circularprogressbar2);
			
			/**/
		//}

		btnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyappDataUsage.this.finish();
				//Intent i = new Intent(MyappDataUsage.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});
		
		btnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyappDataUsage.this.finish();
				Intent i = new Intent(MyappDataUsage.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		btnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyappDataUsage.this.finish();
				Intent i = new Intent(MyappDataUsage.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		btnhelp = (ImageView)findViewById(R.id.btnhelp);
		
		btnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MyappDataUsage.this.finish();
				Intent i = new Intent(MyappDataUsage.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		context = this;
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		
		memberloginid = utils.getMemberLoginID();
		if(utils.getMemberId().length()>0){
		memberid = Long.valueOf(utils.getMemberId());
		}
		/*if (Utils.isOnline(MyappDataUsage.this)) {
		getdatausagewebservice = new GetDataUsageWebServices();
		getdatausagewebservice.execute((String) null);
		}
		else{
			Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
		}*/
		
		}
		
	 private  class GetDataUsageWebServices extends AsyncTask<String ,Void,Void> implements OnCancelListener {
		// private ProgressDialog Dialog = new ProgressDialog(MyappDataUsage.this);
		 ProgressHUD	 mProgressHUD ;
			protected void onPreExecute() {
				mProgressHUD = ProgressHUD.show(MyappDataUsage.this,getString(R.string.app_please_wait_label), true,true,this);
				Utils.log("MYAppDataUsage 157" ,"Aynck Task Executed");
			}

			protected void onPostExecute(Void unused) {
				getdatausagewebservice = null;
				mProgressHUD.dismiss();

					try{
					if (rslt.trim().equalsIgnoreCase("ok")) {
					if (mapdatausage != null) {
						
						Set<String> keys = mapdatausage.keySet();
						String str_keyVal = "";

						for (Iterator<String> i = keys.iterator(); i.hasNext();) {
							str_keyVal = (String) i.next();

						
						String selItem = str_keyVal.trim();
						isLogout = false;
						//finish();
						 DataUsageObj datausage = mapdatausage.get(selItem);
						
						//txtallotedtime.setText(datausage.getSessionTime());
						//txtusedtime.setText(datausage.getActiveTime());
						//txtusedupload.setText(datausage.getUpLoadData());
						//txtdownloadused.setText(datausage.getDownLoadData());
						//txtallotedtime.setText(datausage.getAllotedData());
					
						txttotalallotted.setText(datausage.getAllotedData());
						txtallotedused.setText(datausage.getTotalDataTransfer());
						txttotalremainig.setText(datausage.getRemainData());
						
						Thread.sleep(1000);
						
						Double GetPercentage=(Double.parseDouble(datausage.getTotalDataTransfer())*100)/Double.parseDouble(datausage.getAllotedData());
						
						mProgressPieView.setMax(100);
							mProgressPieView.animateProgressFill(GetPercentage
									.intValue());
							mProgressPieView.setText("");
							
							tvUsed.setText(GetPercentage
									.intValue()+"% Used");
							
							
							/*	mProgressPieView.setProgress(GetPercentage
									.intValue());
							mProgressPieView.animateProgressFill(GetPercentage
									.intValue());
							mProgressPieView.setText("");
							tvUsed.setText(GetPercentage
									.intValue()+"%\n used");
							
							
							mProgressPieView.setOnProgressListener(new OnProgressListener() {
								
								@Override
								public void onProgressCompleted() {
									// TODO Auto-generated method stub
								
								}
								
								@Override
								public void onProgressChanged(int progress, int max) {
									// TODO Auto-generated method stub
									Utils.log("Progress","is:"+progress);
									tvUsed.setText(progress+"%\n used");
								}
							});*/
							
							txttotalallotted.setText(datausage.getAllotedData());
							txtallotedused.setText(datausage.getTotalDataTransfer());
							txttotalremainig.setText(datausage.getRemainData());
							
							txttotalallotted.invalidate();
							txtallotedused.invalidate();
							txttotalremainig.invalidate();
						//	mProgressPieView.animateProgressFill();
						
						/*circularBar.animateProgressTo(0, GetPercentage.intValue(), new ProgressAnimationListener() {
							
							@Override
							public void onAnimationStart() {				
							}
							
							@Override
							public void onAnimationProgress(int progress) {
								circularBar.setTitle(progress + "%");
								
								tvUsed.setText(progress+"%\n used");
							}
							
							@Override
							public void onAnimationFinish() {
								circularBar.setSubTitle("used");
							}
						});
						*/
						}
					}
				}else if (rslt.trim().equalsIgnoreCase("not")) {
					//AlertsBoxFactory.showAlert("Subscriber not found !!! ",context );
					mProgressPieView.setText("NA");
					mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
					circularBar.setTitle("NA");
					circularBar.setSubTitle("");
				}else{
					circularBar.setTitle("NA");
					mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
					circularBar.setSubTitle("");
					//AlertsBoxFactory.showAlert(rslt,context );
				}
				}catch(Exception e){
					//AlertsBoxFactory.showAlert(rslt,context );
					Utils.log("Error","is:"+e);
					mProgressPieView.setText("NA");
					mProgressPieView.setBackgroundColor(getResources().getColor(R.color.label_white_color));
					circularBar.setTitle("NA");
					circularBar.setSubTitle("");
				}	
			}
			@Override
			protected Void doInBackground(String... params) {
				try {
					Utils.log("In BackGnd Aync203", "AynckTaskExceuted");
					DataUsageCaller datausagecaller = new DataUsageCaller(
							getApplicationContext().getResources().getString(
									R.string.WSDL_TARGET_NAMESPACE),
							getApplicationContext().getResources().getString(
									R.string.SOAP_URL), getApplicationContext()
									.getResources().getString(
											R.string.METHOD_GETUSAGEDETAILS));

					datausagecaller.memberid = memberid;

					datausagecaller.join();
					datausagecaller.start();
					rslt = "START";

					while(rslt == "START")  {
						try {
						
							Thread.sleep(10);
						} catch (Exception ex) {
						
							ex.printStackTrace();
							
							Utils.log("Try","Excuted try");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				isFinish=true;
				return null;
			}
			 @Override
				protected void onCancelled() {
				 mProgressHUD.dismiss();
					getdatausagewebservice = null;
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
		public void onBackPressed() {
			// TODO Auto-generated method stub
			this.finish();
			//Intent i= new Intent(MyappDataUsage.this,IONHome.class);
			//startActivity(i);
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		}

		@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//this.finish();

		}	
		@SuppressLint("InlinedApi")
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
		//ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
			
			/*SharedPreferences sharedPreferences = getApplicationContext()
				//	.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
			//if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
				//ivLogo.setVisibility(View.VISIBLE);
			//else
				//ivLogo.setVisibility(View.INVISIBLE);*/
			
			if (Utils.isOnline(MyappDataUsage.this)) {
				
						
			 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				 getdatausagewebservice = new GetDataUsageWebServices();
				getdatausagewebservice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			 }
			    else{
			    new GetDataUsageWebServices().execute();
			    }
		}
		}
		
	}

