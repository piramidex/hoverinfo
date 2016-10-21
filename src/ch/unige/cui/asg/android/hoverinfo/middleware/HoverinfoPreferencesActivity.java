package ch.unige.cui.asg.android.hoverinfo.middleware;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class HoverinfoPreferencesActivity extends PreferenceActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
