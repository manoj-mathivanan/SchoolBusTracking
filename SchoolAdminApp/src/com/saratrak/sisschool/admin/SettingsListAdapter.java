package com.saratrak.sisschool.admin;

import com.saratrak.sisschool.admin.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsListAdapter extends ArrayAdapter<String>  {
	private final Activity context;
	private final String[] itemname;
	private final Integer[] imgid;
	
	public SettingsListAdapter(Activity context, String[] itemname, Integer[] imgid) {
		super(context, R.layout.settings_list, itemname);
		// TODO Auto-generated constructor stub
		
		this.context=context;
		this.itemname=itemname;
		this.imgid=imgid;
	}
	
	public View getView(int position,View view,ViewGroup parent) {
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.settings_list, null,true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		txtTitle.setTypeface(Fonts.book);
		txtTitle.setText(itemname[position]);
		imageView.setImageResource(imgid[position]);
		return rowView;
		
	};
}
