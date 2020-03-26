package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Environment;
import android.provider.Settings.Secure;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.SOAP.AreaWiseSettingSOAP;
import com.cnergee.mypage.SOAP.GetAdditionalAmountSOAP;
import com.cnergee.mypage.SOAP.GetFinalPackageSOAP;
import com.cnergee.mypage.caller.AdjustmentCaller;
import com.cnergee.mypage.caller.PackgeCaller;
import com.cnergee.mypage.caller.SMSAuthenticationCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.PackageList;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.PackageListParsing;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cnergee.krishna.ookla.odigitel.R;

public class ChangePackage extends Activity{

	RadioButton renewOption, adjOption;
	Spinner spinnerList;
	TextView price,validity,servicetax;
	private boolean isFinish = false;
	private String updateFrom="";
	private boolean flag=true;
	public static String rslt = "";
	public static Map<String, PackageList> mapPackageList;
	public static String strXML = "";
	public static String adjStringVal = "";

	static String extStorageDirectory = Environment
			.getExternalStorageDirectory().toString();
	final static String xml_folder = "mypage";
	final static String TARGET_BASE_PATH = extStorageDirectory + "/"
			+ xml_folder + "/";
	public String memberloginid="";
	public String xml_file_postFix = "PackageList.xml";
	public String xml_file;
	public String xml_file_with_path;

	public static Context context;
	public static boolean isAdjOptionClick = false,isVaildUser=false;
	boolean isLogout = false;
	boolean check_intent=false;
	private String sharedPreferences_name;
	public String otp_password="";

	LinearLayout btnpay;
	ImageView btnhome,btnprofile,btnnotification,btnhelp;

	String username;
	String selItem, CurrentPlan;
	String subscriberID, PackageListCode, PrimaryMobileNo, SecondryMobileNo,IsAutoReceipt,PaymentDate;
	double OldPlanRate,NewPlanRate;
	String PackageValidity,ServcieTax;
	ProgressDialog mainDialog;
	String PackageId="";
	/*String AreaCode = "21";
	String AreaCodeFilter ="1";*/
	//String previousPlanRate = "";

	Dialog pg_dialog;

	String MemberId;
	Utils utils = new Utils();
	AdjustmentWebService adjustmentWebService = null;
	PackageListWebService packageListWebService = null;
	public static String settingResult="";

	Bundle extras1;
	String DiscountPercentage="";
	TextView tvLabelDiscount;
	public boolean compulsory_immediate=false;
	AdditionalAmount additionalAmount;


	String PackageRate,AdditionalAmount,AdditionalAmountType, DaysFineAmount,DiscountAmount,finalcharges,FineAmount,  outstandingAmount;
	LinearLayout ll_addtional_details,llClickDetails;
	boolean is_payemnt_detail=false;
	boolean isDetailShow=false;
	TextView TextView02;

	/*@Override
	 public void onDestroy() {
	        super.onDestroy();
	        System.runFinalizersOnExit(true);

	   }*/


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepackage);

		context = this;

		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		utils.setSharedPreferences(sharedPreferences);
		mainDialog= new ProgressDialog(ChangePackage.this);
		MemberId = utils.getMemberId();
		memberloginid = utils.getMemberLoginID();
		check_intent=getIntent().getBooleanExtra("check_intent", false);
		btnhome = (ImageView)findViewById(R.id.btnhome);
		btnprofile = (ImageView)findViewById(R.id.btnprofile);
		btnnotification = (ImageView)findViewById(R.id.btnnotification);
		mapPackageList = new HashMap<String, PackageList>();
		spinnerList = (Spinner) this.findViewById(R.id.planList);
		price = (TextView) findViewById(R.id.price);
		validity = (TextView)findViewById(R.id.validity);
		servicetax = (TextView)findViewById(R.id.servicetax);
		tvLabelDiscount=(TextView) findViewById(R.id.tvLabelDiscount);
		TextView02=(TextView) findViewById(R.id.TextView02);
		xml_file = xml_file_postFix;

		renewOption = (RadioButton) findViewById(R.id.radioRenew);
		adjOption = (RadioButton) findViewById(R.id.radioAdj);
		ll_addtional_details=(LinearLayout)findViewById(R.id.ll_addtional_details);
		llClickDetails=(LinearLayout)findViewById(R.id.llClickDetails);
		otp_password= sharedPreferences.getString("otp_password", "0");
		renewOption.setOnClickListener(updateFromOptionOnClickListener);
		adjOption.setOnClickListener(updateFromOptionOnClickListener);

		ll_addtional_details.setVisibility(View.GONE);
		llClickDetails.setVisibility(View.GONE);
		btnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePackage.this.finish();
				//Intent i = new Intent(ChangePackage.this,IONHome.class);
				//startActivity(i);
				//ChangePackage.this.finish();
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});

		btnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePackage.this.finish();
				Intent i = new Intent(ChangePackage.this,Profile.class);
				startActivity(i);
				ChangePackage.this.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));

			}
		});

		btnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePackage.this.finish();
				Intent i = new Intent(ChangePackage.this,NotificationListActivity.class);
				startActivity(i);
				ChangePackage.this.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));

			}
		});
		btnhelp = (ImageView)findViewById(R.id.btnhelp);

		btnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ChangePackage.this.finish();
				Intent i = new Intent(ChangePackage.this,HelpActivity.class);
				startActivity(i);
				ChangePackage.this.finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));

			}
		});

		llClickDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(is_payemnt_detail){
					if(isDetailShow){
						ll_addtional_details.setVisibility(View.GONE);
						isDetailShow=false;
					}
					else{
						ll_addtional_details.setVisibility(View.VISIBLE);
						isDetailShow=true;
					}

					showPaymentDetails(additionalAmount);
				}
				else{
					ll_addtional_details.setVisibility(View.GONE);
				}
			}
		});
		
		/*Log.i("******* XML FILE ******** ", xml_file);*/
		xml_file_with_path =  TARGET_BASE_PATH + xml_file;
		/*Log.i("******* XML FILE ******** ", xml_file_with_path);*/

		//if (isXMLFile()) {
		//	setPackageList();
		//} else {
		if(Utils.isOnline(ChangePackage.this)){
			//packageListWebService = new PackageListWebService();
			//packageListWebService.execute((String)null);
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				Utils.log("Both Thread"," executed");
				new ValidUserWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
				//new SettingResultAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
			}
			else{
				Utils.log("ValidUser Thread"," executed");
				new ValidUserWebService().execute();
			}
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ChangePackage.this);
			builder.setMessage(
					"Please connect to internet and try again!!")
					.setTitle("Alert")
					.setCancelable(false)
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// Toast.makeText(NotificationListActivity.this,
									// ""+selectedFromList.getNotifyId(),
									// Toast.LENGTH_SHORT).show();

									ChangePackage.this.finish();
									Intent intent = new Intent(
											ChangePackage.this,
											IONHome.class);

									startActivity(intent);
									overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		//}


		spinnerList.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				
				/*renewOption.setChecked(false);
				nowOption.setChecked(false);
				adjOption.setChecked(false);*/
				RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
				radioGroup.clearCheck();

				String selecte_plan = spinnerList.getSelectedItem().toString();
				if(!selecte_plan.equalsIgnoreCase("Select Package")){
					setPlanDetails();
					price.setText(Double.toString(NewPlanRate));
					validity.setText(PackageValidity);
					servicetax.setText(ServcieTax);
					new GetFinalDeductedAmtAsyncTask().execute();


				}else{
					Toast.makeText(ChangePackage.this,
							"Please select valid package from the list",
							Toast.LENGTH_LONG).show();
					price.setText("0");
					validity.setText("0");
					servicetax.setText("0");
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				price.setText("0");
				validity.setText("0");
				servicetax.setText("0");
			}
		});

		btnpay = (LinearLayout)findViewById(R.id.btnpay);
		btnpay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if(spinnerList.getCount()>0){
					if(spinnerList.getSelectedItem().equals("Select package"))
					{
						Toast.makeText(ChangePackage.this,"Please Select Package from Package list",
								Toast.LENGTH_LONG).show();
					}
					else
					{
						SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
						int	isPayMoney=sharedPreferences1.getInt("PAYUMONEY", 0);
						int	CCAvenue=sharedPreferences1.getInt("CCAvenue", 0);

						Utils.log("isPayMoney", ":"+CCAvenue);

						if(CCAvenue>0){
						Utils.log("Compulsory Immediate",":"+compulsory_immediate);
						if(compulsory_immediate){
							if(additionalAmount.isChequeBounced()){
								AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), ChangePackage.this);
							}
							else{
								/*Intent i = new Intent(ChangePackage.this, MakeMyPaymentsAtom.class);
								i.putExtra("PackageName", spinnerList.getSelectedItem().toString());
								i.putExtra("PackageAmount", price.getText().toString());
								i.putExtra("PackageValidity", validity.getText().toString());
								i.putExtra("updateFrom", "I");
								i.putExtra("ServiceTax", servicetax.getText().toString());
								i.putExtra("datafrom", "changepack");
								i.putExtra("ClassName", ChangePackage.this.getClass().getSimpleName());
								i.putExtra("addtional_amount", additionalAmount);
								startActivity(i);
								BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));*/
								show_payment_options();
								//show_pg_dialog();
							}
						}
						else{
							RadioGroup radiogrp = (RadioGroup) findViewById(R.id.radioPayMode);
							int id = radiogrp.getCheckedRadioButtonId();
							Utils.log("id","is:"+id);
							if(id == -1)
							{
								Toast.makeText(ChangePackage.this,"Please Select Package Update details",
										Toast.LENGTH_LONG).show();
							}
							else{
								//ChangePackage.this.finish();	3
								if(additionalAmount.isChequeBounced()){
									AlertsBoxFactory.showAlert(getString(R.string.cheque_bounce_message), ChangePackage.this);
								}
								else{
						/*Intent i = new Intent(ChangePackage.this, MakeMyPaymentsAtom.class);
						i.putExtra("PackageName", spinnerList.getSelectedItem().toString());
						i.putExtra("PackageAmount", price.getText().toString());
						i.putExtra("PackageValidity", validity.getText().toString());
						i.putExtra("updateFrom", updateFrom);
						i.putExtra("ServiceTax", servicetax.getText().toString());
						i.putExtra("datafrom", "changepack");
						i.putExtra("ClassName", ChangePackage.this.getClass().getSimpleName());
						i.putExtra("addtional_amount", additionalAmount);
						
						startActivity(i);
						BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));*/

									show_payment_options();
									//show_pg_dialog();
								}
							}
						}
				}
				else{
					AlertsBoxFactory.showAlert(Utils.Atom_Message, ChangePackage.this);
				}
					}

				}
				else{
					Toast.makeText(ChangePackage.this, "Please connect to internet and try again!!", Toast.LENGTH_LONG).show();
				}

			}
		});
		BaseApplication.getEventBus().register(this);

	}



	OnClickListener updateFromOptionOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(spinnerList.getCount()>0){
				String selecte_plan = spinnerList.getSelectedItem().toString();
				if(selecte_plan.equalsIgnoreCase("Select Package")){
					Toast.makeText(ChangePackage.this,
							"Please select valid package from the list",
							Toast.LENGTH_LONG).show();
					renewOption.setChecked(false);
					adjOption.setChecked(false);
					return;
				}

				if (renewOption.isChecked()) {
					updateFrom = "S";
					isAdjOptionClick = false;
					//setPlanDetails();
					price.setText(Double.toString(NewPlanRate));
					validity.setText(PackageValidity);
					servicetax.setText(ServcieTax);
					new GetFinalDeductedAmtAsyncTask().execute();
					//new GetFinalPriceAsynctask().execute();
					// new UpdatePrice().execute();

				}  else if (adjOption.isChecked()) {
					updateFrom = "I";
					//new AdjustmentWebService().execute();
					AlertsBoxFactory.showAlert("This Feature will <b>Discontinue</b> the Current Package and New Package will be Applied from Today.<br/> Are you Sure? ", ChangePackage.this);
					adjustmentWebService = new AdjustmentWebService();
					adjustmentWebService.execute((String)null);

					//new GetFinalPriceAsynctask().execute();
				}
			}
		}
	};




	private boolean isXMLFile() {

		File file = new File(TARGET_BASE_PATH,xml_file );
		boolean exists = file.exists();

		if (!exists) {
			// It returns false if File or directory does not exist
			if (!file.isFile()) {
				return false;
			} else {
				return true;
			}
		} else {
			// It returns true if File or directory exists
			return true;
		}
	}

	private void setPackageList() {
		// ProgressDialog progressDialog =
		// ProgressDialog.show(ChangePackgeActivity.this,
		// "Loading Package List","Please Wait...");

		try {
			String str_xml = readPackageListXML();
			PackageListParsing packageList = new PackageListParsing(str_xml);
			mapPackageList = packageList.getMapPackageList();

			/*String key = AreaCode + "~" + AreaCodeFilter + "~" + CurrentPlan;
			if (!mapPackageList.containsKey(key)) {
				PackageList init_packageList = new PackageList();
				init_packageList.setAreaCode(AreaCode);
				init_packageList.setAreaCodeFilter(AreaCodeFilter);
				init_packageList.setPackageRate("" + PlanRate);
				init_packageList.setPlanName(CurrentPlan);

				mapPackageList.put(key, init_packageList);
			}*/

		} catch (Exception e) {
			e.printStackTrace();
			AlertsBoxFactory.showErrorAlert("Error XML " + e.toString(),
					context);
		}
		try {
			ArrayAdapter adapterForSpinner = new ArrayAdapter(
					ChangePackage.this,
					android.R.layout.simple_spinner_item);
			spinnerList.setAdapter(adapterForSpinner);
			Set<String> keys = mapPackageList.keySet();
			Iterator<String> i = keys.iterator();
			List<String> unsortList  = new ArrayList<String>();

			while (i.hasNext()) {
				String key = (String) i.next();
				PackageList pl = (PackageList) mapPackageList.get(key);

				if(!pl.getPlanName().equals(CurrentPlan))
					unsortList.add( pl.getPlanName());

			}

			adapterForSpinner.add("Select Package");

			//sort the list
			Collections.sort(unsortList);
			for(String temp: unsortList){
				CharSequence textHolder = "" + temp;
				adapterForSpinner.add(textHolder);
			}
			int defaultPosition = adapterForSpinner.getPosition("Select Package");
			// set the default according to value
			spinnerList.setSelection(defaultPosition);

			// android.os.Debug.waitForDebugger();
			// AlertsBoxFactory.showAlert(">>>  "+tmp_val,context );

		} catch (Exception e) {
			e.printStackTrace();
		}
		// progressdialog.dismiss();
	}

	private String readPackageListXML() {

		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		String str_xml = "";

		try {

			File mydir = context.getDir("mypage", Context.MODE_PRIVATE);
			File xmlFile = new File(mydir,xml_file_postFix);

			br = new BufferedReader(new FileReader(xmlFile));
			// br = new BufferedReader(new
			// InputStreamReader(getAssets().open("xml/PackageList.xml")));
			String temp;
			while ((temp = br.readLine()) != null)
				sb.append(temp);
			str_xml = sb.toString();
			// Log.i(">>>XML <<< ", str_xml);

		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(ChangePackage.this,
					"PackageList File Not Found.", Toast.LENGTH_LONG).show();
			Toast.makeText(ChangePackage.this,
					"Create file mypage/PackageList.xml", Toast.LENGTH_LONG)
					.show();

		} finally {
			try {
				if(br != null)
					br.close(); // stop reading
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return str_xml;
	}

	private void writeXMLFile() {
		// Log.i(">>>XML <<< ", strXML);
		try {
			File direct = new File(extStorageDirectory + "/" + xml_folder);
			if (!direct.exists()) {
				direct.mkdirs(); // directory is created;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		FileOutputStream fOut = null;
		OutputStreamWriter xmlOutWriter = null;

		try {
			File mydir = context.getDir("mypage", Context.MODE_PRIVATE);
			File xmlFile = new File(mydir,xml_file_postFix);
			xmlFile.createNewFile();
			fOut = new FileOutputStream(xmlFile);
			xmlOutWriter = new OutputStreamWriter(fOut);
			xmlOutWriter.append(strXML);

			/*Toast.makeText(getBaseContext(),
					"Done writing '/mypage/'"+xml_file+" file.",
					Toast.LENGTH_LONG).show();*/
			// new ReadPackageListXML().execute();

		}catch(IOException io){
			io.printStackTrace();
			Toast.makeText(getBaseContext(), io.getMessage(), Toast.LENGTH_LONG)
					.show();
		}catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		} finally {
			try {
				if(xmlOutWriter != null){
					xmlOutWriter.flush();
					xmlOutWriter.close();
				}
				if(xmlOutWriter != null){
					fOut.flush();
					fOut.close();
				}

			} catch (IOException io) {
			}
			strXML = null;
		}
		setPackageList();
	}

	private class PackageListWebService extends AsyncTask<String, Void, Void>implements OnCancelListener {

		ProgressHUD mProgressHUD;
		String ConnectionTypeId="",AreaId="";
		protected void onPreExecute() {

			mProgressHUD = ProgressHUD.show(ChangePackage.this,getString(R.string.app_please_wait_label),true,true,this);
			SharedPreferences sharedPreferences1 = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
			ConnectionTypeId=sharedPreferences1.getString("ConnectionTypeId", "0");

			AreaId=sharedPreferences1.getString("AreaCode", "0");

		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			packageListWebService = null;
		}
		protected void onPostExecute(Void unused) {


			packageListWebService = null;
			mProgressHUD.dismiss();

			if (rslt.trim().equalsIgnoreCase("ok")) {
				writeXMLFile();

			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				PackgeCaller caller = new PackgeCaller(getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_PACKAGELIST_BY_CONNECTIONTYPEID));
				caller.setMemberId(Long.parseLong(MemberId));
				caller.setConnectionTypeId(ConnectionTypeId);
				caller.setAreaCode(AreaId);
				//caller.join();
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
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

	}

	private class AdjustmentWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
		ProgressHUD mProgressHUD;



		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(ChangePackage.this,getString(R.string.app_please_wait_label), true,false, this);

		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			adjustmentWebService = null;

		}
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			adjustmentWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					NewPlanRate = Double.parseDouble(adjStringVal);
					if(NewPlanRate!=0){
						if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
							NewPlanRate=(NewPlanRate+Double.valueOf(additionalAmount.getAdditionalAmount()));
						}
						if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
							NewPlanRate=(NewPlanRate-Double.valueOf(additionalAmount.getDiscountAmount()));
						}
						additionalAmount.setFinalcharges(String.valueOf(NewPlanRate));
						price.setText(Double.toString(NewPlanRate));
						validity.setText(PackageValidity);
						servicetax.setText(ServcieTax);
						isAdjOptionClick = true;
					}
					else{
						RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
						radioGroup.clearCheck();
						/*Log.i(">>>>>New PLan Rate<<<<<<", adjStringVal);*/

						AlertsBoxFactory.showAlert("Conversion is not possible.", context);


					}
				} catch (NumberFormatException nue) {
					RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
					radioGroup.clearCheck();
					/*Log.i(">>>>>New PLan Rate<<<<<<", adjStringVal);*/
					if(adjStringVal.equalsIgnoreCase("anyType{}")){
						AlertsBoxFactory.showAlert("Conversion is not possible.", context);
					}else{
						AlertsBoxFactory.showAlert(adjStringVal, context);
					}
				}

			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			isFinish=true;
			//this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {

			try {
				AdjustmentCaller adjCaller = new AdjustmentCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL),
						getApplicationContext().getResources().getString(
								R.string.METHOD_PACKAGEADJUSTMENT));
				adjCaller.setMemberId(Long.parseLong(MemberId));
				setPlanDetails();
				adjCaller.setNewPlan(str_planname);
				//adjCaller.setAreaCode(AreaCode);
				//adjCaller.setAreaCodeFilter(AreaCodeFilter);

				adjCaller.join();
				adjCaller.start();
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
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

	}




	private String str_planname;
	private void setPlanDetails() {

		String selecte_plan = spinnerList.getSelectedItem().toString();
		String map_kye = selecte_plan;

		PackageList packageList = (PackageList) mapPackageList
				.get(map_kye);
		if (packageList != null) {
			str_planname = packageList.getPlanName();
			//str_planrate = packageList.getPackageRate();
			try{

				NewPlanRate = Double.parseDouble(packageList.getPackageRate());
				PackageValidity = (packageList.getPackagevalidity());
				ServcieTax = (packageList.getServiceTax());
				PackageId= (packageList.getPackageId());
			}catch(NumberFormatException nu){
				NewPlanRate = 0;
				PackageValidity = "0";
				ServcieTax = "0";
				PackageId="";
			}
		} else {
			str_planname = CurrentPlan;
			//str_planrate = PlanRate + "";
			NewPlanRate = 0;
			PackageValidity = "0";
			ServcieTax = "0";
			PackageId="";
		}
	}




	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.finish();
		if(check_intent){
			this.finish();
			//Intent i = new Intent(ChangePackage.this,RenewPackage.class);
			//startActivity(i);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		}
		else{
			Intent i = new Intent(ChangePackage.this,IONHome.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		}
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

	}

//*******************************Check Valid User Web Service*********starts here**************************

	protected class ValidUserWebService extends AsyncTask<String, Void, Void> implements OnCancelListener  {


		ProgressHUD mProgressHUD;


		// final AlertDialog alert =new
		// AlertDialog.Builder(Login.this).create();

		//private ProgressDialog Dialog = new ProgressDialog(SMSAuthenticationActivity.this);

		protected void onPreExecute() {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				Utils.log("Valid User","Progress Not Showing");
			}
			else{
				mProgressHUD = ProgressHUD.show(ChangePackage.this,getString(R.string.app_please_wait_label), true,true, this);
			}

		}

		@SuppressLint("CommitPrefEdits")
		protected void onPostExecute(Void unused) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){

			}
			else{
				mProgressHUD.dismiss();
			}
			if (rslt.trim().equalsIgnoreCase("ok")) {

				if (isVaildUser) {
					//new PackageListWebService().execute();
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
						new SettingResultAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR );
					}
					else{
						new SettingResultAsyncTask().execute();
					}
				} else {
					//mProgressHUD.dismiss();
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
					//	Utils.log("Data","cleared");
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
							ChangePackage.this.finish();
							Intent intent = new Intent(
									ChangePackage.this,
									LoginFragmentActivity.class);
							intent.putExtra("from", "2");
							startActivity(intent);
							BaseApplication.getEventBus().post(new FinishEvent(IONHome.class.getSimpleName()));
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
				//mProgressHUD.dismiss();
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
				smsCaller.OneTimePwd=otp_password;
				//smsCaller.setAllData(true);
				smsCaller.setCallData("changepack");
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
			//mProgressHUD.dismiss();
			mProgressHUD.dismiss();
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

/*		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}*/

	}

//*******************************Check Valid User Web Service*********ends here**************************

	//******************************* Web Service*********starts here**************************
	public class SettingResultAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		String Arearslt="";
		ProgressHUD mProgressHUD;
		@Override
		protected void onPreExecute() {

			mProgressHUD = ProgressHUD.show(ChangePackage.this,getString(R.string.app_please_wait_label), true,false, this);
			super.onPreExecute();
			settingResult="";
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub

			AreaWiseSettingSOAP areaWiseSettingSOAP= new  AreaWiseSettingSOAP(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
					.getResources().getString(
							R.string.METHOD_GET_AREA_SETTING)
			);
			SharedPreferences sharedPreferences1 = getApplicationContext()
					.getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);
			String areaCode=sharedPreferences1.getString("AreaCode", "0");

			try {
				Arearslt=	areaWiseSettingSOAP.getAreaWiseSetting(areaCode, "UP");
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


			mProgressHUD.dismiss();
			super.onPostExecute(result);
			if(Arearslt.length()>0){
				if(Arearslt.equalsIgnoreCase("ok")){
					if(settingResult.equalsIgnoreCase("0")){
						AlertDialog.Builder builder = new AlertDialog.Builder(
								ChangePackage.this);
						builder.setMessage(
								"This Facility is not available !! ")
								.setTitle("Alert")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog, int id) {
												// Toast.makeText(NotificationListActivity.this,
												// ""+selectedFromList.getNotifyId(),
												// Toast.LENGTH_SHORT).show();

												ChangePackage.this.finish();
												Intent intent = new Intent(
														ChangePackage.this,
														IONHome.class);

												startActivity(intent);
											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}
					else{
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
							new PackageListWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
						else{
							new PackageListWebService().execute();
						}
					}
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(
							ChangePackage.this);
					builder.setMessage(
							"Please try again!! ")
							.setTitle("Alert")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// Toast.makeText(NotificationListActivity.this,
											// ""+selectedFromList.getNotifyId(),
											// Toast.LENGTH_SHORT).show();

											ChangePackage.this.finish();
											Intent intent = new Intent(
													ChangePackage.this,
													IONHome.class);

											startActivity(intent);
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}

			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangePackage.this);
				builder.setMessage(
						"Please try again!!  ")
						.setTitle("Alert")
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialog, int id) {
										// Toast.makeText(NotificationListActivity.this,
										// ""+selectedFromList.getNotifyId(),
										// Toast.LENGTH_SHORT).show();

										ChangePackage.this.finish();
										Intent intent = new Intent(
												ChangePackage.this,
												IONHome.class);

										startActivity(intent);
									}
								});
				AlertDialog alert = builder.create();
				alert.show();
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}



	}
	//******************************* Web Service*********starts here**************************	

	public class GetFinalPriceAsynctask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		ProgressHUD mProgressHUD;
		String getFinalPriceResult="",getFinalPriceResponse="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(ChangePackage.this,getString(R.string.app_please_wait_label), true,false, this);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetFinalPackageSOAP getFinalPackageSOAP= new GetFinalPackageSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_CALCULATOR_FINAL_PRICE));
			try {
				getFinalPriceResult=getFinalPackageSOAP.getFinalPrice(spinnerList.getSelectedItem().toString(), String.valueOf(NewPlanRate));
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
							NewPlanRate=Double.valueOf(final_price_discount[0]);
							DiscountPercentage=final_price_discount[1];
							if(NewPlanRate>0.0){
								price.setText(String.valueOf(NewPlanRate));
								if(Double.valueOf(DiscountPercentage)>0){
									tvLabelDiscount.setVisibility(View.VISIBLE);
									//tvLabelDiscount.setText("You have got "+DiscountPercentage+"% discount for online payment.");
									tvLabelDiscount.setText("Avail of a "+DiscountPercentage+"% discount by paying online.");
									if(updateFrom.equalsIgnoreCase("I")){
										adjustmentWebService = new AdjustmentWebService();
										adjustmentWebService.execute((String)null);
									}
								}
								else{
									tvLabelDiscount.setVisibility(View.GONE);
									if(updateFrom.equalsIgnoreCase("I")){
										adjustmentWebService = new AdjustmentWebService();
										adjustmentWebService.execute((String)null);
									}

								}
								//showFinalDialog();
							}
							else{
								AlertsBoxFactory.showAlert(getString(R.string.fail_response), ChangePackage.this);
							}

						}
						else{
							AlertsBoxFactory.showAlert(getString(R.string.fail_response), ChangePackage.this);
						}
					}
					else{
						AlertsBoxFactory.showAlert(getString(R.string.fail_response), ChangePackage.this);
					}
				}
				else{
					AlertsBoxFactory.showAlert(getString(R.string.fail_response), ChangePackage.this);
				}
			}
			else{
				AlertsBoxFactory.showAlert(getString(R.string.fail_response), ChangePackage.this);
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
		if(ChangePackage.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			ChangePackage.this.finish();
		}


	}

	public class GetFinalDeductedAmtAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{

		String getResponse="",getResult="";
		ProgressHUD mProgressHUD;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			ll_addtional_details.setVisibility(View.GONE);
			llClickDetails.setVisibility(View.GONE);
			Utils.log("GetFinalDeductedAmtAsyncTask","started");
			mProgressHUD=ProgressHUD.show(ChangePackage.this, getString(R.string.app_please_wait_label), true, false, this);
		}
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetAdditionalAmountSOAP  getAdditionalAmountSOAP= new GetAdditionalAmountSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_ADDITIONAL_AMT));
			try {
				getResult=getAdditionalAmountSOAP.getAddtionalAmount(memberloginid, PackageId, "0");
				Utils.log("PackageID",":"+PackageId);
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
				PackageRate=json_add_amt.optString("PackageRate","0");
				NewPlanRate=Double.valueOf(json_add_amt.optString("finalcharges","0"));
				AdditionalAmount=json_add_amt.optString("AdditionalAmount","0");
				AdditionalAmountType=json_add_amt.optString("AdditionalAmountType","");
				DaysFineAmount=json_add_amt.optString("DaysFineAmount","0");
				DiscountAmount=json_add_amt.optString("DiscountAmount","0");
                outstandingAmount = json_add_amt.optString("OutStandingAmount","0");
				finalcharges=json_add_amt.optString("finalcharges","0");
				FineAmount=json_add_amt.optString("FineAmount","0");
				DiscountPercentage=json_add_amt.optString("OnlineDiscountInPer","0");
				Boolean is_cheque_bounce=json_add_amt.optBoolean("IsChequeBounce",true);
				additionalAmount= new AdditionalAmount(PackageRate,AdditionalAmount, AdditionalAmountType, DaysFineAmount, DiscountAmount, finalcharges, FineAmount, DiscountPercentage,is_cheque_bounce);
				if(DiscountPercentage.length()>0){
					if(Double.valueOf(DiscountPercentage)>0)
						// tvLabelDiscount.setText("You have got "+DiscountPercentage+" % discount for online payment.");
						tvLabelDiscount.setText("Avail of a "+DiscountPercentage+"% discount by paying online.");
				}
				if(AdditionalAmountType.length()>0){
					renewOption.setVisibility(View.GONE);
					adjOption.setVisibility(View.GONE);
					TextView02.setVisibility(View.GONE);
					compulsory_immediate=true;
				}
				else{
					renewOption.setVisibility(View.VISIBLE);
					adjOption.setVisibility(View.VISIBLE);
					TextView02.setVisibility(View.VISIBLE);
					compulsory_immediate=false;
				}

				if(Double.valueOf(additionalAmount.getFinalcharges())>0){
					price.setText(additionalAmount.getFinalcharges());

				}
				if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
					is_payemnt_detail=true;
				}
				if(Double.valueOf(additionalAmount.getFineAmount())>0){
					is_payemnt_detail=true;
				}
				if(Double.valueOf(additionalAmount.getDiscountAmount())>0){
					is_payemnt_detail=true;
				}
				if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
					is_payemnt_detail=true;
				}
				if(is_payemnt_detail){
					price.setText(additionalAmount.getFinalcharges());
					llClickDetails.setVisibility(View.VISIBLE);
				}
				else{
					llClickDetails.setVisibility(View.GONE);
				}

				if(Double.valueOf(finalcharges)>0.0){
					/*Intent i = new Intent(ChangePackage.this,MakeMyPayments.class);
					i.putExtra("PackageName", spinnerList.getSelectedItem().toString());
					i.putExtra("PackageAmount", PackageRate);
					i.putExtra("PackageValidity", PackageValidity);
					i.putExtra("updateFrom", "I");
					i.putExtra("ServiceTax", servicetax.getText().toString());
					i.putExtra("datafrom", "Renew");
					i.putExtra("discount", DiscountPercentage);
					i.putExtra("addtional_amount", additionalamt_obj);
					i.putExtra("ClassName", ChangePackage.this.getClass().getSimpleName());
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
					
					/*i.putExtra("AdditionalAmount", AdditionalAmount);
					i.putExtra("AdditionalAmountType", AdditionalAmountType);
					i.putExtra("DaysFineAmount", DaysFineAmount);
					i.putExtra("DiscountAmount", DiscountAmount);
					i.putExtra("finalcharges", finalcharges);
					i.putExtra("DaysFineAmount", DaysFineAmount);
					i.putExtra("finalcharges", finalcharges);
					i.putExtra("FineAmount", FineAmount);*/

					//RenewPackage.this.finish();

					//showFinalDialog();
				}
				else{
					AlertsBoxFactory.showAlert(getString(R.string.fail_response), ChangePackage.this);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void showPaymentDetails(AdditionalAmount additionalAmount){
		if(additionalAmount!=null){
			//ll_addtional_details.setVisibility(View.VISIBLE);
			LinearLayout ll_package_rate,ll_add_amt,ll_add_reason,ll_discount_amt,ll_fine_amount,ll_days_fine_amt,ll_discount_per,ll_final_amt;

			TextView tv_package_rate,tv_add_amt,tv_add_reason,tv_discount_amt,tv_fine_amount,tv_days_fine_amt,tv_discount_per,tv_final_amt;

			ll_package_rate=(LinearLayout) findViewById(R.id.ll_package_rate);
			ll_add_amt=(LinearLayout) findViewById(R.id.ll_add_amt);
			ll_add_reason=(LinearLayout) findViewById(R.id.ll_add_reason);
			ll_discount_amt=(LinearLayout) findViewById(R.id.ll_discount_amt);
			ll_fine_amount=(LinearLayout) findViewById(R.id.ll_fine_amt);
			ll_days_fine_amt=(LinearLayout) findViewById(R.id.ll_days_fine_amt);
			ll_discount_per=(LinearLayout) findViewById(R.id.ll_discount_per);
			ll_final_amt=(LinearLayout) findViewById(R.id.ll_final_amount);

			tv_package_rate=(TextView) findViewById(R.id.tv_package_rate);
			tv_add_amt=(TextView) findViewById(R.id.tv_add_amt);
			tv_add_reason=(TextView) findViewById(R.id.tv_add_reason);
			tv_discount_amt=(TextView) findViewById(R.id.tv_discount_amt);
			tv_fine_amount=(TextView) findViewById(R.id.tv_fine_amt);
			tv_days_fine_amt=(TextView) findViewById(R.id.tv_days_fine_amt);
			tv_discount_per=(TextView) findViewById(R.id.tv_discount_per);
			tv_final_amt=(TextView) findViewById(R.id.tv_final_amount);

			if(Double.valueOf(additionalAmount.getPackageRate())>0){
				ll_package_rate.setVisibility(View.VISIBLE);
				tv_package_rate.setText(additionalAmount.getPackageRate());
			}
			else{

				ll_package_rate.setVisibility(View.GONE);

			}

			if(Double.valueOf(additionalAmount.getAdditionalAmount())>0){
				ll_add_amt.setVisibility(View.VISIBLE);
				tv_add_amt.setText(additionalAmount.getAdditionalAmount());
			}
			else{

				ll_add_amt.setVisibility(View.GONE);

			}

			if(additionalAmount.getAdditionalAmountType().length()>0){
				ll_add_reason.setVisibility(View.GONE);
				tv_add_reason.setText(additionalAmount.getAdditionalAmountType());
			}
			else{

				ll_add_reason.setVisibility(View.GONE);

			}

			if(Double.valueOf(additionalAmount.getDiscountAmount())>0){
				ll_discount_amt.setVisibility(View.VISIBLE);
				tv_discount_amt.setText(additionalAmount.getDiscountAmount());
			}
			else{

				ll_discount_amt.setVisibility(View.GONE);

			}

			if(Double.valueOf(additionalAmount.getFineAmount())>0){
				ll_fine_amount.setVisibility(View.VISIBLE);
				tv_fine_amount.setText(additionalAmount.getFineAmount());
			}
			else{

				ll_fine_amount.setVisibility(View.GONE);

			}

			if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
				ll_days_fine_amt.setVisibility(View.VISIBLE);
				tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
			}
			else{

				ll_days_fine_amt.setVisibility(View.GONE);

			}
			if(additionalAmount.getDiscountPercentage().length()>0){
				if(Double.valueOf(additionalAmount.getDiscountPercentage())>0){
					ll_discount_per.setVisibility(View.VISIBLE);
					tv_discount_per.setText(additionalAmount.getDiscountPercentage());
				}
				else{
					ll_discount_per.setVisibility(View.GONE);
				}
			}
			else{
				ll_discount_per.setVisibility(View.GONE);
			}

			if(Double.valueOf(additionalAmount.getFinalcharges())>0){
				ll_final_amt.setVisibility(View.VISIBLE);
				tv_final_amt.setText(additionalAmount.getFinalcharges());
			}
			else{
				ll_final_amt.setVisibility(View.GONE);
			}


		}
		else{
			ll_addtional_details.setVisibility(View.GONE);
		}
	}

	public void show_pg_dialog() {
		pg_dialog = new Dialog(ChangePackage.this);
		pg_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pg_dialog.setContentView(R.layout.dialog_payment_gateway);

		int width = 0;
		int height =0;


		Point size = new Point();
		WindowManager w =((Activity)ChangePackage.this).getWindowManager();

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
					proceed_to_pay();
				}
				else{
					AlertsBoxFactory.showAlert(Utils.Atom_Message, ChangePackage.this);
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
					proceed_to_pay();
				}
				else{
					AlertsBoxFactory.showAlert(Utils.CCAvenue_Message, ChangePackage.this);
				}

			}
		});

		pg_dialog.show();
		pg_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		pg_dialog.getWindow().setLayout((width/2)+(width/2), LayoutParams.WRAP_CONTENT);
	}

	public void proceed_to_pay(){
		Intent i=null;

		if(Utils.is_CCAvenue)
			i = new Intent(ChangePackage.this,MakeMyPayments_CCAvenue.class);
		/*else
			i = new Intent(ChangePackage.this,MakeMyPayment_Atom.class);*/

		i.putExtra("PackageName", spinnerList.getSelectedItem().toString());
		i.putExtra("PackageAmount", price.getText().toString());
		i.putExtra("PackageValidity", validity.getText().toString());
		i.putExtra("updateFrom", updateFrom);
		i.putExtra("ServiceTax", servicetax.getText().toString());
		i.putExtra("datafrom", "changepack");
		i.putExtra("ClassName", ChangePackage.this.getClass().getSimpleName());
		i.putExtra("addtional_amount", additionalAmount);
        i.putExtra("outstandingamount",outstandingAmount);
		startActivity(i);
		BaseApplication.getEventBus().post(new FinishEvent("RenewPackage"));
		//RenewPackage.this.finish();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void show_payment_options(){
		SharedPreferences sharedPreferences1 = getApplicationContext().getSharedPreferences(getString(R.string.shared_preferences_renewal), 0);

		int Atom=sharedPreferences1.getInt("Atom", 0);
		int	CCAvenue=sharedPreferences1.getInt("CCAvenue", 0);
		int	PayUMoney=sharedPreferences1.getInt("PAYUMONEY", 0);


		Utils.log("CCAvenue", ":"+CCAvenue);
		Utils.log("Atom", ":"+Atom);
		Utils.log("PayUMoney", ":"+PayUMoney);

		/*if(CCAvenue>0&&Atom>0){
			show_pg_dialog();
		}
		else if(CCAvenue>0){
			Utils.is_CCAvenue=true;
			proceed_to_pay();
		}
		else if(Atom>0){
			Utils.is_CCAvenue=false;
			proceed_to_pay();
		}else if(PayUMoney>0){
			proceed_to_pay();
		}
		else{
			AlertsBoxFactory.showAlert(Utils.Atom_Message, ChangePackage.this);
		}*/

		if(CCAvenue>0 && PayUMoney>0){
			show_pg_dialog();
		}else if(CCAvenue>0){
			Utils.is_CCAvenue=true;
			Utils.is_PayUmoney=false;
			proceed_to_pay();
		}else if(PayUMoney>0){
			Utils.is_CCAvenue=false;
			Utils.is_PayUmoney=true;
			proceed_to_pay();
		}else{
			AlertsBoxFactory.showAlert(Utils.Atom_Message, ChangePackage.this);
		}
	}
}





