package com.saratrak.sisschool;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.saratrak.sisschool.R;
import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends Activity {
	public Button login;
	public EditText username,password;
	public final String senderID= "648886547442";
	public static String gcmRegID="", studentID,studentName;
	public static String onboardingURL= "http://www.saratrak.com/sis/mobile/applogin.php";
	public static String updateURL = "http://www.saratrak.com/sis/mobile/updateregid.php";
	public String existingUser;
	public String address = "";
	public boolean tutorialFlag = true;
	private ProgressBar spinner; 
	static int attempgcm = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		boolean flag = false;
		super.onCreate(savedInstanceState);
		attempgcm = 0;
		setContentView(R.layout.activity_login);
		getActionBar().hide();
		spinner = (ProgressBar) findViewById(R.id.progressBar1);
		username = (EditText) findViewById(R.id.editText1);
    	password = (EditText) findViewById(R.id.editText2);
		login = (Button) findViewById(R.id.button1);
		
		Fonts.init(this);
	    ((TextView) findViewById(R.id.textView1)).setTypeface(Fonts.light);
	    ((TextView) findViewById(R.id.textView2)).setTypeface(Fonts.roman);
	    ((TextView) findViewById(R.id.textView3)).setTypeface(Fonts.roman);
	    ((EditText) findViewById(R.id.editText1)).setTypeface(Fonts.book);
	    ((EditText) findViewById(R.id.editText2)).setTypeface(Fonts.book);
	    ((Button) findViewById(R.id.button1)).setTypeface(Fonts.book);
	    ((Button) findViewById(R.id.button2)).setTypeface(Fonts.medium);
	    
		//one time initialising the variable
		DataBaseHandler.db = new DataBaseHandler(getApplicationContext());
		
		existingUser = DataBaseHandler.db.getID();
		if(existingUser!=null && (existingUser.compareToIgnoreCase("")!=0)){
			spinner.setVisibility(View.GONE); 
			navigate();
		}
		else
			tutorialFlag=false;
		login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	    spinner.setVisibility(View.VISIBLE); 
                	GCMRegistration task = new GCMRegistration();
    	    	    task.execute("login");
    	    	    
                //}
            }
        });
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
	}
	public void navigate(){
		Intent i;
		if(tutorialFlag){
		 i = new Intent(LoginActivity.this, MainActivity.class);
		}
		else
		{
			tutorialFlag = true;
	     i = new Intent(LoginActivity.this, TutorialActivity.class);
	     i.putExtra("back", "main");
		}
		startActivity(i);



	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(existingUser!=null && (existingUser.compareToIgnoreCase("")!=0)){
		GCMRegistration task = new GCMRegistration();
	    task.execute("resume");
		}
	}
	
	public void forgotpassword(View v)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
		View promptView = layoutInflater.inflate(R.layout.forgotpassword, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
		alertDialogBuilder.setView(promptView);

		final EditText userInput = (EditText) promptView
				.findViewById(R.id.editTextDialogUserInput);

		userInput.setText(username.getText().toString());
		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    	try
			    	{
			    	HttpClient sClient =  new   DefaultHttpClient();
			    	HttpPost httpPost = new HttpPost("http://www.saratrak.com/sis/mobile/getpassword.php");
			    	httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					 String body = "user_id=" + userInput.getText().toString();;
					 httpPost.setEntity(new StringEntity(body));
					 HttpResponse httpResponse =  sClient.execute(httpPost);
					 HttpEntity response = httpResponse.getEntity(); 
					 String responseText = EntityUtils.toString(response);
					 JSONArray jsonArray = new JSONArray(responseText);
					 JSONObject json = jsonArray.getJSONObject(0);
					 if(json.getString("status").compareToIgnoreCase("VALID")==0)
					 {
						 String pwd = json.getString("pwd");   	
						 password.setText(pwd);
						 username.setText(userInput.getText().toString()); 
					 }
					 else
					 {
						 Toast.makeText(getApplicationContext(), "User ID not valid", Toast.LENGTH_LONG).show();
					 }
			    	}
			    	catch(Exception e)
			    	{
			    		e.printStackTrace();
			    		Toast.makeText(getApplicationContext(), "Forgot Password failed", Toast.LENGTH_LONG).show();
			    	}
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}


	
	private class GCMRegistration extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String... ags) {
			return registerGCM(ags[0]);
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    Log.i("Login"," task type "+ result);
	     if(result.equals("login")){
	    	 Log.i("Login","executing login request");
	    	 Onboarding task = new Onboarding();
	    	    task.execute(new String[] { });
	     }
	     else{
	    	 // for resume
	     }
	    }
	  }
	  private class Onboarding extends AsyncTask<String, Void, String> {
		  
		    @Override
		    protected String doInBackground(String... ags) {
				 Log.i("Onboarding","inside registration with gcm id as" + gcmRegID);
	             String flag ="";
	             
				String body = "username="+username.getText().toString()+"&password="+password.getText().toString()+"&regid="+gcmRegID+"&phonetype=android";
				Log.i("Onboarding", "body is :" + body);
				HttpResponse httpResponse = null;
				try {
					HttpClient sClient =  new   DefaultHttpClient();
					HttpPost httpPost = new HttpPost(onboardingURL);
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					try {

						StringEntity entity = new StringEntity(body);
						httpPost.setEntity(entity);
						httpResponse =  sClient.execute(httpPost);
						int status = httpResponse.getStatusLine().getStatusCode();
						 HttpEntity response = httpResponse.getEntity(); 
						 String responseText = EntityUtils.toString(response);
						 if(status == 200) {
							 Log.i("Onboarding","Onboarding Successful, with response as :" + responseText);
							 JSONArray jsonArray = new JSONArray(responseText);
							 JSONObject json = jsonArray.getJSONObject(0);
							 studentID=  json.getString("uniqueID");   	
							 studentName=  json.getString("studentName"); 
							 Log.i("Onboarding","Student Name :"+ studentName +"  Stundent Id:"+ studentID);
							 
							 HttpPost httpPostaddress = new HttpPost("http://www.saratrak.com/sis/mobile/getaddress.php");
							 httpPostaddress.setHeader("Content-Type", "application/x-www-form-urlencoded");
							 body = "user_id=" + username.getText().toString();
							 httpPostaddress.setEntity(new StringEntity(body));
							 httpResponse =  sClient.execute(httpPostaddress);
							 response = httpResponse.getEntity(); 
							 responseText = EntityUtils.toString(response);
							 jsonArray = new JSONArray(responseText);
							 json = jsonArray.getJSONObject(0);
							 if(json.getString("status").compareToIgnoreCase("VALID")==0)
								 address = json.getString("address");   	
							 else
								 address = "NO ADDRESS SET";
						}
						else
						{
							Log.i("Onboarding","Onboarding failed, with Response as :" + responseText);
							return null;
						}
						
					} catch (ClientProtocolException e) {
						Log.i("Onboarding","Inside client protocol exception :" + e);
						return null;
					}
				} catch (Exception e) {
					
					Log.i("Onboarding","Inside exception :" + e);
					return null;
				}
			
			 return flag;
		    }

		    @Override
		    protected void onPostExecute(String result) {
		     if(result!=null){
		    	 if(studentName!=null)
		    	 {
	                DataBaseHandler.db.setNameIDRegID(studentName, studentID, gcmRegID,username.getText().toString());
		    		 //DataBaseHandler.db.setNameIDRegID(studentName, "240720151", gcmRegID,username.getText().toString());
	                DataBaseHandler.db.setAddress(address);
	                UpdateGCM task = new UpdateGCM();
		    	    task.execute(new String[] { });
	                spinner.setVisibility(View.GONE); 
	               navigate();
		    	 }  
		     }
		     else
		     {
		    	 spinner.setVisibility(View.GONE); 
		    	 Toast.makeText(getApplicationContext(), "Onboarding Failed", Toast.LENGTH_LONG).show();
		     }
		    }
		  }
	  
	  private class UpdateGCM extends AsyncTask<String, Void, String> {
		  
		    @Override
		    protected String doInBackground(String... ags) {
				 Log.i("UpdateGCM","inside registration with gcm id as" + gcmRegID);
	             String flag ="";
	             runOnUiThread(new Runnable() {
                     @Override
                     public void run() {

                     	 //Toast.makeText(getApplicationContext(),"GCM Registration ID is: " +  gcmRegID, Toast.LENGTH_LONG).show();

                    }
                });
				String body = "user_id="+username.getText().toString()+"&regid="+gcmRegID;
				Log.i("UpdateGCM", "body is :" + body);
				HttpResponse httpResponse = null;
				try {
					HttpClient sClient =  new   DefaultHttpClient();
					HttpPost httpPost = new HttpPost(updateURL);
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					try {

						StringEntity entity = new StringEntity(body);
						httpPost.setEntity(entity);
						httpResponse =  sClient.execute(httpPost);
						int status = httpResponse.getStatusLine().getStatusCode();
						 HttpEntity response = httpResponse.getEntity(); 
						 String responseText = EntityUtils.toString(response);
						 if(status == 200) {
							 JSONArray jsonArray = new JSONArray(responseText);
							 JSONObject json = jsonArray.getJSONObject(0);
							 String updateStatus=  json.getString("status");
							 
							 Log.i("UpdateGCM","updateStatus  :"+ updateStatus );
						}
						else
						{
							Log.i("UpdateGCM","UpdateGCM failed, with Response as :" + responseText);
							return null;
						}
						
					} catch (ClientProtocolException e) {
						Log.i("UpdateGCM","Inside client protocol exception :" + e);
						return null;
					}
				} catch (Exception e) {
					Log.i("UpdateGCM","Inside exception :" + e);
					return null;
				}
			
			 return flag;
		    }

		    @Override
		    protected void onPostExecute(String result) {
		     if(result!=null){
		    	//use like this in all classes. db is static variable
		    	 if(existingUser!=null && (existingUser.compareToIgnoreCase("")!=0)){
	                DataBaseHandler.db.setGCM(gcmRegID);
	                spinner.setVisibility(View.GONE); 
	               //navigate();
		    	 }
		     }
		     else
		     {
		    	 spinner.setVisibility(View.GONE); 
		    	 Toast.makeText(getApplicationContext(), "gcm update failed", Toast.LENGTH_LONG).show();
		     }
		    }
		  }

 public String registerGCM(String taskType){
	    GCMRegistrar.checkDevice(this.getBaseContext());
        GCMRegistrar.checkManifest(this.getBaseContext());
        String gcmRegIDNew = GCMRegistrar.getRegistrationId(this.getBaseContext());
        if(!gcmRegIDNew.equals(gcmRegID)){
        	gcmRegID = gcmRegIDNew;
        	 UpdateGCM task = new UpdateGCM();
	    	    task.execute(new String[] { });
        }
        Log.i("GCMReg","registering to GCM");
          GCMRegistrar.register(this.getBaseContext(), senderID);
        Log.i("GCM","GCM registration Id :" + gcmRegID + "for task type :" + taskType);
        if((gcmRegID.isEmpty())&&(attempgcm<3))
        {
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	attempgcm++;
        	registerGCM(taskType);
        }
        return taskType;
 }
	
}
