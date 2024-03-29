package org.firstinspires.ftc.teamcode.robot.operations;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.game.Field;
import org.firstinspires.ftc.teamcode.game.Match;
import org.firstinspires.ftc.teamcode.robot.components.drivetrain.DriveTrain;

import java.util.Date;
import java.util.Locale;

/**
 * Drive in the direction specified in degrees, the amount specified in mms at the speed specified
 */
public class DriveInDirectionOperation extends DriveForDistanceOperation {
    protected double direction;

    /**
     * Create an operation to drive in the specified heading
     * @param travelDistance - distance in mms
     * @param heading - the heading in radians
     * @param speed
     * @param title
     */
    public DriveInDirectionOperation(double travelDistance, double heading,
                                     double speed, String title) {
        super(travelDistance, speed, title);
        this.direction = heading;
    }

    public String toString() {
        return String.format(Locale.getDefault(), "DriveInDirection: %.2f(%.2f\")@%.2f --%s",
                this.distance, (this.distance / Field.MM_PER_INCH), Math.toDegrees(this.direction),
                this.title);
    }

    public boolean isComplete() {
        if (driveTrain.driveTrainWithinRange()) {
            return true;
        } else {
            driveInDirection(distance, direction, speed, driveTrain);
            return false;
        }
    }

    public static void driveInDirection(double distance, double direction, double speed, DriveTrain driveTrain) {
        double currentBearing = Math.toDegrees(driveTrain.getExternalHeading());

        // adjust relative speed based on heading error.
        double bearingError = AngleUnit.normalizeDegrees(Math.toDegrees(direction) - currentBearing);
        double steer = DriveTrain.getSteer(bearingError, DriveTrain.P_DRIVE_COEFFICIENT);

        double speedToUse = speed;
        double leftSpeed = speedToUse - steer;
        double rightSpeed = speedToUse + steer;

        // Normalize speeds if either one exceeds +/- 1.0;
        double max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
        if (max > 1.0) {
            leftSpeed /= max;
            rightSpeed /= max;
        }


        Match.log(String.format(Locale.getDefault(), "%.2f vs %.2f, Steer: %.2f, Bearing error: %.2f, Setting power LF:%.2f,LR:%.2f,RF:%.2f,RR%.2f",
                Math.toDegrees(direction), currentBearing, steer, bearingError, leftSpeed, leftSpeed, rightSpeed, rightSpeed));

        driveTrain.setLeftFrontPower(leftSpeed);
        driveTrain.setLeftRearPower(leftSpeed);
        driveTrain.setRightFrontPower(rightSpeed);
        driveTrain.setRightRearPower(rightSpeed);
    }

    public double getSpeed() {
        return this.speed;
    }
}

