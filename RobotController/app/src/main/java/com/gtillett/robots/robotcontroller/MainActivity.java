package com.gtillett.robots.robotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startBlueToothController(View view){

        // Initialize Bluetooth on this device
        BluetoothAdapter adapter = initializeDeviceBlueTooth();
        if (validateBoothtoothAdapter(adapter))
        {
            // set the adapter to use by the rest of the application
            BluetoothConnector.Adapter = adapter;

            // Bluetooth is enabled, so start the activity that selects the Device Connection
            startSelectDeviceActivity();
        }
    }

    public BluetoothAdapter initializeDeviceBlueTooth(){
        // take an instance of BluetoothAdapter - Bluetooth radio
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if(adapter != null) {
            // If the BlueTooth Adapter is not enabled, then enable it
            if (!adapter.isEnabled()) {
                Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOnIntent, REQUEST_ENABLE_BT);
            }
        }

        // Return the BluetoothAdapter
        return adapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){

            // TODO: Do we need this override method?

        }
    }


    private boolean validateBoothtoothAdapter(BluetoothAdapter adapter){
        if (adapter == null ) {
            Toast.makeText(getApplicationContext(), "No Bluetooth Support found.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        if (!adapter.isEnabled() ) {
            Toast.makeText(getApplicationContext(),"Bluetooth was NOT enabled." ,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // Otherwise
        return true;
    }



    private void startSelectDeviceActivity(){
        // Create an instance of the BlueToothController activity,
        //  and pass it the value
        Intent intent = new Intent(this, SelectBluetoothDeviceActivity.class );
        startActivity(intent);
        finish();
    }

}
