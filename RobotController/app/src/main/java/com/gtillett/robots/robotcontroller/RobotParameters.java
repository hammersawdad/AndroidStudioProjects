package com.gtillett.robots.robotcontroller;


import android.content.Context;
import android.content.SharedPreferences;

public class RobotParameters {
    public static int ClockwiseMaxSpeed = 180;
    public static int CounterMaxSpeed = 0;
    public static int StopSpeed = 90;
    public static int SteeringSensitivityOffset = 40;
    public static int SteeringCenterZoneOffset = 5;

    public static Servo ServoA = new Servo();
    public static Servo ServoB = new Servo();

    private static final String SHARED_PREFERENCES_IDENTIFIER = "com.gtillett.robots.robotcontroller.RobotParameters";


    public static void ReadParametersFromDisk(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_IDENTIFIER, Context.MODE_PRIVATE);

        RobotParameters.ClockwiseMaxSpeed = Integer.parseInt(sharedPreferences.getString("ClockwiseMaxSpeed", "180"));
        RobotParameters.CounterMaxSpeed = Integer.parseInt(sharedPreferences.getString("CounterMaxSpeed", "0"));
        RobotParameters.SteeringSensitivityOffset = Integer.parseInt(sharedPreferences.getString("SteeringSensitivityOffset", "40"));
        RobotParameters.SteeringCenterZoneOffset = Integer.parseInt(sharedPreferences.getString("SteeringCenterZoneOffset", "5"));
        RobotParameters.ServoA.Offset = Integer.parseInt(sharedPreferences.getString("ServoAOffset", "0"));
        RobotParameters.ServoB.Offset = Integer.parseInt(sharedPreferences.getString("ServoBOffset", "0"));

    }

    public static void WriteParametersToDisk(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.clear();
        edit.putString("ClockwiseMaxSpeed", (Integer.toString(RobotParameters.ClockwiseMaxSpeed)));
        edit.putString("CounterMaxSpeed", (Integer.toString(RobotParameters.CounterMaxSpeed)));
        edit.putString("SteeringSensitivityOffset", (Integer.toString(RobotParameters.SteeringSensitivityOffset)));
        edit.putString("SteeringCenterZoneOffset", (Integer.toString(RobotParameters.SteeringCenterZoneOffset)));
        edit.putString("ServoAOffset", (Integer.toString(RobotParameters.ServoA.Offset)));
        edit.putString("ServoBOffset", (Integer.toString(RobotParameters.ServoB.Offset)));
        edit.commit();
    }
}
