package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * Created by conno on 8/17/2017.
 */

@TeleOp(name="Omni: Auto Main", group="Omni")


public class OmniAutoMain extends OpMode {
    public enum MotorState{
        INITIALIZE,
        PUSHBALLS,
        INITIALIZEDRIVEOFFPLATFORM,
        DRIVEOFFPLATFORM,
        SQUAREVUFORIARY,
        SQUAREVUFORIATY,
        SQUAREVUFORIATZ,
        INITIALIZEDRIVETOCRYPTO,
        DRIVETOCRYPTO,
        COUNTBARS,
        COUNTSTAGE,
        ALIGNTOBARS,
        PUSHINCUBE,
        WAIT
    }
    MotorState currentState = MotorState.INITIALIZE;
    float magnitude = 0 ;
    float direction = 0 ;
    float targetHeading = 0 ;
    int barCounter = 0 ;
    public RelicRecoveryVuMark vuMark = null;

    MotorState nextState = null ;
    MotorState stateAfterNext = null ;
    robotHWconnected autoConnectedHW = robotHWconnected.MotorGyroLifterVufor;
    HardwareOmniBot OmniBot ;
    ElapsedTime StabilizationTimer = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

    @Override
    public void init() {
        OmniBot = new HardwareOmniBot(autoConnectedHW) ;
        OmniBot.init(hardwareMap);

        int count = 0;

        composeTelemetry();
    }

    @Override
    public void start() {

        OmniBot.start();

        telemetry.update();

    }

    @Override
    public void loop() {
        OmniBot.VuReader.updateVuforiaCoords();

        //OmniBot.driveOmniBot(0, 0, 0, PIDAxis.ry);


        switch (currentState) {
            case INITIALIZE:
                if (StabilizationTimer.time() >= 500) {
                    nextState = MotorState.INITIALIZEDRIVEOFFPLATFORM ;
                }
                break;
            case INITIALIZEDRIVEOFFPLATFORM:
                //red side (i think)
                OmniBot.driveOmniBot( (float) 0.5, -90, 0, PIDAxis.gyro);
                if ( Math.abs(OmniBot.gyroScope.currentHeadingZ) >= 2.5) {
                    nextState = MotorState.DRIVEOFFPLATFORM ;
                }
                break;
            case DRIVEOFFPLATFORM:
                if (Math.abs(OmniBot.gyroScope.currentHeadingZ) <= 2.5 ) {
                    OmniBot.driveOmniBot(0, 0, 0, PIDAxis.gyro);
                    nextState = MotorState.WAIT ;
                    StabilizationTimer.reset();
                    stateAfterNext = MotorState.SQUAREVUFORIARY ;
                }
                break;
            case SQUAREVUFORIARY:
                OmniBot.driveOmniBot(0, 0, 0, PIDAxis.ry);
                if (Math.abs(OmniBot.VuReader.getVuforiaCoords(HardwareVuforia.vuForiaCoord.rY)) <= 2) {
                    OmniBot.driveOmniBot(0, 0, 0, PIDAxis.ry ) ;
                    nextState = MotorState.WAIT ;
                    StabilizationTimer.reset();
                    stateAfterNext = MotorState.SQUAREVUFORIATY ;
                }
                break;
            case SQUAREVUFORIATY:
                OmniBot.driveOmniBot(0, 0, 0, PIDAxis.ty);
                if (Math.abs(OmniBot.VuReader.getVuforiaCoords(HardwareVuforia.vuForiaCoord.tY)) <= 2) {
                    OmniBot.driveOmniBot(0, 0, 0, PIDAxis.ty ) ;
                    nextState = MotorState.WAIT ;
                    StabilizationTimer.reset();
                    stateAfterNext = MotorState.SQUAREVUFORIATZ ;
                }
                break;
            case SQUAREVUFORIATZ:
                OmniBot.driveOmniBot(0, 0, 0, PIDAxis.tz);
                if (500 - Math.abs(OmniBot.VuReader.getVuforiaCoords(HardwareVuforia.vuForiaCoord.tZ)) <= 2) {
                    OmniBot.driveOmniBot(0, 0, 0, PIDAxis.tz ) ;
                    nextState = MotorState.WAIT ;
                    StabilizationTimer.reset();
                    stateAfterNext = null ;
                }
                break;
            case INITIALIZEDRIVETOCRYPTO:
                OmniBot.driveOmniBot( (float) 0.5, 0, 0, PIDAxis.gyro);
                nextState = MotorState.DRIVETOCRYPTO ;
                break;
            case DRIVETOCRYPTO:
                if (StabilizationTimer.time() >= 500) {
                    nextState = MotorState.WAIT ;
                    stateAfterNext = MotorState.COUNTBARS ;
                }
                break;
            case COUNTBARS:
                OmniBot.driveOmniBot( (float) 0.3, -90, 0, PIDAxis.gyro);
                OmniBot.crypto.lowerCryptoServo();
                if (OmniBot.crypto.cryptoBoxTouchValue2) {
                    barCounter += 1 ;
                    nextState = MotorState.COUNTSTAGE ;
                }
                break;
            case WAIT:
                if (StabilizationTimer.time() >= 300) {
                    nextState = stateAfterNext ;
                }
                break;
        }

        currentState = nextState ;

        vuMark =  OmniBot.VuReader.GetLocation() ;
        telemetry.update();

    }

    @Override
    public void stop() {

    }
    void composeTelemetry() {

        OmniBot.addTelemetry(telemetry);

    }




}