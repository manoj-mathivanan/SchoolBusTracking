package com.saratrak.sisschool.admin;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity {

	static LatLng BUS = new LatLng(19.2617731,72.8763325);
	static ArrayList<LatLng> maplist = new ArrayList<LatLng>();
	static boolean markerplace = false;
	static Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();
	final Handler handler2 = new Handler();
	GoogleMap googleMap;
	final String TAG = "PathGoogleMapActivity";
	private GoogleMap map;
	adaptermap adapter;
	ListView dataGrid;
	ArrayList<LatLng> markerPoints;
	static MarkerOptions bus;
	static HttpClient client = new DefaultHttpClient();
	static HttpClient client2 = new DefaultHttpClient();
	static BitmapDescriptor bd;
	static int count =0;
	static String statusText;
	TextView status;
	String statustext;
	TextView enginestatus;
	Typeface tf ;
	static ArrayList<TimeLineEntry> entries = new ArrayList<TimeLineEntry>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			setContentView(R.layout.activity_home);
			try{

				MapFragment mf = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
				map = mf.getMap();
				map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(19.2617731,72.8763325)));
				map.animateCamera(CameraUpdateFactory.zoomTo(15));
				map.setMyLocationEnabled(true);
				bus  = new MarkerOptions();
			}
			catch(Exception e){
				Log.i("MapActvity","Exception while setting map : "+e);
				Toast.makeText(getApplicationContext(), "Map error fromdevice", Toast.LENGTH_LONG).show();
			}
			getActionBar().hide();
		ActionBar bar = getActionBar();
		bar.show();
		 Typeface tf = Typeface.createFromAsset(this.getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF");
		((TextView) findViewById(R.id.textViewStatus)).setTypeface(tf);
		((TextView) findViewById(R.id.Button01)).setTypeface(Fonts.book);
		((TextView) findViewById(R.id.Button02)).setTypeface(Fonts.book);
		((TextView) findViewById(R.id.Button03)).setTypeface(Fonts.book);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);

		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.header_image3x);
		BitmapDrawable background = new BitmapDrawable(bmp);
		background.setGravity(17);
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		bar.setBackgroundDrawable(background);
		bar.setDisplayShowTitleEnabled(false);
		adapter = new adaptermap(getApplicationContext(),entries);
		dataGrid = (ListView)findViewById(R.id.listView1);
		dataGrid.setAdapter(adapter);
		dataGrid.setVisibility(View.INVISIBLE);
		status = (TextView)findViewById(R.id.textViewStatus);
		status.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
		 tf = Typeface.createFromAsset(getAssets(),"fonts/AVENIRLTSTD-BOOK.OTF");
		
		enginestatus = (TextView)findViewById(R.id.TextView01);
        
		//ImageView image = (ImageView)findViewById(R.id.imageButton1);
		/*try{
			if(DataBaseHandler.db!=null)
			{
				if(statusText!=null){
					
					status.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
					
				}
				if(DataBaseHandler.db.getImage()!=null)
				{
					Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);

					image.setImageBitmap(bmp2);
				}
			}
		}
		catch(Exception e){
			Log.i("MapActivity","Exception while accessing DB :" + e);
		}
*/
		TextView tv2 = (TextView) findViewById(R.id.textViewStatus);
		tv2.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));

		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
	    stoptimertask();
	    super.onDestroy();
	}

	public ArrayList<TimeLineEntry> reversearray(ArrayList<TimeLineEntry> entries2)
	{
		ArrayList<TimeLineEntry> entries3  = new ArrayList<TimeLineEntry>();
		int j = 0;
		for(int i =entries2.size()-1;i>0;i--)
		{
			j++;
			if(j<7)
				entries3.add(entries2.get(i));
		}
		return entries3;
	}
	
	private class buslocation extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... url) {
			try {
				Log.i("bus location","in background thread");
				HttpGet send_call = new HttpGet(url[0]);
				BasicHttpResponse send_call_response;
				send_call_response = (BasicHttpResponse) client.execute(send_call);
				String response = EntityUtils.toString(send_call_response.getEntity());
				if(response.contains("<br"))
					response = "[" + response.split(Pattern.quote("["))[1];
				JSONObject mainObject = ((JSONObject)(new JSONArray(response)).getJSONObject(0));
				
				String address = mainObject.getString("address");
				String input = mainObject.getString("timestamp");
				input = input.substring(input.length()-8, input.length()-3);
				int hours = Integer.parseInt(input.substring(0, input.length()-3));
				String period = (hours>12)?"PM":"AM";
				hours = (hours>12)?hours-12:hours;
				input = hours + ":" + input.substring(input.length()-2, input.length()) + " " + period;
				if(address.contains("null"))
					address = "No address available";
				TimeLineEntry entry = new TimeLineEntry(address, input, System.currentTimeMillis(),mainObject.getString("speed"),Integer.parseInt(mainObject.getString("enginestatus")));
				
					entries.clear();
					entries.add(entry);
				
			
				String title = "test";
				//if(Integer.parseInt(mainObject.getString("enginestatus"))>0)
				//{
				title = mainObject.getString("name");
				
				//statustext = mainObject.getString("address");
				/*HttpGet send_call1 = new HttpGet(mainObject.getString("iconpath"));
	    			BasicHttpResponse send_call_response2;
	    			send_call_response2 = (BasicHttpResponse) client.execute(send_call1);
	    	 		byte[] img = EntityUtils.toByteArray(send_call_response2.getEntity());*/

				Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.bus_icon);
				bd = BitmapDescriptorFactory.fromBitmap(image);
				BUS = new LatLng(Double.parseDouble(mainObject.getString("lat")),Double.parseDouble(mainObject.getString("lng")));
				maplist.add(BUS);
				//}
				return title;

			} catch (Exception e) {
				e.printStackTrace();
				
				return null;
			}


		}
		@Override
		protected void onPostExecute(String title) {
			try{
				if(title!=null)
				{
					dataGrid.invalidateViews();
					adapter.notifyDataSetChanged();
					dataGrid.setVisibility(View.INVISIBLE);
					
					
					//status.setText("BUS IDLE");
					//if(title.compareToIgnoreCase("test")!=0)
					//{
						dataGrid.setVisibility(View.VISIBLE);
					bus.position(BUS);
					bus.title(title);
					bus.icon(bd);
					map.clear();
					map.addMarker(bus);
					//if(status.getText().toString().contains("Loading"))
					
					//if(!markerplace)
					//{
					map.moveCamera(CameraUpdateFactory.newLatLng(BUS));
					//map.moveCamera(CameraUpdateFactory.newLatLng(BUS));
					//map.moveCamera(CameraUpdateFactory.newLatLng(BUS));
					markerplace = true;
					//}
					// map.animateCamera(CameraUpdateFactory.zoomTo(19), 2000, null);

					PolylineOptions lineOptions = new PolylineOptions();

					lineOptions.addAll(maplist);
					lineOptions.width(8);
					lineOptions.color(Color.RED);


					// Drawing polyline in the Google Map for the i-th route
					map.addPolyline(lineOptions);
					//}

				}
				else
				{
					Toast.makeText(getApplicationContext(), "Error in network", Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e){
				Log.i("MapActivity","Exception while drawing line in map : " + e);
				Toast.makeText(getApplicationContext(), "Error in plotting on device", Toast.LENGTH_LONG).show();
			}
		}

	}




	public void onBackPressed() {
		stoptimertask();
		//moveTaskToBack(true);
		super.onBackPressed();
	}   


	@Override
	public void onPause() {

		markerplace = false;
		//stoptimertask();
		super.onPause();
	}

	@Override
	public void onStop() {

		markerplace = false;
		//maplist.clear();
		//stoptimertask();
		super.onStop();
	}


	@Override
	public void onStart() {

		markerplace = false;
		startTimer();
		super.onStart();
	}
	
	public void mapfullscreen(View v)
	{
		RelativeLayout.LayoutParams fullMapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		fullMapParams.setMargins(0, 0, 0, 150);
		findViewById(R.id.map).setLayoutParams(fullMapParams);
		Button resize = (Button)findViewById(R.id.Button04);
		resize.setVisibility(View.VISIBLE);
		Button resize2 = (Button)findViewById(R.id.button1);
		resize2.setVisibility(View.INVISIBLE);
	}
	
	public void alertscreen(View v)
	{
		Intent i;
		i = new Intent(MapActivity.this, AlertActivity.class);
		startActivity(i);
	}
	
	public void homescreen(View v)
	{
		onBackPressed();
	}
	
	public void contactscreen(View v)
	{
		Intent i;
		i = new Intent(MapActivity.this, SettingsActivity.class);
		startActivity(i);
	}
	
	public void maphalfscreen(View v)
	{
		RelativeLayout.LayoutParams fullMapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		fullMapParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320, getResources().getDisplayMetrics());;
		findViewById(R.id.map).setLayoutParams(fullMapParams);
		Button resize = (Button)findViewById(R.id.Button04);
		resize.setVisibility(View.INVISIBLE);
		Button resize2 = (Button)findViewById(R.id.button1);
		resize2.setVisibility(View.VISIBLE);

	}


	@Override
	public void onResume() {

		markerplace = false;
		maplist.clear();
		entries.clear();
		//startTimer();
		//startTimer2();
		super.onResume();
	}

	public void startTimer() {
		//set a new Timer
		stoptimertask();
		timer = new Timer();

		//initialize the TimerTask's job
		initializeTimerTask();

		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
		timer.schedule(timerTask, 1, 10000); //
	}


	public static void stoptimertask() {
		//stop the timer, if it's not already null
		maplist.clear();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {

				handler.post(new Runnable() {
					public void run() {
						try{
						if(DataBaseHandler.db!=null)
						{
							if(DataBaseHandler.db.getID()!=null)
							{
								if(count==0){
									//busTime bt = new busTime();
									if(DataBaseHandler.db.getUserid()!=null){
										String id = DataBaseHandler.db.getUserid();
										Log.i("bus time","getUserid value :" +id );
									//bt.execute(id);
									}
								}
								if(count==29){
									count=0;
								}
								Log.i("bus time","count :" + count);
								count++;
								buslocation bl = new buslocation();
								bl.execute("http://www.saratrak.com/sis/adminapp/appget_devices.php?uniqueId="+helper.id);
							}
							else
							{
								stoptimertask();
							}
						}
						}catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				});
			}
		};

	}



}