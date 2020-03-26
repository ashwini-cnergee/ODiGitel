package com.cnergee.mypage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cnergee.mypage.obj.PaymentHistoryObj;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cnergee.krishna.ookla.odigitel.R;

public class PaymentHistoryAdapter extends ArrayAdapter<PaymentHistoryObj> {

	Context context;
	int layoutResourceId;
	PaymentHistoryObj data[]= null;
	
	public PaymentHistoryAdapter(Context context, int layoutResourceId, PaymentHistoryObj[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		PaymentHistoryHolder holder = null;

		
		if(row == null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new PaymentHistoryHolder();
			//holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
			holder.txtdate = (TextView)row.findViewById(R.id.txtdate);
			holder.txtamount = (TextView)row.findViewById(R.id.txtamount);
			row.setTag(holder);
		}
		else{
			holder = (PaymentHistoryHolder)row.getTag();
		}
		
		PaymentHistoryObj paymentObj = data[position];
		
		String strDate="";
		Date varDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try {
			varDate    =dateFormat.parse(paymentObj.getPaymentDate());
		    dateFormat= new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
		  //  System.out.println("Date :"+dateFormat.format(varDate));
		    strDate=   varDate.toString();
		}catch (Exception e) {
		    // TODO: handle exception
		    e.printStackTrace();
		    strDate="-";
		}
		
		holder.txtdate.setText(paymentObj.getPaymentDate());
		holder.txtamount.setText(" of Rs. " + paymentObj.getAmount());
		
		//Log.i(">>>>>Payment Date<<<<<", paymentObj.getPaymentDate());
		
		//holder.imgIcon.setImageResource(complObj.icon);
		
		return row;
	}
	
	
	static class PaymentHistoryHolder
	{
		//ImageView imgIcon;
		TextView txtdate;
		TextView txtamount;
	}

}
