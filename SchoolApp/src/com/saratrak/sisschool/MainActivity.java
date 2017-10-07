package com.saratrak.sisschool;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.saratrak.sisschool.R;
import com.saratrak.sisschool.adapter.NavDrawerListAdapter;
import com.saratrak.sisschool.model.NavDrawerItem;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerRelativeLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	static int count = 0;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
    public static Context context;
    public static Activity act;
    public static String currentFragment; 
    public static String profileInvoke;
    	 @Override
    	    public void onBackPressed() {
//    	    	count++;
//    	    	if(count<2)
//    	    	{
//    	    	Fragment fragment = new MapActivity();
//    	    	FragmentManager fragmentManager = getFragmentManager();
//    			fragmentManager.beginTransaction()
//    					.replace(R.id.frame_container, fragment).commit();
//    	    	}
//    	    	else
//    	    		moveTaskToBack(true);
    	    	boolean flag = false;
    			// TODO Auto-generated method stub
    			String Selecteditem= currentFragment;
    			FragmentManager fragmentManager = getFragmentManager();
    			Fragment fragment = null;
    			if(Selecteditem=="MapActivity"){
    				moveTaskToBack(true);
    			}
    			else if(Selecteditem=="ProfileActivity"){
    				if(profileInvoke=="MapActivity")
    				fragment = new MapActivity();
    				else
    				fragment = new SettingsActivity();
    				flag = true;
    			}
    			else if(Selecteditem=="SettingsActivity"){
    				fragment = new MapActivity();
    				flag= true;
    			}
    			else if(Selecteditem=="PasswordActivity"){
    				fragment = new ProfileActivity();
    				flag = true;
    			}
    			else if(Selecteditem=="AlertActivity"){
    				fragment = new MapActivity();
    				flag = true;
    			}
    			else if(Selecteditem=="FeedbackActivity"){
    				fragment=new SettingsActivity();
    				flag = true;
    			}
    			else if(Selecteditem=="AboutUsActivity"){
    				fragment=new SettingsActivity();
    				flag = true;
    			}
    			else if(Selecteditem=="VideoActivity"){
    				fragment=new MapActivity();
    				flag = true;
    			}
    			else if(Selecteditem=="FAQActivity"){
    				fragment=new SettingsActivity();
    				flag = true;
    			}
    			if(flag== true){
    				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    				fragmentTransaction.replace(R.id.frame_container, fragment);
    				fragmentTransaction.addToBackStack(null);
    				fragmentTransaction.commit();
    			}
    	    	
    	    }
    	     

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().show();
		/*ActionBar bar = getActionBar();
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		ActionBar.LayoutParams p = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		p.gravity= Gravity.CENTER;

*/
		
		context = getApplicationContext();
		act = this;
		setContentView(R.layout.activity_main);
		

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		
		TextView tv = (TextView)findViewById(R.id.textView);
		
		tv.setTypeface(Fonts.light);
		tv.setTextColor(Color.parseColor("#ab850c"));
		try{
		if(DataBaseHandler.db!=null)
		{
			tv.setText(DataBaseHandler.db.getName());
		if(DataBaseHandler.db.getImage()!=null)
		{
		Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);
		ImageView image = (ImageView)findViewById(R.id.imageViewkidpic);
		image.setImageBitmap(bmp2);
		}
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerRelativeLayout = (RelativeLayout) findViewById(R.id.left_drawer);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.home_menu_icon2x, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) { 
				try{
				if(DataBaseHandler.db!=null)
				{
				if(DataBaseHandler.db.getImage()!=null)
				{
				Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);
				ImageView image = (ImageView)findViewById(R.id.imageViewkidpic);
				image.setImageBitmap(bmp2);
				}
				else
				{
					ImageView image = (ImageView)findViewById(R.id.imageViewkidpic);
					image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.kidpic));
				}
				}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
			
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		try{
			if(MapActivity.maplist!=null)
				MapActivity.stoptimertask();
			}
			catch(Exception e)
			{
				 
			}
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		try{
			if(MapActivity.maplist!=null)
				MapActivity.maplist.clear();
			}
			catch(Exception e)
			{
				
			}
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		ImageView img1 = (ImageView)findViewById(R.id.imageButton1);
		ImageView img2 = (ImageView)findViewById(R.id.imageButton2);
		img1.setVisibility(View.INVISIBLE);
		img2.setVisibility(View.INVISIBLE);
	}
	
	public void openbrowser(View v)
	{
		Uri uri = Uri.parse("http://www.saratrak.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void profilesettings(View v)
	{
		Fragment fragment = new ProfileActivity();
    	FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
	}
	
	public void maphalfscreen(View v)
	{
		RelativeLayout.LayoutParams fullMapParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		fullMapParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 260, getResources().getDisplayMetrics());;
		findViewById(R.id.map).setLayoutParams(fullMapParams);
		Button resize = (Button)findViewById(R.id.Button04);
		resize.setVisibility(View.INVISIBLE);
		Button resize2 = (Button)findViewById(R.id.button1);
		resize2.setVisibility(View.VISIBLE);
		ImageView img1 = (ImageView)findViewById(R.id.imageButton1);
		ImageView img2 = (ImageView)findViewById(R.id.imageButton2);
		img1.setVisibility(View.VISIBLE);
		img2.setVisibility(View.VISIBLE);
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */


	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		count =0;
		switch (position) {
		case 0:
			fragment = new MapActivity();
			break;
		case 1:
			fragment = new AlertActivity();
			break;
		case 2:
			//fragment = new VideoActivity();
			Uri uriUrl = Uri.parse("http://pc.vigocam.com/");
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(launchBrowser); 
			break;
		case 3:
			fragment = new SettingsActivity();
			break;
		case 4:
			try{
			DataBaseHandler.db.cleardb();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			if(MapActivity.entries!=null)
			{
				MapActivity.entries.clear();
				MapActivity.maplist.clear();
			}
			Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerRelativeLayout);
			//mDrawerLayout.closeDrawer(mDrawerList);
			
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void alertscreen(View v)
    {
		count = 0;
    	Fragment fragment = new AlertActivity();
    	FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
    }
    
    public void videoscreen(View v)
    {
    	count = 0;
    	Uri uriUrl = Uri.parse("http://pc.vigocam.com/");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);
    	/*Fragment fragment = new VideoActivity();
    	FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();*/
    }
    
    public void settingscreen(View v)
    {
    	count = 0;
    	Fragment fragment = new SettingsActivity();
    	FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
    }
    

}
