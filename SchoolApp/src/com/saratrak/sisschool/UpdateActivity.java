package com.saratrak.sisschool;

import com.saratrak.sisschool.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UpdateActivity  extends Fragment {
	
	public UpdateActivity(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		MainActivity.currentFragment = "UpdateActivity"; 
        View rootView = inflater.inflate(R.layout.activity_update, container, false);
         
        return rootView;
    }
}
