package com.saratrak.sisschool.admin;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	public static Context context;
	public static Activity act;
	public static String currentFragment; 
	public static String profileInvoke;
	public static Boolean called = false;
	ListView lv;
	// Search EditText
	EditText inputSearch;
	// ArrayList for Listview
	ArrayList<HashMap<String, String>> productList;
	public static BusListAdapter adapter;
	 public static ArrayList<Bus> buses = new ArrayList<Bus>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		act = this;
		setContentView(R.layout.activity_main);
		ActionBar bar = getActionBar();
		bar.show();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.header_image3x);
		BitmapDrawable background = new BitmapDrawable(bmp);
		background.setGravity(17);
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		bar.setBackgroundDrawable(background);
		bar.setDisplayShowTitleEnabled(false);
		buses.clear();
		Bus s = new Bus();
		s.setVehiclename("Loading..");
		s.setColour("G");
		s.setEntry1("-");
		s.setEntry2("-");
		s.setExit1("-");
		s.setExit2("-");
		buses.add(s);
		buslist bl = new buslist();
		bl.execute();
		called = true;
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		lv = (ListView) findViewById(R.id.listView1);
		lv.setClickable(true);
		lv.requestFocus();
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		adapter = new BusListAdapter(this, buses);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long index) {
            	
            	try{
            		if(!buses.get(position).getVehiclename().contains("Loading"))
            		{
            		 String t = buses.get(position).getUniqueId();
            		helper.id = t;
                  Intent i = new Intent(MainActivity.this, MapActivity.class); 
       			 startActivity(i);
            		}
            		else
            		{
            			Toast.makeText(getApplicationContext(), "Check Network connection", Toast.LENGTH_SHORT).show();
            		}
            	}catch(Exception e)
            	{
            		Log.i("Agenda","Exception while calling Maps :" +e);
            		
            	}
                
            }
        });

        lv.setAdapter(adapter);
        
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				return false;
			}
		} );


		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
			adapter.filter(cs);  
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
		

			}

			@Override
			public void afterTextChanged(Editable s) {
				
				
			}

		
		});
		
	}
	
	@Override
	public void onResume() {

		if(!called)
		{
		/*buses.clear();
		Bus s = new Bus();
		s.setVehiclename("Loading..");
		s.setColour("G");
		s.setEntry1("-");
		s.setEntry2("-");
		s.setExit1("-");
		s.setExit2("-");
		buses.add(s);
		adapter = new BusListAdapter(act, buses);
    	lv.invalidateViews();
		lv.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
		buslist bl = new buslist();
		bl.execute();*/
		}
		super.onResume();
	}
	
	private class buslist extends AsyncTask<String, Void, String>{
		 
        @Override
        protected String doInBackground(String... url) {
        	try{
        	HttpClient client = new DefaultHttpClient();
        	HttpGet send_call = new HttpGet("http://www.saratrak.com/sis/adminapp/listview.php");
        	BasicHttpResponse send_call_response = (BasicHttpResponse) client.execute(send_call);
			String response = EntityUtils.toString(send_call_response.getEntity());
			helper.response = response;
			JSONObject obj = new JSONObject(response);
			JSONArray mainArray = obj.getJSONArray("dates");
			buses.clear();

				JSONArray busarray = ((JSONObject) mainArray.get(0)).getJSONArray("vehicles");
				for(int j=0;j<busarray.length();j++)
				{
					JSONObject bus = (JSONObject)busarray.get(j);
						Bus bus1 = new Bus();
						bus1.setUniqueId(bus.getString("uniqueId"));
						bus1.setVehiclename(bus.getString("vehiclename"));
						bus1.setEntry1(bus.getString("entry1"));
						bus1.setExit1(bus.getString("exit1"));
						bus1.setEntry2(bus.getString("entry2"));
						bus1.setExit2(bus.getString("exit2"));
						bus1.setColour(bus.getString("state"));
						buses.add(bus1);
						
					
				}
						
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		return "error";
        	}
            return "success";
        }
        
    	 
        @Override
        protected void onPostExecute(String result) {
        	if(!result.contains("error"))
        	{
        	adapter = new BusListAdapter(act, buses);
        	lv.invalidateViews();
    		lv.setAdapter(adapter);
        	adapter.notifyDataSetChanged();
        	}
        	else
        	{
        		Toast.makeText(getApplicationContext(), "Check Network connection", Toast.LENGTH_SHORT).show();
        	}
        	called = false;
        }
    }
	

	public void onBackPressed() {
		moveTaskToBack(true);
	} 
}
