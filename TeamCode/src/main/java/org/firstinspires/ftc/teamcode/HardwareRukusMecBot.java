package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.VuforiaWebcam;

import java.security.PublicKey;

import static org.firstinspires.ftc.teamcode.FormatHelper.formatDouble;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  11  drive motor:        "drive_wheel_11"
 * Motor channel:  01 drive motor:        "drive_wheel_01"
 * Motor channel:  10 drive motor:        "drive_wheel_10"
 * Motor channel:  00 drive motor:        "drive_wheel_00"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"

 */

enum robotHWconnected {
    MotorOnly,
    MotorLifter,
    MotorLifterMarker,
    MotorLifterMarkerGrabber,
    MotorLifterMarkerGrabberArm,
    MotorGyro,
    MotorGyroServo,
    MotorGyroLifter,
    MotorGyroLifterVuforLocal,
    MotorGyroLifterVuforJewel,
    MotorLifterVufor,
    MotorJewel,
    MotorVufor,
    MotorVuforWebcam,
    VuforOnly,
    VuforWebcam,
    MotorGyroVuforWebcam,
    MotorGyroVuforWebcamLifter,
    MotorGyroVuforWebcamLifterMarker,
    MotorGyroVuforWebcamLifterMarkerGrabber,
    MotorGyroVuforWebcamMarker


}

enum PIDAxis {
    gyro,
    tx,
    ty,
    tz,
    rx,
    ry,
    rz
}

enum Color {
    Blue,
    Red,
    None
}

public class HardwareRukusMecBot
{

    /* Public OpMode members. */
    private Color targetSide = Color.Blue;
    private robotHWconnected connectedHW = robotHWconnected.MotorGyroLifterVuforLocal;
    protected DcMotor Motor00   = null;
    protected DcMotor  Motor01  = null;
    protected DcMotor  Motor10   = null;
    protected DcMotor  Motor11  = null;
    protected Servo servoJewel = null ;
    protected double power00 = 0 ;
    protected double power01 = 0 ;
    protected double power10 = 0 ;
    protected double power11 = 0 ;
    protected static final float motorPowerMin = -1 ;
    protected static final float motorPowerMax = 1 ;
    protected  float gamePad1LeftStickMagnitude = 0 ;
    protected  double maxPower = 1;
    protected boolean mototConnected = false;
    protected boolean gyroConnected = false;
    protected boolean lifterConnected = false ;
    protected boolean vuForLocalConnected = false ;
    protected boolean TeamMarkerConnected = false;
    protected boolean jewelConnected = false;
    protected boolean grabberConnected = false;
    protected boolean vuForWebConnected = false;
    protected boolean armConnected = false ;
    protected HardwareJewel jewelSystem = null ;
    protected HardwareGyro gyroScope = null;
    protected HardwareLifter lifter = null ;
    protected HardwareGrabber grabber = null;
    public HardwareRukusVuforia VuReader = null ;
    protected HWTeamMarkerServo Markerservo = null ;
    protected HardwareArm arm = null ;
    protected double TargetHeading = 0.0;
    private PIDController motorPID = null;
    protected boolean FirstCallPIDDrive = true;
    protected double kp = 0.0055 ;
    protected double ki = 0.000010 ;
    public double correction = 0.0 ;


    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareRukusMecBot(){
    }
/*
    public HardwareRukusMecBot(robotHWconnected ConnectedParts, Boolean We){
        setConnectedHW(ConnectedParts);
    }
*/

    public HardwareRukusMecBot(robotHWconnected ConnectedParts) {
        setConnectedHW(ConnectedParts);
    }

    private void setConnectedHW(robotHWconnected ConnectedParts) {
        if(ConnectedParts == robotHWconnected.MotorOnly) {
            mototConnected = true;
        }
        if((ConnectedParts == robotHWconnected.MotorGyro) || (ConnectedParts == robotHWconnected.MotorGyroServo)){
            mototConnected = true;
            gyroConnected = true;
        }
        if (ConnectedParts == robotHWconnected.MotorLifter) {
            mototConnected = true;
            lifterConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorLifterMarker) {
            mototConnected = true;
            lifterConnected = true ;
            TeamMarkerConnected = true;
        }
        if (ConnectedParts == robotHWconnected.MotorLifterMarkerGrabber) {
            mototConnected = true;
            lifterConnected = true ;
            TeamMarkerConnected = true;
            grabberConnected = true;
        }
        if (ConnectedParts == robotHWconnected.MotorLifterMarkerGrabberArm) {
            mototConnected = true;
            lifterConnected = true ;
            TeamMarkerConnected = true;
            grabberConnected = true;
            armConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorGyroLifter) {
            mototConnected = true;
            gyroConnected = true ;
            lifterConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorGyroLifterVuforLocal) {
            mototConnected = true;
            gyroConnected = true ;
            lifterConnected = true ;
            vuForLocalConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorGyroLifterVuforJewel) {
            mototConnected = true;
            gyroConnected = true ;
            lifterConnected = true ;
            vuForLocalConnected = true ;
            jewelConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorVufor) {
            mototConnected = true;
            vuForLocalConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorVuforWebcam) {
            mototConnected = true;
            vuForWebConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorLifterVufor) {
            mototConnected = true;
            vuForLocalConnected = true ;
            lifterConnected = true;
        }
        if (ConnectedParts == robotHWconnected.MotorJewel) {
            mototConnected = true;
            jewelConnected = true;
        }
        if(ConnectedParts == robotHWconnected.VuforOnly) {
            vuForLocalConnected = true;
        }
        if(ConnectedParts == robotHWconnected.VuforWebcam){
            vuForWebConnected = true;
        }
        if(ConnectedParts == robotHWconnected.MotorGyroVuforWebcam){
            vuForWebConnected = true;
            mototConnected = true;
            gyroConnected = true;
        }
        if (ConnectedParts == robotHWconnected.MotorGyroVuforWebcamLifter){
            vuForWebConnected = true;
            mototConnected = true;
            gyroConnected = true;
            lifterConnected = true ;
        }
        if (ConnectedParts == robotHWconnected.MotorGyroVuforWebcamLifterMarker){
            vuForWebConnected = true;
            mototConnected = true;
            gyroConnected = true;
            lifterConnected = true ;
            TeamMarkerConnected = true;
        }
        if (ConnectedParts == robotHWconnected.MotorGyroVuforWebcamLifterMarkerGrabber){
            vuForWebConnected = true;
            mototConnected = true;
            gyroConnected = true;
            lifterConnected = true ;
            TeamMarkerConnected = true;
            grabberConnected = true;
        }
        if(ConnectedParts == robotHWconnected.MotorGyroVuforWebcamMarker){
            vuForWebConnected = true;
            mototConnected = true;
            gyroConnected = true;
            TeamMarkerConnected = true;
        }
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, Color targColor) {

        // Save reference to Hardware map
        hwMap = ahwMap;
        targetSide = targColor;
        if(mototConnected) {
            // Define and Initialize Motors
            Motor00 = hwMap.dcMotor.get("drive_wheel_00");
            Motor01 = hwMap.dcMotor.get("drive_wheel_01");
            Motor10 = hwMap.dcMotor.get("drive_wheel_10");
            Motor11 = hwMap.dcMotor.get("drive_wheel_11");
            Motor00.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor01.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor10.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor11.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Motor00.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor01.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor10.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Motor11.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            Motor10.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            Motor11.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        }
        // colorSensor = hwMap.colorSensor.get("colorSensor1");
        if(gyroConnected) {
            gyroScope = new HardwareGyro();
            RobotLog.i("defined gyroscope");
            gyroScope.init(hwMap);
            RobotLog.i("Init Complete Gyro");
        }
        if (vuForLocalConnected) {
            VuReader = new HardwareRukusVuforia();
            VuReader.init(hwMap);
            RobotLog.i("defined Vufor") ;
            RobotLog.i("Init Complete Vuforia");
        }
        if (vuForWebConnected) {
            VuReader = new HardwareRukusVuforia("Webcam 1");
            VuReader.init(hwMap);
            RobotLog.i("defined Vufor") ;
            RobotLog.i("Init Complete Vuforia");
        }
        if (lifterConnected) {
            lifter = new HardwareLifter(LifterHWcontroller.Lifter);
            RobotLog.i("defined lifter");
            lifter.init(hwMap);
            RobotLog.i("Init COmplete Lifter");
        }
        if (grabberConnected) {
            grabber = new HardwareGrabber();
            RobotLog.i("defined Grabber");
            grabber.init(hwMap);
        }
        if (TeamMarkerConnected) {
            Markerservo = new HWTeamMarkerServo() ;
            RobotLog.i("defined Markerservo class") ;
            Markerservo.init(hwMap) ;
        }
        if (jewelConnected){
            jewelSystem = new HardwareJewel();
            RobotLog.i("Jewel Defined");
            jewelSystem.init(hwMap, targColor);
        }
        if (armConnected){
            arm = new HardwareArm();
            RobotLog.i("Arm Defined");
            arm.init(hwMap);
        }

        if(mototConnected) {
            // Set all motors to zero power
            Motor00.setPower(0);
            Motor01.setPower(0);
            Motor10.setPower(0);
            Motor11.setPower(0);


            // Set all motors to run without encoders.
            // May want to use RUN_USING_ENCODERS if encoders are installed.
            Motor00.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Motor01.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Motor10.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            Motor11.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void start() {
        if (gyroConnected) {
            gyroScope.start();
        }
        if (lifterConnected) {
            lifter.start();
        }
        if (vuForLocalConnected || vuForWebConnected) {
            VuReader.start();
        }
        if (TeamMarkerConnected) {
            Markerservo.start();
        }
        if(jewelConnected) {
            jewelSystem.start();
        }
        if(armConnected) {
            arm.start();
        }
    }

    public void lowerRobot() {
        if (lifterConnected) {
            lifter.setLifter((float)-0.5);
        }
    }
    public void lowerRobotSlow() {
        if (lifterConnected) {
            lifter.setLifter((float)-0.25);
        }
    }
    public boolean robotDown() {
        if (lifterConnected) {
            return lifter.down() ;
        } else {
            return true;
        }
    }

    public boolean hookReleased() {
        return true;
    }
    public void raiseRobot() {
        if (lifterConnected) {
            lifter.setLifter((float)0.8);
        }
    }
    public boolean robotUp() {
        if (lifterConnected) {
        return lifter.up() ;
        } else {
            return true ;
        }
    }

    public void lifterStop() {
        if(lifterConnected) {
            lifter.setLifter((float) 0.0);
        }
    }
    public void setMarkerServoPosition(double position) {
        Markerservo.setTeamMarkerPosition(position);
    }
    public double getMarkerServoPosition(){
        return(Markerservo.getTeamMarkerServoPosition());
    }

    public double getVuX (){
        if (vuForWebConnected){
            return (VuReader.getVuforiaCoords(HardwareRukusVuforia.vuForiaCoord.tX));
        }
        else {
            return (0.0) ;
        }
    }
    public double getVuY (){
        if (vuForWebConnected){
          return (VuReader.getVuforiaCoords(HardwareRukusVuforia.vuForiaCoord.tY));
        }
        else {
            return (0.0) ;
        }
    }
    public double getVuHeading (){
        if (vuForWebConnected || vuForLocalConnected){
            return (VuReader.getVuforiaCoords(HardwareRukusVuforia.vuForiaCoord.rY));
        }
        else {
            RobotLog.i("you have made a grave mistake vuforia is not connected") ;
            return (0.0) ;
        }
    }
    public boolean VuRukusSeen() {
        if (vuForWebConnected) {
            return (VuReader.isTargetVisible());
        }
        else {
            return (true);
        }
    }


    public void setBotMovement (double motorPower00, double motorPower01, double motorPower10, double motorPower11) {

        motorPower00 = Range.clip(motorPower00, motorPowerMin, motorPowerMax);
        motorPower01 = Range.clip(motorPower01, motorPowerMin, motorPowerMax);
        motorPower10 = Range.clip(motorPower10, motorPowerMin, motorPowerMax);
        motorPower11 = Range.clip(motorPower11, motorPowerMin, motorPowerMax);

        power00 = motorPower00 ;
        power01 = motorPower01 ;
        power10 = motorPower10 ;
        power11 = motorPower11 ;

        Motor00.setPower(motorPower00);
        Motor01.setPower(motorPower01);
        Motor10.setPower(motorPower10);
        Motor11.setPower(motorPower11);
    }

    public double getcurrentHeading() {
        return(gyroScope.currentHeadingZ);
    }
    public double getCurrentAccelerationX () {return (gyroScope.currentAccelerationX) ; }
    public double getCurrentAccelerationY () {return (gyroScope.currentAccelerationY) ; }
    public void dropmarker(){
        Markerservo.dropTeamMarker();
    }
    public void liftmarker(){
        Markerservo.liftMarkerHolder();
    }
    public boolean isMarkerDropped () {
        return (Markerservo.isTeamMarkerDropped()) ;
    }

    public void resetFirstPIDDrive (double kp_, double ki_) {
        RobotLog.i("ResetFirstCall Fixed");
        FirstCallPIDDrive = true;

        kp = kp_ ;
        ki = ki_ ;
    }


    public void gyroDriveStaight(double power00, double power01, double power10, double power11, double targetHeading) {
        double currentDiff = 0.0;

        if(FirstCallPIDDrive) {
            RobotLog.i("Set up PID Target " + targetHeading);
            RobotLog.i("Current Heading" + gyroScope.currentHeadingZ);

            motorPID = new PIDController(targetHeading, kp, ki, 0);
            FirstCallPIDDrive = false;
        }

        currentDiff = targetHeading - gyroScope.currentHeadingZ;
        correction = motorPID.Update(gyroScope.currentHeadingZ);


        power00 += correction;
        power01 += correction;
        power10 -= correction;
        power11 -= correction;



        setBotMovement(power00, power01, power10, power11) ;

    }

    public void rxSquareBot ( float targetHeading ) {
        double currentHeadingRX = 0.0;

 //       VuReader.updateVuforiaCoords();
        currentHeadingRX = getVuX();
//        currentHeadingRX = VuReader.getVuforiaCoords(HardwareRukusVuforia.vuForiaCoord.rX) ;

        if(FirstCallPIDDrive) {

            RobotLog.i("Set up PID Target " + targetHeading);
            RobotLog.i("Current Heading" + currentHeadingRX);

            motorPID = new PIDController(targetHeading, kp, ki, 0);
            FirstCallPIDDrive = false;
        }

        correction = motorPID.Update(currentHeadingRX);

        if(VuReader.isTargetVisible()){
            RobotLog.i("Trackable is ", VuReader.getTrackableName());
        } else {
            RobotLog.i("Trackable not Visable.");
        }

        if ( VuReader.getTrackableName().equals("Blue-Rover") ) {
            setBotMovement(correction, correction, -correction, -correction) ;
        } else if ( VuReader.getTrackableName().equals("Red-FootPrint") ) {
            setBotMovement(-correction, -correction, correction, correction) ;
        }
    }

    public void rySquareBot ( float targetHeading ) {
        double currentHeadingRY = 0.0;
        double parrallelAngle = 0.0 ;

        currentHeadingRY = getVuHeading() ;

        if(FirstCallPIDDrive) {

            parrallelAngle = (1800-getVuY()) / Math.abs(getVuX()) ;
            parrallelAngle = Math.atan(parrallelAngle)*180/Math.PI ;
            targetHeading = (float) parrallelAngle  - 90 ;
            if (Math.signum(getVuX()) != Math.signum(getVuY())) {
                targetHeading = -targetHeading ;
            }
            TargetHeading = targetHeading ;

            RobotLog.i("Set up PID Target " + targetHeading);
            RobotLog.i("Current Heading" + currentHeadingRY);

            motorPID = new PIDController(targetHeading, kp, ki, 0);
            FirstCallPIDDrive = false;
        }

        correction = motorPID.Update(currentHeadingRY);

        if(VuReader.isTargetVisible()){
            RobotLog.i("Trackable is ", VuReader.getTrackableName());
        } else {
            RobotLog.i("Trackable not Visable.");
        }

        if ( VuReader.getTrackableName().equals("Blue-Rover") ) {
            setBotMovement(0, -correction/2, correction/2, correction) ;
            RobotLog.i("At Blue side, current heading " + currentHeadingRY) ;
        } else if ( VuReader.getTrackableName().equals("Red-FootPrint") ) {
            setBotMovement(0, correction/2, -correction/2, -correction) ;
            RobotLog.i("At Red side, current heading " + currentHeadingRY) ;
        } else {
            RobotLog.i("error null picture name") ;
        }
    }

    public void txSquareBot ( float targetHeading ) {
        double currentHeadingTX = 0.0;

//        VuReader.updateVuforiaCoords();
//        currentHeadingTX = VuReader.getVuforiaCoords(HardwareRukusVuforia.vuForiaCoord.tX) ;
        currentHeadingTX = getVuX();


        if(FirstCallPIDDrive) {
            RobotLog.i("Set up PID Target " + targetHeading);
            RobotLog.i("Current Heading" + currentHeadingTX);

            motorPID = new PIDController(targetHeading, kp, ki, 0);
            FirstCallPIDDrive = false;
        }

        correction = motorPID.Update(currentHeadingTX);

        if(VuReader.isTargetVisible()){
            RobotLog.i("Trackable is ", VuReader.getTrackableName());
        } else {
            RobotLog.i("Trackable not Visable.");
        }

        if ( VuReader.getTrackableName().equals("Blue-Rover") ) {
            setBotMovement(-correction, correction, correction, -correction) ;
        } else if ( VuReader.getTrackableName().equals("Red-FootPrint") ) {
            setBotMovement(correction, -correction, -correction, correction) ;
        }
    }

    public void tySquareBot ( float targetHeading ) {
        double currentHeadingTY = 0.0;

//        VuReader.updateVuforiaCoords();
//        currentHeadingTY = VuReader.getVuforiaCoords(HardwareRukusVuforia.vuForiaCoord.tY) ;
        currentHeadingTY =  getVuY();

        if(FirstCallPIDDrive) {
            RobotLog.i("Set up PID Target " + targetHeading);
            RobotLog.i("Current Heading" + currentHeadingTY);

            motorPID = new PIDController(targetHeading, kp, ki, 0);
            FirstCallPIDDrive = false;
        }

        correction = motorPID.Update(currentHeadingTY);

        if(VuReader.isTargetVisible()){
            RobotLog.i("Trackable is ", VuReader.getTrackableName());
        } else {
            RobotLog.i("Trackable not Visable.");
        }

        if ( VuReader.getTrackableName().equals("Blue-Rover") ) {
            setBotMovement(correction, -correction, -correction, correction) ;
        } else if ( VuReader.getTrackableName().equals("Red-FootPrint") ) {
            setBotMovement(-correction, correction, correction, -correction) ;
        }
    }

    public void driveBot (float magnitude, float direction, float targetHeading, PIDAxis axis) {
        if (axis == PIDAxis.gyro) {
            float yValue = (float) Math.cos( direction*(Math.PI/180) ) ;
            float xValue = (float) Math.sin( direction*(Math.PI/180) ) ;

            RobotLog.i("cos is " + yValue);
            RobotLog.i("sin is " + xValue);

            double power00 = (yValue - xValue)*magnitude ;
            double power01 = (yValue + xValue)*magnitude ;
            double power10 = (yValue + xValue)*magnitude ;
            double power11 = (yValue - xValue)*magnitude ;


            gyroDriveStaight(power00, power01, power10, power11, targetHeading);
        } else if (axis == PIDAxis.rx) {
            rxSquareBot(targetHeading);
        } else if (axis == PIDAxis.ry) {
            rySquareBot(targetHeading);
        } else if (axis == PIDAxis.tx) {
            txSquareBot(targetHeading);
        } else if (axis == PIDAxis.ty) {
            tySquareBot(targetHeading);
        }


    }

    public void setLifter (float lifterSpeed) {
        lifter.setLifter(lifterSpeed);
    }


    public double vuforiaCoordinates (HardwareRukusVuforia.vuForiaCoord axis) {return VuReader.getVuforiaCoords(axis) ;}

    public void addTelemetry(Telemetry telemetry) {


        if(gyroConnected){

            while (gyroScope.gyroInitThreadIsAlive())  {

            }

            gyroScope.addTelemetry(telemetry);
            telemetry.addLine()
                    .addData("targetHeading ", new Func<String>() {
                        @Override public String value() {
                            return formatDouble(TargetHeading);
                        }
                    })
                    .addData("currentHeading" , new Func<String>() {
                        @Override public String value() {
                            return formatDouble(gyroScope.currentHeadingZ);
                        }
                    })
                    .addData("Current Error", new Func<String>() {
                        @Override public String value() {
                            return formatDouble(correction) ;
                        }}) ;

        }
        if(mototConnected) {
            telemetry.addLine()
                    .addData("Motor Power 00", new Func<String>() {
                        @Override
                        public String value() {
                            return FormatHelper.formatDouble(Motor00.getPower());
                        }
                    })
                    .addData("Motor Power 01", new Func<String>() {
                        @Override
                        public String value() {
                            return FormatHelper.formatDouble(Motor01.getPower());
                        }
                    });
            telemetry.addLine()
                    .addData("Motor Power 10", new Func<String>() {
                        @Override
                        public String value() {
                            return FormatHelper.formatDouble(Motor10.getPower());
                        }
                    })
                    .addData("Motor Power 11", new Func<String>() {
                        @Override
                        public String value() {
                            return FormatHelper.formatDouble(Motor11.getPower());
                        }
                    });
        }
        if(lifterConnected) {
            lifter.addTelemetry(telemetry);
        }
        if(jewelConnected) {
            //jewelSystem.addTelemetry(telemetry);
        }
        if(grabberConnected) {
            grabber.addTelemetry(telemetry);
        }
        if(TeamMarkerConnected) {
            Markerservo.addTelemetry(telemetry);
        }
        if(vuForLocalConnected || vuForWebConnected) {
            VuReader.addTelemetry(telemetry);
        }
        if (armConnected) {
            arm.addTelemetry(telemetry);
        }
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     */


    public void waitForTick(long periodMs) {
        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
        if(gyroConnected) {
            gyroScope.Update();
            gyroScope.UpdateAcceleration();
        }

        if (vuForWebConnected || vuForLocalConnected) {
            VuReader.UpdateLocation();
            RobotLog.i("updated vuforia coordinates") ;
        }

        RobotLog.i("Motor Powers: 00: " + power00 + ", 01: " + power01 + ", 10: " + power10 + ", 11: " + power11) ;


    }
    public double maxPowerIdentifier (double motorPower00, double motorPower01, double motorPower10, double motorPower11) {
        double power1 = Math.max(motorPower00,motorPower01) ;
        double power2 = Math.max(motorPower10,motorPower11) ;
        double maxPower = Math.max(power1,power2) ;
        return maxPower ;
    }

    public double angle360(double angle) {
        if (angle < 0) {
            angle += 360 ;
        } else if (angle > 360){
            angle -= 360;
        }else {

        }
        return angle ;
    }

    public double angle180(double angle) {
        if (angle > 180) {
            angle -= 360 ;
        } else if (angle < -180){
            angle += 360 ;
        } else {

        }
        return angle ;
    }
}
