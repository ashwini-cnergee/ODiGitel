package com.avenues.lib.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avenues.lib.dto.PaymentOptionDTO;
import com.cnergee.mypage.PaymentOptionSelection_Activity;
import com.cnergee.widgets.MyTextView;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;


public class PayOptAdapter extends ArrayAdapter<PaymentOptionDTO> {
	private Activity context;
	ArrayList<PaymentOptionDTO> data = null;
	int tv_id=100;
	public PayOptAdapter(Activity context, int resource,
			ArrayList<PaymentOptionDTO> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}
	
	@Override public View getDropDownView(int position, View cnvtView, ViewGroup prnt) { 
		return getCustomView(position, cnvtView, prnt); }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	return getCustomView(position, convertView, parent);
}

	
	public View getCustomView(final int position, View convertView, final ViewGroup parent) { 
		View row = convertView;
		ViewHolder holder= new ViewHolder();		
		PaymentOptionDTO paymentOption = data.get(position);
		
		
		
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.pay_opt_spinner_item, parent, false);
			holder.optionName = (MyTextView) row.findViewById(R.id.item_value);
			holder.optionImage = (ImageView) row.findViewById(R.id.iv_pyopt_icon);
			holder.ll_payOptions=(LinearLayout)row.findViewById(R.id.ll_payOptions);
			
			holder.optionName.setId(tv_id+position);
			row.setTag(holder);
		}
		else{
			 holder = (ViewHolder)row.getTag();
		}
	
		if (paymentOption != null) { 
			// Parse the data from each object and set it.
			
			holder.optionName.setText(paymentOption.getPayOptName());
			holder.optionImage.setImageResource(paymentOption.getIcon_resource());
			for(int i=0;i<data.size();i++){
				if(PaymentOptionSelection_Activity.click_position_opt==position){
					holder.optionName.setTextColor(Color.parseColor("#000000"));
				}
				else{
					holder.optionName.setTextColor(Color.parseColor("#A4A3A3"));
					
				}
			}
		}
		
		/*holder.ll_payOptions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i=0;i<data.size();i++){
					View v1=parent.getChildAt(i);
					if(v1!=null){
						TextView c=(TextView)v1.findViewById(tv_id+position);	
						if(i==position){
							c.setTextColor(Color.parseColor("#000000"));
						}
						else{
							c.setTextColor(Color.parseColor("#A4A3A3"));
							
						}
					}
				}
			}
		});*/
		return row;
	}
	
	public class ViewHolder{
		MyTextView optionName ;
		ImageView optionImage ;
		LinearLayout ll_payOptions;
	}

}