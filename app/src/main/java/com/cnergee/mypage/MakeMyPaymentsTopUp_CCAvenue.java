package com.cnergee.mypage;

import java.math.BigInteger;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapFault;

import all.interface_.IError;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.avenues.lib.utility.AvenuesParams;
import com.citruspay.citruspaylib.CitrusSSLLibrary;
import com.citruspay.citruspaylib.model.Address;
import com.citruspay.citruspaylib.model.Card;
import com.citruspay.citruspaylib.model.Customer;
import com.citruspay.citruspaylib.model.ExtraParams;
import com.citruspay.citruspaylib.service.CitrusGetWebClientJSResponse;
import com.citruspay.citruspaylib.utils.CitrusParams;
import com.citruspay.citruspaylib.utils.Constants;
import com.cnergee.mypage.MakeMyPayments_CCAvenue.Get_CCAvenue_Signature;
import com.cnergee.mypage.SOAP.GetPhoneDetailsSOAP;
import com.cnergee.mypage.SOAP.Get_CCAvenue_SignatureSOAP;
import com.cnergee.mypage.caller.AfterInsertPaymentsCaller;
import com.cnergee.mypage.caller.BeforePaymentInsertCaller;
import com.cnergee.mypage.caller.CitrusConstantCaller;
import com.cnergee.mypage.caller.CitrusSignatureCaller;
import com.cnergee.mypage.caller.MemberDetailCaller;
import com.cnergee.mypage.caller.PaymentGatewayCaller;
import com.cnergee.mypage.caller.RenewalCaller;
import com.cnergee.mypage.caller.RenewalCallerTopup;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.CitrusConstantsObj;
import com.cnergee.mypage.obj.MemberDetailsObj;
import com.cnergee.mypage.obj.PaymentGatewayObj;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import cnergee.krishna.ookla.odigitel.R;


public class MakeMyPaymentsTopUp_CCAvenue extends BaseActivity
{
	private static final String retrunURL = "https://sandbox.citruspay.com/demo/jsp/response_android.jsp";
	private boolean isFinish = false;
	public static Context context;
	Utils utils = new Utils();
	public static String rslt = "";
	public long memberid;
	public static String responseMsg ="";
	public static String getTransactionResponse="";
	String ServiceTax,UpdateFrom;
	LinearLayout linnhome,linnprofile,linnnotification,linnhelp;

	TextView txtloginid,txtemailid,txtcontactno,txtnewpackagename,txtnewamount,txtnewvalidity;
	CheckBox privacy,terms;
	private String sharedPreferences_name;
	public static Map<String, MemberDetailsObj> mapMemberDetails;
	public static Map<String, PaymentGatewayObj>mappaymentgateways;
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
	String authIdCode,TxId,TxStatus,pgTxnNo,issuerRefNo,TxMsg;
	public static boolean Changepack;
	
	private static final int ACTION_WEB_VIEW = 111;
	private String customername;
	String TrackId;
	Button btnnb;
	Address address;
	ExtraParams extraParam;
	Card cards;
	Customer customer;
	
	//String address;
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
	String discount="";
	TableLayout tableRowDiscount;
	TextView tvDiscountLabel;
	String speed ="",data="";
	String Topup_Validity="";
	String discount_amt="0";
	String Tag=this.getClass().getSimpleName();
	TextView tvClickDetails;
	public boolean trackid_check=false;
	
	String cca_access_code="",cca_merchant_id="",cca_enc_req="",cca_working_key="",cca_request="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.makemypayments);
		
		iError= (IError) this; 
		txtloginid = (TextView)findViewById(R.id.txtloginid);
		txtemailid = (TextView)findViewById(R.id.txtemailid);
		txtcontactno = (TextView)findViewById(R.id.txtcontactno);
		txtnewpackagename = (TextView)findViewById(R.id.txtnewpackagename);
		txtnewamount = (TextView)findViewById(R.id.txtnewamount);
		txtnewvalidity = (TextView)findViewById(R.id.txtnewvalidity);
		tvClickDetails= (TextView)findViewById(R.id.tvClickDetails);
		tvClickDetails.setVisibility(View.GONE);
		btnnb = (Button)findViewById(R.id.btnnb);
		citrusSSLLibrary = new CitrusSSLLibrary();
		responseScrollLayout = (ScrollView) findViewById(R.id.responseScrollLayout);
		responseLayout = (LinearLayout) findViewById(R.id.responseLayout);
		payNowView = (ScrollView) findViewById(R.id.payNowLayout);
		privacy = (CheckBox)findViewById(R.id.privacy);
		terms = (CheckBox)findViewById(R.id.terms);
		tableRowDiscount=(TableLayout)findViewById(R.id.tableLayoutDiscount);
		tvDiscountLabel=(TextView)findViewById(R.id.tvDiscountLabel);
		context = this;
		Intent intent = getIntent();
		Bundle bundle  = intent.getExtras();
		//Log.i(">>>>>BUndle<<<<<", extra.toString());
		
		/*Log.i(">>>>>PAckageAmount<<<<",bundle .getString("PackageAmount"));*/
				txtnewamount.setText(bundle .getString("PackageAmount"));
				txtnewpackagename.setText(bundle .getString("PackageName"));
				txtnewvalidity.setText(bundle .getString("PackageValidity"));
				ServiceTax = bundle .getString("ServiceTax");
				UpdateFrom = bundle.getString("updateFrom");
				discount = bundle.getString("discount");
				speed= bundle.getString("speed");
				data= bundle.getString("data");
				Topup_Validity=bundle .getString("PackageValidity");
				
				if(bundle.getString("updateFrom").equalsIgnoreCase("changepack"))
				{
					Changepack = true;
					tvDiscountLabel.setVisibility(View.GONE);
				}
				else
				{
					Changepack = false;
					if(discount!=null){
					if(Double.valueOf(discount)>0.0){
						tvDiscountLabel.setVisibility(View.VISIBLE);
						//tvDiscountLabel.setText("You have got "+discount+"% discount for online payment.");						
						tvDiscountLabel.setText("Avail of a "+discount+"% discount by paying online.");
						
					}
					else{
						
						tvDiscountLabel.setVisibility(View.GONE);
					}
					}
					else{
						tvDiscountLabel.setVisibility(View.GONE);
					}
				}
		
		sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		
		utils.setSharedPreferences(sharedPreferences);
		
		memberid = Long.parseLong(utils.getMemberId());
		
	/*	GetCitrusCOnstantWebService = new GetCitrusCOnstantWebService();
		GetCitrusCOnstantWebService.execute((String) null);*/

		if(Utils.isOnline(MakeMyPaymentsTopUp_CCAvenue.this)){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				getpaymentgatewaysdetails = new PaymentGateWayDetails();
				getpaymentgatewaysdetails.executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
	
			} else {
				getpaymentgatewaysdetails = new PaymentGateWayDetails();
				getpaymentgatewaysdetails.execute((String) null);
			}
		}
		else{
			AlertsBoxFactory.showAlert("Please connect to internet !!", MakeMyPaymentsTopUp_CCAvenue.this);
		}
	
		payNowView.setVisibility(View.VISIBLE);
		responseScrollLayout.setVisibility(View.GONE);
				
		btnnb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try{
				if (Double.parseDouble(txtnewamount.getText().toString()) > 0) {
					if (terms.isChecked() == true
							&& privacy.isChecked() == true) {

						if(Utils.isOnline(MakeMyPaymentsTopUp_CCAvenue.this)){
							if(trackid_check){
							
								// TrackId Generated Successfully.
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
									new InsertBeforePayemnt().executeOnExecutor(
											AsyncTask.THREAD_POOL_EXECUTOR,
											(String) null);
								} else {
									new InsertBeforePayemnt().execute((String) null);
								}
							}
							else{
								// TrackId Failed to Generate.
							
								if(Utils.isOnline(MakeMyPaymentsTopUp_CCAvenue.this)){
									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
										getpaymentgatewaysdetails = new PaymentGateWayDetails();
										getpaymentgatewaysdetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
							
									} else {
										
										getpaymentgatewaysdetails = new PaymentGateWayDetails();
										getpaymentgatewaysdetails.execute((String) null);
									}
								}
							}
						}
						else{
							Toast.makeText(MakeMyPaymentsTopUp_CCAvenue.this,
									getString(R.string.app_please_wait_label),
									Toast.LENGTH_LONG).show();
							

						}
						
						
					} else {
						Toast.makeText(MakeMyPaymentsTopUp_CCAvenue.this,
								"Please accept the terms and condition",
								Toast.LENGTH_LONG).show();
						return;

					}

				} else {
					AlertsBoxFactory
							.showAlert(
									"Due to some data mismatch we are unable to process your request\n Please contact admin",
									MakeMyPaymentsTopUp_CCAvenue.this);
				}
				}
				catch (Exception e) {
					// TODO: handle exception
					AlertsBoxFactory
					.showAlert(
							"Due to some data mismatch we are unable to process your request\n Please contact admin",
							MakeMyPaymentsTopUp_CCAvenue.this);
				}

			}
		});
		
		linnhome = (LinearLayout)findViewById(R.id.inn_banner_home);
		linnprofile = (LinearLayout)findViewById(R.id.inn_banner_profile);
		linnnotification = (LinearLayout)findViewById(R.id.inn_banner_notification);
		
		linnhome.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPaymentsTopUp_CCAvenue.this.finish();
				//Intent i = new Intent(MakeMyPaymentsTopUp.this,IONHome.class);
				//startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent("TopupActivity"));
			}
		});
		
		linnprofile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPaymentsTopUp_CCAvenue.this.finish();
				Intent i = new Intent(MakeMyPaymentsTopUp_CCAvenue.this,Profile.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		linnnotification.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPaymentsTopUp_CCAvenue.this.finish();
				Intent i = new Intent(MakeMyPaymentsTopUp_CCAvenue.this,NotificationListActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
			}
		});
		
		linnhelp = (LinearLayout)findViewById(R.id.inn_banner_help);
		
		linnhelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MakeMyPaymentsTopUp_CCAvenue.this.finish();
				Intent i = new Intent(MakeMyPaymentsTopUp_CCAvenue.this,HelpActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
				
			}
		});
		
	}
	
	
	private class GetSignatureFromMerchantServer extends
	AsyncTask<Void, Void, String> implements OnCancelListener{

		
		ProgressHUD mProgressHUD;
List<NameValuePair> nameValuePairs;

public GetSignatureFromMerchantServer(List<NameValuePair> nameValuePairs) {
	this.nameValuePairs = nameValuePairs;
}

@Override
protected void onPreExecute() {
	super.onPreExecute();
	
	
	mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, "Loading...", true, true, this);
	
//	progressDialog = new ProgressDialog(MakeMyPayments.this);
	//progressDialog.setTitle("Loading...");
	mProgressHUD.setCancelable(false);
	mProgressHUD.setMessage("Authenticating Transaction");
	//progressDialog.show();
	
	// ashok
	//Log.i(">>>"," ****** START NEW ACTIVITY ******** ");
}

@Override
protected String doInBackground(Void... params) {
	//generatedHMAC=CitrusHttpResponse.communicateToServer(Constants.GENERATE_HMAC_URL, nameValuePairs);
	//generatedHMAC=CitrusHttpResponse.communicateToServer("http://localhost:8080/sample-kit/jsp/hmac_signature.jsp");
	Utils.log("Top Up", "hmac " + generatedHMAC);
	///ExtraParams.setHMACValue(generatedHMAC);

	//return HMACSignature;
	//return HMACSignature;
	return null;
}

@Override
protected void onPostExecute(String result) {
	super.onPostExecute(result);

	// ashok
		//Log.i(">>>"," ****** I AM IN POST EXE ******** ");
	//Utils.log("gaurav", "result==" + result);
	
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
	
	
	
	
	/*Log.i(">>>>emailid<<<",txtemailid.getText().toString());*/
	customer.setEmail(txtemailid.getText().toString());
	customer.setPhoneNumber(txtcontactno.getText().toString());
	/*Log.i(">>>>PhoneNumber<<<",txtcontactno.getText().toString());*/

	cards = new Card();
	cards.setCardHolderName("");
	cards.setCardNumber("");
	cards.setCardType("");
	cards.setCvvNumber("");
	cards.setExpiryMonth("");
	cards.setExpiryYear("");
	cards.setPaymentMode("NET_BANKING");

	extraParam = new ExtraParams();

	extraParam.setReturnUrl(retrunURL);
	//Log.i(">>>>Amount<<<",txtnewamount.getText().toString());
	extraParam.setOrderAmountValue(txtnewamount.getText().toString());
	//Log.i(">>>>Trackid<<<",TrackId);
	extraParam.setMerchantTxnId(TrackId);
	extraParam.setHMACValue(GetSignature);

	extraParam.setCurrency("INR");
	
	
		Intent launchIntent = new Intent(MakeMyPaymentsTopUp_CCAvenue.this,
				WebViewScreen.class);
		launchIntent.putExtra(CitrusParams.PARAM_CUSTOMER, customer);
		launchIntent.putExtra(CitrusParams.PARAM_ADDRESS, address);
		launchIntent.putExtra(CitrusParams.PARAM_CARD, cards);
		launchIntent.putExtra(CitrusParams.PARAM_EXTRAS, extraParam);

		startActivityForResult(launchIntent, ACTION_WEB_VIEW);
		BaseApplication.getEventBus().post(new FinishEvent(Utils.Last_Class_Name));
	
	if (mProgressHUD != null && mProgressHUD.isShowing()) {
		mProgressHUD.dismiss();
	}

}

@Override
public void onCancel(DialogInterface dialog) {

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
/*ImageView ivLogo= (ImageView) findViewById(R.id.imgdvois);
		
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for private mode
		if(sharedPreferences.getString("showLogo", "0").equalsIgnoreCase("1"))
			ivLogo.setVisibility(View.VISIBLE);
		else
			ivLogo.setVisibility(View.INVISIBLE);*/
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//System.out.println(">>>>>>ON ACTIVITY RESULT--requestCode--<<<<<"+ requestCode);
		//System.out.println(">>>>>>ON ACTIVITY RESULT--requestCode--<<<<<" + resultCode);
		//Log.i(">>>>>>ON ACTIVITY RESULT<<<<<", data.toString());
		if (requestCode == ACTION_WEB_VIEW) {
			try {
				
				//Log.i(">>>>>>ON ACTIVITY RESULT<<<<<", "This is Test for On ActivityResult");
				
				//String jsonResponse=getIntent().getStringExtra("jsvalue");
				CitrusGetWebClientJSResponse citrusGetWebClientJSResponse=new CitrusGetWebClientJSResponse(citrusSSLLibrary.getWebClientJsResponse());//optional
				 authIdCode = citrusGetWebClientJSResponse.getAuthIdCode();
				 TxId = citrusGetWebClientJSResponse.getTxId();
				 TxStatus = citrusGetWebClientJSResponse.getTxStatus();
				 pgTxnNo = citrusGetWebClientJSResponse.getPgTxnNo();
				 issuerRefNo = citrusGetWebClientJSResponse.getIssuerRefNo();
				 TxMsg = citrusGetWebClientJSResponse.getTxMsg();
				// Log.i(">>>>>> RESULT IS <<<<<", "s"+authIdCode+" "+TxId+" "+TxStatus);
				 
				// Utils.log("Response","is"+TxMsg);
				 
				 /*authIdCode = "064486";
				 TxId = "DSSV000000004167NB";
				 TxStatus = "SUCCESS";
				 pgTxnNo = "201312991999";
				 issuerRefNo = "201312991999";
				 TxMsg = "Transaction Successful";*/
				//authIdCode = citrusGetWebClientJSResponse.getAuthIdCode();
				//TxId = citrusGetWebClientJSResponse.getTxId();
				/*if(citrusGetWebClientJSResponse.get)
				mTranscationIdOrderId.setText("Transaction Number: "+citrusGetWebClientJSResponse.getTranssactionId()+" |  Order Amount: "+ citrusGetWebClientJSResponse.getAmount()+" "+citrusGetWebClientJSResponse.getCurrency());
				mTextMessage.setText(citrusGetWebClientJSResponse.getTxMsg());
				mTxRefNo.setText(citrusGetWebClientJSResponse.getTxRefNo());
				mTxStatus.setText(citrusGetWebClientJSResponse.getTxStatus());*/
				
				//getTransactionResponse = citrusGetWebClientJSResponse.getTxRefNo();
				
				/*Intent intent = new Intent(MakeMyPayments.this,
						TransResponse.class);
				intent.putExtra("jsvalue", citrusSSLLibrary.getWebClientJsResponse());
				startActivity(intent);
				MakeMyPayments.this.finish();*/
				 
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
			TextView heading = new TextView(MakeMyPaymentsTopUp_CCAvenue.this);
			heading.setLayoutParams(MATCH_WRAP_LAYOUT);
			//heading.setTextAppearance(this, R.style.boldText);
			heading.setText("Transaction Details");
			heading.setTextSize(18);
			responseLayout.addView(heading);
			//Log.i(">>>>>>ResponseData<<<<", response.toString());
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
		TextView textView = new TextView(MakeMyPaymentsTopUp_CCAvenue.this);
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
	
	private class GetMemberDetailWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{

		ProgressHUD mProgressHUD;
		
		protected void onPreExecute() {
		
			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this,getString(R.string.app_please_wait_label), true, true, this);
			Utils.log(Tag+" 2.Progress"," start");
		}

		protected void onPostExecute(Void unused) {
			getMemberDetailWebService = null;
			mProgressHUD.dismiss();

			Utils.log(Tag+" 2.Progress"," end");
			//Log.i(">>>>.MemberDetails<<<<<<", mapMemberDetails.toString());
				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapMemberDetails != null) {
					
					Set<String> keys = mapMemberDetails.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					}
					String selItem = str_keyVal.trim();
					isLogout = false;
					//finish();
					MemberDetailsObj memberDetails = mapMemberDetails.get(selItem);
					txtloginid.setText(memberDetails.getMemberLoginId());
					txtemailid.setText(memberDetails.getEmailId());
					txtcontactno.setText(memberDetails.getMobileNo());
					//address = memberDetails.getInstLocAddressLine1() + " " + memberDetails.getInstLocAddressLine2();
					customername = memberDetails.getMemberName();
					
					getpaymentgatewaysdetails = new PaymentGateWayDetails();
					getpaymentgatewaysdetails.execute((String) null);
					
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber Not Found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){AlertsBoxFactory.showAlert(rslt,context );}	
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
										R.string.METHOD_SUBSCRIBER_DETAILS)
										);

				memberdetailCaller.memberid = memberid;
				memberdetailCaller.setAllData(false);
				memberdetailCaller.setTopup_flag(true);
				
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
	
	
	
	private class PaymentGateWayDetails extends AsyncTask<String, Void, Void> implements OnCancelListener{
	 
		
		
		ProgressHUD mProgressHUD;
		protected void onPreExecute() {
		
			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true, true, this);
			Utils.log(Tag+" 3.Progress"," start");
		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			getpaymentgatewaysdetails = null;
			
		}
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			Utils.log(Tag+" 3.Progress"," end");
			getpaymentgatewaysdetails = null;
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					TrackId = adjTrackval;
					/*if(TrackId.length()>0){
						
						TrackId = TrackId.substring(0, TrackId.length() - 2)+"CC";
						Utils.log("TrackId", "is:"+TrackId);
						
					}*/
					trackid_check=true;
					//Log.i(">>>>TrackId<<<<", TrackId);
				} catch (NumberFormatException nue) {
					RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioPayMode);
					radioGroup.clearCheck();
					//Log.i(">>>>>New PLan Rate<<<<<<", adjTrackval);
					iError.display();
				}

			} else {
				iError.display();
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
								R.string.SOAP_URL),
						getApplicationContext().getResources().getString(
								R.string.METHOD_GET_TRANSACTIONID_WITH_BANK_NAME),"CA");
				adjCaller.setMemberId(utils.getMemberId());
				//adjCaller.setAreaCode(AreaCode);
				//adjCaller.setAreaCodeFilter(AreaCodeFilter);
				adjCaller.setTopup_falg(true);
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
			/*	AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
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
	
	

	
	private class GetCitrusSignature extends AsyncTask<String, Void, Void> implements OnCancelListener
	{

		ProgressHUD mProgressHUD;
		
		protected void onPreExecute() {
					
			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true, true, this);
		}
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			citrussignature = null;
			
		}
		protected void onPostExecute(Void unused) {
			
			mProgressHUD.dismiss();
			citrussignature = null;
			Log.i("Result >>>> "," is"+rslt);
			
			if (rslt.trim().equalsIgnoreCase("ok")) {
				try {
					GetSignature = HMACSignature;
					/*
					 * ashok change
					 */
					
					//Log.i(" >>>> "," ********* GO FOR OTHER WS *************** ");
					Log.i(" >>>> "," ********* GO FOR OTHER WS *************** "+GetSignature);
					
				//	InsertBeforePayemnt = new InsertBeforePayemnt();
				//	InsertBeforePayemnt.execute((String) null);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
					new GetAllDetailsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
					else
					new GetAllDetailsAsyncTask().execute();	
					/*getsignaturefrommerchant = new GetSignatureFromMerchantServer(nameValuePairs);
					getsignaturefrommerchant.execute();
					*/
					
				} catch (NumberFormatException nue) {
					//Log.i(">>>>>New PLan Rate<<<<<<", HMACSignature);
					if(HMACSignature.equalsIgnoreCase("anyType{}")){
						AlertsBoxFactory.showAlert("Conversion is not possible.", context);
					}else{
						AlertsBoxFactory.showAlert(HMACSignature, context);
					}
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
				CitrusSignatureCaller citrusCaller = new CitrusSignatureCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL),
						getApplicationContext().getResources().getString(
								R.string.METHOD_GET_HMAC_SIGNATURE),false);
				Log.i(">>>TrackId<<<", TrackId);
				//Log.i(" >>>> "," ********* *************** ");
				citrusCaller.setMemberId(memberid);
				citrusCaller.setAmount(txtnewamount.getText().toString());
				citrusCaller.setTrackId(TrackId);
				
				//adjCaller.setAreaCode(AreaCode);
				//adjCaller.setAreaCodeFilter(AreaCodeFilter);
				
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
				/*AlertsBoxFactory.showErrorAlert("Error web-service response "
						+ e.toString(), context);*/
			}
			return null;
		}
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
		@Override
		protected void onCancelled(Void result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
			Utils.log("AsyncTask","Cancelled");
		}
		
	}
	
	
	/* Insert Before Going to Payment Gateway */
	
	private class InsertBeforePayemnt extends AsyncTask<String, Void, Void>  implements OnCancelListener{

	ProgressHUD mProgressHUD;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {

		
		mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this,getString(R.string.app_please_wait_label),true,true,this);
		
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			InsertBeforePayemnt = null;
			//submit.setClickable(true);
		}

		
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			//submit.setClickable(true);
			InsertBeforePayemnt = null;

			if (rslt.trim().equalsIgnoreCase("ok")) 
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					new Get_CCAvenue_Signature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
				}
				else{
				new Get_CCAvenue_Signature().execute();
				}
				
				/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					getsignaturefrommerchant = new GetSignatureFromMerchantServer(nameValuePairs);
					getsignaturefrommerchant.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else{
					getsignaturefrommerchant.execute();
				}*/
				
			} else {
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				//setCurrDateTime();
				//Log.i(" >>>>> ",getCurrDateTime());
				
				
				
				
				BeforePaymentInsertCaller caller = new BeforePaymentInsertCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_BEFORE_MEMBER_PAYMENTS_NEW),false);
				
				paymentsObj.setMemberId(Long.valueOf(utils.getMemberId()));
				paymentsObj.setTrackId(TrackId);
				paymentsObj.setAmount(txtnewamount.getText().toString().trim());
				paymentsObj.setPackageName(txtnewpackagename.getText().toString()); //hjsaask
				paymentsObj.setServiceTax(ServiceTax);
				paymentsObj.setDiscount_Amount("0");
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
				/*AlertsBoxFactory.showAlert(rslt,context );*/
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
		
			mProgressHUD.dismiss();
		}
	}
	
	public class Get_CCAvenue_Signature extends AsyncTask<String, Void, Void> implements OnCancelListener{

	ProgressHUD mProgressHUD;
	String get_SignResult="",get_Sign_Response="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD=ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true, false, this);
		}
		
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			Get_CCAvenue_SignatureSOAP get_CCAvenue_SignatureSOAP= new Get_CCAvenue_SignatureSOAP(getString(R.string.WSDL_TARGET_NAMESPACE), getString(R.string.SOAP_URL), getString(R.string.METHOD_GET_CCAVENUE_SIGNATURE));
			try {
				get_SignResult=get_CCAvenue_SignatureSOAP.getSignature(String.valueOf(memberid), txtnewamount.getText().toString()	, TrackId);
				get_Sign_Response=get_CCAvenue_SignatureSOAP.getResponse();
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
			if(get_SignResult!=null&&get_SignResult.length()>0&&get_SignResult.equalsIgnoreCase("ok")){
				if(get_Sign_Response!=null&&get_Sign_Response.length()>0){
					Utils.log("CCAvenueSignature Response","is"+get_Sign_Response);
					parse_CCAvenueSignature_Response(get_Sign_Response);
				}
				else{
					iError.display();
					//AlertsBoxFactory.showAlert("We are unable to initiate Payment. ", MakeMyPaymentsTopUp_CCAvenue.this);
				}
			}
			else{
				iError.display();
				//AlertsBoxFactory.showAlert("We are unable to initiate Payment.", MakeMyPaymentsTopUp_CCAvenue.this);
			}
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
		
	}
	
	
	
/* Insert Before Going to Payment Gateway */
	
	private class InsertAfterPayemnt extends AsyncTask<String, Void, Void> implements OnCancelListener{

		ProgressHUD mProgressHUD;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {
		
			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true, true, this);
			
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			InsertBeforePayemnt = null;
			//submit.setClickable(true);
		}

		
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			//submit.setClickable(true);
			InsertBeforePayemnt = null;

			if (rslt.trim().equalsIgnoreCase("ok")) 
			{	
				if(TxStatus.equals("SUCCESS"))
				{
					RenewalProcessWebService = new RenewalProcessWebService();
					RenewalProcessWebService.execute((String) null);
				}
				else
				{
					MakeMyPaymentsTopUp_CCAvenue.this.finish();
					Intent intent = new Intent(getApplicationContext(),
							TransResponse.class);
					intent.putExtra("jsvalue", citrusSSLLibrary.getWebClientJsResponse());
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
					//MakeMyPayments.this.finish();
					
					
					
				}
				
				/*RenewalProcessWebService = new RenewalProcessWebService();
				RenewalProcessWebService.execute((String) null);*/
				
			} else {
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				//setCurrDateTime();
				//Log.i(" >>>>> ",getCurrDateTime());
				
				
				
				
				AfterInsertPaymentsCaller caller = new AfterInsertPaymentsCaller(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_AFTER_MEMBER_PAYMENTS),false);
				
				paymentsObj.setAuthIdCode(authIdCode);
				paymentsObj.setTxId(TxId);
				paymentsObj.setTxStatus(TxStatus);
				paymentsObj.setPgTxnNo(pgTxnNo);
				paymentsObj.setIssuerRefNo(issuerRefNo);
				paymentsObj.setTxMsg(TxMsg);
				paymentsObj.setDiscount(discount_amt);
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
				/*AlertsBoxFactory.showAlert(rslt,context );*/
			}
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
		
			mProgressHUD.dismiss();
			
		}
	}
	
	
	
	
	
	
	/* For Renewal Process */
	
	private class RenewalProcessWebService extends AsyncTask<String, Void, Void> implements OnCancelListener{

	
		ProgressHUD mProgressHUD;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {
			
			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true, true, this);
		}


		
		
		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			RenewalProcessWebService = null;
			//submit.setClickable(true);
		}
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			//submit.setClickable(true);
			RenewalProcessWebService = null;

			if (rslt.trim().equalsIgnoreCase("ok")) 
			{
				//finish();
				MakeMyPaymentsTopUp_CCAvenue.this.finish();

				Intent intent = new Intent(getApplicationContext(),
						TransResponse.class);
				intent.putExtra("jsvalue", citrusSSLLibrary.getWebClientJsResponse());
				startActivity(intent);
								/*Intent intent = new Intent(MakeMyPayments.this,IONHome.class);
				startActivity(intent);*/
			} else {
				AlertsBoxFactory.showAlert(rslt,context );
				return;
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				//setCurrDateTime();
				//Log.i(" >>>>> ",getCurrDateTime());
				
				
				
				
				RenewalCallerTopup caller = new RenewalCallerTopup(
						getApplicationContext().getResources().getString(
								R.string.WSDL_TARGET_NAMESPACE),
						getApplicationContext().getResources().getString(
								R.string.SOAP_URL), getApplicationContext()
								.getResources().getString(
										R.string.METHOD_RENEWAL_PROCESS_TOPUP));
				
				paymentsObj.setMobileNumber(utils.getMobileNoPrimary());
				paymentsObj.setSubscriberID(utils.getMemberLoginID());
				paymentsObj.setPlanName(txtnewpackagename.getText().toString());//sdasd
				paymentsObj.setPaidAmount(Double.parseDouble(txtnewamount.getText().toString().trim()));
				//paymentsObj.setTrackId(TrackId);
				paymentsObj.setTrackId(TrackId);
				//System.out.println("-------------Change Package :-----------" + Changepack);
				paymentsObj.setIsChangePlan(Changepack);
				paymentsObj.setActionType(UpdateFrom);
				//CHANGE HERE
				
				paymentsObj.setPaymentId(authIdCode);
				//paymentsObj.setPaymentId("060577");
				paymentsObj.setTxMsg(TxMsg);
				paymentsObj.setSpeed(speed.length()>0?speed:"0");
				paymentsObj.setData(data);
				paymentsObj.setDiscount(discount_amt);
				paymentsObj.setTopup_validity(Topup_Validity);
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
			/*	AlertsBoxFactory.showAlert(rslt,context );*/
			}
			return null;
		}

	/*	@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
*/



		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
	}
	
	private class GetCitrusCOnstantWebService extends AsyncTask<String, Void, Void> implements OnCancelListener {
	
		ProgressHUD mProgressHUD;
		

		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true,true, this);
			Utils.log(Tag+" 1.Progress"," start");
		}

		protected void onPostExecute(Void unused) {
			GetCitrusCOnstantWebService = null;
			mProgressHUD.dismiss();
			Utils.log(Tag+" 1.Progress"," end");
				try{
				if (rslt.trim().equalsIgnoreCase("ok")) {
				if (mapcitrusconstants != null) {
					
					
					Set<String> keys = mapcitrusconstants.keySet();
					String str_keyVal = "";

					for (Iterator<String> i = keys.iterator(); i.hasNext();) {
						str_keyVal = (String) i.next();

					
					String selItem = str_keyVal.trim();
					isLogout = false;
					//finish();
					 CitrusConstantsObj CitrusConstants = mapcitrusconstants.get(selItem);
					 Constants.VANITY_URL = CitrusConstants.getVANITYURL();
					 Constants.MYCITRUS_SERVER_URL = CitrusConstants.getMYCITRUS_SERVER_URL(); 
					 
					// Utils.log("Constants.VANITY_URL","is"+Constants.VANITY_URL);
					 //Utils.log("Constants.MYCITRUS_SERVER_URL","is "+Constants.MYCITRUS_SERVER_URL);
						
					//Utils.log("onstants.MERCHANTTXNID","is"+Constants.MERCHANTTXNID);
					
					nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair(
							CitrusParams.PARAM_VANITY_URL, Constants.VANITY_URL));
					nameValuePairs.add(new BasicNameValuePair(
							CitrusParams.PARAM_ORDER_AMOUNT, txtnewamount.getText().toString()));
					nameValuePairs.add(new BasicNameValuePair(
							CitrusParams.PARAM_CURRENCY, "INR"));
					nameValuePairs.add(new BasicNameValuePair(Constants.MERCHANTTXNID,
							TrackId));
					}
					getMemberDetailWebService = new GetMemberDetailWebService();
					getMemberDetailWebService.execute((String) null);
				}
			}else if (rslt.trim().equalsIgnoreCase("not")) {
				AlertsBoxFactory.showAlert("Subscriber not found !!! ",context );
			}else{
				AlertsBoxFactory.showAlert(rslt,context );
			}
			}catch(Exception e){AlertsBoxFactory.showAlert(rslt,context );}	
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
										true);

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
			isFinish=true;
			return null;
		}
		 @Override
			protected void onCancelled() {
			 mProgressHUD.dismiss();
				GetCitrusCOnstantWebService = null;
			}

		

		@Override
		public void onCancel(DialogInterface dialog) {
			
			mProgressHUD.dismiss();
		}

	
		}
	
	
	public class GetAllDetailsAsyncTask extends AsyncTask<String, Void, Void> implements OnCancelListener{
		
		ProgressHUD mProgressHUD;
		
		String	AppVersion="";
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			mProgressHUD = ProgressHUD.show(MakeMyPaymentsTopUp_CCAvenue.this, getString(R.string.app_please_wait_label), true, true, this);
			
			PackageInfo pInfo = null ;
			
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
									R.string.METHOD_UPDATE_PHONE_DETAILS)
									);

			try {
				getPhoneDetailsSOAP.getPhoneDetails(memberid, Build.MANUFACTURER.toString(), AppVersion);
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
			
			mProgressHUD.dismiss();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				InsertBeforePayemnt = new InsertBeforePayemnt();
				InsertBeforePayemnt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
				
			}
			else{
				InsertBeforePayemnt = new InsertBeforePayemnt();
				InsertBeforePayemnt.execute((String) null);
			}
		}

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
		/*Intent i = new Intent(MakeMyPaymentsTopUp.this,TopupActivity.class);
        startActivity(i);*/
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        if(citrussignature!=null){
        if(citrussignature.getStatus() == AsyncTask.Status.PENDING){
            // My AsyncTask has not started yet
        }

        if(citrussignature.getStatus() == AsyncTask.Status.RUNNING){
            // My AsyncTask is currently doing work in doInBackground()
        	citrussignature.cancel(true);
        }

        if(citrussignature.getStatus() == AsyncTask.Status.FINISHED){
            // My AsyncTask is done and onPostExecute was called
        }
        }
	}
	
	public void parse_CCAvenueSignature_Response(String response){
		try {
			JSONObject mainJsonObject= new JSONObject(response);
			if(mainJsonObject.has("NewDataSet")){
				JSONObject newdatasetJson=mainJsonObject.getJSONObject("NewDataSet");
				if(newdatasetJson.has("Table1")){
					
					JSONObject tableJson=newdatasetJson.getJSONObject("Table1");
					if(tableJson.has("TrackId"))
						TrackId=tableJson.getString("TrackId");
					else
						showExitAlert("Please try Again \n  We are unable to process your request", MakeMyPaymentsTopUp_CCAvenue.this);
					cca_access_code=tableJson.getString("AccessCode");
					cca_enc_req=tableJson.getString("EncRequest");
					cca_merchant_id=tableJson.getString("MerchantId");
					cca_working_key=tableJson.getString("WorkingKey");
					cca_request=tableJson.getString("ccaRequest");
					MakeMyPaymentsTopUp_CCAvenue.this.finish();
					AdditionalAmount additionalAmount=null;
					Intent call_webview= new Intent(MakeMyPaymentsTopUp_CCAvenue.this, WebView_CCAvenue_TopUpActivity.class);
					call_webview.putExtra(AvenuesParams.ACCESS_CODE, cca_access_code);
					call_webview.putExtra(AvenuesParams.ENC_VAL, cca_enc_req);
					call_webview.putExtra(AvenuesParams.MERCHANT_ID, cca_merchant_id);
					call_webview.putExtra(AvenuesParams.WORKING_KEY, cca_working_key);
					call_webview.putExtra(AvenuesParams.AMOUNT, txtnewamount.getText().toString());
					call_webview.putExtra("TrackId", TrackId);					
					call_webview.putExtra(AvenuesParams.CCA_REQUEST, cca_request);
					call_webview.putExtra("additional_amount",additionalAmount );
					call_webview.putExtra("Changepack", Changepack);
					call_webview.putExtra("UpdateFrom", UpdateFrom);
					call_webview.putExtra("PackageName", txtnewpackagename.getText().toString());
					call_webview.putExtra("Topup_Validity", Topup_Validity);
					call_webview.putExtra("speed", speed);
					call_webview.putExtra("data", data);
					call_webview.putExtra("paidAmount",txtnewamount.getText().toString());
					call_webview.putExtra("is_topup", true);
					
					startActivity(call_webview);
					
					
				}
				else{
					iError.display();
					//showExitAlert("Please try Again \n  We are unable to process your request", MakeMyPaymentsTopUp_CCAvenue.this);
				}
			}
			else{
				iError.display();
				//showExitAlert("Please try Again \n  We are unable to process your request", MakeMyPaymentsTopUp_CCAvenue.this);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			iError.display();
			//showExitAlert("Please try Again \n  We are unable to process your request", MakeMyPaymentsTopUp_CCAvenue.this);
		}
		
	}
	 public  void showExitAlert(String message, Context ctx){
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
			
			TextView	dtv = (TextView) dialog.findViewById(R.id.tv1);

			TextView txt = (TextView) dialog.findViewById(R.id.tv);

			txt.setText(Html.fromHtml(message));

			Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

			
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MakeMyPaymentsTopUp_CCAvenue.this.finish();
					dialog.dismiss();
				}
			});
		
			dialog.show();
			//(width/2)+((width/2)/2)
			//dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
			dialog.getWindow().setLayout((width/2)+(width/2)/2, LayoutParams.WRAP_CONTENT);		
	   }
	 
	 
}
