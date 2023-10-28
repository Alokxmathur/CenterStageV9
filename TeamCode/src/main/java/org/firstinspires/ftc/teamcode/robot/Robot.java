package org.firstinspires.ftc.teamcode.robot;

import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.Arm;
import org.firstinspires.ftc.teamcode.robot.components.DroneLauncher;
import org.firstinspires.ftc.teamcode.robot.components.InOutTake;
import org.firstinspires.ftc.teamcode.robot.components.LED;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetectorWebcam;
import org.firstinspires.ftc.teamcode.robot.operations.Operation;
import org.firstinspires.ftc.teamcode.robot.operations.OperationThread;

/**
 * This class represents our robot.
 * <p>
 * It supports the following controls:
 * GamePad1:
 * Left stick - drive, right stick - rotate
 * x - abort pending operations
 * <p>
 * a - lowest arm level
 * b - middle arm level
 * y - top arm level
 * Dpad Up - raise intake platform
 * Dpad Down - lower intake platform
 * Dpad Left - forward rotator
 * Dpad right -backward rotator
 * <p>
 * GamePad2:
 * Left stick - y axis - carousel speed
 * <p>
 * Dpad Up - raise output arm
 * Dpad Down - lower output arm
 * <p>
 * Dpad Left -
 * If right bumper is pressed
 * Open Lid more
 * Else
 * retract output arm
 * Dpad Right - extend output arm
 * If right bumper is pressed
 * Close Lid more
 * Else
 * extend output arm
 * <p>
 * Left trigger -
 * If right bumper is pressed: open to capping position
 * else open bucket
 * Right trigger - close bucket
 * <p>
 * x - if left bumper is pressed, tell output that this is the correct intake level for intake
 * otherwise
 * go to intake position
 * a - if left bumper is pressed, tell output that this is the correct intake level for low level
 * otherwise
 * go to lowest arm level
 * b - if left bumper is pressed, tell output that this is the correct intake level for middle level
 * otherwise
 * go to middle arm level
 * y - if left bumper is pressed, tell output that this is the correct intake level for top level
 * otherwise
 * go to top arm level
 */

public class Robot {

    Telemetry telemetry;
    private HardwareMap hardwareMap;
    Match match;

    OperationThread operationThreadPrimary;
    OperationThread operationThreadSecondary;
    OperationThread operationThreadTertiary;

    DriveTrain driveTrain;
    LED led;
    Arm arm;
    InOutTake inOutTake;
    DroneLauncher droneLauncher;

    ObjectDetectorWebcam webcam;

    boolean everythingButCamerasInitialized = false;

    //Our sensors etc.

    //our state
    String state = "pre-initialized";

    public Robot() {
        Log.d("SilverTitans", "Robot: got created");
    }

    /**
     * Initialize our robot
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry, Match match) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.match = match;

        //initialize our components
        initCameras();
        initDriveTrain();
        this.led = new LED(hardwareMap);
        this.led.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);

        this.droneLauncher = new DroneLauncher(hardwareMap);

        this.arm = new Arm(hardwareMap);
        this.inOutTake= new InOutTake(hardwareMap);

        telemetry.addData("Status", "Creating operations thread, please wait");
        telemetry.update();

        Match.log("Started operations threads");
        this.operationThreadPrimary = new OperationThread(this, "Primary", telemetry);
        operationThreadPrimary.start();
        this.operationThreadSecondary = new OperationThread(this, "Secondary", telemetry);
        operationThreadSecondary.start();
        this.operationThreadTertiary = new OperationThread(this, "Tertiary", telemetry);
        operationThreadTertiary.start();

        this.everythingButCamerasInitialized = true;
    }

    public void initDriveTrain() {
        //Create our drive train
        telemetry.addData("Status", "Initializing drive train, please wait");
        telemetry.update();
        this.driveTrain = new DriveTrain(hardwareMap);
    }

    public void initCameras() {
        //initialize webcam
        Match.log("Initializing Webcam");
        telemetry.addData("Status", "Initializing Webcam, please wait");
        telemetry.update();
        this.webcam = new ObjectDetectorWebcam();
        this.webcam.init(hardwareMap, telemetry);
    }

    /**
     * Stop the robot
     */
    public void stop() {
        //Stop all of our motors
        Match.log("Stopping robot");
        if (this.operationThreadPrimary != null) {
            this.operationThreadPrimary.abort();
        }
        if (this.operationThreadSecondary != null) {
            this.operationThreadSecondary.abort();
        }
        if (this.operationThreadTertiary != null) {
            this.operationThreadTertiary.abort();
        }
        if (this.driveTrain != null) {
            this.driveTrain.stop();
        }
        if (this.webcam != null) {
            this.webcam.stop();
        }
        Match.log(("Robot stopped"));
    }

    public void queuePrimaryOperation(Operation operation) {
        this.operationThreadPrimary.queueUpOperation(operation);
    }

    public void queueSecondaryOperation(Operation operation) {
        this.operationThreadSecondary.queueUpOperation(operation);
    }

    public void queueTertiaryOperation(Operation operation) {
        this.operationThreadTertiary.queueUpOperation(operation);
    }

    /**
     * Returns the current x value of robot's center in mms
     *
     * @return the current x position in mms
     */
    public double getCurrentX() {
        return 0;//this.vslamCamera.getPoseEstimate().getX() * Field.MM_PER_INCH;
    }

    /**
     * Returns the current y value of robot's center in mms
     *
     * @return the current y position in mms
     */
    public double getCurrentY() {
        return 0;//this.vslamCamera.getPoseEstimate().getY() * Field.MM_PER_INCH;
    }

    /**
     * Returns the current heading of the robot in radians
     *
     * @return the heading in radians
     */
    public double getCurrentTheta() {
        return 0;//AngleUnit.normalizeRadians(this.vslamCamera.getPoseEstimate().getHeading());
    }

    public boolean allOperationsCompleted() {
        return primaryOperationsCompleted() && secondaryOperationsCompleted() && tertiaryOperationsCompleted();
    }

    public boolean primaryOperationsCompleted() {
        return !this.operationThreadPrimary.hasEntries();
    }

    public boolean secondaryOperationsCompleted() {
        return !this.operationThreadSecondary.hasEntries();
    }

    public boolean tertiaryOperationsCompleted() {
        return !this.operationThreadTertiary.hasEntries();
    }

    public boolean havePosition() {
        return true;//vslamCamera.havePosition();
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean fullyInitialized() {
        return this.everythingButCamerasInitialized;
    }

    /*
        gamePad 2 dpad up/down open/close claw incrementally
        gamePad 2 dpad left/right open/close claw totally
    */
    public void handleGameControllers(Gamepad gamePad1, Gamepad gamePad2) {
        if (gamePad1.x) {
            this.operationThreadPrimary.abort();
            this.operationThreadSecondary.abort();
            this.operationThreadTertiary.abort();
        }

        this.handleDriveTrain(gamePad1);
        handleIntake(gamePad1, gamePad2);

        if (gamePad2.right_trigger > 0.2) {
            droneLauncher.launchDrone();
        }
    }

    public void handleLED(Gamepad gamePad1, Gamepad gamePad2) {

    }

    public void handleDriveTrain(Gamepad gamePad1) {
        if (this.primaryOperationsCompleted()) {
            double multiplier = gamePad1.right_trigger > 0.1 ? .6 : (gamePad1.left_trigger > 0.1 ? 1 : .3);
            double x = Math.pow(gamePad1.left_stick_x, 5) * multiplier; // Get left joystick's x-axis value.
            double y = -Math.pow(gamePad1.left_stick_y, 5) * multiplier; // Get left joystick's y-axis value.

            double rotation = Math.pow(gamePad1.right_stick_x, 5) * multiplier; // Get right joystick's x-axis value for rotation

            this.driveTrain.drive(Math.atan2(x, y), Math.hypot(x, y), rotation);
        }
    }

    /**
     * Handle the intake
     * @param gamePad1
     * @param gamePad2
     */
    public void handleIntake(Gamepad gamePad1, Gamepad gamePad2) {
        /*
            gamePad 1 dpad left/right turn on/off the intake
        */
        if (gamePad1.dpad_left) {
            //start intake
            inOutTake.setSpeed(-1);
        }
        if (gamePad1.dpad_right) {
            //stop intake
            inOutTake.setSpeed(0);
        }

        /*
            gamePad 1 dpad up/down move rotator incrementally
        */
        if (gamePad1.dpad_up) {
            arm.backwardRotatorIncrementally();
        } else if (gamePad1.dpad_down) {
            arm.forwardRotatorIncrementally();
        }

        /*
            gamePad 2 dpad left/right open/close claw totally
        */
        if (gamePad2.dpad_left) {
            arm.openClaw();
        }
        if (gamePad2.dpad_right) {
            arm.closeClaw();
        }
        /*
            gamePad 2 dpad up/down open/close claw incrementally
        */
        if (gamePad2.dpad_up) {
            arm.openClawIncrementally();
        } else if (gamePad2.dpad_down) {
            arm.closeClawIncrementally();
        }

        if (secondaryOperationsCompleted()) {
            //handle shoulder movement
            if (Math.abs(gamePad2.left_stick_y) > 0.05) {
                this.arm.setShoulderPower(gamePad2.left_stick_y);
            } else {
                this.arm.retainShoulder();
            }
            if (Math.abs(gamePad2.right_stick_y) > 0.05) {
                this.arm.setElbowPower(Math.pow(gamePad2.right_stick_y, 7));
            } else {
                    this.arm.retainElbow();
            }
        }
    }

    public void setInitialPose(Pose2d pose) {
        //this.driveTrain.setLocalizer(vslamCamera);
        //this.vslamCamera.setCurrentPose(pose);
    }

    public void reset() {
        if (this.driveTrain != null) {
            this.driveTrain.ensureWheelDirection();
            this.driveTrain.reset();
        }
        if (this.arm != null) {
            this.arm.ensureMotorDirections();
        }
    }

    public Pose2d getPose() {
        return this.driveTrain.getPoseEstimate();
    }

    public DriveTrain getDriveTrain() {
        return this.driveTrain;
    }

    public void setPattern(RevBlinkinLedDriver.BlinkinPattern pattern) {
        this.led.setPattern(pattern);
    }

    public RevBlinkinLedDriver.BlinkinPattern getLEDStatus() {
        return led.getPattern();
    }

    public String getArmStatus() {
        return this.arm.getStatus();
    }

    public Arm getArm() {
        return this.arm;
    }

    public String getIntakeStatus() {
        return this.inOutTake.getStatus();
    }

    public ObjectDetectorWebcam getWebcam() {
        return this.webcam;
    }

    public Field.SpikePosition getSpikePosition() {
        return webcam.getSpikePosition();
    }
}
