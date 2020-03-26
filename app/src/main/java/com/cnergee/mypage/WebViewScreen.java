package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citruspay.citruspaylib.CitrusSSLLibrary;
import com.citruspay.citruspaylib.JsHandler;
import com.citruspay.citruspaylib.model.Address;
import com.citruspay.citruspaylib.model.Card;
import com.citruspay.citruspaylib.model.Customer;
import com.citruspay.citruspaylib.model.ExtraParams;
import com.citruspay.citruspaylib.utils.CitrusParams;
import com.citruspay.citruspaylib.utils.Constants;
import com.cnergee.mypage.utils.Utils;
import com.traction.ashok.util.KillProcess;

import org.apache.http.util.EncodingUtils;

import java.util.HashMap;
import java.util.Map;

import cnergee.krishna.ookla.odigitel.R;

public class WebViewScreen extends Activity {
	WebView _mwebView;
	protected int CURRENT_SDK_VERSION = Build.VERSION.SDK_INT;
	protected Map<String, String> jsonResponse;
	private final String TAG = getClass().getName();
	private JsHandler _jsHandler;
	public static Context context;
	private boolean isFinish = false;
	boolean isLogout = false;
	CitrusSSLLibrary citrusSSLLibrary;
	private Customer customer;

	private Address address;
	private Card cards;
	private ExtraParams extraParam;
	String fname, lname, email, streetAddress, city, state, country, pinCode,
			mobile, currencyValue, merchantTxnIdValue, orderAmountValue,
			returnUrlValue, hMacUrlValue, paymentMode, issuerCode,
			cardHolderName, cardNumber, expiryMonth, cardType, cvvNumber,
			expiryYear, _mHAC;
	ProgressDialog progressDialog = null;

	// ProgressBar progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_screen);
		init();
		initWebView(_mwebView);
		// requestToRemoteServer();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

	private void init() {
		// TODO Auto-generated method stub
		_mwebView = (WebView) findViewById(R.id.webView1);
		citrusSSLLibrary = new CitrusSSLLibrary();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

			customer = (Customer) extras
					.getSerializable(CitrusParams.PARAM_CUSTOMER);
			address = (Address) extras
					.getSerializable(CitrusParams.PARAM_ADDRESS);
			cards = (Card) extras.getSerializable(CitrusParams.PARAM_CARD);
			extraParam = (ExtraParams) extras
					.getSerializable(CitrusParams.PARAM_EXTRAS);
			Utils.log("Web View", "return:"+extraParam.getReturnUrl());
		}

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView(WebView _mWebView) {

		citrusSSLLibrary.setWebViewPropertiesForMerchantServer(
				WebViewScreen.this, _mWebView);
		progressDialog = new ProgressDialog(WebViewScreen.this);
		progressDialog.setTitle("Loading...");
		progressDialog
				.setMessage("Please wait. Redirecting to Payment Gateway.");
		progressDialog.setCancelable(false);
		progressDialog.show();

		_mwebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);

				if (newProgress < 100) {

				}

				if (newProgress == 100) {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					_mwebView.setVisibility(View.VISIBLE);
				}
			}

		});

		_mwebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				super.onReceivedSslError(view, handler, error);
			}
		});

		/*
		 * Kishor
		 */
		// get the data

		Bundle bundle = this.getIntent().getBundleExtra(
				"com.cnergee.make.payment.INTENT");
		// String strComplaintNo = bundle.getString("complaintNo");

		/**
		 * Method 1
		 * 
		 * @author gauravgupta This is HashMap calling for Parameters. Here user
		 *         can simply pass all key-pair value in Hashmap.
		 * */
		HashMap<String, String> _mAllParameters = new HashMap<String, String>();
		/*
		 * _mAllParameters.put(CitrusParams.PARAM_FIRST_NAME, "gaurav");
		 * _mAllParameters.put(CitrusParams.PARAM_LAST_NAME, "gupta");
		 */
		try {

			// Log.i(" >>>CHECK <<  ",bundle.getString("phoneNumber"));

			/*
			 * _mAllParameters.put(CitrusParams.PARAM_FIRST_NAME, "Gaurang");
			 * _mAllParameters.put(CitrusParams.PARAM_LAST_NAME, "Wala");
			 * _mAllParameters.put(CitrusParams.PARAM_PHONE_NUMBER,
			 * bundle.getString(CitrusParams.PARAM_PHONE_NUMBER));
			 * _mAllParameters.put(CitrusParams.PARAM_CURRENCY,
			 * bundle.getString(CitrusParams.PARAM_CURRENCY));
			 * _mAllParameters.put(CitrusParams.PARAM_ORDER_AMOUNT,
			 * bundle.getString(CitrusParams.PARAM_AMOUNT));
			 * _mAllParameters.put(
			 * CitrusParams.PARAM_HMAC,bundle.getString(CitrusParams
			 * .PARAM_SECSIGNATURE));
			 * _mAllParameters.put(CitrusParams.PARAM_MERCHANTTXNID
			 * ,bundle.getString(CitrusParams.PARAM_MERCHANTTXNID));
			 * _mAllParameters.put(CitrusParams.PARAM_RETURN_URL,
			 * bundle.getString(CitrusParams.PARAM_RETURN_URL));
			 */

			_mAllParameters.put(CitrusParams.PARAM_FIRST_NAME, "");
			_mAllParameters.put(CitrusParams.PARAM_LAST_NAME, "");
			_mAllParameters.put(CitrusParams.PARAM_PHONE_NUMBER,
					CitrusParams.PARAM_PHONE_NUMBER);
			_mAllParameters.put(CitrusParams.PARAM_CURRENCY,
					CitrusParams.PARAM_CURRENCY);
			_mAllParameters.put(CitrusParams.PARAM_ORDER_AMOUNT,
					CitrusParams.PARAM_AMOUNT);
			_mAllParameters.put(CitrusParams.PARAM_HMAC,
					CitrusParams.PARAM_SECSIGNATURE);
			_mAllParameters.put(CitrusParams.PARAM_MERCHANTTXNID,
					CitrusParams.PARAM_MERCHANTTXNID);
			_mAllParameters.put(CitrusParams.PARAM_RETURN_URL,
					CitrusParams.PARAM_RETURN_URL);

			// _mAllParameters.put(CitrusParams.PARAM_PHONE_NUMBER,
			// bundle.getString(CitrusParams.PARAM_PHONE_NUMBER));
			// _mAllParameters.put(CitrusParams.PARAM_CURRENCY,
			// bundle.getString(CitrusParams.PARAM_CURRENCY));
			// _mAllParameters.put(CitrusParams.PARAM_ORDER_AMOUNT,
			// bundle.getString(CitrusParams.PARAM_AMOUNT));
			// _mAllParameters.put(CitrusParams.PARAM_HMAC,
			// bundle.getString(CitrusParams.PARAM_SECSIGNATURE));
			// _mAllParameters.put(CitrusParams.PARAM_MERCHANTTXNID,bundle.getString(CitrusParams.PARAM_MERCHANTTXNID));
			// _mAllParameters.put(CitrusParams.PARAM_RETURN_URL,bundle.getString(CitrusParams.PARAM_RETURN_URL));
		} catch (NullPointerException n) {
			n.printStackTrace();
			_mAllParameters.put(CitrusParams.PARAM_PHONE_NUMBER, "9004094250");
			_mAllParameters.put(CitrusParams.PARAM_CURRENCY, "INR");
			_mAllParameters.put(CitrusParams.PARAM_ORDER_AMOUNT, "10.00");
			_mAllParameters.put(CitrusParams.PARAM_HMAC, "");
			_mAllParameters.put(CitrusParams.PARAM_MERCHANTTXNID, "");
			_mAllParameters
					.put(CitrusParams.PARAM_RETURN_URL,
							"https://sandbox.citruspay.com/demo/jsp/response_android.jsp");

		}

		// Log.i(" >>>_mAllParameters ------------ ",_mAllParameters.toString());
		String postData = citrusSSLLibrary
				.makeWebViewPostParameters(_mAllParameters);

		//Log.i(" >>> POST DATA ", postData);
		// Log.i(" >>> Next Line ",">>>>>>>>>>>>>>>>>>>");

		/*
		 * Log.i("****** ",customer.getEmail());
		 * Log.i("****** ",customer.getPhoneNumber());
		 * 
		 * 
		 * Log.i("******customer-------- ",customer.getEmail());
		 * Log.i("******customer-------- ",customer.getFirstName());
		 * Log.i("******customer-------- ",customer.getLastName());
		 * Log.i("******customer-------- ",customer.getPhoneNumber());
		 * 
		 * Log.i("******address-------- ",address.getAddressStreet1());
		 * Log.i("******address-------- ",address.getAddressCity());
		 * Log.i("******address-------- ",address.getAddressState());
		 * Log.i("******address-------- ",address.getAddressCountry());
		 * Log.i("******address-------- ",address.getAddressZip());
		 * 
		 * //Log.i("******cards-------- ",cards);
		 * Log.i("******extraParam-------- ",extraParam.getCurrency());
		 * //Log.i("******extraParam-------- ",extraParam.getHmacUrl());
		 * Log.i("******extraParam-------- ",extraParam.getHMACValue());
		 * Log.i("******extraParam-------- ",extraParam.getMerchantTxnId());
		 * Log.i("******extraParam-------- ",extraParam.getOrderAmountValue());
		 * Log.i("******extraParam-------- ",extraParam.getReturnUrl());
		 */

		String postData_2 = citrusSSLLibrary.makeWebViewPostParameters(
				customer, address, cards, extraParam).trim();
		 Utils.log(" >>> POST DATA 2 ",postData_2);
		 Utils.log(" >>> POST URL ",":"+Constants.MYCITRUS_SERVER_URL);
		 

		_mwebView.postUrl(Constants.MYCITRUS_SERVER_URL,
				EncodingUtils.getBytes(postData_2, "UTF-8"));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isLogout) {
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			KillProcess kill = new KillProcess(context, am);
			kill.killAppsProcess();
		} else {
			System.runFinalizersOnExit(true);
		}

		if(progressDialog!=null&&progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		//if (!isFinish)
		//	finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
