package ch.unige.cui.asg.android.hoverinfo.middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.unige.cui.asg.android.hoverinfo.middleware.buffer.Buffer;
import ch.unige.cui.asg.android.hoverinfo.middleware.network.Network;
import ch.unige.cui.asg.android.hoverinfo.middleware.retrieval.SubscriptionsManager;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * This activity enables the user to check the current status of the
 * hovering information service and configure its parameters.
 * 
 * @author alfredo
 *
 */
public class HoverinfoActivity extends Activity {
	
	
	    private TableLayout tlMetrics;


		@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.main);
	        
	        tlMetrics = (TableLayout) findViewById(R.id.tlMetrics);
	        
	        populateStatus();
	        displayStatus();
	

	        // Check service status
	        
	        ToggleButton btServiceOnOff = (ToggleButton)findViewById(R.id.btServiceOnOff);
	        btServiceOnOff.setChecked(HoverinfoService.isRunning);
	        btServiceOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					startStopService(isChecked);
				}
	        });
	        
	        
	        //--------------------------
	        // wifi tests
	        //--------------------------
	        
	        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
	        WifiInfo wi = wm.getConnectionInfo();
	        Log.v("HoverInfoActivity", "BSSID:" + wi.getBSSID());
	        Log.v("HoverInfoActivity", "SSID:" + wi.getSSID());
	        Log.v("HoverInfoActivity", "IP:" + wi.getIpAddress());
	        Log.v("HoverInfoActivity", "MAC:" + wi.getMacAddress());
	        
	        

	    }

	    
		private void startStopService(boolean start) {
			if (start) {
				startService(new Intent("ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService"));
			}
			else {
				stopService(new Intent("ch.unige.cui.asg.android.hoverinfo.middleware.HoverinfoService"));
			}
		}
    	
	    
	    //==========================================
	    // MENU
	    //==========================================
	    
	    
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main, menu);
	        return true;
	    }
	    
	    
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        
	        	case R.id.SETTINGS:
	        		startActivity(new Intent(getBaseContext(), HoverinfoPreferencesActivity.class));
	        		break;
	        		
	        }
	            return false;
	    }
	    
	    
	    //------------------------------------------
	    // STATUS DISPLAYING
	    //------------------------------------------
	    // - service on/off state
	    // - current number of replicas
	    // - number of removed replicas
	    // - total number of replicas
	    // - number of sent/recv messages
	    // - number of subscriptions
	    // - number of notifications
	    // - number of retrieval queries
	    // - number of answers to queries
	    //------------------------------------------


	    
	    protected Map<String, Measurable> modules;
	    protected Map<String, ArrayList<TextView>> labels;
	    protected Map<String, ArrayList<TextView>> values;
	    
	    
	    protected void populateStatus() {

	    	modules = new HashMap<String, Measurable>();
	    	labels = new HashMap<String, ArrayList<TextView> >();
	    	values = new HashMap<String, ArrayList<TextView> >();
	    	
	    	registerModule("buffer", (Measurable) Buffer.getInstance());
	    	registerModule("network", (Measurable) Network.getInstance());
	    	
	    	populateStatusWithModuleMetrics("buffer");
	    	populateStatusWithModuleMetrics("network");
	    	
	    	
	    }
	    
	    protected void registerModule(String moduleName, Measurable module) {
	    	modules.put(moduleName, module);
	    }
	    
	    protected void populateStatusWithModuleMetrics(String moduleName) {
	    	
	    	Measurable module = modules.get(moduleName);
	    	
	    	ArrayList<TextView> l = new ArrayList<TextView>();
	    	ArrayList<TextView> v = new ArrayList<TextView>();

	    	String[] metrics = module.getMetricsList();
	    	for (int i = 0; i < metrics.length; i ++) {

	    		// label
	    		
	    		TextView tv = new TextView(this);
	    		tv.setText(metrics[i]);
	    		l.add(tv);
	    		
	    		// value

	    		TextView tv2 = new TextView(this);
	    		tv2.setText("nan");
	    		v.add(tv2);
	    		
	    		// insert view in the metrics layout
	    		
	    		TableRow row = new TableRow(this);
	    		row.addView(tv);
	    		row.addView(tv2);
	    		tlMetrics.addView(row);
	    	}
	    	
	    	labels.put(moduleName, l);
	    	values.put(moduleName, v);
	    	
	    }

	    protected void displayStatus() {
	    	
	    	displayStatusModule("buffer");
	    	displayStatusModule("network");
	    	
	    }
	    
	    protected void displayStatusModule(String moduleName) {
	    	
	    	Measurable module = modules.get(moduleName);
	    	
	    	ArrayList<TextView> v = values.get(moduleName);
	    	
	    	for (int i = 0; i < v.size(); i++) {
	    		
	    		// update displayed metric value
	    		
	    		double value = module.getMetricValue(i);
	    		TextView tv = v.get(i);
	    		tv.setText("" + value);
	    	}
	    	
	    }
	    
}
