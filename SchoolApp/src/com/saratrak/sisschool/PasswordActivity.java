package com.saratrak.sisschool;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.saratrak.sisschool.R;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordActivity extends Fragment {
    EditText oldPwd, newPwd1,newPwd2;
    Button save;
    public static String pwdChangeURL ="http://www.saratrak.com/sis/mobile/updatepassword.php",user_id;
	public PasswordActivity(){}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		MainActivity.currentFragment = "PasswordActivity"; 
		getActivity().getActionBar().show();
        View rootView = inflater.inflate(R.layout.activity_password, container, false);
        oldPwd = (EditText) rootView.findViewById(R.id.editText1);
        newPwd1 = (EditText) rootView.findViewById(R.id.editText2);
        newPwd2 = (EditText) rootView.findViewById(R.id.editText3);
        ((TextView) rootView.findViewById(R.id.textView2)).setTypeface(Fonts.roman);
	    ((TextView) rootView.findViewById(R.id.textView3)).setTypeface(Fonts.roman);
	    ((TextView) rootView.findViewById(R.id.textView4)).setTypeface(Fonts.roman);
	    ((TextView) rootView.findViewById(R.id.textView1)).setTypeface(Fonts.light);
	    ((Button) rootView.findViewById(R.id.button1)).setTypeface(Fonts.book);
        if(DataBaseHandler.db.getImage()!=null)
		{
		Bitmap bmp2 = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage(), 0, DataBaseHandler.db.getImage().length);
		ImageView image = (ImageView)rootView.findViewById(R.id.imageView2);
		image.setImageBitmap(bmp2);
		}
        save = (Button)rootView.findViewById(R.id.button1);
		save.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	if(newPwd1.getText().toString().equals("") || newPwd2.getText().toString().equals("") || oldPwd.getText().toString().equals("")){
		        		  Toast.makeText(MainActivity.act,"Please fill all the field(s)",Toast.LENGTH_SHORT).show();  
		        	}
		        	else if(!newPwd1.getText().toString().equals(newPwd2.getText().toString())){
		        		Toast.makeText(MainActivity.act,"Passwords mismatch! Please re-enter new password set",Toast.LENGTH_SHORT).show();
		        	}
		        	else{	
		        	user_id= DataBaseHandler.db.getUserid();
		        	PwdChange task = new PwdChange();
		        	task.execute();
		        	}
		        	user_id= DataBaseHandler.db.getUserid();
		        	PwdChange task = new PwdChange();
    	    	    task.execute();
		        }
		    });
        return rootView;
    }
	  private class PwdChange extends AsyncTask<String, Void, String> {
		  
		    @Override
		    protected String doInBackground(String... ags) {
				String body = "user_id="+user_id+"&oldpassword="+oldPwd.getText().toString()+"&newpassword="+newPwd1.getText().toString();
				HttpResponse httpResponse = null;
				try {
					HttpClient sClient =  new   DefaultHttpClient();
					HttpPost httpPost = new HttpPost(pwdChangeURL);
					httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
					StringEntity entity = new StringEntity(body);
					httpPost.setEntity(entity);
					httpResponse =  sClient.execute(httpPost);
					 HttpEntity response = httpResponse.getEntity(); 
					 String responseText = EntityUtils.toString(response);
					Log.i("PasswordUpdate","body :"+body +"response is :" + responseText);
					return responseText;
					 
				} catch (Exception e) {
					

				}
			
			 return null;
		    }

		    @Override
		    protected void onPostExecute(String result) {
		    	JSONArray jsonArray;
		    	JSONObject json = null;
				try {
					jsonArray = new JSONArray(result);
					 json = jsonArray.getJSONObject(0);
					 if(json.getString("status").compareToIgnoreCase("VALID")==0){
						 Toast.makeText(MainActivity.act,"Update password successful",Toast.LENGTH_SHORT).show();
					 }
					 else{
						 Toast.makeText(MainActivity.act,"Update password failed",Toast.LENGTH_SHORT).show();
					 }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

		    }
		  }
}
