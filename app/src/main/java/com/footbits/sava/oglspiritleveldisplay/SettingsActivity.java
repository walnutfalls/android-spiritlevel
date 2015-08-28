package com.footbits.sava.oglspiritleveldisplay;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsActivity extends Activity {

    public static final String PreferencesFileName = "Preferences";
    public static final String MinRangePrefKey = "min_range";
    public static final String MaxRangePrefKey = "max_range";

    private SharedPreferences preferences;

    @Bind(R.id.start_spirit_level) Button startSpiritLevelButton;
    @Bind(R.id.min_range) EditText minRange;
    @Bind(R.id.max_range) EditText maxRange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        preferences = getSharedPreferences(PreferencesFileName, MODE_PRIVATE);
        setInputsFromPrefs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume () {
        super.onResume();
        setInputsFromPrefs();
    }


    public void openSpiritLevel(View v) {
        SharedPreferences.Editor settingsEditor =
                getSharedPreferences(PreferencesFileName, MODE_PRIVATE).edit();

        settingsEditor.putFloat(MinRangePrefKey, Float.parseFloat(minRange.getText().toString()));
        settingsEditor.putFloat(MaxRangePrefKey, Float.parseFloat(maxRange.getText().toString()));

        settingsEditor.commit();

        Intent intent = new Intent(this, SpiritLevelActivity.class);
        startActivity(intent);
    }

    private void setInputsFromPrefs() {
        if(preferences.contains(MinRangePrefKey)) {
            float min = preferences.getFloat(MinRangePrefKey, 0);
            minRange.setText(Float.toString(min));
        }

        if(preferences.contains(MaxRangePrefKey)) {
            float max = preferences.getFloat(MaxRangePrefKey, 0);
            maxRange.setText(Float.toString(max));
        }
    }

}
