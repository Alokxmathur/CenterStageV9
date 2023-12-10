package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
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
    double DISTANCE_TO_BACK_INTO_PROP = 16*Field.MM_PER_INCH;

    @Override
    public void start() {
        if (match.getAlliance() == Alliance.Color.NotSelected) {
            return;
        }
        Field.SpikePosition spikePosition = match.getSpikePosition();

        double bearingToBackdrop;
        double bearingToDepositPurple = 0;
        switch (spikePosition) {
            case NotSeen:
            case Middle: {
                bearingToDepositPurple = Math.toRadians(180);
                break;
            }
            case Left: {
                bearingToDepositPurple = Math.toRadians(-135);
                break;
            }
            case Right: {
                bearingToDepositPurple = Math.toRadians(-225);
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

        super.start();
        State state = new State("Deliver Purple Pixel");

        state.addSecondaryOperation(new LedOperation(robot.getLed(), RevBlinkinLedDriver.BlinkinPattern.BLUE_VIOLET, "Purple pixel mode"));
        state.addPrimaryOperation(new DriveForDistanceOperation(DISTANCE_TO_MIDDLE_OF_SPIKES, RobotConfig.CAUTIOUS_SPEED, "Leave wall"));
        state.addPrimaryOperation(new BearingOperation(bearingToDepositPurple, robot.getDriveTrain(), "Show rear to prop"));
        state.addPrimaryOperation(new DriveForDistanceOperation(-DISTANCE_TO_BACK_INTO_PROP, RobotConfig.CAUTIOUS_SPEED, "Bump prop"));
        state.addPrimaryOperation(new DriveForDistanceOperation(DISTANCE_TO_BACK_INTO_PROP*3/4, RobotConfig.CAUTIOUS_SPEED, "Move back to spike"));
        state.addPrimaryOperation(new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel"));
        state.addPrimaryOperation(new WaitOperation(500, "Wait half a sec"));
        state.addPrimaryOperation(new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Up, "Lift dropper up"));
        state.addPrimaryOperation(new DriveForDistanceOperation(DISTANCE_TO_BACK_INTO_PROP*1/4, RobotConfig.CAUTIOUS_SPEED, "Move away from pixel"));
        state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, robot.getDriveTrain(), "face backdrop"));

        states.add(state);
        //at this point we should be facing the backdrop

        state = new State("Approach backdrop");

        state.addSecondaryOperation(new LedOperation(robot.getLed(), RevBlinkinLedDriver.BlinkinPattern.YELLOW, "Yellow pixel mode"));
        //state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Deposit1, "Deposit position 1"));
        //move forward towards middle spike
        //state.addPrimaryOperation(new DriveForDistanceOperation(DISTANCE_TO_TRAVEL_TO_BACKDROP, RobotConfig.CAUTIOUS_SPEED, "Approach backdrop"));
        states.add(state);

        state = new State("Deliver yellow pixel");
        int desiredAprilTagId = 0;
        //Spike Mark 1
        if (spikePosition == Field.SpikePosition.Left) {
            if (match.getAlliance() == Alliance.Color.BLUE) {
                desiredAprilTagId = 1;
            }
            else {
                desiredAprilTagId = 4;
            }
        }
        //Spike Mark 2
        else if (spikePosition == Field.SpikePosition.Middle) {
            if (match.getAlliance() == Alliance.Color.BLUE) {
                desiredAprilTagId = 2;
            }
            else {
                desiredAprilTagId = 5;
            }
        }
        //Spike Mark 3
        else {
            if (match.getAlliance() == Alliance.Color.BLUE) {
                desiredAprilTagId = 3;
            }
            else {
                desiredAprilTagId = 6;
            }
        }
        state.addPrimaryOperation(new DriveToAprilTag(10*Field.MM_PER_INCH, desiredAprilTagId, RobotConfig.CAUTIOUS_SPEED, "Align with apriltag"));
        states.add(state);

        state = new State("Drop yellow pixel");
        state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, robot.getDriveTrain(), "Face backdrop"));
        state.addPrimaryOperation(new DriveForDistanceOperation(5*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Reach backdrop"));
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Deposit1, "Ready to drop"));
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.ThrowUp, "Expel pixel"));
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Deposit2, "Go higher to clear pixel"));
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Abstain, "Stop in/out take"));

        states.add(state);


        state = new State("Navigate");
        state.addPrimaryOperation(new DriveForDistanceOperation(-10*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back Away"));
        if (match.getAlliance() == Alliance.Color.RED) {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }
        else {
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(36*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }
        //state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Travel, "Move arm to travel position"));

        states.add(state);

        Match.log("Created and added state");
    }
}
