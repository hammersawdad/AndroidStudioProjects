package com.gtillett.robots.robotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class SelectBluetoothDeviceActivity extends AppCompatActivity {

    // Constants


    private ListView myListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bluetooth_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the controls
        initializeControls();

        // Display the list of available devices
        displayExistingBoundDevices();
    }

    private void initializeControls(){
        Button findBtn = (Button)findViewById(R.id.search);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //find(v);
            }
        });

        myListView = (ListView)findViewById(R.id.listView1);

        // Set the ListView "onclick" event
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (myListView.getItemAtPosition(myItemInt));
                selectBluetoothDevice(selectedFromList);
            }
        });
    }

    private void displayExistingBoundDevices(){

        BluetoothAdapter adapter = BluetoothConnector.Adapter;

        // Initialize the Array Adapter, and BIND the ListView to it
        ArrayAdapter<String> btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(btArrayAdapter);

        // Find the existing Bound devices
        Set<BluetoothDevice> existingPairedDevices = adapter.getBondedDevices();

        // Populate the Array Adapter
        //  the ListView should populate automatically, since it's bound to the ArrayAdapter
        for(BluetoothDevice device : existingPairedDevices)
            btArrayAdapter.add(device.getName()+ "\n" + device.getAddress());
    }



    public void selectBluetoothDevice(String selectedItem) {

        // Parse the string.  It is: [Bluetooth Name]\n[Bluetooth ID]
        String[] stringParts = selectedItem.split("\n");

        if (stringParts.length == 2) {

            // The Bluetooth ID is:  stringParts[1]

            // Activate the Bluetooth Input/Output steams
            if (activateBluetoothStreams(stringParts[1])) {

                // The device was selected successfully.  Start the Robot Activity
                startControlRobotActivity();

            }
            else{
                Toast.makeText(getApplicationContext(), "Selected Bluetooth device was not enabled.",
                        Toast.LENGTH_LONG).show();
            }

         }
        else
        {
            Toast.makeText(getApplicationContext(), "There was a problem selecting a Device.",
                    Toast.LENGTH_LONG).show();
        }


    }

    private boolean activateBluetoothStreams(String selectedBluetoothID){
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();

        BluetoothDevice device = findSelectedDevice(blueAdapter, selectedBluetoothID);

        // Verify that a device was found
        if (device == null){
            Toast.makeText(getApplicationContext(), "Selected Bluetooth device was not found.",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // Activate the Input and Output streams
        boolean success = false;
        try{
            ParcelUuid[] uuids = device.getUuids();
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            socket.connect();

            // Store the output stream in a static variable
            BluetoothConnector.OutputStream = socket.getOutputStream();

            //inStream = socket.getInputStream();

            success = true;
        }
        catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Failure initializing steams.",
                    Toast.LENGTH_LONG).show();
        }

        return success;
    }

    private BluetoothDevice findSelectedDevice(BluetoothAdapter blueAdapter, String selectedBluetoothID)
    {

        if (blueAdapter == null) return null;
        if (!blueAdapter.isEnabled()) return null;

        BluetoothDevice deviceFound = null;
        Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            try {
                BluetoothDevice[] devices = bondedDevices.toArray(new BluetoothDevice[bondedDevices.size()]);

                for (BluetoothDevice device : devices) {
                    if (device.getAddress().equals(selectedBluetoothID)) {
                        deviceFound = device;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If the device wasn't found
        return deviceFound;
    }

    private void startControlRobotActivity(){
        // Create an instance of the BlueToothController activity,
        //  and pass it the value
        Intent intent = new Intent(this, ControlRobotActivity.class );
        startActivity(intent);
        finish();
    }


}
