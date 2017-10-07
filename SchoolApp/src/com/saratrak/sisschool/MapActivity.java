package com.saratrak.sisschool;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.saratrak.sisschool.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Fragment {

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
	TextView name;
	TextView status;
	String statustext;
	TextView enginestatus;
	Typeface tf ;
	static ArrayList<TimeLineEntry> entries = new ArrayList<TimeLineEntry>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		try{
		MainActivity.currentFragment = "MapActivity"; 
		MainActivity.profileInvoke= "MapActivity";
		markerplace = false;
		rootView = inflater.inflate(R.layout.activity_home, container, false);
		DataBaseHandler.alert = "";
		ActionBar bar = getActivity().getActionBar();
		((TextView) rootView.findViewById(R.id.textViewName)).setTypeface(Fonts.medium);
		((TextView) rootView.findViewById(R.id.textViewStatus)).setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
		((TextView) rootView.findViewById(R.id.Button01)).setTypeface(Fonts.book);
		((TextView) rootView.findViewById(R.id.Button02)).setTypeface(Fonts.book);
		((TextView) rootView.findViewById(R.id.Button03)).setTypeface(Fonts.book);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy);
		//DataBaseHandler.db.setGCMText("test");
		//DataBaseHandler.db.setGCMTime(System.currentTimeMillis()+"");

		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.header_image3x);
		BitmapDrawable background = new BitmapDrawable(bmp);
		//background.setTileModeY(android.graphics.Shader.TileMode.REPEAT); 
		//background.setGravity(119);
		background.setGravity(17);
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		bar.setBackgroundDrawable(background);
		bar.setDisplayShowTitleEnabled(false);



		//entries.clear();
		//maplist.clear();
		//entries.add(new TimeLineEntry("Left from School","1:47 pm",""));
		//entries.add(new TimeLineEntry("Loading..","12:00 am",""));
		adapter = new adaptermap(getActivity().getApplicationContext(),entries);
		dataGrid = (ListView) rootView.findViewById(R.id.listView1);
		dataGrid.setAdapter(adapter);
		dataGrid.setVisibility(View.INVISIBLE);
		name = (TextView) rootView.findViewById(R.id.textViewName);
		status = (TextView) rootView.findViewById(R.id.textViewStatus);
		status.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
		 tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-BOOK.OTF");
		
		enginestatus = (TextView) rootView.findViewById(R.id.TextView01);
        
		ImageView image = (ImageView) rootView.findViewById(R.id.imageButton1);
		try{
			if(DataBaseHandler.db!=null)
			{
				name.setText(DataBaseHandler.db.getName());
				if(statusText!=null){
					//status.setText(statusText);
					status.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
					
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


		Typeface tf1a = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-MEDIUM.OTF");
		TextView tv = (TextView) rootView.findViewById(R.id.textViewName);
		tv.setTypeface(tf1a);

		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHTOBLIQUE.OTF");
		TextView tv2 = (TextView) rootView.findViewById(R.id.textViewStatus);
		tv2.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));

		//startTimer();
		//startTimer2();
		try{

			MapFragment mf = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
			if(mf==null)
				mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
			map = mf.getMap();
			map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(19.2617731,72.8763325)));
			map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
			map.setMyLocationEnabled(true);
			bus  = new MarkerOptions();
			//map.addMarker(new MarkerOptions()
		   // .position(BUS)
		   // .title("Singapore International School"));
		}
		catch(Exception e){
			Log.i("MapActvity","Exception while setting map : "+e);
		}

		/*
		if(map!=null){

            // Enable MyLocation Button in the Map

            LatLng origin =  new LatLng(53.558, 9.927);
            LatLng dest = new LatLng(53.551, 9.993);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);


        }
		 */
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return rootView;
	}
	
	@Override
	public void onDestroyView() {

	    FragmentManager fm = getFragmentManager();

	    Fragment xmlFragment = fm.findFragmentById(R.id.map);
	    if (xmlFragment != null) {
	        fm.beginTransaction().remove(xmlFragment).commit();
	    }
	    stoptimertask();
	    super.onDestroyView();
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
	private class busTime extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... id) {
			Log.i("bus time","input :"+id[0]);
			String body = "user_id="+id[0];
			HttpResponse httpResponse = null;
			String responseText = null;
			String time = null;
			try {
				HttpClient sClient =  new   DefaultHttpClient();
				HttpPost httpPost = new HttpPost("http://www.saratrak.com/sis/mobile/arrivalduration.php");
				httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
				StringEntity entity = new StringEntity(body);
				httpPost.setEntity(entity);
				httpResponse =  sClient.execute(httpPost);
				 HttpEntity response = httpResponse.getEntity(); 
				 responseText = EntityUtils.toString(response);
				Log.i("bus time","body :"+body +"response is :" + responseText);
				 
			     try {
			    	 int len = responseText.length();
			    	 String parsedInput = responseText.substring(1, len-1);
		    	 JSONObject jObj = new JSONObject(parsedInput);
		    	   
			    	 time=jObj.get("time").toString();  
//					
					Log.i("bus time","time  :" + time);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				 
			} catch (Exception e) {
				e.printStackTrace();

			}
			return time;
		}
		@Override
		protected void onPostExecute(String time) {
			if(time!=null && time !="")
			{
				statusText = "Arriving approx. in "+time;
			status.setText(statusText);
			status.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
			}else
			{
				statusText="";
				status.setText(statusText);
				status.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF"));
			}
		}
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

				Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.bus_icon);
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
					
					try{
						if(DataBaseHandler.db!=null)
						{
							if(System.currentTimeMillis()-Long.parseLong(DataBaseHandler.db.getGCMTime())<600000)
							{
								//status.setText(DataBaseHandler.db.getGCMText());
							}
						}
					}
					catch(Exception e)
					{
						
					}
					
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
					status.setText("Error in Network communication");
				}
			}
			catch(Exception e){
				Log.i("MapActivity","Exception while drawing line in map : " + e);
			}
		}

	}




	public void onBackPressed() {
		//stoptimertask();

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


	@Override
	public void onResume() {

		markerplace = false;
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
								if(count==29){
									count=0;
								}
								if(count==0){
									busTime bt = new busTime();
									if(DataBaseHandler.db.getUserid()!=null){
										String id = DataBaseHandler.db.getUserid();
										Log.i("bus time","getUserid value :" +id );
									bt.execute(id);
									}
								}
								
								//Log.i("bus time","count :" + count);
								count++;
								buslocation bl = new buslocation();
								bl.execute("http://www.saratrak.com/sis/mobile/appget_devices.php?uniqueId="+DataBaseHandler.db.getID());
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