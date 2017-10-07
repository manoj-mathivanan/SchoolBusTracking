package com.saratrak.sisschool;

import android.app.Activity;
import android.graphics.Typeface;

public class Fonts {
	
	public static Typeface black; 
	public static Typeface blackob; 
	public static Typeface book; 
	public static Typeface bookob; 
	public static Typeface heavy; 
	public static Typeface heavyob; 
	public static Typeface light; 
	public static Typeface lightob; 
	public static Typeface medium; 
	public static Typeface mediumob; 
	public static Typeface ob; 
	public static Typeface roman; 
	
	public static void init(Activity ac)
	{
	black = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-BLACK.OTF");
	blackob = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-BLACKOBLIQUE.OTF");
	book = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-BOOK.OTF");
	bookob = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-BOOKOBLIQUE.OTF");
	heavy = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-HEAVY.OTF");
	heavyob = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-HEAVYOBLIQUE.OTF");
	light = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-LIGHT.OTF");
	lightob = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-LIGHTOBLIQUE.OTF");
	medium = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-MEDIUM.OTF");
	mediumob = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-MEDIUMOBLIQUE.OTF");
	ob = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-OBLIQUE.OTF");
	roman = Typeface.createFromAsset(ac.getAssets(),"fonts/AVENIRLTSTD-ROMAN.OTF");
	
	
	}


}
