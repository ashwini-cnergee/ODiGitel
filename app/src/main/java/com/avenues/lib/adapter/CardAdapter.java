package com.avenues.lib.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.avenues.lib.dto.CardTypeDTO;
import com.cnergee.widgets.MyTextView;

import java.util.ArrayList;

import cnergee.krishna.ookla.odigitel.R;


public class CardAdapter extends ArrayAdapter<CardTypeDTO> {
	private Activity context;
	ArrayList<CardTypeDTO> data = null;

	public CardAdapter(Activity context, int resource,
			ArrayList<CardTypeDTO> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) { 
		
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.card_type_spinner, parent, false);
		}
		CardTypeDTO cardType = data.get(position);
		if (cardType != null) { // Parse the data from each object and set it.
			MyTextView cardName = (MyTextView) row.findViewById(R.id.item_value);
			cardName.setText(cardType.getCardName());
		}
		return row;
	}

	
}