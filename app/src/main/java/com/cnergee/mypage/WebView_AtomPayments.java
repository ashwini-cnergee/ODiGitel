package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.caller.AfterInsertPaymentsCaller;
import com.cnergee.mypage.caller.RenewalCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.FinishEvent;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cnergee.krishna.ookla.odigitel.R;

public class WebView_AtomPayments extends Activity {

	PaymentsObj paymentsObj = new PaymentsObj();
	WebView webView;
	StringBuffer str_param = new StringBuffer();
	String atom_URL, atom_track_id, atom_error, atom_access_code, statusMsg;
	// additionalAmount;
	String payamount;
	String order_id, amount, bank_trans;
	String Atom_Trans_Status = "";
	AdditionalAmount additionalAmount;
	String type = "Renew";
	Utils utils = new Utils();
	String[] json;
	ProgressDialog pd;
	//String TrackId;
	public boolean Changepack;
	public static String rslt = "", responseMsg = "";

	private boolean flag = true;
	String Authcode_id, bank_refrence, atoms_status, PackageName = "",	UpdateFrom = "";;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atom_webview);
		webView = (WebView) findViewById(R.id.atom_web_view);

		Utils.log("Curent class", "" + this.getClass());
		Intent geti = getIntent();
		atom_URL = geti.getStringExtra("returnUrl");
		atom_error = geti.getStringExtra("Error");
		atom_track_id = geti.getStringExtra("trackId");
		atom_access_code = geti.getStringExtra("AccessCode");
		Changepack= geti.getBooleanExtra("Changepack",false);
		UpdateFrom= geti.getStringExtra("UpdateFrom");
		// payamount = geti.getDoubleExtra("additional_amount",0);
		// additionalAmount = geti.getStringExtra("additional_amount");
		PackageName=geti.getStringExtra("PackageName");
		additionalAmount = (AdditionalAmount) geti
				.getSerializableExtra("additional_amount");

		Utils.log("WebAdditional Amount", "is:" + additionalAmount.getFinalcharges());
		Utils.log("atom_track_id", "is:" + atom_track_id);
		Utils.log("WebAmount", "is:" + additionalAmount.getPackageRate());
		Utils.log("WebAmount", "is:" + additionalAmount.getDaysFineAmount());
		//Utils.log("WebAmount", "is:" + additionalAmount.toString());
		//	Utils.log("WebAmount", "is:" + geti.getSerializableExtra("additional_amount"));
		Utils.log("TrackId", "is:" + atom_track_id);
		Utils.log("AccessCode", "" + atom_access_code);
		Utils.log("Error", "" + atom_error);

		String	sharedPreferences_name = getString(R.string.shared_preferences_name);
		SharedPreferences sharedPreferences = getApplicationContext()
				.getSharedPreferences(sharedPreferences_name, 0); // 0 - for
		// private
		// mode

		utils.setSharedPreferences(sharedPreferences);
/*
		paymentsObj.setTxId(atom_track_id);
		paymentsObj.setTxStatus(atoms_status);*/
		Utils.log("On Create Proge", "DialogOnCr");
		pd = ProgressDialog.show(WebView_AtomPayments.this, "",
				"Loading. Please wait...", true);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");


		WebSettings zoomenable = webView.getSettings();
		zoomenable.setBuiltInZoomControls(true);
		WebSettings wideviewenable = webView.getSettings();
		wideviewenable.setUseWideViewPort(true);
		//	zoomenable.setDisplayZoomControls(true);
		webView.setWebViewClient(new WebViewClient() {



			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				Toast.makeText(getApplicationContext(),
						"Oh no! " + description, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				Utils.log("OnProgressDialog","On Dialog");
				if(pd!=null){
					if(pd.isShowing()){

					}
					else{
						//pd=null;
						pd = ProgressDialog.show(WebView_AtomPayments.this, "",
								"Loading. Please wait...", true);
					}

				}
				else{
					pd = ProgressDialog.show(WebView_AtomPayments.this, "",
							"Loading. Please wait...", true);
				}
				Utils.log("onPageStarted url", "is:" + url);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(webView, url);
				Utils.log("dismiss","dismiss");
				if(pd!=null){
					if (pd.isShowing()) {
						pd.dismiss();
						pd=null;
						Utils.log("dismiss","dismiss");

					}


				}

				Utils.log("OnPageFinish", "is");
				// for payment uRl
				if (url.indexOf("/PaymentStatusTransMobile.aspx") != -1) {
					// if(url.indexOf("google.co.in")!=-1){
					Utils.log("On Url Index","url Index Executed");
					webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
					// webView.loadUrl(url);
					Utils.log("Response url", "is:" + url);
				}
				Utils.log("2dismiss","2dismiss");
				Utils.log("Current URL",""+url);
				Utils.log("atom_URL", ":"+atom_URL);
			}
		});

		try {

			Utils.log("On Load RecieveURL", "Recieved URL");
			Utils.log("atom_URL", ":"+atom_URL);

			webView.loadUrl(atom_URL);
			Utils.log("atom_URL", ":"+atom_URL);


			// Utils.log("Response",""+url);
			Utils.log("load", "url" + webView.getUrl());
			//Utils.log("Loading Url on page finish",":"+(webView.loadUr l(atom_URL)));
			Utils.log("OnfinshLoading",":"+webView.getUrl());

			//	Utils.log("atom response url",""+webView.getHitRect());


		} catch (Exception e) {
			// showToast("Exception occured while opening webview.");

			Utils.log("Exception", "is:" + e);
		}

	}

	@Override
	public void onBackPressed() {

		final Dialog dialog = new Dialog(WebView_AtomPayments.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.transaction_response);
		/*
		 * Display display = ((Activity)ctx).
		 * getWindowManager().getDefaultDisplay(); Point size = new Point();
		 * display.getSize(size);*/


		int width = 0;
		int height = 0;
		dialog.setCancelable(false);

		Point size = new Point();
		WindowManager w = ((Activity) WebView_AtomPayments.this)
				.getWindowManager();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			w.getDefaultDisplay().getSize(size);
			width = size.x;
			height = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			width = d.getWidth();
			height = d.getHeight();

		}

		TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
		TextView txtmsg = (TextView) dialog.findViewById(R.id.tvMessage);

		txtmsg.setText("Are you sure you want to cancel the transaction");

		Button btnUpdate = (Button) dialog.findViewById(R.id.btnyes);
		Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);

		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();

				atoms_status = "onbackpressed";

				Utils.log("Yes atom_track_id", "" + atom_track_id);
				Utils.log("status", "" + atoms_status);
				Utils.log("Curent class", "" + this.getClass());

				paymentsObj.setTxId(atom_track_id);
				paymentsObj.setTxStatus(atoms_status);
				
				/*webView.clearHistory();
				webView.clearCache(isFinishing());*/
				//finish();



				Utils.log("WebAdditional Amount",
						"" + additionalAmount.getFinalcharges());
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					new InsertAfterPayemnt().executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
					Utils.log("InsertPayment", "Insert Pay executed");
				} else {
					new InsertAfterPayemnt().execute();
					Utils.log("InsertPayment", "Insert Pay executed");
				}
				//atoms_status = "on back pressed";





			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});

		dialog.show();
		// (width/2)+((width/2)/2)
		// dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
				LayoutParams.WRAP_CONTENT);

		return;
	}

/*	public void showExitAlert(String message, Context ctx) {

		Utils.log("show alert", "alert");
		final Dialog dialog = new Dialog(ctx);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.custom_dialog_box);
		
		 * Display display = ((Activity)ctx).
		 * getWindowManager().getDefaultDisplay(); Point size = new Point();
		 * display.getSize(size);
		 
		int width = 0;
		int height = 0;
		dialog.setCancelable(false);

		Point size = new Point();
		WindowManager w = ((Activity) ctx).getWindowManager();

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

		TextView dtv = (TextView) dialog.findViewById(R.id.tv1);

		TextView txt = (TextView) dialog.findViewById(R.id.tv);

		txt.setText(Html.fromHtml(message));

		Button dialogButton = (Button) dialog.findViewById(R.id.btnSubmit);

		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				WebView_AtomPayments.this.finish();
				Intent i = new Intent(WebView_AtomPayments.this,
						RenewPackage.class);
				dialog.dismiss();
				startActivity(i);

				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);

			}
		});

		dialog.show();
		// (width/2)+((width/2)/2)
		// dialog.getWindow().setLayout((width/2)+((width/2)/2), height/2);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
				LayoutParams.WRAP_CONTENT);
	}*/

	class MyJavaScriptInterface {
		@JavascriptInterface
		public void processHTML(String html) {
			// process the html as needed by the app
			// System.out.println(html);
			String[] actual_response = null;
			Utils.log("HTML", "is" + html);
			if (html != null) {
				if (html.length() > 0) {
					String s = html;

					Utils.log("Before", "Split" + s);

					actual_response = TextUtils.split(html, "S\\@gar123\\$");

					Utils.log("actual response",""+actual_response);
					Utils.log("Length", "is:" + actual_response.length);
					// Utils.log("Response","is"+actual_response[0]);
					Utils.log("Response", "is:" + actual_response[1]);

					parseResponseatom(actual_response[1]);
					Utils.log("Parse Executed", "parse Executed");
				}
			}

			/*
			 * if(html.indexOf("Failure")!=-1){ Atom_Trans_Status = "DECLINE";
			 * }else if(html.indexOf("Success")!=-1){ Atom_Trans_Status =
			 * "SUCCESS"; }else if(html.indexOf("Aborted")!=-1){
			 * Atom_Trans_Status = "CANCELLED"; }else{ Atom_Trans_Status =
			 * "UNKNOWN"; }
			 */
			// parseCCAvenueResponse(actual_response[1], Atom_Trans_Status);

			json = actual_response;

			Utils.log("Status", "is:" + Atom_Trans_Status);
			Utils.log("Json", "" + actual_response[1]);

			// Utils.log("Response","  is:"+ actual_response[1]);
			// String status = null;
			/*
			 * if(html.indexOf("Failure")!=-1){ atom_error = "1"; }else
			 * if(html.indexOf("Success")!=-1){ CCAvenue_Trans_Status =
			 * "SUCCESS"; }else if(html.indexOf("Aborted")!=-1){
			 * CCAvenue_Trans_Status = "CANCELLED"; }else{ CCAvenue_Trans_Status
			 * = "UNKNOWN"; }
			 */

			/*
			 * parseCCAvenueResponse(actual_response[1], CCAvenue_Trans_Status);
			 * 
			 * Utils.log("Status","is"+CCAvenue_Trans_Status);
			 * 
			 * if(actual_response.length>2)
			 * parseCCAvenueResponse(actual_response[1], CCAvenue_Trans_Status);
			 * else{ Intent intent = new
			 * Intent(getApplicationContext(),TransResponseCCAvenue.class);
			 * intent.putExtra("transStatus",CCAvenue_Trans_Status);
			 * startActivity(intent); }
			 */

			// Toast.makeText(getApplicationContext(), status,
			// Toast.LENGTH_SHORT).show();

		}
	}

	private void parseResponseatom(String json) {
		try {

			Utils.log("onParse", "parse");

			Utils.log("JsonOnParse", "" + json);

			JSONObject mainjson = new JSONObject(json);

			order_id = mainjson.getString("mmp_txn");
			Authcode_id = mainjson.getString("mer_txn");
			amount = mainjson.getString("amt");
			Atom_Trans_Status = mainjson.getString("f_code");
			// bank_refrence = mainjson.getString("bank_name");
			bank_trans = mainjson.getString("bank_txn");



			Utils.log("Authcode", "" + Authcode_id); // TransactionReference no.
			Utils.log("Order id", "" + order_id); // Transaction Number.
			Utils.log("amount", "" + amount); // amt
			Utils.log("Atom_Trans_Status is f_code", "" + Atom_Trans_Status); // Txn status.
			Utils.log("Status", "" + atoms_status); // Txn status.
			Utils.log("bank_trans", "" + bank_trans); // Pg Txn no.
			Utils.log("TarckId", "" + atom_track_id);


			// Atom_Trans_Status = "OK";

			if (Atom_Trans_Status.equalsIgnoreCase("OK")) {

				atoms_status = "SUCCESS";
				Utils.log("OnSuccess", "" + atoms_status);
				Utils.log("OnSuccess", "" + Atom_Trans_Status);
				statusMsg = " Transaction SUCCESS";

			} else {
				atoms_status = "Failed";
				statusMsg = " Transaction Failed";
				Utils.log("Onfailed", "" + atoms_status);
			}
			Utils.log("atomstatus", "" + atoms_status);
			/*
			 * paymentsObj.setAuthIdCode(order_id);
			 * paymentsObj.setTxStatus(atoms_status);
			 * paymentsObj.setTxMsg(atoms_status);
			 * //paymentsObj.setTxId(Authcode_id);
			 * paymentsObj.setTxId(atom_track_id);
			 * paymentsObj.setIssuerRefNo(bank_trans);
			 * paymentsObj.setAmount(amount);
			 */

			paymentsObj.setAuthIdCode(order_id);
			paymentsObj.setTxStatus(atoms_status);
			paymentsObj.setTxMsg(atoms_status);
			paymentsObj.setTxId(atom_track_id);
			paymentsObj.setIssuerRefNo(bank_trans);
			paymentsObj.setAmount(amount);

			// paymentsObj.setTrackId(atom_track_id);

			Utils.log("TrackId", "" + atom_track_id);
			Utils.log("Authcode", "" + Authcode_id);
			Utils.log("Order id", "" + order_id);
			Utils.log("Parse amount", "" + amount);
			Utils.log("Atom_Trans_Status", "" + atoms_status);
			Utils.log("Status", "" + atoms_status);
			Utils.log("bank_trans", "" + bank_trans);
			Utils.log("TrackId", "" + atom_track_id);

			// / amount is hardcorded

			payamount = amount;

			Utils.log("**************", "***********************");
			Utils.log("Authcode", "" + Authcode_id);
			Utils.log("Order id", "" + order_id);
			Utils.log("Parse amount", "" + amount);
			Utils.log("Atom_Trans_Status", "" + Atom_Trans_Status);
			Utils.log("Status", "" + atoms_status);
			Utils.log("bank_trans", "" + bank_trans);
			Utils.log("TrackId", "" + atom_track_id);

			Utils.log("Payment Authcode", "" + paymentsObj.getAuthIdCode());
			Utils.log("Payment TransactionId", "" + paymentsObj.getTrackId());
			Utils.log("Payment TXId", "" + paymentsObj.getTxId());
			Utils.log("Payment status", "" + paymentsObj.getTxStatus());

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				new InsertAfterPayemnt().executeOnExecutor(
						AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
				Utils.log("InsertPayment", "Insert Pay executed");
			} else {
				new InsertAfterPayemnt().execute();
				Utils.log("InsertPayment", "Insert Pay executed");
			}

		} catch (JSONException e) {

			Utils.log("Error", "" + e);

		} catch (Exception e) {
			// TODO: handle exception
			Utils.log("Error", "" + e);
		}

	}

	// for insertAfterpayment

	public class InsertAfterPayemnt extends AsyncTask<String, Void, Void>
			implements OnCancelListener {

		ProgressHUD mProgressHUD;

		protected void onPreExecute() {

			Utils.log("Started", " InsertAfterPayment");
			mProgressHUD = ProgressHUD
					.show(WebView_AtomPayments.this,
							getString(R.string.app_please_wait_label), true,
							true, this);

		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();

			// submit.setClickable(true);
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		protected void onPostExecute(Void unused) {

			mProgressHUD.dismiss();
			// submit.setClickable(true);
			Utils.log("InsertPayment OnPost", "Insert Pay executed ONPost");

			Utils.log("additional amnt", "" + additionalAmount.getFinalcharges());
			Utils.log("Result", "" + rslt);

			// String rslt = "ok";

			Utils.log("OnPostOf InsertPayment", "InsertPayment");

			Utils.log("Result", "" + rslt);

			if (rslt.trim().equalsIgnoreCase("ok")) {
				// Utils.log("Now in renew",":"+additionalAmount.getAdditionalAmountType());
				Utils.log("additional amnt",
						"" + additionalAmount.getFinalcharges());

				Utils.log("On Post InsertAfter",
						"atoms_status is:" +atoms_status );
				if (atoms_status.equals("SUCCESS")) {
					if (additionalAmount.getFinalcharges() != null) {
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
						Utils.log("Additional Amount ", "is null");
					}

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {new RenewalProcessWebService().executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
					} else {
						new RenewalProcessWebService().execute();
					}
					// Utils.log("Additional Amount", "" +
					// additionalAmount.getFinalcharges());
				} else {
					/*
					webView.clearHistory(); // clear history
					WebView_AtomPayments.this.finish();
					*/
					WebView_AtomPayments.this.finish();
					Intent intent = new Intent(getApplicationContext(), TransResponseAtom.class);

					intent.putExtra("Amount", amount);
					intent.putExtra("statusMsg", statusMsg);
					intent.putExtra("transStatus", atoms_status);
					intent.putExtra("order_id", "" + order_id);
					// intent.putExtra("AuthCode", Authcode_id);
					intent.putExtra("Amount", additionalAmount.getFinalcharges());
					intent.putExtra("order_status", atoms_status);
					intent.putExtra("bank_ref_no", bank_trans);
					intent.putExtra("Track_id", atom_track_id);

					// Utils.log("amount",amount);
					Utils.log("tracking_id", "" + Authcode_id);
					Utils.log("Order id", "" + order_id);
					Utils.log("amount", "" + amount);
					Utils.log("statusMsg", "" + statusMsg);
					Utils.log("Status", "" + atoms_status);
					Utils.log("bank_trans", "" + bank_trans);
					Utils.log("TrackId", "" + atom_track_id);

					startActivity(intent);
					BaseApplication.getEventBus().post(new FinishEvent(MakeMyPaymentsAtom.class.getSimpleName()));
					BaseApplication.getEventBus().post(new FinishEvent(RenewPackage.class.getSimpleName()));
					BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));
					// MakeMyPayments.this.finish();

					/*
					 * intent.putExtra("Amount",amount);
					 * intent.putExtra("statusMsg",statusMsg);
					 * intent.putExtra("transStatus", atoms_status);
					 * intent.putExtra("order_id", order_id);
					 * intent.putExtra("tracking_id", Authcode_id);
					 * //intent.putExtra("amount",
					 * additionalAmount.getFinalcharges());
					 * intent.putExtra("Amount",
					 * additionalAmount.getFinalcharges());
					 * intent.putExtra("order_status", atoms_status);
					 * intent.putExtra("bank_ref_no",bank_trans);
					 */
				}

			} else {
				AlertsBoxFactory.showAlert(rslt, WebView_AtomPayments.this);
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
								R.string.METHOD_AFTER_MEMBER_PAYMENTS),
						true);

				Utils.log("Payment Authcode", "" + paymentsObj.getAuthIdCode());
				Utils.log("Payment TransactionId", "" + paymentsObj.getTrackId());
				Utils.log("Payment TXId", "" + paymentsObj.getTxId());

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

	private class RenewalProcessWebService extends
			AsyncTask<String, Void, Void> implements OnCancelListener {

		ProgressHUD mProgressHUD;
		PaymentsObj paymentsObj = new PaymentsObj();

		protected void onPreExecute() {

			Utils.log("OnRenewalExecuted", "OnRenewalExe");
			mProgressHUD = ProgressHUD
					.show(WebView_AtomPayments.this,
							getString(R.string.app_please_wait_label), true,
							false, this);
		}

		@Override
		protected void onCancelled() {
			mProgressHUD.dismiss();
			// submit.setClickable(true);
		}

		protected void onPostExecute(Void unused) {

			 mProgressHUD.dismiss();
			// submit.setClickable(true);

			if (rslt.trim().equalsIgnoreCase("ok")) {
				// finish();

				WebView_AtomPayments.this.finish();;

				Intent intent = new Intent(getApplicationContext(), TransResponseAtom.class);
				intent.putExtra("Amount", amount);
				intent.putExtra("statusMsg", statusMsg);
				intent.putExtra("transStatus", atoms_status);
				intent.putExtra("order_id", "" + order_id);
				// intent.putExtra("AuthCode", Authcode_id);
				intent.putExtra("Amount", additionalAmount.getFinalcharges());
				intent.putExtra("order_status", atoms_status);
				intent.putExtra("bank_ref_no", bank_trans);
				intent.putExtra("Track_id", atom_track_id);

				Utils.log("amount", "" + amount);
				Utils.log("Amount", "" + additionalAmount.getFinalcharges());
				Utils.log("status Message", "" + statusMsg);
				Utils.log("transaction Status", "" + atoms_status);
				Utils.log("Order ID", "" + order_id);
				Utils.log("bank Reffrence no", "" + bank_trans);
				Utils.log("Order Status", "" + atoms_status);
				Utils.log("AuthCode", "" + Authcode_id);

				startActivity(intent);
				BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));
				BaseApplication.getEventBus().post(new FinishEvent(MakeMyPaymentsAtom.class.getSimpleName()));
				BaseApplication.getEventBus().post(new FinishEvent(ChangePackage.class.getSimpleName()));
				BaseApplication.getEventBus().post(new FinishEvent(RenewPackage.class.getSimpleName()));
				/*
				 * Intent intent = new
				 * Intent(MakeMyPayments.this,IONHome.class);
				 * startActivity(intent);
				 * 
				 * intent.putExtra("transStatus", atoms_status);
				 * intent.putExtra("order_id", order_id);
				 * intent.putExtra("tracking_id", Authcode_id);
				 * //intent.putExtra("amount",
				 * additionalAmount.getFinalcharges());
				 * intent.putExtra("amount", additionalAmount);
				 * intent.putExtra("order_status", atoms_status);
				 * intent.putExtra("bank_ref_no",bank_trans);
				 */
			} else {
				AlertsBoxFactory.showAlert(rslt, WebView_AtomPayments.this);
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
				 * R.string.METHOD_MYPAGE_RENEWAL_PROCESS));
				 */
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
				paymentsObj.setPaidAmount(Double.parseDouble(additionalAmount
						.getFinalcharges()));
				paymentsObj.setTrackId(atom_track_id);
				// System.out.println("-------------Change Package :-----------"
				// + Changepack);
				Utils.log("Action Type", ":" + UpdateFrom);
				paymentsObj.setIsChangePlan(Changepack);
				paymentsObj.setActionType(UpdateFrom);
				paymentsObj.setPaymentId(Authcode_id);
				paymentsObj.setTxMsg(atoms_status);



				Utils.log("Payment Authcode", "" + paymentsObj.getAuthIdCode());
				Utils.log("Payment TransactionId", "" + paymentsObj.getTrackId());
				Utils.log("Payment TXId", "" + paymentsObj.getTxId());

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
			mProgressHUD.dismiss();
		}
	}

}
