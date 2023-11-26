package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.MiniArmOperation;

import java.util.Locale;

public class IntakeBucket {
    Servo clamp, intakeArm;

    public IntakeBucket (HardwareMap hardwareMap){
        this.clamp = hardwareMap.get(Servo.class, RobotConfig.CLAMP);
        this.intakeArm = hardwareMap.get(Servo.class, RobotConfig.INTAKE_ARM);

        assumeInitialPosition();
    }

    private void assumeInitialPosition() {
        this.clamp.setPosition(RobotConfig.CLAMP_INITIAL_POSITION);
        this.intakeArm.setPosition(RobotConfig.INTAKE_ARM_INITIAL_POSITION);
    }

    public void clamp() {
        this.clamp.setPosition(RobotConfig.CLAMP_INITIAL_POSITION);
    }

    public void unclamp() {
        this.clamp.setPosition(RobotConfig.CLAMP_OPEN_POSITION);
    }

    public void raise() {
        this.intakeArm.setPosition(RobotConfig.INTAKE_ARM_RAISED_POSITION);
    }

    public void lower() {
        this.intakeArm.setPosition((RobotConfig.INTAKE_ARM_LOWERED_POSITION));
    }

    public void incrementalUp() {
        this.intakeArm.setPosition(intakeArm.getPosition() + RobotConfig.SERVO_INCREMENT);
    }

    public void decrementalDown() {
        this.intakeArm.setPosition(intakeArm.getPosition() - RobotConfig.SERVO_INCREMENT);
    }

    public void setPositions(MiniArmOperation.Type type) {
        switch (type) {
            case Up: {
                intakeArm.setPosition(RobotConfig.INTAKE_ARM_RAISED_POSITION);
                break;
            }
            case Drop: {
                intakeArm.setPosition(RobotConfig.INTAKE_ARM_LOWERED_POSITION);
                break;
            }
        }
    }

    public void stop() {

    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "CL:%.3f, IA:%.3f",
                clamp.getPosition(), intakeArm.getPosition());
    }
}
