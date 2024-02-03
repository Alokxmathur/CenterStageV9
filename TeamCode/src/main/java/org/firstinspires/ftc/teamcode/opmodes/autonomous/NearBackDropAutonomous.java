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

public abstract class NearBackDropAutonomous extends AutonomousHelper {
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
                } else {
                    desiredAprilTagId = 4;
                }
                break;
            }
            case Middle: {
                bearingToSpike = Math.toRadians(0);
                if (alliance == Alliance.Color.BLUE) {
                    desiredAprilTagId = 2;
                } else {
                    desiredAprilTagId = 5;
                }
                break;
            }
            case Right: {
                bearingToSpike = Math.toRadians(-65.0);
                if (alliance == Alliance.Color.BLUE) {
                    desiredAprilTagId = 3;
                } else {
                    desiredAprilTagId = 6;
                }
                break;
            }
        }
        if (alliance == Alliance.Color.RED) {
            bearingToBackdrop = Math.toRadians(-90);
        } else {
            bearingToBackdrop = Math.toRadians(90);
        }
        bearingToAudience = -bearingToBackdrop;

        boolean startedNearAudience = (alliance == Alliance.Color.RED && startingPosition == Field.StartingPosition.Left) ||
                (alliance == Alliance.Color.BLUE && startingPosition == Field.StartingPosition.Right);
        boolean startedNearBackDrop = !startedNearAudience;

        State state = new State("Deliver Purple Pixel");
        //raise the arm
        state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.Raised, "Get arm to raised level"));
        state.addSecondaryOperation(new DroneOperation(DroneOperation.Type.Travel, "Get drone launcher to travel level"));
        //Move forward to the middle spike mark using the color sensor - this guarantees proper distance
        state.addPrimaryOperation(
                new DriveToColorOperation(
                        Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                        0, RobotConfig.FIND_COLOR_SPEED, "Reach middle spike"));

        if (spikePosition != Field.SpikePosition.Middle) {
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -6.0 * Field.MM_PER_INCH, 0, RobotConfig.CAUTIOUS_SPEED / 2,
                    "Come back a bit before turning towards spike"));
            state.addPrimaryOperation(new BearingOperation(bearingToSpike, "Face " + spikePosition + " spike"));
            //drive up to spike
            state.addPrimaryOperation(
                    new DriveToColorOperation(
                            Match.getInstance().getAlliance() == Alliance.Color.RED ? DriveToColorOperation.Type.RED : DriveToColorOperation.Type.BLUE,
                            bearingToSpike, RobotConfig.FIND_COLOR_SPEED, "Reach spike on " + spikePosition));
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -3.0 * Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit before dropping purple pixel as we extend beyond the spike"));
            //Drop pixel
            state.addPrimaryOperation(
                    new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel")
            );
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -5.0 * Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit more to clear purple pixel"));

        } else {
            //middle spike position
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -3.0 * Field.MM_PER_INCH, 0, RobotConfig.CAUTIOUS_SPEED,
                    "Come back a bit before dropping purple pixel as we extend beyond the spike"));
            //Drop pixel
            state.addPrimaryOperation(
                    new MiniArmOperation(robot.getMiniArm(), MiniArmOperation.Type.Drop, "Drop purple pixel")
            );
            state.addPrimaryOperation(new DriveInDirectionOperation(
                    -3.0 * Field.MM_PER_INCH, bearingToSpike, RobotConfig.CAUTIOUS_SPEED / 2,
                    "Come back a bit more to clear purple pixel"));
        }
        states.add(state);

        state = new State("Approach backdrop from backdrop side");
        //face the backdrop
        state.addPrimaryOperation(new BearingOperation(bearingToBackdrop, "Face backdrop"));
        //shift a bit to avoid running over the dropped purple pixel on the spike close to the backdrop
        if (alliance == Alliance.Color.RED && spikePosition == Field.SpikePosition.Right) {
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(
                    6.0 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Shift right to avoid purple pixel"));
        }
        if (alliance == Alliance.Color.BLUE && spikePosition == Field.SpikePosition.Left) {
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(
                    6.0 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Shift left to avoid purple pixel"));
        }

        //drive up to the proper April Tag and also lower arm to deposit yellow pixel
        state.addPrimaryOperation(new DriveToAprilTag(20.0 * Field.MM_PER_INCH, desiredAprilTagId, "Drive to April Tag"));
        state.addSecondaryOperation(new ArmOperation(ArmOperation.Type.AutoDeposit, "Get arm to auto-deposit level"));
        states.add(state);

        state = new State("Get bucket on backdrop and drop yellow pixel");
        //run into backdrop
        state.addPrimaryOperation(new DriveToAprilTag(11.5 * Field.MM_PER_INCH, desiredAprilTagId, "Drive to April Tag"));
        //shift right an inch as our pixel is carried on left side of the intake
        state.addPrimaryOperation(new StrafeRightForDistanceOperation(Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Shift right a bit to center pixel"));
        //push yellow pixel out
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Expel, "Expel pixel"));
        state.addPrimaryOperation(new WaitOperation(1000, "wait a second to let pixel fall"));
        states.add(state);
        //navigate
        states.add(navigate(alliance, desiredAprilTagId));
    }
    public static State navigate(Alliance.Color alliance, int desiredAprilTagId) {
        State state = new State("Navigate");
        //move back a bit to clear backdrop
        state.addPrimaryOperation(new DriveForDistanceOperation(-4 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Back Away"));

        double distanceToStrafe = 20 * Field.MM_PER_INCH;
        if (alliance == Alliance.Color.RED) {
            distanceToStrafe += ((6-desiredAprilTagId) * 8 * Field.MM_PER_INCH);
            //strafe right (in case of red) to get near drivers' side wall to allow alliance partner access to the backdrop
            state.addPrimaryOperation(new StrafeRightForDistanceOperation(20 * Field.MM_PER_INCH, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        } else {
            distanceToStrafe += (-(1-desiredAprilTagId) * 8 * Field.MM_PER_INCH);
            //strafe left (in case of blue) to get near drivers' side wall to allow alliance partner access to the backdrop
            state.addPrimaryOperation(new StrafeLeftForDistanceOperation(distanceToStrafe, RobotConfig.CAUTIOUS_SPEED, "Navigate"));
        }
        //stop intaking
        state.addPrimaryOperation(new ArmOperation(ArmOperation.Type.Abstain, "Stop eating"));
        return state;
    }
}
