package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.game.Alliance;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.operations.ArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.BearingOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveInDirectionOperation;
import org.firstinspires.ftc.teamcode.robot.operations.DriveToAprilTag;
import org.firstinspires.ftc.teamcode.robot.operations.DriveToColorOperation;
import org.firstinspires.ftc.teamcode.robot.operations.LedOperation;
import org.firstinspires.ftc.teamcode.robot.operations.MiniArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftToAprilTagOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightToAprilTagOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;

public abstract class AutonomousV2 extends AutonomousHelper {

    @Override
    public void start() {
        super.start();

        //stop looking for prop
        robot.getVisionPortal().disableObjectDetection();

        Alliance.Color alliance = match.getAlliance();
        Field.StartingPosition startingPosition = match.getStartingPosition();
        Field.SpikePosition spikePosition = match.getSpikePosition();
        int desiredAprilTagId = 0;
        double bearingToBackdrop, bearingToAudience, bearingToSpike = 0;

        switch (spikePosition) {
            case Left: {
                bearingToSpike = Math.toRadians(65.0);
                if (alliance == Alliance.Color.BLUE) {
                    desiredAprilTagId = 1;
                }
                else {
                    desiredAprilTagId = 4;
                }
                break;
            }
            case Middle: {
                bearingToSpike = Math.toRadians(0);
                if (alliance == Alliance.Color.BLUE) {
                    desiredAprilTagId = 2;
                }
                else {
                    desiredAprilTagId = 5;
                }
                break;
            }
            case Right: {
                bearingToSpike = Math.toRadians(-65.0);
                if (alliance == Alliance.Color.BLUE) {
                    desiredAprilTagId = 3;
                }
                else {
                    desiredAprilTagId = 6;
                }
                break;
            }
        }
        if (alliance == Alliance.Color.RED) {
            //Red Alliance
            bearingToBackdrop = Math.toRadians(-90);
        }
        else {
            //Blue alliance
            bearingToBackdrop = Math.toRadians(90);
        }
        bearingToAudience = -bearingToBackdrop;
        
        boolean nearAudience = (alliance == Alliance.Color.RED && startingPosition == Field.StartingPosition.Left) ||
                (alliance == Alliance.Color.BLUE && startingPosition == Field.StartingPosition.Right);
        boolean startingNearBackDrop = !nearAudience;

        State state = new State("Deliver Purple Pixel");

        //Turn on blue-violet pattern on the led to state we are trying to deposit the purple pixel
        state.addTertiaryOperation(new LedOperation(robot.getLed(), RevBlinkinLedDriver.BlinkinPattern.BLUE_VIOLET, "Purple pixel mode"));
        //raise the arm
        state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.Raised, "Get arm to raised level"));
        //Move forward to the middle spike mark
        state.addPrimaryOperation(
                new DriveToColorOperation(
                        Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                        0, RobotConfig.CAUTIOUS_SPEED/2, "Reach middle spike"));

        if (spikePosition != Field.SpikePosition.Middle) {
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -6*Field.MM_PER_INCH, 0, RobotConfig.CAUTIOUS_SPEED/2,
                    "Come back a bit before turning towards spike"));
            state.addPrimaryOperation(new BearingOperation(bearingToSpike, "Face " + spikePosition + " spike"));
            //drive up to spike
            state.addPrimaryOperation(
                    new DriveToColorOperation(
                            Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                            bearingToSpike, RobotConfig.CAUTIOUS_SPEED, "Reach spike on " + spikePosition));
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -3*Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit before dropping purple pixel"));
            //Drop pixel
            state.addPrimaryOperation(
                    new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel")
            );
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -9*Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a more to clear purple pixel"));

        }
        else {
            //middle spike position
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -3*Field.MM_PER_INCH, 0, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit before dropping purple pixel"));
            //Drop pixel
            state.addPrimaryOperation(
                    new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel")
            );
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -9*Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED/2,
                    "Come back a more to clear purple pixel"));
        }
        if (nearAudience) {
            state.addPrimaryOperation(new BearingOperation(bearingToAudience, "Face audience"));
        }
        else {
            state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, "Face backdrop"));
        }
        state.setCompletionBasedUpon(State.CompletionBasedUpon.PRIMARY_OPERATIONS);
        states.add(state);
        //we try to drop yellow pixel and navigate if we are starting close to the backdrop
        if (startingNearBackDrop) {

            //at this point we should be facing the backdrop
            state = new State("Approach backdrop");
            //Turn on yellow pattern on the led to state we are trying to deposit the yellow pixel
            state.addTertiaryOperation(
                    new LedOperation(robot.getLed(), RevBlinkinLedDriver.BlinkinPattern.YELLOW, "Yellow pixel mode"));

            //drive up to the proper April Tag and also lower arm to deposit yellow pixel
            state.addPrimaryOperation(new DriveToAprilTag(20 * Field.MM_PER_INCH, desiredAprilTagId, "Drive to April Tag"));
            state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.Eat, "Start eating pixel so it does not fall off"));
            state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.AutoDeposit, "Get arm to auto-deposit level"));
            state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Abstain, "Stop eating pixel"));

            states.add(state);

            state = new State("Drop yellow pixel");
            //run into backdrop
            state.addPrimaryOperation(new DriveToAprilTag(11.5 * Field.MM_PER_INCH, desiredAprilTagId, "Drive to April Tag"));
            //push yellow pixel out
            state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Expel, "Expel pixel"));
            state.addPrimaryOperation(new WaitOperation(3000, "wait three seconds"));
            states.add(state);


            state = new State("Navigate");
            state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.Abstain, "Stop in/out take"));

            state.addPrimaryOperation(new DriveForDistanceOperation(-4 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back Away"));
            if (alliance == Alliance.Color.RED) {
                state.addPrimaryOperation(new StrafeRightForDistanceOperation(20 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
            } else {
                state.addPrimaryOperation(new StrafeLeftForDistanceOperation(20 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
            }

            states.add(state);
        }
        else {
            //we are not starting close to the backdrop
            state = new State("Reach backdrop from audience side");
            //rotate to face the audience
            state.addPrimaryOperation(new BearingOperation(-bearingToBackdrop, "Face audience"));
            //find and align with the small april tag on the audience side
            state.addPrimaryOperation(new DriveToAprilTag(
                    20*Field.MM_PER_INCH,
                    alliance == Alliance.Color.RED ? 8 : 10,
                    "Align with april tag"));
            //realign to face audience
            state.addPrimaryOperation(new BearingOperation(bearingToAudience, "Realign: Face audience"));

            if (alliance == Alliance.Color.RED) {
                state.addPrimaryOperation(new StrafeRightForDistanceOperation(30*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Reach middle tile"));
            }
            else {
                state.addPrimaryOperation(new StrafeLeftForDistanceOperation(30*Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Reach middle tile"));
            }
            //rotate to face the backdrop
            state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, "Face backdrop"));
            states.add(state);

            state = new State("Go under bridge");
            state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Travel, "Lower arm to go under bridge"));
            state.addPrimaryOperation((new DriveInDirectionOperation(4*Field.TILE_WIDTH, bearingToBackdrop, RobotConfig.CAUTIOUS_SPEED, "Get under door")));
            states.add(state);

            state = new State("Raise to auto deposit position and move forward");
            state.addPrimaryOperation((new DriveInDirectionOperation(Field.TILE_WIDTH/2, bearingToBackdrop, RobotConfig.CAUTIOUS_SPEED, "Get near backdrop")));
            state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.AutoDeposit, "Arm to auto-deposit position"));
            states.add(state);

            state = new State("Strafe to see tags");
            if (alliance == Alliance.Color.RED) {
                state.addPrimaryOperation(
                        new StrafeRightToAprilTagOperation(
                                desiredAprilTagId,
                                "Strafe until we see the april tag"));
            }
            else {
                state.addPrimaryOperation(
                    new StrafeLeftToAprilTagOperation(
                        desiredAprilTagId,
                            "Strafe until we see the april tag"));
            }
            state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, "Realign to backdrop"));
            //run into backdrop
            state.addPrimaryOperation(new DriveToAprilTag(11.5 * Field.MM_PER_INCH, desiredAprilTagId,  "Drive to April Tag"));
            //push yellow pixel out
            state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Expel, "Expel pixel"));
            //backup a little
            state.addPrimaryOperation((new DriveInDirectionOperation(-4*Field.TILE_WIDTH, bearingToBackdrop, RobotConfig.CAUTIOUS_SPEED, "Back away from backdrop")));

            states.add(state);

        }
    }
}
