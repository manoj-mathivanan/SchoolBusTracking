package com.saratrak.sisschool.admin;

import java.util.List;

import com.saratrak.sisschool.admin.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class adapter_alerts extends ArrayAdapter<AlertEntry> {
  private final Activity context;
  private final List<AlertEntry> values;
  public static String lastdate="";

  public adapter_alerts(Activity context, List<AlertEntry> values) {
    super(context, R.layout.alert_item, values);
    this.context = context;
    this.values = values;
  }

  @SuppressLint("ViewHolder") @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  LayoutInflater inflator = context.getLayoutInflater();
	  View rowView = inflator.inflate(R.layout.alert_item, null);
   
    ImageView alert_read = (ImageView) rowView.findViewById(R.id.alert_img);
    TextView alert_text = (TextView) rowView.findViewById(R.id.alert_text);
    TextView alert_time = (TextView) rowView.findViewById(R.id.alert_time);
    alert_text.setTypeface(Fonts.book);
    alert_time.setTypeface(Fonts.book);
    if(values.get(position).getAlertid()==-1)
    {
    	alert_read.setVisibility(View.INVISIBLE);
    	alert_text.setText("    " + values.get(position).getText());
    	alert_text.setTextColor(Color.parseColor("#ED3237"));
    	alert_time.setVisibility(View.INVISIBLE);
    }
    else
    {
    if(values.get(position).getIsRead()==false)
    {
    	alert_read.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.alerts_unread_icon));
    	alert_text.setTextColor(Color.parseColor("#1d1d26"));
    	alert_time.setTextColor(Color.parseColor("#1d1d26"));
    }
    else
    {
    	alert_read.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.alerts_unread_icon));
    	alert_text.setTextColor(Color.parseColor("#bfbfbf"));
    	alert_time.setTextColor(Color.parseColor("#bfbfbf"));
    }
    
    alert_text.setText(values.get(position).getText());
    alert_time.setText(values.get(position).getTime());
    }

    return rowView;
  }
} 