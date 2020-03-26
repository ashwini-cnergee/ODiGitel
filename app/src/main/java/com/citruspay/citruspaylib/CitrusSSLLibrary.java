package com.citruspay.citruspaylib;

import java.util.HashMap;
import java.util.Iterator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

import com.citruspay.citruspaylib.model.Address;
import com.citruspay.citruspaylib.model.Card;
import com.citruspay.citruspaylib.model.Customer;
import com.citruspay.citruspaylib.model.ExtraParams;
import com.citruspay.citruspaylib.utils.CitrusParams;
import com.citruspay.citruspaylib.utils.Constants;

public class CitrusSSLLibrary extends Activity {
	private static String mHACValue;
	JsHandler _jsHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_citrus_ssllibrary);
	}

	/*
	 * public String generateRandomMerchantTransactionId() { BigInteger b = new
	 * BigInteger(64, new Random()); String tId = b.toString(); return tId; }
	 */

	/*public String generateHMACFromMerchantServer(String serverUrl,
			List<NameValuePair> nameValuePair, Activity activity) {
		// TODO Auto-generated method stub
		mHACValue = CitrusHttpResponse.communicateToServer(serverUrl,
				nameValuePair);
		return mHACValue;

	}
*/
	/**
	 * @author gauravgupta This will accpt Hashmap as key value pair and make
	 *         parameter and return that parameter to calling side. Retrun type
	 *         is string. Hashmap signature is <String,String>
	 * 
	 * */

	public String makeWebViewPostParameters(HashMap<String, String> keyValuePair) {
		Iterator<String> myVeryOwnIterator = keyValuePair.keySet().iterator();
		StringBuilder postParameterValues = new StringBuilder();
		String prefix = "";

		while (myVeryOwnIterator.hasNext()) {

			String key = (String) myVeryOwnIterator.next();
			String value = (String) keyValuePair.get(key);
			postParameterValues
					.append(key + Constants.PARAMETER_EQUALS + value);
			prefix = Constants.PARAMETER_SEPERATOR;
			postParameterValues.append(prefix);

			// Toast.makeText(getApplicationContext(),
			// "Key: "+key+" Value: "+value, Toast.LENGTH_LONG).show();
		}
		return postParameterValues.toString().substring(0,
				postParameterValues.length() - 1);
	}

	/**
	 * @author gauravgupta This will set properties to webview to make them
	 *         compatable to send n receive request. It will call JsHandler
	 *         class and return response from merchant server
	 * */

	@SuppressLint("SetJavaScriptEnabled")
	public void setWebViewPropertiesForMerchantServer(Activity activity,
			WebView _mwebView) {
		_mwebView.getSettings().setJavaScriptEnabled(true);
		_mwebView.setBackgroundColor(Color.parseColor("#808080"));

		// Set whether the DOM storage API is enabled.
		_mwebView.getSettings().setDomStorageEnabled(true);

		// setBuiltInZoomControls = false, removes +/- controls on screen
		_mwebView.getSettings().setBuiltInZoomControls(false);

		_mwebView.getSettings().setPluginState(PluginState.ON);
		_mwebView.getSettings().setAllowFileAccess(true);

		_mwebView.getSettings().setAppCacheMaxSize(1024 * 8);
		_mwebView.getSettings().setAppCacheEnabled(true);

		_jsHandler = new JsHandler(activity, _mwebView);
		_mwebView.addJavascriptInterface(_jsHandler, "JsHandler");

		_mwebView.getSettings().setUseWideViewPort(false);
		_mwebView.setWebChromeClient(new WebChromeClient());
		_mwebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		_mwebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		_mwebView.requestFocus(View.FOCUS_DOWN);

	}

	/**
	 * @author gauravgupta This will accpt Custom getter-setter class as
	 *         parameter and make parameter and return that parameter to calling
	 *         side. Retrun type is string. Method signature is
	 *         <Customer,Address,Card,ExtraParams>
	 * 
	 * */

	public String makeWebViewPostParameters(Customer customer, Address address,
			Card card, ExtraParams extraParam) {
		StringBuilder postParameterValues = new StringBuilder();

		if (customer != null) {
			if (customer.getFirstName() != null)
				postParameterValues
						.append(addToPostUrl(CitrusParams.PARAM_FIRST_NAME,
								customer.getFirstName()));
			if (customer.getLastName() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_LAST_NAME, customer.getLastName()));
			if (customer.getEmail() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_EMAIL, customer.getEmail()));
			if (customer.getPhoneNumber() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_PHONE_NUMBER,
						customer.getPhoneNumber()));

		}
		if (address != null) {
			if (address.getAddressCity() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_ADDRESS_CITY,
						address.getAddressCity()));
			if (address.getAddressCountry() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_ADDRESS_COUNTRY,
						address.getAddressCountry()));
			if (address.getAddressState() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_ADDRESS_STATE,
						address.getAddressState()));
			if (address.getAddressStreet1() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_ADDRESS_STREET1,
						address.getAddressStreet1()));
			if (address.getAddressZip() != null)
				postParameterValues
						.append(addToPostUrl(CitrusParams.PARAM_ADDRESS_ZIP,
								address.getAddressZip()));
		}
		if (card != null) {
			if (card.getCardHolderName() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_CARD_HOLDER_NAME,
						card.getCardHolderName()));
			if (card.getCardNumber() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_CARD_NUMBER, card.getCardNumber()));
			if (card.getCardType() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_CARD_TYPE, card.getCardType()));
			if (card.getCvvNumber() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_CVV_NUMBER, card.getCvvNumber()));
			if (card.getExpiryMonth() != null)
				postParameterValues
						.append(addToPostUrl(CitrusParams.PARAM_EXPIRY_MONTH,
								card.getExpiryMonth()));
			if (card.getExpiryYear() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_EXPIRY_YEAR, card.getExpiryYear()));
			if (card.getPaymentMode() != null)
				postParameterValues
						.append(addToPostUrl(CitrusParams.PARAM_PAYMENT_MODE,
								card.getPaymentMode()));

		}
		if (extraParam != null) {
			if (extraParam.getCurrency() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_CURRENCY, extraParam.getCurrency()));
			if (extraParam.getHMACValue() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_HMAC, extraParam.getHMACValue()));
			if (extraParam.getMerchantTxnId() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_MERCHANTTXNID,
						extraParam.getMerchantTxnId()));
			if (extraParam.getOrderAmountValue() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_ORDER_AMOUNT,
						extraParam.getOrderAmountValue()));
			if (extraParam.getReturnUrl() != null)
				postParameterValues.append(addToPostUrl(
						CitrusParams.PARAM_RETURN_URL,
						extraParam.getReturnUrl()));

		}
		return postParameterValues.toString().substring(0,
				postParameterValues.length() - 1);
	}

	/**
	 * @author gauravgupta Retrun key n value in proper parameter format
	 * */

	public String addToPostUrl(String paramKey, String paramValue)

	{
		return paramKey.concat(Constants.PARAMETER_EQUALS).concat(paramValue)
				.concat(Constants.PARAMETER_SEPERATOR);

	}

	/**
	 * @author gauravgupta
	 * This will return json response after completing or canceling webview transcation procss
	 * */
	public String getWebClientJsResponse() {
		return JsHandler.sJsonResponse;

	}

}
