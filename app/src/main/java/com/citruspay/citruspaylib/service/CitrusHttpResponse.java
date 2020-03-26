package com.citruspay.citruspaylib.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.citruspay.citruspaylib.utils.Constants;
import com.cnergee.mypage.utils.Utils;


public class CitrusHttpResponse {
	@SuppressWarnings("unused")
	public static String postData(String url, String nameValuepair[]) {
		
		Log.i("This is Citrus Response-----------","This is Cytrus test Response");
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		String responseline = " ";
		try {
			/*
			 * 	public static final String SAPERATOR_PAIR = "~";
				public static final String SAPERATOR_PAIR_VALUE = "#";
			 */
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (nameValuepair != null && (nameValuepair.length > 0)	&& !nameValuepair[0].trim().equals("")) {
				String[] namevaluepairstring = nameValuepair[0].split(Constants.SAPERATOR_PAIR);
				for (int i = 0; i < namevaluepairstring.length; i++) {
					// load pic after this loop separately if present
					if (!namevaluepairstring[i].split(Constants.SAPERATOR_PAIR_VALUE)[0].equals(Constants.JSON_PIC)) {
						nameValuePairs
								.add(new BasicNameValuePair(namevaluepairstring[i].split(Constants.SAPERATOR_PAIR_VALUE)[0],
										namevaluepairstring[i].split(Constants.SAPERATOR_PAIR_VALUE)[1]));
						
						post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					} else if (namevaluepairstring[i]
							.split(Constants.SAPERATOR_PAIR_VALUE)[0]
							.equals(Constants.JSON_PIC)
							&& nameValuepair.length > 1) {

						nameValuePairs
								.add(new BasicNameValuePair(
										namevaluepairstring[i]
												.split(Constants.SAPERATOR_PAIR_VALUE)[0],
										nameValuepair[1]));
						
						post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					}

				}
			}

			response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder lineBuilder = new StringBuilder();
			String line;
			while (((line = rd.readLine()) != null)) {
				lineBuilder.append(line);
			}
			responseline = lineBuilder.toString();
			// TODO testing purpose check output over console
			/*if (lineBuilder != null)
				//System.out.println(lineBuilder.toString());
			else
				System.out.println("NULL");*/
		} catch (IOException e) {
			e.printStackTrace();
		}

		return responseline;
	}
	
	@SuppressWarnings("unused")
	public static String communicateToServer(String url,List<NameValuePair> nameValuePairs) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		String responseline = " ";
		BufferedReader rd;
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = client.execute(post);
			if(response.getEntity()!=null){
			 rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder lineBuilder = new StringBuilder();
			String line;
			while (((line = rd.readLine()) != null)) {
				lineBuilder.append(line);
			}
			
			
			responseline = lineBuilder.toString();
			
			/*if (lineBuilder != null)
				System.out.println(lineBuilder.toString());
			else
				System.out.println("NULL");*/
			
			}
			else
				responseline="";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseline;
	}
	
	
	
	
	public static String httpGetData(String myurl) {
		
		 InputStream in = null;
		 String response=null;
		// String rawURL=myurl;
		  byte[] data = new byte[8000];
	        try {
	        	  URL url = new URL(myurl);   
	              URLConnection conn = url.openConnection();
	              conn.connect();
	            
	              in = conn.getInputStream();
	              Utils.log("Buffer Size +++++++++++++", ""+in.toString().length());
	              BufferedReader rd = new BufferedReader(new InputStreamReader(in),in.toString().length());
	              String line;
	              StringBuilder sb =  new StringBuilder();
	              while ((line = rd.readLine()) != null) {
	              		sb.append(line);
	              }
	              rd.close();
	              response = sb.toString();

	             in.read(data);
	          Utils.log("INPUT STREAM PROFILE RESPONSE",response);
	            in.close();
	        } catch (IOException e1) {
	        	Utils.log("CONNECTION  ERROR", "+++++++++++++++++++++++++++");
	            // TODO Auto-generated catch block
	        	
	            e1.printStackTrace();
	        }
	        return response;
	}
	
	
	
	
	
	
	
	@SuppressWarnings("unused")
	public static String postData(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		String responseline = " ";
		try {
			response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuilder lineBuilder = new StringBuilder();
			String line;
			while (((line = rd.readLine()) != null)) {
				lineBuilder.append(line);
			}
			responseline = lineBuilder.toString();
			// TODO testing purpose check output over console
			/*if (lineBuilder != null)
				System.out.println(lineBuilder.toString());
			else
				System.out.println("NULL");*/
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("From Post DATA FUNCTION "+responseline);
		return responseline;
	}
	
}

