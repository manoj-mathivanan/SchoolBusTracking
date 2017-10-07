package com.saratrak.sisschool.admin;

import java.util.List;
import java.util.regex.Pattern;

import com.saratrak.sisschool.admin.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adaptermap extends ArrayAdapter<TimeLineEntry> {
	private final Context context;
	private final List<TimeLineEntry> values;

	public adaptermap(Context context, List<TimeLineEntry> values) {
		super(context, R.layout.timeline_item, values);
		this.context = context;
		this.values = values;
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.timeline_item, parent, false);
		try{
			TextView time_text = (TextView) rowView.findViewById(R.id.time_text);
			TextView time_time = (TextView) rowView.findViewById(R.id.time_time);
			TextView time_loc = (TextView) rowView.findViewById(R.id.time_loc);
			TextView time_engine = (TextView) rowView.findViewById(R.id.TextView01);
			time_text.setTypeface(Fonts.light);
			time_time.setTypeface(Fonts.book);
			time_engine.setTypeface(Fonts.book);
			time_loc.setTypeface(Fonts.book);
			/* ImageView imageView = (ImageView) rowView.findViewById(R.id.time_img);
    if(position==0)
    	imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.home_green_color_dot_with_line));
    else
    {
    	imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.home_green_color_dot_with_line_only));
    	imageView.setPadding(0, 0, 0, 0);
    }*/
			time_text.setText(values.get(position).getStatus());
			time_time.setText(values.get(position).getTime());
			time_engine.setVisibility(View.VISIBLE);
			if(values.get(position).getEnginestatus()>0)
			{
				time_engine.setText("Engine ON");
				time_loc.setVisibility(View.VISIBLE);
				time_engine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.engineon,0,0,0); 

			}
			else
			{
				time_engine.setText("Engine OFF");
				time_loc.setVisibility(View.INVISIBLE);
				time_engine.setCompoundDrawablesWithIntrinsicBounds(R.drawable.engineoff,0,0,0); 
			}
			
			if(values.get(position).getSpeed().toString().split(Pattern.quote("."))[0]!=null){
				if(Integer.parseInt(values.get(position).getSpeed().split(Pattern.quote("."))[0])>5)
				{
					time_loc.setText(values.get(position).getSpeed().toString().split(Pattern.quote("."))[0] + " Km/hr");
				}
				else{
					time_loc.setVisibility(View.INVISIBLE);
				}
			}else if(values.get(position).getSpeed().toString()!=null){
					if(Integer.parseInt(values.get(position).getSpeed().toString())>5)
					{
						time_loc.setText(values.get(position).getSpeed().toString() + " Km/hr");
					}
					else
					{
						time_loc.setVisibility(View.INVISIBLE);
					}
				}
				else
				{
					time_loc.setVisibility(View.INVISIBLE);
				}

		}
		catch(Exception e){
			Log.i("AdapterMap","Exception while accessing DB :" + e);
		}

		return rowView;
	}
} 