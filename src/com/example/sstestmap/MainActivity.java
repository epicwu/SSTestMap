package com.example.sstestmap;


import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
public class MainActivity extends Activity {
	
	public static Context appContext;
	public static double centerLat = 60.1879383;
	public static double centerLon = 24.8318201;
	
	public static ArrayList<String> cellids = null;
	public static ArrayList<ThreeNumber> rawData = null;
	public static String selectedCell = "All";
	
	public static int selectedNum = 0;
	
	
	public static Intent downloadData = null;
	
	
	
	//Open GPS
	
	private LocationManager locManager = null;   
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appContext = this;
	    //ActionBar gets initiated
        ActionBar actionbar = getActionBar();
        //Tell the ActionBar we want to use Tabs.
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //initiating both tabs and set text to it.
        ActionBar.Tab searchTab = actionbar.newTab().setText("Query");
        ActionBar.Tab mapTab = actionbar.newTab().setText("Map");
        //create the two fragments we want to use for display content
        Fragment searchFragment = new FragmentQuery();
        Fragment mapFragment = new FragmentMap();
        //set the Tab listener. Now we can listen for clicks.
        searchTab.setTabListener(new MyTabsListener(searchFragment));
        mapTab.setTabListener(new MyTabsListener(mapFragment));
        //add the two tabs to the actionbar
        actionbar.addTab(searchTab);
        actionbar.addTab(mapTab);
        
        //Update Position
        setGPSLocation();
        
        
        //
        downloadData =  new Intent(this.getApplicationContext(),BackgroundDownloadData.class); 
        
        /*/允许在主进程里使用网络
        if (android.os.Build.VERSION.SDK_INT > 9) {
        	StrictMode.ThreadPolicy policy = 
        	        new StrictMode.ThreadPolicy.Builder().permitAll().build();
        	StrictMode.setThreadPolicy(policy);
        	}*/
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onStart() {
	    super.onStart();
        //data = drawHeatMap();
	    //********运行BackgroundDownloadData
	    startService(downloadData);
	}
	void setGPSLocation()
	{
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
                1000, 1, new LocationListener() {  
   
                     @Override  
                     public void onLocationChanged(Location location) {  
                         // 当GPS定位信息发生改变时，更新位置  
                         updateView(location);  
                     }  
   
                     @Override  
                     public void onProviderDisabled(String provider) {  
                         updateView(null);  
                     }  
   
                     @Override  
                     public void onProviderEnabled(String provider) {  
                         // 当GPS LocationProvider可用时，更新位置  
                         updateView(locManager  
                                 .getLastKnownLocation(provider));  
   
                     }  
   
                     @Override  
                     public void onStatusChanged(String provider, int status,  
                             Bundle extras) {  
                     }  
                 });  	
	}
	void updateView(Location loc)
	{
		if (loc == null)
		{

		} else
		{
			if (Math.abs(loc.getLatitude() - centerLat) + Math.abs(loc.getLongitude()-centerLon) > 0.0010) 
			{
				centerLat = loc.getLatitude();
				centerLon = loc.getLongitude();
				startService(downloadData);
			}
		}
	}


}