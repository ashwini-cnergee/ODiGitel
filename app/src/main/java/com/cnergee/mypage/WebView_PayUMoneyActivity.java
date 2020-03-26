package com.cnergee.mypage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnergee.mypage.caller.AfterInsertPaymentsCaller;
import com.cnergee.mypage.caller.RenewalCaller;
import com.cnergee.mypage.obj.AdditionalAmount;
import com.cnergee.mypage.obj.PaymentsObj;
import com.cnergee.mypage.utils.AlertsBoxFactory;
import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cnergee.krishna.ookla.odigitel.R;


public class WebView_PayUMoneyActivity extends BaseActivity {

    WebView webView;

    String sccess_url ="",failure_url ="",merchant_key="",merchant_id= "",merchant_hash="",merchant_salt="",
            web_number="",web_emailId="",web_First_name="",web_track_id="",UpdateFrom="",PackageName="",PackageAmount="";

    String mHash="";

    String order_id, amount, bank_trans,Authcode_id,PayU_Trans_Status,trans_msg,Trans_id;
    String additional_amt_type;

    String type="Renew";
    Utils utils=new Utils();

    private String mBaseURL = "https://secure.payu.in";
    private String mServiceProvider = "payu_paisa";
    private String mProductInfo = "ODiGitel_Broadband";
    private String mAction = "";

    public boolean Changepack;
    ProgressDialog pd;
    public static String rslt = "", responseMsg = "";

    PaymentsObj paymentsObj = new PaymentsObj();

    String sharedPreferences_name;

    Context context;

    AdditionalAmount additionalAmount;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        context=getApplicationContext();

        pd = ProgressDialog.show(WebView_PayUMoneyActivity.this, "", "Loading. Please wait...", true);

        webView=(WebView)findViewById(R.id.webView1);
        Intent intent = getIntent();

        sharedPreferences_name = getString(R.string.shared_preferences_name);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(sharedPreferences_name, 0); // 0 - for
        // private
        // mode
        utils.setSharedPreferences(sharedPreferences);

        sccess_url=intent.getStringExtra("sccess_url");
        failure_url=intent.getStringExtra("failure_url");
        merchant_key=intent.getStringExtra("merchant_key");
        merchant_id=intent.getStringExtra("merchant_id");
        merchant_hash=intent.getStringExtra("merchant_hash");
        merchant_salt=intent.getStringExtra("merchant_salt");
        web_number=intent.getStringExtra("web_number");
        web_emailId=intent.getStringExtra("web_emailId");
        web_First_name=intent.getStringExtra("web_First_name");
        web_track_id=intent.getStringExtra("web_track_id");

        additionalAmount=(AdditionalAmount)intent.getSerializableExtra("additional_amount");
        Changepack= intent.getBooleanExtra("Changepack",false);
        UpdateFrom=intent.getStringExtra("UpdateFrom");
        PackageName=intent.getStringExtra("PackageName");
        PackageAmount=intent.getStringExtra("PackageAmount");
        //PackageAmount="1.00";

        mAction = mBaseURL.concat("/_payment");

        Utils.log("Additional Amount",":"+additionalAmount.getAdditionalAmountType());
/*

        if((additionalAmount!=null)&&(additionalAmount.getAdditionalAmountType() != null)&&(additionalAmount.getAdditionalAmountType().length() > 0) ) {
            additional_amt_type=additionalAmount.getAdditionalAmountType();
        }else{
            additional_amt_type="Renew";
        }

*/

        mHash = hashCal("SHA-512", merchant_key + "|" +
                web_track_id + "|" +
                PackageAmount + "|" +
                mProductInfo + "|" +
                web_First_name + "|" +
                web_emailId + "|||||||||||" +
                merchant_salt);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(context, "Oh no! " + error, Toast.LENGTH_SHORT).show();
            }

            /*   @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e( "SSL Error! ",":" + error);
                handler.proceed();
            }*/

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //  if(is_activity_running)
                // progressHUD.dismiss();
                if(pd!=null){
                    if (pd.isShowing()) {
                        pd.dismiss();
                        pd=null;
                    }
                }

                if(url.indexOf("/PayUMoneyResponseMobile.aspx")!=-1){
                    webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
            }
        });

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(2);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        Log.e("Web view ","Called");

        /**
         * Mapping Compulsory Key Value Pairs
         */

        Map<String, String> mapParams = new HashMap<>();

        mapParams.put("key", merchant_key);
        mapParams.put("txnid", web_track_id);
        mapParams.put("amount", String.valueOf(PackageAmount));
        mapParams.put("productinfo", mProductInfo);
        mapParams.put("firstname", web_First_name);
        mapParams.put("email", web_emailId);
        mapParams.put("phone", web_number);
        mapParams.put("surl", sccess_url);
        mapParams.put("furl", failure_url);
        mapParams.put("hash", mHash);
        mapParams.put("service_provider", mServiceProvider);

        webViewClientPost(webView, mAction, mapParams.entrySet());
    }

    public void webViewClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {

        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));

        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");

        Log.e("TAG", "webViewClientPost called: " + sb.toString());
        webView.loadData(sb.toString(), "text/html", "utf-8");
    }

    public String hashCal(String type, String str) {
        byte[] hashSequence = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashSequence);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException NSAE) {
        }
        return hexString.toString();
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            // process the html as needed by the app
            Log.e("Web view","Called  JavaScriptInterface");
            System.out.println(html);
            String[] actual_response = null;
            Log.e("HTML", "is" + html);
            if (html != null) {
                if (html.length() > 0) {
                    String s = html;
                    Log.e("Before", "Split" + s);
                    actual_response = TextUtils.split(html, "S\\@gar123\\$");

                    Log.e("actual response",""+actual_response);

                    Utils.log("Response  1","is"+actual_response[1]);
                    Utils.log("Response  2","is"+actual_response[2]);

                    parse_PayUMoney(actual_response[1]);
                }
            }
        }
    }

    public void parse_PayUMoney(String json){
        Utils.log("parse payu json",":"+json.toString());
        try {
            Utils.log("parse payu json1",":"+json.toString());

            JSONObject mainjson = new JSONObject(json);

            //Utils.log("amount_split Id",":"+mainjson.getString("amount_split"));

            order_id = mainjson.optString("payuMoneyId");
            Authcode_id = mainjson.optString("mihpayid");
            amount = mainjson.optString("amount");
            Utils.log("parse payu amount",":"+amount);

            PayU_Trans_Status = mainjson.optString("status");
            bank_trans = mainjson.optString("bank_ref_num");
            trans_msg=mainjson.optString("field9");
            Trans_id=mainjson.optString("txnid");

            paymentsObj.setAuthIdCode(order_id);
            paymentsObj.setTxStatus(PayU_Trans_Status);
            paymentsObj.setTxMsg(trans_msg);
            paymentsObj.setTxId(Trans_id);
            paymentsObj.setIssuerRefNo(bank_trans);
            paymentsObj.setAmount(amount);
            paymentsObj.setPgTxnNo(Authcode_id);

            Utils.log("InsertPayment", "Insert Pay executed1");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new InsertAfterPayemnt().executeOnExecutor(
                        AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                Utils.log("InsertPayment", "Insert Pay executed2");
            } else {
                new InsertAfterPayemnt().execute();
                Utils.log("InsertPayment", "Insert Pay executed3");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class InsertAfterPayemnt extends AsyncTask<String, Void, Void>
            implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;


        protected void onPreExecute() {

            Utils.log("Started", " InsertAfterPayment");
            mProgressHUD = ProgressHUD
                    .show(WebView_PayUMoneyActivity.this,
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

                Utils.log("STATUS AFTER",":"+paymentsObj.getTxStatus());

                if(paymentsObj.getTxStatus().equalsIgnoreCase("success")){

                    if (additionalAmount.getFinalcharges() != null) {
                        if (additionalAmount.getAdditionalAmountType() != null) {
                            if (additionalAmount.getAdditionalAmountType().length() > 0) {
                                if (additionalAmount.getAdditionalAmountType().contains("#")) {
                                    String split[] = additionalAmount.getAdditionalAmountType().split("#");
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
/*
                    if (additional_amt_type.contains("#")) {
                        String split[] = additional_amt_type.split("#");
                        if (split.length > 0) {
                            type = split[1];
                            Utils.log("Additional type if",":"+type);
                        }
                    } else {
                        type = additional_amt_type;
                        Utils.log("Additional type else",":"+type);
                    }*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new RenewalProcessWebService().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                    } else {
                        new RenewalProcessWebService().execute();
                    }
                }else {
                    WebView_PayUMoneyActivity.this.finish();

                    Intent intent = new Intent(getApplicationContext(), TransResponseCCAvenue.class);
                    intent.putExtra("transStatus", trans_msg);
                    intent.putExtra("order_id", order_id);
                    intent.putExtra("tracking_id",web_track_id );
                    intent.putExtra("amount", PackageAmount);
                    intent.putExtra("order_status", PayU_Trans_Status);
                    intent.putExtra("bank_ref_no", bank_trans);
                    intent.putExtra("payment_id", Authcode_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }else{
                Utils.log("Call else after",":");
                Intent intent = new Intent(getApplicationContext(), TransResponseCCAvenue.class);
                intent.putExtra("transStatus", trans_msg);
                intent.putExtra("order_id", order_id);
                intent.putExtra("tracking_id",web_track_id );
                intent.putExtra("amount", PackageAmount);
                intent.putExtra("order_status", PayU_Trans_Status);
                intent.putExtra("bank_ref_no", bank_trans);
                intent.putExtra("payment_id", Authcode_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                AfterInsertPaymentsCaller caller = new AfterInsertPaymentsCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL), getApplicationContext()
                        .getResources().getString(
                                R.string.METHOD_AFTER_MEMBER_PAYMENTS), true);

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
                AlertsBoxFactory.showAlert(rslt, WebView_PayUMoneyActivity.this);

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
            AsyncTask<String, Void, Void> implements DialogInterface.OnCancelListener {

        ProgressHUD mProgressHUD;
        ProgressDialog progressDialog;
        PaymentsObj paymentsObj = new PaymentsObj();

        protected void onPreExecute() {

            if(mProgressHUD==null){
                mProgressHUD = ProgressHUD
                        .show(WebView_PayUMoneyActivity.this,
                                getString(R.string.app_please_wait), true,
                                false, this);
            }

			/*progressDialog= new ProgressDialog(WebView_CCAvenueActivity.this);
			progressDialog.show();
			progressDialog.setCancelable(false);*/
        }

        @Override
        protected void onCancelled() {
            mProgressHUD.dismiss();
            //progressDialog.dismiss();
            // submit.setClickable(true);
        }

        protected void onPostExecute(Void unused) {

            mProgressHUD.dismiss();
            // submit.setClickable(true);
            //progressDialog.dismiss();

            if (rslt.trim().equalsIgnoreCase("ok")) {
                // finish();
                WebView_PayUMoneyActivity.this.finish();

                Intent intent = new Intent(getApplicationContext(), TransResponseCCAvenue.class);
                intent.putExtra("transStatus", trans_msg);
                intent.putExtra("order_id", order_id);
                intent.putExtra("tracking_id",Trans_id );
                intent.putExtra("amount", amount);
                intent.putExtra("order_status", PayU_Trans_Status);
                intent.putExtra("bank_ref_no", bank_trans);
                intent.putExtra("payment_id", Authcode_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                AlertsBoxFactory.showAlert(rslt, WebView_PayUMoneyActivity.this);
                return;
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                RenewalCaller caller = new RenewalCaller(
                        getApplicationContext().getResources().getString(
                                R.string.WSDL_TARGET_NAMESPACE),
                        getApplicationContext().getResources().getString(
                                R.string.SOAP_URL),
                        getApplicationContext().getResources().getString(
                                R.string.METHOD_RENEWAL_REACTIVATE_METHOD));


                paymentsObj.setMobileNumber(utils.getMobileNoPrimary());
                paymentsObj.setSubscriberID(utils.getMemberLoginID());
                // System.out.println("-------------Change Package :-----------"
                // + Changepack);

                paymentsObj.setIsChangePlan(Boolean.parseBoolean(String.valueOf(Changepack)));
                paymentsObj.setPlanName(PackageName);
                paymentsObj.setPaidAmount(Double.parseDouble(amount));
                paymentsObj.setTrackId(Trans_id);
                paymentsObj.setActionType(UpdateFrom);
                paymentsObj.setPaymentId(order_id);
                paymentsObj.setTxMsg(trans_msg);

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

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            //progressDialog.dismiss();
        }
    }

/*
    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(WebView_PayUMoneyActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.transaction_response);

        int width = 0;
        int height = 0;
        dialog.setCancelable(false);

        Point size = new Point();
        WindowManager w = ((Activity) WebView_PayUMoneyActivity.this).getWindowManager();

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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                PayU_Trans_Status = "onbackpressed";
                trans_msg="Cancelled by User";

                Utils.log("Yes atom_track_id", "" + Trans_id);
                Utils.log("status", "" + PayU_Trans_Status);

                paymentsObj.setTxId(Trans_id);
                paymentsObj.setTxStatus(PayU_Trans_Status);
                paymentsObj.setTxMsg(trans_msg);

                Utils.log("WebAdditional Amount",
                        "" + additionalAmount.getFinalcharges());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new InsertAfterPayemnt().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String) null);
                    Utils.log("InsertPayment", "Insert Pay executed");
                } else {
                    new InsertAfterPayemnt().execute();
                    Utils.log("InsertPayment", "Insert Pay executed");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout((width / 2) + (width / 2) / 2,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        return;
    }*/
}