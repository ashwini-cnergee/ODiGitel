package com.cnergee.mypage;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.fragments.LaunchComplaintFragment;
import com.cnergee.fragments.Resolve_Yourself_Fragment;
import com.cnergee.mypage.SOAP.ResetPasswordCaller;
import com.cnergee.mypage.SOAP.SelfResolutionSOAP;
import com.cnergee.mypage.caller.InsertComplaintCaller;
import com.cnergee.mypage.caller.LogOutCaller;
import com.cnergee.mypage.caller.ReleaseMacCaller;
import com.cnergee.mypage.obj.Answer;
import com.cnergee.mypage.obj.ComplaintCategoryList;
import com.cnergee.mypage.obj.ComplaintObj;
import com.cnergee.mypage.obj.Question;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import com.special.ResideMenu.ResideMenu;
import com.squareup.otto.Subscribe;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cnergee.krishna.ookla.odigitel.R;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class SelfResolution  extends BaseActivity {

	public static  Context context;
	public static String rslt = "";
	public static String statusResponse="";
	Utils utils = new Utils();
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;
	Button btnreset ,ed_comments,btn_existing_machine;
	EditText txt,ed_comentss;
	TextView textWeb;
	public static String Memberid ;
	private String sharedPreferences_name;
	private final static String  TAG="SelfResolution";
	String Resolution="";
	//ProgressDialog mainDialog;

	ProgressHUD mProgressHUD ;
	Button btnOKK, btnCancell;
	//Spinner complSpinner;
	ArrayList<String>ComplaintId = new ArrayList<String>();
	ArrayList<String>ComplaintName = new ArrayList<String>();
	private InsertComplaintWebService InsertComplaintWebService = null;
	public PopupWindow popdisplay;
	public String memberid;
	public static String responseMsg ="";
	public static String rsltLogOut = "";
	public static String statusResponseForPwd="We are unable to process your request. \n Please try again later";
	public static String statusResponseForMac="We are unable to process your request. \n Please try again later";
	public static String statusResponseForLogOut="We are unable to process your request. \n Please try again later";
	//Button btnOKK , btnCancell; 
	ArrayList<ComplaintCategoryList> alComplaintCategoryLists;
	int position;
	String Comp_Id="";
	String ClassName="";
	Fragment frag_resolve_problem,frag_launch_complaint;
	FragmentTransaction ft;
	Resolve_Yourself_Fragment resolve_Yourself_Fragment= new Resolve_Yourself_Fragment();
	LaunchComplaintFragment launchComplaintFragment= new LaunchComplaintFragment();
	public int not_online_count=0;
	QuestionFragment questionFragment= new QuestionFragment();;
	public boolean is_dialog_frag_called=false;
	LinkedHashMap<String, String> hash_Selected_Answer= new LinkedHashMap<String, String>();
	ArrayList< Integer>alEditTextId=new ArrayList<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);

		setContentView(R.layout.self_resolution);

		linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		textWeb = (TextView)findViewById(R.id.txt_head2);
		btnreset = (Button)findViewById(R.id.btn_rlse);
		btn_existing_machine= (Button)findViewById(R.id.btn_mac);
		ImageView	ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer);

		btn_existing_machine.setVisibility(View.GONE);
		ivMenuDrawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
			}
		});

		not_online_count=0;

		btnreset.setVisibility(View.GONE);
		Intent i = getIntent();
		//ClassName=i.getStringExtra("ClassName");
		btn_existing_machine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.ll_layout, launchComplaintFragment);
				ft.addToBackStack(null);
				ft.commit();*/
			}
		});

		alComplaintCategoryLists =i.getParcelableArrayListExtra("complaintcategorylist");
		if(alComplaintCategoryLists !=null){

			for(int j=0; j< alComplaintCategoryLists.size(); j++ )
			{
				//ComplaintName.add(alComplaintCategoryLists.get(j).getComnplaintName());
				//ComplaintId.add(alComplaintCategoryLists.get(j).getComplaintId());
				if(alComplaintCategoryLists.get(j).getComnplaintName().equalsIgnoreCase("No Connectivity")){
					Comp_Id=alComplaintCategoryLists.get(j).getComplaintId();
				}

			}

			for ( int i1 =0 ;i1< alComplaintCategoryLists.size();i1++){
				if(alComplaintCategoryLists.contains("No Connectivity")){

					position = i1;

				}

			}
		}
		//Utils.log("Complaint Catgory","size"+alComplaintCategoryLists.size());






		btnreset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.log("in OnClick lI", "executed 68" + Resolution);
				if(Resolution.equalsIgnoreCase("PayNow")){
					Utils.log("in Renew", "70" + "executed");
					Utils.pg_sms_request=false;
					Utils.pg_sms_uniqueid="";
					SelfResolution.this.finish();
					Intent i = new Intent(SelfResolution.this,RenewPackage.class);
					i.putExtra("renew", "Self");
					startActivity(i);
					//BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}

				if(Resolution.equalsIgnoreCase("Password")){

					Utils.log("in Password", "76");
					new  ResetPasswordWebService().execute();
				}
				if(Resolution.equalsIgnoreCase("Logoff")){

					new LogOutWebService().execute();
				}
				if(Resolution.equalsIgnoreCase("Mac")){

					new ReleaseMacWebService().execute();

				}
				if(Resolution.equalsIgnoreCase("Compalint")){
					ComplaintPopup();

				}
				if(Resolution.equalsIgnoreCase("Refresh")){



					new	GetSelfResolutionWebService().execute();


					//	add_Resolve_ProblemFragment();

				}
			}


		});





		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SelfResolution.this.finish();
				Intent i = new Intent(SelfResolution.this,IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("Complaints"));
			}
		});

		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SelfResolution.this.finish();
				Intent i = new Intent(SelfResolution.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("Complaints"));
				BaseApplication.getEventBus().post(new FinishEvent("IONHome"));
			}
		});

		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SelfResolution.this.finish();
				Intent i = new Intent(SelfResolution.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("Complaints"));
				BaseApplication.getEventBus().post(new FinishEvent("IONHome"));
			}
		});

		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);

		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SelfResolution.this.finish();
				Intent i = new Intent(SelfResolution.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("Complaints"));
				BaseApplication.getEventBus().post(new FinishEvent("IONHome"));
			}
		});

		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(sharedPreferences_name, 0);


		utils.setSharedPreferences(sharedPreferences);
		Utils.log("line no 87", " Web Services Executed");

		LocalBroadcastManager.getInstance(this).registerReceiver(showQuestions,
				new IntentFilter("show_question_dialog"));

		Memberid = (utils.getMemberId());
		new GetSelfResolutionWebService().execute();

		BaseApplication.getEventBus().register(this);
	}

	@Subscribe
	public void	onFinishEvent(FinishEvent event){
		if(SelfResolution.this.getClass().getSimpleName().equalsIgnoreCase(event.getMessage())){
			SelfResolution.this.finish();
		}

	}

	protected void onResume() {
		super.onResume();

	};


	BroadcastReceiver showQuestions= new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Utils.log("Question Dialog", "Called");

			hash_Selected_Answer.clear();
			if(is_dialog_frag_called){
				questionFragment.show(getSupportFragmentManager(), "Dialog");
			}
			else{
				questionFragment=new QuestionFragment();
				questionFragment.show(getSupportFragmentManager(), "Dialog");
			}


		}
	};

	private void ComplaintPopup() {

		try{

			LayoutInflater inflater =(LayoutInflater) SelfResolution.this.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.self_resl_complaint, null);

			btnCancell = (Button)layout.findViewById(R.id.btn_cancell);
			ed_comentss =(EditText)layout.findViewById(R.id.commentss);
			btnOKK =(Button)layout.findViewById(R.id.btn_okk);

			//	complSpinner =(Spinner)layout.findViewById(R.id.spinnerListt);



			Utils.log("in Compl Category", "web services Executed");
			ArrayAdapter<String> ComplainArray = new ArrayAdapter<String>(SelfResolution.this, android.R.layout.simple_spinner_item,ComplaintName);
			//	complSpinner.setAdapter(ComplainArray);


			Utils.log("in Compl spinner add", "web spinner web  Executed");
			popdisplay = new PopupWindow(layout,LayoutParams.MATCH_PARENT-22,(LayoutParams.WRAP_CONTENT));

			popdisplay.showAtLocation(layout, Gravity.CENTER, 0, 0);

			popdisplay.setFocusable(true);
			popdisplay.update();

			btnCancell.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					popdisplay.dismiss();

				}
			});

			btnOKK.setOnClickListener(new OnClickListener() {



				@Override
				public void onClick(View v) {
					Utils.log("On Click ", "OK Button Executed");
					if(utils.isOnline(SelfResolution.this)){
						Utils.log("on Ok Click", "Button Executed");
						new InsertComplaintWebService().execute();
					}
				}


			});


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private class GetSelfResolutionWebService extends AsyncTask<String , Void ,Void> implements OnCancelListener{


		SelfResolutionSOAP getSelfResolutionResult;
		String SelfResult =" ";
		String response = " ";

		protected void onPreExecute() {
			Utils.log("Aync Task 157", "Executed On Pre");
			// TODO Auto-generated method stub

			mProgressHUD = ProgressHUD.show(SelfResolution.this, getString(R.string.self_please_wait_label), true, false, this);

		}


		@Override
		protected void onPostExecute(Void result) {

			mProgressHUD.dismiss();

			try{
				Utils.log(TAG + "Result :", "" + SelfResult);
				Utils.log(TAG + "Response :", "" + response);
				if(SelfResult.length()>0){
					if(SelfResult.equalsIgnoreCase("OK")){
						if(response.length()>0){
							//String response = "Wrong password entered#Password";
							if(response.contains("#")){
								//if(response.contains("#")){
								String arr[] = response.split("#");
								Utils.log(TAG + "Response :", "" + arr[0]);
								Utils.log(TAG + "Response :", "" + arr[1]); //Renew
								String a = arr[1].trim();

								btnreset.setVisibility(View.VISIBLE);
								if(a.equalsIgnoreCase("PayNow")){
									not_online_count=0;
									Resolution=a;
									textWeb.setText(Html.fromHtml(arr[0]));
									btnreset.setBackgroundColor(getResources().getColor(R.color.self_blue));
									textWeb.setTextColor(getResources().getColor(R.color.self_blue));
									btnreset.setText("PayNow");

								}

								if(a.equalsIgnoreCase("Password")){
									not_online_count=0;
									Resolution=a;
									textWeb.setText(Html.fromHtml(arr[0]));
									btnreset.setBackgroundColor(getResources().getColor(R.color.self_red));
									textWeb.setTextColor(getResources().getColor(R.color.self_red));
									btnreset.setText("Resolve");
								}

								if(a.equalsIgnoreCase("Mac")){
									not_online_count=0;
									Resolution=a;
									textWeb.setText(Html.fromHtml(arr[0]));
									btnreset.setBackgroundColor(getResources().getColor(R.color.self_orange));
									textWeb.setTextColor(getResources().getColor(R.color.self_orange));
									btnreset.setText(getString(R.string.new_mac));
									btn_existing_machine.setVisibility(View.VISIBLE);
									btn_existing_machine.setText(getString(R.string.exist_mac));
								}
								if(a.equalsIgnoreCase("LogOff")){
									not_online_count=0;
									Resolution=a;
									textWeb.setText(arr[0]);
									btnreset.setBackgroundColor(getResources().getColor(R.color.self_green));
									textWeb.setTextColor(getResources().getColor(R.color.self_green));

									btnreset.setText("LogOff ");

								}
								if(a.equalsIgnoreCase("Compalint")){
									not_online_count=0;
									Resolution=a;
									textWeb.setText(Html.fromHtml(arr[0]));
									btnreset.setBackgroundColor(getResources().getColor(R.color.self_orange));
									textWeb.setTextColor(getResources().getColor(R.color.self_orange));
									btnreset.setText("Launch Complaint");
								}
								if(a.equalsIgnoreCase("Refresh")){
									not_online_count++;

									if(not_online_count>2){
										Resolution="Compalint";
										textWeb.setText(Html.fromHtml("Connectivity issue."));
										btnreset.setBackgroundColor(getResources().getColor(R.color.self_orange));
										textWeb.setTextColor(getResources().getColor(R.color.self_orange));
										btnreset.setText("Launch Complaint");
										ComplaintPopup();
									}
									else{
										Resolution=a;
										textWeb.setText(Html.fromHtml(arr[0]));
										btnreset.setBackgroundColor(getResources().getColor(R.color.self_blue));
										textWeb.setTextColor(getResources().getColor(R.color.self_blue));
										btnreset.setText("Refresh");
									}
									//add_Resolve_ProblemFragment();
								}
							}else{

								btnreset.setVisibility(View.GONE);
								textWeb.setText(response);
								textWeb.setTextColor(getResources().getColor(R.color.self_red));

							}
						}

					}
					else{
						//add_Resolve_ProblemFragment();
						//add_LaunchComplaint_Fragment();
					}

				}
				else{
					Toast.makeText(getApplicationContext(), (String)response,
							Toast.LENGTH_LONG).show();
					//	add_Resolve_ProblemFragment();
					//add_LaunchComplaint_Fragment();
				}

			}catch(Exception e){
				e.printStackTrace();
				//add_Resolve_ProblemFragment();
				//add_LaunchComplaint_Fragment();
			}

		}

		@Override
		protected Void doInBackground(String... params) {

			try{

				Utils.log("Do in Ba 108", " Web Services Executed");
				getSelfResolutionResult = new SelfResolutionSOAP(getString(R.string.WSDL_TARGET_NAMESPACE),getString(R.string.SOAP_URL),getString(R.string.METHOD_GET_SELF_RESOLUTION));

				SelfResult = getSelfResolutionResult.GetSelfResolutionResult(Memberid);

				response = getSelfResolutionResult.getjsonResponse();

			}
			catch(SocketTimeoutException e){
				Utils.log("Timeout", "Exception" + e);
			}catch(Exception e){
				Utils.log("Excpetion", "is:" + e);
				e.printStackTrace();
			}


			return null;
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
	
	
	/*<***********************************Release MAc Webservices Starts From here************************************>
	*/

	private class ReleaseMacWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{
		//private ProgressDialog Dialog1 = new ProgressDialog(
		//	Complaints.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(SelfResolution.this, getString(R.string.app_please_wait_label), true, true, this);

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
								R.string.METHOD_RESET_MAC),false);

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
			Utils.log("Post Execute called", "yes");
			mProgressHUD.dismiss();
			AlertsBoxFactory.showAlert(statusResponseForMac, SelfResolution.this);
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
	
	/*<******************************************Self Resolution Web Services Ends here ******************************>*/

	//*******************************ResetPassword Web Service*********starts here**************************	

	private class ResetPasswordWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{


		@Override
		protected void onPreExecute() {
			Utils.log("Aync Task 315", "Executed");
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(SelfResolution.this, getString(R.string.app_please_wait_label), true, true, this);

		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				Utils.log("Aync Task 326", "Executed");
				ResetPasswordCaller caller = new ResetPasswordCaller(getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_RESET_PASSWORD),"self");

				caller.setMemberId(utils.getMemberId());
				caller.setMemberLoginId(utils.getMemberLoginID());

				caller.join();
				caller.start();
				rslt = "START";

				Utils.log("Password send", "343");
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
			AlertsBoxFactory.showAlert(statusResponseForPwd, SelfResolution.this);
			//Utils.log("Response for status",""+statusResponse);
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

	//*******************************ResetPassword Web Service*********ends here**************************	


	//*******************************Logout Web Service*********starts here**************************
	private class LogOutWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
		//private ProgressDialog Dialog1 = new ProgressDialog(
		//	Complaints.this);
		@Override
		protected void onPreExecute() {
			Utils.log("in PreExecute Asynctask", "375");// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(SelfResolution.this, getString(R.string.app_please_wait_label), true, true, this);

		}
		@Override
		protected Void doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {

				Utils.log("in do in Bckgrnd Asynct", "388 Aynctask Executed");
				LogOutCaller caller = new LogOutCaller(getApplicationContext()
						.getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_LOG_OUT_BROWSER),false);

				caller.setMemberId(utils.getMemberId());
				caller.setMemberLoginId(utils.getMemberLoginID());
				Utils.log("399 log off", "logOff Executed");
				caller.join();
				caller.start();
				rsltLogOut = "START";

				while (rsltLogOut == "START") {
					try {
						Thread.sleep(10);
						Utils.log("407 log off Start", "logOff Executed");
					}catch (Exception ex) {
						ex.printStackTrace();
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
			Utils.log("in PostExecute Asynctask", "421");
			Utils.log("Log outPost Execute called", "yes");
			mProgressHUD.dismiss();

			AlertsBoxFactory.showAlert(statusResponseForLogOut, SelfResolution.this);
			Utils.log("on Post out ", "executed yes");
			//Utils.log("Response for status",""+statusResponse);
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


	private class InsertComplaintWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {



		//private ProgressDialog Dialog = new ProgressDialog(SelfResolution.this);
		ComplaintObj complaintobj = new ComplaintObj();

		protected void onPreExecute() {
			Utils.log("on insert Complaint ", "WebAsyncTask Executed");
			mProgressHUD = ProgressHUD.show(SelfResolution.this, getString(R.string.app_please_wait_label), true, false, this);
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
				Toast.makeText(SelfResolution.this,responseMsg , Toast.LENGTH_LONG).show();
				popdisplay.dismiss();

			} else {
				//	AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {

				Utils.log("do in Complint Executed", "Cpmlnt Executed");
				Utils.log("do bck line 651", " Executed on line 655");
				//Log.i("ComplaintId",""+ComplaintId.get(spinnerList.getSelectedItemPosition()));
				//complaintobj.setMemberComplaintNo(txtcomplaintno.getText().toString());
				Utils.log("ComplaintId ", " Size:" + ComplaintId.size());
				Utils.log("Set ComplaintId ", " :" + Comp_Id);
				Utils.log("Set MemberId ", " :" + Memberid);
				//Utils.log("Set Comments ", " :"+ed_comentss.getText().toString());
				complaintobj.setComplaintId(Comp_Id);
				complaintobj.setMemberId(Memberid);
				//complaintobj.setMessage(ed_comentss.getText().toString());
				Utils.log("ComplaintId", "" + Comp_Id);

				InsertComplaintCaller caller = new InsertComplaintCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
						.getResources().getString(
								R.string.METHOD_INSERT_COMPLAINTS),"self");


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
				Utils.log("Error SelfResolution", " 694" + e);
			}
			return null;
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


	public void add_Resolve_ProblemFragment(){
		ft  = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.ll_layout, resolve_Yourself_Fragment, "fragmentright");
		ft.commit();
	}

	public void add_LaunchComplaint_Fragment(){
		ft  = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.ll_layout, launchComplaintFragment, "fragmentright");
		ft.commit();
	}

	public void replace_Resolve_ProblemFragment(){
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.ll_layout, resolve_Yourself_Fragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	public void replace_LaunchComplaint_Fragment(){
		ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.ll_layout, launchComplaintFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

	//*******************************LogOut Web Service*********ends here**************************	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Utils.log("backPress", "Called");
		if(questionFragment!=null){
			Utils.log("finish", "Dialog fragment 1");
			if(questionFragment.isVisible()){

				Utils.log("finish", "Dialog fragment 2");
				questionFragment.dismiss();

			}
			else{
				this.finish();
				overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		}
		else{
			this.finish();
			overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);}
	/*Intent i = new Intent(SelfResolution.this,Complaints.class);
	startActivity(i);
	overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);*/
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Utils.log("Self Resolution", "finished");
		LocalBroadcastManager.getInstance(this).unregisterReceiver(showQuestions);
		Resolve_Yourself_Fragment.alQuestion.clear();
		Resolve_Yourself_Fragment.alAnswer.clear();
	}

	@SuppressLint("ValidFragment")
	public class QuestionFragment extends DialogFragment implements TextWatcher {
		View view;
		ArrayList<Question> alLocalQuestions=new ArrayList<Question>();
		ArrayList<Answer> alLocalAnswers=new ArrayList<Answer>();
		LinearLayout layout;
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			view=null;

			//alLocalQuestions=Resolve_Yourself_Fragment.alQuestion;
			//alLocalAnswers=Resolve_Yourself_Fragment.alAnswer;

			alLocalQuestions.addAll(Resolve_Yourself_Fragment.alQuestion);
			alLocalAnswers.addAll(Resolve_Yourself_Fragment.alAnswer);
		}

		@Override
		public View onCreateView(LayoutInflater inflater,
								 ViewGroup container,
								 Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			view=inflater.inflate(R.layout.fragment_question, container	, false);
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			layout = (LinearLayout)view.findViewById(R.id.ll_top_form);

			Utils.log("LocalQuestions", "size:" + alLocalQuestions.size());
			int width,height;
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
			getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			getQuestionView();
			
				
			
			
			/*final RadioGroup group = new RadioGroup(getActivity());
			final RadioButton button1 = new RadioButton(getActivity());
			button1.setId(1); // this id can be generated as you like.
			group.addView(button1,
			    new RadioGroup.LayoutParams(
			        RadioGroup.LayoutParams.WRAP_CONTENT,    
			        RadioGroup.LayoutParams.WRAP_CONTENT));
			final RadioButton button2 = new RadioButton(getActivity());
			button1.setId(2); // this id can be generated as you like.
			button2.setChecked(true);
			group.addView(button2,
			    new RadioGroup.LayoutParams(
			        RadioGroup.LayoutParams.WRAP_CONTENT,    
			        RadioGroup.LayoutParams.WRAP_CONTENT));
			layout.addView(group,
			    new LinearLayout.LayoutParams(
			        LinearLayout.LayoutParams.MATCH_PARENT,    
			        LinearLayout.LayoutParams.WRAP_CONTENT));*/

			return view;
		}



		public void getQuestionView(){
			if(alLocalQuestions!=null){
				for(int i=0;i<alLocalQuestions.size();i++){
					Question question=alLocalQuestions.get(i);
					if(question!=null){
						if(question.getQuestion_Text().length()>0){
							LinearLayout ll_question= new LinearLayout(getActivity());

							LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

							ll_question.setLayoutParams(layoutParams);
							ll_question.setBackgroundResource(R.drawable.menu_dropdown_panel_nms);
							ll_question.setPadding(10, 20, 20, 10);
							layout.addView(ll_question);
							ll_question.setOrientation(LinearLayout.VERTICAL);
							TextView tv= new TextView(getActivity());
							tv.setPadding(10, 20, 20, 10);
							tv.setId(Integer.valueOf(question.getQuestion_Id()));
							tv.setText(question.getQuestion_Text());
							ll_question.addView(tv);


							if(question.getAnswerType().length()>0){

								if(question.getAnswerType().equalsIgnoreCase("RadioButtonList")){
									final RadioGroup group = new RadioGroup(getActivity());
									if(alLocalAnswers!=null){
										if(alLocalAnswers.size()>0){
											for(int j=0;j<alLocalAnswers.size();j++){
												Answer answer= alLocalAnswers.get(j);
												if(answer.getAns_Ques_Id().equalsIgnoreCase(question.getQuestion_Id())){

													final RadioButton button1 = new RadioButton(getActivity());
													button1.setId(Integer.valueOf(answer.getAns_Id())); // this id can be generated as you like.
													button1.setText(answer.getAns_Text());
													group.addView(button1,
															new RadioGroup.LayoutParams(
																	RadioGroup.LayoutParams.WRAP_CONTENT,
																	RadioGroup.LayoutParams.WRAP_CONTENT));
													button1.setOnClickListener(RadioAnswerClickListner);

												}
											}

											ll_question.addView(group);
										}
									}

								}


								if(question.getAnswerType().equalsIgnoreCase("TextBox")){

									if(alLocalAnswers!=null){
										if(alLocalAnswers.size()>0){
											for(int j=0;j<alLocalAnswers.size();j++){
												Answer answer= alLocalAnswers.get(j);
												if(answer.getAns_Ques_Id().equalsIgnoreCase(question.getQuestion_Id())){

													EditText etAnswer=new EditText(getActivity());
													etAnswer.setId(Integer.valueOf(answer.getAns_Id()));
													ll_question.addView(etAnswer);
													alEditTextId.add(Integer.valueOf(answer.getAns_Id()));
													etAnswer.addTextChangedListener(this);
												}
											}


										}
									}

								}

								if(question.getAnswerType().equalsIgnoreCase("CheckBoxList")){

									if(alLocalAnswers!=null){
										if(alLocalAnswers.size()>0){
											for(int j=0;j<alLocalAnswers.size();j++){
												Answer answer= alLocalAnswers.get(j);
												if(answer.getAns_Ques_Id().equalsIgnoreCase(question.getQuestion_Id())){

													final CheckBox cb1 = new CheckBox(getActivity());
													cb1.setId(Integer.valueOf(answer.getAns_Id())); // this id can be generated as you like.
													cb1.setText(answer.getAns_Text());
													ll_question.addView(cb1);
													cb1.setOnClickListener(CheckAnswerClickListner);

												}
											}


										}
									}

								}

							}

							if(question.getSolution_Text().length()>0){

								TextView tv_ques_soluiotn= new TextView(getActivity());
								tv_ques_soluiotn.setText(Html.fromHtml("<u>Click Here to view Solution</u>"));
								tv_ques_soluiotn.setPadding(10, 20, 20, 10);
								tv_ques_soluiotn.setId(Integer.valueOf(100+question.getQuestion_Id()));
								tv_ques_soluiotn.setOnClickListener(QuestionClickListener);
								ll_question.addView(tv_ques_soluiotn);
							}


						}
					}

				}


			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new Dialog(getActivity(), getTheme()){
				@Override
				public void onBackPressed() {
					//do your stuff
					alLocalQuestions.clear();
					alLocalAnswers.clear();
					//questionFragment.getDialog().dismiss();

					AlertBeforeDismiss_QuestionDialog();
					Utils.log("Dialog", "Dismiss:" + Resolve_Yourself_Fragment.alQuestion.size());
				}
			};
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();

		}


		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();

		}

		public void finishFrag(){
			getActivity().finish();
		}

		public OnClickListener RadioAnswerClickListner= new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<alLocalAnswers.size();i++){
					Answer ans=alLocalAnswers.get(i);
					if(v.getId()==Integer.valueOf(ans.getAns_Id())){
						Toast.makeText(getActivity(), ""+v.getId(), Toast.LENGTH_LONG).show();
						hash_Selected_Answer.put(ans.getAns_Ques_Id(), ans.getAns_Id());
					}
				}
			}
		};

		public OnClickListener CheckAnswerClickListner= new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//	Toast.makeText(getActivity(), ""+v.getId(), Toast.LENGTH_LONG).show();
				for(int i=0;i<alLocalAnswers.size();i++){
					Answer ans=alLocalAnswers.get(i);
					if(v.getId()==Integer.valueOf(ans.getAns_Id())){

						Toast.makeText(getActivity(), ""+v.getId(), Toast.LENGTH_LONG).show();
						String ansIds="";
						String quesid=ans.getAns_Ques_Id();
						CheckBox c=(CheckBox)v;
						if(hash_Selected_Answer.containsKey(quesid)){
							ansIds=hash_Selected_Answer.get(quesid);
						}


						if(!c.isChecked()){
							Utils.log("Chekced", "id:" + v.getId());
							if(ansIds.length()>0){
								String[] arr_ansId=ansIds.split(",");
								String local_ansIds="";
								for(int i1=0;i1<arr_ansId.length;i1++){

									if(arr_ansId[i1].equalsIgnoreCase(ans.getAns_Id())){

									}
									else{
										if(local_ansIds.length()>0){
											local_ansIds+=","+arr_ansId[i1];
										}
										else{
											local_ansIds=arr_ansId[i1];
										}
									}
								}
								ansIds=local_ansIds;
							}
							else{
								ansIds="";
							}

							Utils.log("Unchecked", "Answer:" + ansIds);
						}
						else{

							Utils.log("Chekced", "id:" + v.getId());
							if(ansIds.length()>0){
								ansIds+=","+ans.getAns_Id();
							}
							else{
								ansIds=ans.getAns_Id();
							}

							Utils.log("Checked", "Answer:" + ansIds);
						}



						hash_Selected_Answer.put(ans.getAns_Ques_Id(), ansIds);
					}
				}
			}
		};



		public OnClickListener QuestionClickListener= new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.log("Question ClickListener", " clicked");
				for(int i=0;i<alLocalQuestions.size();i++){
					Question ques=alLocalQuestions.get(i);
					if(v.getId()==Integer.valueOf(100+(ques.getQuestion_Id()))){
						Toast.makeText(getActivity(), ""+ques.getSolution_Text(), Toast.LENGTH_LONG).show();
						Dialog dialog = new Dialog(getActivity());
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						//tell the Dialog to use the dialog.xml as it's layout description
						dialog.setContentView(R.layout.solution_dialog);
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


						TextView tv_solution=(TextView)dialog.findViewById(R.id.tv_Solution_Text);
						ImageViewTouch iv_solution=(ImageViewTouch)dialog.findViewById(R.id.image);
						LinearLayout ll_image=(LinearLayout)dialog.findViewById(R.id.ll_solution_image);
						tv_solution.setText(ques.getSolution_Text());
						String img_bitmap=ques.getSolution_Image();
						if(img_bitmap!=null&&img_bitmap.length()>0){
							ll_image.setVisibility(View.VISIBLE);
							byte[] image = Base64.decode(img_bitmap, Base64.DEFAULT);
							Bitmap bmmp = BitmapFactory.decodeByteArray(image,0,image.length);
							iv_solution.setImageBitmap(bmmp);
						}
						else{
							ll_image.setVisibility(View.GONE);
						}
						dialog.show();
						//(width/2)+((width/2)/2)
						//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
						dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
						//dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);
						dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					}
					else{
						Utils.log("Question ClickListener", " clicked is" + Integer.valueOf(100 + (ques.getQuestion_Id())));
					}
				}
			}
		};
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			for(int i=0;i<alLocalAnswers.size();i++){
				String ques_id="",ans_id="";
				ans_id=	alLocalAnswers.get(i).getAns_Id();
				ques_id=alLocalAnswers.get(i).getAns_Ques_Id();
				for(int j=0;j<alEditTextId.size();j++){
					if(Integer.valueOf(ans_id)==alEditTextId.get(j)){
						EditText qtyView = (EditText) view.findViewById(alEditTextId.get(j));
						if(qtyView!=null){
							if(s.length()>0)
								hash_Selected_Answer.put(ques_id, s.toString());
							else
								hash_Selected_Answer.remove(ques_id);
						}
						else{

						}
					}
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
									  int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
								  int count) {
			// TODO Auto-generated method stub

		}

	}

	public void AlertBeforeDismiss_QuestionDialog(){
		final Dialog dialog = new Dialog(SelfResolution.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.dialog_question_dialog);
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

		TextView tvMessage=(TextView)dialog.findViewById(R.id.tvMessage);
		Button btnLaunchComp=(Button)dialog.findViewById(R.id.btn_dialog_launch_comp);
		Button btnResolve=(Button)dialog.findViewById(R.id.btn_dialog_resolve);
		tvMessage.setText("Did your Problem resolved?");
		btnLaunchComp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				questionFragment.getDialog().dismiss();
				replace_LaunchComplaint_Fragment();
			}
		});
		btnResolve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				questionFragment.getDialog().dismiss();
				for (Entry<String, String> entry : hash_Selected_Answer.entrySet()) {
					// System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				}
			}
		});

		dialog.show();
		//(width/2)+((width/2)/2)
		//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

}