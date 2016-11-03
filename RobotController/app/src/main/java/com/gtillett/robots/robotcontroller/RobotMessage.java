package com.gtillett.robots.robotcontroller;

/**
 * Created by gtillett on 2/19/2016.
 */
public class RobotMessage {

    private static final String ROBOT_MESSAGE_START = "*";
    private static final String ROBOT_MESSAGE_END = "#";
    private static final String ROBOT_MESSAGE_DELIMITER = "|";

    public static String FormatSteerMessage(Servo servoA, Servo servoB){
        String delimiter = " ";
        String steerMessageText = "".
                concat(Integer.toString(servoA.CurrentRotationPosition + servoA.Offset))
                .concat(delimiter).
                        concat(Integer.toString(servoB.CurrentRotationPosition + servoB.Offset));
        return steerMessageText;
    }

    public static String FormatRobotMessage(int action, String message){
        return ROBOT_MESSAGE_START.
                concat(Integer.toString(action)).
                concat(ROBOT_MESSAGE_DELIMITER).
                concat(message).
                concat(ROBOT_MESSAGE_END);
    }
}
