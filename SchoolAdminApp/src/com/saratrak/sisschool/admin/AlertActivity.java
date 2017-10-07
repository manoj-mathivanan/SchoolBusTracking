package com.saratrak.sisschool.admin;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class AlertActivity extends Activity {

	
	
	ListView dataGrid;
	Activity act;
	static HttpClient client = new DefaultHttpClient();
	static adapter_alerts adapter;
	static ArrayList<AlertEntry> entries = new ArrayList<AlertEntry>();
	//TextView tv ;
	static String lastdate="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alerts);
		act = this;
		ActionBar bar = getActionBar();
		bar.show();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.header_image3x);
		BitmapDrawable background = new BitmapDrawable(bmp);
		background.setGravity(17);
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		bar.setBackgroundDrawable(background);
		bar.setDisplayShowTitleEnabled(false);
		/*
		entries.clear();
		entries.add(new AlertEntry(0,false,"","Loading..."));
		adapter = new adapter_alerts(this,entries);
		dataGrid = (ListView)findViewById(R.id.listView1);
		dataGrid.setAdapter(adapter);
		*/
		//adapter.notifyDataSetChanged();
		dataGrid = (ListView)findViewById(R.id.listView1);
		Alerts al = new Alerts();
		al.execute("http://www.saratrak.com/sis/adminapp/listview.php");

    }
	
 
	
	private class Alerts extends AsyncTask<String, Void, String>{
		 
        @Override
        protected String doInBackground(String... url) {
        	try{
        	//HttpGet send_call = new HttpGet(url[0]);
        	//BasicHttpResponse send_call_response = (BasicHttpResponse) client.execute(send_call);
			String response = helper.response;
			JSONObject obj = new JSONObject(response);
			JSONArray mainArray = obj.getJSONArray("dates");
			
			entries.clear();
			
			for(int i=0;i<mainArray.length();i++)
			{
				JSONObject entry = (JSONObject) mainArray.get(i);
				String input = entry.getString("entries_date");
				boolean exists = false;
				if(!exists)
				{
			    String date = input;
				DateFormat df = new SimpleDateFormat("dd");
				Date dateobj = new Date();
				
				if(lastdate.compareToIgnoreCase(date)!=0)
				{
					AlertEntry alertday = new AlertEntry();
					alertday.setAlertid(-1);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

						Date date2 = formatter.parse(date);

					if(date2.getDate()==dateobj.getDate())
						alertday.setText("Today");
					else
						alertday.setText(getMonthForInt(date2.getMonth()) + " " + date2.getDate() + ", " + (date2.getYear()+1900));
					
					alertday.setIsRead(false);
					entries.add(alertday);
				}
				lastdate = date;
				Boolean isavailable = false;
				JSONArray busarray = ((JSONObject) mainArray.get(i)).getJSONArray("vehicles");
				for(int j=0;j<busarray.length();j++)
				{
					JSONObject bus = (JSONObject)busarray.get(j);
					String id = bus.getString("uniqueId");
					if(id.compareToIgnoreCase(helper.id)==0)
					{
						
						if(bus.getString("entry1").compareToIgnoreCase("-")!=0)
						{
							isavailable = true;
							AlertEntry alert = new AlertEntry();
							alert.setAlertid(i);
							alert.setIsRead(false);
							alert.setText("");
							String name = bus.getString("vehiclename");
							String time = bus.getString("entry1");
							alert.setText("Bus no: " + name + " has reached school campus");
							SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
							Date date2 = formatter.parse(time);
							if(date2.getHours()>12)
								alert.setTime(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " PM");
							else
								alert.setTime(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " AM");
							entries.add(alert);
						}
						if(bus.getString("exit1").compareToIgnoreCase("-")!=0)
						{
							AlertEntry alert = new AlertEntry();
							alert.setAlertid(i);
							alert.setIsRead(false);
							alert.setText("");
							String name = bus.getString("vehiclename");
							String time = bus.getString("exit1");
							alert.setText("Bus no: " + name + " has left school campus");
							SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
							Date date2 = formatter.parse(time);
							if(date2.getHours()>12)
								alert.setTime(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " PM");
							else
								alert.setTime(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " AM");
							entries.add(alert);
						}
						if(bus.getString("entry2").compareToIgnoreCase("-")!=0)
						{
							AlertEntry alert = new AlertEntry();
							alert.setAlertid(i);
							alert.setIsRead(false);
							alert.setText("");
							String name = bus.getString("vehiclename");
							String time = bus.getString("entry2");
							alert.setText("Bus no: " + name + " has reached school campus");
							SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
							Date date2 = formatter.parse(time);
							if(date2.getHours()>12)
								alert.setTime(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " PM");
							else
								alert.setTime(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " AM");
							entries.add(alert);
						}
						if(bus.getString("exit2").compareToIgnoreCase("-")!=0)
						{
							AlertEntry alert = new AlertEntry();
							alert.setAlertid(i);
							alert.setIsRead(false);
							alert.setText("");
							String name = bus.getString("vehiclename");
							String time = bus.getString("exit2");
							alert.setText("Bus no: " + name + " has left school campus");
							SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
							Date date2 = formatter.parse(time);
							if(date2.getHours()>12)
								alert.setTime(date2.getHours()-12 + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " PM");
							else
								alert.setTime(date2.getHours() + ":" + (date2.getMinutes()<10?"0"+date2.getMinutes():date2.getMinutes()) + " AM");
							entries.add(alert);
						}
						
						break;
					}
				}
				if(!isavailable)
				{
					entries.remove(entries.size()-1);
				}
				
				}
			}
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		return null;
        	}
            return "";
        }
        
    	String getMonthForInt(int num) {
            String month = "wrong";
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();
            if (num >= 0 && num <= 11 ) {
                month = months[num];
            }
            return month;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result!=null)
        	{
        	
        	adapter = new adapter_alerts(act,entries);
        	dataGrid.invalidateViews();
    		dataGrid.setAdapter(adapter);
        	adapter.notifyDataSetChanged();
        	}
        	else
        	{
        		Toast.makeText(getApplicationContext(), "Error in server response", Toast.LENGTH_LONG).show();
        	}
        }
    }
	

	
	
 
   
}