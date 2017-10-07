package com.saratrak.sisschool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.saratrak.sisschool.R;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AlertActivity extends Fragment {

	
	
	ListView dataGrid;
	static HttpClient client = new DefaultHttpClient();
	static adapter_alerts adapter;
	static ArrayList<AlertEntry> entries = new ArrayList<AlertEntry>();
	TextView tv ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity.currentFragment="AlertActivity";
		View rootView = inflater.inflate(R.layout.activity_alerts, container, false);
		entries.clear();
		entries.add(new AlertEntry(0,false,"","Loading..."));
		Alerts al = new Alerts();
		al.execute("http://www.saratrak.com/sis/mobile/getalerts.php");
		dataGrid = (ListView) rootView.findViewById(R.id.listView1);
		tv = (TextView) rootView.findViewById(R.id.textView2);
		adapter = new adapter_alerts(getActivity(),entries);
		dataGrid.setAdapter(adapter);
		((TextView) rootView.findViewById(R.id.textView1)).setTypeface(Fonts.light);
	    ((TextView) rootView.findViewById(R.id.textView2)).setTypeface(Fonts.light);
	    try{
			if(DataBaseHandler.db!=null)
			{
		if(DataBaseHandler.db.getImage()!=null)
		{
		Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);
		ImageView image = (ImageView)rootView.findViewById(R.id.imageView2);
		image.setImageBitmap(bmp2);
		}
			}
	    }
			catch(Exception e)
			{
				Log.i("AlertActivity","Exception while accessing DB :" + e);
			}
		
		dataGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				updatealert ua = new updatealert();
				ua.execute(String.valueOf(entries.get(position).getAlertid()) + "," + entries.get(position).getStudent_id());
		    }
		});
		
		
		
		return rootView;
    }
	
 
	
	private class Alerts extends AsyncTask<String, Void, String>{
		 
        @Override
        protected String doInBackground(String... url) {
        	try{
        	HttpPost send_call = new HttpPost(url[0]);
        	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        	nameValuePair.add(new BasicNameValuePair("user_id", DataBaseHandler.db.getUserid()));
        	send_call.setEntity(new UrlEncodedFormEntity(nameValuePair));
			BasicHttpResponse send_call_response = (BasicHttpResponse) client.execute(send_call);
			String response = EntityUtils.toString(send_call_response.getEntity());
			JSONArray mainArray = new JSONArray(response);
			entries.clear();
			for(int i=0;i<mainArray.length();i++)
			{
				JSONObject entry = (JSONObject) mainArray.get(i);
				AlertEntry alert = new AlertEntry();
				String input = entry.getString("alertid");
				alert.setAlertid(Integer.parseInt(input));
				input = entry.getString("alertdate");
				boolean exists = false;
				for(int j = 0;j<entries.size();j++)
				{
					if(alert.getAlertid()==entries.get(j).getAlertid())
					{
						exists = true;
						break;
					}
				}
				if(!exists)
				{
			    String date = input.split(Pattern.quote(" "))[0];
			    date = date.split(Pattern.quote("-"))[2] + "-" + date.split(Pattern.quote("-"))[1] + "-" + date.split(Pattern.quote("-"))[0];
				input = input.substring(input.length()-8, input.length()-3);
				int hours = Integer.parseInt(input.substring(0, input.length()-3));
				String period = (hours>12)?"PM":"AM";
				hours = (hours>12)?hours-12:hours;
				input = hours + ":" + input.substring(input.length()-2, input.length()) + " " + period;
				DateFormat df = new SimpleDateFormat("dd");
				Date dateobj = new Date();
				if(Integer.parseInt(df.format(dateobj).toString())==Integer.parseInt(date.split(Pattern.quote("-"))[0]))
					alert.setTime("Today" + "\n" + input);
				else if(Integer.parseInt(df.format(dateobj).toString())-Integer.parseInt(date.split(Pattern.quote("-"))[0])==1)
					alert.setTime("Yesterday" + "\n" + input);
				else
					alert.setTime(date + "\n" + input);
				input = entry.getString("alertstatus");
				alert.setIsRead((Integer.parseInt(input) == 0) ? false : true);
				input = entry.getString("alerttext");
				alert.setText(input);
				input = entry.getString("student_id");
				alert.setStudent_id(input);
				entries.add(alert);
				}
			}
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
            return "";
        }
 
        @Override
        protected void onPostExecute(String result) {
        	int i=0;
        	for(int j = 0;j<entries.size();j++)
			{
				if(!entries.get(j).getIsRead())
				{
					i++;
				}
			}
        	tv.setText(i + " UNREAD");
        	adapter = new adapter_alerts(getActivity(),entries);
        	dataGrid.invalidateViews();
    		dataGrid.setAdapter(adapter);
        	adapter.notifyDataSetChanged();
        }
    }
	
	private class updatealert extends AsyncTask<String, Void, String>{
		 
        @Override
        protected String doInBackground(String... alertid) {
        	try{
        	HttpPost send_call = new HttpPost("http://www.saratrak.com/sis/mobile/updatealertflag.php");
        	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        	nameValuePair.add(new BasicNameValuePair("student_id", alertid[0].split(",")[1]));
        	nameValuePair.add(new BasicNameValuePair("alert_id", alertid[0].split(",")[0]));
        	send_call.setEntity(new UrlEncodedFormEntity(nameValuePair));
			BasicHttpResponse send_call_response = (BasicHttpResponse) client.execute(send_call);
			String response = EntityUtils.toString(send_call_response.getEntity());
			return alertid[0].split(",")[0];
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
            return null;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result!=null)
        	{
        		for(int j = 0;j<entries.size();j++)
				{
					if(Integer.parseInt(result)==entries.get(j).getAlertid())
					{
						entries.get(j).setIsRead(true);
					}
				}
        		
        	}
        	int i=0;
        	for(int j = 0;j<entries.size();j++)
			{
				if(!entries.get(j).getIsRead())
				{
					i++;
				}
			}
        	tv.setText(i + " UNREAD");
        	//adapter_alerts adapter = new adapter_alerts(getActivity(),entries);
    		//dataGrid.setAdapter(adapter);
        	adapter.notifyDataSetChanged();
        }
    }
 
   
}