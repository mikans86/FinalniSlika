package com.example.new_home.termin30_test.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.new_home.termin30_test.R;

/**
 * Created by New_home on 12.3.2017.
 */

public class SettingActivity extends AppCompatActivity {

    // onCreate method is a lifecycle method called when he activity is starting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Replaces activity's content with a an instance of PreferenceFragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();

    }

    // onOptionsItemSelected method is called whenever an item in your options menu is selected.
    // It is used to handle menu item clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // PreferenceFragment is used to automatically load preference GUI from an XML resource and
    // save preferences into preferences.xml
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Loads preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}
