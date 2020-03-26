package com.cnergee.mypage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citruspay.citruspaylib.CitrusSSLLibrary;
import com.citruspay.citruspaylib.model.Address;
import com.citruspay.citruspaylib.model.Card;
import com.citruspay.citruspaylib.model.Customer;
import com.citruspay.citruspaylib.model.ExtraParams;
import com.citruspay.citruspaylib.service.CitrusGetWebClientJSResponse;
import com.citruspay.citruspaylib.utils.CitrusParams;
import com.citruspay.citruspaylib.utils.Constants;
import com.cnergee.mypage.SOAP.GetPhoneDetailsSOAP;
import com.cnergee.mypage.caller.AfterInsertPaymentsCaller;
import com.cnergee.mypage.caller.BeforePaymentInsertCaller;
import com.cnergee.mypage.caller.CitrusConstantCaller;
import com.cnergee.mypage.caller.CitrusSignatureCaller;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.caller.PaymentGatewayCaller;
import com.cnergee.mypage.caller.RenewalCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.CitrusConstantsObj;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PaymentGatewayObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapFault;

import java.math.BigInteger;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import all.interface_.IError;
import cnergee.krishna.ookla.odigitel.R;

public class MakeMyPayments extends BaseActivity {
	//private static final String retrunURL = "https://sandbox.citruspay.com/demo/jsp/response_android.jsp";
	
	public static String dynamic_retrunURL = "";
	private boolean isFinish = false;
	public static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	public long memberid;
	public static String responseMsg = "";
	public static String getTransactionResponse = "";
	String ServiceTax, UpdateFrom;
	LinearLayout linnhome, linnprofile, linnnotification, linnhelp;

	TextView txtloginid, txtemailid, txtcontactno, txtnewpackagename,
			txtnewamount, txtnewvalidity;
	CheckBox privacy, terms;
	private String sharedPreferences_name;
	public static Map<String, MemberDetailsObj> mapMemberDetails;
	public static Map<String, PaymentGatewayObj> mappaymentgateways;
	public static Map<String, CitrusConstantsObj> mapcitrusconstants;
	private GetMemberDetailWebService getMemberDetailWebService = null;
	private PaymentGateWayDetails getpaymentgatewaysdetails = null;
	private GetCitrusCOnstantWebService GetCitrusCOnstantWebService = null;
	private GetCitrusSignature citrussignature = null;
	private GetSignatureFromMerchantServer getsignaturefrommerchant = null;
	boolean isLogout = false;
	CitrusSSLLibrary citrusSSLLibrary = new CitrusSSLLibrary();
	List<NameValuePair> nameValuePairs;
	static String generatedHMAC, currencyValue;
	String authIdCode="", TxId="", TxStatus="", pgTxnNo="", issuerRefNo="", TxMsg="";
	public static boolean Changepack;

	private static final int ACTION_WEB_VIEW = 111;
	private String customername;
	String TrackId;
	Button btnnb;
	Address address;
	ExtraParams extraParam;
	Card cards;
	Customer customer;
	public boolean isDetailShow = false;
	public boolean is_allow = true;
	// String address;
	public static String adjTrackval = "";
	public static String HMACSignature = "";
	private Map<String, String> response;
	private LayoutParams MATCH_WRAP_LAYOUT = new LayoutParams(
			LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

	String GetSignature;
	private boolean isDataReceived;
	private LinearLayout responseLayout;
	private ScrollView payNowView, responseScrollLayout;
	private InsertBeforePayemnt InsertBeforePayemnt = null;
	private InsertAfterPayemnt InsertAfterPayemnt = null;
	private RenewalProcessWebService RenewalProcessWebService = null;
	String discount = "";
	TableLayout tableRowDiscount;
	TextView tvDiscountLabel;
	String ClassName = "";
	AdditionalAmount additionalAmount;
	public boolean is_payemnt_detail = false;
	TextView tvClickDetails;
	LinearLayout ll_addtional_details, llClickDetails;
	String type = "Renew";
	private boolean is_activity_running=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makemypayments);
		iError=(IError) this;
		is_activity_running=false;
		txtloginid = (TextView) findViewById(R.id.txtloginid);
		txtemailid = (TextView) findViewById(R.id.txtemailid);
		txtcontactno = (TextView) findViewById(R.id.txtcontactno);
		txtnewpackagename = (TextView) findViewById(R.id.txtnewpackagename);
		txtnewamount = (TextView) findViewById(R.id.txtnewamount);
		txtnewvalidity = (TextView) findViewById(R.id.txtnewvalidity);
		btnnb = (Button) findViewById(R.id.btnnb);
		ll_addtional_details = (LinearLayout) findViewById(R.id.ll_addtional_details);
		tvClickDetails = (TextView) findViewById(R.id.tvClickDetails);
		citrusSSLLibrary = new CitrusSSLLibrary();
		responseScrollLayout = (ScrollView) findViewById(R.id.responseScrollLayout);
		responseLayout = (LinearLayout) findViewById(R.id.responseLayout);
		payNowView = (ScrollView) findViewById(R.id.payNowLayout);
		privacy = (CheckBox) findViewById(R.id.privacy);
		terms = (CheckBox) findViewById(R.id.terms);
		tableRowDiscount = (TableLayout) findViewById(R.id.tableLayoutDiscount);
		tvDiscountLabel = (TextView) findViewById(R.id.tvDiscountLabel);
		llClickDetails = (LinearLayout) findViewById(R.id.llClickDetails);
		ll_addtional_details = (LinearLayout) findViewById(R.id.ll_addtional_details);
		context = this;
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// Log.i(">>>>>BUndle<<<<<", extra.toString());

		/* Log.i(">>>>>PAckageAmount<<<<",bundle .getString("PackageAmount")); */
		// txtnewamount.setText(bundle .getString("PackageAmount"));
		txtnewpackagename.setText(bundle.getString("PackageName"));
		txtnewvalidity.setText(bundle.getString("PackageValidity"));
		ServiceTax = bundle.getString("ServiceTax");
		UpdateFrom = bundle.getString("updateFrom");
		discount = bundle.getString("discount");
		ClassName = bundle.getString("ClassName");
		additionalAmount = (AdditionalAmount) bundle
				.getSerializable("addtional_amount");

		Utils.log("UpdateFrom", "is:" + UpdateFrom);

		if (bundle.getString("datafrom").equalsIgnoreCase("changepack")) {
			Changepack = true;
			tvDiscountLabel.setVisibility(View.GONE);

		} else {
			Changepack = false;

			Utils.log("Renew", "account");
			tvDiscountLabel.setVisibility(View.VISIBLE);

			/*
			 * if(additionalAmount!=null){
			 * if(additionalAmount.getDiscountPercentage().length()>0){
			 * if(Double.valueOf(additionalAmount.getDiscountPercentage())>0){
			 * tvDiscountLabel
			 * .setText("You have got "+additionalAmount.getDiscountPercentage
			 * ()+"% discount for online payment."); } } else{
			 * tvDiscountLabel.setVisibility(View.GONE); }
			 * 
			 * if(Double.valueOf(additionalAmount.getFinalcharges())>0){
			 * txtnewamount.setText(additionalAmount.getFinalcharges());
			 * 
			 * } if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
			 * is_payemnt_detail=true; }
			 * if(Double.valueOf(additionalAmount.getFineAmount())>0){
			 * is_payemnt_detail=true; }
			 * if(Double.valueOf(additionalAmount.getDiscountAmount())>0){
			 * is_payemnt_detail=true; }
			 * if(Double.valueOf(additionalAmount.getDaysFineAmount())>0){
			 * is_payemnt_detail=true; } if(is_payemnt_detail){
			 * txtnewamount.setText(additionalAmount.getFinalcharges());
			 * llClickDetails.setVisibility(View.VISIBLE); } else{
			 * llClickDetails.setVisibility(View.GONE); } } else{
			 * tvDiscountLabel.setVisibility(View.GONE); }
			 */
		}

		if (additionalAmount != null) {
			if (additionalAmount.getDiscountPercentage().length() > 0) {
				if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
					// tvDiscountLabel.setText("You have got "+additionalAmount.getDiscountPercentage()+"% discount for online payment.");
					tvDiscountLabel.setText("Avail of a "
							+ additionalAmount.getDiscountPercentage()
							+ "% discount by paying online.");
				} else {
					tvDiscountLabel.setVisibility(View.GONE);
				}
			} else {
				tvDiscountLabel.setVisibility(View.GONE);
			}

			if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
				txtnewamount.setText(additionalAmount.getFinalcharges());

			}
			if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
				is_payemnt_detail = true;
			}
			if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
				is_payemnt_detail = true;
			}
			if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
				is_payemnt_detail = true;
			}
			if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
				is_payemnt_detail = true;
			}
			if (is_payemnt_detail) {
				txtnewamount.setText(additionalAmount.getFinalcharges());
				llClickDetails.setVisibility(View.VISIBLE);
			} else {
				llClickDetails.setVisibility(View.GONE);
			}
		} else {
			tvDiscountLabel.setVisibility(View.GONE);
		}
		txtnewamount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (is_payemnt_detail) {
					showPaymentDetailsDialog(additionalAmount);
				}
			}
		});
		llClickDetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (is_payemnt_detail) {
					if (isDetailShow) {
						ll_addtional_details.setVisibility(View.GONE);
						isDetailShow = false;
					} else {
						ll_addtional_details.setVisibility(View.VISIBLE);
						isDetailShow = true;
					}

					showPaymentDetails(additionalAmount);
				} else {
					ll_addtional_details.setVisibility(View.GONE);
				}
			}
		});
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
																	// private
																	// mode

		utils.setSharedPreferences(sharedPreferences);

		memberid = Long.parseLong(utils.getMemberId());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			GetCitrusCOnstantWebService = new GetCitrusCOnstantWebService();

			GetCitrusCOnstantWebService.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, (String) null);

		} else {
			GetCitrusCOnstantWebService = new GetCitrusCOnstantWebService();
			GetCitrusCOnstantWebService.execute((String) null);
		}

		payNowView.setVisibility(View.VISIBLE);
		responseScrollLayout.setVisibility(View.GONE);

		btnnb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try{
				if (Double.parseDouble(txtnewamount.getText().toString()) > 0) {
					if (terms.isChecked() == true
							&& privacy.isChecked() == true) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
							citrussignature = new GetCitrusSignature();
							citrussignature.executeOnExecutor(
									AsyncTask.THREAD_POOL_EXECUTOR,
									(String) null);
						} else {
							citrussignature = new GetCitrusSignature();
							citrussignature.execute((String) null);
						}
					} else {
						Toast.makeText(MakeMyPayments.this,
								"Please accept the terms and condition",
								Toast.LENGTH_LONG).show();
						return;

					}

				} else {
					if(is_activity_running)
					AlertsBoxFactory
							.showAlert(
									"Due to some data mismatch we are unable to process your request\n Please contact admin",
									MakeMyPayments.this);
				}
				}
				catch (Exception e) {
					// TODO: handle exception
					if(is_activity_running)
					AlertsBoxFactory
					.showAlert(
							"Due to some data mismatch we are unable to process your request\n Please contact admin",
							MakeMyPayments.this);
				}

			}
		});

		linnhome = (LinearLayout) findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout) findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout) findViewById(R.id.inn_banner_notification);

		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPayments.this.finish();
				Intent i = new Intent(MakeMyPayments.this, IONHome.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				BaseApplication.getEventBus().post(
						new FinishEvent("PlanCalculatorActivity"));
				BaseApplication.getEventBus().post(
						new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(
						new FinishEvent( ChangePackage.class.getSimpleName()));
				BaseApplication.getEventBus().post(
						new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPayments.this.finish();
				Intent i = new Intent(MakeMyPayments.this, Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				BaseApplication.getEventBus().post(
						new FinishEvent("PlanCalculatorActivity"));
				BaseApplication.getEventBus().post(
						new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(
						new FinishEvent( ChangePackage.class.getSimpleName()));
				BaseApplication.getEventBus().post(
						new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPayments.this.finish();
				Intent i = new Intent(MakeMyPayments.this,
						NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				BaseApplication.getEventBus().post(
						new FinishEvent("PlanCalculatorActivity"));
				BaseApplication.getEventBus().post(
						new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(
						new FinishEvent(Utils.Last_Class_Name));
			}
		});

		linnhelp = (LinearLayout) findViewById(R.id.inn_banner_help);

		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPayments.this.finish();
				Intent i = new Intent(MakeMyPayments.this, HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				BaseApplication.getEventBus().post(
						new FinishEvent("PlanCalculatorActivity"));
				BaseApplication.getEventBus().post(
						new FinishEvent("RenewPackage"));
				BaseApplication.getEventBus().post(
						new FinishEvent( ChangePackage.class.getSimpleName()));
				BaseApplication.getEventBus().post(
						new FinishEvent(Utils.Last_Class_Name));
			}
		});
	}


	private class GetSignatureFromMerchantServer extends
			AsyncTask<Void, Void, String> implements OnCancelListener {

		ProgressHUD mProgressHUD;
		List<NameValuePair> nameValuePairs;

		public GetSignatureFromMerchantServer(List<NameValuePair> nameValuePairs) {
			this.nameValuePairs = nameValuePairs;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			mProgressHUD = ProgressHUD.show(MakeMyPayments.this, "Loading...",
					true, true, this);

			// progressDialog = new ProgressDialog(MakeMyPayments.this);
			// progressDialog.setTitle("Loading...");
			mProgressHUD.setCancelable(false);
			mProgressHUD.setMessage("Authenticating Transaction");
			// progressDialog.show();

			// ashok
			// Log.i(">>>"," ****** START NEW ACTIVITY ******** ");
		}

		@Override
		protected String doInBackground(Void... params) {
			// generatedHMAC=CitrusHttpResponse.communicateToServer(Constants.GENERATE_HMAC_URL,
			// nameValuePairs);
			// generatedHMAC=CitrusHttpResponse.communicateToServer("http://localhost:8080/sample-kit/jsp/hmac_signature.jsp");
			Utils.log("gaurav", "hmac " + generatedHMAC);
			// /ExtraParams.setHMACValue(generatedHMAC);

			// return HMACSignature;
			// return HMACSignature;
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			// ashok
			// Log.i(">>>"," ****** I AM IN POST EXE ******** ");
			// Utils.log("gaurav", "result==" + result);

			Utils.log("Dynamic Return Url","is:"+dynamic_retrunURL);
			Object Data;

			address = new Address();
			address.setAddressStreet1("");
			address.setAddressCity("");
			address.setAddressCountry("");
			address.setAddressState("");
			address.setAddressZip("");

			customer = new Customer();
			customer.setFirstName("");
			customer.setLastName("");

			/* Log.i(">>>>emailid<<<",txtemailid.getText().toString()); */
			customer.setEmail(txtemailid.getText().toString());
			customer.setPhoneNumber(txtcontactno.getText().toString());
			/* Log.i(">>>>PhoneNumber<<<",txtcontactno.getText().toString()); */

			cards = new Card();
			cards.setCardHolderName("");
			cards.setCardNumber("");
			cards.setCardType("");
			cards.setCvvNumber("");
			cards.setExpiryMonth("");
			cards.setExpiryYear("");
			cards.setPaymentMode("NET_BANKING");

			extraParam = new ExtraParams();

			extraParam.setReturnUrl(dynamic_retrunURL);
			// Log.i(">>>>Amount<<<",txtnewamount.getText().toString());
			extraParam.setOrderAmountValue(txtnewamount.getText().toString());
			// Log.i(">>>>Trackid<<<",TrackId);
			extraParam.setMerchantTxnId(TrackId);
			extraParam.setHMACValue(GetSignature);

			extraParam.setCurrency("INR");

			Intent launchIntent = new Intent(MakeMyPayments.this,
					WebViewScreen.class);
			launchIntent.putExtra(CitrusParams.PARAM_CUSTOMER, customer);
			launchIntent.putExtra(CitrusParams.PARAM_ADDRESS, address);
			launchIntent.putExtra(CitrusParams.PARAM_CARD, cards);
			launchIntent.putExtra(CitrusParams.PARAM_EXTRAS, extraParam);

			startActivityForResult(launchIntent, ACTION_WEB_VIEW);
			BaseApplication.getEventBus().post(new FinishEvent(ClassName));
			BaseApplication.getEventBus().post(
					new FinishEvent(Utils.Last_Class_Name));
			BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));
			BaseApplication.getEventBus().post(
					new FinishEvent(Make_my_plan_Activity.class.getSimpleName()));
			BaseApplication.getEventBus().post(
					new FinishEvent( RenewPackage.class.getSimpleName()));
			if(Utils.pg_sms_request)
				BaseApplication.getEventBus().post(new FinishEvent(NotificationListActivity.class.getSimpleName()));
			if(is_activity_running){
			if (mProgressHUD != null && mProgressHUD.isShowing()) {
				mProgressHUD.dismiss();
			}
			}

		}

		@Override
		public void onCancel(DialogInterface dialog) {
			if(is_activity_running)
			mProgressHUD.dismiss();

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isDataReceived) {
			changeTheContentView();
			isDataReceived = false;
		}
		is_activity_running=true;
		/*
		 * ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		 * 
		 * SharedPreferences sharedPreferences = getApplicationContext()
		 * .getSharedPreferences(sharedPreferences_name, 0); // 0 - for private
		 * mode if(sharedPreferences.getString("showLogo",
		 * "0").equalsIgnoreCase("1")) ivLogo.setVisibility(View.VISIBLE); else
		 * ivLogo.setVisibility(View.INVISIBLE);
		 */
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		is_activity_running=false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// System.out.println(">>>>>>ON ACTIVITY RESULT--requestCode--<<<<<"+
		// requestCode);
		// System.out.println(">>>>>>ON ACTIVITY RESULT--requestCode--<<<<<" +
		// resultCode);
		// Log.i(">>>>>>ON ACTIVITY RESULT<<<<<", data.toString());
		if (requestCode == ACTION_WEB_VIEW) {
			try {

				// Log.i(">>>>>>ON ACTIVITY RESULT<<<<<",
				// "This is Test for On ActivityResult");

				// String jsonResponse=getIntent().getStringExtra("jsvalue");
				CitrusGetWebClientJSResponse citrusGetWebClientJSResponse = new CitrusGetWebClientJSResponse(
						citrusSSLLibrary.getWebClientJsResponse());// optional
				authIdCode = citrusGetWebClientJSResponse.getAuthIdCode();
				TxId = citrusGetWebClientJSResponse.getTxId();
				TxStatus = citrusGetWebClientJSResponse.getTxStatus();
				pgTxnNo = citrusGetWebClientJSResponse.getPgTxnNo();
				issuerRefNo = citrusGetWebClientJSResponse.getIssuerRefNo();
				TxMsg = citrusGetWebClientJSResponse.getTxMsg();
				// Log.i(">>>>>> RESULT IS <<<<<",
				// "s"+authIdCode+" "+TxId+" "+TxStatus);

				// Utils.log("Response","is"+TxMsg);

				/*
				 * authIdCode = "064486"; TxId = "DSSV000000004167NB"; TxStatus
				 * = "SUCCESS"; pgTxnNo = "201312991999"; issuerRefNo =
				 * "201312991999"; TxMsg = "Transaction Successful";
				 */
				// authIdCode = citrusGetWebClientJSResponse.getAuthIdCode();
				// TxId = citrusGetWebClientJSResponse.getTxId();
				/*
				 * if(citrusGetWebClientJSResponse.get)
				 * mTranscationIdOrderId.setText
				 * ("Transaction Number: "+citrusGetWebClientJSResponse
				 * .getTranssactionId()+" |  Order Amount: "+
				 * citrusGetWebClientJSResponse
				 * .getAmount()+" "+citrusGetWebClientJSResponse.getCurrency());
				 * mTextMessage
				 * .setText(citrusGetWebClientJSResponse.getTxMsg());
				 * mTxRefNo.setText(citrusGetWebClientJSResponse.getTxRefNo());
				 * mTxStatus
				 * .setText(citrusGetWebClientJSResponse.getTxStatus());
				 */

				// getTransactionResponse =
				// citrusGetWebClientJSResponse.getTxRefNo();

				/*
				 * Intent intent = new Intent(MakeMyPayments.this,
				 * TransResponse.class); intent.putExtra("jsvalue",
				 * citrusSSLLibrary.getWebClientJsResponse());
				 * startActivity(intent); MakeMyPayments.this.finish();
				 */

				InsertAfterPayemnt = new InsertAfterPayemnt();
				InsertAfterPayemnt.execute((String) null);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void changeTheContentView() {
		if (response != null && response.size() > 0) {
			TextView heading = new TextView(MakeMyPayments.this);
			heading.setLayoutParams(MATCH_WRAP_LAYOUT);
			// heading.setTextAppearance(this, R.style.boldText);
			heading.setText("Transaction Details");
			heading.setTextSize(18);
			responseLayout.addView(heading);
			// Log.i(">>>>>>ResponseData<<<<", response.toString());
			addFieldsToTheMainLayout("TxId", "Transaction id");
			addFieldsToTheMainLayout("TxStatus", "Transaction status");
			addFieldsToTheMainLayout("TxRefNo", "Transaction reference number");
			addFieldsToTheMainLayout("amount", "Transaction Amount");
			addFieldsToTheMainLayout("TxMsg", "Transaction Message");
			addFieldsToTheMainLayout("transactionId", "Citrus transaction id");
			addFieldsToTheMainLayout(null, "User Details");
			addFieldsToTheMainLayout("firstName", "First name");
			addFieldsToTheMainLayout("lastName", "Last name");
			addFieldsToTheMainLayout("email", "Email");
			addFieldsToTheMainLayout("mobileNo", "Mobile");
			addFieldsToTheMainLayout("addressStreet1", "Address Street");
			addFieldsToTheMainLayout("addressCity", "City");
			addFieldsToTheMainLayout("addressState", "State");
			addFieldsToTheMainLayout("addressZip", "Pincode");
			addFieldsToTheMainLayout("addressCountry", "Country");
			addFieldsToTheMainLayout(null, "Other Details");
			addFieldsToTheMainLayout("pgTxnNo", "PG transaction number");
			addFieldsToTheMainLayout("pgRespCode", "PG response code");
			addFieldsToTheMainLayout("authIdCode", "Authorization id code");
			addFieldsToTheMainLayout("issuerRefNo", "Issuer Reference Number");
			addFieldsToTheMainLayout("productSKU", "Product SKU");
			payNowView.setVisibility(View.GONE);
			responseScrollLayout.setVisibility(View.VISIBLE);
		}
	}

	private void addFieldsToTheMainLayout(String fieldName, String viewHeading) {
		String txStatus = response.get(fieldName);
		TextView textView = new TextView(MakeMyPayments.this);
		textView.setLayoutParams(MATCH_WRAP_LAYOUT);
		if (fieldName != null) {
			textView.setText(Html.fromHtml("<b>" + viewHeading + " : " + "</b>"
					+ "<br />" + "<small>" + txStatus + "</small>"));
		} else {
			textView.setText(Html.fromHtml("<br />" + "<b>" + viewHeading
					+ "</b>"));
		}
		if (txStatus != null && !txStatus.equals("") || fieldName == null)
			responseLayout.addView(textView);
	}

	private class GetMemberDetailWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener {

		ProgressHUD mProgressHUD;

		protected void onPreExecute() {

			mProgressHUD = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);
			Utils.log("2 Progress", "start");
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
				mProgressHUD.dismiss();
			Utils.log("2 Progress", "end");

			// Log.i(">>>>.MemberDetails<<<<<<", mapMemberDetails.toString());
			try {
				if (rslt.trim().equalsIgnoreCase("ok")) {
					if (mapMemberDetails != null) {

						Set<String> keys = mapMemberDetails.keySet();
						String str_keyVal = "";

						for (Iterator<String> i = keys.iterator(); i.hasNext();) {
							str_keyVal = (String) i.next();

						}
						String selItem = str_keyVal.trim();
						isLogout = false;
						// finish();
						MemberDetailsObj memberDetails = mapMemberDetails
								.get(selItem);
						txtloginid.setText(memberDetails.getMemberLoginId());
						txtemailid.setText(memberDetails.getEmailId());
						txtcontactno.setText(memberDetails.getMobileNo());
						// address = memberDetails.getInstLocAddressLine1() +
						// " " + memberDetails.getInstLocAddressLine2();
						customername = memberDetails.getMemberName();

						getpaymentgatewaysdetails = new PaymentGateWayDetails();
						getpaymentgatewaysdetails.execute((String) null);

					}
				} else if (rslt.trim().equalsIgnoreCase("not")) {
					if(is_activity_running)
					AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",
							context);
				} else {
					if(is_activity_running)
					AlertsBoxFactory.showAlert(rslt, context);
				}
			} catch (Exception e) {
				if(is_activity_running)
				AlertsBoxFactory.showAlert(rslt, context);
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				MemberDetailCaller memberdetailCaller = new MemberDetailCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_SUBSCRIBER_DETAILS));

				memberdetailCaller.memberid = memberid;

				memberdetailCaller.setAllData(false);
				memberdetailCaller.setTopup_flag(false);
				memberdetailCaller.join();
				memberdetailCaller.start();
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

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
	}

	private class PaymentGateWayDetails extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;

		protected void onPreExecute() {

			mProgressHUD = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);
			Utils.log("3 Progress", "start");
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			getpaymentgatewaysdetails = null;

		}

		protected void onPostExecute(Void unused) {
			Utils.log("3 Progress", "end");
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
			mProgressHUD.dismiss();
			getpaymentgatewaysdetails = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					TrackId = adjTrackval;
					// Log.i(">>>>TrackId<<<<", TrackId);
				} catch (NumberFormatException nue) {
					RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
					radioGroup.clearCheck();
					// Log.i(">>>>>New PLan Rate<<<<<<", adjTrackval);

				}

			} else {
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {

			try {
				PaymentGatewayCaller adjCaller = new PaymentGatewayCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_TRANSACTIONID_WITH_BANK_NAME),"CP");
				adjCaller.setMemberId(utils.getMemberId());
				// adjCaller.setAreaCode(AreaCode);
				// adjCaller.setAreaCodeFilter(AreaCodeFilter);
				adjCaller.setTopup_falg(false);

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
				/*
				 * AlertsBoxFactory.showErrorAlert("Error web-service response "
				 * + e.toString(), context);
				 */
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();

		}

	}

	public String generateRandomMerchantTransactionId() {
		BigInteger b = new BigInteger(64, new Random());
		String tId = b.toString();
		return tId;
	}

	private class GetCitrusSignature extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;

		protected void onPreExecute() {

			mProgressHUD = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);
		}

		@Override
		protected void onCancelled() {
			if(is_activity_running)
			mProgressHUD.dismiss();
			citrussignature = null;

		}

		protected void onPostExecute(Void unused) {
			if(is_activity_running)
			mProgressHUD.dismiss();
			citrussignature = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					GetSignature = HMACSignature;
					/*
					 * ashok change
					 */

					// Log.i(" >>>> "," ********* GO FOR OTHER WS *************** ");
					// Log.i(" >>>> "," ********* GO FOR OTHER WS *************** "+GetSignature);

					// InsertBeforePayemnt = new InsertBeforePayemnt();
					// InsertBeforePayemnt.execute((String) null);
					
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new GetAllDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
					}
					else
					new GetAllDetailsAsyncTask().execute();
					/*
					 * getsignaturefrommerchant = new
					 * GetSignatureFromMerchantServer(nameValuePairs);
					 * getsignaturefrommerchant.execute();
					 */

				} catch (NumberFormatException nue) {
					// Log.i(">>>>>New PLan Rate<<<<<<", HMACSignature);
					if (HMACSignature.equalsIgnoreCase("anyType{}")) {
						if(is_activity_running)
						AlertsBoxFactory.showAlert(
								"Conversion is not possible.", context);
					} else {
						if(is_activity_running)
						AlertsBoxFactory.showAlert(HMACSignature, context);
					}
				}

			} else {
				if(is_activity_running)
				AlertsBoxFactory.showAlert("Invalid web-service response "
						+ rslt, context);
			}
			this.cancel(true);
		}

		@Override
		protected Void doInBackground(String... arg0) {

			try {
				CitrusSignatureCaller citrusCaller = new CitrusSignatureCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_HMAC_SIGNATURE),true);
				// Log.i(">>>TrackId<<<", TrackId);
				// Log.i(" >>>> "," ********* *************** ");
				citrusCaller.setMemberId(memberid);
				citrusCaller.setAmount(txtnewamount.getText().toString());
				citrusCaller.setTrackId(TrackId);

				// adjCaller.setAreaCode(AreaCode);
				// adjCaller.setAreaCodeFilter(AreaCodeFilter);

				citrusCaller.join();
				citrusCaller.start();
				rslt = "START";

				while (rslt == "START") {
					try {
						Thread.sleep(10);
					} catch (Exception ex) {
					}
				}

			} catch (Exception e) {
				/*
				 * AlertsBoxFactory.showErrorAlert("Error web-service response "
				 * + e.toString(), context);
				 */
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			if(is_activity_running)
			mProgressHUD.dismiss();
		}

	}

	/* Insert Before Going to Payment Gateway */

	private class InsertBeforePayemnt extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {

			mProgressHUD = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);

		}

		@Override
		protected void onCancelled() {
			if(is_activity_running)
			mProgressHUD.dismiss();
			InsertBeforePayemnt = null;
			// submit.setClickable(true);
		}

		protected void onPostExecute(Void unused) {
			if(is_activity_running)
			mProgressHUD.dismiss();
			// submit.setClickable(true);
			InsertBeforePayemnt = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				getsignaturefrommerchant = new GetSignatureFromMerchantServer(
						nameValuePairs);
				getsignaturefrommerchant.execute();

			} else {
				if(is_activity_running)
				AlertsBoxFactory.showAlert(rslt, context);
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				// setCurrDateTime();
				// Log.i(" >>>>> ",getCurrDateTime());

				BeforePaymentInsertCaller caller = new BeforePaymentInsertCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_BEFORE_MEMBER_PAYMENTS_NEW),true);

				paymentsObj.setMemberId(Long.valueOf(utils.getMemberId()));
				paymentsObj.setTrackId(TrackId);
				paymentsObj.setAmount(txtnewamount.getText().toString().trim());
				paymentsObj.setPackageName(txtnewpackagename.getText().toString());
				paymentsObj.setServiceTax(ServiceTax);
				paymentsObj.setDiscount_Amount(additionalAmount.getDiscountAmount());
				if(Utils.pg_sms_request){
					if(Utils.pg_sms_uniqueid.length()>0){
						paymentsObj.setPg_sms_unique_id(Utils.pg_sms_uniqueid);
					}
					else{
						paymentsObj.setPg_sms_unique_id(null);
					}
				}
				else{
					paymentsObj.setPg_sms_unique_id(null);
				}
				caller.setPaymentdata(paymentsObj);

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
				/* AlertsBoxFactory.showAlert(rslt,context ); */
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			if(is_activity_running)
			mProgressHUD.dismiss();
		}
	}

	/* Insert Before Going to Payment Gateway */

	private class InsertAfterPayemnt extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {

			mProgressHUD = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);

		}

		@Override
		protected void onCancelled() {
			if(is_activity_running)
			mProgressHUD.dismiss();
			InsertBeforePayemnt = null;
			// submit.setClickable(true);
		}

		protected void onPostExecute(Void unused) {
			if(is_activity_running){
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
			mProgressHUD.dismiss();
			}
			// submit.setClickable(true);
			InsertBeforePayemnt = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				// Utils.log("Now in renew",":"+additionalAmount.getAdditionalAmountType());
				
				if (TxStatus.equals("SUCCESS")) {
					if (additionalAmount != null) {
						if (additionalAmount.getAdditionalAmountType() != null) {
							if (additionalAmount.getAdditionalAmountType()
									.length() > 0) {
								if (additionalAmount.getAdditionalAmountType()
										.contains("#")) {
									String split[] = additionalAmount
											.getAdditionalAmountType().split(
													"#");
									if (split.length > 0) {
										type = split[1];
									}
								}
							} else {

							}
						}
					} else {

					}
					RenewalProcessWebService = new RenewalProcessWebService();
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					 {
						 RenewalProcessWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
					 }
					 else{
						 RenewalProcessWebService.execute((String) null);
					 }
				} else {
					MakeMyPayments.this.finish();
					Intent intent = new Intent(getApplicationContext(),
							TransResponse.class);
					intent.putExtra("jsvalue",
							citrusSSLLibrary.getWebClientJsResponse());
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_left,
							R.anim.slide_out_right);
					// MakeMyPayments.this.finish();

				}
				
				/*RenewalProcessWebService = new RenewalProcessWebService();
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 {
					 RenewalProcessWebService.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);					
				 }
				 else{
					 RenewalProcessWebService.execute((String) null);
				 }*/
			} else {
				if(is_activity_running)
				AlertsBoxFactory.showAlert(rslt, context);
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				// setCurrDateTime();
				// Log.i(" >>>>> ",getCurrDateTime());

				AfterInsertPaymentsCaller caller = new AfterInsertPaymentsCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_AFTER_MEMBER_PAYMENTS),true);

				paymentsObj.setAuthIdCode(authIdCode);
				paymentsObj.setTxId(TxId);
				paymentsObj.setTxStatus(TxStatus);
				paymentsObj.setPgTxnNo(pgTxnNo);
				paymentsObj.setIssuerRefNo(issuerRefNo);
				paymentsObj.setTxMsg(TxMsg);

				caller.setPaymentdata(paymentsObj);

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
				/* AlertsBoxFactory.showAlert(rslt,context ); */
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			if(is_activity_running)
			mProgressHUD.dismiss();

		}
	}

	/* For Renewal Process */

	private class RenewalProcessWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener {

		ProgressHUD mProgressHUD1;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {

			mProgressHUD1 = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);
		}

		@Override
		protected void onCancelled() {
			if(is_activity_running)
			this.mProgressHUD1.dismiss();
			RenewalProcessWebService = null;
			// submit.setClickable(true);
		}

		protected void onPostExecute(Void unused) {

			//if(mProgressHUD1!=null&&mProgressHUD1.isShowing())
			if(is_activity_running)
				mProgressHUD1.dismiss();
			// submit.setClickable(true);
			RenewalProcessWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) {
				// finish();
				MakeMyPayments.this.finish();

				Intent intent = new Intent(getApplicationContext(),
						TransResponse.class);
				intent.putExtra("jsvalue",
						citrusSSLLibrary.getWebClientJsResponse());
				startActivity(intent);
				/*
				 * Intent intent = new
				 * Intent(MakeMyPayments.this,IONHome.class);
				 * startActivity(intent);
				 */
			} else {
				if(is_activity_running)
				AlertsBoxFactory.showAlert(rslt, context);
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				// setCurrDateTime();
				// Log.i(" >>>>> ",getCurrDateTime());

				/*
				 * RenewalCaller caller = new RenewalCaller(
				 * getApplicationContext().getResources().getString(
				 * R.string.WSDL_TARGET_NAMESPACE),
				 * getApplicationContext().getResources().getString(
				 * R.string.SOAP_URL), getApplicationContext()
				 * .getResources().getString(
				 * R.string.METHOD_MYPAGE_RENEWAL_PROCESS))
				 */;
				RenewalCaller caller = new RenewalCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL),
						getApplicationContext().getResources().getString(
								R.string.METHOD_RENEWAL_REACTIVATE_METHOD));

				paymentsObj.setMobileNumber(utils.getMobileNoPrimary());
				paymentsObj.setSubscriberID(utils.getMemberLoginID());
				paymentsObj.setPlanName(txtnewpackagename.getText().toString());
				paymentsObj.setPaidAmount(Double.parseDouble(txtnewamount.getText().toString().trim()));
				paymentsObj.setTrackId(TrackId);
				// System.out.println("-------------Change Package :-----------"
				// + Changepack);
				paymentsObj.setIsChangePlan(Changepack);
				paymentsObj.setActionType(UpdateFrom);
				paymentsObj.setPaymentId(authIdCode);
				//paymentsObj.setPaymentId("123456");
				paymentsObj.setTxMsg(TxMsg);

				caller.setPaymentdata(paymentsObj);
				caller.setRenewalType(type);

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
				/* AlertsBoxFactory.showAlert(rslt,context ); */
			}
			return null;
		}

		/*
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * }
		 */

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD1.dismiss();
		}
	}

	private class GetCitrusCOnstantWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener {

		ProgressHUD mProgressHUD;

		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(MakeMyPayments.this, getString(R.string.app_please_wait_label), true, true, this);
			Utils.log("1 Progress", "start");
		}

		protected void onPostExecute(Void unused) {
			GetCitrusCOnstantWebService = null;
			if(is_activity_running){
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
			mProgressHUD.dismiss();
			}
			Utils.log("1 Progress", "end");

			try {
				//rslt="error";
				if (rslt.trim().equalsIgnoreCase("ok")) {
					if (mapcitrusconstants != null) {

						Set<String> keys = mapcitrusconstants.keySet();
						String str_keyVal = "";

						for (Iterator<String> i = keys.iterator(); i.hasNext();) {
							str_keyVal = (String) i.next();

							String selItem = str_keyVal.trim();
							isLogout = false;
							// finish();
							CitrusConstantsObj CitrusConstants = mapcitrusconstants
									.get(selItem);
							Constants.VANITY_URL = CitrusConstants
									.getVANITYURL();
							Constants.MYCITRUS_SERVER_URL = CitrusConstants
									.getMYCITRUS_SERVER_URL();

							// Utils.log("Constants.VANITY_URL","is"+Constants.VANITY_URL);
							// Utils.log("Constants.MYCITRUS_SERVER_URL","is "+Constants.MYCITRUS_SERVER_URL);

							// Utils.log("onstants.MERCHANTTXNID","is"+Constants.MERCHANTTXNID);

							nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair(
									CitrusParams.PARAM_VANITY_URL,
									Constants.VANITY_URL));
							nameValuePairs.add(new BasicNameValuePair(
									CitrusParams.PARAM_ORDER_AMOUNT,
									txtnewamount.getText().toString()));
							nameValuePairs.add(new BasicNameValuePair(
									CitrusParams.PARAM_CURRENCY, "INR"));
							nameValuePairs.add(new BasicNameValuePair(
									Constants.MERCHANTTXNID, TrackId));
						}
						getMemberDetailWebService = new GetMemberDetailWebService();
						getMemberDetailWebService.execute((String) null);
					}
				} else if (rslt.trim().equalsIgnoreCase("not")) {
					if(is_activity_running)
					AlertsBoxFactory.showAlert("Payment Gateway not configured ",
							context);
					is_allow=false;
				} else {
					if(is_activity_running){
						//AlertsBoxFactory.showAlert(rslt, context);
						if (rslt.trim().equalsIgnoreCase("error")) {
							iError.display();
						}
						else{
							iError.display();
						}
						
					}
				}
			} catch (Exception e) {
				if(is_activity_running)
				AlertsBoxFactory.showAlert(rslt, context);
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				CitrusConstantCaller citruscaller = new CitrusConstantCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_GET_CITRUS_CONSTANTS),
						false);

				citruscaller.memberid = memberid;

				citruscaller.join();
				citruscaller.start();
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
			isFinish = true;
			return null;
		}

		@Override
		protected void onCancelled() {
			if(is_activity_running)
			mProgressHUD.dismiss();
			GetCitrusCOnstantWebService = null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			if(is_activity_running)
			mProgressHUD.dismiss();
		}

	}

	public class GetAllDetailsAsyncTask extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;

		String AppVersion = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			mProgressHUD = ProgressHUD
					.show(MakeMyPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);

			PackageInfo pInfo = null;

			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				AppVersion = pInfo.versionName;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetPhoneDetailsSOAP getPhoneDetailsSOAP = new GetPhoneDetailsSOAP(
					getApplicationContext().getResources().getString(
							R.string.WSDL_TARGET_NAMESPACE),
					getApplicationContext().getResources().getString(
							R.string.SOAP_URL), getApplicationContext()
							.getResources().getString(
									R.string.METHOD_UPDATE_PHONE_DETAILS));

			try {
				getPhoneDetailsSOAP.getPhoneDetails(memberid,
						Build.MANUFACTURER.toString(), AppVersion);
			} catch (SocketTimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SoapFault e) {
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
			if(is_activity_running){
			if(mProgressHUD!=null&&mProgressHUD.isShowing())
			mProgressHUD.dismiss();
			}
			InsertBeforePayemnt = new InsertBeforePayemnt();
			InsertBeforePayemnt.execute((String) null);
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			if(is_activity_running)
			mProgressHUD.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		this.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		/*
		 * Intent i = new Intent(MakeMyPayments.this,RenewPackage.class);
		 * startActivity(i); overridePendingTransition(R.anim.slide_in_left,
		 * R.anim.slide_out_right);
		 */
	}

	public void showPaymentDetailsDialog(AdditionalAmount additionalAmount) {
		if (additionalAmount != null) {
			final Dialog dialog = new Dialog(MakeMyPayments.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.dialog_additional_amount);

			int width = 0;
			int height = 0;

			Point size = new Point();
			WindowManager w = ((Activity) MakeMyPayments.this)
					.getWindowManager();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				w.getDefaultDisplay().getSize(size);
				width = size.x;
				height = size.y;
			} else {
				Display d = w.getDefaultDisplay();
				width = d.getWidth();
				height = d.getHeight();
				;
			}

			LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;

			TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;

			ll_package_rate = (LinearLayout) dialog
					.findViewById(R.id.ll_package_rate);
			ll_add_amt = (LinearLayout) dialog.findViewById(R.id.ll_add_amt);
			ll_add_reason = (LinearLayout) dialog
					.findViewById(R.id.ll_add_reason);
			ll_discount_amt = (LinearLayout) dialog
					.findViewById(R.id.ll_discount_amt);
			ll_fine_amount = (LinearLayout) dialog
					.findViewById(R.id.ll_fine_amt);
			ll_days_fine_amt = (LinearLayout) dialog
					.findViewById(R.id.ll_days_fine_amt);
			ll_discount_per = (LinearLayout) dialog
					.findViewById(R.id.ll_discount_per);
			ll_final_amt = (LinearLayout) dialog
					.findViewById(R.id.ll_final_amount);

			tv_package_rate = (TextView) dialog
					.findViewById(R.id.tv_package_rate);
			tv_add_amt = (TextView) dialog.findViewById(R.id.tv_add_amt);
			tv_add_reason = (TextView) dialog.findViewById(R.id.tv_add_reason);
			tv_discount_amt = (TextView) dialog
					.findViewById(R.id.tv_discount_amt);
			tv_fine_amount = (TextView) dialog.findViewById(R.id.tv_fine_amt);
			tv_days_fine_amt = (TextView) dialog
					.findViewById(R.id.tv_days_fine_amt);
			tv_discount_per = (TextView) dialog
					.findViewById(R.id.tv_discount_per);
			tv_final_amt = (TextView) dialog.findViewById(R.id.tv_final_amount);

			if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
				ll_package_rate.setVisibility(View.VISIBLE);
				tv_package_rate.setText(additionalAmount.getPackageRate());
			} else {

				ll_package_rate.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
				ll_add_amt.setVisibility(View.VISIBLE);
				tv_add_amt.setText(additionalAmount.getAdditionalAmount());
			} else {

				ll_add_amt.setVisibility(View.GONE);

			}

			if (additionalAmount.getAdditionalAmountType().length() > 0) {
				ll_add_reason.setVisibility(View.GONE);
				tv_add_reason.setText(additionalAmount
						.getAdditionalAmountType());
			} else {

				ll_add_reason.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
				ll_discount_amt.setVisibility(View.VISIBLE);
				tv_discount_amt.setText(additionalAmount.getDiscountAmount());
			} else {

				ll_discount_amt.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
				ll_fine_amount.setVisibility(View.VISIBLE);
				tv_fine_amount.setText(additionalAmount.getFineAmount());
			} else {

				ll_fine_amount.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
				ll_days_fine_amt.setVisibility(View.VISIBLE);
				tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
			} else {

				ll_days_fine_amt.setVisibility(View.GONE);

			}
			if (additionalAmount.getDiscountPercentage().length() > 0) {
				if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
					ll_discount_per.setVisibility(View.VISIBLE);
					tv_discount_per.setText(additionalAmount
							.getDiscountPercentage());
				} else {
					ll_discount_per.setVisibility(View.GONE);
				}
			} else {
				ll_discount_per.setVisibility(View.GONE);
			}

			if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
				ll_final_amt.setVisibility(View.VISIBLE);
				tv_final_amt.setText(additionalAmount.getFinalcharges());
			} else {
				ll_final_amt.setVisibility(View.GONE);
			}
			Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});

			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
					LayoutParams.WRAP_CONTENT);

			dialog.show();

		}
	}

	public void showPaymentDetails(AdditionalAmount additionalAmount) {
		if (additionalAmount != null) {
			// ll_addtional_details.setVisibility(View.VISIBLE);
			LinearLayout ll_package_rate, ll_add_amt, ll_add_reason, ll_discount_amt, ll_fine_amount, ll_days_fine_amt, ll_discount_per, ll_final_amt;

			TextView tv_package_rate, tv_add_amt, tv_add_reason, tv_discount_amt, tv_fine_amount, tv_days_fine_amt, tv_discount_per, tv_final_amt;

			ll_package_rate = (LinearLayout) findViewById(R.id.ll_package_rate);
			ll_add_amt = (LinearLayout) findViewById(R.id.ll_add_amt);
			ll_add_reason = (LinearLayout) findViewById(R.id.ll_add_reason);
			ll_discount_amt = (LinearLayout) findViewById(R.id.ll_discount_amt);
			ll_fine_amount = (LinearLayout) findViewById(R.id.ll_fine_amt);
			ll_days_fine_amt = (LinearLayout) findViewById(R.id.ll_days_fine_amt);
			ll_discount_per = (LinearLayout) findViewById(R.id.ll_discount_per);
			ll_final_amt = (LinearLayout) findViewById(R.id.ll_final_amount);

			tv_package_rate = (TextView) findViewById(R.id.tv_package_rate);
			tv_add_amt = (TextView) findViewById(R.id.tv_add_amt);
			tv_add_reason = (TextView) findViewById(R.id.tv_add_reason);
			tv_discount_amt = (TextView) findViewById(R.id.tv_discount_amt);
			tv_fine_amount = (TextView) findViewById(R.id.tv_fine_amt);
			tv_days_fine_amt = (TextView) findViewById(R.id.tv_days_fine_amt);
			tv_discount_per = (TextView) findViewById(R.id.tv_discount_per);
			tv_final_amt = (TextView) findViewById(R.id.tv_final_amount);

			if (Double.valueOf(additionalAmount.getPackageRate()) > 0) {
				ll_package_rate.setVisibility(View.VISIBLE);
				tv_package_rate.setText(additionalAmount.getPackageRate());
			} else {

				ll_package_rate.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getAdditionalAmount()) > 0) {
				ll_add_amt.setVisibility(View.VISIBLE);
				tv_add_amt.setText(additionalAmount.getAdditionalAmount());
			} else {

				ll_add_amt.setVisibility(View.GONE);

			}

			if (additionalAmount.getAdditionalAmountType().length() > 0) {
				ll_add_reason.setVisibility(View.GONE);
				tv_add_reason.setText(additionalAmount
						.getAdditionalAmountType());
			} else {

				ll_add_reason.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getDiscountAmount()) > 0) {
				ll_discount_amt.setVisibility(View.VISIBLE);
				tv_discount_amt.setText(additionalAmount.getDiscountAmount());
			} else {

				ll_discount_amt.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getFineAmount()) > 0) {
				ll_fine_amount.setVisibility(View.VISIBLE);
				tv_fine_amount.setText(additionalAmount.getFineAmount());
			} else {

				ll_fine_amount.setVisibility(View.GONE);

			}

			if (Double.valueOf(additionalAmount.getDaysFineAmount()) > 0) {
				ll_days_fine_amt.setVisibility(View.VISIBLE);
				tv_days_fine_amt.setText(additionalAmount.getDaysFineAmount());
			} else {

				ll_days_fine_amt.setVisibility(View.GONE);

			}
			if (additionalAmount.getDiscountPercentage().length() > 0) {
				if (Double.valueOf(additionalAmount.getDiscountPercentage()) > 0) {
					ll_discount_per.setVisibility(View.VISIBLE);
					tv_discount_per.setText(additionalAmount
							.getDiscountPercentage());
				} else {
					ll_discount_per.setVisibility(View.GONE);
				}
			} else {
				ll_discount_per.setVisibility(View.GONE);
			}

			if (Double.valueOf(additionalAmount.getFinalcharges()) > 0) {
				ll_final_amt.setVisibility(View.VISIBLE);
				tv_final_amt.setText(additionalAmount.getFinalcharges());
			} else {
				ll_final_amt.setVisibility(View.GONE);
			}
		} else {
			ll_addtional_details.setVisibility(View.GONE);
		}
	}

	public class ReactivateProcess extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
