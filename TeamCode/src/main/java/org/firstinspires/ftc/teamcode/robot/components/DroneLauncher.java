package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class DroneLauncher {
    Servo droneLauncher, droneHolder;

    public DroneLauncher(HardwareMap hardwareMap) {
        this.droneLauncher =hardwareMap.get(Servo.class, RobotConfig.DRONE_LAUNCHER);
        this.droneHolder =hardwareMap.get(Servo.class, RobotConfig.DRONE_HOLDER);

        assumeInitialPosition();
    }

    public void assumeInitialPosition() {
        this.droneLauncher.setPosition(RobotConfig.DRONE_TRIGGER_INITIAL_POSITION);
        this.droneHolder.setPosition(RobotConfig.DRONE_HOLDER_INITIAL_POSITION);
    }

    public void releaseHold() {
        this.droneHolder.setPosition(RobotConfig.DRONE_HOLDER_RELEASE_POSITION);
    }
    public void launchDrone()  {
        this.droneLauncher.setPosition(RobotConfig.DRONE_TRIGGER_RELEASE_POSITION);
    }
    public void holdDrone() {
        this.droneHolder.setPosition(RobotConfig.DRONE_HOLDER_INITIAL_POSITION);
        this.droneLauncher.setPosition(RobotConfig.DRONE_TRIGGER_INITIAL_POSITION);
    }

    public void incrementalHold() {
        this.droneHolder.setPosition(droneHolder.getPosition() + RobotConfig.TRIGGER_INCREMENT);
    }

    public void decrementalHold() {
        this.droneHolder.setPosition(droneHolder.getPosition() - RobotConfig.TRIGGER_INCREMENT);
    }

    public void incrementalLaunch() {
        this.droneLauncher.setPosition(droneLauncher.getPosition() + RobotConfig.TRIGGER_INCREMENT);
    }

    public void decrementalLaunch() {
        this.droneLauncher.setPosition(droneLauncher.getPosition() - RobotConfig.TRIGGER_INCREMENT);
    }
    public void stop() {
    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "L:%.3f, H:%.3f",
                droneLauncher.getPosition(),
                droneHolder.getPosition());
    }
}
