package com.example.sstestmap;

import java.util.ArrayList;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentMap extends Fragment{
    private static final String TAG = "FragmentMapDisplay";

	MapView mMapView = null;
	IMapController mController  = null;
	ResourceProxy mResourceProxy = null;
	
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "----------Fragment onAttach----------");
        

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "----------Fragment onCreate----------");
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "----------Fragment onCreateView----------");
		return inflater.inflate(R.layout.fragmentmap, container, false);
		
	}
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "----------Fragment onActivityCreated----------");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "----------Fragment onStart----------");
	       mMapView = (MapView) getView().findViewById(R.id.myOSMmapview);  
	       mController = mMapView.getController();  
	        //ResourceProxy init  
	        mResourceProxy = new DefaultResourceProxyImpl(getActivity());  
	        mMapView.setTileSource(TileSourceFactory.MAPNIK);  
	        mMapView.setBuiltInZoomControls(true);  
	        mMapView.setMultiTouchControls(true);  
	        mController.setZoom(15);
	        GeoPoint center = new GeoPoint(MainActivity.centerLat,MainActivity.centerLon);  
	        mController.setCenter(center);
	        
	        
	        //draw heat map

	        
	        ArrayList<OverlayItem> mItems = drawHeatMap(MainActivity.rawData);
	        
	        if (mItems != null)
	        {
	        	ItemizedIconOverlay<OverlayItem> currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(
					mItems,
					new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
						public boolean onItemSingleTapUp(final int index,
								final OverlayItem item) {
							return true;
						}

						public boolean onItemLongPress(final int index,
								final OverlayItem item) {
							return true;
						}
					}, new DefaultResourceProxyImpl(getActivity()));

	        	mMapView.getOverlays().add(currentLocationOverlay);
	        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "----------Fragment onResume----------");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "----------Fragment onPause----------");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "----------Fragment onStop----------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "----------Fragment onDestroyView----------");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "----------Fragment onDestroy----------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "----------Fragment onDetach----------");
    }
	ArrayList<OverlayItem> drawHeatMap(ArrayList<ThreeNumber> ret)
	{
		if (ret == null) return null;
		ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
		int theCellID = -1;
		if (!MainActivity.selectedCell.equals( "All" ))
			theCellID = Integer.parseInt(MainActivity.selectedCell);
		
		for (int i = 0; i < ret.size(); ++i) {
			
			if (theCellID>=0 && ret.get(i).cellid != theCellID) continue;
			
			Bitmap bitmap = Bitmap.createBitmap(64, 64, Config.ARGB_4444);
			Canvas canvas = new Canvas(bitmap);
			// Canvas canvas = new Canvas();
			Paint paint = new Paint();

			// paint.setColor(Color.RED);

			double k = (ret.get(i).ss + 113) / 64.0;
			// Log.e("k = ",Double.toString(k));
			paint.setARGB(255, (int) (k * 255), (int) (255 * (1 - k)), 0);

			paint.setAlpha(150);
			canvas.drawCircle(32, 32, 32, paint);

			GeoPoint now = new GeoPoint(((double) ret.get(i).lat) / 10000,
					((double) ret.get(i).lon) / 10000);

			OverlayItem overlayItem = new OverlayItem("Here",
					"SampleDescription", now);// Position
			overlayItem.setMarker(new BitmapDrawable(bitmap));

			mItems.add(overlayItem);
		}

        return mItems;
	}

}