package org.firstinspires.ftc.teamcode.robot.components;

public class ArmPosition {
    public int getShoulder() {
        return shoulder;
    }

    public int getElbow() {
        return elbow;
    }

    int shoulder, elbow;
    double rotator, claw;

    public ArmPosition(int shoulder, int elbow, double rotator, double claw) {
        this.shoulder = shoulder;
        this.elbow = elbow;
        this.rotator = rotator;
        this.claw = claw;
    }

    public double getRotator() {
        return rotator;
    }
    public double getClaw() {
        return claw;
    }
}
