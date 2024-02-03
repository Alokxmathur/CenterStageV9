package org.firstinspires.ftc.teamcode.robot.components;

public class ArmState {

    int shoulder, elbow;
    double rotator, wrist;

    boolean eat;

    public ArmState(int shoulder, int elbow, double rotator, double wrist, boolean eat) {
        this.shoulder = shoulder;
        this.elbow = elbow;
        this.rotator = rotator;
        this.wrist = wrist;
        this.eat = eat;
    }
    public int getShoulder() {
        return shoulder;
    }

    public int getElbow() {
        return elbow;
    }
    public double getRotator() {
        return rotator;
    }
    public double getWrist() {
        return wrist;
    }

    public boolean eat() {
        return eat;
    }
}
