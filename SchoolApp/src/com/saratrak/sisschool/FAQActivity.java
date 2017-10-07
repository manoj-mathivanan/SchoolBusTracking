package com.saratrak.sisschool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.saratrak.sisschool.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class FAQActivity extends Fragment {

public FAQActivity(){};
ExpandableListAdapter listAdapter;
ExpandableListView expListView;
List<String> listDataHeader;
HashMap<String, List<String>> listDataChild;

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
   MainActivity.currentFragment="FAQActivity";
   getActivity().getActionBar().show();
    View rootView = inflater.inflate(R.layout.activity_faq, container, false);
    // get the listview
    DisplayMetrics metrics = new DisplayMetrics();
    expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
    //expListView.setIndicatorBounds(metrics.widthPixels-GetPixelFromDips(35), metrics.widthPixels-GetPixelFromDips(5));
    // preparing list data
    prepareListData();

    listAdapter = new ExpandableListAdapter(MainActivity.act, listDataHeader, listDataChild);

    // setting list adapter
    expListView.setAdapter(listAdapter);
    return rootView;
}
public int GetPixelFromDips(float pixels) {
    // Get the screen's density scale 
    final float scale = getResources().getDisplayMetrics().density;
    // Convert the dps to pixels, based on density scale
    return (int) (pixels * scale + 0.5f);
} 
/*
 * Preparing the list data
 */
private void prepareListData() {
    listDataHeader = new ArrayList<String>();
    listDataChild = new HashMap<String, List<String>>();

    // Adding child data
    listDataHeader.add("What is GPS tracking?");
    listDataHeader.add("What are the benefits of the school bus tracking system created by Saratrak?");
    listDataHeader.add("How do parents get notifications on mobile?");
    listDataHeader.add("Will I receive notifications in real-time?");
    listDataHeader.add("Is it necessary to have an internet connection to receive the notifications?");

    // Adding child data
    List<String> q1 = new ArrayList<String>();
    q1.add("GPS tracking refers to tracking the position of a person or vehicle using Global Positioning system (GPS). It is achieved with the help of GPS satellites and is commonly used by everyone in the world, from militaries to civilians, from aircrafts to ships, from explorers to adventurers.");
    List<String> q2 = new ArrayList<String>();
    q2.add("The Saratrak GPS tracker is the most advanced tracking technology mounted on school buses helps parents know the exact position of their child’s school bus."+
    		System.getProperty ("line.separator")+
    		System.getProperty ("line.separator")+
"With this app on your mobile you can get an alert 10 minutes before the school bus reaches the pick up (or drop off) point."+
System.getProperty ("line.separator")+
System.getProperty ("line.separator")+
"The real-time tracking feature allows you to see where the school bus is, on the map and the real-time video feature allows you to see your child through the camera/s installed in the bus.");
    List<String> q3 = new ArrayList<String>();
    q3.add("You get notifications regarding the location of your child’s school bus in the form of an Android push notifications or iOS push notifications in your phone.");
    List<String> q4 = new ArrayList<String>();
    q4.add("Yes, you will receive notification regarding the location of your child’s school bus in real time.");
    List<String> q5 = new ArrayList<String>();
    q5.add("Yes, it is necessary to have a working Internet connection (as WiFi or cellular data) in order to receive notifications");
    

    listDataChild.put(listDataHeader.get(0), q1); // Header, Child data
    listDataChild.put(listDataHeader.get(1), q2);
    listDataChild.put(listDataHeader.get(2), q3);
    listDataChild.put(listDataHeader.get(3), q4);
    listDataChild.put(listDataHeader.get(4), q5);
}

}
