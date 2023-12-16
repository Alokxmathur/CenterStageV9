package org.firstinspires.ftc.teamcode.robot.components;

public class ArmPosition {
    public int getShoulder() {
        return shoulder;
    }

    public int getElbow() {
        return elbow;
    }

    int shoulder, elbow;
    double rotator, wrist;

    public ArmPosition(int shoulder, int elbow, double rotator, double wrist) {
        this.shoulder = shoulder;
        this.elbow = elbow;
        this.rotator = rotator;
        this.wrist = wrist;
    }

    public double getRotator() {
        return rotator;
    }
    public double getWrist() {
        return wrist;
    }
}
