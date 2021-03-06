package com.saratrak.sisschool.admin;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import com.saratrak.sisschool.admin.R;

import android.app.Application;
 
@ReportsCrashes(
mode = ReportingInteractionMode.DIALOG,
resDialogText = R.string.crash_dialog_text,
resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
mailTo = "ma.manoj@gmail.com,manjunath.gudisi@gmail.com,buddhapuneeth@gmail.com,mamanoj@gmail.com"
)
public class MyApplication extends Application {
 
 @Override
 public void onCreate() {
  super.onCreate();
   
  ACRA.init(this);
   
  // instantiate the report sender with the email credentials.
  // these will be used to send the crash report
   
  // register it with ACRA.
  //ACRA.getErrorReporter().setReportSender(reportSender);
   
 }
}