package com.saratrak.sisschool;

import com.saratrak.sisschool.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.view.View;

public class SettingsActivity extends Fragment {
	public SettingsActivity(){}
	ListView list;
	TextView settings;
	String[] itemname ={
			"Tutorial",
			"Profile",
			"Feedback",
			"FAQ",
			"About US"
	};

	Integer[] imgid={
			R.drawable.tutorial_icon,
			R.drawable.settings_profile_icon,
			R.drawable.settings_feedback_icon,
			R.drawable.settings_faq_icon,
			R.drawable.settings_about_us_icon,
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity.currentFragment = "SettingsActivity"; 
		MainActivity.profileInvoke = "SettingsActivity";
		getActivity().getActionBar().show();
		View rootView = inflater.inflate(R.layout.activity_settings, container, false);
		SettingsListAdapter adapter=new SettingsListAdapter(MainActivity.act, itemname, imgid);
		settings = (TextView)rootView.findViewById(R.id.textView1);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF");
		settings.setTypeface(tf);
		try{
		if(DataBaseHandler.db.getImage()!=null)
		{
		Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);
		ImageView image = (ImageView) rootView.findViewById(R.id.imageView2);
		image.setImageBitmap(bmp2);
		}
		}
		catch(Exception e){
			Log.i("SettingsActivity","Exception while setting bitmap : "+e);
		}
		list=(ListView)rootView.findViewById(R.id.listView1);
		list.setClickable(true);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean flag = false;
				// TODO Auto-generated method stub
				String Selecteditem= itemname[+position];
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				Fragment fragment = null;
				if(Selecteditem=="Tutorial"){
					Intent i = new Intent(MainActivity.act, TutorialActivity.class);
					i.putExtra("back", "settings");
        			startActivity(i);
					flag = false;
				}
				else if(Selecteditem=="Profile"){
					fragment = new ProfileActivity();
					flag = true;
				}
				else if(Selecteditem=="Feedback"){
					Intent intent=new Intent(Intent.ACTION_SEND);
					String[] recipients={"sistracko@saratrak.com","natasha@saratrak.com","transport@sisindia"};
					intent.putExtra(Intent.EXTRA_EMAIL, recipients);
					intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for app");
					intent.putExtra(Intent.EXTRA_TEXT,"Enter your feedback");
					intent.setType("text/html");
					startActivity(Intent.createChooser(intent, "Send mail"));
				}
				else if(Selecteditem=="About US"){
					fragment = new AboutUsActivity();
					flag = true;
				}
				else if(Selecteditem=="FAQ"){
					fragment = new FAQActivity();
					flag = true;
				}
				if(flag== true){
					FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.frame_container, fragment);
					fragmentTransaction.addToBackStack("SettingsActivity");
					fragmentTransaction.commit();
				}
			}
		});
		return rootView;
	}
	
	
}
