package com.saratrak.sisschool.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class BusListAdapter extends BaseAdapter implements OnClickListener   {
	 private Activity context;

	    private List<Bus> busList;
	    private ArrayList<Bus> arraylist;
	    
	    public BusListAdapter(Activity context, List<Bus> busList){
	    	this.context = context;
	    	this.busList = busList;
	    	this.arraylist = new ArrayList<Bus>();
			this.arraylist.addAll(busList);
	    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return busList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return busList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Bus s = busList.get(position);
		 LayoutInflater inflator = context.getLayoutInflater();
		  View rowView = inflator.inflate(R.layout.bus_layout, null);
	   try{
	    ImageView status = (ImageView) rowView.findViewById(R.id.status);
	    TextView vehiclename = (TextView) rowView.findViewById(R.id.vehicle);
	    TextView entry1 = (TextView) rowView.findViewById(R.id.entry1);
	    TextView entry1am = (TextView) rowView.findViewById(R.id.entry1am);
	    TextView exit1 = (TextView) rowView.findViewById(R.id.exit1);
	    TextView exit1am = (TextView) rowView.findViewById(R.id.exit1am);
	    TextView entry2 = (TextView) rowView.findViewById(R.id.entry2);
	    TextView entry2am = (TextView) rowView.findViewById(R.id.entry2am);
	    TextView exit2 = (TextView) rowView.findViewById(R.id.exit2);
	    TextView exit2am = (TextView) rowView.findViewById(R.id.exit2am);
	    
	    vehiclename.setText(s.getVehiclename());
	    
	    if(s.getColour().compareToIgnoreCase("Y")==0)
	    	status.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.dot_yellow));
	    else if(s.getColour().compareToIgnoreCase("R")==0)
		    status.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.dot_red));
		else if(s.getColour().compareToIgnoreCase("G")==0)
		    status.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.dot_green));
	    
	    if(s.getEntry1().compareToIgnoreCase("-")!=0)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
			Date date2 = formatter.parse(s.getEntry1());
			if(date2.getHours()>12)
			{
				entry1.setText(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				entry1am.setText("PM");
			}
			else
			{
				entry1.setText(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				entry1am.setText("AM");
			}
		}
	    else
	    {
	    	entry1.setText("   -   ");
	    	entry1am.setText("");
	    }
		
	    if(s.getExit1().compareToIgnoreCase("-")!=0)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
			Date date2 = formatter.parse(s.getExit1());
			if(date2.getHours()>12)
			{
				exit1.setText(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				exit1am.setText("PM");
			}
			else
			{
				exit1.setText(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				exit1am.setText("AM");
			}
		}
	    else
	    {
	    	exit1.setText("   -   ");
	    	exit1am.setText("");
	    }
	    if(s.getEntry2().compareToIgnoreCase("-")!=0)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
			Date date2 = formatter.parse(s.getEntry2());
			if(date2.getHours()>12)
			{
				entry2.setText(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				entry2am.setText("PM");
			}
			else
			{
				entry2.setText(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				entry2am.setText("AM");
			}
		}
	    else
	    {
	    	entry2.setText("   -   ");
	    	entry2am.setText("");
	    }
		
	    if(s.getExit2().compareToIgnoreCase("-")!=0)
		{
			SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
			Date date2 = formatter.parse(s.getExit2());
			if(date2.getHours()>12)
			{
				exit2.setText(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				exit2am.setText("PM");
			}
			else
			{
				exit2.setText(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()));
				exit2am.setText("AM");
			}
		}
	    else
	    {
	    	exit2.setText("   -   ");
	    	exit2am.setText("");
	    }
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
	   }

	    return rowView;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void filter(CharSequence charText) {
		// TODO Auto-generated method stub
		//charText = ((String) charText).toLowerCase(Locale.getDefault());
		busList.clear();
		if (charText.length() == 0) {
			busList.addAll(arraylist);
		} else {
			for (Bus wp : arraylist) {
				if ((wp.getVehiclename().toLowerCase().contains(charText))||(wp.getVehiclename().contains(charText))) {
					busList.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}

}
