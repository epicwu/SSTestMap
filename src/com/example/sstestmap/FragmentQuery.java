package com.example.sstestmap;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentQuery extends Fragment {

	EditText editTextLat = null;
	EditText editTextLon = null;
	Spinner spinnerCellID = null;  
    private ArrayAdapter<String> adapter = null;  
    private static TextView tvStats = null;
    
	public static Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1)
			{
				//Refresh
				if (tvStats != null)
				{
					tvStats.setText(Analyse(MainActivity.rawData));
				}
			}
		};
	};
	
	static String Analyse( ArrayList<ThreeNumber> data )
	{
		if (data == null) return "no data";
		String ret = "Avg: ";
		int n = data.size();
		double[] sigs = new double[n];
		double sum = 0,avg;
		for (int i = 0; i < n; ++i)
		{
			sigs[i] =  data.get(i).ss;
			sum += sigs[i];
		}
		avg = sum / n;
		ret += Double.toString(avg) + "\n";
		Arrays.sort(sigs);
		int cnt = 1;
		for (int i = 0 ; i < n; ++i)
			if (i >= (cnt * n / 5))
			{
				ret += Integer.toString(cnt) + "/5 : " + sigs[i] + "\n";
				cnt++;
			}
		return ret; 
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragmentquery, container, false);
	}

	@Override
	public void onStart() {
	    super.onStart();
        editTextLat = (EditText) getView().findViewById(R.id.editTextLat);
        editTextLon = (EditText) getView().findViewById(R.id.editTextLon);
	    editTextLat.setText(Double.toString(MainActivity.centerLat));
	    editTextLon.setText(Double.toString(MainActivity.centerLon));
	    tvStats = (TextView) getView().findViewById(R.id.statsInfo);
	    
        
        
	    editTextLat.setOnFocusChangeListener(new OnFocusChangeListener() {          
	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus) {
	            	if (Math.abs(MainActivity.centerLat - Double.parseDouble(editTextLat.getText().toString())) > 0.00001)
	            	{
	            		MainActivity.centerLat = Double.parseDouble(editTextLat.getText().toString());
	            		getActivity().startService(MainActivity.downloadData);
					}	                
	            }
	        }
	    });
	    editTextLon.setOnFocusChangeListener(new OnFocusChangeListener() {          
	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus) {
	            	if (Math.abs(MainActivity.centerLon - Double.parseDouble(editTextLon.getText().toString())) > 0.00001)
	            	{
	            		MainActivity.centerLon = Double.parseDouble(editTextLon.getText().toString());
	            		getActivity().startService(MainActivity.downloadData);
	            	}
	            }
	        }
	    });
	    
	    spinnerCellID = (Spinner) getView().findViewById(R.id.spinner1);
        //将可选内容与ArrayAdapter连接
	    if (MainActivity.cellids == null)
	    {
	    	ArrayList<String> tmpss = new ArrayList<String>();
	    	tmpss.add("All");
	    	adapter=new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, tmpss);
	    } else
        adapter=new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, MainActivity.cellids);  
        //设置下拉列表风格  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        //将adapter添加到spinner中  
        spinnerCellID.setAdapter(adapter);  
        spinnerCellID.setSelection(MainActivity.selectedNum);
        //添加Spinner事件监听  
        spinnerCellID.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()  
        {  
        	@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

        		MainActivity.selectedCell = spinnerCellID.getSelectedItem().toString();
        		MainActivity.selectedNum = arg2;
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				MainActivity.selectedCell = "All";
				MainActivity.selectedNum = 0;
			}  
        });  
        
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = "Refresh";
        handler.sendMessage(msg);
	    
	}
	
    @SuppressLint("NewApi")
	@Override
    public void onPause() {
        super.onPause();
    }
}