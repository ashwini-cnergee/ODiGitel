package com.cnergee.mypage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.cnergee.mypage.utils.Utils;
import com.cnergee.widgets.ProgressHUD;
import cnergee.krishna.ookla.odigitel.R;

public class PGSMS_Webview_Activity extends BaseActivity implements OnCancelListener{
	WebView wv_pg_sms;
	String sms_link="";
	ProgressHUD mProgressHUD;
	ImageView ivMenuDrawer;
	RelativeLayout rl_topbanner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_screen);
		wv_pg_sms=(WebView) findViewById(R.id.webView1) ;
		ivMenuDrawer=(ImageView) findViewById(R.id.ivMenuDrawer) ;
		rl_topbanner=(RelativeLayout) findViewById(R.id.topbanner) ;
		rl_topbanner.setVisibility(View.VISIBLE);
		sms_link=getIntent().getStringExtra("SMS_LINK");
		
		mProgressHUD=mProgressHUD = ProgressHUD
				.show(PGSMS_Webview_Activity.this,
						getString(R.string.app_please_wait_label), true,
						false, this);
		Utils.log("Log", ":"+sms_link);
		if(sms_link.length()>0){
			wv_pg_sms.getSettings().setJavaScriptEnabled(true);
			wv_pg_sms.loadUrl(sms_link);
			wv_pg_sms.setWebViewClient(new WebViewClient(){
				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					// TODO Auto-generated method stub
					super.onPageStarted(view, url, favicon);
					if(url.indexOf("lp.aspx")!=-1){
						url=url+"&m=1";
						wv_pg_sms.loadUrl(url);
						Utils.log("LP Page", "started");
					}
				}
				
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					if(url.indexOf("ccavenue")!=-1||url.indexOf("citurs")!=-1){
						mProgressHUD.dismiss();
					}
				}
				
				@Override
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					// TODO Auto-generated method stub
					super.onReceivedError(view, errorCode, description, failingUrl);
				}
			});
		}
		
		ivMenuDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showExitDialog();
			}
		});
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showExitDialog();
		
	}

	public void showExitDialog(){
		final Dialog dialog = new Dialog(PGSMS_Webview_Activity.this);
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
		
		
		
		    TextView	dtv = (TextView) dialog.findViewById(R.id.tvTitle);

		TextView txt = (TextView) dialog.findViewById(R.id.tvMessage);

		txt.setText(Html.fromHtml("If transaction is complete, then go back"+"\n"+"Are you sure?"));
		dtv.setText(Html.fromHtml("Confirmation"));
		
		Button btn_yes = (Button) dialog.findViewById(R.id.btn_dialog_resolve);
		Button btn_no = (Button) dialog.findViewById(R.id.btn_dialog_launch_comp);
		btn_no.setText("No");
		
		btn_yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PGSMS_Webview_Activity.this.finish();
				Intent intent = new Intent(
						PGSMS_Webview_Activity.this,
						IONHome.class);
				
				startActivity(intent);
				dialog.dismiss();
			}
		});
		
		btn_no.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			
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
