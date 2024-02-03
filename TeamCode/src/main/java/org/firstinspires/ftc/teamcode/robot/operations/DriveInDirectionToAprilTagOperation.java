package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.Locale;

public class DriveInDirectionToAprilTagOperation extends DriveInDirectionOperation {
    private final double distanceToAprilTag;

    private final int desiredAprilTag;

    public DriveInDirectionToAprilTagOperation(
            double direction,
            double maxDistance,
            double distanceToAprilTag,
            int desiredAprilTag,
            double speed,
            String title) {
        super(maxDistance, direction, speed, title);
        this.distanceToAprilTag = distanceToAprilTag;
        this.desiredAprilTag = desiredAprilTag;
    }

    public String toString() {
        return String.format(Locale.getDefault(),
                "DriveInDirectionToAprilTagOperation: Max Distance: %.2f\",Tag:%d, Distance to tag:%.2f, @%.2f --%s",
                this.distanceToAprilTag / Field.MM_PER_INCH,
                this.desiredAprilTag,
                super.distance / Field.MM_PER_INCH,
                this.speed,
                this.title);
    }

    public boolean isComplete() {
        if (super.isComplete()) {
            Match.log("Completed operation as max distance reached");
            return true;
        }
        AprilTagDetection detection;
        if ((detection = DriveToAprilTag.findTarget(desiredAprilTag)) != null) {
            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double rangeError = (detection.ftcPose.range - distanceToAprilTag / Field.MM_PER_INCH);

            /*
            We consider we have arrived if we are within .5 inches of the desired distance
             */
            if (Math.abs(rangeError) < .5) {
                driveTrain.stop();
                return true;
            }
        }
        return false;
    }

    public double getDistanceToTravel() {
        return this.distanceToAprilTag;
    }

    @Override
    public void startOperation() {
        super.startOperation();
    }
}
