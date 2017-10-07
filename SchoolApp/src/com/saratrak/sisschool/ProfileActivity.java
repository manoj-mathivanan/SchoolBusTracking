package com.saratrak.sisschool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.saratrak.sisschool.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity  extends Fragment  {
	protected static final int REQUEST_CAMERA = 0;
	protected static final int SELECT_FILE = 1;
	protected static final int PICTURE_RESULT = 5;
	protected static final int PIC_CROP = 6;
	ImageView updatePic;
	ImageView  profilePic;
	ContentValues values;
	String addresstext;
	EditText address;
	TextView changePwd;
	Uri imageUri;
	Uri outputFileUri;
	public ProfileActivity(){}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity.currentFragment = "ProfileActivity"; 
		getActivity().getActionBar().show();
		View rootView = inflater.inflate(R.layout.activity_profile, container, false);


		EditText name = (EditText)rootView.findViewById(R.id.editText1);
		name.setText(DataBaseHandler.db.getUserid());
		address = (EditText)rootView.findViewById(R.id.editText2);
		address.setText(DataBaseHandler.db.getAddress());
		profilePic = (ImageView)rootView.findViewById(R.id.imageView2);
		try{
		if(DataBaseHandler.db.getImage()!=null)
		{
			Bitmap bitmap = BitmapFactory.decodeByteArray(DataBaseHandler.db.getImage() , 0, DataBaseHandler.db.getImage().length);
			profilePic.setImageBitmap(bitmap);
		}
		}
		catch(Exception e){
			Log.i("ProfileActivity","Exception while accessing DB :" + e);
		}
		        updatePic = (ImageView)rootView.findViewById(R.id.imageButton1);
				updatePic.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
		
						selectImage();
					}
				});
				
		profilePic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				selectImage();
			}
		});

		Button chaddress = (Button)rootView.findViewById(R.id.button1);
		chaddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//addresstext = address.getText().toString();
				//Updateaddress update = new Updateaddress();
				//update.execute("http://www.saratrak.com/sis/mobile/submitaddress.php");
				Toast.makeText(getActivity(), "Profile Picture saved", Toast.LENGTH_LONG).show();
				getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
				getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
			}
		});

		changePwd=(TextView) rootView.findViewById(R.id.TextView01);
		changePwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				Fragment fragment =  new PasswordActivity();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.replace(R.id.frame_container, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});

		((TextView) rootView.findViewById(R.id.textView2)).setTypeface(Fonts.roman);
		((TextView) rootView.findViewById(R.id.textView3)).setTypeface(Fonts.roman);
		((TextView) rootView.findViewById(R.id.textView4)).setTypeface(Fonts.roman);
		((TextView) rootView.findViewById(R.id.TextView01)).setTypeface(Fonts.roman);

		((EditText) rootView.findViewById(R.id.editText1)).setTypeface(Fonts.book);
		((EditText) rootView.findViewById(R.id.editText2)).setTypeface(Fonts.book);

		((TextView) rootView.findViewById(R.id.textView2)).setTypeface(Fonts.light);

		((Button) rootView.findViewById(R.id.button1)).setTypeface(Fonts.book);

		return rootView;
	}




	private class Updateaddress extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... url) {
			try{
				HttpClient client = new DefaultHttpClient();
				HttpPost send_call = new HttpPost(url[0]);
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
				nameValuePair.add(new BasicNameValuePair("user_id", DataBaseHandler.db.getUserid()));
				nameValuePair.add(new BasicNameValuePair("address", addresstext));
				send_call.setEntity(new UrlEncodedFormEntity(nameValuePair));
				BasicHttpResponse send_call_response = (BasicHttpResponse) client.execute(send_call);
				String response = EntityUtils.toString(send_call_response.getEntity());
				DataBaseHandler.db.setAddress(addresstext);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			if(result!=null)
				Toast.makeText(getActivity(), "Address updated", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getActivity(), "Address update failed", Toast.LENGTH_LONG).show();
		}
	}


	private void selectImage() {
		try{
		final CharSequence[] items = { "Use Camera", "Select from Photo Gallery","Remove Photo", "CANCEL" };
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.act);
		builder.setTitle("Add Profile Pic");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Use Camera")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
					  outputFileUri = Uri.fromFile(file);
					  intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					//					//intent.putExtra(MediaStore.EXTRA_OUTPUT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, REQUEST_CAMERA);

				} else if (items[item].equals("Select from Photo Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 0);
					intent.putExtra("aspectY", 0);
					intent.putExtra("outputX", 150);
					intent.putExtra("outputY", 150);
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("CANCEL")) {
					dialog.dismiss();
				}else 
				{
					profilePic.setImageResource(R.drawable.kidpic);
					DataBaseHandler.db.clearImage();
				}
			}
		});
		builder.show();
		}
		catch(Exception e){
			Log.i("Profile","Exception while selecting image : "+ e);
		}
	}
	public Bitmap getRoundedShape(Bitmap bitmap) {
		try{
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		Rect rect;
		if(bitmap.getWidth()<bitmap.getHeight())
			rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
		else
			rect = new Rect(0, 0, bitmap.getHeight(), bitmap.getHeight());


		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		output.compress(Bitmap.CompressFormat.PNG, 100, bos);
		DataBaseHandler.db.setImage(bos.toByteArray());

		return output;
		}
		catch(Exception e){
			Log.i("Profile","Exception while rounding the image : " +e);
			return null;
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		if(data==null)
			if(requestCode==REQUEST_CAMERA)
				Toast.makeText(getActivity().getApplicationContext(), "Device camera app not supported. Pick from gallery", Toast.LENGTH_LONG).show();
		try{
			PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, data,
                    PendingIntent.FLAG_UPDATE_CURRENT);
			pendingIntent.cancel();
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == REQUEST_CAMERA) {

					//Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
					// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					//thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					//String path = Images.Media.insertImage(getActivity().getContentResolver(), thumbnail, "Title", null);
					//Uri picUri = Uri.parse(path);
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), outputFileUri);
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					  String path = Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "KID", null);
					
					  
					performCrop(Uri.parse(path));


				} else if (requestCode == SELECT_FILE) {
					Uri iru = data.getData();
					if(iru!=null)
					{
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), iru);
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
						  String path = Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "KID", null);
						performCrop(Uri.parse(path));
					}
					else
					{
						Bundle bun = data.getExtras();
						if(bun==null)
							Toast.makeText(getActivity(), "Cropping not supported on this device", Toast.LENGTH_LONG).show();
						else
						{
							Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
							ByteArrayOutputStream bytes = new ByteArrayOutputStream();
							thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
							  String path = Images.Media.insertImage(getActivity().getContentResolver(), thumbnail, "KID", null);
							performCrop(Uri.parse(path));
						}
							
					}
				} else if (requestCode == PIC_CROP) {
					Uri iru = data.getData();
					
					if(iru!=null)
					{
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), iru);
						profilePic.setImageBitmap(getRoundedShape(bitmap));
					}
					else
					{
						Bundle bun = data.getExtras();
						if(bun==null)
							Toast.makeText(getActivity(), "Cropping not supported on this device", Toast.LENGTH_LONG).show();
						else
						{
							Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
							ByteArrayOutputStream bytes = new ByteArrayOutputStream();
							thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
							  String path = Images.Media.insertImage(getActivity().getContentResolver(), thumbnail, "KID", null);
							performCrop(Uri.parse(path));
						}
							
					}
					
				}
				else if(requestCode == PICTURE_RESULT)
				{
					if (requestCode == PICTURE_RESULT)
						if (resultCode == Activity.RESULT_OK) {
							try {
								Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
										getActivity().getContentResolver(), imageUri);
								profilePic.setImageBitmap(getRoundedShape(thumbnail));
								//imageurl = getRealPathFromURI(imageUri);    
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
				}
			}
		}
		catch(Exception e){
			Log.i("Profile"," Exception in onActivityResult :"+ e);
		}
	}
	private void performCrop(Uri picUri) {

		try{
			//call the standard crop action intent (the user device may not support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
			//indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			//set crop properties
			cropIntent.putExtra("crop", "true");
			//indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			//indicate output X and Y
			cropIntent.putExtra("outputX", 150);
			cropIntent.putExtra("outputY", 150);
			//retrieve data on return
			//cropIntent.putExtra("return-data", true);
			File file = new File(Environment.getExternalStorageDirectory(), "testcrop.jpg");
			  outputFileUri = Uri.fromFile(file);
			  cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			//start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}catch(Exception e){
			Log.i("Profile","Exception while cropping :" + e);
		}
	}

}
