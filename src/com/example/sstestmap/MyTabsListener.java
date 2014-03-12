package com.example.sstestmap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Build;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyTabsListener implements TabListener {

	public Fragment fragment;
	
	public MyTabsListener(Fragment fragment) {
		this.fragment = fragment;
		}
	
	@SuppressLint("NewApi")
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(MainActivity.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@SuppressLint("NewApi")
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}

}