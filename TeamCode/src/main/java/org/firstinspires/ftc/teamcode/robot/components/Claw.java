package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

public class Claw {

    Servo claw;

    public Claw(HardwareMap hardwareMap) {
        this.claw = hardwareMap.get(Servo.class, RobotConfig.BUCKET);

        assumeInitialPosition();
    }

    public void assumeInitialPosition() {
        this.claw.setPosition(RobotConfig.WRIST_INTAKE_POSITION);
    }
}
