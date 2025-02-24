package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by conno on 10/26/2017.
 */

public class HardwareCryptoBoxLegacy {

    DigitalChannel cryptoBoxTouch1 = null ;
    DigitalChannel cryptoBoxTouch2 = null ;
    TouchSensor cryptoBoxEndTouch = null ;
    Servo cryptoBoxServo = null ;
    protected boolean cryptoBoxTouchValue1 = false ;
    protected boolean cryptoBoxTouchValue2 = false ;
    protected float cryptoBoxServoPositionUp = (float) 0.3 ;
    protected float cryptoBoxServoPositionDown = (float) 0.7 ;
    HardwareMap hwMap = null ;

    public HardwareCryptoBoxLegacy() {

    }

    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        cryptoBoxTouch1 = hwMap.digitalChannel.get("cryptoBoxTouch1") ;
        cryptoBoxTouch2 = hwMap.digitalChannel.get("cryptoBoxTouch2") ;
        cryptoBoxEndTouch = hwMap.touchSensor.get("touchSensorLeg");
        cryptoBoxServo = hwMap.servo.get("cryptoBoxServo") ;

        cryptoBoxTouch1.setMode(DigitalChannel.Mode.INPUT);
        cryptoBoxTouch2.setMode(DigitalChannel.Mode.INPUT);

        cryptoBoxServo.setPosition(cryptoBoxServoPositionUp);
    }

    public void start() {

    }

    public void raiseCryptoServo() {
        cryptoBoxServo.setPosition(cryptoBoxServoPositionUp);
    }
    public void lowerCryptoServo() {
        cryptoBoxServo.setPosition(cryptoBoxServoPositionDown);
    }

    public boolean updateCryptoTouch1() {
        return cryptoBoxTouch1.getState();
    }
    public boolean updateCryptoTouch2() {
        return cryptoBoxTouch2.getState();
    }
    public boolean isEndTouched() {
        return cryptoBoxEndTouch.isPressed();
    }

    public void addTelemetry(Telemetry telemetry) {
        telemetry.addLine()
                .addData("CryptoTouch1 ", new Func<String>() {
                    @Override
                    public String value() {
                        return Boolean.toString(cryptoBoxTouch1.getState());
                    }
                })
                .addData("CryptoTouch2 ", new Func<String>() {
                    @Override
                    public String value() {
                        return Boolean.toString(cryptoBoxTouch2.getState());
                    }
                })
                .addData("CryptoEndTouch ", new Func<String>() {
                    @Override
                    public String value() {
                        return Boolean.toString(isEndTouched());
                    }
                });
        telemetry.addLine()
                .addData("CryptoServo ", new Func<String>() {
                    @Override
                    public String value() {
                        return FormatHelper.formatDouble(cryptoBoxServo.getPosition());
                    }
                });
    }

}
