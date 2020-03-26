package com.cnergee.mypage.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnergee.mypage.obj.HelpObject;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;

public class HelpAdapter extends ArrayAdapter<HelpObject> {
	Context ctx;
	ArrayList<HelpObject> alHelp;
	public HelpAdapter(Context context, int textViewResourceId,
			ArrayList<HelpObject> objects) {
		super(context,  textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.alHelp=objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		ViewHolder holder;
		HelpObject help=(HelpObject) this.getItem(position);
		
		if(row == null){
			LayoutInflater inflater = ((Activity)ctx).getLayoutInflater();
			row = inflater.inflate(R.layout.help_item, parent, false);
			holder = new ViewHolder();
			//holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
			holder.tvHelp = (TextView)row.findViewById(R.id.tvHelp);
			holder.ivHelp = (ImageView)row.findViewById(R.id.ivHelp);
			holder.view_color = (View)row.findViewById(R.id.view_help);
			row.setTag(holder);
		}
		else{
			holder = (ViewHolder)row.getTag();
		}
		
	
		holder.tvHelp.setText(help.getText_name());
		holder.ivHelp.setBackgroundResource(help.getDrawable_resource());
		holder.view_color.setBackgroundResource(help.getColor());
	
		//Log.i(">>>>>Payment Date<<<<<", paymentObj.getPaymentDate());
		
		//holder.imgIcon.setImageResource(complObj.icon);
		
		return row;
	}
	
	
	public class ViewHolder
	{
		//ImageView imgIcon;
		TextView tvHelp;
		View view_color;
		ImageView ivHelp;
	}
}
