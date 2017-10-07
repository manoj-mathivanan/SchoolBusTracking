package com.saratrak.sisschool;

import com.saratrak.sisschool.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FeedbackActivity extends Fragment {
	
	public FeedbackActivity(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        MainActivity.currentFragment="FeedbackActivity";
        getActivity().getActionBar().show();
        View rootView = inflater.inflate(R.layout.activity_feedback, container, false);
        
        ((TextView) rootView.findViewById(R.id.textView3)).setTypeface(Fonts.roman);
	    ((TextView) rootView.findViewById(R.id.textView2)).setTypeface(Fonts.roman);
	    ((TextView) rootView.findViewById(R.id.textView1)).setTypeface(Fonts.light);
	    ((EditText) rootView.findViewById(R.id.editText1)).setTypeface(Fonts.book);
	    ((EditText) rootView.findViewById(R.id.editText2)).setTypeface(Fonts.book);
	    ((Button) rootView.findViewById(R.id.button1)).setTypeface(Fonts.book);
         
        return rootView;
    }
}
