package com.saratrak.sisschool.admin;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class SettingsActivity extends Activity {
	public SettingsActivity(){}
	ListView list;

	String[] itemname ={
			"Feedback",
			"About Us",
			"Logout",
	};

	Integer[] imgid={
			R.drawable.settings_feedback_icon,
			R.drawable.settings_about_us_icon,
			R.drawable.settings_faq_icon
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.show();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.header_image3x);
		BitmapDrawable background = new BitmapDrawable(bmp);
		background.setGravity(17);
		bar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
		bar.setBackgroundDrawable(background);
		bar.setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_settings);
		SettingsListAdapter adapter=new SettingsListAdapter(SettingsActivity.this, itemname, imgid);
		
		try{
		if(DataBaseHandler.db.getImage()!=null)
		{
		Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);
		ImageView image = (ImageView)findViewById(R.id.imageView2);
		image.setImageBitmap(bmp2);
		}
		}
		catch(Exception e){
			Log.i("SettingsActivity","Exception while setting bitmap : "+e);
		}
		list=(ListView)findViewById(R.id.listView1);
		list.setClickable(true);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String Selecteditem= itemname[+position];
				if(Selecteditem=="About Us"){
					Intent i;
					i = new Intent(SettingsActivity.this, AboutUsActivity.class);
					startActivity(i);
				}
				else if(Selecteditem=="Logout"){
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
						Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
			            startActivity(i);
				}
				else if(Selecteditem=="Feedback"){
					Intent intent=new Intent(Intent.ACTION_SEND);
					String[] recipients={"sistracko@saratrak.com","natasha@saratrak.com","transport@sisindia.com"};
					intent.putExtra(Intent.EXTRA_EMAIL, recipients);
					intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback for admin app");
					intent.putExtra(Intent.EXTRA_TEXT,"Enter your feedback");
					intent.setType("text/html");
					startActivity(Intent.createChooser(intent, "Send mail"));
				}
			}
		});
	}
	
	
}
