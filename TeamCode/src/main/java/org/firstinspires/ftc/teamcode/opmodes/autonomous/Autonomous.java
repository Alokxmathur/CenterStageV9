package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveToAprilTag;
import org.firstinspires.ftc.teamcode.robot.operations.LedOperation;
import org.firstinspires.ftc.teamcode.robot.operations.MiniArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;

public abstract class Autonomous extends AutonomousHelper {
    double DISTANCE_TO_MIDDLE_OF_SPIKES = 35.0 * Field.MM_PER_INCH;
    double DISTANCE_TO_PUSH_PROP = 12*Field.MM_PER_INCH;

    @Override
    public void start() {
        super.start();

        if (match.getAlliance() == Alliance.Color.NotSelected) {
            return;
        }
        Field.SpikePosition spikePosition = match.getSpikePosition();
        int desiredAprilTagId = 0;
        double bearingToBackdrop;
        double distanceToPushProp = DISTANCE_TO_PUSH_PROP;
        double bearingToPushProp = 0;
        switch (spikePosition) {
            case NotSeen:
            case Left: {
                bearingToPushProp = Math.toRadians(45);
                if (match.getAlliance() == Alliance.Color.BLUE) {
                    desiredAprilTagId = 1;
                }
                else {
                    desiredAprilTagId = 4;
                }
                break;
            }
            case Middle: {
                bearingToPushProp = Math.toRadians(0);
                if (match.getAlliance() == Alliance.Color.BLUE) {
                    desiredAprilTagId = 2;
                }
                else {
                    desiredAprilTagId = 5;
                }
                distanceToPushProp -= 3*Field.MM_PER_INCH;
                break;
            }
            case Right: {
                bearingToPushProp = Math.toRadians(-45);
                if (match.getAlliance() == Alliance.Color.BLUE) {
                    desiredAprilTagId = 3;
                }
                else {
                    desiredAprilTagId = 6;
                }
                break;
            }
        }
        if (match.getAlliance() == Alliance.Color.RED) {
            //Red Alliance
            bearingToBackdrop = Math.toRadians(-90);
        }
        else {
            //Blue alliance
            bearingToBackdrop = Math.toRadians(90);
         }

        State state = new State("Deliver Purple Pixel");

        //Turn on blue-violet pattern on the led to state we are trying to deposit the purple pixel
        state.addSecondaryOperation(new LedOperation(robot.getLed(), RevBlinkinLedDriver.BlinkinPattern.BLUE_VIOLET, "Purple pixel mode"));
        //Move forward a little to clear the wall so we can rotate
        state.addPrimaryOperation(
                new DriveForDistanceOperation(DISTANCE_TO_MIDDLE_OF_SPIKES, RobotConfig.CAUTIOUS_SPEED, "Leave wall"));
        //Turn so we can push the prop with the front of the robot
        state.addPrimaryOperation(new BearingOperation(bearingToPushProp, robot.getDriveTrain(), "Point front to prop"));
        //drive forward into the prop to push it away
        state.addPrimaryOperation(
                new DriveForDistanceOperation(distanceToPushProp, RobotConfig.CAUTIOUS_SPEED, "Bump prop"));
        //drive backwards to get away from prop
        state.addPrimaryOperation(
                new DriveForDistanceOperation(-distanceToPushProp, RobotConfig.CAUTIOUS_SPEED, "Away from prop"));
        //special case where dropped pixel might be in the way to the backdrop
        if ((spikePosition == Field.SpikePosition.Right && match.getAlliance() == Alliance.Color.RED)
            || (spikePosition == Field.SpikePosition.Left && match.getAlliance() == Alliance.Color.BLUE)) {
            //back up some more so we don't run over purple pixel
            state.addPrimaryOperation(
                    new DriveForDistanceOperation(-5*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Backup more so we don't run over purple pixel"));
        }
        //rotate to face the backdrop
        state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, robot.getDriveTrain(), "Face backdrop"));
        states.add(state);

        //at this point we should be facing the backdrop
        state = new State("Approach backdrop");
        //Turn on yellow pattern on the led to state we are trying to deposit the yellow pixel
        state.addTertiaryOperation(
                new LedOperation(robot.getLed(), RevBlinkinLedDriver.BlinkinPattern.YELLOW, "Yellow pixel mode"));

        //drive up to the proper April Tag and also lower arm to deposit yellow pixel
        state.addPrimaryOperation(new DriveToAprilTag(20*Field.MM_PER_INCH, desiredAprilTagId, RobotConfig.CAUTIOUS_SPEED/2, "Drive to April Tag"));
        //state.addPrimaryOperation(new StrafeRightForDistanceOperation(3*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Shift to line up pixel"));
        state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.AutoDeposit, "Get arm to auto-deposit level"));
        states.add(state);

        state = new State("Drop yellow pixel");
        //run into backdrop
        state.addPrimaryOperation(new DriveToAprilTag(12*Field.MM_PER_INCH, desiredAprilTagId, RobotConfig.CAUTIOUS_SPEED/2, "Drive to April Tag"));
        //state.addPrimaryOperation(new DriveForDistanceOperation(13*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Reach backdrop"));
        //push yellow pixel out
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Expel, "Expel pixel"));
        //state.addPrimaryOperation(new WaitOperation(3000, "wait three seconds"));
        states.add(state);


        state = new State("Navigate");
        state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.Abstain, "Stop in/out take"));

        state.addPrimaryOperation(new DriveForDistanceOperation(-10*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back Away"));
        if (match.getAlliance() == Alliance.Color.RED) {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }
        else {
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }

        states.add(state);

        Match.log("Created and added state");
    }
}
