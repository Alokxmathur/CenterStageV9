package org.firstinspires.ftc.teamcode.robot.operations;

import com.acmerobotics.roadrunner.drive.Drive;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Drive in the direction specified in degrees, the amount specified in mms at the speed specified
 */
public class DriveToAprilTag extends Operation {

    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    protected double distance;
    protected double speed;
    protected int aprilTagId;
    protected DriveTrain driveTrain;

    boolean targetFound;
    AprilTagDetection desiredTag;
    double drive, turn, strafe;

    /**
     * Create an operation to drive in the specified heading
     * @param distance - the distance to be away from the april tag
     * @param aprilTagId - the april tag id to align with
     * @param speed
     * @param title
     */
    public DriveToAprilTag(double distance, int aprilTagId,
                           double speed, String title) {
        this.distance = distance;
        this.speed = speed;
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
        findTarget();
        drive = turn = strafe = 0;
        if (targetFound) {
            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double rangeError = (desiredTag.ftcPose.range - distance / Field.MM_PER_INCH);
            double headingError = desiredTag.ftcPose.bearing;
            double yawError = desiredTag.ftcPose.yaw;

            // Use the speed and turn "gains" to calculate how we want the robot to move.
            drive = Range.clip(rangeError * SPEED_GAIN, -speed, speed);
            turn = Range.clip(headingError * TURN_GAIN, -speed * .6, speed * .6);
            strafe = Range.clip(-yawError * STRAFE_GAIN, -speed, speed);
            Match.log(this.title + ", distance away:" + rangeError);

            /*
            We consider we have arrived if we are within .5 inches of the desired distance,
            within .5 inches of left to right and within 1 degree of facing the aprilTag
             */
            if (Math.abs(rangeError) < .5 && Math.abs(yawError) < .5 && Math.abs(headingError) < 1) {
                driveTrain.stop();
                return true;
            }
        }
        moveRobot(drive, strafe, turn);
        return false;

    }

    @Override
    public void startOperation() {
    }

    @Override
    public void abortOperation() {
        driveTrain.stop();
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getDistance() {
        return this.distance;
    }

    private void findTarget() {
        targetFound = false;
        // Step through the list of detected tags and look for a matching tag
        List<AprilTagDetection> currentDetections = Match.getInstance().getRobot().getVisionPortal().getAprilTags();
        if (currentDetections.size() > 0) {
            for (AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if (detection.id == aprilTagId) {
                        // Yes, we want to use this tag.
                        targetFound = true;
                        desiredTag = detection;
                        Match.log("Found target, id=" + aprilTagId);
                        break;  // don't look any further.
                    }
                }
            }
        }
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
    public void moveRobot(double x, double y, double yaw) {
        // Calculate wheel powers.
        double leftFrontPower    =  x -y -yaw;
        double rightFrontPower   =  x +y +yaw;
        double leftBackPower     =  x +y -yaw;
        double rightBackPower    =  x -y +yaw;

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

