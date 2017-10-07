package com.saratrak.sisschool;

import com.saratrak.sisschool.R;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AboutUsActivity  extends Fragment {
	public AboutUsActivity(){}
	TextView aboutUs;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        MainActivity.currentFragment="AboutUsActivity";
        getActivity().getActionBar().hide();
        View rootView = inflater.inflate(R.layout.activity_about_us, container, false);
        aboutUs = (TextView)rootView.findViewById(R.id.textView1);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/AVENIRLTSTD-BOOK.OTF");
        String aboutUsText = "Saratrak is a company of highly talented professionals who push GPS tracking technology to the next level."+System.getProperty ("line.separator")+
        		"We create, innovate and re-innovate all over again to ensure the creativity and quality of our products is way ahead of competition."+System.getProperty ("line.separator")+
        		"Our service, too, is better than the best in the world and all our products are designed for Android platform."+System.getProperty ("line.separator")+
        		"We are headquartered in Mumbai but we have presence in all major cities and TIER-2 cities in India." ;
        aboutUs.setText(aboutUsText);
        aboutUs.setTypeface(tf);
        return rootView;
    }
}
