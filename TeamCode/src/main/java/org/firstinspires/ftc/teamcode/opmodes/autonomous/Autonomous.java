package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.Arm;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.FollowTrajectory;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftForDistanceWithHeadingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.TurnAntiClockwiseOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetector;
import org.firstinspires.ftc.teamcode.robot.components.vision.detector.ObjectDetectorWebcam;

public abstract class Autonomous extends AutonomousHelper {

    @Override
    public void start() {
        super.start();
        State state = new State("Deliver Purple Pixel");
        //state.addSecondaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Travel, "Raise Arm to travel"));
        Field.SpikePosition spikePosition = match.getSpikePosition();
        //Spike Mark 1
        if (spikePosition == Field.SpikePosition.Left) {
            state.addPrimaryOperation(new DriveForDistanceOperation(44*Field.MM_PER_INCH - RobotConfig.ROBOT_LENGTH, RobotConfig.CAUTIOUS_SPEED, "Deliver to Spike Mark 1"));
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(12*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Line up with Spike Mark 1"));
            state.addPrimaryOperation(new DriveForDistanceOperation(-8*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back away"));
            state.addPrimaryOperation(new BearingOperation(Math.toRadians(-90), robot.getDriveTrain(), "Turn Toward Backdrop"));
            state.addPrimaryOperation(new DriveForDistanceOperation(12*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Go to Interim Position"));
        }
        //Spike Mark 2
        else if (spikePosition == Field.SpikePosition.Middle) {
            state.addPrimaryOperation(new DriveForDistanceOperation(47*Field.MM_PER_INCH - RobotConfig.ROBOT_LENGTH, RobotConfig.CAUTIOUS_SPEED, "Deliver to Spike Mark 2"));
            state.addPrimaryOperation(new DriveForDistanceOperation(-12*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back away"));
            state.addPrimaryOperation(new BearingOperation(Math.toRadians(-90), robot.getDriveTrain(),"Turn Toward Backdrop"));
            state.addPrimaryOperation(new DriveForDistanceOperation(24*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Go to Interim Position"));
        }
        else {
            //Spike Mark 3
            state.addPrimaryOperation(new DriveForDistanceOperation(44*Field.MM_PER_INCH - RobotConfig.ROBOT_LENGTH, RobotConfig.CAUTIOUS_SPEED, "Deliver to Spike Mark 3"));
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(12*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Line up with Spike Mark 3"));
            state.addPrimaryOperation(new DriveForDistanceOperation(-8*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back away"));
            state.addPrimaryOperation(new BearingOperation(Math.toRadians(-90), robot.getDriveTrain(),"Turn Toward Backdrop"));
            state.addPrimaryOperation(new DriveForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Go to Interim Position"));
        }
        states.add(state);

        //position should be x = 36, y = 36

        state = new State("Approach Backdrop");
        //Spike Mark 1
        if (spikePosition == Field.SpikePosition.Left) {
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(8, 10, "Slide Left"));
            state.addPrimaryOperation(new DriveForDistanceOperation(15*Field.MM_PER_INCH, 10, "Approach Claw to BackDrop"));
        }
        //Spike Mark 2
        else if (spikePosition == Field.SpikePosition.Middle) {
            state.addPrimaryOperation(new DriveForDistanceOperation(15*Field.MM_PER_INCH, 10, "Approach Claw to BackDrop"));
        }
        //Spike Mark 3
        else {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(8*Field.MM_PER_INCH, 10, "Slide Right"));
            state.addPrimaryOperation(new DriveForDistanceOperation(15*Field.MM_PER_INCH, 10, "Approach Claw to BackDrop"));
        }
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Open, "Drop Yellow Pixel"));
        //states.add(state);

        state = new State("Navigate to Scoring Area");
        state.addPrimaryOperation(new DriveForDistanceOperation(-3*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back Away"));
        state.addPrimaryOperation(new ArmOperation(robot.getArm(), ArmOperation.Type.Pickup, "Lower Arm"));

        //Spike Mark 1
        /*if () {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(32, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }*/
        //Spike Mark 2
        //else if () {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(24, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        //}
        //Spike Mark 3
        /*else if () {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(16, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }*/
        //states.add(state);

        /*state = new State("Grab second cone");
        //state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Close, "Close Claw"));
        //state.addSecondaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.High, "Level High"));
        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromStackTrajectory(),
                "Retract from stack"
        ));

        state.addPrimaryOperation(new FollowTrajectory(
                field.getDeliverSecondConeTrajectory(),
                "Deliver second cone"
        ));
        //state.addPrimaryOperation(new ClawOperation(robot.getClaw(), ClawOperation.Type.Open, "Open Claw"));

        states.add(state);

        state = new State("Navigate");


        state.addPrimaryOperation(new FollowTrajectory(
                field.getRetractFromSecondConeDeliveryTrajectory(),
                "Retract from second cone"
        ));

        state.addPrimaryOperation(new FollowTrajectory(
                field.getNavigationTrajectory(match.getSignalNumber()),
                "Reach right tile to navigate"
        ));
        //state.addPrimaryOperation(new WinchOperation(robot.getWinch(), robot.getFourBar(), WinchOperation.Type.Ground, "Lower"));
        states.add(state);
*/
        Match.log("Created and added state");
    }
}
