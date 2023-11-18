package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class DroneLauncher {
    Servo droneLauncher;

    public DroneLauncher(HardwareMap hardwareMap) {
        this.droneLauncher =hardwareMap.get(Servo.class, RobotConfig.DRONE_LAUNCHER);

        assumeInitialPosition();
    }

    public void assumeInitialPosition() {
        this.droneLauncher.setPosition(RobotConfig.DRONE_TRIGGER_INITIAL_POSITION);
    }

    public void launchDrone() {
        this.droneLauncher.setPosition(RobotConfig.DRONE_TRIGGER_RELEASE_POSITION);
    }
    public void holdDrone() {
        this.droneLauncher.setPosition(RobotConfig.DRONE_TRIGGER_INITIAL_POSITION);
    }

    public void incrementalRelease() {
        this.droneLauncher.setPosition(droneLauncher.getPosition() + RobotConfig.TRIGGER_INCREMENT);
    }

    public void decrementalRelease() {
        this.droneLauncher.setPosition(droneLauncher.getPosition() - RobotConfig.TRIGGER_INCREMENT);
    }

    public void stop() {
    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "T:%.3f",
                droneLauncher.getPosition());
    }
}
