package org.firstinspires.ftc.teamcode.robot.components;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import java.util.Locale;

public class DroneLauncher {
    Servo droneHolder, droneLoader;

    public DroneLauncher(HardwareMap hardwareMap) {
        this.droneHolder = hardwareMap.get(Servo.class, RobotConfig.DRONE_HOLDER);
        this.droneLoader = hardwareMap.get(Servo.class, RobotConfig.DRONE_LOADER);

        assumeInitialPosition();
    }

    public void assumeInitialPosition() {
        this.droneHolder.setPosition(RobotConfig.DRONE_HOLDER_HOLD_POSITION);
        this.droneLoader.setPosition(RobotConfig.DRONE_LOADER_INITIAL_POSITION);
    }

    public void releaseDrone()  {
        this.droneHolder.setPosition(RobotConfig.DRONE_TRIGGER_RELEASE_POSITION);
    }
    public void holdDrone() {
        this.droneHolder.setPosition(RobotConfig.DRONE_HOLDER_HOLD_POSITION);
    }

    public void incrementalLoad() {
        this.droneLoader.setPosition(droneLoader.getPosition() + RobotConfig.TRIGGER_INCREMENT);
    }

    public void decrementalLoad() {
        this.droneLoader.setPosition(droneLoader.getPosition() - RobotConfig.TRIGGER_INCREMENT);
    }

    public void incrementalHold() {
        this.droneHolder.setPosition(droneHolder.getPosition() + RobotConfig.TRIGGER_INCREMENT);
    }

    public void decrementalHold() {
        this.droneHolder.setPosition(droneHolder.getPosition() - RobotConfig.TRIGGER_INCREMENT);
    }
    public void stop() {
    }

    public String getStatus() {
        return String.format(Locale.getDefault(), "Holder:%.3f, Loader:%.3f",
                droneHolder.getPosition(),
                droneLoader.getPosition());
    }

    public void goToShootingPosition() {
        this.droneLoader.setPosition(RobotConfig.DRONE_LOADER_SHOOTING_POSITION);
    }
    public void goToTravelPosition() {
        this.droneLoader.setPosition(RobotConfig.DRONE_LOADER_TRAVEL_POSITION);
    }
}
