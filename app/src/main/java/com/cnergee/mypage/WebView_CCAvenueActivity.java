package com.cnergee.mypage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.avenues.lib.utility.AvenuesParams;
import com.avenues.lib.utility.Constants;
import com.avenues.lib.utility.RSAUtility;
import com.avenues.lib.utility.ServiceUtility;
import com.cnergee.mypage.caller.AfterInsertPaymentsCaller;
import com.cnergee.mypage.caller.RenewalCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import cnergee.krishna.ookla.odigitel.R;



public class WebView_CCAvenueActivity extends Activity {

	WebView wvClient;
	StringBuffer str_param=new StringBuffer();
	String vResponse="",encVal="";
	public static String rslt="",responseMsg="";
	AdditionalAmount additionalAmount;
	String CCAvenue_Trans_Status="";
	String type="Renew";
	PaymentsObj paymentsObj = new PaymentsObj();
	Utils utils = new Utils();
	public  boolean Changepack;
	public String UpdateFrom="",PackageName="";
	String TrackId="";
	String AuthIdCode="",bank_ref_no="";

	public  boolean is_activity_running=true,is_exit_webview=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_screen);
		
		wvClient=(WebView) findViewById(R.id.webView1);
		
		//wvClient.loadUrl("https://www.google.co.in");
		
		Intent lastIntent= getIntent();
		
		String	cca_req=lastIntent.getStringExtra(AvenuesParams.CCA_REQUEST);
		
		Utils.log(AvenuesParams.ACCESS_CODE, lastIntent.getStringExtra(AvenuesParams.ACCESS_CODE));
		Utils.log(AvenuesParams.ENC_VAL, lastIntent.getStringExtra(AvenuesParams.ENC_VAL));
		Utils.log(AvenuesParams.MERCHANT_ID, lastIntent.getStringExtra(AvenuesParams.MERCHANT_ID));		
		Utils.log(AvenuesParams.WORKING_KEY, lastIntent.getStringExtra(AvenuesParams.WORKING_KEY));		
		Utils.log("TrackId", lastIntent.getStringExtra("TrackId"));	
		Utils.log("cca req", cca_req);
		
	String	sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
																	// private
																	// mode
		
		utils.setSharedPreferences(sharedPreferences);
		vResponse=lastIntent.getStringExtra(AvenuesParams.ENC_VAL);
		additionalAmount = (AdditionalAmount) lastIntent.getSerializableExtra("additional_amount");
	
		Changepack=lastIntent.getBooleanExtra("Changepack", true);
		TrackId= lastIntent.getStringExtra("TrackId");
		UpdateFrom= lastIntent.getStringExtra("UpdateFrom");
		PackageName= lastIntent.getStringExtra("PackageName");
		str_param.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,lastIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
		str_param=str_param.append(cca_req);
		str_param.append(ServiceUtility.addToPostParams(AvenuesParams.LANGUAGE,"EN"));	
		
		if(!ServiceUtility.chkNull(vResponse).equals("") 
				&& ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
			StringBuffer vEncVal = new StringBuffer("");
			
			vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, lastIntent.getStringExtra(AvenuesParams.AMOUNT)));
			vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, "INR"));			
			encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);
			
		}
		
		if(encVal!=null)
		str_param.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL,URLEncoder.encode(encVal)));
		else{
			MakeMyPayments_CCAvenue.showExitAlert("We are facing technical problem \n We will fix it shortly.", WebView_CCAvenueActivity.this);
		}
		Utils.log("URL", "is:"+Constants.TRANS_URL);
		Utils.log("Request to be", "sent:"+str_param);
		Utils.log("ACTION TYPE", "is:"+UpdateFrom);
		wvClient.getSettings().setJavaScriptEnabled(true);
		wvClient.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		wvClient.setWebViewClient(new WebViewClient(){
			ProgressHUD progressHUD;
			@Override  
    	    public void onPageFinished(WebView view, String url) {
    	        super.onPageFinished(wvClient, url);
    	      //  if(is_activity_running)
    	       // progressHUD.dismiss();
    	        if(url.indexOf("/ccavResponseHandlerMobile.aspx")!=-1){
    	        	wvClient.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    	        }
    	    }  

			
    	    @Override
    	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	        Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
    	    }
    	    @Override
    	    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    	    	// TODO Auto-generated method stub
    	    	super.onPageStarted(view, url, favicon);
    	    	//if(is_activity_running)
    	    	//progressHUD=ProgressHUD.show(WebView_CCAvenueActivity.this, "Please do not press \n back button or refresh. ", false, false, null);
    	    }
		});
		
		String vPostParams = str_param.substring(0,str_param.length()-1);
		Utils.log("Last Param",":"+vPostParams);
		try {
			Utils.log("BEFORE LOAD URL", ":"+Constants.TRANS_URL);
			wvClient.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
			
			Utils.log("load","url"+wvClient.getUrl());
		} catch (Exception e) {
			//showToast("Exception occured while opening webview.");
			
			Utils.log("Exception","is:"+e);
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		is_activity_running=true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		is_activity_running=false;
	}
	
	class MyJavaScriptInterface
	{
		@JavascriptInterface
	    public void processHTML(String html)
	    {
	        // process the html as needed by the app
	    	//System.out.println(html);
			String[] actual_response = null;
			Utils.log("HTML","is"+html);
			if(html!=null){
				if(html.length()>0){
					String s=html;

					Utils.log("Before","Split"+s);
				
					actual_response=TextUtils.split(html, "S\\@gar123\\$");
					
					Utils.log("Length","is:"+actual_response.length);
				}
				
			}
			
			
						
			//Utils.log("Response","  is:"+ actual_response[1]);
			//String status = null;
			if(actual_response!=null){
				if(actual_response.length>1){
			    	if(html.indexOf("Failure")!=-1){
			    		CCAvenue_Trans_Status = "DECLINE";
			    	}else if(html.indexOf("Success")!=-1){
			    		CCAvenue_Trans_Status = "SUCCESS";
			    	}else if(html.indexOf("Aborted")!=-1){
			    		CCAvenue_Trans_Status = "CANCELLED";
			    	}else{
			    		CCAvenue_Trans_Status = "UNKNOWN";
			    	}
			    	
			    	//Utils.log("Actual Response", "is"+actual_response[1]);
			    	parseCCAvenueResponse(actual_response[1], CCAvenue_Trans_Status);
			    	
			    	Utils.log("Status","is"+CCAvenue_Trans_Status);
				}
				else{
					is_exit_webview=true;
				}
			}
			else{
				is_exit_webview=true;
			}
	    	/*
	    	if(actual_response.length>2)
	    		parseCCAvenueResponse(actual_response[1], CCAvenue_Trans_Status);
	    	else{
	    		Intent intent = new Intent(getApplicationContext(),TransResponseCCAvenue.class);
				intent.putExtra("transStatus",CCAvenue_Trans_Status);
				startActivity(intent);
	    	}*/
	    		
	    	//Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
	    	
	    }
	}

	public static String[] commaDelimitedListToStringArray(String str, String escapeChar) {
	    // these characters need to be escaped in a regular expression
	    String regularExpressionSpecialChars = "/.*+?|()[]{}\\<>";

	    String escapedEscapeChar = escapeChar;

	    // if the escape char for our comma separated list needs to be escaped 
	    // for the regular expression, escape it using the \ char
	    if(regularExpressionSpecialChars.indexOf(escapeChar) != -1) 
	        escapedEscapeChar = "\\" + escapeChar;

	    // see http://stackoverflow.com/questions/820172/how-to-split-a-comma-separated-string-while-ignoring-escaped-commas
	    String[] temp = str.split("(?<!" + escapedEscapeChar + "),", -1);

	    // remove the escapeChar for the end result
	    String[] result = new String[temp.length];
	    for(int i=0; i<temp.length; i++) {
	        result[i] = temp[i].replaceAll(escapedEscapeChar + ",", ",");
	    }

	    return result;
	}
	
	public void parseCCAvenueResponse(String json,String status){
		try {
			if(json!=null){
				if(json.length()>0){
					JSONObject mainJson= new JSONObject(json);
					
					String order_id=mainJson.getString("order_id");
					AuthIdCode=mainJson.getString("tracking_id");
					String amount=mainJson.getString("amount");
					String order_status=mainJson.getString("order_status");
					bank_ref_no=mainJson.getString("bank_ref_no");
					
					
					paymentsObj.setAuthIdCode(AuthIdCode);
					paymentsObj.setTxId(order_id);
					paymentsObj.setTxStatus(order_status);
					paymentsObj.setPgTxnNo(bank_ref_no);
					paymentsObj.setIssuerRefNo(bank_ref_no);
					paymentsObj.setTxMsg(CCAvenue_Trans_Status);
					
					
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new InsertAfterPayemnt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
					}
					else{
						new InsertAfterPayemnt().execute();
					}
					
					/*Intent intent = new Intent(getApplicationContext(),TransResponseCCAvenue.class);
					intent.putExtra("transStatus",status);
					intent.putExtra("order_id",order_id);
					intent.putExtra("tracking_id",tracking_id);
					intent.putExtra("amount",amount);
					intent.putExtra("order_status",order_status);
					startActivity(intent);*/
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/* Insert Before Going to Payment Gateway */

	private class InsertAfterPayemnt extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;
		

		protected void onPreExecute() {

			Utils.log("Started", " InsertAfterPayment");
			mProgressHUD = ProgressHUD
					.show(WebView_CCAvenueActivity.this,
							getString(R.string.app_please_wait_label), true,
							true, this);

		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			
			// submit.setClickable(true);
		}

		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			// submit.setClickable(true);
			
			is_exit_webview=true;
			if (rslt.trim().equalsIgnoreCase("ok")) {
				 Utils.log("CCAVENUE RESPONSE",":"+CCAvenue_Trans_Status);
				if (CCAvenue_Trans_Status.equals("SUCCESS")) {
			
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
					
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						new RenewalProcessWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String)null);
					}
					else{
						new RenewalProcessWebService().execute();
					}
				
					
				} else {
					
					WebView_CCAvenueActivity.this.finish();

					Intent intent = new Intent(getApplicationContext(),TransResponseCCAvenue.class);
					intent.putExtra("transStatus",CCAvenue_Trans_Status);
					intent.putExtra("order_id",TrackId);
					intent.putExtra("tracking_id",AuthIdCode);
					intent.putExtra("amount",additionalAmount.getFinalcharges());
					intent.putExtra("order_status",CCAvenue_Trans_Status);
					startActivity(intent);					
					// MakeMyPayments.this.finish();
				}

			} else {
				AlertsBoxFactory.showAlert(rslt, WebView_CCAvenueActivity.this);
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

			mProgressHUD.dismiss();

		}
	}
	
	
	/* For Renewal Process */

	private class RenewalProcessWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener {

		//ProgressHUD mProgressHUD;
		ProgressDialog progressDialog;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {

		/*	if(mProgressHUD==null){
			mProgressHUD = ProgressHUD
					.show(WebView_CCAvenueActivity.this,
							getString(R.string.app_please_wait_label), true,
							true, this);
			}*/
			
			/*progressDialog= new ProgressDialog(WebView_CCAvenueActivity.this);
			progressDialog.show();
			progressDialog.setCancelable(false);*/
		}

		@Override
		protected void onCancelled() {
			//mProgressHUD.dismiss();
			//progressDialog.dismiss();
			// submit.setClickable(true);
		}

		protected void onPostExecute(Void unused) {

			//mProgressHUD.dismiss();
			// submit.setClickable(true);
			//progressDialog.dismiss();

			if (rslt.trim().equalsIgnoreCase("ok")) {
				// finish();
				WebView_CCAvenueActivity.this.finish();
				
				Intent intent = new Intent(getApplicationContext(),TransResponseCCAvenue.class);
				intent.putExtra("transStatus",CCAvenue_Trans_Status);
				intent.putExtra("order_id",TrackId);
				intent.putExtra("tracking_id",AuthIdCode);
				intent.putExtra("amount",additionalAmount.getFinalcharges());
				intent.putExtra("order_status",CCAvenue_Trans_Status);
				intent.putExtra("bank_ref_no",bank_ref_no);
				startActivity(intent);
				
				
				/*
				 * Intent intent = new
				 * Intent(MakeMyPayments.this,IONHome.class);
				 * startActivity(intent);
				 */
			} else {
				AlertsBoxFactory.showAlert(rslt, WebView_CCAvenueActivity.this);
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
				paymentsObj.setPlanName(PackageName);
				paymentsObj.setPaidAmount(Double.parseDouble(additionalAmount.getFinalcharges()));
				paymentsObj.setTrackId(TrackId);
				// System.out.println("-------------Change Package :-----------"
				// + Changepack);
				Utils.log("Action Type",":"+UpdateFrom);
				paymentsObj.setIsChangePlan(Changepack);
				paymentsObj.setActionType(UpdateFrom);				
				paymentsObj.setPaymentId(AuthIdCode);
				paymentsObj.setTxMsg(CCAvenue_Trans_Status);

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
			//progressDialog.dismiss();
		}
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub{		
		if(is_exit_webview)
		{
			
		}
		return;
	}
	
	public void show_exit_dialog(){
		
	}
}
