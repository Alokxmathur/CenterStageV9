package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;

import java.util.Locale;

public class Arm {
    DcMotor shoulder, elbow;
    Servo rotator, wrist, sorter;
    boolean shoulderRetained, elbowRetained;

    public Arm(HardwareMap hardwareMap) {
        //initialize our shoulder motor
        this.shoulder = hardwareMap.get(DcMotor.class, RobotConfig.SHOULDER);
        this.shoulder.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //initialize our elbow motor
        this.elbow = hardwareMap.get(DcMotor.class, RobotConfig.ELBOW);
        this.elbow.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //and the rotator
        this.rotator = hardwareMap.get(Servo.class, RobotConfig.ROTATOR);
        //and the bucket
        this.wrist = hardwareMap.get(Servo.class, RobotConfig.BUCKET);
        //and the sorter
        this.sorter = hardwareMap.get(Servo.class, RobotConfig.SORTER);

        ensureMotorDirections();
        assumeInitialPosition();
    }

    public void ensureMotorDirections() {
        this.elbow.setDirection(DcMotorSimple.Direction.FORWARD);
        this.shoulder.setDirection(DcMotorSimple.Direction.FORWARD);
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

    public void leftSorterIncrementally() {
        this.sorter.setPosition(sorter.getPosition() - RobotConfig.SERVO_INCREMENT);
    }

    public void rightSorterIncrementally() {
        this.sorter.setPosition(sorter.getPosition() + RobotConfig.SERVO_INCREMENT);
    }

    public void sorterLeft() {
        this.sorter.setPosition(RobotConfig.SORTER_LEFT_POSITION);
    }

    public void sorterRight() {
        this.sorter.setPosition(RobotConfig.SORTER_RIGHT_POSITION);
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
            }
        }
    }

    private void setPositions(ArmPosition armPosition) {
        setElbowPosition(armPosition.getElbow());
        setShoulderPosition(armPosition.getShoulder());
        rotator.setPosition(armPosition.getRotator());
        wrist.setPosition(armPosition.getWrist());
        sorter.setPosition(armPosition.getSorter());
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
     * Returns true if elbow and shoulder are within range
     * @return
     */
    public boolean isWithinRange() {
        //Match.log(getStatus());
        return shoulderIsWithinRange() && elbowIsWithinRange();
    }

    private boolean shoulderIsWithinRange() {
        return Math.abs(shoulder.getTargetPosition() - shoulder.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_SHOULDER_ERROR;
    }

    private boolean elbowIsWithinRange() {
        return Math.abs(elbow.getTargetPosition() - elbow.getCurrentPosition()) <= RobotConfig.ACCEPTABLE_ELBOW_ERROR;
    }

    /**
     * Returns the status of the arm
     * Reports the current position, target position and power of the shoulder,
     * current position, target position and power of the elbow,
     * the position of the wrist and the position of the claw
     * @return
     */
    public String getStatus() {
        return String.format(Locale.getDefault(), "S:%d->%d@%.2f, E:%d->%d@%.2f, R:%.3f, W:%.3f, S:%.3f",
                shoulder.getCurrentPosition(), shoulder.getTargetPosition(), shoulder.getPower(),
                elbow.getCurrentPosition(), elbow.getTargetPosition(), elbow.getPower(),
                rotator.getPosition(), wrist.getPosition(), sorter.getPosition());
    }
}
