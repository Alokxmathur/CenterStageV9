package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightForDistanceOperation;

public abstract class Autonomous extends AutonomousHelper {

    double DISTANCE_TO_TRAVEL_TO_MIDDLE_SPIKE = 42.0 * Field.MM_PER_INCH;
    double DISTANCE_TO_PUSH_PIXEL = 5 * Field.MM_PER_INCH;

    @Override
    public void start() {
        double bearingToBackdrop = 0;
        if (match.getAlliance() == Alliance.Color.RED) {
            bearingToBackdrop = Math.toRadians(-90);
        }
        else {
            bearingToBackdrop = Math.toRadians(90);
        }

        super.start();
        State state = new State("Deliver Purple Pixel");
        Field.SpikePosition spikePosition = match.getSpikePosition();

        double distanceForward = DISTANCE_TO_TRAVEL_TO_MIDDLE_SPIKE;
        if (spikePosition == Field.SpikePosition.Middle) {
            distanceForward+= DISTANCE_TO_PUSH_PIXEL;
        }
        //move forward towards middle spike
        state.addPrimaryOperation(new DriveForDistanceOperation(distanceForward - RobotConfig.ROBOT_LENGTH, RobotConfig.CAUTIOUS_SPEED, "Deliver to Spike Mark 2"));

        //Spike Mark 1
        if (spikePosition == Field.SpikePosition.Left) {
            //turn to left spike
            state.addPrimaryOperation(new BearingOperation(-bearingToBackdrop/2, robot.getDriveTrain(), "Turn Toward Left spike"));
            //push pixel
            state.addPrimaryOperation(new DriveForDistanceOperation(DISTANCE_TO_PUSH_PIXEL, RobotConfig.CAUTIOUS_SPEED, "Push pixel"));
        }
        else if (spikePosition == Field.SpikePosition.Right){
            //Spike Mark 3
            state.addPrimaryOperation(new BearingOperation(bearingToBackdrop/2, robot.getDriveTrain(),"Turn Toward Right spike"));
            //push pixel
            state.addPrimaryOperation(new DriveForDistanceOperation(DISTANCE_TO_PUSH_PIXEL, RobotConfig.CAUTIOUS_SPEED, "Push pixel"));
        }
        //backup from pixel
        state.addPrimaryOperation(new DriveForDistanceOperation(-DISTANCE_TO_PUSH_PIXEL, RobotConfig.CAUTIOUS_SPEED, "Backup to leave pixel"));
        state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, robot.getDriveTrain(), "Face backdrop"));
        states.add(state);

        //position should be x = 36, y = 36

        state = new State("Approach Backdrop");
        state.addPrimaryOperation(new DriveForDistanceOperation(1.7*Field.TILE_WIDTH, RobotConfig.CAUTIOUS_SPEED, "Approach backdrop"));
        //Spike Mark 1
        if (spikePosition == Field.SpikePosition.Left) {
            if (match.getAlliance() == Alliance.Color.RED) {
                state.addPrimaryOperation(new StrafeLeftForDistanceOperation(12 * Field.MM_PER_INCH, 20, "Slide Left"));
            }
            //state.addPrimaryOperation(new DriveForDistanceOperation(15*Field.MM_PER_INCH, 10, "Approach Claw to BackDrop"));
        }
        //Spike Mark 2
        else if (spikePosition == Field.SpikePosition.Middle) {
            if (match.getAlliance() == Alliance.Color.RED) {
                state.addPrimaryOperation(new StrafeLeftForDistanceOperation(8 * Field.MM_PER_INCH, 20, "Slide Left"));
            }
            else {
                state.addPrimaryOperation(new StrafeRightForDistanceOperation(8 * Field.MM_PER_INCH, 20, "Slide Left"));
            }
        }
        //Spike Mark 3
        else {
            if (match.getAlliance() == Alliance.Color.BLUE) {
                state.addPrimaryOperation(new StrafeRightForDistanceOperation(8 * Field.MM_PER_INCH, 20, "Slide Left"));
            }
        }
        states.add(state);

        state = new State("Drop yellow pixel");
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Deposit, "Move arm to deposit position"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Drop Yellow Pixel"));
        states.add(state);


        state = new State("Navigate to Scoring Area");
        state.addPrimaryOperation(new DriveForDistanceOperation(-10*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back Away"));
        if (match.getAlliance() == Alliance.Color.RED) {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }
        else {
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Travel, "Move arm to travel position"));

        states.add(state);

        Match.log("Created and added state");
    }
}
