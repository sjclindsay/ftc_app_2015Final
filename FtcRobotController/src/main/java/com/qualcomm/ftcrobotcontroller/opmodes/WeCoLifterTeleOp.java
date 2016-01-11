package com.qualcomm.ftcrobotcontroller.opmodes;

import android.widget.DigitalClock;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
/**
 * Created by akhil on 12/22/2015.
 */
public class WeCoLifterTeleOp extends OpMode {


    @Override
    public void init() {

    }
    DcMotor motorLifter;
    DigitalChannel lifterHolofectSensor;

    Servo servoLifter;
    Servo servoHook;

    public enum POSITION {TOP, MIDDLE, BOTTOM}
    public POSITION currentPosition = POSITION.BOTTOM;
    double stickDirection = 0;
    double maxServoIncrease = 0.01 ;
    double servoTimer = 0;
    double servoLifterZero = 0.5 ;
    double servoHookPosition = 1;
    double servoLifterPosition = 0.5 ;
    double servoPrevious = 0;


    float motorPowerMin = -1 ;
    float motorPowerMax = 1  ;
    final static double servoMinRange = 0.0 ;
    final static double servoMaxRange = 1.0 ;

    @Override
    public void start(){


        motorLifter = hardwareMap.dcMotor.get("motorLifter");
        motorLifter = hardwareMap.dcMotor.get("motor_3");
    	lifterHolofectSensor =  hardwareMap.digitalChannel.get("halleffect_1");
        servoLifter = hardwareMap.servo.get("servo_2");
        servoHook = hardwareMap.servo.get("servo_3");
        servoPrevious = servoLifter.getPosition() ;
    }

    @Override
    public void loop() {
        //sets motor power
        float motorLifterpower = -gamepad2.left_stick_y  ;
        if(lifterHolofectSensor.getState() == false) {
            DbgLog.msg("==== Hall Effect On ===");
        }
        if (lifterHolofectSensor.getState() == false) {
            if (stickDirection == 1 || currentPosition == POSITION.TOP) {
            currentPosition = POSITION.TOP ;
            if (stickDirection == 0) {
                motorLifterpower = -gamepad2.left_stick_y ;
            } else {
                motorLifterpower = 0;
            }
            }
            if (stickDirection == 0 || currentPosition == POSITION.BOTTOM) {
            currentPosition = POSITION.BOTTOM ;
            if (stickDirection == 1) {
                motorLifterpower = -gamepad2.left_stick_y ;
            } else {
                motorLifterpower = 0;
            }
            }
        }
        if (lifterHolofectSensor.getState() == true) {
            currentPosition = POSITION.MIDDLE;
        }

        servoLifterPosition = servoLifterPosition + -gamepad2.right_stick_y/500;
        if (gamepad2.right_stick_button == true) {
            servoLifterPosition = servoLifterZero;
            servoTimer = 1 ;
        }
        if ( servoTimer == 1) {
            servoLifterPosition = servoLifterZero ;
            if(servoLifter.getPosition() == servoLifterZero) {
                servoTimer =0 ;
            }
        }

        if (gamepad2.right_trigger == 1 ) {
            DbgLog.msg("===== Set Hook to 1 =====");
            servoHookPosition = 1.0 ;
        } else if (gamepad2.left_trigger  == 1) {
            DbgLog.msg("===== Set Hook to 0 =====");
            servoHookPosition = 0.0 ;
        }


        //clips motor and servo power/position
        motorLifterpower = Range.clip(motorLifterpower, motorPowerMin, motorPowerMax) ;
        servoLifterPosition = Range.clip(servoLifterPosition, servoMinRange, servoMaxRange) ;
        servoHookPosition = Range.clip(servoHookPosition, servoMinRange, servoMaxRange) ;

        //if (Math.abs(servoLifterPosition-servoPrevious) >= maxServoIncrease ) {
        //    servoLifterPosition = servoPrevious + maxServoIncrease ;
        //}

        //sets motor and servo power/position
        motorLifter.setPower(motorLifterpower) ;
        servoLifter.setPosition(servoLifterPosition);
        servoHook.setPosition(servoHookPosition);

        servoPrevious = servoLifter.getPosition() ;
        if (-gamepad2.left_stick_y > 0) {
            stickDirection = 1;
        } else if (-gamepad2.left_stick_y < 0) {
            stickDirection = 0;
        }

        telemetry.addData("0 Motor Lifter", "Motor Lifter power is " + String.format("%.2f", motorLifterpower)); //motor Lifter power
        telemetry.addData("Servo Lifter Position", "Servo Lifter is at " + String.format("%f", servoLifterPosition)) ; //servo lifter position
        telemetry.addData("Servo Hook Position", "Servo Hook is at " + String.format("%f", servoHookPosition)) ; //servo hook position
    }

    @Override
    public void stop(){

    }
}
