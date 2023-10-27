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
    public static final double M_PER_INCH = MM_PER_INCH / 1000;
    //the width of each tile
    public static final double TILE_WIDTH = 24 * MM_PER_INCH;
    // the width of the FTC field (from the center point to the outer panels)
    public static final double FIELD_WIDTH = 6 * TILE_WIDTH;

    public static volatile boolean initialized = false;
    public static final Object mutex = new Object();

    public static final double sqrtOfTwo = Math.sqrt(2);

    public enum StartingPosition {
        Left, Right
    }
    public enum SpikePosition {
        Left, Middle, Right, NotSeen
    }

    public static final Pose2d redLeftStartingPose = new Pose2d(
            (-TILE_WIDTH - RobotConfig.ROBOT_WIDTH / 2 - 0.25 * Field.MM_PER_INCH) / MM_PER_INCH,
            (-FIELD_WIDTH / 2 + RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH,
            Math.toRadians(-90));
    public static final Pose2d redLeftTurnaroundPose = new Pose2d(
            (-TILE_WIDTH / 2) / MM_PER_INCH,
            -(2.5 * TILE_WIDTH),
            Math.toRadians(-90));
    public static final Pose2d redLeftDeliverLoadedConePose = new Pose2d(
            (-RobotConfig.ROBOT_CENTER_FROM_BACK / sqrtOfTwo) / MM_PER_INCH,
            -(TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(225));
    public static final Pose2d redLeftPickupConeIntermediatePose = new Pose2d(
            (-TILE_WIDTH / 2) / MM_PER_INCH,
            -(TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d redLeftPickupConePose = new Pose2d(
            -(FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE - 120) / MM_PER_INCH,
            -(TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d redLeftDeliverSecondConePose = new Pose2d(
            -(RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            -(TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));

    public static final Pose2d redRightStartingPose =
            new Pose2d(
                    1.5*TILE_WIDTH / MM_PER_INCH,
                    (-FIELD_WIDTH / 2 + RobotConfig.ROBOT_CENTER_FROM_BACK) / MM_PER_INCH,
                    Math.toRadians(90));
    public static final Pose2d redRightTurnaroundPose = new Pose2d(
            (TILE_WIDTH / 2) / MM_PER_INCH,
            -(2.5 * TILE_WIDTH) / MM_PER_INCH,
            Math.toRadians(-90));
    public static final Pose2d redRightDeliverLoadedConePose = new Pose2d(
            (RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            -(TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(-45));
    public static final Pose2d redRightPickupConeIntermediatePose = new Pose2d(
            (TILE_WIDTH) / MM_PER_INCH,
            -(0.8 * TILE_WIDTH) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d redRightPickupConePose = new Pose2d(
            (FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE - 150) / MM_PER_INCH,
            -(TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d redRightDeliverSecondConePose = new Pose2d(
            (TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            -(RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(225));

    public static final Pose2d blueLeftStartingPose =
            new Pose2d(
                    (TILE_WIDTH + RobotConfig.ROBOT_WIDTH / 2 + 0.25 * Field.MM_PER_INCH) / MM_PER_INCH,
                    (FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH,
                    Math.toRadians(90));
    public static final Pose2d blueLeftTurnaroundPose = new Pose2d(
            (TILE_WIDTH / 2) / MM_PER_INCH,
            (2.5 * TILE_WIDTH) / MM_PER_INCH,
            Math.toRadians(90));
    public static final Pose2d blueLeftDeliverLoadedConePose = new Pose2d(
            (RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            (TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));
    public static final Pose2d blueLeftPickupConeIntermediatePose = new Pose2d(
            (TILE_WIDTH / 2) / MM_PER_INCH,
            (TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d blueLeftPickupConePose = new Pose2d(
            (FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE - 120) / MM_PER_INCH,
            (TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(180));
    public static final Pose2d blueLeftDeliverSecondConePose = new Pose2d(
            (TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            (RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(135));

    public static final Pose2d blueRightStartingPose =
            new Pose2d(
                    (-TILE_WIDTH - RobotConfig.ROBOT_WIDTH / 2 - 0.25 * Field.MM_PER_INCH) / MM_PER_INCH,
                    (FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_FRONT) / MM_PER_INCH,
                    Math.toRadians(90));
    public static final Pose2d blueRightTurnaroundPose = new Pose2d(
            -(TILE_WIDTH / 2) / MM_PER_INCH,
            (2.5 * TILE_WIDTH) / MM_PER_INCH,
            Math.toRadians(90));
    public static final Pose2d blueRightDeliverLoadedConePose = new Pose2d(
            -(RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            (TILE_WIDTH + RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(135));
    public static final Pose2d blueRightPickupConeIntermediatePose = new Pose2d(
            (-TILE_WIDTH / 2) / MM_PER_INCH,
            (TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d blueRightPickupConePose = new Pose2d(
            -(FIELD_WIDTH / 2 - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE - 120) / MM_PER_INCH,
            (TILE_WIDTH / 2) / MM_PER_INCH,
            Math.toRadians(0));
    public static final Pose2d blueRightDeliverSecondConePose = new Pose2d(
            -(TILE_WIDTH - RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            (RobotConfig.ROBOT_CENTER_FROM_HELD_CONE / sqrtOfTwo) / MM_PER_INCH,
            Math.toRadians(45));

    Trajectory turnaroundTrajectory;

    public Trajectory getTurnaroundTrajectory() {
        return turnaroundTrajectory;
    }

    Trajectory deliverLoadedConeTrajectory;

    public Trajectory getDeliverLoadedConeTrajectory() {
        return deliverLoadedConeTrajectory;
    }

    Trajectory retractFromLoadedConeDeliveryTrajectory;

    public Trajectory getRetractFromLoadedConeDeliveryTrajectory() {
        return retractFromLoadedConeDeliveryTrajectory;
    }

    Trajectory pickupConeTrajectory;

    public Trajectory getPickupConeTrajectory() {
        return pickupConeTrajectory;
    }

    Trajectory retractFromStackTrajectory;

    public Trajectory getRetractFromStackTrajectory() {
        return retractFromStackTrajectory;
    }

    Trajectory deliverSecondConeTrajectory;

    public Trajectory getDeliverSecondConeTrajectory() {
        return deliverSecondConeTrajectory;
    }

    Trajectory retractFromSecondConeDeliveryTrajectory;

    public Trajectory getRetractFromSecondConeDeliveryTrajectory() {
        return retractFromSecondConeDeliveryTrajectory;
    }

    Trajectory[] navigationTrajectories = new Trajectory[3];

    public Trajectory getNavigationTrajectory(int signalNumber) {
        return navigationTrajectories[signalNumber - 1];
    }

    private Pose2d startingPose;

    public void init(Alliance.Color alliance, StartingPosition startingPosition) {
        switch (alliance) {
            case RED: {
                switch (startingPosition) {
                    case Left: {
                        startingPose = redLeftStartingPose;
                        break;
                    }
                    case Right: {
                        startingPose = redRightStartingPose;
                        break;
                    }
                }
                break;
            }
            case BLUE: {
                switch (startingPosition) {
                    case Left: {
                        startingPose = blueLeftStartingPose;
                        break;
                    }
                    case Right: {
                        startingPose = blueRightStartingPose;
                        break;
                    }
                }
                break;
            }
        }
        Thread initThread = new Thread(() -> {
            Match.log("Field initialization started for "
                    + alliance + ", " + startingPosition.toString());
            createTrajectories(alliance, startingPosition);

            synchronized (mutex) {
                initialized = true;
            }
        });
        initThread.start();
    }

    private void createTrajectories(Alliance.Color alliance, StartingPosition startingPosition) {
        Pose2d turnaroundPose = null;
        Pose2d deliverLoadedConePose = null;
        Pose2d pickConeIntermediatePose = null;
        Pose2d pickupConePose = null;
        Pose2d deliverSecondConePose = null;
        switch (alliance) {
            case RED: {
                switch (startingPosition) {
                    case Right: {
                        turnaroundPose = redRightTurnaroundPose;
                        deliverLoadedConePose = redRightDeliverLoadedConePose;
                        pickConeIntermediatePose = redRightPickupConeIntermediatePose;
                        pickupConePose = redRightPickupConePose;
                        deliverSecondConePose = redRightDeliverSecondConePose;
                        break;
                    }
                    case Left: {
                        turnaroundPose = redLeftTurnaroundPose;
                        deliverLoadedConePose = redLeftDeliverLoadedConePose;
                        pickConeIntermediatePose = redLeftPickupConeIntermediatePose;
                        pickupConePose = redLeftPickupConePose;
                        deliverSecondConePose = redLeftDeliverSecondConePose;
                        break;
                    }
                }
                break;
            }
            case BLUE: {
                switch (startingPosition) {
                    case Right: {
                        turnaroundPose = blueRightTurnaroundPose;
                        deliverLoadedConePose = blueRightDeliverLoadedConePose;
                        pickConeIntermediatePose = blueRightPickupConeIntermediatePose;
                        pickupConePose = blueRightPickupConePose;
                        deliverSecondConePose = blueRightDeliverSecondConePose;
                        break;
                    }
                    case Left: {
                        turnaroundPose = blueLeftTurnaroundPose;
                        deliverLoadedConePose = blueLeftDeliverLoadedConePose;
                        pickConeIntermediatePose = blueLeftPickupConeIntermediatePose;
                        pickupConePose = blueLeftPickupConePose;
                        deliverSecondConePose = blueLeftDeliverSecondConePose;
                        break;
                    }
                }
                break;
            }
        } // end switch (alliance)
        turnaroundTrajectory =
                accurateTrajectoryBuilder(startingPose, true)
                        .splineToConstantHeading(turnaroundPose.vec(), turnaroundPose.getHeading() + Math.toRadians(180.0))
                        .build();
        deliverLoadedConeTrajectory =
                accurateTrajectoryBuilder(turnaroundPose, true)
                        .splineTo(deliverLoadedConePose.vec(), deliverLoadedConePose.getHeading()
                                + Math.toRadians(180))
                        .build();
        retractFromLoadedConeDeliveryTrajectory =
                accurateTrajectoryBuilder(deliverLoadedConeTrajectory.end(), deliverLoadedConeTrajectory.end().getHeading())
                        .forward(12)
                        .build();
        pickupConeTrajectory = accurateTrajectoryBuilder(retractFromLoadedConeDeliveryTrajectory.end(), true)
                .splineTo(pickConeIntermediatePose.vec(), pickConeIntermediatePose.getHeading() + Math.toRadians(180))
                .splineTo(pickupConePose.vec(), pickupConePose.getHeading() + Math.toRadians(180))
                .build();
        retractFromStackTrajectory = accurateTrajectoryBuilder(pickupConeTrajectory.end(), true)
                .forward(55)
                .build();
        deliverSecondConeTrajectory = accurateTrajectoryBuilder(retractFromStackTrajectory.end(), true)
                .splineTo(deliverSecondConePose.vec(), deliverSecondConePose.getHeading() + Math.toRadians(180))
                .build();
        retractFromSecondConeDeliveryTrajectory = accurateTrajectoryBuilder(deliverSecondConeTrajectory.end(), deliverSecondConeTrajectory.end().getHeading())
                .splineTo(retractFromStackTrajectory.end().vec(), retractFromStackTrajectory.end().getHeading())
                .build();
        for (int i = 0; i < 3; i++) {
            switch (alliance) {
                case BLUE: {
                    switch (startingPosition) {
                        case Left: {
                            navigationTrajectories[i] =
                                    accurateTrajectoryBuilder(retractFromSecondConeDeliveryTrajectory.end(), true)
                                            .forward(-((2-i) * TILE_WIDTH + 10 * MM_PER_INCH) / MM_PER_INCH)
                                            .build();
                            break;
                        }
                        case Right: {
                            navigationTrajectories[i] =
                                    accurateTrajectoryBuilder(retractFromSecondConeDeliveryTrajectory.end(), true)
                                            .forward(-((i) * TILE_WIDTH + 10 * MM_PER_INCH) / MM_PER_INCH)
                                            .build();
                            break;
                        }
                    }
                }
                case RED: {
                    switch (startingPosition) {
                        case Left: {
                            accurateTrajectoryBuilder(retractFromSecondConeDeliveryTrajectory.end(), true)
                                    .forward(-((2-i) * TILE_WIDTH + 10 * MM_PER_INCH) / MM_PER_INCH)
                                    .build();
                            break;
                        }
                        case Right: {
                            navigationTrajectories[i] =
                                    accurateTrajectoryBuilder(retractFromSecondConeDeliveryTrajectory.end(), true)
                                            .forward(-((i) * TILE_WIDTH + 10 * MM_PER_INCH) / MM_PER_INCH)
                                            .build();
                            break;
                        }
                    }
                }
            }
        }
    }

    public static boolean isNotInitialized() {
        synchronized (mutex) {
            return !initialized;
        }
    }

    public Pose2d getStartingPose() {
        return startingPose;
    }
}