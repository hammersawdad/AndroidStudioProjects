package com.gtillett.robots.robotcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class ControlRobotActivity extends AppCompatActivity {

    //  CONSTANTS
    private static final int ROBOT_ACTION_STEER = 1;
    private static final int ROBOT_ACTION_LIGHT = 2;

    private static final int SEEKBAR_CENTER_VALUE = 50;


    // ENUMERATIONS
    private enum ServoDirection { Forward, Stop, Backward }

    // CLASS SCOPED VARIABLES
    private ServoDirection mServoDirection;
    private SeekBar seekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_robot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Initialize controls
            initializeControls();
     }


    private void initializeControls(){

        // Load any saved settings
        RobotParameters.ReadParametersFromDisk(this);

        // Set the default direction to STOP
        mServoDirection = ServoDirection.Stop;

        // Setup methods for the Seekbar
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                // If the SeekBar changed due to touching it, then steer the robot
                if (fromUser) {
                    steerRobot(progresValue);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void initializeButton_onClick(View view){
        Intent intent = new Intent(this, RobotParametersActivity.class );
        startActivity(intent);
    }

    private void steerRobot(int directionValue){
        // The directionValue should be between 0 and 100
        //   a value of 50 is straight

        // Get the offset from center (always a positive number)
        int offsetFromCenter = calculateOffsetFromCenter(directionValue);

        // If moving FORWARD then ...
        if (mServoDirection == ServoDirection.Forward) {
            // If turning LEFT
            if (directionValue <= SEEKBAR_CENTER_VALUE) {
                RobotParameters.ServoA.CurrentRotationPosition = RobotParameters.CounterMaxSpeed;
                RobotParameters.ServoB.CurrentRotationPosition = calculateSlowWheel(RobotParameters.ClockwiseMaxSpeed, offsetFromCenter);
            }
            // If turning RIGHT
            if (directionValue > SEEKBAR_CENTER_VALUE) {
                RobotParameters.ServoA.CurrentRotationPosition = calculateSlowWheel(RobotParameters.CounterMaxSpeed, offsetFromCenter);
                RobotParameters.ServoB.CurrentRotationPosition = RobotParameters.ClockwiseMaxSpeed;
            }
        }


        // If moving BACKWARD then ...
        if (mServoDirection == ServoDirection.Backward) {
            // If turning LEFT
            if (directionValue <= SEEKBAR_CENTER_VALUE) {
                RobotParameters.ServoA.CurrentRotationPosition = calculateSlowWheel(RobotParameters.ClockwiseMaxSpeed, offsetFromCenter);
                RobotParameters.ServoB.CurrentRotationPosition = RobotParameters.CounterMaxSpeed;
            }
            // If turning RIGHT
            if (directionValue > SEEKBAR_CENTER_VALUE) {
                RobotParameters.ServoA.CurrentRotationPosition = RobotParameters.ClockwiseMaxSpeed;
                RobotParameters.ServoB.CurrentRotationPosition = calculateSlowWheel(RobotParameters.CounterMaxSpeed, offsetFromCenter);
            }
        }


        // If STOPPING then ...
        if (mServoDirection == ServoDirection.Stop) {
            // If turning LEFT
            RobotParameters.ServoA.CurrentRotationPosition = RobotParameters.StopSpeed;
            RobotParameters.ServoB.CurrentRotationPosition = RobotParameters.StopSpeed;
        }

        // Send the Message to the Robot
        String message = RobotMessage.FormatSteerMessage(RobotParameters.ServoA, RobotParameters.ServoB);
        updateText(message);
        sendRobotMessage(ROBOT_ACTION_STEER, message);
    }

    private int calculateOffsetFromCenter(int directionValue){
        // Get the offset from center (always a positive number)
        int offsetFromCenter = Math.abs(SEEKBAR_CENTER_VALUE - directionValue);

        // Determine if the seekbar is very close to the center.
        //  If so, then treat is as the center, to help drive straight
        if (offsetFromCenter <= RobotParameters.SteeringCenterZoneOffset)
        {
            // the seekbar is right near the center, so treat is as being dead center
            offsetFromCenter = 0;
        }
        else
        {
            // the seekbar is outside of the "padded" zone, so ...
            // 1. subtract the Padding Offset
            // 2. add the Steering Sensivity offset
            offsetFromCenter = offsetFromCenter - RobotParameters.SteeringCenterZoneOffset + RobotParameters.SteeringSensitivityOffset;
        }

        return offsetFromCenter;
    }

    private int calculateSlowWheel(int fullSpeed, int offsetFromCenter)
    {
        // Default to Full Speed
        int rotationPosition = fullSpeed;

        // Don't allow the return value to go past the mid-point (the STOP value)
        if (fullSpeed == RobotParameters.ClockwiseMaxSpeed)
        {
            rotationPosition = fullSpeed - offsetFromCenter;
            if (rotationPosition < RobotParameters.StopSpeed)
            {
                rotationPosition = RobotParameters.StopSpeed;
            }
        }

        if (fullSpeed == RobotParameters.CounterMaxSpeed)
        {
            rotationPosition = fullSpeed + offsetFromCenter;
            if (rotationPosition > RobotParameters.StopSpeed)
            {
                rotationPosition = RobotParameters.StopSpeed;
            }
        }

        return rotationPosition;
    }

    private void updateText(String text){
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText(text);
    }

    public void sendBluetoothDataOn(View view) {
        sendRobotMessage(ROBOT_ACTION_LIGHT, "ON");
    }
    public void sendBluetoothDataOff(View view){
        sendRobotMessage(ROBOT_ACTION_LIGHT, "OFF");
    }

    public void moveForward(View view){
        // Recenter the steering bar
        recenterSteeringControl();

        // Move Forward
        mServoDirection = ServoDirection.Forward;
        steerRobot(SEEKBAR_CENTER_VALUE);
    }
    public void moveBackward(View view){
        // Recenter the steering bar
        recenterSteeringControl();

        // Move Forward
        mServoDirection = ServoDirection.Backward;
        steerRobot(SEEKBAR_CENTER_VALUE);
    }
    public void moveStop(View view){
        // Recenter the steering bar
        recenterSteeringControl();

        // Stop
        mServoDirection = ServoDirection.Stop;
        steerRobot(SEEKBAR_CENTER_VALUE);
    }


    private void recenterSteeringControl() {
        // Recenter the steering bar
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setProgress(SEEKBAR_CENTER_VALUE);
    }

    private void sendRobotMessage(int action, String message)
    {
        // Format the message
        String robotMessage = RobotMessage.FormatRobotMessage(action, message);

        // Send the Bluetooth data
        BluetoothConnector.Write(robotMessage);
    }

}
