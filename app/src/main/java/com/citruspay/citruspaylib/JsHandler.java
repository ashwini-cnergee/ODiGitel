package com.citruspay.citruspaylib;

import android.app.Activity;

import android.webkit.WebView;

/**
 * Class to handle all calls from JS & from Java too
 **/
public class JsHandler
 {
public static String sJsonResponse;
	Activity activity;
	String TAG = "JsHandler";
	WebView webView;
	
	
	public JsHandler(Activity _contxt,WebView _webView) {
		activity = _contxt;
		webView = _webView;
	}

	/**
	 * This function handles call from JS
	 */
	public void sendResponse(String jsString) {
		//showDialog(jsString);
		//Log.i(">>>Response<<<<", "JS Handler LOG");
		//Log.i(">>>sendResponse------------", jsString.toString());
		//System.out.println("This is Test Activity Result......"+jsString);
	showResponse(jsString);
	}

	/**
	 * @author gauravgupta
	 * This function will send intent to response page . Json value as bundle 
	 * */
	public void showResponse(String jsString)
	{
		sJsonResponse=jsString;
		activity.finish();
		//webView.setVisibility(View.GONE);
		
		
		
	}
	
	/**
	 * This function handles call from Android-Java
	 */
	public void javaFnCall(String jsString) {
		
		final String webUrl = "javascript:diplayJavaMsg('"+jsString+"')";
		// Add this to avoid android.view.windowmanager$badtokenexception unable to add window
		if(!activity.isFinishing()) 
			// loadurl on UI main thread
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				webView.loadUrl(webUrl); 
			}
		});
	}
	
	
	
	/**
	 * function shows Android-Native Alert Dialog
	 */
	public void showDialog(String msg){/*
		
		AlertDialog alertDialog = new AlertDialog.Builder(activity).create();  
		alertDialog.setTitle(activity.getString(R.string.app_dialog_title));
		alertDialog.setMessage(msg);  
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,activity.getString(R.string.ok_text), new DialogInterface.OnClickListener() 
		{  
			public void onClick(DialogInterface dialog, int which) 
			{  
				dialog.dismiss();
			}
		}); 
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,activity.getString(R.string.cancel_text), new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		}); 
		alertDialog.show();
	*/}
}