
package com.saratrak.sisschool;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.saratrak.sisschool.R;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{
	private static final int HELLO_ID = 1;
	private static final String TAG = "GCM service";
	public String lat,lon,event,msg;
	public GCMIntentService()
	{
		super( "648886547442");
        // TODO Auto-generated constructor stub
        Log.i( "GCM", "GCMIntentService constructor called" );
	}

	@Override
	protected void onError(Context arg0, String errorId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		// TODO Auto-generated method stub
		// this is when notif data is passed as headers
//		String data = intent.getStringExtra( "c2dm_data" );
//		Log.i(TAG, "new message= "+ intent.getStringExtra( "c2dm_data" ));
		 //this is when notif data is passed as part of URL
		String data = intent.getStringExtra( "message" );
		Log.i(TAG, "new message= "+ intent.getStringExtra( "cdata" ));
		//this is for resource bundle push data
//		String data = intent.getStringExtra( "data" );
//		Log.i(TAG, "new message= "+ intent.getStringExtra( "data" ));
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.app_icon;
		CharSequence tickerText = "SIS Tracko";
		long when = System.currentTimeMillis();

		//Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		CharSequence contentTitle = "SIS Tracko";
		CharSequence contentText = data;
		Intent notificationIntent = new Intent(this, com.saratrak.sisschool.LoginActivity.class);
		int mNotificationId = 001;
		 
		PendingIntent resultPendingIntent =
		        PendingIntent.getActivity(
		                context,
		                0,
		                notificationIntent,
		                PendingIntent.FLAG_CANCEL_CURRENT
		        );
		 
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		        context);
		Notification notification = mBuilder.setSmallIcon(icon).setTicker(contentTitle).setWhen(0)
		        .setAutoCancel(true)
		        .setContentTitle(contentTitle)
		        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
		        .setContentIntent(resultPendingIntent)
		        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
		        .setContentText(contentText).build();
		 
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(mNotificationId, notification);
		
		
		Log.i("GCM"," showing Notification" );
		
		if(contentText!=null)
			DataBaseHandler.alert = contentText.toString();
		
		try{
			if(DataBaseHandler.db!=null)
			{
				DataBaseHandler.db.setGCMText(contentText.toString());
				DataBaseHandler.db.setGCMTime(System.currentTimeMillis()+"");
			}
		}
		catch(Exception e){
			Log.i("MapActivity","Exception while accessing DB :" + e);
		}
	}

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		// TODO Auto-generated method stub
		 Log.i(TAG, "Device registered: regId = " + registrationId);
		// Helper.regId=registrationId;
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		 Log.i(TAG, "unregistered = " + arg1);
	}

}
