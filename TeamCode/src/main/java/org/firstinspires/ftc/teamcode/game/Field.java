package org.firstinspires.ftc.teamcode.game;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.robot.RobotConfig;

import static org.firstinspires.ftc.teamcode.roadrunner.drive.SilverTitansMecanumDrive.accurateTrajectoryBuilder;

/**
 * Created by Silver Titans on 9/16/17.
 */
public class Field {
    public static final float MM_PER_INCH = 25.4f;

    public static volatile boolean initialized = true;
    public static final Object mutex = new Object();

    public enum StartingPosition {
        Left, Right
    }
    public enum SpikePosition {
        Left, Middle, Right, NotSeen
    }
    public void init(Alliance.Color alliance, StartingPosition startingPosition) {
    }

    public static boolean isNotInitialized() {
        synchronized (mutex) {
            return !initialized;
        }
    }
}