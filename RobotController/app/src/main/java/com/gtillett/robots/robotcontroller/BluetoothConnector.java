package com.gtillett.robots.robotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import java.io.IOException;
import java.io.OutputStream;

public class BluetoothConnector {

    private static final int BLUETOOTH_THROTTLE_MILLISECONDS = 100;

    private static long lastBlueToothTransmit = 0;

    public final static String BLUETOOTH_ID = "com.gtillett.robots.robotcontroller.BLUETOOTH_ID";

    public static BluetoothAdapter Adapter;
    public static OutputStream OutputStream;


    public static void Write(String message)  {
        // Ensure that we don't send messages too quickly
        long currentTimeMillis = System.currentTimeMillis();
        if (lastBlueToothTransmit  + BLUETOOTH_THROTTLE_MILLISECONDS > currentTimeMillis) {
            return;
        }

        lastBlueToothTransmit = currentTimeMillis;

        try {
            OutputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
