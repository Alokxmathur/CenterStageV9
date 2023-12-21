package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;

import java.util.Locale;

public class Arm {
    public static final int CORE_HEX_MOTOR_COUNT_PER_REV = 288;
    public static final int INOUT_GEAR_RATIO = 3;

    DcMotor shoulder, elbow, inOutMotor;
    Servo rotator, wrist;
    DigitalChannel shoulderLimitSwitch, elbowLimitSwitch;
    boolean shoulderRetained,
            elbowRetained,
            shoulderReset,
            elbowReset,
            intakeReset,
            shoulderLowered,
            elbowLowered;

    public Arm(HardwareMap hardwareMap) {
        //the shoulder limit switch
        this.shoulderLimitSwitch = hardwareMap.get(DigitalChannel.class, RobotConfig.SHOULDER_LIMIT_SWITCH);
        //the elbow limit switch
        //initialize our shoulder motor
        this.shoulder = hardwareMap.get(DcMotor.class, RobotConfig.SHOULDER);
        this.shoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.elbowLimitSwitch = hardwareMap.get(DigitalChannel.class, RobotConfig.ELBOW_LIMIT_SWITCH);
        //initialize our elbow motor
        this.elbow = hardwareMap.get(DcMotor.class, RobotConfig.ELBOW);
        this.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //initialize our intake motor
        this.inOutMotor = hardwareMap.get(DcMotor.class, RobotConfig.INOUT_MOTOR);
        this.inOutMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.inOutMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.inOutMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //and the rotator
        this.rotator = hardwareMap.get(Servo.class, RobotConfig.ROTATOR);
        //and the bucket
        this.wrist = hardwareMap.get(Servo.class, RobotConfig.BUCKET);

        ensureMotorDirections();
        assumeInitialPosition();
    }

    public boolean resetArm() {
        if (!elbowReset) {
            //retainShoulder();
            //initialize elbow (lower and then raise) unless that's already done
            lowerThenRaiseElbow();
            return false;
        }
        else if (!shoulderReset) {
            //initialize shoulder (lower and then raise) unless that's already done
            lowerThenRaiseShoulder();
            return false;
        }
        else if (!intakeReset) {
            abstain();
            intakeReset = true;
            return false;
        }
        return true;
    }

    private boolean lowerThenRaiseShoulder() {
        //if the shoulder limit switch has not yet been pressed
        if (!shoulderLowered) {
            //find the state of the shoulder limit switch: true means it is pressed
            if (!(shoulderLowered = shoulderLimitSwitch.getState())) {
                setShoulderPower(-.2);
            }
            else {
                setShoulderPower(0);
            }
            return false;
        }
        else if (!shoulderReset) {
            //if the shoulder had been lowered but not raised since,
            //find if the limit switch has been raised since it was pressed
            if (!(shoulderReset = !shoulderLimitSwitch.getState())) {
                setShoulderPower(.1);
                return false;
            }
            else {
                //the limit switch is now not pressed, we are done. reset encoder and return true
                this.shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                return true;
            }
        }
        return true;
    }
    private boolean lowerThenRaiseElbow() {
        //if the elbow limit switch has not yet been pressed
        if (!elbowLowered) {
            //find the state of the elbow limit switch: true means it is pressed
            if (!(elbowLowered = elbowLimitSwitch.getState()))
            {
                setElbowPower(.4);
            }
            else {

                setElbowPower(0);
            }
            return false;
        }
        else if (!elbowReset) {
            //if the elbow had been lowered but not raised since,
            //find if the limit switch has been raised since it was pressed
            if (!(elbowReset = !elbowLimitSwitch.getState())) {
                setElbowPower(-.1);
                return false;
            }
            else {
                //the limit switch is now not pressed, we are done. reset encoder and return true
                this.elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                return true;
            }
        }
        return elbowReset;
    }

    public void ensureMotorDirections() {
        this.elbow.setDirection(DcMotorSimple.Direction.FORWARD);
        this.shoulder.setDirection(DcMotorSimple.Direction.FORWARD);
        this.inOutMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void assumeInitialPosition() {
        setPositions(RobotConfig.ARM_STARTING_POSITION);
    }

    public void raiseBucketIncrementally() {
        this.wrist.setPosition(wrist.getPosition() - RobotConfig.SERVO_INCREMENT);
    }

    public void lowerBucketIncrementally() {
        this.wrist.setPosition(wrist.getPosition() + RobotConfig.SERVO_INCREMENT);
    }

    public void intakePositionWrist() {
        this.wrist.setPosition(RobotConfig.WRIST_INTAKE_POSITION);
    }

    public void dumpPositionWrist() {
        this.wrist.setPosition(RobotConfig.WRIST_DUMP_POSITION);
    }
    public void forwardRotator() {
        this.rotator.setPosition(RobotConfig.ROTATOR_STARTING_POSITION);
    }
    public void backwardRotator() {
        this.rotator.setPosition(RobotConfig.ROTATOR_TURNED_OVER_POSITION);
    }

    public void backwardRotatorIncrementally() {
        this.rotator.setPosition(rotator.getPosition() + RobotConfig.SERVO_INCREMENT);
    }

    public void forwardRotatorIncrementally() {
        this.rotator.setPosition(rotator.getPosition() - RobotConfig.SERVO_INCREMENT);
    }

    public void stop() {
    }

    public void setPositions(ArmOperation.Type type) {
        switch (type) {
            case Intake: {
                setPositions(RobotConfig.ARM_INTAKE_POSITION);
                break;
            }
            case Deposit1: {
                setPositions(RobotConfig.ARM_DEPOSIT_POSITION_1);
                break;
            }
            case Deposit2: {
                setPositions(RobotConfig.ARM_DEPOSIT_POSITION_2);
                break;
            }
            case Deposit3: {
                setPositions(RobotConfig.ARM_DEPOSIT_POSITION_3);
                break;
            }
            case AutoDeposit: {
                setPositions(RobotConfig.ARM_AUTO_DEPOSIT_POSITION);
                break;
            }
            case Travel: {
                setPositions(RobotConfig.ARM_TRAVEL_POSITION);
                break;
            }
            case InterimTravel:Travel: {
                setPositions(RobotConfig.ARM_INTERIM_TRAVEL_POSITION);
                break;
            }
            case Travel_From_Deposit: {
                setPositions(RobotConfig.ARM_INTERIM_TRAVEL_POSITION);
                break;
            }
            case PreHang: {
                setPositions(RobotConfig.ARM_PRE_HANG_POSITION);
                break;
            }
            case Hang1: {
                setPositions(RobotConfig.ARM_HANG_POSITION_1);
                break;
            }
            case Hang2: {
                setPositions(RobotConfig.ARM_HANG_POSITION_2);
                break;
            }
            case Raised: {
                setPositions(RobotConfig.ARM_RAISED_POSITION);
                break;
            }
        }
    }

    private void setPositions(ArmPosition armPosition) {
        setElbowPosition(armPosition.getElbow());
        setShoulderPosition(armPosition.getShoulder());
        rotator.setPosition(armPosition.getRotator());
        wrist.setPosition(armPosition.getWrist());
    }

    /**
     * Set the shoulder motor position
     * @param position
     */
    public void setShoulderPosition(int position) {
        this.shoulder.setTargetPosition(position);
        this.shoulder.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.shoulder.setPower(RobotConfig.MAX_SHOULDER_POWER);
    }

    /**
     * Retain shoulder in its current position
     */
    public void retainShoulder() {
        if (!shoulderRetained) {
            setShoulderPosition(shoulder.getCurrentPosition());
            shoulderRetained = true;
        }
    }

    /**
     * Set the shoulder power
     * @param power
     */
    public void setShoulderPower(double power) {
        this.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.shoulder.setPower(power*RobotConfig.MAX_SHOULDER_POWER);
        shoulderRetained = false;
    }

    /**
     * Set the elbow position
     * @param position
     */
    public void setElbowPosition(int position) {
        this.elbow.setTargetPosition(position);
        this.elbow.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.elbow.setPower(RobotConfig.MAX_ELBOW_POWER);
    }

    /**
     * Retain elbow in its current position
     */
    public void retainElbow() {
        if (!elbowRetained) {
            setElbowPosition(elbow.getCurrentPosition());
            elbowRetained = true;
        }
    }

    /**
     * Set the elbow power
     * @param power
     */
    public void setElbowPower(double power) {
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.elbow.setPower(power);
        elbowRetained = false;
    }

    /**
     * Set the inout motor power
     * @param power
     */
    public void setInOutPower(double power) {
        this.inOutMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.inOutMotor.setPower(power);
    }

    /**
     * Returns true if elbow and shoulder are within range
     * @return
     */
    public boolean isWithinRange() {
        return shoulderIsWithinRange() && elbowIsWithinRange();
    }

    private boolean shoulderIsWithinRange() {
        return Math.abs(shoulder.getTargetPosition() - shoulder.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_SHOULDER_ERROR;
    }

    private boolean elbowIsWithinRange() {
        return Math.abs(elbow.getTargetPosition() - elbow.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_ELBOW_ERROR;
    }

    public void eat() {
        this.setInOutPower(1);
    }
    public void abstain() {
        this.inOutMotor.setTargetPosition(
                this.inOutMotor.getCurrentPosition());
        this.inOutMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.inOutMotor.setPower(1);
    }
    public void throwUp() {
        expel();
        //this.setInOutPower(-.3);
    }
    public void expel() {
        this.inOutMotor.setTargetPosition(
                this.inOutMotor.getCurrentPosition()
                        - (int) (CORE_HEX_MOTOR_COUNT_PER_REV/INOUT_GEAR_RATIO*2));
        this.inOutMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.inOutMotor.setPower(.5);
    }

    /**
     * Returns the status of the arm
     * Reports the current position, target position and power of the shoulder,
     * current position, target position and power of the elbow,
     * the in-out motor's speed
     * the position of the wrist and the position of the sorter
     * the state of the shoulder limit switch
     * @return
     */
    public String getStatus() {
        String shoulderInit = shoulderReset ? "Shoulder initialized" : "Shoulder initializing";
        String elbowInit = elbowReset ? "Elbow initialized" : "Elbow initializing";
        String intakeInit = elbowReset ? "Intake initialized" : "Intake initializing";


        return String.format(Locale.getDefault(),
                "Sh:%d->%d@%.2f, El:%d->%d@%.2f, In:%d->%d@%.2f (%s), R:%.3f, W:%.3f, %s, %s, %s",
                shoulder.getCurrentPosition(), shoulder.getTargetPosition(), shoulder.getPower(),
                elbow.getCurrentPosition(), elbow.getTargetPosition(), elbow.getPower(),
                inOutMotor.getCurrentPosition(), inOutMotor.getTargetPosition(), inOutMotor.getPower(), inOutMotor.getMode(),
                rotator.getPosition(), wrist.getPosition(),
                shoulderInit, elbowInit, intakeInit);
    }

    public boolean intakeWithinRange() {
        return Math.abs(inOutMotor.getTargetPosition() - inOutMotor.getCurrentPosition()) < 5;
    }
}
