package com.gtillett.robots.robotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RobotParametersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_parameters);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        populateControls();
    }

     private void populateControls(){
        EditText editClockwise = (EditText)findViewById(R.id.editClockwise);
        EditText editCounter = (EditText)findViewById(R.id.editCounter);
        EditText editSensitivity = (EditText)findViewById(R.id.editSensitivity);
         EditText editCenterZone = (EditText)findViewById(R.id.editCenterZone);
        EditText editServoAOffset = (EditText)findViewById(R.id.editServoAOffset);
        EditText editServoBOffset = (EditText)findViewById(R.id.editServoBOffset);

        editClockwise.setText(Integer.toString(RobotParameters.ClockwiseMaxSpeed));
        editCounter.setText(Integer.toString(RobotParameters.CounterMaxSpeed));
        editSensitivity.setText(Integer.toString(RobotParameters.SteeringSensitivityOffset));
         editCenterZone.setText(Integer.toString(RobotParameters.SteeringCenterZoneOffset));

        editServoAOffset.setText(Integer.toString(RobotParameters.ServoA.Offset));
        editServoBOffset.setText(Integer.toString(RobotParameters.ServoB.Offset));
    }

    private void saveChanges(){
        EditText editClockwise = (EditText)findViewById(R.id.editClockwise);
        EditText editCounter = (EditText)findViewById(R.id.editCounter);
        EditText editSensitivity = (EditText)findViewById(R.id.editSensitivity);
        EditText editCenterZone = (EditText)findViewById(R.id.editCenterZone);
        EditText editServoAOffset = (EditText)findViewById(R.id.editServoAOffset);
        EditText editServoBOffset = (EditText)findViewById(R.id.editServoBOffset);

        RobotParameters.ClockwiseMaxSpeed = convertInt(editClockwise);
        RobotParameters.CounterMaxSpeed = convertInt(editCounter);
        RobotParameters.SteeringSensitivityOffset = convertInt(editSensitivity);
        RobotParameters.SteeringCenterZoneOffset = convertInt(editCenterZone);
        RobotParameters.ServoA.Offset = convertInt(editServoAOffset);
        RobotParameters.ServoB.Offset = convertInt(editServoBOffset);

        RobotParameters.WriteParametersToDisk(this);
    }

    public void buttonSaveOnClick(View view){
        saveChanges();
        finish();
    }
    public void buttonCancelOnClick(View view){
        finish();
    }

    private int convertInt(EditText editText){
        return Integer.parseInt(editText.getText().toString());
    }

}
