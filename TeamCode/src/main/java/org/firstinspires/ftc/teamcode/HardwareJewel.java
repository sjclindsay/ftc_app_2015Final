package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by matth on 9/16/2017.
 */

enum connectedColor {
    RED,
    BLUE
}

public class HardwareJewel {

    private Servo servoJewel = null;
    private Servo unimportantServoJewel = null ;
    private HardwareColorSensor jewelSensor;
    private connectedColor colorSide = null ;


    public static float servoJewelMaxPosition = (float) 1.0;
    public static float servoJewelInitalPosition = (float) 0.0;
    public static float servoJewelDownPosition = (float) 0.8;


    HardwareMap hwMap = null;

    /* Constructor */
    public HardwareJewel() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, Color targColor) {
        String servoJewelName = "";
        String servoNullName =  "" ;
        // Save reference to Hardware map
        hwMap = ahwMap;
        if(targColor == Color.Red) {
            servoJewelName = "servoJewelRed";
            servoNullName = "servoJewelBlue";
            colorSide = connectedColor.RED ;
        } else if(targColor == Color.Blue) {
            servoJewelName = "servoJewelBlue";
            servoNullName = "servoJewelRed" ;
            colorSide = connectedColor.BLUE ;
        } else {
            RobotLog.e("No Target Color Passed");
        }
        servoJewel = hwMap.servo.get(servoJewelName) ;
        unimportantServoJewel = hwMap.servo.get(servoNullName) ;


        if (targColor == Color.Red) {
            servoJewel.setPosition(servoJewelInitalPosition);
            unimportantServoJewel.setPosition(servoJewelDownPosition);
        } else if (targColor == Color.Blue) {
            servoJewel.setPosition(servoJewelDownPosition);
            unimportantServoJewel.setPosition(servoJewelInitalPosition);
        }


        jewelSensor = new HardwareColorSensor();
        jewelSensor.init(hwMap, targColor);

    }

    public void start() {
        led_low();
    }

    //put functions here

    public void raiseServoJewel() {
        if (colorSide == connectedColor.RED) {
            servoJewel.setPosition(servoJewelInitalPosition);
        } else if (colorSide == connectedColor.BLUE ){
            servoJewel.setPosition(0.7);
        }
    }

    public void lowerServoJewel() {
        if (colorSide == connectedColor.RED) {
            servoJewel.setPosition(servoJewelDownPosition);
        } else if ( colorSide == connectedColor.BLUE){
            servoJewel.setPosition(0);
        }
    }

    public void setServoJewelPosition(double position) {

        position = position * 0.3 + 0.5;

        position = Range.clip(position, 0.5, servoJewelMaxPosition);

        servoJewel.setPosition(position);
    }
    public void reverseServo () {
        servoJewel.setDirection(Servo.Direction.REVERSE);
    }

    public Color WhatColor (){
        return jewelSensor.WhatColor();
    }

    public void led_off() {
        jewelSensor.sensorLED.setPulseWidthOutputTime(1);
    }

    public void led_low() { jewelSensor.sensorLED.setPulseWidthOutputTime(300);}

    public void led_high() { jewelSensor.sensorLED.setPulseWidthOutputTime(500);}


    public void led_on() {
        jewelSensor.sensorLED.setPulseWidthOutputTime(100);
    }


    public void addTelemetry(Telemetry telemetry) {
        telemetry.addLine()
                .addData("ServoJewel ", new Func<String>() {
                    @Override
                    public String value() {
                        return FormatHelper.formatDouble(servoJewel.getPosition());
                    }
                });
        telemetry.addLine()
                .addData("Color ", new Func<String>() {
                    @Override
                    public String value() {
                        return FormatHelper.formatColor(jewelSensor.WhatColor());
                    }
                })
                .addData("Red ", new Func<String>() {
                    @Override
                    public String value() {
                        return FormatHelper.formatDouble(jewelSensor.colorSensor.red());
                    }
                })
                .addData("Blue ", new Func<String>() {
                    @Override
                    public String value() {
                        return FormatHelper.formatDouble(jewelSensor.colorSensor.blue());
                    }
                })
                .addData("Green ", new Func<String>() {
                    @Override
                    public String value() {
                        return FormatHelper.formatDouble(jewelSensor.colorSensor.green());
                    }
                });
    }
}