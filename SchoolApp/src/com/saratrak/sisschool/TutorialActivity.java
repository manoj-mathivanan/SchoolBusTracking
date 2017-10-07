package com.saratrak.sisschool;


import com.saratrak.sisschool.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class TutorialActivity extends Activity {
	ImageView iv;
	Button cancel,next;
	int count;
	String existingUser;
	String swap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String back = getIntent().getStringExtra("back");
		Log.i("Tutorial"," back to screen is :" + back);
		MainActivity.currentFragment="TutorialActivity";
		count=0;
		setContentView(R.layout.activity_tutorial);
		iv = (ImageView)findViewById(R.id.imageView1);
		cancel =(Button)findViewById(R.id.button1);
		try{
		existingUser = DataBaseHandler.db.getID();
		}
		catch(Exception e){
			Log.i("Tutorial","Exception while DB fetch :" + e);
		}
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("Tutorial","Count" + count);
				if(swap=="next")
					count = count-1;
				else
					count--;
				count = count%7;
				swap = "back";
				switch (count){
				case -1:
					if(back.equals("main")){
						Intent i = new Intent(TutorialActivity.this, MainActivity.class);
						startActivity(i);
					}
					else if(back.equals("settings"))
					{
						moveToSettings();
					}
					break;

				case 0:
					next.setText("YES");
					cancel.setText("CANCEL");
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_03x));
					//iv.setBackgroundResource(R.drawable.tutorial_13x);
					break;
				case 1:
					next.setText("NEXT");
					cancel.setText("BACK");
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_13x));
					//iv.setBackgroundResource(R.drawable.tutorial_13x);
					break;
				case 2:
					//iv.setBackgroundResource(R.drawable.tutorial_23x);
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_23x)); 
					break;
				case 3:
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_33x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_33x);
					break;
				case 4:
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_43x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_43x);
					break;
				case 5:
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_53x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_53x);
					break;
				case 6:
					next.setText("GOT IT");
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_63x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_63x);
					break;



				default:
					break;
				}
			}
		});
		next = (Button)findViewById(R.id.button2);
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i("Tutorial","Count" + count);
				if(swap=="back")
					count = count+1;
				else
					count++;
				count = count%7;
				swap="next";
				switch (count){
				case 1:
					next.setText("NEXT");
					cancel.setText("BACK");
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_13x));
					//iv.setBackgroundResource(R.drawable.tutorial_13x);
					break;
				case 2:
					//iv.setBackgroundResource(R.drawable.tutorial_23x);
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_23x)); 
					break;
				case 3:
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_33x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_33x);
					break;
				case 4:
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_43x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_43x);
					break;
				case 5:
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_53x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_53x);
					break;
				case 6:
					next.setText("GOT IT");
					iv.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.tutorial_63x)); 
					//iv.setBackgroundResource(R.drawable.tutorial_63x);
					break;
				case 0:

					if(back.equals("main")){
						Intent i = new Intent(TutorialActivity.this, MainActivity.class);
						startActivity(i);
					}
					else if(back.equals("settings"))
					{
						moveToSettings();
					}
					break;
				default:
					break;
				}

				;
			}
		});
	}
    public void moveToSettings(){
//    	FragmentManager fragmentManager = MainActivity.act.getFragmentManager();
//		Fragment fragment =  new SettingsActivity();
//		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		fragmentTransaction.replace(R.id.frame_container, fragment);
//		fragmentTransaction.addToBackStack(null);
//		fragmentTransaction.commit();
    	
    	int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
