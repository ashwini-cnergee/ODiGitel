package com.cnergee.mypage;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.GetAdditionalAmountSOAP;
import com.cnergee.mypage.SOAP.GetFinalPackageSOAP;
import com.cnergee.mypage.caller.PackageDetailCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PackageDetails;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cnergee.krishna.ookla.odigitel.R;

public class RenewPackage extends BaseActivity{

	RelativeLayout pickuprequest,upgradepack;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	RelativeLayout creditdebit,netbanking;
	public String memberloginid="";
	private boolean flag= true;
	ProgressDialog mProgressDialog;
	public static String checkValue = "";
	public static String CheckForRenewal = "";
	//ImageView btnhomeimg;
	RelativeLayout rlSpecialOffer;
	String PackageName="",PackageRate="",ServiceTax="",PackageValidity="";
	public static Map<String, PackageDetails> mapPackageDetails;

	boolean isLogout = false;
	private boolean isFinish = false;
	public static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	public long memberid;
	private String sharedPreferences_name;

	private GetMemberDetailWebService getMemberDetailWebService = null;

	public static Map<String, MemberDetailsObj> mapMemberDetails;
	public boolean is_member_check=false;
	public boolean is_allow_renewal=false;

	String check,outstandingAmount;
	String DiscountPercentage="";
	String meesage_restrict="";
	int count_run=1;
	public static boolean  is_renew_running=false;
	Dialog pg_dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renewpackage);

		pickuprequest = (RelativeLayout)findViewById(R.id.pickuprequest);
		linnhome =(LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile =(LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification =(LinearLayout)findViewById(R.id.inn_banner_notification);


		creditdebit = (RelativeLayout)findViewById(R.id.creditdebit);
		netbanking = (RelativeLayout)findViewById(R.id.netbanking);
		rlSpecialOffer = (RelativeLayout)findViewById(R.id.rlSpecialOffer);
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);

		BaseApplication.getEventBus().register(this);

		is_renew_running=true;

		ivMenuDrawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});

		rlSpecialOffer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//RenewPackage.this.finish();
				if(Utils.isOnline(RenewPackage.this)){

					Intent i = new Intent(RenewPackage.this, SpecialOfferActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

				}else {
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}
			}
		});

		context = this;

		Intent i = getIntent();
		check = i.getStringExtra("renew");

		Utils.log("Checked",""+check);

		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode

		utils.setSharedPreferences(sharedPreferences);
		memberloginid = utils.getMemberLoginID();

		SharedPreferences sharedPreferences1 = getApplicationContext()
				.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0); // 0 - for private mode

		PackageName = sharedPreferences1.getString("PackageName", "-");
		PackageRate = sharedPreferences1.getString("Amount", "-");
		PackageValidity = sharedPreferences1.getString("PackageValidity", "-");
		ServiceTax = sharedPreferences1.getString("ServiceTax", "-");

		pickuprequest.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(Utils.isOnline(RenewPackage.this)){
					//new firstRunTask().execute();
					RenewPackage.this.finish();
					Intent i = new Intent(RenewPackage.this,PaymentPickup_New.class);
					i.putExtra("intent_check", true);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}
			}
		});


		//new GetFinalDeductedAmtAsyncTask().execute();
		
		/*btnhomeimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			RenewPackage.this.finish();
			Intent i = new Intent(RenewPackage.this,IONHome.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});*/

		upgradepack = (RelativeLayout)findViewById(R.id.upgradepack);
		upgradepack.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(Utils.isOnline(RenewPackage.this)){
					//RenewPackage.this.finish();
					Intent i = new Intent(RenewPackage.this,PackgedetailActivity.class);
					i.putExtra("check_intent",true);
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}
			}
		});

		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RenewPackage.this.finish();
				//Intent i = new Intent(RenewPackage.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});

		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RenewPackage.this.finish();
				Intent i = new Intent(RenewPackage.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RenewPackage.this.finish();
				Intent i = new Intent(RenewPackage.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);

		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RenewPackage.this.finish();
				Intent i = new Intent(RenewPackage.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});

		creditdebit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(Utils.isOnline(RenewPackage.this)){
					
					/*SharedPreferences sharedPreferences1 = getApplicationContext()
							.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
					int	Atom=sharedPreferences1.getInt("Atom", 0);
					if(Atom>0){
						checkValue="creditdebit";
						if(!is_member_check){
							getMemberDetailWebService = new GetMemberDetailWebService();
							getMemberDetailWebService.execute((String) null);
						}
						else{
							Utils.log("Allow",":"+is_allow_renewal);
							if(is_allow_renewal){
							new GetFinalDeductedAmtAsyncTask().execute();
							}
							else{
								if(is_renew_running)
								AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
							}
							
						}
						
					

						Intent i = new Intent(RenewPackage.this,MakeMyPayments.class);
						i.putExtra("PackageName", PackageName);
						i.putExtra("PackageAmount", PackageRate);
						i.putExtra("PackageValidity", PackageValidity);
						i.putExtra("updateFrom", "I");
						i.putExtra("ServiceTax", ServiceTax);
						i.putExtra("datafrom", "Renew");
						startActivity(i);
						RenewPackage.this.finish();
					}
					else{
						Toast.makeText(getApplicationContext(), Utils.Atom_Message, Toast.LENGTH_LONG).show();
					}*/

					checkValue="creditdebit";
					show_payment_options("creditdebit");
				}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}
			}
		});

		netbanking.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(Utils.isOnline(RenewPackage.this)){
					/*SharedPreferences sharedPreferences1 = getApplicationContext()
							.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
				int	Atom=sharedPreferences1.getInt("Atom", 0);
					if(Atom>0){
						checkValue="netbanking";
						if(!is_member_check){
						getMemberDetailWebService = new GetMemberDetailWebService();
						getMemberDetailWebService.execute((String) null);
						}
						else{
							
							Utils.log("Allow",":"+is_allow_renewal);
								if(is_allow_renewal){
									new GetFinalDeductedAmtAsyncTask().execute();
								}
								else{
									if(is_renew_running)
									AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
								}							
						}				
					}
					else{
						Toast.makeText(getApplicationContext(), Utils.Atom_Message, Toast.LENGTH_LONG).show();
					}*/
					checkValue="netbanking";
					show_payment_options("netbanking");
				}
				else{
					Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}
			}
		});
		if(Utils.isOnline(RenewPackage.this)){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			}
			else{
				getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.execute((String) null);
			}
			checkValue="initial";
		}
		else{
			Toast.makeText(getApplicationContext(), "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
		}
	}

	private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {

		ProgressHUD mProgressHUD;
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(RenewPackage.this,getString(R.string.app_please_wait_label), true,true,this);
			Utils.log("count","is:"+count_run);
			count_run++;
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			mProgressHUD.dismiss();

			try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
					if (mapPackageDetails != null) {

						Utils.log("Allow onPost",":"+is_allow_renewal);
						Set<String> keys = mapPackageDetails.keySet();
						String str_keyVal = "";

						for (Iterator<String> i = keys.iterator(); i.hasNext();) {
							str_keyVal = (String) i.next();

						}
						String selItem = str_keyVal.trim();
						isLogout = false;
						//finish();
						PackageDetails packageDetails = mapPackageDetails.get(selItem);

						is_member_check=true;
					
					/*txtloginid.setText(Details.getMemberLoginId());
					txtregisterdate.setText(packageDetails.getMemberRegsiterDate());
					txtexpdate.setText(packageDetails.getExpiryDate());
					txtpackage.setText(packageDetails.getPackageName());
					txtipaddress.setText(packageDetails.getIpAddress());*/

						PackageName = packageDetails.getPackageName();
						PackageRate = packageDetails.getAmount();
						ServiceTax = packageDetails.getServiceTax();
						PackageValidity = packageDetails.getPackageValidity();
						CheckForRenewal = packageDetails.getCheckForRenewal();

						if(!packageDetails.getCheckForRenewal().equalsIgnoreCase("GO")){
							//	AlertsBoxFactory.showAlertColorTxt("#FFFFFF",""+packageDetails.getCheckForRenewal(),context );
							is_allow_renewal=false;
							Utils.log("Allow NOT GO",":"+is_allow_renewal);
							meesage_restrict=packageDetails.getCheckForRenewal();
							if(is_renew_running)
								AlertsBoxFactory.showAlert(packageDetails.getCheckForRenewal(),context );
						}
						else
						{
							is_allow_renewal=true;
							if(checkValue.equalsIgnoreCase("initial")){


								Utils.log("Allow Initial",":"+is_allow_renewal);
							}
							if(checkValue.equalsIgnoreCase("netbanking")){

								Utils.log("Allow Initial",":"+is_allow_renewal);
								new GetFinalDeductedAmtAsyncTask().execute();

								//new GetFinalPriceAsynctask().execute();
						/*Intent i = new Intent(RenewPackage.this,MakeMyPayments.class);
						i.putExtra("PackageName", PackageName);
						i.putExtra("PackageAmount", PackageRate);
						i.putExtra("PackageValidity", PackageValidity);
						i.putExtra("updateFrom", "I");
						i.putExtra("ServiceTax", ServiceTax);
						i.putExtra("datafrom", "Renew");
						startActivity(i);
						RenewPackage.this.finish();
						overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
							}
							if(checkValue.equalsIgnoreCase("creditdebit")){


								new GetFinalDeductedAmtAsyncTask().execute();
								//new GetFinalPriceAsynctask().execute();
					/*Intent i = new Intent(RenewPackage.this,MakeMyPayments.class);
					i.putExtra("PackageName", PackageName);
					i.putExtra("PackageAmount", PackageRate);
					i.putExtra("PackageValidity", PackageValidity);
					i.putExtra("updateFrom", "I");
					i.putExtra("ServiceTax", ServiceTax);
					i.putExtra("datafrom", "Renew");
					startActivity(i);
					RenewPackage.this.finish();
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
							}
						}

					}
				}else if (rslt.trim().equalsIgnoreCase("not")) {
					if(is_renew_running)
						AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
				}else{
					if(is_renew_running)
						AlertsBoxFactory.showAlert2(rslt,context );
				}
			}catch(Exception e){
				if(is_renew_running)
					AlertsBoxFactory.showAlert(rslt,context );}
			isFinish=true;
		}
		@Override
		protected Void doInBackground(String... params) {
			try {
				PackageDetailCaller packagedetailCaller = new PackageDetailCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_PACKAGE_DETAILS)
				);

				packagedetailCaller.memberloginid = memberloginid;


				packagedetailCaller.join();
				packagedetailCaller.start();
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
	public void onBackPressed() {
		if(check!=null){
			if(check.equalsIgnoreCase("Home")){
				this.finish();
				//Intent i =new Intent(RenewPackage.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
			}
			if(check.equalsIgnoreCase("Self")){
				this.finish();
				//Intent i = new Intent(RenewPackage.this,SelfResolution.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);

			}

			else {
				this.finish();
				//Intent i =new Intent(RenewPackage.this,IONHome.class);
				//startActivity(i);
				Utils.log("on Flag bck", "bck Executed");
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		}
		else{
			this.finish();
			//Intent i =new Intent(RenewPackage.this,IONHome.class);
			//startActivity(i);
			Utils.log("On BackPresed", "on Back Executed");
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//this.finish();
		is_renew_running=false;

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		is_renew_running=true;
		/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
	
	/*	private class firstRunTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(RenewPackage.this);
        
        mProgressDialog.setMessage("Please Wait.....");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    	RenewPackage.this.finish();
    	Intent i = new Intent(RenewPackage.this,PaymentPickup_New.class);
    	i.putExtra("intent_check", true);
    	startActivity(i);
        mProgressdialog.dismiss();
        
    }
}*/

	public class GetFinalPriceAsynctask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		ProgressHUD mProgressHUD;
		String getFinalPriceResult="",getFinalPriceResponse="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(RenewPackage.this,getString(R.string.app_please_wait_label), true,true, this);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetFinalPackageSOAP getFinalPackageSOAP= new GetFinalPackageSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_CALCULATOR_FINAL_PRICE));
			try {
				getFinalPriceResult=getFinalPackageSOAP.getFinalPrice(PackageName, PackageRate);
				getFinalPriceResponse=getFinalPackageSOAP.getResponse();

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
			mProgressHUD.dismiss();
			if(getFinalPriceResult.length()>0){
				if(getFinalPriceResult.equalsIgnoreCase("ok")){
					if(getFinalPriceResponse.length()>0){
						String[] final_price_discount=getFinalPriceResponse.split("#");
						if(final_price_discount.length>1){
							PackageRate=final_price_discount[0];
							DiscountPercentage=final_price_discount[1];
							if(Double.valueOf(PackageRate)>0.0){
								if(is_renew_running){
									//Intent i = new Intent(RenewPackage.this,MakeMyPayments.class);
									Intent i = new Intent(RenewPackage.this,MakeMyPayment_Atom.class);
									i.putExtra("PackageName", PackageName);
									i.putExtra("PackageAmount", PackageRate);
									i.putExtra("PackageValidity", PackageValidity);
									i.putExtra("updateFrom", "S");
									i.putExtra("ServiceTax", ServiceTax);
									i.putExtra("datafrom", "Renew");
									i.putExtra("discount", DiscountPercentage);
									i.putExtra("ClassName", RenewPackage.this.getClass().getSimpleName());
									startActivity(i);
									//RenewPackage.this.finish();
									overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
									//showFinalDialog();
								}
							}
							else{
								AlertsBoxFactory.showAlert(getString(R.string.fail_response), RenewPackage.this);
							}

						}
						else{
							AlertsBoxFactory.showAlert(getString(R.string.fail_response), RenewPackage.this);
						}
					}
					else{
						AlertsBoxFactory.showAlert(getString(R.string.fail_response), RenewPackage.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert(getString(R.string.fail_response), RenewPackage.this);
				}
			}
			else{
				AlertsBoxFactory.showAlert(getString(R.string.fail_response), RenewPackage.this);
			}
		}
		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();


		}
	}

	@Subscribe
	public void	onFinishEvent(FinishEvent event){
		Utils.log(""+RenewPackage.this.getClass().getSimpleName(),"finish");
		Utils.log(""+RenewPackage.this.getClass().getSimpleName(),"::"+Utils.Last_Class_Name);
		if(RenewPackage.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			RenewPackage.this.finish();
		}

	}

	public class GetFinalDeductedAmtAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{

		String getResponse="",getResult="";
		ProgressHUD mProgressHUD;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utils.log("GetFinalDeductedAmtAsyncTask","started");
			mProgressHUD=ProgressHUD.show(RenewPackage.this, getString(R.string.app_please_wait_label), true, false, this);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetAdditionalAmountSOAP  getAdditionalAmountSOAP= new GetAdditionalAmountSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ADDITIONAL_AMT));
			try {

				getResult=getAdditionalAmountSOAP.getAddtionalAmount(memberloginid, "0", "0");
				getResponse=getAdditionalAmountSOAP.getResponse();

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
			mProgressHUD.dismiss();
			if(getResult.length()>0){
				if(getResult.equalsIgnoreCase("ok")){
					if(getResponse.length()>0){
						Utils.log("Get Additional",":"+getResponse);
						parseAdditionalAmtData(getResponse);
					}
				}
			}
		}
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}

	}

	public void parseAdditionalAmtData(String json){
		JSONObject mainJson;
		try {
			mainJson = new JSONObject(json);
			JSONObject NewDataSetJson=mainJson.getJSONObject("NewDataSet");
			if(NewDataSetJson.has("AdditionAmount")){

				JSONObject json_add_amt=NewDataSetJson.getJSONObject("AdditionAmount");
				String PackageRate=json_add_amt.optString("PackageRate","0");
				String AdditionalAmount=json_add_amt.optString("AdditionalAmount","0");
				String AdditionalAmountType=json_add_amt.optString("AdditionalAmountType","");
				String DaysFineAmount=json_add_amt.optString("DaysFineAmount","0");
				String DiscountAmount=json_add_amt.optString("DiscountAmount","0");
                outstandingAmount = json_add_amt.optString("OutStandingAmount","0");
				String finalcharges=json_add_amt.optString("finalcharges","0");
				String FineAmount=json_add_amt.optString("FineAmount","0");
				DiscountPercentage=json_add_amt.optString("OnlineDiscountInPer","0");
				Boolean is_cheque_bounce=json_add_amt.optBoolean("IsChequeBounce",true);
				AdditionalAmount additionalamt_obj= new AdditionalAmount(PackageRate,AdditionalAmount, AdditionalAmountType, DaysFineAmount, DiscountAmount, finalcharges, FineAmount, DiscountPercentage,is_cheque_bounce);


				if(Double.valueOf(finalcharges)>0.0){

					if(is_cheque_bounce){
						AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), RenewPackage.this);
					}else{

						proceed_to_pay(additionalamt_obj);
													
				/*//	Intent i = new Intent(RenewPackage.this,MakeMyPayments_CCAvenue.class);
						
					Intent i = new Intent(RenewPackage.this,MakeMyPayment_Atom.class);
				
					i.putExtra("PackageName", PackageName);
					i.putExtra("PackageAmount", PackageRate);
					i.putExtra("PackageValidity", PackageValidity);
					i.putExtra("updateFrom", "S");
					i.putExtra("ServiceTax", ServiceTax);
					i.putExtra("datafrom", "Renew");
					i.putExtra("discount", DiscountPercentage);
					i.putExtra("addtional_amount", additionalamt_obj);
					
					
					i.putExtra("ClassName", RenewPackage.this.getClass().getSimpleName());
					startActivity(i);
					//RenewPackage.this.finish();
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					//showFinalDialog();*/
					}
				}
				else{
					AlertsBoxFactory.showAlert(getString(R.string.fail_response), RenewPackage.this);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void show_pg_dialog(String check) {
		pg_dialog = new Dialog(RenewPackage.this);
		pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pg_dialog.setContentView(R.layout.dialog_payment_gateway);

		int width = 0;
		int height =0;


		Point size = new Point();
		WindowManager w =((Activity)RenewPackage.this).getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);
			width = size.x;
			height = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			width = d.getWidth();
			height   = d.getHeight();;
		}
		RelativeLayout rl_payment_gateway_1=(RelativeLayout) pg_dialog.findViewById(R.id.rl_pg_1);
		RelativeLayout rl_payment_gateway_2=(RelativeLayout) pg_dialog.findViewById(R.id.rl_pg_2);

		rl_payment_gateway_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.is_CCAvenue=false;
				SharedPreferences sharedPreferences1 = getApplicationContext()
						.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
				int Atom=sharedPreferences1.getInt("Atom", 1);
				if(Atom>0){
					//proceed_to_pay();


					checkValue="netbanking";
					if(!is_member_check){
						getMemberDetailWebService = new GetMemberDetailWebService();
						getMemberDetailWebService.execute((String) null);
					}
					else{

						Utils.log("Allow",":"+is_allow_renewal);
						if(is_allow_renewal){
							new GetFinalDeductedAmtAsyncTask().execute();
						}
						else{
							if(is_renew_running)
								AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
						}
					}

				}
				else{
					AlertsBoxFactory.showAlert(Utils.Atom_Message, RenewPackage.this);
				}
			}
		});

		rl_payment_gateway_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SharedPreferences sharedPreferences1 = getApplicationContext()
						.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
				int	CCAvenue=sharedPreferences1.getInt("CCAvenue", 0);

				if(CCAvenue>0){
					Utils.is_CCAvenue=true;
					//proceed_to_pay();

					checkValue="netbanking";
					if(!is_member_check){
						getMemberDetailWebService = new GetMemberDetailWebService();
						getMemberDetailWebService.execute((String) null);
					}
					else{

						Utils.log("Allow",":"+is_allow_renewal);
						if(is_allow_renewal){
							new GetFinalDeductedAmtAsyncTask().execute();
						}
						else{
							if(is_renew_running)
								AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
						}
					}

				}
				else{
					AlertsBoxFactory.showAlert(Utils.CCAvenue_Message, RenewPackage.this);
				}
			}
		});

		pg_dialog.show();
		pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		pg_dialog.getWindow().setLayout((width/2)+(width/2), LayoutParams.WRAP_CONTENT);
	}

	public void proceed_to_pay(AdditionalAmount additionalAmount){
		Intent i=null;
		RenewPackage.this.finish();
		if(Utils.is_aggrepay) {

			i = new Intent(RenewPackage.this, MakeMyPayment_AggrePay.class);
		}
		i.putExtra("PackageName", PackageName);
		i.putExtra("PackageAmount", additionalAmount.getFinalcharges());
		i.putExtra("PackageValidity", PackageValidity);
		i.putExtra("updateFrom", "S");
		i.putExtra("ServiceTax", ServiceTax);
		i.putExtra("datafrom", "Renew");
		i.putExtra("discount", DiscountPercentage);
		i.putExtra("addtional_amount", additionalAmount);
        i.putExtra("outstandingamount",outstandingAmount);
		i.putExtra("ClassName", RenewPackage.this.getClass().getSimpleName());
		startActivity(i);
		//RenewPackage.this.finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void show_payment_options(String check){
		SharedPreferences sharedPreferences1 = getApplicationContext()
				.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);

		int citrus=sharedPreferences1.getInt("citrus", 0);
		int	CCAvenue=sharedPreferences1.getInt("CCAvenue", 0);
		int Atom=sharedPreferences1.getInt("Atom", 0);
		int PayUMoney=sharedPreferences1.getInt("PAYUMONEY", 0);
		int AggrePay=sharedPreferences1.getInt("AggrePay", 0);

		Utils.log("CCAvenue", ":"+CCAvenue);
		Utils.log("citrus", ":"+citrus);
		Utils.log("PAYUMONEY", ":"+PayUMoney);

		/*if(CCAvenue>0&&Atom>0){
			show_pg_dialog(check);
		}
		else */if(AggrePay>0){
			Utils.is_aggrepay=true;
			start_payment_process();
		}else{
			AlertsBoxFactory.showAlert("Payment Gateways", RenewPackage.this);
		}

		/*else if(Atom>0){
			Utils.is_CCAvenue=false;
			start_payment_process();
		}else if(PayUMoney>0){
			start_payment_process();
		}
		else{
			AlertsBoxFactory.showAlert("Payment Gateways", RenewPackage.this);
		}*/
	}

	public void start_payment_process(){
		if(!is_member_check){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			}
			else{
				getMemberDetailWebService = new GetMemberDetailWebService();
				getMemberDetailWebService.execute((String) null);
			}
		}
		else{

			Utils.log("Allow",":"+is_allow_renewal);
			if(is_allow_renewal){
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					new GetFinalDeductedAmtAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				else
					new GetFinalDeductedAmtAsyncTask().execute();
			}
			else{
				if(is_renew_running)
					AlertsBoxFactory.showAlert(meesage_restrict, RenewPackage.this);
			}

		}
	}
}