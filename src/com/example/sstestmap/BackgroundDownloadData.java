package com.example.sstestmap;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Message;
import android.util.Log;

public class BackgroundDownloadData extends IntentService {
	public BackgroundDownloadData()
	{
		super("downloadDataInBackground");
	}
    public BackgroundDownloadData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        
        // Do work here, based on the contents of dataString
        getData();
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = "使用Message.Obtain+Hander.sendMessage()发送消息";
        FragmentQuery.handler.sendMessage(msg);
    }
	void getData()
	{
		String MODEL_NAME = Build.MODEL;
		MODEL_NAME = MODEL_NAME.replace(" ", "_");
		MODEL_NAME = MODEL_NAME.replace("-", "_");
		
        MongoHelper mongo = new MongoHelper( MODEL_NAME, "82.130.27.36");
        mongo.connect();
        mongo.checkCollection();
        GeoPoint center = new GeoPoint(MainActivity.centerLat,MainActivity.centerLon);
        
        int lat = (int) ( center.getLatitude() * 10000 ); 
        int lon = (int) ( center.getLongitude() * 10000 );
        
        Log.e("lat = ",Integer.toString(lat));
        Log.e("lon = ",Integer.toString(lon));
        
		ArrayList<ThreeNumber> ret = mongo.getGPS(lat, lon);
		Log.e("size of ret=", Integer.toString(ret.size()));
		
		MainActivity.rawData =  ret;
		
		ArrayList<String> tmp = new ArrayList<String>(); 
		tmp.add("All");
		for (int i = 0;i<ret.size();++i)
		{
			String ss = Integer.toString( ret.get(i).cellid );
			if (!tmp.contains(ss)) tmp.add(ss);
		}
		
		MainActivity.cellids = tmp;
	}
	
}