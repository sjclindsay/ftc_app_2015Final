package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Func;

/**
 * Created by conno on 8/17/2017.
 */

@TeleOp(name="Mec: RevTeleOp2", group="Mec")


public class MecRevTeleOp2 extends OpMode {

    float motorLeft1power = 0;
    float motorLeft2power = 0;
    float motorRight1power = 0;
    float motorRight2power = 0;
    float motorLifterPower = 0 ;
    float servoGrabberPosition = 0 ;
    float leftStickY = 0 ;
    boolean controller1 = true;
    boolean controller2 = false ;
    float dPadScalar = 1 ;
    int dPadScalarPower = 0 ;
    HardwareRukusMecBot MecBot ;
    boolean waitForUpRelease = false ;
    boolean waitForDownRelease = false ;
    protected  float gamePad1LeftStickMagnitude = 0 ;
    protected  double maxPower = 1;
    float [] polarCoordinates = {0,0} ;
    float [] currentPolarCoordinates = {0,0} ;
    float stickAngle = 0 ;
    double targetHeading = 0.0 ;
    boolean triggerLeftToggle = false ;
    int dPadLeftOff = 0 ;
    boolean triggerRightToggle = false ;
    int  dPadRightOff = 0 ;
    boolean headingToggle = false ;
    boolean directionToggle  = false ;
    boolean yToggle = false ;
    boolean yDebounce = false;

    int armCase = 0 ;

    @Override
    public void init() {

        MecBot = new HardwareRukusMecBot(robotHWconnected.MotorLifterMarkerGrabberArm) ;
        MecBot.init(hardwareMap, Color.Red);
        waitForUpRelease = false ;
        waitForUpRelease = false ;
        dPadScalarPower = 0 ;
        motorLeft1power = 0;
        motorLeft2power = 0;
        motorRight1power = 0;
        motorRight2power = 0;
        motorLifterPower = 0 ;
        composeTelemetry();
    }

    @Override
    public void start() {

        MecBot.start();
    }

    @Override
    public void loop() {
        leftStickY = -gamepad1.left_stick_y ;

        dPadScalarPower = dPadScale(gamepad1.dpad_up,gamepad1.dpad_down,dPadScalarPower) ;
        dPadScalar = (float) Math.pow(2, dPadScalarPower) ;


        //currentPolarCoordinates = getCurrentPolarCoordinate(-gamepad2.left_stick_y, gamepad2.left_stick_x) ;
        //currentPolarCoordinates[0] = currentPolarCoordinates[0]/dPadScalar ;

        //targetHeading -= gamepad2.right_stick_x ;

        if (gamepad1.left_trigger >= 0.5 && !triggerLeftToggle) {
            triggerLeftToggle = true ;
            dPadLeftOff = (dPadLeftOff + 1)%2 ;
        } else if (gamepad1.left_trigger <= 0.5 && triggerLeftToggle) {
            triggerLeftToggle = false ;
        }
        if (gamepad1.right_trigger >= 0.5 && !triggerRightToggle) {
            triggerRightToggle = true ;
            dPadRightOff = (dPadRightOff + 1)%2 ;
        } else if (gamepad1.right_trigger <= 0.5 && triggerRightToggle) {
            triggerRightToggle = false ;
        }


            motorLeft1power = (leftStickY  + gamepad1.left_stick_x - gamepad1.right_stick_x)/dPadScalar ;
            motorLeft2power = (leftStickY  - gamepad1.left_stick_x - gamepad1.right_stick_x)/dPadScalar;
            motorRight1power = (leftStickY - gamepad1.left_stick_x + gamepad1.right_stick_x)/dPadScalar;
            motorRight2power = (leftStickY + gamepad1.left_stick_x + gamepad1.right_stick_x)/dPadScalar;

        if (gamepad1.y && !yDebounce) {
            yToggle = true ;
            yDebounce = true ;
        } else if (!gamepad1.y && yDebounce) {
            yToggle = false ;
            yDebounce = false ;
        }
        if (yToggle) {
            motorLeft1power = -(leftStickY + gamepad1.left_stick_x + gamepad1.right_stick_x)/dPadScalar ;
            motorLeft2power = -(leftStickY - gamepad1.left_stick_x + gamepad1.right_stick_x)/dPadScalar ;
            motorRight1power = -(leftStickY  - gamepad1.left_stick_x - gamepad1.right_stick_x)/dPadScalar ;
            motorRight2power = -(leftStickY  + gamepad1.left_stick_x - gamepad1.right_stick_x)/dPadScalar ;
        }

/*            if (dPadLeftOff == 1) {
                motorLeft1power = 0 ;
                motorLeft2power = 0 ;
            }
            if (dPadRightOff == 1) {
                motorRight1power = 0 ;
                motorRight2power = 0 ;
            } */
            if(MecBot.lifterConnected) {
                if ((gamepad2.left_bumper) && (!gamepad2.right_bumper)) {
                    motorLifterPower = (float) 1.0;
                } else if ((gamepad2.right_bumper) && (!gamepad2.left_bumper)) {
                    motorLifterPower = (float) -1.0;
                } else {
                    motorLifterPower = (float) 0.0;
                }
                if (!MecBot.lifter.lifterRangeLower.getState() && !MecBot.lifter.lifterRangeUpper.getState()) {
                    telemetry.addData("Error Error Holofect sensors both read ", MecBot.lifter.lifterRangeLower.getState()) ;
                } else {
                    if (!MecBot.lifter.lifterRangeUpper.getState()) {
                        motorLifterPower = Range.clip(motorLifterPower, -1, 0);
                    }
                }
            }
            if(MecBot.TeamMarkerConnected && gamepad2.y) {
                MecBot.setMarkerServoPosition(MecBot.getMarkerServoPosition()+0.2);

            }
            if(MecBot.TeamMarkerConnected && gamepad2.x) {
                MecBot.setMarkerServoPosition(MecBot.getMarkerServoPosition()-0.2);
            }
            if(MecBot.grabberConnected ) {
                MecBot.grabber.setServoGrabber1Position(gamepad2.right_trigger); //Grabber up and Down
                MecBot.grabber.setServoGrabber2Position(gamepad2.left_trigger);  //Grabber open and Shut

            }


            if (MecBot.armConnected) {
                MecBot.arm.bumperServoExtender(gamepad1.right_bumper, gamepad1.left_bumper);
                MecBot.arm.triggerArmControl(gamepad1.right_trigger, gamepad1.left_trigger);
                /*
                if (gamepad2.a) {
                    armCase = 1 ;
                } else if (gamepad2.b) {
                    armCase = 2 ;
                } else {
                 armCase = 0 ;
                }
                switch(armCase) {
                    case 0:
                        RobotLog.i("Arm is connected") ;
                        if (gamepad2.dpad_left) {
                            MecBot.arm.stowArm();
                        } else if (gamepad2.dpad_up) {
                            MecBot.arm.raiseArm();
                        } else if (gamepad2.dpad_right) {
                            MecBot.arm.lowerArm();
                        }
                        MecBot.arm.manualArmControl(-gamepad2.left_stick_y, (float)0.3);
                    break;
                    case 1:
                        //MecBot.arm.calibrateArmUp();
                    break;
                    case 2:
                        //MecBot.arm.calibrateArmDown();
                    break;
                }

                MecBot.arm.setServoExtender(gamepad1.right_trigger, gamepad1.left_trigger);
                */
            }

            MecBot.setBotMovement(motorLeft1power, motorLeft2power, motorRight1power, motorRight2power);
            if(MecBot.lifterConnected) {
                MecBot.setLifter(motorLifterPower);
            }

        MecBot.waitForTick(40);
        telemetry.update();

    }

    @Override
    public void stop() {

    }
    void composeTelemetry() {
        telemetry.addLine()
                .addData("Motor Speed " , new Func<String>() {
                    @Override
                    public String value() {
                        return String.valueOf(100/dPadScalar);
                    }
                });
        MecBot.addTelemetry(telemetry);
    }


    public int dPadScale (boolean dPadUpValue, boolean dPadDownValue, int dPadScale) {
        if (dPadUpValue && !waitForUpRelease) {
            waitForUpRelease = true ;
            dPadScale -- ;
        } else if (!dPadUpValue && waitForUpRelease) {
            waitForUpRelease = false ;
        } else if (dPadDownValue && !waitForDownRelease) {
            waitForDownRelease = true ;
            dPadScale ++ ;
        } else if (!dPadDownValue && waitForDownRelease) {
            waitForDownRelease = false ;
        }
        dPadScale = Range.clip(dPadScale,0, 3) ;
        return dPadScale ;
    }

    public double normalizeAngle(double angle) {
        if (angle > 180) {
            angle -= 360 ;
        }
        if (angle < -180) {
            angle += 360 ;
        }
        return angle ;
    }

    public float [] getCurrentPolarCoordinate (float padStickLeftY, float padStickLeftX) {
        if (padStickLeftX == 0 ) {
            if (padStickLeftY >= 0) {
                stickAngle = 90;
            } else  {
                stickAngle = -90 ;
            }
        } else {
            stickAngle = (float) ( Math.atan( padStickLeftY/padStickLeftX)*(180/Math.PI) );
        }

        if (padStickLeftY < 0 && padStickLeftX > 0) {
            stickAngle += 360 ;
        } else if (padStickLeftY < 0 && padStickLeftX < 0) {
            stickAngle += 180 ;
        } else if (padStickLeftY > 0 && padStickLeftX < 0) {
            stickAngle += 180 ;
        }

        gamePad1LeftStickMagnitude = (float) Math.pow((padStickLeftX*padStickLeftX + padStickLeftY*padStickLeftY), 0.5) ;
        polarCoordinates[0] = gamePad1LeftStickMagnitude ;
        polarCoordinates[1] = stickAngle ;

        return polarCoordinates ;
    }

    public double maxPowerIdentifier (double motorPower00, double motorPower01, double motorPower10, double motorPower11) {
        double power1 = Math.max(motorPower00,motorPower01) ;
        double power2 = Math.max(motorPower10,motorPower11) ;
        return Math.max(power1,power2) ;
    }

}

/*
if (gamepad2.dpad_up) {
        polarCoordinates[0] = (float) 0.2 ;
        } else {
        polarCoordinates[0] = 0 ;
        }

        if (gamepad2.left_bumper && !headingToggle) {
        headingToggle = true ;
        OmniBot.resetFirstPIDDrive();
        targetHeading = targetHeading + 90;
        RobotLog.i("target heading is " + targetHeading);
        RobotLog.i("button pressed") ;
        } else if (!gamepad2.left_bumper && headingToggle) {
        headingToggle = false ;
        RobotLog.i("button released ") ;
        }

        if (gamepad2.right_bumper && !headingToggle) {
        headingToggle = true;
        OmniBot.resetFirstPIDDrive();
        targetHeading = targetHeading - 90;
        RobotLog.i("target heading is " + targetHeading);
        }else if (!gamepad2.right_bumper && headingToggle) {
        headingToggle = false ;
        }

        if (gamepad2.dpad_left && !directionToggle) {
        directionToggle = true ;
        OmniBot.resetFirstPIDDrive();
        polarCoordinates[1] = polarCoordinates[1] + 45;
        RobotLog.i("direction is " + polarCoordinates[1]);
        } else if (!gamepad2.dpad_left && directionToggle) {
        directionToggle = false ;
        }

        if (gamepad2.dpad_right && !directionToggle) {
        directionToggle = true ;
        OmniBot.resetFirstPIDDrive();
        polarCoordinates[1] = polarCoordinates[1] - 45 ;
        RobotLog.i("direction is " + polarCoordinates[1]);
        } else if (!gamepad2.dpad_right && directionToggle) {
        directionToggle = false ;
        }

        targetHeading = normalizeAngle(targetHeading) ;
        polarCoordinates[1] = (float) normalizeAngle((double) polarCoordinates[1]);

        RobotLog.i("target heading is " + targetHeading);

        OmniBot.driveOmniBot(polarCoordinates[0], polarCoordinates[1],  (float) targetHeading);
*/