package org.firstinspires.ftc.teamcode.opmodes.autonomous;

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
import org.firstinspires.ftc.teamcode.robot.operations.DroneOperation;
import org.firstinspires.ftc.teamcode.robot.operations.MiniArmOperation;
import org.firstinspires.ftc.teamcode.robot.operations.State;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeLeftToAprilTagOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightForDistanceOperation;
import org.firstinspires.ftc.teamcode.robot.operations.StrafeRightToAprilTagOperation;
import org.firstinspires.ftc.teamcode.robot.operations.WaitOperation;

public abstract class NearAudienceAutonomous extends AutonomousHelper {
    boolean tryYellowPixel = true;

    public void setTryYellowPixel(boolean tryYellowPixel) {
        this.tryYellowPixel = tryYellowPixel;
    }
    @Override
    public void start() {
        super.start();

        //stop looking for prop
        robot.getVisionPortal().disableObjectDetection();

        Alliance.Color alliance = match.getAlliance();
        Field.SpikePosition spikePosition = match.getSpikePosition();
        int desiredAprilTagId = 0;
        double bearingToBackdrop, bearingToSpike = 0, bearingToDriveTeam = Math.toRadians(180);

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
            bearingToBackdrop = Math.toRadians(-90);
        }
        else {
            bearingToBackdrop = Math.toRadians(90);
        }

        State state = new State("Deliver Purple Pixel");
        //raise the arm
        state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.Raised, "Get arm to raised level"));
        state.addSecondaryOperation(new DroneOperation(DroneOperation.Type.Travel, "Get drone launcher to travel level"));
        //Move forward to the middle spike mark using the color sensor - this guarantees proper positioning of the robot
        state.addPrimaryOperation(
                new DriveToColorOperation(
                        Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                        0, RobotConfig.FIND_COLOR_SPEED, "Reach middle spike"));

        if (spikePosition == Field.SpikePosition.Left || spikePosition == Field.SpikePosition.Right) {
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -6.0*Field.MM_PER_INCH, 0, RobotConfig.CAUTIOUS_SPEED/2,
                    "Come back a bit before turning towards spike"));
            state.addPrimaryOperation(new BearingOperation(bearingToSpike, "Face " + spikePosition + " spike"));
            //drive up to spike
            state.addPrimaryOperation(
                    new DriveToColorOperation(
                            Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                            bearingToSpike, RobotConfig.FIND_COLOR_SPEED, "Reach spike on " + spikePosition));
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -2.5*Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit before dropping purple pixel as we extend beyond the spike"));
            //Drop pixel
            state.addPrimaryOperation(
                    new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel")
            );
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -4.0*Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit more to clear purple pixel"));

        }
        state.addPrimaryOperation(new BearingOperation(bearingToDriveTeam, "Face drive team"));
        //Move backwards to the middle spike mark using the color sensor - this guarantees proper distance
        state.addPrimaryOperation(
                new DriveToColorOperation(
                        Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                        bearingToDriveTeam, -RobotConfig.FIND_COLOR_SPEED, "Reach middle spike backwards"));
        if (spikePosition == Field.SpikePosition.Middle) {
            //Drop pixel
            state.addPrimaryOperation(
                    new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel")
            );
        }
        state.addPrimaryOperation(new DriveInDirectionOperation(
                -7.0*Field.MM_PER_INCH, bearingToDriveTeam, RobotConfig.CAUTIOUS_SPEED,
                "Come back a bit more to clear purple pixel on middle spike"));
        states.add(state);

        if (tryYellowPixel) {
            state = new State("Approach backdrop from backdrop side");
            //turn to face the backdrop
            state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, "Face backdrop"));
            states.add(state);

            state = new State("Lower arm to go under bridge");
            state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.ExtendedTravel, "Lower arm to go under bridge"));
            states.add(state);

            state = new State("Go under bridge");
            //go under bridge
            state.addPrimaryOperation(
                    new DriveInDirectionOperation(
                            2.8 * Field.TILE_WIDTH, bearingToBackdrop, 1,
                            "Go under stage door"));
            state.addSecondaryOperation(new WaitOperation(1000, "Wait before raising arm so we clear the stage door"));
            state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.AutoDeposit, "Raise arm so we can strafe"));

            states.add(state);
/*
            state = new State("Continue to backdrop");
            state.addPrimaryOperation(
                    new DriveInDirectionOperation(
                            1.0 * Field.TILE_WIDTH, bearingToBackdrop, 1,
                            "Go towards backdrop"));
            states.add(state);

 */

            state = new State("Strafe to see desired april tag");
            //state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.AutoDeposit, "Get into deposit mode"));
            //realign to backdrop
            state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, "Realign with backdrop"));
            if (alliance == Alliance.Color.RED) {
                state.addPrimaryOperation(
                        new StrafeRightToAprilTagOperation(
                                desiredAprilTagId,
                                "Strafe right until we see the april tag"));
            } else {
                state.addPrimaryOperation(
                        new StrafeLeftToAprilTagOperation(
                                desiredAprilTagId,
                                "Strafe left until we see the april tag"));
            }
            states.add(state);

            state = new State("Get bucket on backdrop and drop yellow pixel");
            //run into backdrop
            state.addPrimaryOperation(new DriveToAprilTag(11.5 * Field.MM_PER_INCH, desiredAprilTagId, "Drive to April Tag"));
            //shift right an inch as our pixel is carried on left side of the intake
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Shift right a bit to center pixel"));
            //push yellow pixel out
            state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Expel, "Expel pixel"));
            //move back a bit to clear backdrop
            state.addPrimaryOperation(new DriveInDirectionOperation(-8 * Field.MM_PER_INCH, bearingToBackdrop, RobotConfig.CAUTIOUS_SPEED, "Back Away"));
            states.add(state);
        }
    }

}
