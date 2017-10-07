package com.saratrak.sisschool.admin;

import com.saratrak.sisschool.admin.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AboutUsActivity  extends Activity {
	public AboutUsActivity(){}
	TextView aboutUs;
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
		setContentView(R.layout.activity_about_us);
        
        aboutUs = (TextView)findViewById(R.id.textView1);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/AVENIRLTSTD-BOOK.OTF");
        String aboutUsText = "Saratrak is a company of highly talented professionals who push GPS tracking technology to the next level."+System.getProperty ("line.separator")+
        		"We create, innovate and re-innovate all over again to ensure the creativity and quality of our products is way ahead of competition."+System.getProperty ("line.separator")+
        		"Our service, too, is better than the best in the world and all our products are designed for Android platform."+System.getProperty ("line.separator")+
        		"We are headquartered in Mumbai but we have presence in all major cities and TIER-2 cities in India." ;
        aboutUs.setText(aboutUsText);
        aboutUs.setTypeface(tf);
    }
	
	public void openbrowser(View v)
	{
		Uri uri = Uri.parse("http://www.saratrak.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}
