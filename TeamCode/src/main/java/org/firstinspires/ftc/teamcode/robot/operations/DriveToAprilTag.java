package org.firstinspires.ftc.teamcode.robot.operations;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.RobotConfig;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;
import java.util.Locale;

/**
 * Drive in the direction specified in degrees, the amount specified in mms at the speed specified
 */
public class DriveToAprilTag extends Operation {

    public static final double SPEED_GAIN  =  0.08  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    public static final double STRAFE_GAIN =  0.04 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    public static final double TURN_GAIN   =  0.03  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    public static final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    protected double distance;
    protected int aprilTagId;
    protected DriveTrain driveTrain;

    AprilTagDetection desiredTag;
    /**
     * Create an operation to drive in the specified heading
     * @param distance - the distance to be away from the april tag
     * @param aprilTagId - the april tag id to align with
     * @param title
     */
    public DriveToAprilTag(double distance, int aprilTagId, String title) {
        this.distance = distance;
        this.aprilTagId = aprilTagId;
        this.driveTrain = Match.getInstance().getRobot().getDriveTrain();
        this.title = title;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "DriveToAprilTag: D:%.2f(%.2f\"),Tag:@%d --%s",
                this.distance, (this.distance / Field.MM_PER_INCH), this.aprilTagId,
                this.title);
    }

    public boolean isComplete() {
        return driveToAprilTag(aprilTagId, distance, driveTrain);
    }

    /**
     * Drive the robot so that it is aligned with a specified april tag id and is within the
     * specified distance from it
     * @param aprilTagId
     * @param distance - in mms
     * @param driveTrain
     * @return
     */
    public static boolean driveToAprilTag(int aprilTagId, double distance, DriveTrain driveTrain) {
        double drive = 0, turn = 0, strafe = 0;
        boolean arrived = false;
        AprilTagDetection desiredTag;
        if ((desiredTag = findTarget(aprilTagId)) != null) {
            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double rangeError = (desiredTag.ftcPose.range - distance / Field.MM_PER_INCH);
            double headingError = desiredTag.ftcPose.bearing;
            double yawError = desiredTag.ftcPose.yaw;

            double speed = RobotConfig.APRIL_TAG_SPEED;
            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive = Range.clip(rangeError * SPEED_GAIN, -speed, speed);
            turn = Range.clip(headingError * TURN_GAIN, -speed * .6, speed * .6);
            strafe = Range.clip(-yawError * STRAFE_GAIN, -speed, speed);

            /*
            We consider we have arrived if we are within 1 inch of the desired distance,
            within 1 inches of left to right and within 2 degrees of facing the aprilTag
             */
            if (Math.abs(rangeError) < 1 && Math.abs(yawError) < 1 && Math.abs(headingError) < 2) {
                arrived = true;
            }
            else {
                /*
                Match.log(String.format(Locale.getDefault(),
                        "Drive to april tag: Range eror:%.2f, yawError:%.2f, heading error: %.2f," +
                                "drive: %.2f, strafe: %.2f, turn: %.2f",
                        rangeError, yawError, headingError,
                        drive, strafe, turn));

                 */
            }
        }
        /*else {
            //if we are not seeing the tag, we say we have arrived as there is no chance we are going to see it
            arrived = true;
        }*/
        if (arrived) {
            driveTrain.stop();
        }
        else {
            moveRobot(drive, strafe, turn, driveTrain);
        }
        return arrived;

    }

    @Override
    public void startOperation() {
        Match.getInstance().getRobot().getLed().turnOnWhiteLED(true);
    }

    @Override
    public void abortOperation() {
        driveTrain.stop();
    }

    public double getDistance() {
        return this.distance;
    }

    /**
     * Return an april tag detection if it is seen. Return null if it is not
     * @param tagToFind
     * @return
     */
    public static AprilTagDetection findTarget(int tagToFind) {
        // Step through the list of detected tags and look for a matching tag
        List<AprilTagDetection> currentDetections = Match.getInstance().getRobot().getVisionPortal().getAprilTags();
        //Match.log("Found " + currentDetections.size() + " april tags");
        if (currentDetections.size() > 0) {
            for (AprilTagDetection aprilTag : currentDetections) {
                // Look to see if we have size info on this tag.
                if (aprilTag.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if (aprilTag.id == tagToFind) {
                        // Yes, we want to use this tag.
                        return aprilTag;  // don't look any further.
                    }
                }
            }
        }
        else {
            Match.log("No april tags to align with");
        }
        return null;
    }
    /**
     * Move robot according to desired axes motions
     * <p>
     * Positive X is forward
     * <p>
     * Positive Y is strafe left
     * <p>
     * Positive Yaw is counter-clockwise
     */
    public static void moveRobot(double forward, double strafe, double rotate, DriveTrain driveTrain) {
        // Calculate wheel powers.
        double leftFrontPower    =  forward -strafe -rotate;
        double rightFrontPower   =  forward +strafe +rotate;
        double leftBackPower     =  forward +strafe -rotate;
        double rightBackPower    =  forward -strafe +rotate;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send powers to the wheels.
        driveTrain.setLeftFrontPower(leftFrontPower);
        driveTrain.setRightFrontPower(rightFrontPower);
        driveTrain.setLeftRearPower(leftBackPower);
        driveTrain.setRightRearPower(rightBackPower);
    }
}

