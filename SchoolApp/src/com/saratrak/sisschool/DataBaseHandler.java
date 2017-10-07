package com.saratrak.sisschool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHandler extends SQLiteOpenHelper {
	
	public static DataBaseHandler db;
	public static String alert="";

private static final int DATABASE_VERSION = 1;

private static final String DATABASE_NAME = "schoolapp";

private static final String TABLE_DETAILS = "details";

private static final String NAME = "name";
private static final String ID = "id";
private static final String USERID = "userid";
private static final String ADDRESS = "address";
private static final String REGID = "regid";
private static final String IMAGE = "image";
private static final String GCMTEXT = "gcmtext";
private static final String GCMTIME = "gcmtime";

public DataBaseHandler(Context context) {
super(context, DATABASE_NAME, null, DATABASE_VERSION);
}

@Override
public void onCreate(SQLiteDatabase db) {
String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DETAILS + "("+ID+" TEXT PRIMARY KEY,"+ NAME + " TEXT,"+ ADDRESS + " TEXT," + REGID + " TEXT, image BLOB, userid TEXT, gcmtext TEXT,gcmtime TEXT"+")";
db.execSQL(CREATE_CONTACTS_TABLE);
}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
onCreate(db);
}

public String getAddress()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(2);
	}
	return null;
}

public String getRegID()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(3);
	}
	return null;
}

public String getGCMText()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(6);
	}
	return null;
}

public String getGCMTime()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(7);
	}
	return null;
}

public String getName()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(1);
	}
	return null;
}

public String getID()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(0);
	}
	return null;
}

public String getUserid()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getString(5);
	}
	return null;
}

public void cleardb()
{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
		onCreate(db);
}

public void setNameIDRegID(String name, String id,String regid,String userid)
{
	SQLiteDatabase db = this.getWritableDatabase();
	db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILS);
	onCreate(db);
	ContentValues values = new ContentValues();
	values.put(NAME, name);
	values.put(ID, id);
	values.put(REGID,regid);
	values.put(USERID,userid);
	values.put(ADDRESS, "");
	db.insert(TABLE_DETAILS, null, values);
	db.close();
}

public void setAddress(String address)
{
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(ADDRESS, address);
	db.update(TABLE_DETAILS, values, ID + " = ?",new String[] { getID() });
	db.close();
}

public void setGCM(String id)
{
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(REGID, id);
	db.update(TABLE_DETAILS, values, ID + " = ?",new String[] { getID() });
	db.close();
}

public void setGCMText(String gcm)
{
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(GCMTEXT, gcm);
	db.update(TABLE_DETAILS, values, ID + " = ?",new String[] { getID() });
	db.close();
}

public void setGCMTime(String time)
{
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(GCMTIME, time);
	db.update(TABLE_DETAILS, values, ID + " = ?",new String[] { getID() });
	db.close();
}

public byte[] getImage()
{
	String selectQuery = "SELECT * FROM details";
	SQLiteDatabase db = this.getWritableDatabase();
	Cursor cursor = db.rawQuery(selectQuery, null);
	if (cursor.moveToFirst()) 
	{
		return cursor.getBlob(4);
	}
	return null; 
}

public void setImage(byte[] img)
{
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.put(IMAGE, img);
	db.update(TABLE_DETAILS, values, ID + " = ?",new String[] { getID() });
	db.close();
}

public void clearImage()
{
	SQLiteDatabase db = this.getWritableDatabase();
	ContentValues values = new ContentValues();
	values.putNull(IMAGE);
	db.update(TABLE_DETAILS, values, ID + " = ?",new String[] { getID() });
	db.close();
}


}

